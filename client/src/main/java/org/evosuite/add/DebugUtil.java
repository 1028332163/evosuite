package org.evosuite.add;

import java.util.HashMap;
import java.util.Map;

public class DebugUtil {
	public static int smallCnt = 0;
	public static int zeroCnt = 0;
	
	public static Map<String, Integer> content2cnt = new HashMap<String, Integer>();
	public static int contentT = 2;

	public static void limitInfo(String infomation) {
		Integer cnt = content2cnt.get(infomation);
		if(cnt==null) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
			content2cnt.put(infomation, 1);
		}else {
			if(cnt<contentT) {
				org.evosuite.utils.LoggingUtils.getEvoLogger().info(infomation);
				content2cnt.put(infomation, cnt+1);
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
}
// org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer: int hashCode()