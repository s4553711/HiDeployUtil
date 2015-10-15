package com.ck.run;

import com.ck.analyzer.ResourcesAnalyze;
import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class ResRunner {
    public static void main(String[] args) {	
    	System.out.println("Log> Start resouce check");
    	String[] hosts = ConfigHelper.getProperty("cluster").split(":");
    	for(String host : hosts) {
    		RemoteTool ssh = new RemoteTool(ConfigHelper.getProperty("user"), ConfigHelper.getProperty("pass"), host);
    		for(String pipeline : ConfigHelper.getProperty("res_list").split(":")) {
    			for(String res : ResourcesAnalyze.findFiles("rnade", ssh)) {
    				System.out.printf("Warn Pipeline : %-8s, Host : %-20s Missing .. %-30s\n", pipeline, host, res);
    			}
    		}
    	}
    	System.out.println("Log> Finish resouce check");
    }
}
