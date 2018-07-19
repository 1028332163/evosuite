package org.evosuite.add;

import org.evosuite.Properties;
import org.evosuite.ga.archive.Archive;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testcase.execution.ExecutionResult;

public class ClsRiskCovTestFitness extends TestFitnessFunction {

	private static final long serialVersionUID = -6220917157929294742L;

	protected final String className;

	public ClsRiskCovTestFitness(String riskClass) {
		super();
		this.className = riskClass;
	}

	@Override
	public double getFitness(TestChromosome individual, ExecutionResult result) {
		double fitness = 1.0;
		if (CoveredUtil.getCoveredCls(result).contains(className)) {
			fitness = 0.0;
		}
		updateIndividual(this, individual, fitness);

		if (fitness == 0.0) {
			individual.getTestCase().addCoveredGoal(this);
		}
		if (Properties.TEST_ARCHIVE) {
			Archive.getArchiveInstance().updateArchive(this, individual, fitness);
		}
		return fitness;
	}

	@Override
	public int compareTo(TestFitnessFunction other) {
		if (other instanceof ClsRiskCovTestFitness) {
			ClsRiskCovTestFitness otherClsFitness = (ClsRiskCovTestFitness) other;
			return className.compareTo(otherClsFitness.className);
		}
		return compareClassName(other);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClsRiskCovTestFitness other = (ClsRiskCovTestFitness) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		return true;
	}

	@Override
	public String getTargetClass() {
		return className;
	}

	@Override
	public String getTargetMethod() {
		// TODO Auto-generated method stub
		return null;
	}

}
