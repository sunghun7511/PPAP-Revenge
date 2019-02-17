package kr.kshgroup.ppap_revenge.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.kshgroup.ppap_revenge.ManagerBase;
import kr.kshgroup.ppap_revenge.ProgramManager;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPFileManager;
import kr.kshgroup.ppap_revenge.options.ArgumentManager;
import kr.kshgroup.ppap_revenge.options.ProgramOption;

public class BaseFileManager implements ManagerBase {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public BaseFileManager() {
		protectJava7();
	}

	private static final List<FileManagerBase<?>> fileManagers = new ArrayList<FileManagerBase<?>>();

	@Override
	public void initialize() {
		ProgramOption option = ProgramManager.getManager(ArgumentManager.class).getOption();

		File base_path = new File(option.workingDirectory);

		if (!base_path.exists()) {
			base_path.mkdirs();
		}

		fileManagers.add(new PPAPFileManager());

		for (FileManagerBase<?> m : fileManagers) {
			m.initialize();
		}
	}

	@Override
	public void close() {
		for (FileManagerBase<?> m : fileManagers) {
			m.close();
		}
	}

}
