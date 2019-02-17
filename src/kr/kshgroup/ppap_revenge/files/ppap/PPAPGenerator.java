package kr.kshgroup.ppap_revenge.files.ppap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.kshgroup.ppap_revenge.ProgramManager;
import kr.kshgroup.ppap_revenge.exceptions.PPAPIOException;
import kr.kshgroup.ppap_revenge.exceptions.PPAPValueException;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class PPAPGenerator extends PPAPRunnable {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPGenerator(PPAPFileManager manager) {
		super(manager);
		protectJava7();
	}

	private File mainSource;
	private final List<File> sources = new ArrayList<File>();
	private File target;

	private byte key = (byte) 0xbb;
	private String author = "";

	@Override
	public void initialize() {
	}

	@Override
	public void close() {
	}

	public void setMainSouce(File main) {
		if (main == null) {
			return;
		}
		this.mainSource = main;
	}

	public void setKey(byte key) {
		this.key = key;
	}

	public void addSource(File source) {
		if (source == null) {
			return;
		}
		this.sources.add(source);
	}

	public void setTarget(File target) {
		if (target == null) {
			return;
		}
		this.target = target;
	}

	public void setAuthor(String author) {
		if (author == null) {
			return;
		}
		this.author = author;
	}

	@Override
	public void run() {
		if (mainSource == null || !mainSource.exists() || mainSource.isDirectory()) {
			SneakyThrower.sneakyThrow(new PPAPValueException("Main file cannot be null"));
		}

		ByteArrayOutputStream bo = new ByteArrayOutputStream();

		try {
			PPAPFile file = new PPAPFile();

			// PPAP Meta
			PPAPMeta meta = new PPAPMeta();

			meta.ppapKey = this.key;
			meta.authorLen = author == null ? 0 : author.getBytes("utf-8").length;
			meta.author = author == null ? "" : author;
			meta.classCount = 1 + sources.size();

			file.meta = meta;

			// PPAP Main Class
			PPAPClass main = new PPAPClass();

			main.className = ProgramManager.removeExtension(mainSource.getName());
			main.classNameLength = main.className.getBytes("utf-8").length;
			main.data = manager.readFile(mainSource);
			main.length = main.data.length;

			file.mainClass = main;

			// PPAP Classes
			for (File cls : sources) {
				PPAPClass pc = new PPAPClass();

				pc.className = ProgramManager.removeExtension(cls.getName());
				pc.classNameLength = pc.className.getBytes("utf-8").length;
				pc.data = manager.readFile(cls);
				pc.length = pc.data.length;

				file.classes.add(pc);
			}

			// PPAP File Signature
			bo.write(PPAPFile.SIGNITURE);

			// PPAP Meta
			bo.write(PPAPMeta.SIGNITURE);
			bo.write(file.meta.ppapKey);
			bo.write(manager.int2barr(file.meta.authorLen));
			bo.write(file.meta.author.getBytes("utf-8"));
			bo.write(manager.int2barr(file.meta.classCount));

			// PPAP Main Class
			bo.write(manager.crypt(PPAPClass.SIGNITURE, file.meta.ppapKey));
			bo.write(manager.crypt(manager.int2barr(file.mainClass.classNameLength), file.meta.ppapKey));
			bo.write(manager.crypt(file.mainClass.className.getBytes("utf-8"), file.meta.ppapKey));
			bo.write(manager.crypt(manager.int2barr(file.mainClass.length), file.meta.ppapKey));
			bo.write(manager.crypt(file.mainClass.data, file.meta.ppapKey));

			// PPAP Classes
			for (PPAPClass cls : file.classes) {
				bo.write(manager.crypt(PPAPClass.SIGNITURE, file.meta.ppapKey));
				bo.write(manager.crypt(manager.int2barr(cls.classNameLength), file.meta.ppapKey));
				bo.write(manager.crypt(cls.className.getBytes("utf-8"), file.meta.ppapKey));
				bo.write(manager.crypt(manager.int2barr(cls.length), file.meta.ppapKey));
				bo.write(manager.crypt(cls.data, file.meta.ppapKey));
			}
		} catch (IOException e) {
			SneakyThrower.sneakyThrow(new PPAPIOException("Error on generate PPAP file.", e));
		}

		byte[] fileContent = bo.toByteArray();
		manager.writeFile(target, fileContent);
	}
}
