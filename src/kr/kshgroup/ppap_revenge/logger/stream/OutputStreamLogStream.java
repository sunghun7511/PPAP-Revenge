package kr.kshgroup.ppap_revenge.logger.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kshgroup.ppap_revenge.exceptions.PPAPIOException;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class OutputStreamLogStream extends LogStream {
	public static void protectJava7_() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private OutputStream stream;

	public OutputStreamLogStream() {
		protectJava7_();

		this.stream = System.out;
	}

	public OutputStreamLogStream(OutputStream stream) {
		this.stream = stream;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void close() {
	}

	public void setStream(OutputStream stream) {
		if (stream != null) {
			this.stream = stream;
		}
	}

	@Override
	public void castLog(int level, String message) {
		if (level >= getLogLevel()) {
			try {
				if (!message.endsWith("\r\n"))
					if (message.endsWith("\n"))
						message = message.substring(0, message.length() - 1) + "\r\n";
					else
						message += "\r\n";
				stream.write(message.getBytes("utf-8"));
			} catch (IOException e) {
				SneakyThrower.sneakyThrow(new PPAPIOException("OutputStreamLogStream Error", e));
			}
		}
	}

}
