package com.ck.util;

import java.util.Map;

import com.ck.analyzer.AppAnalyze;
import com.ck.analyzer.AppScanner;

public class Runner {
    public static void main(String[] args) {	
    	System.out.println("Log> Start");
    	String[] hosts = ConfigHelper.getProperty("cluster").split(":");
    	for(String host : hosts) {
    		RemoteTool ssh = new RemoteTool(ConfigHelper.getProperty("user"), ConfigHelper.getProperty("pass"), host);
    		Map<String, Boolean> lists = AppScanner.scan(ssh);
    		for(String pipeline : ConfigHelper.getProperty("app_list").split(":")) {
    			for(String app : AppAnalyze.findMissingApp(pipeline, ssh, lists)) {
    				System.out.printf("Warn Pipeline : %-8s, Host : %-20s Missing .. %-30s\n", pipeline, host, app);
    			}
    		}
    	}
    	System.out.println("Log> End");
    }
}
