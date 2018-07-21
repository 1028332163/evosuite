package org.evosuite.add.shell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.evosuite.add.FileUtil;

/**
 * @author asus
 *validate this path whether executes to end.
 */
public class CallPathValidator {

	String[] mthds;
	String[] jarPaths;

	public CallPathValidator(String[] mthds, String[] jarPaths) {
		super();
		this.mthds = mthds;
		this.jarPaths = jarPaths;
	}

	public void modifyMthdOnPath() throws Exception {
		FileUtil.delFolder(ShellConfig.modifyCp);
		for (int i = 0; i < mthds.length - 1; i++) {
			new MethodModifier(mthds[i],jarPaths[i], mthds[i + 1] ).modifyMthd();
		}
	}
	public void exeEvosuite() {
		String mvnCmd = "mvn org.evosuite.plugins:evosuite-maven-plugin:8.15:generate -f=D:\\cWS\\eclipse1\\testcase.top -Dmodify_cp=D:\\ws_testcase\\modifyCp -Dclass=neu.lab.testcase.top.MthdTop -Dcriterion=MTHD_PROB_RISK -Drisk_method=\"<neu.lab.testcase.bottom.MthdBottom: void m2()>\" -Dmthd_prob_distance_file=D:\\ws_testcase\\image\\distance_mthdBranch\\neu.lab+testcase.top+1.0@neu.lab+testcase.bottom@1.0.txt -Dmaven.test.skip=true -e";
	}
	public void validateCallPath() throws Exception {
		modifyMthdOnPath();//make class don't have branch.
		
	}
	
	public static void main(String[] args) throws Exception {
//		BufferedReader reader = new BufferedReader(new FileReader(
//				"D:\\ws_testcase\\image\\path\\neu.lab+testcase.top+1.0@neu.lab+testcase.bottom@1.0.txt"));
		BufferedReader reader = new BufferedReader(new FileReader(
				"D:\\ws_testcase\\image\\path\\org.ff4j+ff4j-webapi+1.7.1@org.apache.commons+commons-lang3@3.2.1.txt"));
		String line = reader.readLine();
		List<String> mthds = new ArrayList<String>();
		List<String> jarPaths = new ArrayList<String>();
		while (line != null) {
			if (!line.equals("")) {
				if(line.startsWith("pathLen")) {
					
				}else {
					System.out.println(line);
					String[] mthd_path = line.split("> ");
					if(mthd_path.length==2) {
						mthds.add(mthd_path[0]+">");
						jarPaths.add(mthd_path[1]);
					}else {
						mthds.add(mthd_path[0]);
						new CallPathValidator(mthds.toArray(new String[0]),jarPaths.toArray(new String[0])).modifyMthdOnPath();
						break;
					}
				}
			}
			line = reader.readLine();
		}
		reader.close();
	}
}
