package kr.kshgroup.ppap_revenge.files.ppap;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.exceptions.PPAPIOException;
import kr.kshgroup.ppap_revenge.files.FileManagerBase;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class PPAPFileManager implements FileManagerBase<byte[]> {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private PPAPRunnable loader;
	private PPAPRunnable generator;

	public PPAPFileManager() {
		protectJava7();

		loader = new PPAPLoader(this);
		generator = new PPAPGenerator(this);
	}

	@Override
	public void initialize() {
		loader.initialize();
		generator.initialize();
	}

	@Override
	public void close() {
		loader.close();
		generator.close();
	}

	public PPAPLoader getLoader() {
		return (PPAPLoader) loader;
	}

	public PPAPGenerator getGenerator() {
		return (PPAPGenerator) generator;
	}

	@Override
	public byte[] readFile(String filename) {
		return readFile(new File(filename));
	}

	@Override
	public void writeFile(String filename, byte[] data) {
		writeFile(new File(filename), data);
	}

	@Override
	public void appendFile(String filename, byte[] data) {
		appendFile(new File(filename), data);
	}

	@Override
	public byte[] readFile(File file) {
		byte[] data = null;
		try {
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			SneakyThrower.sneakyThrow(new PPAPIOException("Error on append file", e));
		}
		return data;
	}

	@Override
	public void writeFile(File file, byte[] data) {
		try {
			if (!file.exists()) {
				file.getAbsoluteFile().getParentFile().mkdirs();
				file.createNewFile();
			}
			Files.write(file.toPath(), data);
		} catch (IOException e) {
			SneakyThrower.sneakyThrow(new PPAPIOException("Error on append file", e));
		}
	}

	@Override
	public void appendFile(File file, byte[] data) {
		try {
			if (!file.exists()) {
				file.getAbsoluteFile().getParentFile().mkdirs();
				file.createNewFile();
			}
			Files.write(file.toPath(), data, StandardOpenOption.APPEND);
		} catch (IOException e) {
			SneakyThrower.sneakyThrow(new PPAPIOException("Error on append file", e));
		}
	}

	public byte[] int2barr(int i) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array();
	}

	public int barr2int(byte[] arr) {
		return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	public byte[] crypt(byte[] base, byte key) {
		if (key == 0) {
			return base;
		}
		for (int i = 0; i < base.length; i++) {
			base[i] ^= key;
		}
		return base;
	}
}
