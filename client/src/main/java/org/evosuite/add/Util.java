package org.evosuite.add;

import java.util.HashSet;
import java.util.Set;

import org.evosuite.testcase.execution.ExecutionResult;

/**
 * neu.lab.plug.testcase.homemade.host.H1.<init>(Lneu/lab/plug/testcase/homemade/a/A1;)V
 * 1.V 2.<> 3.return value 4.; 5.Lneu 6.I int 7./
 * 
 * @author asus
 *
 */
public class Util {
	/**
	 * method is evosuite-pattern.
	 * 
	 * @param result
	 * @return
	 */
	public static Set<String> getCoveredMthd(ExecutionResult result) {
		Set<String> corveredMthd = new HashSet<String>();
		for (String mthd : result.getTrace().getCoveredMethods()) {
			corveredMthd.add(mthd);
		}
		for (Throwable t : result.getAllThrownExceptions()) {
			// org.evosuite.utils.LoggingUtils.getEvoLogger()
			// .info("lzw Throwable:" + t.getMessage() + " " + t.getClass().getName());
			if (t instanceof NoSuchMethodError) {
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw NoSuchMethod:" + t.getMessage());
				corveredMthd.add(t.getMessage());
			}
		}
		return corveredMthd;
	}

	/**
	 * transform evosuite-method
	 * 
	 * @param evoMthd
	 * @return neu.lab.plug.testcase.homemade.host.H1.<init>(Lneu/lab/plug/testcase/homemade/a/A1;)V
	 *         neu.lab.plug.testcase.homemade.host.H1.<init>(neu.lab.plug.testcase.homemade.a.A1)
	 */
	public static String evo2std(String evoMthd) {
		// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw transform:" +
		// evoMthd);
		// neu.lab.plug.testcase.homemade.host.H1.<init> +
		// Lneu/lab/plug/testcase/homemade/a/A1;)V
		String[] spilrStrs = evoMthd.split("\\(");
		String pre = spilrStrs[0];
		System.out.println(spilrStrs[1]);
		String argsStr = spilrStrs[1].split("\\)")[0];
		System.out.println(argsStr);
		String[] argArr = argsStr.split(";");
		String newArgs = "";
		for (String oldArg : argArr) {
			if (!oldArg.equals("")) {
				// Lneu/lab/plug/testcase/homemade/a/A1 ->neu.lab.plug.testcase.homemade.a.A
				String newArg = "";
				switch (oldArg) {
				case "Z":
					newArg = "boolean";
					break;
				case "C":
					newArg = "char";
					break;
				case "S":
					newArg = "short";
					break;
				case "I":
					newArg = "int";
					break;
				case "F":
					newArg = "float";
					break;
				case "J":
					newArg = "long";
					break;
				case "D":
					newArg = "double";
					break;
				default:
					if (oldArg.startsWith("L")) {
						newArg = oldArg.substring(1, oldArg.length()).replace("/", ".");
					} else {
						newArg = oldArg.replace("/", ".");
					}
					break;
				}
				newArgs += (newArg + ",");
			}

		}
		if (!newArgs.equals(""))
			newArgs = newArgs.substring(0, newArgs.length() - 1);// delete last ,
		return pre + "(" + newArgs + ")";
		// return pre+"("+
		// return ("<"+evoMthd.substring(0, evoMthd.length()-1)+">").
	}

	public static String soot2std(String sootMthd) {
		String[] cls_suffix = sootMthd.split(":");
		String cls = cls_suffix[0].substring(1);
		String[] name_args = cls_suffix[1].split("\\(");
		String name = name_args[0].split(" ")[2];
		String args = name_args[1].substring(0, name_args[1].length() - 2);
		return cls + "." + name + "(" + args + ")";
	}

	/**
	 * neu.lab.plug.testcase.homemade.host.H1.<init>(neu.lab.plug.testcase.homemade.a.A1)
	 * neu.lab.plug.testcase.homemade.host.H1.<init>(Lneu/lab/plug/testcase/homemade/a/A1;)V
	 * 
	 * @param stdMthd
	 * @return
	 */
	public static String std2evo(String stdMthd) {
		String[] pre_oldArgs = stdMthd.split("\\(");
		String pre = pre_oldArgs[0];
		String oldArgs = pre_oldArgs[1];
		System.out.println(oldArgs);
		String newArgs = "";
		if (!")".equals(oldArgs)) {
			for (String oldArg : oldArgs.split("\\)")[0].split(",")) {
				if (!oldArg.equals("")) {
					String newArg = "";
					switch (oldArg) {
					case "bollean":
						newArg = "Z";
						break;
					case "char":
						newArg = "C";
						break;
					case "short":
						newArg = "S";
						break;
					case "int":
						newArg = "I";
						break;
					case "float":
						newArg = "F";
						break;
					case "long":
						newArg = "J";
						break;
					case "double":
						newArg = "D";
						break;
					default:
						newArg = ("L" + oldArg).replace(".", "/");
						break;
					}
					newArgs += (newArg + ";");
				}
			}
		}

		return pre + "(" + newArgs + ")V";
	}

	public static void main(String[] args) {
		System.out.println(Util.evo2std("neu.lab.plug.testcase.homemade.host.H1.m2()V"));
		System.out.println(
				Util.evo2std("neu.lab.plug.testcase.homemade.host.H1.<init>(Lneu/lab/plug/testcase/homemade/a/A1;)V"));
		System.out.println(Util.soot2std("<neu.lab.testcase.top.Top: void m2(int)>")); //

		System.out.println(Util.soot2std(
				"<neu.lab.plug.testcase.homemade.host.H1: void <init>(neu.lab.plug.testcase.homemade.a.A1)>"));

		System.out.println(
				Util.std2evo("neu.lab.plug.testcase.homemade.host.H1.<init>(neu.lab.plug.testcase.homemade.a.A1)"));
		System.out.println(
				Util.std2evo("neu.lab.plug.testcase.homemade.host.H1.<init>(neu.lab.plug.testcase.homemade.a.A1)"));
		System.out.println(Util.std2evo("neu.lab.testcase.bottom.B.m2()"));
	}

}
