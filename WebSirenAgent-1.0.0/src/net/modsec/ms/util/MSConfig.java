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
package net.modsec.ms.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class MSConfig loads the sysetm configurations into
 * a map. 
 * <pre>
 * 		MSConfig config = MSConfig.getInstance(context);
 * </pre>
 */
public class MSConfig {
	
	
	private final static Logger log = LoggerFactory.getLogger(MSConfig.class);
	private static MSConfig instance= null;
	private Properties prop =  new Properties();
	private Map<String, String> propMap = null;
	
	/**
	 * Loads the configurations from the configuration file.
	 */
	private MSConfig(){
		
		log.info("loading MSService configuration :");
		propMap = new HashMap<String, String>();
    	String fileName = "conf/config.properties";
		InputStream is;
		try {
			
			is = new FileInputStream(fileName);
			prop.load(is);
			for( Entry<Object, Object> entry:prop.entrySet()){
				propMap.put((String) entry.getKey(), (String) entry.getValue());
			}
			
			log.info("MSService configurations Loaded ... ");
			
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
			
		} catch(IOException | NullPointerException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * To get instance of agent configurations. It will initialize object if it does not exists already.
	 */
	public static MSConfig getInstance(){
		
		if(instance == null){
			instance = new MSConfig();
		}
		return instance;
		
	}
	
	/**
	 * Gets the loaded configuration map.
	 * @return configuration map.
	 */
	public Map<String, String> getConfigMap(){
		
		return propMap;
		
	}
	

}
