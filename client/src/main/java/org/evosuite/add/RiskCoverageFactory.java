package org.evosuite.add;

import java.util.ArrayList;
import java.util.List;

import org.evosuite.testsuite.AbstractFitnessFactory;

public class RiskCoverageFactory extends AbstractFitnessFactory<RiskCoverageTestFitness> {

	@Override
	public List<RiskCoverageTestFitness> getCoverageGoals() {
		List<RiskCoverageTestFitness> goals = new ArrayList<RiskCoverageTestFitness>();
//		goals.add(new RiskCoverageTestFitness("neu.lab.testcase.bottom.B","m2()V"));
		for(String mthd:MethodDistance.i().getRiskMthds()) {
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw oriMthd:"+mthd);
			String evoMthd = Util.std2evo(mthd);

			int index = evoMthd.lastIndexOf(".");
			String className = evoMthd.substring(0,index);
			String methodName = evoMthd.substring(index+1);
			
			String sig = className + "." + methodName;
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw evoMthd:"+sig);
//
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw evoMthd:"+sig.startsWith("org.objectweb.a"));
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw evoMthd:"+sig.startsWith("org.objectweb.as"));
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw evoMthd:"+sig.startsWith("org.objectweb.asm"));
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw evoMthd:"+sig.equals("org.objectweb.asm.Label.<init>()V"));
			goals.add(new RiskCoverageTestFitness(className,methodName));
		}
		return goals;
	}


}
