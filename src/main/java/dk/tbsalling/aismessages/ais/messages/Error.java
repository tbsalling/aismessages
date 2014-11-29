/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class Error extends AISMessage {

<<<<<<< HEAD:src/main/java/dk/tbsalling/aismessages/messages/Error.java
	public Error(String rawMessage, String errorDescription) {
		super(AISMessageType.Error, null, null, null);
		this.rawMessage = rawMessage;
=======
    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.Error;
    }

    public Error(NMEAMessage[] nmeaMessages, String errorDescription) {
        super(nmeaMessages);
>>>>>>> 865a52b408daf8101cda114a89e4fce7b588254b:src/main/java/dk/tbsalling/aismessages/ais/messages/Error.java
		this.errorDescription = errorDescription;
	}

    @Override
    public String toString() {
        return "Error{" +
                "messageType=" + getMessageType() +
                ", errorDescription='" + errorDescription + '\'' +
                "} " + super.toString();
    }

    @SuppressWarnings("unused")
	public String getErrorDescription() {
		return errorDescription;
	}

	private final String errorDescription;
}
