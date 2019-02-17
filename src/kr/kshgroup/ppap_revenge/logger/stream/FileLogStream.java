package kr.kshgroup.ppap_revenge.logger.stream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.exceptions.PPAPIOException;
import kr.kshgroup.ppap_revenge.exceptions.PPAPValueException;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class FileLogStream extends LogStream {
	public void protectJava7_() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private File output;

	public FileLogStream() {
		protectJava7_();
	}

	public FileLogStream(File file) {
		this.output = file;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void close() {
	}

	public void setFile(File file) {
		this.output = file;
	}

	@Override
	public void castLog(int level, String message) {
		if (level >= getLogLevel()) {
			if (output == null) {
				SneakyThrower.sneakyThrow(new PPAPValueException("FileLogStream Error - log output file is null"));
			}
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(output, true))) {
				bw.write(message);
			} catch (IOException e) {
				SneakyThrower.sneakyThrow(new PPAPIOException("FileLogStream Error", e));
			}
		}
	}
}
