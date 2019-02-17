package kr.kshgroup.ppap_revenge.options;

import java.util.ArrayList;
import java.util.Arrays;

public class ProgramOption {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}
	
	public ProgramOption() {
		protectJava7();
	}

	public RunType runType = RunType.PRINT_HELP;

	public boolean debug = false;

	public String workingDirectory = "./";

	public boolean logToFile = false;
	public String log_file = "log/ppap.log";

	public String targetFile;
	public String sourceFile;

	public String mainFile;

	public String author;
}
