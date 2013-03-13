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

import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  A class for reading amq configurations from config file 
 *  ("/app/config.cfg") and initialize connection with AMQ Broker
 *  accordingly
 */
public class AMQConService {
	
	private final static Logger log = LoggerFactory.getLogger(AMQConService.class);
	private ActiveMQConnectionFactory connectionFactory = null;
	private Connection connection = null;
	private Session session = null;
	private String url=null;
	private String topic=null;
	private MSConfig config = null;
	
    
    /**
     * Constructor reads the configurations and initializes connection with AMQ Broker. 
     * @param prefix for reading desired amq configurations
     */
    public AMQConService(String prefix){
    	
    	config = MSConfig.getInstance();
    	Map<String, String> propMap = config.getConfigMap();
    	
		this.url=propMap.get("Connection");
		this.topic=propMap.get(prefix+".Topic");
		log.info("Host :"+ this.url+":"+this.topic);
		
		this.connectionFactory=new ActiveMQConnectionFactory(url);
		try {
			
			this.connection=connectionFactory.createConnection();
			this.session=this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		} catch (JMSException e) {
			
			e.printStackTrace();
		
		}
    }
    
    /**
     * To get AMQ connection
     * @return connection
     */
    public Connection getConnection(){
    	return this.connection;
    }
    
    /**
     * To get AMQ session object
     * @return session
     */
    public Session getSession(){
    	return this.session;
    }
    
    /**
     * Gets topic name which is read from configurations
     * @return topicName
     */
    public String getTopicName(){
    	return this.topic;
    }
}
