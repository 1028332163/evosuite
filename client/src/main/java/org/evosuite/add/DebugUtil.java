package org.evosuite.add;

public class DebugUtil {
	public static int smallCnt = 0;
	public static int zeroCnt = 0;

	public static void infoSmall(String infomation) {
		if(smallCnt<3) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
			smallCnt++;
		}
	}
	public static void infoZero(String infomation) {
		if(zeroCnt<3) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
			zeroCnt++;
		}
	}
}
//org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer: int hashCode()