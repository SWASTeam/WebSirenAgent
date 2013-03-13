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
package net.modsec.ms.main;

import net.modsec.ms.connector.ConnectorService;
import net.modsec.ms.log.pub.EventService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The WebSirenAgent class is a main class thats starts the agent. 
 */
public class WebSirenAgent {
	
	private final static Logger log = LoggerFactory.getLogger(WebSirenAgent.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		
		log.info(".... Starting Services ....");
		
		//Running a Connector Service
		ConnectorService.startConService();
		// Running a service for listening to events
		new EventService();
		
	}
	
}
