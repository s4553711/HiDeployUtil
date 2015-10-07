package com.ck.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteTool {
	private String user;
	private String pass;
	private String host;
	public RemoteTool(String user, String pass, String host) {
		this.user = user;
		this.pass = pass;
		this.host = host;
	}

	public String exec(String cmd, boolean log, boolean checkExitStatus) {
		Session session = null;
		ChannelExec channel = null;
		try {
			session = newSession();
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(cmd);
			channel.connect();
			String slurp = slurp(cmd, channel, checkExitStatus);
			return slurp;
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
		return "";
	}

	public String exec(String cmd) {
		return exec(cmd, false, false);
	}

	private Session newSession() throws Exception {
		Session session;
		session = new JSch().getSession(user, host, 22);
		session.setPassword(pass);
		session.setConfig("StrictHostKeyChecking", "no");
		// session.setConfig("UserKnownHostsFile", "/dev/null");
		session.setConfig("GSSAPIAuthentication", "no");
		session.setConfig("cipher.s2c", "blowfish-cbc");
		session.setConfig("cipher.c2s", "blowfish-cbc");
		session.connect();
		return session;
	}

	private String slurp(String command, ChannelExec channel, boolean checkExitStatus) throws IOException, InterruptedException {
		StringBuffer stdoutBuffer = new StringBuffer();
		StringBuffer stderrBuffer = new StringBuffer();
		InputStream stdoutStream = channel.getInputStream();
		InputStream stderrStream = channel.getErrStream();
		try {
			byte[] tmp = new byte[1024];
			while (true) {
				while (stdoutStream.available() >= 0) {
					int i = stdoutStream.read(tmp, 0, 1024);
					if (i < 0)
						break;
					stdoutBuffer.append(new String(tmp, 0, i));
				}
				while (stderrStream.available() >= 0) {
					int i = stderrStream.read(tmp, 0, 1024);
					if (i < 0)
						break;
					stderrBuffer.append(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					break;
				}
				TimeUnit.MILLISECONDS.sleep(5000);
			}
		} finally {
			closeSilently(stdoutStream);
			closeSilently(stderrStream);
		}
		if (checkExitStatus && channel.getExitStatus() != 0) {
			System.out.println("Error> " + stderrBuffer.toString().trim() + " , " + command);
		}
		return stdoutBuffer.toString().trim();
	}

	private void closeSilently(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
