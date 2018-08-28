package org.evosuite.add;

import java.util.HashMap;
import java.util.Map;

import org.evosuite.Properties;

public class NodeProbDistance {

	private Map<String, Double> top2dis;// <top-method,distance>
	private Map<String, Double> top2branch;// <top-method,branchNum>
	private Map<String, Double> top2fitness;

	public NodeProbDistance(Map<String, Double> top2probInverse) {
		this.top2fitness = top2probInverse;
	}

	public NodeProbDistance() {
		top2dis = new HashMap<String, Double>();
		top2branch = new HashMap<String, Double>();
		top2fitness = new HashMap<String, Double>();
	}

	public void addMetric(String top, Double distance, Double branchNum) {
		top2dis.put(top, distance);
		top2branch.put(top, branchNum);

		top2fitness.put(top, calFitness(distance, branchNum));
		//		org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw test:"+top+" "+ calFitness(distance,prob));
	}

	private double calFitness(Double distance, Double branchNum) {
		//distance is main factor
		//		double stdDist;
		//		if(distance.equals(Double.MAX_VALUE)) {
		//			stdDist = 1;
		//		}else {
		//			stdDist = distance/(distance+1);
		//		}
		//		return 10*branchNum + stdDist;
		//distance is main factor
		double stdBranchNum;
		if (branchNum.equals(Double.MAX_VALUE)) {
			stdBranchNum = 1;
		} else {
			stdBranchNum = branchNum / (branchNum + 1);
		}
		return 10 * distance + stdBranchNum;
	}

	/**
	 * @param topClass stdFormat
	 * @return
	 */
	public Double getProbFitness(String topClass) {
		String stdRiskClass = MthdFormatUtil.soot2std(Properties.RISK_METHOD);
		if (stdRiskClass.equals(topClass)) {
			return 0.0;
		}
		Double fitness = top2fitness.get(topClass);
		if (fitness == null) {
			return Double.MAX_VALUE;
		}
		return fitness;
		// Double probInverse = top2fitness.get(topClass);
		// if (probInverse != null) {
		// return probInverse - 1;
		// }
		// return Double.MAX_VALUE - 1;
	}

}
