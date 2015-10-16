package com.ck.util;

import java.util.Map;

import com.ck.analyzer.AppAnalyze;
import com.ck.analyzer.AppScanner;
import com.ck.analyzer.RefGenomeAnalyzer;
import com.ck.analyzer.ResourcesAnalyze;
import com.ck.util.ConfigHelper;

import org.apache.tools.ant.BuildException;

public class AntRunner {

	private boolean quiet = false;
	
	public void setQuiet(boolean bool) {
		this.quiet  = bool;
	}
	
	public void execute() {
		boolean findMissing = false;
		String[] hosts = ConfigHelper.getProperty("cluster").split(":");
    	for(String host : hosts) {
    		RemoteTool ssh = new RemoteTool(ConfigHelper.getProperty("user"), ConfigHelper.getProperty("pass"), host);
    		Map<String, Boolean> lists = AppScanner.scan(ssh);
    		for(String pipeline : ConfigHelper.getProperty("app_list").split(":")) {
    			System.out.printf("Check app on %-12s, host %s\n", pipeline, host);
    			for(String app : AppAnalyze.findMissingApp(pipeline, ssh, lists)) {
    				findMissing = true;
    				System.out.printf("Warn Pipeline : %-15s, Host : %-20s missing app .. %-30s\n", pipeline, host, app);
    			}
    		}   
    		for(String pipeline : ConfigHelper.getProperty("res_list").split(":")) {
    			System.out.printf("Check res on %-12s, host %s\n", pipeline, host);
    			for(String res : ResourcesAnalyze.findFiles(pipeline, ssh)) {
    				findMissing = true;
    				System.out.printf("Warn Pipeline : %-15s, Host : %-20s missing resource .. %-30s\n", pipeline, host, res);
    			}
    			System.out.printf("Check ref on %-12s, host %s\n", pipeline, host);
    			for(String res : RefGenomeAnalyzer.findFiles(pipeline, ssh)) {
    				System.out.printf("Warn Pipeline : %-15s, Host : %-20s Missing ref .. %-30s\n", pipeline, host, res);
    			} 
    		}
    	}
    	if (findMissing && !quiet) {
    		throw new BuildException("Find missing apps.");
    	}
	}
}
