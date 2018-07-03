package org.evosuite.add;

import java.util.Map;
import java.util.Set;

public class NodeDistances {

	private Map<String, Map<String, Double>> distances;// <bottom-method,<top-method,distance>>

	public NodeDistances(Map<String, Map<String, Double>> distances) {
		this.distances = distances;
	}

	public Set<String> getRiskTargets() {
		return distances.keySet();
	}

	public Double getDistance(String bottom, String top) {
		Double d = this.distances.get(bottom).get(top);
		if (null != d) {
			return d;
		}
		return Double.MAX_VALUE;
	}

	/**
	 * @param top
	 * @return minimum distance in all distances that is from each bottom to top.
	 */
	public Double getDistance(String top) {

		Double minimum = Double.MAX_VALUE;
//		String cls = "org.apache.http.conn.util.PublicSuffixListParser";
//		cls = cls.replace("org.evosuite.shaded.", "");
//		if (cls.equals(top)) {
//			minimum = 0.0;
//		}
//		String cls = "com.amazonaws.AmazonWebServiceRequest";
//		cls = cls.replace("org.evosuite.shaded.", "");
//		if (cls.equals(top)) {
//			minimum = 0.0;
//		}
		String minBottom = "";
		for (String bottom : this.distances.keySet()) {
			Map<String, Double> top2dis = this.distances.get(bottom);
			Double dis = top2dis.get(top);
			if (dis != null) {
				if (dis < minimum) {
					minimum = dis;
					minBottom = bottom;
				}
			}
		}

		if (minimum == 1) {
			DebugUtil.infoSmall("===lzw small fitness:" + top + "->" + minBottom);
		}
		return minimum;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String source : distances.keySet()) {
			Map<String, Double> dises = distances.get(source);
			for (String target : dises.keySet()) {
				sb.append(source + "," + target + "," + dises.get(target));
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(GlobalVar.i().getMthdDistance().toString());
		System.out.println(Util.evo2std("neu.lab.testcase.top.ClassTop.m1(Ljava/lang/String;I)V"));
		Double dis = GlobalVar.i().getMthdDistance()
				.getDistance(Util.evo2std("neu.lab.testcase.top.ClassTop.m1(Ljava/lang/String;I)V"));
		System.out.println(dis);
	}

}
