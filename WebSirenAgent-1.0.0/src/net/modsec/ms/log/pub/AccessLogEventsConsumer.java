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

import java.util.Collection;


import org.jwall.web.audit.AuditEvent;
import org.jwall.web.audit.AuditEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listens to Access log events. Its incomplete and not fullu implemented.
 */
public class AccessLogEventsConsumer implements AuditEventListener {

	private final static Logger log = LoggerFactory.getLogger(AccessLogEventsConsumer.class);
	static int count=0;
	
	/**
	 * @param evt
	 */
	@Override
	public void eventArrived(AuditEvent evt) {
		
		log.info( "event arrived: " + evt.getEventId());
		onAuditEvent(evt);
	}

	/**
	 * @param evts
	 */
	@Override
	public void eventsArrived(Collection<AuditEvent> evts) {
		
		for (AuditEvent evt : evts){
		    log.info("event arrived in collection : " + evt.getEventId());
		    onAuditEvent(evt);
		}
		
	}
	
	/**
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void onAuditEvent(AuditEvent evt){

		
	}
	
}
