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

package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

/**
 * used to broadcast differential corrections for GPS. The data in the payload
 * is intended to be passed directly to GPS receivers capable of accepting such
 * corrections.
 * 
 * @author tbsalling
 * @copyright S-Consult ApS, Denmark. All rights reserved. See license.txt file.	
 * 
 */
@SuppressWarnings("serial")
public class GNSSBinaryBroadcastMessage extends DecodedAISMessage {

	public GNSSBinaryBroadcastMessage(Integer repeatIndicator, MMSI sourceMmsi, Float latitude, Float longitude, String binaryData) {
		super(AISMessageType.GNSSBinaryBroadcastMessage, repeatIndicator, sourceMmsi);
		this.latitude = latitude;
		this.longitude = longitude;
		this.binaryData = binaryData;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}

	public final String getBinaryData() {
		return binaryData;
	}
	
	public static GNSSBinaryBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.GNSSBinaryBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(40, 58)) / 10f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(58, 75)) / 10f;
		String binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(80, 816));

		return new GNSSBinaryBroadcastMessage(repeatIndicator, sourceMmsi, latitude, longitude, binaryData);
	}
	
	private final Float latitude;
	private final Float longitude;
	private final String binaryData;
}
