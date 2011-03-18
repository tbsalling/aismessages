package dk.tbsalling.nmea.messages;

/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
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
	
	public NMEAMessage(String messageType, int numberOfFragments,
			int fragmentNumber, char radioChannelCode, String encodedPayload,
			byte fillBits, byte checksum) {
		this.messageType = messageType;
		this.numberOfFragments = numberOfFragments;
		this.fragmentNumber = fragmentNumber;
		this.radioChannelCode = radioChannelCode;
		this.encodedPayload = encodedPayload;
		this.fillBits = fillBits;
		this.checksum = checksum;
	}

	public final String getEncodedPayload() {
		return encodedPayload;
	}

	public final char getRadioChannelCode() {
		return radioChannelCode;
	}

	public final byte getFillBits() {
		return fillBits;
	}

	public final byte getChecksum() {
		return checksum;
	}

	public String toString() {
		final StringBuffer nmeaString = new StringBuffer();
		nmeaString.append("!");
		nmeaString.append(messageType);
		nmeaString.append(",");
		nmeaString.append(numberOfFragments);
		nmeaString.append(",");
		nmeaString.append(fragmentNumber);
		nmeaString.append(",");
		nmeaString.append(",");
		nmeaString.append(radioChannelCode);
		nmeaString.append(",");
		nmeaString.append(encodedPayload);
		nmeaString.append(",");
		nmeaString.append(fillBits);
		nmeaString.append("*");
		nmeaString.append(checksum);
		return nmeaString.toString();
	}
	
	private final String messageType;
	private final int numberOfFragments;
	private final int fragmentNumber;
	private final char radioChannelCode;
	private final String encodedPayload;
	private final byte fillBits;
	private final byte checksum;
}
