package com.ck.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class AppAnalyze {
	
	private String prefix;
	private RemoteTool ssh;

	public AppAnalyze(String prefix) {
		this.prefix = prefix;
	}

	public List<String> findMissingApp() {
		List<String> list = new ArrayList<>();
		String appList = ConfigHelper.getProperty(prefix+"_app");
		if (appList == null) {
			return list;
		}
		for(String app : appList.split(":")) {
			String fullPath = ConfigHelper.getProperty("app_path")+app;
			for(String line : ssh.exec("[ -e "+fullPath+" ] && echo 1 || echo 0").split("\n")) {
				if (line.equals("0")) {
					list.add(fullPath);
				}
			}
		}
		return list;
	}

	public void setSshClient(RemoteTool ssh) {
		this.ssh = ssh;
	}
}
