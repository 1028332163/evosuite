package org.evosuite.add;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.evosuite.testcase.ExecutableChromosome;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.testsuite.AbstractTestSuiteChromosome;
import org.evosuite.testsuite.TestSuiteFitnessFunction;

public class ClsRiskCovSuiteFitness extends TestSuiteFitnessFunction{

	
	private static final long serialVersionUID = -4305873657002732560L;

	@Override
	public double getFitness(AbstractTestSuiteChromosome<? extends ExecutableChromosome> individual) {
		Double fitness = Double.MAX_VALUE;
		List<ExecutionResult> results = runTestSuite(individual);
		Set<String> reachedClses = new HashSet<String>();

		for (ExecutionResult result : results) {
			// if (result.hasTimeout() || result.hasTestException()) {
			// continue;
			// }
			// for (String mthd : result.getTrace().getCoveredMethods()) {
			//// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" +
			// mthd);
			// reachedMethods.add(mthd);
			// }
			reachedClses.addAll(Util.getCoveredCls(result));
		}

		for (String reachedCls : reachedClses) {
//			 org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" +
//			 reachedMthd);
			Double dis = GlobalVar.i().getClsDistance().getDistance(reachedCls);
//			if (dis == 1) {
//				DebugUtil.infoSmall("===lzw small fitness:" + reachedCls);
//			}
			if (dis == 0) {
				DebugUtil.infoZero("===lzw zero fitness:" + reachedCls);
			}
			if (dis < fitness) {
				fitness = dis;
			}
		}
		updateIndividual(this, individual, fitness);
		return fitness;
	}

}
