package org.evosuite.add;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.evosuite.Properties;

public class GlobalVar {
	private static GlobalVar instance;
	private NodeDistances mthdDistance;
	private NodeDistances clsDistance;
	private NodeProbDistance mthdProbDist;
	

	private GlobalVar() {

	}

	public static GlobalVar i() {
		if (instance == null) {
			instance = new GlobalVar();
		}
		return instance;
	}

	

	public NodeDistances getMthdDistance() {
		if (mthdDistance == null) {
			mthdDistance = loadMthdDistance();
		}
		return mthdDistance;
	}

	public NodeDistances getClsDistance() {
		if (clsDistance == null) {
			clsDistance = loadClsDistance();
		}
		return clsDistance;
	}

	public NodeProbDistance getNodeProbDistance() {
		if (mthdProbDist == null) {
			mthdProbDist = loadMthdProbDistance();
		}
		return mthdProbDist;
	}

	private NodeProbDistance loadMthdProbDistance() {
		NodeProbDistance distances = new NodeProbDistance();
		try {
			org.evosuite.utils.LoggingUtils.getEvoLogger()
					.info("load method distance from: " + Properties.MTHD_PROB_DISTANCE_FILE);
			BufferedReader reader = new BufferedReader(new FileReader(Properties.MTHD_PROB_DISTANCE_FILE));
			String riskMthd = MthdFormatUtil.soot2std(Properties.RISK_METHOD);
			String line = reader.readLine();
			while (line != null) {
				if (!"".equals(line)) {
					String[] mmdhp = line.split(">,");// method-method-distance-isFromHost-probability
					String bottom = MthdFormatUtil.soot2std(mmdhp[0] + ">");
					if (riskMthd.equals(bottom)) {
						String top = MthdFormatUtil.soot2std(mmdhp[1] + ">");
						Double distance = Double.valueOf(mmdhp[2].split(",")[0]);
						Double branchNum = Double.valueOf(mmdhp[2].split(",")[2]);
						distances.addMetric(top, distance, branchNum);
//						Double distance = 
//						Double probInverse;
//						if (prob == 0) {
//							probInverse = Double.MAX_VALUE;
//						} else {
//							probInverse = 1.0 / prob;
//						}
//						top2probInverse.put(top, probInverse);
					}
				}
				line = reader.readLine();
			}
//			top2probInverse.put(riskMthd, 1.0);
//			top2fitness.put(riskMthd, 0.0);
			reader.close();
		} catch (Exception e) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().error("load distance file error", e);
		}

		return distances;
	}
	
	

	private NodeDistances loadMthdDistance() {
		Map<String, Map<String, Double>> distances = new HashMap<String, Map<String, Double>>();
		try {
			BufferedReader reader;
			if (null == Properties.MTHD_DISTANCE_FILE) {
				String distanceFile = "D:\\ws\\distance_mthd\\neu.lab+testcase.top+1.0.txt";
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("load method distance from: " + distanceFile);
				reader = new BufferedReader(new FileReader(distanceFile));
			} else {
				org.evosuite.utils.LoggingUtils.getEvoLogger()
						.info("load method distance from: " + Properties.MTHD_DISTANCE_FILE);
				reader = new BufferedReader(new FileReader(Properties.MTHD_DISTANCE_FILE));
			}
			String line = reader.readLine();
			while (line != null) {
				if (!"".equals(line)) {
					// System.out.println(line);
					String[] mmd = line.split(">,");// method-method-distance
					String bottom = MthdFormatUtil.soot2std(mmd[0] + ">");
					String top = MthdFormatUtil.soot2std(mmd[1] + ">");
					Double distance = Double.valueOf(mmd[2].split(",")[0]);
					Map<String, Double> m2d = distances.get(bottom);
					if (null == m2d) {
						m2d = new HashMap<String, Double>();
						distances.put(bottom, m2d);
					}
					m2d.put(top, distance);

				}
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().error("load distance file error", e);
		}
		return new NodeDistances(distances);
	}

	private NodeDistances loadClsDistance() {
		Map<String, Map<String, Double>> distances = new HashMap<String, Map<String, Double>>();
		try {
			BufferedReader reader;
			if (null == Properties.CLS_DISTANCE_FILE) {
				String filePath = "D:\\ws\\distance_cls\\neu.lab+testcase.top+1.0.txt";
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("load class distance from: " + filePath);
				reader = new BufferedReader(new FileReader(filePath));
			} else {
				org.evosuite.utils.LoggingUtils.getEvoLogger()
						.info("load class distance from: " + Properties.CLS_DISTANCE_FILE);
				reader = new BufferedReader(new FileReader(Properties.CLS_DISTANCE_FILE));
			}
			String line = reader.readLine();
			while (line != null) {
				if (!"".equals(line)) {
					// System.out.println(line);
					String[] mmd = line.split(",");// method-method-distance
					String bottom = mmd[0];
					String top = mmd[1];
					Double distance = Double.valueOf(mmd[2]);
					Map<String, Double> m2d = distances.get(bottom);
					if (null == m2d) {
						m2d = new HashMap<String, Double>();
						distances.put(bottom, m2d);
					}
					m2d.put(top, distance);

				}
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().error("load distance file error", e);
		}
		return new NodeDistances(distances);
	}
}
