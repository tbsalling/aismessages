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

import dk.tbsalling.aismessages.decoder.Decoder;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.TxRxMode;

@SuppressWarnings("serial")
public class ChannelManagement extends DecodedAISMessage {

	public ChannelManagement(
			Integer repeatIndicator, MMSI sourceMmsi, Integer channelA,
			Integer channelB, TxRxMode transmitReceiveMode, Boolean power,
			Float longitudeNorthEast, Float northEastLatitude,
			Float southWestLongitude, Float southWestLatitude,
			MMSI destinationMmsi1, MMSI destinationMmsi2, Boolean addressed,
			Boolean bandA, Boolean bandB, Integer zoneSize) {
		super(AISMessageType.ChannelManagement, repeatIndicator, sourceMmsi);
		this.channelA = channelA;
		this.channelB = channelB;
		this.transmitReceiveMode = transmitReceiveMode;
		this.power = power;
		this.northEastLongitude = longitudeNorthEast;
		this.northEastLatitude = northEastLatitude;
		this.southWestLongitude = southWestLongitude;
		this.southWestLatitude = southWestLatitude;
		this.destinationMmsi1 = destinationMmsi1;
		this.destinationMmsi2 = destinationMmsi2;
		this.addressed = addressed;
		this.bandA = bandA;
		this.bandB = bandB;
		this.zoneSize = zoneSize;
	}

    @SuppressWarnings("unused")
	public final Integer getChannelA() {
		return channelA;
	}

    @SuppressWarnings("unused")
	public final Integer getChannelB() {
		return channelB;
	}

    @SuppressWarnings("unused")
	public final TxRxMode getTransmitReceiveMode() {
		return transmitReceiveMode;
	}

    @SuppressWarnings("unused")
	public final Boolean getPower() {
		return power;
	}

    @SuppressWarnings("unused")
	public final Float getNorthEastLongitude() {
		return northEastLongitude;
	}

    @SuppressWarnings("unused")
	public final Float getNorthEastLatitude() {
		return northEastLatitude;
	}

    @SuppressWarnings("unused")
	public final Float getSouthWestLongitude() {
		return southWestLongitude;
	}

    @SuppressWarnings("unused")
	public final Float getSouthWestLatitude() {
		return southWestLatitude;
	}

    @SuppressWarnings("unused")
	public final MMSI getDestinationMmsi1() {
		return destinationMmsi1;
	}

    @SuppressWarnings("unused")
	public final MMSI getDestinationMmsi2() {
		return destinationMmsi2;
	}

    @SuppressWarnings("unused")
	public final Boolean getAddressed() {
		return addressed;
	}

    @SuppressWarnings("unused")
	public final Boolean getBandA() {
		return bandA;
	}

    @SuppressWarnings("unused")
	public final Boolean getBandB() {
		return bandB;
	}

    @SuppressWarnings("unused")
	public final Integer getZoneSize() {
		return zoneSize;
	}

	public static ChannelManagement fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ChannelManagement))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		Integer channelA = Decoder.convertToUnsignedInteger(encodedMessage.getBits(40, 52));
		Integer channelB = Decoder.convertToUnsignedInteger(encodedMessage.getBits(52, 64));
		TxRxMode transmitReceiveMode = TxRxMode.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(64, 68)));
		Boolean power = Decoder.convertToBoolean(encodedMessage.getBits(68, 69));
		Boolean addressed = Decoder.convertToBoolean(encodedMessage.getBits(139, 140));
		Boolean bandA = Decoder.convertToBoolean(encodedMessage.getBits(140, 141));
		Boolean bandB = Decoder.convertToBoolean(encodedMessage.getBits(141, 142));
		Integer zoneSize = Decoder.convertToUnsignedInteger(encodedMessage.getBits(142, 145));
		MMSI destinationMmsi1 = !addressed ? null : MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(69, 99)));
		MMSI destinationMmsi2 = !addressed ? null : MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(104, 134)));
		Float northEastLatitude = addressed ? null : Decoder.convertToFloat(encodedMessage.getBits(87, 104)) / 10f;
		Float northEastLongitude = addressed ? null : Decoder.convertToFloat(encodedMessage.getBits(69, 87)) / 10f;
		Float southWestLatitude = addressed ? null : Decoder.convertToFloat(encodedMessage.getBits(122, 138)) / 10f;
		Float southWestLongitude = addressed ? null : Decoder.convertToFloat(encodedMessage.getBits(104, 122)) / 10f;

		return new ChannelManagement(repeatIndicator, sourceMmsi, channelA,
				channelB, transmitReceiveMode, power, northEastLongitude,
				northEastLatitude, southWestLongitude, southWestLatitude,
				destinationMmsi1, destinationMmsi2, addressed, bandA, bandB,
				zoneSize);
	}
		
	private final Integer channelA;
	private final Integer channelB;
	private final TxRxMode transmitReceiveMode;
	private final Boolean power;
	private final Float northEastLongitude;
	private final Float northEastLatitude;
	private final Float southWestLongitude;
	private final Float southWestLatitude;
	private final MMSI destinationMmsi1;
	private final MMSI destinationMmsi2;
	private final Boolean addressed;
	private final Boolean bandA;
	private final Boolean bandB;
	private final Integer zoneSize;
}
