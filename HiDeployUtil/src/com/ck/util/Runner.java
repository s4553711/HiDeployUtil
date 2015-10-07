package com.ck.util;

import com.ck.analyzer.AppAnalyze;

public class Runner {
    public static void main(String[] args) {	
    	System.out.println("Log> Start");
    	String[] hosts = ConfigHelper.getProperty("cluster").split(":");
    	for(String host : hosts) {
    		RemoteTool ssh = new RemoteTool(ConfigHelper.getProperty("user"), ConfigHelper.getProperty("pass"), host);
    		for(String pipeline : ConfigHelper.getProperty("app_list").split(":")) {
    			AppAnalyze analyzer = new AppAnalyze(pipeline);
    			analyzer.setSshClient(ssh);
    			for(String app : analyzer.findMissingApp()) {
    				System.out.printf("Warn Pipeline : %-8s, Host : %-20s Missing .. %-30s\n", pipeline, host, app);
    			}
    		}    		
    	}
    	System.out.println("Log> End");
    }
}
