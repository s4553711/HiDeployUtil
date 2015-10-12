package com.ck.analyzer;

import java.util.HashMap;
import java.util.Map;

import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class AppScanner {

	public static Map<String, Boolean> scan(RemoteTool ssh) {
		Map<String, Boolean> lists = new HashMap<>();
		String cmd = "ls -l `find "+ConfigHelper.getProperty("app_path")+" -maxdepth 1 -type l -print`";
		for(String line : ssh.exec(cmd).split("\n")) {
			String[] columns = line.split("\\s+");
			//System.out.println("> "+columns[8]);
			lists.put(columns[8].replace(ConfigHelper.getProperty("app_path"), ""), true);
		}
		return lists;
	}
}
