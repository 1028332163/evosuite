package org.evosuite.add;

import java.util.ArrayList;
import java.util.List;

import org.evosuite.Properties;
import org.evosuite.testsuite.AbstractFitnessFactory;

public class MthdProbCovFactory extends AbstractFitnessFactory<MthdProbRiskCovTestFitness>{

	@Override
	public List<MthdProbRiskCovTestFitness> getCoverageGoals() {
		List<MthdProbRiskCovTestFitness> goals = new ArrayList<MthdProbRiskCovTestFitness>();
		org.evosuite.utils.LoggingUtils.getEvoLogger().info("risk method:"+Properties.RISK_METHOD);
		String mthd = Util.soot2std(Properties.RISK_METHOD);
		String evoMthd = Util.std2evo(mthd);
		int index = evoMthd.lastIndexOf(".");
		String className = evoMthd.substring(0,index);
		String methodName = evoMthd.substring(index+1);
		goals.add(new MthdProbRiskCovTestFitness(className,methodName));
		return goals;
	}

}
