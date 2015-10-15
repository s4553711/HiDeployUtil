package com.ck.run;

import org.apache.tools.ant.BuildException;

import com.ck.analyzer.ResourcesAnalyze;
import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class AntResRunner {

	private boolean quiet = false;
	
	public void setQuiet(boolean bool) {
		this.quiet  = bool;
	}

    public void execute() {	
    	boolean findMissing = false;
    	String[] hosts = ConfigHelper.getProperty("cluster").split(":");
    	for(String host : hosts) {
    		RemoteTool ssh = new RemoteTool(ConfigHelper.getProperty("user"), ConfigHelper.getProperty("pass"), host);
    		for(String pipeline : ConfigHelper.getProperty("res_list").split(":")) {
    			for(String res : ResourcesAnalyze.findFiles(pipeline, ssh)) {
    				findMissing = true;
    				System.out.printf("Warn Pipeline : %-8s, Host : %-20s Missing .. %-30s\n", pipeline, host, res);
    			}
    		}
    	}
    	if (findMissing && !quiet) {
    		throw new BuildException("Find missing resources.");
    	}
    }
}
