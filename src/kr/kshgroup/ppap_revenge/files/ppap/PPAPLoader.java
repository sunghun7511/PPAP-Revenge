package kr.kshgroup.ppap_revenge.files.ppap;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.exceptions.PPAPValueException;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class PPAPLoader extends PPAPRunnable {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPLoader(PPAPFileManager manager) {
		super(manager);
		protectJava7();
	}

	private File source;
	private PPAPFile loaded;

	@Override
	public void initialize() {
	}

	@Override
	public void close() {
	}

	public void setSource(File source) {
		if (source == null) {
			return;
		}
		this.source = source;
	}

	public PPAPFile getLoaded() {
		return loaded;
	}

	@Override
	public void run() {
		try {
			this.loaded = loadFromBytes(manager.readFile(source));
		} catch (PPAPValueException e) {
			SneakyThrower.sneakyThrow(e);
		}
	}

	private static boolean startsWith(byte[] data, byte[] check, int start) {
		if (data == null || check == null) {
			return false;
		}
		if (check.length > data.length - start) {
			return false;
		}
		for (int i = start; i < check.length; i++) {
			if (data[i] != check[i]) {
				return false;
			}
		}
		return true;
	}

	private static byte[] slice(byte[] base, int start, int length) {
		return Arrays.copyOfRange(base, start, start + length);
	}

	private PPAPFile loadFromBytes(byte[] data) throws PPAPValueException {
		int seek = 0;

		// PPAP File
		if (!startsWith(data, PPAPFile.SIGNITURE, seek)) {
			throw new PPAPValueException("Invalid PPAPFile signature");
		}
		seek += PPAPFile.SIGNITURE.length;

		PPAPFile file = new PPAPFile();

		// PPAP Meta
		if (!startsWith(data, PPAPMeta.SIGNITURE, seek)) {
			throw new PPAPValueException("Invalid PPAPMeta signature");
		}
		seek += PPAPMeta.SIGNITURE.length;
		PPAPMeta meta = new PPAPMeta();

		meta.ppapKey = data[seek];
		seek++;

		meta.authorLen = manager.barr2int(slice(data, seek, 4));
		seek += 4;

		try {
			meta.author = new String(slice(data, seek, meta.authorLen), "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		seek += meta.authorLen;

		meta.classCount = manager.barr2int(slice(data, seek, 4));
		seek += 4;

		file.meta = meta;

		// PPAP Main Class
		if (!startsWith(data, PPAPClass.SIGNITURE, seek)) {
			throw new PPAPValueException("Invalid PPAPClass signature (Main Class)");
		}
		seek += PPAPClass.SIGNITURE.length;

		PPAPClass mainClass = new PPAPClass();

		mainClass.classNameLength = manager.barr2int(manager.crypt(slice(data, seek, 4), meta.ppapKey));
		seek += 4;

		try {
			mainClass.className = new String(manager.crypt(slice(data, seek, mainClass.classNameLength), meta.ppapKey),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		seek += mainClass.classNameLength;

		mainClass.length = manager.barr2int(manager.crypt(slice(data, seek, 4), meta.ppapKey));
		seek += 4;

		mainClass.data = manager.crypt(slice(data, seek, mainClass.length), meta.ppapKey);
		seek += mainClass.length;

		file.mainClass = mainClass;

		// PPAP Classes

		for (int i = 0; i < meta.classCount - 1; i++) {
			if (!startsWith(data, PPAPClass.SIGNITURE, seek)) {
				throw new PPAPValueException("Invalid PPAPClass signature");
			}
			seek += PPAPClass.SIGNITURE.length;

			PPAPClass cls = new PPAPClass();

			cls.classNameLength = manager.barr2int(manager.crypt(slice(data, seek, 4), meta.ppapKey));
			seek += 4;

			try {
				cls.className = new String(manager.crypt(slice(data, seek, cls.classNameLength), meta.ppapKey),
						"utf-8");
			} catch (UnsupportedEncodingException e) {
			}
			seek += cls.classNameLength;

			cls.length = manager.barr2int(manager.crypt(slice(data, seek, 4), meta.ppapKey));
			seek += 4;

			cls.data = manager.crypt(slice(data, seek, cls.length), meta.ppapKey);
			seek += cls.length;

			file.classes.add(cls);
		}

		return file;
	}
}
