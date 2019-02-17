package kr.kshgroup.ppap_revenge.files.ppap;

import java.util.ArrayList;
import java.util.Arrays;

public class PPAPClass {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPClass() {
		protectJava7();
	}

	public static final byte[] SIGNITURE = new byte[] { (byte) 0xbb, (byte) 0xad };

	public int classNameLength;
	public String className;
	public int length;
	public byte data[];
}
