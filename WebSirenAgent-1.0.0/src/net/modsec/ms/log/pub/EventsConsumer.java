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

import java.text.SimpleDateFormat;
import java.util.Collection;


import net.modsec.ms.connector.ConnectorService;
import net.modsec.ms.util.Severity;

import org.json.simple.JSONObject;
import org.jwall.web.audit.AuditEvent;
import org.jwall.web.audit.AuditEventListener;
import org.jwall.web.audit.AuditEventMessage;
import org.jwall.web.audit.ModSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to the audit events trigged by modsecurity rules. 
 */
public class EventsConsumer implements AuditEventListener {

	private final static Logger log = LoggerFactory.getLogger(EventsConsumer.class);
	
	/**
	 * Handles the single audit event at a time.
	 * @param evt audit event object containing whole event including http message.
	 */
	@Override
	public void eventArrived(AuditEvent evt) {
		
		log.info( "event arrived: " + evt.getEventId());
		onAuditEvent(evt);
	}

	
	/**
	 * Handles the multiple audit events at a time.
	 * @param evts audit event objects containing whole event including http message.
	 */
	@Override
	public void eventsArrived(Collection<AuditEvent> evts) {
		
		for (AuditEvent evt : evts){
		    log.info("event arrived in collection : " + evt.getEventId());
		    onAuditEvent(evt);
		}
		
	}
	
	/**
	 * This function is for extracting data from audit event object.
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void onAuditEvent(AuditEvent evt){
		

		log.info("Audit Event Arrived:" + evt.getEventId() + ":" );
		String ruleID = "";
		String severity = "";
		String tags = "";
		String source = "";
		String line = "";
		String file = "";
		String msgStr = "";
		
		
		JSONObject json = new JSONObject();

		json.put("object", "AuditEvent");
		json.put("id", evt.getEventId());
		
		try{
			int count = 0;
			for(AuditEventMessage msg:evt.getEventMessages()){
				
				msgStr += msg.getRuleMsg();
				source += msg.getTxId();
				ruleID += msg.getRuleId();
				line += msg.getLine(); 
				file += msg.getFile();
				severity += msg.getSeverity();
				
				for(String str: msg.getRuleTags()){
					tags += str + " | "; 
				}
				
				count ++;
				if(evt.getEventMessages().length-1== count){
					
					msgStr += " | ";
					source += " | ";
					ruleID += " | ";
					line += " | "; 
					file += " | ";
					severity += " | ";
					
				}
				
			}
			
		} catch(Exception e){
			log.info("Parsing Message Error : " + e.getMessage());
		}
		
		String date =new  SimpleDateFormat("dd.MM.yyyy | HH:mm:ss").format(evt.getDate());	
		json.put("date", date);
		
		json.put("sensor", evt.get(AuditEvent.SENSOR));
		json.put("sensorID", evt.get(AuditEvent.SENSOR_ID));
		json.put("sensorName", evt.get(AuditEvent.SENSOR_NAME));
		json.put("sensorAddr", evt.get(AuditEvent.SENSOR_ADDR));
		json.put("sensorType", evt.get(AuditEvent.SENSOR_TYPE));
		
		json.put("site_id", evt.get(AuditEvent.SITE_ID));
		json.put("site_name", evt.get(AuditEvent.SITE_NAME));
		json.put("webAppID", evt.get(ModSecurity.WEBAPPID));
		
		json.put("serverName", "" + evt.get(ModSecurity.SERVER_NAME));
		json.put("serverAddr", "" + evt.get(ModSecurity.SERVER_ADDR));
		json.put("serverPort", "" + evt.get(ModSecurity.SERVER_PORT));
		
		json.put("remoteUser", evt.get(ModSecurity.REMOTE_USER ));
		json.put("remoteHost", evt.get(ModSecurity.REMOTE_HOST ));
		json.put("remoteAddr", evt.get(ModSecurity.REMOTE_ADDR ));
		json.put("remotePort", evt.get(ModSecurity.REMOTE_PORT ));
		
		json.put("eventType", evt.getAuditEventType().toString());
		json.put("type", evt.getType().toString());
		json.put("recieved_at", evt.get(AuditEvent.RECEIVED_AT));

		json.put("tx", evt.get(ModSecurity.TX));
		json.put("txID", evt.get(ModSecurity.TX_ID));
		
		json.put("ruleID",ruleID);
		json.put("file",file);
		json.put("line",line);
		json.put("source",source);
		json.put("tags",tags);
		
		boolean sevCheck = true;
		for(Severity sev: Severity.values()){
			
			if(sev.getValue().equals(severity.trim())){
				json.put("severity",sev.toString());
				sevCheck = false;
			}	
			
		}
		if(sevCheck){
			json.put("severity","None");
		}
		
		json.put("message",msgStr);
		
		log.info("Sending Message : " + json.toJSONString());
		ConnectorService.getMSLogProducer().send(json.toJSONString());
		
	}

	
}
