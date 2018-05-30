package org.evosuite.add;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.evosuite.Properties;

public class GlobalVar {
	private static GlobalVar instance;

	private GlobalVar() {

	}

	public static GlobalVar i() {
		if (instance == null) {
			instance = new GlobalVar();
		}
		return instance;
	}

	private NodeDistance mthdDistance;
	private NodeDistance clsDistance;

	public NodeDistance getMthdDistance() {
		if(mthdDistance==null) {
			mthdDistance = loadMthdDistance();
		}
		return mthdDistance;
	}

	public NodeDistance getClsDistance() {
		if(clsDistance==null) {
			clsDistance = loadClsDistance();
		}
		return clsDistance;
	}

	private NodeDistance loadMthdDistance() {
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
					String bottom = Util.soot2std(mmd[0] + ">");
					String top = Util.soot2std(mmd[1] + ">");
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
		return new NodeDistance(distances);
	}

	private NodeDistance loadClsDistance() {
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
		return new NodeDistance(distances);
	}
}
