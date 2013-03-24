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

package dk.tbsalling.aismessages.nmea.messages;

import dk.tbsalling.aismessages.nmea.exceptions.NMEAParseException;
import dk.tbsalling.aismessages.nmea.exceptions.UnsupportedMessageType;

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

public class NMEAMessage {

	public static NMEAMessage fromString(String nmeaString) {
		return new NMEAMessage(nmeaString);
	}

	public final boolean isValid() {
		return true;
	}

	public final String getMessageType() {
		return messageType;
	}

	public final Integer getNumberOfFragments() {
		return numberOfFragments;
	}

	public final Integer getFragmentNumber() {
		return fragmentNumber;
	}

	public final Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public final String getRadioChannelCode() {
		return radioChannelCode;
	}

	public final String getEncodedPayload() {
		return encodedPayload;
	}

	public final Integer getFillBits() {
		return fillBits;
	}

	public final Integer getChecksum() {
		return checksum;
	}

	public final String getRawMessage() {
		return rawMessage;
	}

	private NMEAMessage() {
		this.messageType = null;
		this.numberOfFragments = null;
		this.fragmentNumber = null;
		this.sequenceNumber = null;
		this.radioChannelCode = null;
		this.encodedPayload = null;
		this.fillBits = null;
		this.checksum = null;
		this.rawMessage = null;
	}
	
	private NMEAMessage(String rawMessage) {
		// !AIVDM,1,1,,B,15MvlfPOh2G?nwbEdVDsnSTR00S?,0*41
		
		final String nmeaMessageRegExp = "^!.*\\*[0-9A-Fa-f]{2}$";
		
		if (! rawMessage.matches(nmeaMessageRegExp))
			throw new NMEAParseException(rawMessage, "Message does not comply with regexp \"" + nmeaMessageRegExp + "\"");

		String[] msg = rawMessage.split(",");
		if (msg.length != 7)
			throw new NMEAParseException(rawMessage, "Expected 7 fields separated by commas; got " + msg.length);

		this.rawMessage = new String(rawMessage);
		this.messageType = isBlank(msg[0]) ? null : msg[0].replace("!", "");
		this.numberOfFragments = isBlank(msg[1]) ? null : Integer.valueOf(msg[1]);
		this.fragmentNumber = isBlank(msg[2]) ? null : Integer.valueOf(msg[2]);
		this.sequenceNumber = isBlank(msg[3]) ? null : Integer.valueOf(msg[3]);
		this.radioChannelCode = isBlank(msg[4]) ? null : msg[4];
		this.encodedPayload = isBlank(msg[5]) ? null : msg[5];
		
		String msg1[] = msg[6].split("\\*");
		if (msg1.length != 2)
			throw new NMEAParseException(rawMessage, "Expected checksum fields to start with *");
		
		this.fillBits = isBlank(msg1[0]) ? null : Integer.valueOf(msg1[0]);
		this.checksum = isBlank(msg1[1]) ? null : Integer.valueOf(msg1[1], 16);

		validate();
	}

	private void validate() {
		if (! ("AIVDM".equals(messageType) || "AIVDO".equals(messageType))) {
			throw new UnsupportedMessageType(messageType);
		}
	}
	
	private final static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}
	
	private final String messageType;
	private final Integer numberOfFragments;
	private final Integer fragmentNumber;
	private final Integer sequenceNumber;
	private final String radioChannelCode;
	private final String encodedPayload;
	private final Integer fillBits;
	private final Integer checksum;
	private final String rawMessage;
}
