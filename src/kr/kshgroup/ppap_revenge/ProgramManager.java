package kr.kshgroup.ppap_revenge;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.kshgroup.ppap_revenge.exceptions.PPAPValueException;
import kr.kshgroup.ppap_revenge.files.BaseFileManager;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPFile;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPFileManager;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPGenerator;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPLoader;
import kr.kshgroup.ppap_revenge.logger.LogManager;
import kr.kshgroup.ppap_revenge.options.ArgumentManager;
import kr.kshgroup.ppap_revenge.options.ProgramOption;
import kr.kshgroup.ppap_revenge.options.RunType;
import kr.kshgroup.ppap_revenge.reflections.ClassManager;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class ProgramManager implements ManagerBase {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private final String[] args;

	private static final List<ManagerBase> managers = new ArrayList<ManagerBase>();

	public ProgramManager(String[] args) {
		protectJava7();
		this.args = args;
	}

	@Override
	public void initialize() {
		managers.add(this);
		managers.add(new ArgumentManager(args));
		managers.add(new BaseFileManager());
		managers.add(new LogManager());
		managers.add(new ClassManager());
		managers.add(new PPAPFileManager());

		for (ManagerBase m : managers) {
			if (!(m instanceof ProgramManager)) {
				m.initialize();
			}
		}
	}

	@Override
	public void close() {
		for (ManagerBase m : managers) {
			if (!(m instanceof ProgramManager)) {
				m.close();
			}
		}
	}

	public static String removeExtension(String n) {
		int ind;
		if ((ind = n.lastIndexOf('.')) != -1)
			return n.substring(0, ind);
		return n;
	}

	public void run() {
		ArgumentManager arg = getManager(ArgumentManager.class);
		ProgramOption option = arg.getOption();

		if (option.runType == RunType.EXECUTE) {
			if (option.sourceFile == null) {
				SneakyThrower.sneakyThrow(new PPAPValueException("Cannot found source file"));
			}
			PPAPFileManager pm = getManager(PPAPFileManager.class);
			PPAPLoader ld = pm.getLoader();

			File source = new File(option.sourceFile);
			if (!source.exists() || !source.isFile()) {
				SneakyThrower.sneakyThrow(new PPAPValueException("Cannot found source file"));
			}

			ld.setSource(source);
			ld.run();

			PPAPFile ppap = ld.getLoaded();

			ClassManager cm = getManager(ClassManager.class);
			cm.setFile(ppap);

			cm.run();
		} else if (option.runType == RunType.GENERATE) {
			if (option.sourceFile == null) {
				SneakyThrower.sneakyThrow(new PPAPValueException("Cannot found source file"));
			}
			PPAPFileManager pm = getManager(PPAPFileManager.class);
			PPAPGenerator gn = pm.getGenerator();

			gn.setAuthor(option.author);

			File source = new File(option.sourceFile);

			if (source.isFile()) {
				gn.setMainSouce(source);
			} else {
				if (option.mainFile == null) {
					SneakyThrower.sneakyThrow(new PPAPValueException("Cannot found main file"));
				}
				File main = new File(source, option.mainFile);
				for (File f : source.listFiles()) {
					if (f.getAbsolutePath().equalsIgnoreCase(main.getAbsolutePath()))
						continue;
					gn.addSource(f);
				}
				gn.setMainSouce(main);
			}

			String targetName = option.targetFile;

			if (targetName == null) {
				targetName = removeExtension(source.getName()) + ".ppap";
			}

			File target = new File(targetName);

			if (target.isDirectory()) {
				target = new File(target, removeExtension(source.getName()) + ".ppap");
			}

			gn.setTarget(target);

			gn.run();
		}
	}

	public static <T extends ManagerBase> T getManager(Class<T> type) {
		for (ManagerBase m : managers) {
			if (m.getClass().equals(type)) {
				return (T) m;
			}
		}
		return null;
	}
}
