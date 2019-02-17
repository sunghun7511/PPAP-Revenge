package kr.kshgroup.ppap_revenge.exceptions;

import java.util.ArrayList;
import java.util.Arrays;

public class PPAPValueException extends Exception {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPValueException(String message) {
		super(message);
		protectJava7();
	}
}
