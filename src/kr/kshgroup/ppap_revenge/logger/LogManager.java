package kr.kshgroup.ppap_revenge.logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.ManagerBase;
import kr.kshgroup.ppap_revenge.ProgramManager;
import kr.kshgroup.ppap_revenge.exceptions.PPAPIOException;
import kr.kshgroup.ppap_revenge.logger.stream.FileLogStream;
import kr.kshgroup.ppap_revenge.options.ArgumentManager;
import kr.kshgroup.ppap_revenge.options.ProgramOption;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class LogManager implements ManagerBase {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public LogManager() {
		protectJava7();
	}

	@Override
	public void initialize() {
		Log root = Log.getRootLog();

		ProgramOption option = ProgramManager.getManager(ArgumentManager.class).getOption();

		if (option.logToFile) {
			String base_path = option.workingDirectory;
			String log_path = option.log_file;

			File output = new File(base_path, log_path);

			if (!output.exists()) {
				output.getParentFile().mkdirs();
				try {
					output.createNewFile();
				} catch (IOException e) {
					SneakyThrower.sneakyThrow(new PPAPIOException("Errror in create logger file", e));
				}
			}

			root.setStream(new FileLogStream(output));
		}

	}

	@Override
	public void close() {
	}
}
