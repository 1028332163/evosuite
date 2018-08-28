package org.evosuite.add;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.evosuite.Properties;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testsuite.TestSuiteChromosome;

public class DebugUtil {
	private static final String reachedResult = "D:\\ws_testcase\\image\\reachedMethods.txt";
	public static int smallCnt = 0;
	public static int zeroCnt = 0;

	public static Map<String, Integer> content2cnt = new HashMap<String, Integer>();
    private static Map<String,PrintWriter> path2printer = new HashMap<String,PrintWriter>();
	public static int contentT = 2;

	public static void limitInfo(String infomation) {
		Integer cnt = content2cnt.get(infomation);
		if (cnt == null) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
			content2cnt.put(infomation, 1);
		} else {
			if (cnt < contentT) {
				org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
				content2cnt.put(infomation, cnt + 1);
			}
		}
	}

	public static void infoSmall(String infomation) {
		if (smallCnt < 3) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
			smallCnt++;
		}
	}

	public static void infoZero(String infomation) {
		if (zeroCnt < 3) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
			zeroCnt++;
		}
	}

	public static void printFinalCover(TestSuiteChromosome bestIndividual) {
//		org.evosuite.utils.LoggingUtils.getEvoLogger()
//				.info("bestIndividual type:" + bestIndividual.getClass().getName());
//		Set<String> reachedMethods = new TreeSet<String>();
//		for (TestChromosome testChromosome : bestIndividual.getTestChromosomes()) {
//			reachedMethods.addAll(CoveredUtil.getCoveredMthd(testChromosome.getLastExecutionResult()));
//		}
////		org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw final-coverdMethod:");
//
//		try {
//			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(reachedResult)));
//			for (String reachedMethod : reachedMethods) {
////				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw final-coverdMethod:" + reachedMethod);
//				printer.println(reachedMethod);
//			}
//			printer.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
	}

	/**
	 * callled when target class initial error
	 */
	public static void printFinalCover() {
//		try {
//			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(reachedResult)));
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw final-coverdMethod:");
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info(Properties.RISK_METHOD);
//			printer.println(MthdFormatUtil.soot2evo(Properties.RISK_METHOD));
//			printer.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
	}

	public static PrintWriter getPrinter(String filePath, boolean append) {
		try {
			if(path2printer.get(filePath)==null) {
				path2printer.put(filePath, new PrintWriter(new BufferedWriter(new FileWriter(filePath, append))));
			}
			return path2printer.get(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
// org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer: int hashCode()