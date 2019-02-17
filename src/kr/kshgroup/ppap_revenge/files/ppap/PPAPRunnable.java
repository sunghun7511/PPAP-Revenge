package kr.kshgroup.ppap_revenge.files.ppap;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class PPAPRunnable {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	protected final PPAPFileManager manager;

	public PPAPRunnable(PPAPFileManager manager) {
		protectJava7();

		this.manager = manager;
	}

	public abstract void initialize();

	public abstract void close();

	public abstract void run();
}
