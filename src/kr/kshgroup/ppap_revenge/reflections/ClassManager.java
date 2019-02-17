package kr.kshgroup.ppap_revenge.reflections;

import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.ManagerBase;
import kr.kshgroup.ppap_revenge.exceptions.PPAPValueException;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPClass;
import kr.kshgroup.ppap_revenge.files.ppap.PPAPFile;

public class ClassManager implements ManagerBase {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public ClassManager() {
		protectJava7();
	}

	private InvokeUtil invoker;
	private PPAPFile file;

	public void setFile(PPAPFile file) {
		if (file != null) {
			this.file = file;
		}
	}

	@Override
	public void initialize() {
		invoker = new InvokeUtil();
	}

	@Override
	public void close() {
	}

	public void run() {
		if (file == null) {
			SneakyThrower.sneakyThrow(new PPAPValueException("Cannot found ppap file"));
		}
		CustomClassLoader loader = new CustomClassLoader();
		Class<?> main = loader.loadClass(file.mainClass.className, file.mainClass.data);

		for (PPAPClass cls : file.classes) {
			loader.loadClass(cls.className, cls.data);
		}

		invoker.invokeMain(main, new String[] {});
	}

	public class CustomClassLoader extends ClassLoader {
		public CustomClassLoader() {
		}

		public Class<?> loadClass(String className, byte[] arr) {
			return defineClass(className, arr, 0, arr.length);
		}
	}
}
