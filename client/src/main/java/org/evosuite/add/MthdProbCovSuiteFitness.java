package org.evosuite.add;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.testcase.ExecutableChromosome;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.testsuite.AbstractTestSuiteChromosome;
import org.evosuite.testsuite.TestSuiteFitnessFunction;

public class MthdProbCovSuiteFitness extends TestSuiteFitnessFunction {

	private static final long serialVersionUID = 8142494194204951582L;

	@Override
	public double getFitness(AbstractTestSuiteChromosome<? extends ExecutableChromosome> individual) {
		Double fitness = Double.MAX_VALUE;
		List<ExecutionResult> results = runTestSuite(individual);
		Set<String> reachedMethods = new HashSet<String>();
		Set<String> reachedExClses = new HashSet<String>();

		for (ExecutionResult result : results) {
			// if (result.hasTimeout() || result.hasTestException()) {
			// continue;
			// }
			// for (String mthd : result.getTrace().getCoveredMethods()) {
			//// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" +
			// mthd);
			// reachedMethods.add(mthd);
			// }
			reachedMethods.addAll(CoveredUtil.getCoveredMthd(result));
			reachedExClses.addAll(CoveredUtil.getExClses(result));
		}

		for (String reachedMthd : reachedMethods) {
			// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" +
			// reachedMthd);
			Double dis = GlobalVar.i().getNodeProbDistance().getProbFitness(Util.evo2std(reachedMthd));

			if (dis == 0) {
				DebugUtil.infoZero("===lzw zero fitness:" + reachedMthd);
			}
			if (dis < fitness) {
				fitness = dis;
			}
		}
		String bottomCls = Util.evoMthd2cls(Properties.RISK_METHOD);
		if (reachedExClses.contains(bottomCls))
			fitness = 0.0;
		updateIndividual(this, individual, fitness);
		// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw fitness:" +
		// fitness);
		return fitness;
	}

}
