package org.evosuite.add.shell;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.TraceClassVisitor;

public class AsmEn {

	public static void main(String[] args) throws Exception {
//		modifyMthd();
		classTrace();
	}

	private static void modifyMthd() throws Exception {
		ClassReader cr = new ClassReader(new FileInputStream(
				"D:\\ws\\github_snapshot\\truth-release_0_41\\extensions\\liteproto\\target\\classes\\com\\google\\common\\truth\\extensions\\proto\\LiteProtoSubject.class"));

		ClassNode cn = new ClassNode();

		cr.accept(cn, 0);
		System.out.println(cn.name);
		for (MethodNode mn : cn.methods) {
//			System.out.println(cn.name.replace("/", ".") + "." + mn.name + mn.desc);
			// debug(mn);
			System.out.println("====");
			// all path
			Paths paths = getAllExePath(mn);
			Map<LabelNode, Integer> label2num = getLabel2num(mn);
			System.out.println(paths.getPathsStr(label2num));
			// remaining path
			List<LabelNode> callLabels = getCallLabels(mn, "java.lang.Object.toString()Ljava/lang/String;");
			Path remianPath = paths.getRemainPath(callLabels);
			// filter node
			deleteBranch(mn, remianPath);
		}
		// //
		ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		byte[] b = cw.toByteArray();
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("D:\\cTestWs\\asmtest\\App.class"));
		out.write(b);
		out.close();
	}

	private static void deleteBranch(MethodNode mn, Path remianPath) {
		ListIterator<AbstractInsnNode> ite = mn.instructions.iterator();
		LabelNode currentLabel = null;
		while (ite.hasNext()) {
			AbstractInsnNode insNode = ite.next();
			if (insNode instanceof LabelNode) {
				currentLabel = (LabelNode) insNode;
			}
			if (!remianPath.contains(currentLabel) || insNode instanceof JumpInsnNode) {
				ite.remove();
			}
		}
	}

	/**
	 * LabelNodes whose statements call evoMthd.
	 * 
	 * @param mn
	 * @param evoMthd
	 * @return
	 * @throws Exception
	 */
	private static List<LabelNode> getCallLabels(MethodNode mn, String evoMthd) throws Exception {
		List<LabelNode> callLabels = new ArrayList<LabelNode>();
		ListIterator<AbstractInsnNode> ite = mn.instructions.iterator();
		LabelNode currentLabel = null;
		while (ite.hasNext()) {
			AbstractInsnNode insNode = ite.next();
			if (insNode instanceof LabelNode) {
				currentLabel = (LabelNode) insNode;
			}
			if (insNode instanceof MethodInsnNode) {
				MethodInsnNode mthdIns = (MethodInsnNode) insNode;
				String calledMthd = mthdIns.owner.replace("/", ".") + "." + mthdIns.name + mthdIns.desc;
				if (evoMthd.equals(calledMthd)) {
					callLabels.add(currentLabel);
				}
			}
		}
		if (callLabels.size() == 0) {
			throw new Exception("don't have method " + evoMthd);
		}
		return callLabels;
	}

	private static void debug(MethodNode mn) {
		StringBuilder sb = new StringBuilder();
		ListIterator<AbstractInsnNode> ite = mn.instructions.iterator();
		while (ite.hasNext()) {
			AbstractInsnNode insNode = ite.next();
			// System.out.println(insNode);
			if (insNode instanceof LabelNode) {
				sb.append("\n");
				sb.append(insNode.toString() + "\n");
			} else {
				sb.append(insNode.toString() + "\n");
			}
			if (insNode instanceof MethodInsnNode) {
				MethodInsnNode mthdIns = (MethodInsnNode) insNode;
				System.out.println(mthdIns.owner.replace("/", ".") + "." + mthdIns.name + mthdIns.desc);
			}
			// if (insNode instanceof JumpInsnNode) {
			// ite.remove();
			// }
		}
		System.out.println(sb.toString());
	}

	private static Map<LabelNode, Integer> getLabel2num(MethodNode mn) {
		Map<LabelNode, Integer> label2num = new HashMap<LabelNode, Integer>();
		int labelNum = 0;
		ListIterator<AbstractInsnNode> ite = mn.instructions.iterator();
		while (ite.hasNext()) {
			AbstractInsnNode insNode = ite.next();
			// System.out.println(insNode);
			if (insNode instanceof LabelNode) {
				label2num.put((LabelNode) insNode, labelNum);
				labelNum++;
			}
		}
		return label2num;
	}

	private static Paths getAllExePath(MethodNode mn) {
		Paths paths = new Paths();
		ListIterator<AbstractInsnNode> ite = mn.instructions.iterator();
		boolean hasAddFirst = false;
		LabelNode lastLabel = null;
		while (ite.hasNext()) {
			AbstractInsnNode insNode = ite.next();
			if (insNode instanceof LabelNode) {// add sequence node.
				if (!hasAddFirst) {// first labelNode
					paths.addFirstNode((LabelNode) insNode);
					hasAddFirst = true;
				} else {
					if (lastLabel != null)
						paths.addNewNode(lastLabel, (LabelNode) insNode);
				}
				lastLabel = (LabelNode) insNode;
			}
			if (insNode instanceof JumpInsnNode) {
				JumpInsnNode jumpNode = ((JumpInsnNode) insNode);
				if (jumpNode.getOpcode() == Opcodes.GOTO) {
					paths.addNewNode(lastLabel, jumpNode.label);
					lastLabel = null;
				} else {
					paths.addNewBranchNode(lastLabel, jumpNode.label);
				}
			}
		}
		return paths;
	}

	private static void classTrace() throws Exception {
		ClassReader cr = new ClassReader(new FileInputStream("D:\\ws\\github_snapshot\\truth-release_0_41\\extensions\\liteproto\\target\\classes\\com\\google\\common\\truth\\extensions\\proto\\LiteProtoSubject.class"));

		PrintWriter p1 = new PrintWriter(new FileWriter("d:\\cWs\\notepad++\\out.txt", false));
		cr.accept(new TraceClassVisitor(p1), 0);
	}
}
