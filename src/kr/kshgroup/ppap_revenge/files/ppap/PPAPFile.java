package kr.kshgroup.ppap_revenge.files.ppap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PPAPFile {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPFile() {
		protectJava7();
	}

	public static final byte[] SIGNITURE = new byte[] { (byte) 0xbb, (byte) 0xab };
	public PPAPMeta meta;

	public PPAPClass mainClass;
	public final List<PPAPClass> classes = new ArrayList<PPAPClass>();
}
