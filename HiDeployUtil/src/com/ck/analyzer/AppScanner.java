package com.ck.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.ck.util.RemoteTool;

public class AppScanner {

	public static List<String> scan(RemoteTool ssh) {
		List<String> lists = new ArrayList<>();
		String cmd = "ls -l `find /opt/app/ -maxdepth 1 -type l -print`";
		for(String line : ssh.exec(cmd).split("\n")) {
			String[] columns = line.split("\\s+");
			//System.out.println("> "+columns[8]);
			lists.add(columns[8]);
		}
		return lists;
	}
}
