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

/**
 * This enumeration defines the list constants that are used in modsecurity configurations.
 */
public enum ModSecConfigFields {

	//Basic configuration options
	SecRuleEngine,
	SecRequestBodyAccess,
	SecResponseBodyAccess,
	
	//PCRE Tuning
	SecPcreMatchLimit,
	SecPcreMatchLimitRecursion,
	
	// Handling of file uploads
	SecUploadKeepFiles,
	SecUploadFileLimit,
	
	//Debug log
	SecDebugLog,
	SecDebugLogLevel,
	
	//Serial audit log
	SecAuditEngine,
	SecAuditLogRelevantStatus,
	SecAuditLogParts,
	SecAuditLogType,
	SecAuditLog,
	
	//Maximum request body size we will accept for buffering
	SecRequestBodyLimit,
	
	//Store up to 128 KB in memory
	SecRequestBodyInMemoryLimit,
	
	//Buffer response bodies of up to 512 KB in length
	SecResponseBodyLimit
	
	
}
