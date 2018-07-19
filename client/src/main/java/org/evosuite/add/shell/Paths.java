package org.evosuite.add.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.LabelNode;

public class Paths {

	List<Path> paths;

	public Paths() {
		paths = new ArrayList<Path>();
	}

	public void addFirstNode(LabelNode firstNode) {
		paths.add(new Path(firstNode));
	}

	/**
	 * every path that ends with prefix will appends suffix.
	 * 
	 * @param prefix
	 * @param suffix
	 */
	public void addNewNode(LabelNode prefix, LabelNode suffix) {

		for (Path path : paths) {
			if (path.getLast() == prefix) {
				path.addNode(suffix);
			}
		}

	}

	public void addNewBranchNode(LabelNode prefix, LabelNode suffix) {
		List<Path> newPaths = new ArrayList<Path>();
		for (Path path : paths) {
			if (path.getLast() == prefix) {
				Path newPath = path.clone();
				newPath.addNode(suffix);
				newPaths.add(newPath);
			}
		}
		paths.addAll(newPaths);
	}

	public String getPathsStr(Map<LabelNode,Integer> label2num) {
		StringBuilder sb = new StringBuilder();
		for (Path path : this.paths) {
			sb.append(path.getPathStr(label2num) + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * @param callLabels : LabelNodes whose statements call evoMthd.
	 * @return path that occurs LabelNode in callLabels.
	 * @throws Exception 
	 */
	public Path getRemainPath(List<LabelNode> callLabels) throws Exception {
		Path reaminPath = null;
		int minPos = -1;
		for(Path path:paths) {
			int firstPos = path.getLabelPos(callLabels);
			if(firstPos!=-1) {
				if(reaminPath == null||firstPos<minPos) {
					reaminPath = path;
					minPos = firstPos;
				}
			}
		}
		if(reaminPath==null) {
			throw new Exception("can't find callLabels in all path");
		}
		return reaminPath;
	}

	// public void addNewBranchNode(LabelNode prefix, List<LabelNode> suffixNodes) {
	// for (Path path : paths) {
	// if (path.getLast() == prefix) {
	// for (int i = 0; i < suffixNodes.size() - 1; i++) {
	// Path newPath = path.clone();
	// newPath.addNode(suffixNodes.get(i));
	// paths.add(newPath);
	// }
	// path.addNode(suffixNodes.get(suffixNodes.size() - 1));
	// }
	// }
	// }
}
