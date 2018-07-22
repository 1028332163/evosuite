package org.evosuite.add;

import java.util.ArrayList;
import java.util.List;

import org.evosuite.testsuite.AbstractFitnessFactory;

public class MthdRiskCovFactory extends AbstractFitnessFactory<MthdRiskCovTestFitness> {

	@Override
	public List<MthdRiskCovTestFitness> getCoverageGoals() {
		List<MthdRiskCovTestFitness> goals = new ArrayList<MthdRiskCovTestFitness>();
//		goals.add(new RiskCoverageTestFitness("neu.lab.testcase.bottom.B","m2()V"));
		for(String mthd:GlobalVar.i().getMthdDistance().getRiskTargets()) {
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw oriMthd:"+mthd);
			String evoMthd = MthdFormatUtil.std2evo(mthd);

			int index = evoMthd.lastIndexOf(".");
			String className = evoMthd.substring(0,index);
			String methodName = evoMthd.substring(index+1);
			
			goals.add(new MthdRiskCovTestFitness(className,methodName));
		}
		return goals;
	}


}
