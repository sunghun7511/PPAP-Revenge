package kr.kshgroup.ppap_revenge.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.kshgroup.ppap_revenge.exceptions.PPAPValueException;
import kr.kshgroup.ppap_revenge.logger.Log;
import kr.kshgroup.ppap_revenge.logger.LogLevel;
import kr.kshgroup.ppap_revenge.reflections.SneakyThrower;

public class ArgumentParser {
	public static void protectJava7() {
		new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11)).stream().filter(num -> num % 2 == 0);
	}

	private Map<String, Argument> arguments = new HashMap<String, Argument>();

	public ArgumentParser() {
		protectJava7();
	}

	public boolean addArgument(String name, String... alias) {
		return addArgument(name, null, false, alias);
	}

	public boolean addArgument(String name, String description, String... alias) {
		return addArgument(name, description, false, alias);
	}

	public boolean addArgument(String name, String description, boolean needSubArg, String... alias) {
		if (name == null || name.isEmpty()) {
			return false;
		}
		if (arguments.containsKey(name)) {
			return false;
		}
		if (alias != null) {
			for (int i = 0; i < alias.length; i++) {
				for (Map.Entry<String, Argument> ent : arguments.entrySet()) {
					if (alias[i].equals(ent.getKey()) || ent.getValue().alias.contains(alias[i])) {
						return false;
					}
				}
			}
		}

		Argument a = new Argument();

		a.description = description;
		a.needSubArg = needSubArg;
		a.alias.addAll(Arrays.asList(alias));

		arguments.put(name, a);
		return true;
	}

	public boolean parse(String[] args) {
		if (args == null) {
			return false;
		}
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (!arg.startsWith("--")) {
				SneakyThrower.sneakyThrow(new PPAPValueException("Invalid argument '" + arg + "'"));
			}
			String key = arg.substring(2);

			for (Map.Entry<String, Argument> ent : arguments.entrySet()) {
				if (ent.getKey().equals(key) || ent.getValue().alias.contains(key)) {
					Argument a = ent.getValue();

					a.hasValue = true;

					if (a.needSubArg) {
						i++;
						if (i >= args.length) {
							SneakyThrower.sneakyThrow(new PPAPValueException("Need more argument '" + key + "'"));
						}

						a.value = args[i];
					}
				}
			}
		}
		return true;
	}

	public boolean hasArgumentValue(String name) {
		for (Map.Entry<String, Argument> ent : arguments.entrySet()) {
			if (ent.getKey().equals(name) || ent.getValue().alias.contains(name)) {
				return ent.getValue().hasValue;
			}
		}
		return false;
	}

	public String getArgumentValue(String name) {
		for (Map.Entry<String, Argument> ent : arguments.entrySet()) {
			if (ent.getKey().equals(name) || ent.getValue().alias.contains(name)) {
				return ent.getValue().value;
			}
		}
		return null;
	}

	public void printHelp(String arg0) {
		printHelp(arg0, Log.getRootLog(), LogLevel.INFO);
	}

	public void printHelp(String arg0, Log log, int level) {
		int overflow_len = 60;
		String usage0 = "";
		List<String> usages = new ArrayList<String>();
		List<String> options = new ArrayList<String>();

		for (Map.Entry<String, Argument> ent : arguments.entrySet()) {
			String name = ent.getKey();
			Argument arg = ent.getValue();

			String usage = "[--" + name + (arg.needSubArg ? " " + name.toUpperCase() : "") + "]";

			if ((usage0 + " " + usage).length() + 30 > overflow_len) {
				if (usages.size() == 0) {
					usages.add("");
				}
				String last_usage = usages.get(usages.size() - 1);
				if ((last_usage + " " + usage).length() > overflow_len) {
					last_usage = "";
					usages.add(last_usage);
				}
				last_usage += " " + usage;
				usages.set(usages.size() - 1, last_usage);
			} else {
				usage0 += " " + usage;
			}

			options.add(String.format("%-30s", "--" + name + (arg.needSubArg ? " " + name.toUpperCase() : "")) + "\t"
					+ arg.description);
			for (int i = 0; i < arg.alias.size(); i++) {
				options.add("  --" + arg.alias.get(i));
			}
		}

		log.log(level, "");
		log.log(level, "PPAP Revenge");
		log.log(level, "  made by KSHGroup(kshgroup@kshgroup.kr)");
		log.log(level, "");
		log.log(level, "  Usage : " + arg0 + usage0);
		for (int i = 0; i < usages.size(); i++)
			log.log(level, "    " + usages.get(i));
		log.log(level, "");
		log.log(level, "  Options");
		for (int i = 0; i < options.size(); i++)
			log.log(level, "    " + options.get(i));
		log.log(level, "");
	}

	private static class Argument {
		private String description;
		private boolean needSubArg;

		private final List<String> alias = new ArrayList<String>();

		private boolean hasValue = false;
		private String value;
	}
}
