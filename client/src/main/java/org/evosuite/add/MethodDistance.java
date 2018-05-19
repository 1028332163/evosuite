package org.evosuite.add;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.evosuite.Properties;

public class MethodDistance {

	private Map<String, Map<String, Double>> distances;// <bottom-method,<top-method,distance>>

	private MethodDistance(Map<String, Map<String, Double>> distances) {
		this.distances = distances;
	}

	private static MethodDistance instance;

	public static MethodDistance i() {
		if (instance == null) {
			instance = loadDistances();
		}
		return instance;
	}

	public Set<String> getRiskMthds() {
		return distances.keySet();
	}

	private static MethodDistance loadDistances() {
		Map<String, Map<String, Double>> distances = new HashMap<String, Map<String, Double>>();
		try {
			BufferedReader reader;
			if(null==Properties.DISTANCE_FILE) {
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("load distance from: D:\\cWS\\notepad++\\MethodDistance.txt");
				reader= new BufferedReader(new FileReader("D:\\cWS\\notepad++\\MethodDistance.txt"));
			}else {
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("load distance from: "+Properties.DISTANCE_FILE);
				reader= new BufferedReader(new FileReader(Properties.DISTANCE_FILE));
			}
			String line = reader.readLine();
			while (line != null) {
				if (!"".equals(line)) {
//					System.out.println(line);
					String[] mmd = line.split(">,");// method-method-distance
					String bottom = Util.soot2std(mmd[0]+">");
					String top = Util.soot2std(mmd[1]+">");
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
		return new MethodDistance(distances);
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
		for (Map<String, Double> top2dis : this.distances.values()) {
			Double dis = top2dis.get(top);
			if (dis != null) {
				if (dis < minimum)
					minimum = dis;
			}
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
	 System.out.println(MethodDistance.i().toString());
	 }
}
