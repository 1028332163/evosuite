package org.evosuite.add;

import java.util.HashMap;
import java.util.Map;

public class NodeProbDistance {

	private Map<String, Double> top2dis;// <top-method,distance>
	private Map<String, Double> top2prob;// <top-method,probability>
	private Map<String, Double> top2fitness;

	public NodeProbDistance(Map<String, Double> top2probInverse) {
		this.top2fitness = top2probInverse;
	}

	public NodeProbDistance() {
		top2dis = new HashMap<String, Double>();
		top2prob = new HashMap<String, Double>();
		top2fitness = new HashMap<String, Double>();
	}

	public void addMetric(String top,Double distance,Double prob) {
		top2dis.put(top, distance);
		top2prob.put(top, prob);
		
		top2fitness.put(top, calFitness(distance,prob));
//		org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw test:"+top+" "+ calFitness(distance,prob));
	}
	
	private double calFitness(Double distance,Double prob) {
		double stdDist;
		if(distance.equals(Double.MAX_VALUE)) {
			stdDist = 1;
		}else {
			stdDist = distance/(distance+1);
		}
		return 10*prob + stdDist;
	}

	public Double getProbFitness(String topClass) {
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
