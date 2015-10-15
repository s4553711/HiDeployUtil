package com.ck.analyzer;

import java.util.ArrayList;
import java.util.List;
import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class ResourcesAnalyze {

	public static List<String> findFiles(String prefix, RemoteTool ssh) {
		List<String> list = new ArrayList<>();
		String appList = ConfigHelper.getProperty(prefix+"_resources");
		if (appList == null) {
			return list;
		}
		for(String res : appList.split(":")) {
				String fullPath = ConfigHelper.getProperty("res_path") + res;
				for(String line : ssh.exec("[ -e "+fullPath+" ] && echo 1 || echo 0").split("\n")) {
					if (line.equals("0")) {
						list.add(fullPath);
					}
				}
		}
		return list;
	}
}
