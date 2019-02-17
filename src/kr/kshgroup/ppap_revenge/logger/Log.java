package kr.kshgroup.ppap_revenge.logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import kr.kshgroup.ppap_revenge.logger.stream.LogStream;
import kr.kshgroup.ppap_revenge.logger.stream.OutputStreamLogStream;

public final class Log {
	public void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private static Log root = new Log("");
	private static HashMap<String, Log> logs = new HashMap<String, Log>();

	static {
		logs.put("", root);
	}

	public static Log getRootLog() {
		return root;
	}

	public static Log getLog(String name) {
		if (name == null) {
			return root;
		}
		if (!logs.containsKey(name)) {
			logs.put(name, new Log(name));
		}
		return logs.get(name);
	}

	public static void d(String message) {
		getRootLog().log(LogLevel.DEBUG, message);
	}

	public static void i(String message) {
		getRootLog().log(LogLevel.INFO, message);
	}

	public static void w(String message) {
		getRootLog().log(LogLevel.WARNING, message);
	}

	public static void e(String message) {
		getRootLog().log(LogLevel.ERROR, message);
	}

	private final String name;
	private LogStream stream = new OutputStreamLogStream();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String format = "[{time}][{name}][{thread}] {message}";

	private Log(String name) {
		protectJava7();

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStream(LogStream stream) {
		if (stream != null) {
			this.stream = stream;
		}
	}

	public void setDateFormat(String format) {
		if (format != null) {
			this.dateFormat = new SimpleDateFormat(format);
		}
	}

	public void setFormat(String format) {
		if (format != null) {
			this.format = format;
		}
	}

	public void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	public void info(String message) {
		log(LogLevel.INFO, message);
	}

	public void warning(String message) {
		log(LogLevel.WARNING, message);
	}

	public void error(String message) {
		log(LogLevel.ERROR, message);
	}

	public void log(int level, String message) {
		String log_str = format;
		Date time = new Date();

		log_str = log_str.replace("{time}", dateFormat.format(time));
		log_str = log_str.replace("{name}", name.equals("") ? "MAIN" : name);
		log_str = log_str.replace("{thread}", Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
		log_str = log_str.replace("{message}", message);

		stream.castLog(level, log_str);
	}
}
