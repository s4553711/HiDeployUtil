package com.ck.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class AppAnalyze {

	public static List<String> findMissingApp(String prefix, RemoteTool ssh, Map<String, Boolean> apps) {
		List<String> list = new ArrayList<>();
		String appList = ConfigHelper.getProperty(prefix+"_app");
		if (appList == null) {
			return list;
		}
		for(String app : appList.split(":")) {
			if (!apps.containsKey(app)) {
				String fullPath = ConfigHelper.getProperty("app_path")+app;
				for(String line : ssh.exec("[ -e "+fullPath+" ] && echo 1 || echo 0").split("\n")) {
					if (line.equals("0")) {
						list.add(fullPath);
					}
				}
			}
		}
		return list;
	}
}
