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
 * This enum defines the constants that maps the modsecurity severity level with the meaningful strings.
 */
public enum Severity {

	EMERGENCY("8"), ALERT("7"), CRITICAL("6"), ERROR("5"), 
	WARNING("4"), NOTICE("3"), DEBUG("2"), INFO("1") ;
	
	private String value = ""; // for a display name of each constant

	/**
	 * @param value
	 */
	private Severity(String value) {
		this.value = value;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return this.value;
	}

}
