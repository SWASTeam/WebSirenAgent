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
package net.modsec.ms.log.pub;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.modsec.ms.util.AMQConService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A producer class is used for initializing message producer for producing audit log messages to
 * Websiren(webapp).   
 */
public class EventsProducer {
	
	
	private final static Logger log = LoggerFactory.getLogger(EventsProducer.class);
	private  Session session = null;
	private  Destination destination = null;
	private MessageProducer producer = null;
	private	AMQConService service=null;
	private String prefix="ConnToAuditEventService";
	
	/**
	 * Initializes message producer using the WebSirenAgent configurations.
	 */
	public void init(){
		
		log.info("init...");
		service=new AMQConService(prefix);
		this.session=service.getSession();
		try {
			
			this.destination = session.createTopic(service.getTopicName());
			this.producer = session.createProducer(destination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * To disconnect producer with Messaging Queue.
	 */
	public void doDisconnect() {
		
		try {
			this.session.close();
			this.session = null;
		} catch (JMSException e) {
		
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Sends audit events to WebSiren(webapp).
	 */
	public  void send(String data) {
		
		log.debug("Sending data" + data);
		try {
			TextMessage message = session.createTextMessage(data);
			producer.send(message);

		} catch (Exception e) {
			log.error("Exception while sending ",e);
			
 		}
		
	}
	
}
