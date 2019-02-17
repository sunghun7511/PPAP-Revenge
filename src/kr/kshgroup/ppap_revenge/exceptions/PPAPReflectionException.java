package kr.kshgroup.ppap_revenge.exceptions;

import java.util.ArrayList;
import java.util.Arrays;

public class PPAPReflectionException extends Throwable {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPReflectionException(String message, Exception e) {
		super(message, e);
		protectJava7();
	}
}
