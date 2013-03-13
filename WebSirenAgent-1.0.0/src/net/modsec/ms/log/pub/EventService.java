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

import java.io.File;
import java.io.IOException;

import net.modsec.ms.util.MSConfig;

import org.jwall.log.io.AccessLogReader;
import org.jwall.web.audit.AuditEventDispatcher;
import org.jwall.web.audit.io.AccessLogAuditReader;
import org.jwall.web.audit.io.AuditEventReader;
import org.jwall.web.audit.io.ModSecurity2AuditReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EventService class is used to start audit event reader service.
 */
public class EventService {
	
	
	private final static Logger log = LoggerFactory.getLogger(AuditEventDispatcher.class);
	AuditEventReader reader = null;
	AuditEventDispatcher dispatcher = null;
	
	/**
	 * registers audit log file and starts listening audit events. 
	 */
	public EventService(){

		MSConfig config = MSConfig.getInstance();
		String filePath = config.getConfigMap().get("AuditLogsFilePath");
		log.info("Audit File Path : " + filePath);
		
		File auditFile = new File(filePath);
		
		try {
			
			log.info("Intializing Modsec audit reader");
			reader = new ModSecurity2AuditReader(auditFile);
			dispatcher = new AuditEventDispatcher(reader); 
			dispatcher.addAuditEventListener(new EventsConsumer());
			dispatcher.start();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
}
