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

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import net.modsec.ms.util.AMQConService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The ConnectorConsumer class is used for subscribing for WebSiren(webapp)'s modsecurity
 * management requests.
 */
public class ConnectorConsumer {
	
	private final static Logger log = (Logger) LoggerFactory.getLogger(ConnectorConsumer.class);
	private Session session = null;
	private Destination destination = null;
	private Connection connection=null;
	private MessageListener listener=null;
	private MessageConsumer consumer = null;
	private	AMQConService service=null;
	private String prefix="MSServiceToConn";
	
	/**
	 * Initializes message consumer using the WebSirenAgent configurations.
	 */
	public void init(){
		
		log.info("init...");
		this.service=new AMQConService(prefix);
		this.session=service.getSession();
		this.connection=service.getConnection();
		try {
			
			this.destination = session.createTopic(service.getTopicName());
			this.consumer = session.createConsumer(destination);
			// Listen for arriving messages
			this.listener = new ConnectorListener();
			this.consumer.setMessageListener(listener);
			this.connection.start();
			log.info("connected");
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * To disconnect consumer with Messaging Queue.
	 */
	public void doDisconnect() {
		
		try {
			this.session.close();
			this.session = null;
		} catch (JMSException e) {
		
			e.printStackTrace();
		}
	}
	
}
