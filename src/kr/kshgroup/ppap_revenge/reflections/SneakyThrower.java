package kr.kshgroup.ppap_revenge.reflections;

import java.util.ArrayList;
import java.util.Arrays;

public class SneakyThrower {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public SneakyThrower() {
		protectJava7();
	}

	public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
		throw (E) e;
	}
}
