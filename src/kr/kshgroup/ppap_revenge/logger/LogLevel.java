package kr.kshgroup.ppap_revenge.logger;

import java.util.ArrayList;
import java.util.Arrays;

public class LogLevel {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public LogLevel() {
		protectJava7();
	}

	public final static int OFF = Integer.MIN_VALUE;
	public final static int DEBUG = 400;
	public final static int INFO = 500;
	public final static int WARNING = 600;
	public final static int ERROR = 700;
	public final static int ALL = Integer.MAX_VALUE;
}
