package kr.kshgroup.ppap_revenge.exceptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PPAPIOException extends Throwable {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPIOException(String message, IOException e) {
		super(message, e);
		protectJava7();
	}
}
