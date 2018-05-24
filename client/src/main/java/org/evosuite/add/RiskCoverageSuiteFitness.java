package org.evosuite.add;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.evosuite.testcase.ExecutableChromosome;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.testsuite.AbstractTestSuiteChromosome;
import org.evosuite.testsuite.TestSuiteFitnessFunction;

public class RiskCoverageSuiteFitness extends TestSuiteFitnessFunction {

	private static final long serialVersionUID = -7721713060612435201L;

	@Override
	public double getFitness(AbstractTestSuiteChromosome<? extends ExecutableChromosome> individual) {
		Double fitness = Double.MAX_VALUE;
		List<ExecutionResult> results = runTestSuite(individual);
		Set<String> reachedMethods = new HashSet<String>();

		for (ExecutionResult result : results) {
			// if (result.hasTimeout() || result.hasTestException()) {
			// continue;
			// }
			// for (String mthd : result.getTrace().getCoveredMethods()) {
			//// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" +
			// mthd);
			// reachedMethods.add(mthd);
			// }
			reachedMethods.addAll(Util.getCoveredMthd(result));
		}

		for (String reachedMthd : reachedMethods) {
//			 org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" +
//			 reachedMthd);
			Double dis = MethodDistance.i().getDistance(Util.evo2std(reachedMthd));
			if (dis == 0) {
				org.evosuite.utils.LoggingUtils.getEvoLogger().info("===lzw zero fitness:" + reachedMthd);
//				for(String reachedMthd1 : reachedMethods) {
//					org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw covered method:" + reachedMthd1);
//				}
			}
			if (dis < fitness) {
				fitness = dis;
			}
		}
		// org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw fitness:" +
		// fitness);
		updateIndividual(this, individual, fitness);
		return fitness;
	}

}
