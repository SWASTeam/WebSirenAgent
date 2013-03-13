/*
* This file is part of WebSirenAgent.
*
*  WebSirenAgent is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.

*  WebSirenAgent is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with WebSirenAgent.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.modsec.ms.connector;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import net.modsec.ms.util.MSConfig;
import net.modsec.ms.util.ModSecConfigFields;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class is used for handling requests comming from WebSiren(webapp) for managing modesecurity from
 * GUI. 
 */
public class ConnRequestHandler {

	
	private final static Logger log = LoggerFactory.getLogger(ConnRequestHandler.class);
	
	/**
	 * Parses the incomming requests and process it accordingly.
	 * @param msg
	 */
	public static void onConRequest(String msg){
		
		log.info("onConRequest called :" + msg);
		JSONParser parser = new JSONParser(); 
		try {
			
			Object obj = parser.parse(msg);
			JSONObject json = (JSONObject) obj;
			
			String action = (String) json.get("action");
			
			if(action.equals("start")){
				
				onStartRequest(json);
			
			} else if(action.equals("stop")){
				
				onStopRequest(json);
			
			} else if(action.equals("restart")){
				
				onRestartRequest(json);
			
			} else if(action.equals("status")){
				
				onStatusRequest(json);
			
			} else if(action.equals("readMSConfig")){
				
				onReadMSConfig(json);
			
			} else if(action.equals("writeMSConfig")){
				
				onWriteMSConfig(json);
			
			} else if(action.equals("deployRules")){
				
				onDeployMSRules(json);
			
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Starts the modsecurity instance if not running.
	 * @param json 	
	 */
	@SuppressWarnings("unchecked")
	public static void onStartRequest(JSONObject json){
		
		log.info("onStartRequest called.. : " + json.toJSONString());
		MSConfig config = MSConfig.getInstance();
		String cmd = config.getConfigMap().get("MSStart"); 
		
		JSONObject resp = executeShScript(cmd, json);
		if(((String)resp.get("status")).equals("0")){
			resp.put("message", "Modsecuity Sucessfully Started");
		}
		
		log.info("Sending Json :" + resp.toJSONString());
		ConnectorService.getConnectorProducer().send(resp.toJSONString());
		resp.clear();
		
	}
	
	/**
	 * Stops the modsecurity instance if not stopped before.
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public static void onStopRequest(JSONObject json){
			
		log.info("onStopRequest called.. : " + json.toJSONString());
		MSConfig config = MSConfig.getInstance();
		String cmd = config.getConfigMap().get("MSStop"); 
		
		JSONObject resp = executeShScript(cmd, json);
		if(((String)resp.get("status")).equals("0") ){
			resp.put("message", "Modsecuity Sucessfully Stoped");
		}
		
		log.info("Sending Json :" + resp.toJSONString());
		ConnectorService.getConnectorProducer().send(resp.toJSONString());
		resp.clear();
		
	}
	
	/**
	 * Restarts the instance.
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public static void onRestartRequest(JSONObject json){
		
		log.info("onRestartRequest called.. : " + json.toJSONString());
		MSConfig config = MSConfig.getInstance();
		String cmd = config.getConfigMap().get("MSRestart"); 
		
		JSONObject resp = executeShScript(cmd, json);
		if(((String)resp.get("status")).equals("0")){
			resp.put("message", "Modsecuity Sucessfully Restarted");
		}
		
		log.info("Sending Json :" + resp.toJSONString());
		ConnectorService.getConnectorProducer().send(resp.toJSONString());
		resp.clear();
		
	}
	
	/**
	 * Checks the modsecurity current status whether its running or stopped.
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public static void onStatusRequest(JSONObject json){
		
		log.info("onStatusRequest called.. : " + json.toJSONString());
		MSConfig config = MSConfig.getInstance();
		String cmd = config.getConfigMap().get("MSStatus"); 
		
		JSONObject resp = executeShScript(cmd, json);
		if(((String)resp.get("status")).equals("0")){
			
			log.info("Message After Execution:" + (String)resp.get("message"));
			if(((String)resp.get("message")).contains("running")){
				resp.put("msStatus", "1");
			} else{
				resp.put("msStatus", "0");
			}
			
			
		}
		
		log.info("Sending Json :" + resp.toJSONString());
		ConnectorService.getConnectorProducer().send(resp.toJSONString());
		resp.clear();
		
	}
	
	/**
	 * Reads the modsecurity configuration file on modsecurity machine.
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public static void onReadMSConfig(JSONObject json){
		
		log.info("onReadMSConfig called.. : " + json.toJSONString());
		MSConfig serviceCfg = MSConfig.getInstance();
		
    	String fileName = serviceCfg.getConfigMap().get("MSConfigFile");
		InputStream ins;
		BufferedReader br;
		
		try {
			
			File file = new File(fileName);
			DataInputStream in;
			
			@SuppressWarnings("resource")
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			FileLock lock = channel.lock();
			
			try{
				
				ins = new FileInputStream(file);
				in = new  DataInputStream(ins);
				br = new BufferedReader(new InputStreamReader(in));
				
				String line = "";
				
				while((line = br.readLine()) != null){
					
					//log.info("Line :" + line);
					for(ModSecConfigFields field: ModSecConfigFields.values() ){
						
						if(line.startsWith(field.toString())){
							
							if(line.trim().split(" ")[0].equals(field.toString())){
								json.put(field.toString(), line.trim().split(" ")[1].replace("\"", ""));
							}
							
						}
						
					}
					
				}
				log.info("ModSecurity Configurations configurations Loaded ... ");
			
			} finally{
				
				lock.release();
				
			}
			br.close();
			in.close();
			ins.close();
			
			
		} catch (FileNotFoundException e1) {
			
			json.put("status", "1");
			json.put("message", "configuration file not found");
			e1.printStackTrace();
			
		} catch(IOException | NullPointerException e){
			
			json.put("status", "1");
			json.put("message", "configuration file is corrupt");
			e.printStackTrace();
			
		}
		
		log.info("Sending Json :" + json.toJSONString());
		ConnectorService.getConnectorProducer().send(json.toJSONString());
		json.clear();
		
	}
	
	/**
	 * Writes the modified modsecurity configurations to configuration file.
	 * @param json contains modsecurity configurations as json object.
	 */
	@SuppressWarnings("unchecked")
	public static void onWriteMSConfig(JSONObject json){
		
		log.info("onWriteMSConfig called.. : " + json.toJSONString());
		
		MSConfig serviceCfg = MSConfig.getInstance();
		JSONObject jsonResp= new JSONObject();
    	String fileName = serviceCfg.getConfigMap().get("MSConfigFile");
    	String modifiedStr = "";
    	
		InputStream ins = null;
		FileOutputStream out = null;
		BufferedReader br = null;
		
		try {
			
			File file = new File(fileName);
			DataInputStream in;
			
			@SuppressWarnings("resource")
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			FileLock lock = channel.lock();
			
			try{
				
				ins = new FileInputStream(file);
				in = new DataInputStream(ins);
				br = new BufferedReader(new InputStreamReader(in));
				
				String line = "";
				boolean check;
				
				while((line = br.readLine()) != null){
					
					check = true;
					//log.info("Line :" + line);
					for(ModSecConfigFields field: ModSecConfigFields.values() ){
						
						if(line.startsWith(field.toString())){
							if(line.trim().split(" ")[0].equals(field.toString())){
								if(json.containsKey(field.toString())){
									
									if(((String)json.get(field.toString())).equals("") || json.get(field.toString()) == null){
										
										log.info("---------- Log Empty value ----:" + (String)json.get(field.toString()));
										json.remove(field.toString());
										check = false;
										continue;
									
									} else{
		
										modifiedStr += field.toString() + " " + json.remove(field.toString()) + "\n";
										check = false;
										
									}
									
								}
							}
						} 
						
					}
					
					if(check){
						modifiedStr += line + "\n";
					}
					
				}
				
				for(ModSecConfigFields field: ModSecConfigFields.values()){
					if(json.containsKey(field.toString())){
						
						if(json.get(field.toString()) == null || ((String)json.get(field.toString())).equals("")){
							
							log.info("---------- Log Empty value ----:" + (String)json.get(field.toString()));
							json.remove(field.toString());
							check = false;
							continue;
						
						} else{
							modifiedStr += field.toString() + " " + json.remove(field.toString()) + "\n";
						}
						
					}
					
				}
				
				//modified string writing to modsecurity configurations
				log.info("Writing File :" + modifiedStr);
				out = new FileOutputStream(fileName);
				out.write(modifiedStr.getBytes());	

				log.info("ModSecurity Configurations configurations Written ... ");
				
			} finally{
				
				lock.release();
				
			}
			
			br.close();
			in.close();
			ins.close();
			out.close();
			
			//For Restarting modsecurity so that modified configuration can be applied
			JSONObject restartJson = new JSONObject();
			restartJson.put("action", "restart");
			
			String cmd = serviceCfg.getConfigMap().get("MSRestart"); 
			
			executeShScript(cmd, restartJson);
			
			jsonResp.put("action", "writeMSConfig");
			jsonResp.put("status", "0");
			jsonResp.put("message", "Configurations updated!");
			
			
		} catch (FileNotFoundException e1) {
			
			jsonResp.put("action", "writeMSConfig");
			jsonResp.put("status", "1");
			jsonResp.put("message", "Internal Service is down!");
			e1.printStackTrace();
			
		} catch(IOException | NullPointerException e){
			
			jsonResp.put("action", "writeMSConfig");
			jsonResp.put("status", "0");
			jsonResp.put("message", "Unable to modify configurations. Sorry of inconvenience");
			e.printStackTrace();
			
		}
		
		log.info("Sending Json :" + jsonResp.toJSONString());
		ConnectorService.getConnectorProducer().send(jsonResp.toJSONString());
		jsonResp.clear();
	}
	
	/**
	 * @param cmd
	 */
	/**
	 * Executes the shell scripts for starting, restarting and stopping modsecurity. 
	 * @param cmd - path of the shell script.
	 * @param jsonResp  - response to WebSiren(webapp) request.
	 * @return jsonresponse - response to WebSiren(webapp) request. 
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject executeShScript(String cmd, JSONObject jsonResp){
		
		log.info("starting shell script execution ... :" + cmd);
		try {			
			
			Process process = Runtime.getRuntime().exec(cmd);
			String outStr = "", s = "";
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((s = br.readLine()) != null)
			{
				outStr += s;
			} 
			
			log.info("Output String : " + outStr);
	
			String errOutput = "" ;
			BufferedReader br2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
				while (br2.ready() && (s = br2.readLine()) != null)
				{
				  errOutput += s;
				}
			log.info("Error String : " + errOutput);
			
			if(errOutput.contains("Syntax error")){
				
				jsonResp.put("status", "1");
				jsonResp.put("message", "Failed to start modsecurity");
				
			} else{
				
				jsonResp.put("status", "0");
				jsonResp.put("message", outStr);
			
			}
		
		} catch (IOException e) {
			
			jsonResp.put("status", "1");
			jsonResp.put("message", "Error: internal service is down");
			log.info("Error Message: " + e.getMessage());
			//e.printStackTrace();
		
		}
		
		return jsonResp;
	}
	
	/**
	 * Parses the json, extracts the rules and deploy those rules into modsecurity. It takes the rule 
	 * string from the json and creates a rule file then copy it to modsecurity rule file folder. It alsp
	 * restarts the modsecurity after deploying the rule
	 * @param json reponses whether the rule is successfully deployed or not.
	 */
	@SuppressWarnings("unchecked")
	public static void onDeployMSRules(JSONObject json){
		
		log.info("onDeployMSRules called.. : " + json.toJSONString());
		
		MSConfig serviceCfg = MSConfig.getInstance();
		JSONObject jsonResp= new JSONObject();
		
		String ruleDirPath = serviceCfg.getConfigMap().get("RuleFileDir");
    	String ruleFileString = (String) json.get("ruleString");
    	String ruleFileName = "";
    	
    	InputStream ins = null;
		FileOutputStream out = null;
		BufferedReader br = null;
		
		if(json.containsKey("groupName")){
			
			ruleFileName += ((String)json.get("groupName")).toLowerCase() + ".conf";
			
		} else{
			
			 UUID randomName = UUID.randomUUID();
			 ruleFileName += randomName.toString() + ".conf";
			 
		}
		
		try {
			
			//modified string writing to modsecurity configurations
			log.info("Writing a rule to File :" + ruleFileName);
			File file = new File(ruleDirPath + "/" + ruleFileName);
			file.createNewFile();
			out = new FileOutputStream(file);
			out.write(ruleFileString.getBytes());
			out.close();
			
			log.info("ModSecurity Rules Written ... ");
			
			//For Restarting modsecurity so that modified configuration can be applied
			JSONObject restartJson = new JSONObject();
			restartJson.put("action", "restart");
			
			String cmd = serviceCfg.getConfigMap().get("MSRestart"); 
			
			String status = (String)executeShScript(cmd, restartJson).get("status");
			
			//if rule file is giving syntax error while deploying rules on server end 
			if(status.equals("1")){
				
				if(file.delete()){
					log.info("Successfully deleted conflicting file : " + file.getName());
					executeShScript(cmd, restartJson);
				} else {
					log.info("unable to delete file : " + file.getName());
				}
				jsonResp.put("action", "deployRules");
				jsonResp.put("status", "1");
				jsonResp.put("message", "Unable to deploy specified Rules. They either" +
						"conflicting to the already deployed rules");
			
			} else {
				
				jsonResp.put("action", "deployRules");
				jsonResp.put("status", "0");
				jsonResp.put("message", "Rules Deployed!");
			
			}
			
			
		} catch (FileNotFoundException e1) {
			
			jsonResp.put("action", "deployRules");
			jsonResp.put("status", "1");
			jsonResp.put("message", "Internal Service is down!");
			e1.printStackTrace();
			
		} catch(IOException | NullPointerException e){
			
			jsonResp.put("action", "deployRules");
			jsonResp.put("status", "0");
			jsonResp.put("message", "Unable to create rule file on Server.");
			e.printStackTrace();
			
		}
		
		log.info("Sending Json :" + jsonResp.toJSONString());
		ConnectorService.getConnectorProducer().send(jsonResp.toJSONString());
		jsonResp.clear();
	}
	
	
}
