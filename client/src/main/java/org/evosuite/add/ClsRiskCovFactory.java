package org.evosuite.add;

import java.util.ArrayList;
import java.util.List;

import org.evosuite.testsuite.AbstractFitnessFactory;

public class ClsRiskCovFactory extends AbstractFitnessFactory<ClsRiskCovTestFitness> {

	@Override
	public List<ClsRiskCovTestFitness> getCoverageGoals() {
		List<ClsRiskCovTestFitness> goals = new ArrayList<ClsRiskCovTestFitness>();
		// goals.add(new RiskCoverageTestFitness("neu.lab.testcase.bottom.B","m2()V"));
		for (String cls : GlobalVar.i().getClsDistance().getRiskTargets()) {

			goals.add(new ClsRiskCovTestFitness(cls));
		}
		return goals;
	}

}
