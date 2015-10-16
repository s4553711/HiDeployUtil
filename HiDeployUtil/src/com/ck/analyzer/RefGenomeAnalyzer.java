package com.ck.analyzer;

import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

import com.ck.util.ConfigHelper;
import com.ck.util.RemoteTool;

public class RefGenomeAnalyzer extends ResourcesAnalyze {	

	private static boolean checkEnable(String prefix, String term) {
		if (ConfigHelper.getProperty(prefix+"_"+term+"_enable") != null) {
			return Boolean.valueOf(ConfigHelper.getProperty(prefix+"_"+term+"_enable"));
		} else {
			return false;
		}
	}
	
	public static List<String> findFiles(String prefix, RemoteTool ssh) {
		List<String> contigCheckList = asList(
			"contig.flank.intervals",
			"contig.flank.sort.intervals"
		);
		List<String> GatkCheckList = asList(
			"ref.dict",
			"ref.fasta.fai"
		);			

		List<String> list = new ArrayList<>();
		String appList = ConfigHelper.getProperty(prefix+"_ref_resources");
		if (appList == null)
			return list;
		for (String res : appList.split(":")) {
			if (checkEnable(prefix, "bt2")) {
				if (!checkBt2Ref(res, ssh)) {
					list.add(ConfigHelper.getProperty("res_ref_path") + res);
				}
			}
			if (checkEnable(prefix, "bt")) {
				if (!checkBtRef(res, ssh)) {
					list.add(ConfigHelper.getProperty("res_ref_path") + res);
				}
			}			
			if (checkEnable(prefix, "bwa")) {
				if (!checkBwa(res, ssh)){
					list.add(ConfigHelper.getProperty("res_ref_path") + res);
				}
			}
			if (checkEnable(prefix, "gatk")) {
				if (!generalCheck(res, ssh, GatkCheckList)){
					list.add(ConfigHelper.getProperty("res_ref_path") + res);
				}
			}
			if (checkEnable(prefix, "contig")) {
				if (!generalCheck(res, ssh, contigCheckList)){
					list.add(ConfigHelper.getProperty("res_ref_path") + res);
				}
			}
		}
		return list;
	}

	private static boolean generalCheck(String res, RemoteTool ssh, List<String> lists) {
		boolean result = true;
		for(String file : lists) {
			if (findMissing(ConfigHelper.getProperty("res_ref_path") + res + "/" + file, ssh)) {
				result = false;
			}		
		}
		return result;
	}
	
	private static boolean checkBwa(String res, RemoteTool ssh) {
		List<String> lists = asList(
			"ref.fasta",
			"ref.fasta.amb",
			"ref.fasta.ann",
			"ref.fasta.bwt",
			"ref.fasta.pac",
			"ref.fasta.sa"
		);
		boolean result = true;
		for(String file : lists) {
			if (findMissing(ConfigHelper.getProperty("res_ref_path") + res + "/" + file, ssh)) {
				System.out.println("missing "+file);
				result = false;
			}		
		}
		return result;
	}

	private static boolean checkBtRef(String res, RemoteTool ssh) {
		List<String> lists = asList(
			"ref.1.ebwt",
			"ref.2.ebwt",
			"ref.3.ebwt",
			"ref.4.ebwt",
			"ref.rev.1.ebwt",
			"ref.rev.2.ebwt",
			"ref.fa"
		);
		boolean result = true;
		for(String file : lists) {
			if (findMissing(ConfigHelper.getProperty("res_ref_path") + res + "/" + file, ssh)) {
				result = false;
			}		
		}
		return result;
	}
	
	private static boolean checkBt2Ref(String res, RemoteTool ssh) {
		List<String> lists = asList(
			"ref.1.bt2",
			"ref.2.bt2",
			"ref.3.bt2",
			"ref.4.bt2",
			"ref.rev.1.bt2",
			"ref.rev.2.bt2",
			"ref.fa",
			"ref.fa.fai",
			"contig.flank.intervals",
			"contig.flank.sort.intervals"
		);
		boolean result = true;
		for(String file : lists) {
			if (findMissing(ConfigHelper.getProperty("res_ref_path") + res + "/" + file, ssh)) {
				result = false;
			}		
		}
		return result;
	}
	
	private static boolean findMissing(String fullPath, RemoteTool ssh) {
		boolean result = false;
		for(String line : ssh.exec("[ -e "+fullPath+" ] && echo 1 || echo 0").split("\n")) {
			if (line.equals("0")) {
				result = true;
			}
		}
		return result;
	}

}
