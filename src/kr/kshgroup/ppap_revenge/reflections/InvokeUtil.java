package kr.kshgroup.ppap_revenge.reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.exceptions.PPAPReflectionException;

public class InvokeUtil {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public InvokeUtil() {
		protectJava7();
	}

	private byte[] main = { (byte) 0x6d, (byte) 0x61, (byte) 0x69, (byte) 0x6e };

	public void invokeMain(Class<?> cls, String[] args) {
		try {
			Method m = cls.getDeclaredMethod(new String(main), new Class[] { String[].class });
			m.setAccessible(true);
			m.invoke(null, (Object) args);
		} catch (Exception e) {
			SneakyThrower.sneakyThrow(new PPAPReflectionException("Error on invoke " + cls.getName(), e));
		}
	}
}
