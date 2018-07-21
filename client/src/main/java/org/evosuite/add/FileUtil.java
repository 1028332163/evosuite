package org.evosuite.add;

import java.io.File;

public class FileUtil {
	public static boolean moveFile(String srcFileName, String tgtDir) {

		File srcFile = new File(srcFileName);
		if (!srcFile.exists() || !srcFile.isFile())
			return false;

		File destDir = new File(tgtDir);
		if (!destDir.exists())
			destDir.mkdirs();

		return srcFile.renameTo(new File(tgtDir + File.separator + srcFile.getName()));
	}

	/**
	 * D:\ws_testcase\modifyCp or D:\ws_testcase\modifyCp\
	 * 
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
		try {
			FileUtil.delAllFile(folderPath); //
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 
				delFolder(path + "/" + tempList[i]);// 
				flag = true;
			}
		}
		return flag;
	}
}
