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

	public GNSSBinaryBroadcastMessage(Integer repeatIndicator, MMSI sourceMmsi, int spare1, 
			Float latitude, Float longitude, int spare2, int mType, int stationId, int zCount, int sequenceNumber, 
			int numOfWords, int health, String binaryData) {
		super(AISMessageType.GNSSBinaryBroadcastMessage, repeatIndicator, sourceMmsi);
		this.spare1 = spare1;
		this.latitude = latitude;
		this.longitude = longitude;
		this.spare2 = spare2;
		this.mType = mType;
		this.stationId = stationId;
		this.zCount = zCount;
		this.sequenceNumber = sequenceNumber;
		this.numOfWords = numOfWords;
		this.health = health;
		this.binaryData = binaryData;
	}
	
	public final int getSpare1() {
		return spare1;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}
	
	public final int getSpare2() {
		return spare2;
	}
	
	public final int getMType() {
		return mType;
	}
	
	public final int getStationId() {
		return stationId;
	}
	
	public final int getZCount() {
		return zCount;
	}
	
	public final int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public final int getNumOfWords() {
		return numOfWords;
	}
	
	public final int getHealth() {
		return health;
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

		int spare1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(40, 58)) / 10f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(58, 75)) / 10f;
		int spare2 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(75, 80));
		
		int mType = 0;
		int stationId = 0;
		int zCount = 0;
		int sequenceNumber = 0;
		int numOfWords = 0;
		int health = 0;
		if(encodedMessage.getNumberOfBits() > 80) {
			mType = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(80, 86));
			stationId = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(86, 96));
			zCount = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(96, 109));
			sequenceNumber = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(109, 112));
			numOfWords = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(112, 117));
			health = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(117, 120));
		}
		String binaryData = DecoderImpl.convertToBitString(encodedMessage.getBits(80, encodedMessage.getNumberOfBits()));

		return new GNSSBinaryBroadcastMessage(repeatIndicator, sourceMmsi, spare1, latitude, longitude, spare2, mType, stationId, 
				zCount, sequenceNumber, numOfWords, health, binaryData);
	}
	
	private final int spare1;
	private final Float latitude;
	private final Float longitude;
	private final int spare2;
	private final int mType;
	private final int stationId;
	private final int zCount;
	private final int sequenceNumber;
	private final int numOfWords;
	private final int health;
	private final String binaryData;
}
