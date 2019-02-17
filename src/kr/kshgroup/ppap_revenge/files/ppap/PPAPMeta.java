package kr.kshgroup.ppap_revenge.files.ppap;

import java.util.ArrayList;
import java.util.Arrays;

public class PPAPMeta {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	public PPAPMeta() {
		protectJava7();
	}

	public static final byte[] SIGNITURE = new byte[] { (byte) 0xbb, (byte) 0xac };

	public byte ppapKey;
	public int authorLen;
	public String author;
	public int classCount;
}
