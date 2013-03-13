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

import javax.jms.Message;
import javax.jms.MessageListener;

import net.modsec.ms.util.MessageUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A AMQ Listener class for listening requests for managing modsecurity from WebSiren(webapp).  
 * */
public class ConnectorListener implements MessageListener{
	
	private final static Logger log = LoggerFactory.getLogger(ConnectorListener.class);
	
	/** (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message msg) {
		
		log.info("Message Received :" + msg);
		String msgString = MessageUtil.getMessageString(msg);
		ConnRequestHandler.onConRequest(msgString);
		
	}
	
}
