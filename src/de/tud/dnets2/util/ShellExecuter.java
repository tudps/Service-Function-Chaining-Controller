/**
 * This file is part of the program/application/library Service Function Chaining Controller (SFCC), a component of the Software-Defined Network Service Chaining Demonstrator.
 *
 * © 2015 Negoescu, Victor and Blendin, Jeremias
 *
 * This program is licensed under the Apache-2.0 License (http://opensource.org/licenses/Apache-2.0). See also file LICENSING
 *
 * Development sponsored by Deutsche Telekom AG [ opensource@telekom.de<mailto:opensource@telekom.de> ]
 * 
 * Modificationsections:
 * 1. 2014, Negoescu, Victor-Phillipp: Initial Release.
 */
package de.tud.dnets2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
@author Sergej Melnikowitsch

created Feb 27, 2014
 */

public class ShellExecuter {
	String command;
	
	private void logProcess(Process p) throws IOException{
		StringBuilder s = new StringBuilder();
		BufferedReader input =
		    new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		//Here we first read the next line into the variable
		//line and then check for the EOF condition, which
		//is the return value of null
		while((line = input.readLine()) != null){
		    s.append(line+'\n');
		}
		
		BufferedReader err =
			    new BufferedReader(new InputStreamReader(p.getErrorStream()));
		s = new StringBuilder();
		//Here we first read the next line into the variable
		//line and then check for the EOF condition, which
		//is the return value of null
		while((line = err.readLine()) != null){
		    s.append(line+'\n');
		}
	}
	
//	/**
//	 * Executes a command via Linux' bash.
//	 * @param command you want to execute.
//	 */
//	public ShellExecuter(String command) {
//		// TODO Auto-generated constructor stub
//		this.command = command;
//	}
	
	public synchronized void execute(String cmd, Object... params){
		command = cmd;
		for(Object s : params){
			command = command + " " + s.toString();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
				try {
					Process p = processBuilder.start();
					p.waitFor();
					logProcess(p);
					p.destroy();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

}
