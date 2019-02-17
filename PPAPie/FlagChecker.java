
public class FlagChecker {
	private final String input;
	private byte[] flag_check = { (byte) 0x6c, (byte) 0x67, (byte) 0x65, (byte) 0x69, (byte) 0x74, (byte) 0x5f,
			(byte) 0x74, (byte) 0x74, (byte) 0x74, (byte) 0x74, (byte) 0x74, (byte) 0x74, (byte) 0x17, (byte) 0x4a,
			(byte) 0x7b, (byte) 0x74, (byte) 0x15, (byte) 0x4a, (byte) 0x17, (byte) 0x7b, (byte) 0x10, (byte) 0x54,
			(byte) 0x54, (byte) 0x48, (byte) 0x17, (byte) 0x7b, (byte) 0x45, (byte) 0x54, (byte) 0x54, (byte) 0x15,
			(byte) 0x41, (byte) 0x7b, (byte) 0x54, (byte) 0x15, (byte) 0x41, (byte) 0x7b, (byte) 0x7a, (byte) 0x4b,
			(byte) 0x7a, (byte) 0x7b, (byte) 0x64, (byte) 0x5, (byte) 0x64, (byte) 0x5, (byte) 0x59 };

	public FlagChecker(String input) {
		this.input = input;
	}

	public boolean flag_check(byte[] input, byte[] flag) {
		if (input.length != flag.length) {
			return false;
		}
		for (int i = 0; i < flag.length; i++) {
			if ((input[i] ^ (byte) 0x24) != flag[i])
				return false;
		}
		return true;
	}

	public void run() {
		byte[] inputBytes = input.getBytes();

		if (flag_check(inputBytes, flag_check)) {
			System.out.println("CORRECT!!");
		} else {
			System.out.println("No...");
		}
	}
}
