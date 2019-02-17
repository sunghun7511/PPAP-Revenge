package kr.kshgroup.ppap_revenge.options;

import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.ManagerBase;

public class ArgumentManager implements ManagerBase {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private final String[] args;
	private final ProgramOption option = new ProgramOption();

	public ArgumentManager(String[] args) {
		protectJava7();

		this.args = args;
	}

	@Override
	public void initialize() {
		ArgumentParser parser = new ArgumentParser();

		parser.addArgument("help", "print help", false, "h");
		parser.addArgument("execute", "execute ppap file", false, "e");
		parser.addArgument("generate", "generate ppap file", false, "g");
		parser.addArgument("debug", "set mode to debug", false, "d");
		parser.addArgument("work-dir", "set working directory", true, "w");
		parser.addArgument("log-file", "set file for logging", true, "l");
		parser.addArgument("target", "set target file", true, "t");
		parser.addArgument("source", "set source file", true, "s");
		parser.addArgument("main-file", "set main file name (if source is directory)", true, "m");
		parser.addArgument("author", "set author", true, "a");

		parser.parse(args);

		if (parser.hasArgumentValue("help")) {
		} else if (parser.hasArgumentValue("execute")) {
			option.runType = RunType.EXECUTE;
		} else if (parser.hasArgumentValue("generate")) {
			option.runType = RunType.GENERATE;
		}
		if (parser.hasArgumentValue("debug")) {
			option.debug = true;
		}
		if (parser.hasArgumentValue("work-dir")) {
			option.workingDirectory = parser.getArgumentValue("work-dir");
		}
		if (parser.hasArgumentValue("log-file")) {
			option.logToFile = true;
			option.log_file = parser.getArgumentValue("log-file");
		}
		if (parser.hasArgumentValue("target")) {
			option.targetFile = parser.getArgumentValue("target");
		}
		if (parser.hasArgumentValue("main-file")) {
			option.mainFile = parser.getArgumentValue("main-file");
		}
		if (parser.hasArgumentValue("source")) {
			option.sourceFile = parser.getArgumentValue("source");
		}
		if (parser.hasArgumentValue("author")) {
			option.author = parser.getArgumentValue("author");
		}

		if (option.runType == RunType.PRINT_HELP) {
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			String arg0 = stack[stack.length - 1].getClassName();

			parser.printHelp(arg0);
		}
	}

	@Override
	public void close() {

	}

	public ProgramOption getOption() {
		return option;
	}
}
