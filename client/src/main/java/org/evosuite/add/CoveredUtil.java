package org.evosuite.add;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.utils.LoggingUtils;

public class CoveredUtil {

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
			//			 org.evosuite.utils.LoggingUtils.getEvoLogger()
			//			 .info("lzw Throwable:" + t.getMessage() + " " + t.getClass().getName());
			if (t instanceof NoSuchMethodError || t instanceof NoSuchMethodException) {
				DebugUtil.limitInfo("lzw NoSuchMethod:" + t.getMessage());
				corveredMthd.add(t.getMessage());
			}
//			if (t instanceof java.lang.NullPointerException) {
//				t.printStackTrace(DebugUtil.getPrinter("D:\\cWs\\notepad++\\nullpoint.txt", false));
//				//				 org.evosuite.utils.LoggingUtils.getEvoLogger().error("nullPoint",t);
//			}
		}
		String bottomCls = MthdFormatUtil.sootMthd2cls(Properties.RISK_METHOD);
		if (CoveredUtil.getExClses(result).contains(bottomCls)) {
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("******lzw :get covered riskMethod from class.");
			corveredMthd.add(MthdFormatUtil.soot2evo(Properties.RISK_METHOD));
		}
		//		for(String mthd:corveredMthd) {
		//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw coveredMthd:"+mthd);
		//		}
		return corveredMthd;
	}

	private static Set<String> getExClses(ExecutionResult result) {
		Set<String> exClses = new HashSet<String>();
		for (Throwable t : result.getAllThrownExceptions()) {
			// org.evosuite.utils.LoggingUtils.getEvoLogger()
			// .info("lzw Throwable:" + t.getMessage() + " " + t.getClass().getName());
			if (t instanceof ClassNotFoundException || t instanceof NoClassDefFoundError) {
				String coveredCls = CoveredUtil.extraExpCls(t.getMessage());
				//				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw no class exception:" + t.toString());
				//				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw no this class:" + "|" + coveredCls + "|");
				exClses.add(coveredCls);
			}
		}
		return exClses;
	}

	public static Set<String> getCoveredCls(ExecutionResult result) {
		// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw expsize:" +
		// result.getAllThrownExceptions().size());
		// if (result.getAllThrownExceptions().size() == 1) {
		// org.evosuite.utils.LoggingUtils.getEvoLogger()
		// .info("lzw expsize:" +
		// result.getAllThrownExceptions().iterator().next().getClass().getName());
		// }
		// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw
		// expsize:"+result.getAllThrownExceptions().);
		Set<String> corveredClses = new HashSet<String>();
		for (String mthd : result.getTrace().getCoveredMethods()) {
			corveredClses.add(MthdFormatUtil.evoMthd2cls(mthd));
		}
		for (Throwable t : result.getAllThrownExceptions()) {
			// org.evosuite.utils.LoggingUtils.getEvoLogger()
			// .info("lzw Throwable:" + t.getMessage() + " " + t.getClass().getName());
			if (t instanceof ClassNotFoundException || t instanceof NoClassDefFoundError) {
				String coveredCls = CoveredUtil.extraExpCls(t.getMessage());
				try {
					java.io.File file = new java.io.File("D:\\ws_testcase\\image\\trace.txt");
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					java.io.PrintWriter p = new java.io.PrintWriter(new java.io.FileWriter(file, true));
					p.println("============================");
					// while (t != null) {
					t.printStackTrace(p);
					// p.println("toString:" + t.toString());
					// p.println("message:" + t.getMessage());
					// t = t.getCause();
					// }
					p.close();
				} catch (Exception e) {
					org.evosuite.utils.LoggingUtils.getEvoLogger().error("trace error:", e);
				}
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw exception:" + t.toString());
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw no this class:" + "|" + coveredCls + "|");
				corveredClses.add(coveredCls);
			}
		}
		return corveredClses;
	}

	private static String extraExpCls(String expMessage) {
		String expCls;
		if (expMessage.startsWith("Could not initialize class")) {
			expCls = expMessage.substring(27);
		} else if (expMessage.startsWith("Class ") && expMessage.endsWith(" not found")) {
			expCls = expMessage.substring(6, expMessage.length() - 10);
		} else if (expMessage.startsWith("Class '")
				&& expMessage.endsWith("' should be in target project, but could not be found!")) {
			expCls = expMessage.substring(7, expMessage.length() - 54);
		} else {
			expCls = expMessage;
		}
		expCls = expCls.replace("/", ".");
		// System.out.println("expCls:"+expCls+"|");
		return expCls;
	}

}
