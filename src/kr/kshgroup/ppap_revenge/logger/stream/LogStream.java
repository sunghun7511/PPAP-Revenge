package kr.kshgroup.ppap_revenge.logger.stream;

import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.logger.LogLevel;

public abstract class LogStream {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	protected int logLevel;

	public LogStream() {
		this(LogLevel.INFO);
	}

	public LogStream(int logLevel) {
		protectJava7();

		this.logLevel = logLevel;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public abstract void initialize();

	public abstract void close();

	public abstract void castLog(int level, String message);
}
