package kr.kshgroup.ppap_revenge;

import java.util.ArrayList;
import java.util.Arrays;

public class EntryMain {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public static void main(String[] args) {
		protectJava7();

		ProgramManager pr = new ProgramManager(args);

		pr.initialize();
		pr.run();
		pr.close();
	}
}
