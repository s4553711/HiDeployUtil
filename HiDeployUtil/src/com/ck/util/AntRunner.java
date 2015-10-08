package com.ck.util;

import com.ck.analyzer.AppAnalyze;
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
    		for(String pipeline : ConfigHelper.getProperty("app_list").split(":")) {
    			AppAnalyze analyzer = new AppAnalyze(pipeline);
    			analyzer.setSshClient(ssh);
    			for(String app : analyzer.findMissingApp()) {
    				findMissing = true;
    				System.out.printf("Warn Pipeline : %-8s, Host : %-20s Missing .. %-30s\n", pipeline, host, app);
    			}
    		}    		
    	}
    	if (findMissing && !quiet) {
    		throw new BuildException("Find missing apps.");
    	}
	}
}
