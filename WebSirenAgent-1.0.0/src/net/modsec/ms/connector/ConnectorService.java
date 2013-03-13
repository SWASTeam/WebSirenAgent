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

import net.modsec.ms.log.pub.EventsProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ConnectorService class is used to initialize all consumers and producers
 * used for communication with WebSiren(webapp). It also 
 * lazy-loads( loads if needed ) the producers and consumers
 */
public class ConnectorService {
	
private final static Logger log = LoggerFactory.getLogger(ConnectorService.class);
	
	private static ConnectorProducer conProd = null; //for producing modsecurity responses
	private static EventsProducer msLogProd = null; //for producing log responses responses
	private static ConnectorConsumer conCons = null; //for consuming modsecurity requests
	
	
	/**
	 * Starts the Consumers and Producers
	 */
	public static void startConService(){
		
		log.info("Starting Modsec message producers");
		conProd = new ConnectorProducer();
		conProd.init();
		
		log.info("Starting Modsec log producers");
		msLogProd = new EventsProducer();
		msLogProd.init();
		
		log.info("Starting Modsec message consumers");
		conCons = new ConnectorConsumer();
		conCons.init();
		
	}
	
	/**
	 * Gets the ConnectorProducer. It also initializes producer if it didn't initialized before. 
	 * @return ConnectorProducer
	 */
	public static ConnectorProducer getConnectorProducer(){
		
		if(conProd == null){
			
			conProd = new ConnectorProducer();
			conProd.init();
		
		}
		return conProd; 
		
	}
	
	/**
	 * Gets the MSLogProducer. It also initializes producer if it didn't initialized before. 
	 * @return MSLogProducer
	 */
	public static EventsProducer getMSLogProducer(){
		
		if(msLogProd == null){
			
			msLogProd = new EventsProducer();
			msLogProd.init();
		
		}
		return msLogProd; 
		
	}
	
	/**
	 * Gets the ConnectorConsumer. It also initializes consumer if it didn't initialized before. 
	 * @return ConnectorConsumer object. 
	 */
	public static ConnectorConsumer getConnectorConsumer(){
		
		if(conCons == null){
			
			conCons = new ConnectorConsumer();
			conCons.init();
		
		}
		return conCons; 
		
	}

}
