package org.evosuite.add;

import org.evosuite.Properties;
import org.evosuite.ga.archive.Archive;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testcase.execution.ExecutionResult;

public class MthdRiskCovTestFitness extends TestFitnessFunction {

	private static final long serialVersionUID = -8547956190067458550L;

	protected final String className;
	protected final String methodName;
	protected final String riskMthdSig;

	public MthdRiskCovTestFitness(String riskClass, String riskMethod) {
		super();
		this.className = riskClass;
		this.methodName = riskMethod;
		riskMthdSig = riskClass + "." + riskMethod;
//		org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw goal:"+riskMthdSig);
//		org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw goal:"+riskMthdSig.equals("org.objectweb.asm.Label.<init>()V"));
	}

	@Override
	public double getFitness(TestChromosome individual, ExecutionResult result) {
		double fitness = 1.0;  
//		String s = "org.objectweb.asm.Label.<init>()V";
//		s=s.replace("org.evosuite.shaded.", "");
//		if(s.equals(riskMthdSig)) {
//			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw target goal:"+Util.getCoveredMthd(result).contains(riskMthdSig));
//			for(String mthd:Util.getCoveredMthd(result)) {
//				org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw target goal:"+mthd);
//			}
//		}
		if(CoveredUtil.getCoveredMthd(result).contains(riskMthdSig)) {
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
		if (other instanceof MthdRiskCovTestFitness) {
			MthdRiskCovTestFitness otherMethodFitness = (MthdRiskCovTestFitness) other;
			if (className.equals(otherMethodFitness.getClassName()))
				return methodName.compareTo(otherMethodFitness.getMethod());
			else
				return className.compareTo(otherMethodFitness.getClassName());
		}
		return compareClassName(other);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
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
		MthdRiskCovTestFitness other = (MthdRiskCovTestFitness) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (methodName == null) {
			if (other.methodName != null) {
				return false;
			}
		} else if (!methodName.equals(other.methodName)) {
			return false;
		}
		return true;
	}

	@Override
	public String getTargetClass() {
		return getClassName();
	}

	private String getClassName() {
		return this.className;
	}

	@Override
	public String getTargetMethod() {
		for (StackTraceElement ele : Thread.currentThread().getStackTrace()) {
			org.evosuite.utils.LoggingUtils.getEvoLogger().info("lzw trace:" + ele);
		}
		return getMethod();
	}

	private String getMethod() {
		return this.methodName;
	}

}
