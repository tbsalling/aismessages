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
import dk.tbsalling.aismessages.messages.types.ShipType;

@SuppressWarnings("serial")
public class ClassBCSStaticDataReport extends DecodedAISMessage {

	public ClassBCSStaticDataReport(
			Integer repeatIndicator, MMSI sourceMmsi, Integer partNumber,
			String shipName, ShipType shipType, String vendorId,
			String callsign, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort, MMSI mothershipMmsi) {
		super(AISMessageType.ClassBCSStaticDataReport, repeatIndicator, sourceMmsi);
		this.partNumber = partNumber;
		this.shipName = shipName;
		this.shipType = shipType;
		this.vendorId = vendorId;
		this.callsign = callsign;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.mothershipMmsi = mothershipMmsi;
	}

	public final Integer getPartNumber() {
		return partNumber;
	}

	public final String getShipName() {
		return shipName;
	}

	public final ShipType getShipType() {
		return shipType;
	}

	public final String getVendorId() {
		return vendorId;
	}

	public final String getCallsign() {
		return callsign;
	}

	public final Integer getToBow() {
		return toBow;
	}

	public final Integer getToStern() {
		return toStern;
	}

	public final Integer getToStarboard() {
		return toStarboard;
	}

	public final Integer getToPort() {
		return toPort;
	}

	public final MMSI getMothershipMmsi() {
		return mothershipMmsi;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClassBCSStaticDataReport [partNumber=")
				.append(partNumber).append(", shipName=").append(shipName)
				.append(", shipType=").append(shipType).append(", vendorId=")
				.append(vendorId).append(", callsign=").append(callsign)
				.append(", toBow=").append(toBow).append(", toStern=")
				.append(toStern).append(", toStarboard=").append(toStarboard)
				.append(", toPort=").append(toPort).append(", mothershipMmsi=")
				.append(mothershipMmsi).append("]");
		return builder.toString();
	}

	public static ClassBCSStaticDataReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ClassBCSStaticDataReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer partNumber = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 40));

		String shipName = partNumber == 1 ? null : DecoderImpl.convertToString(encodedMessage.getBits(40, 160));
		ShipType shipType = partNumber == 0 ? null : ShipType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 48)));
		String vendorId = partNumber == 0 ? null : DecoderImpl.convertToString(encodedMessage.getBits(48, 90));
		String callsign = partNumber == 0 ? null : DecoderImpl.convertToString(encodedMessage.getBits(90, 132));
		Integer toBow = partNumber == 0 ? null : DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(132, 141));
		Integer toStern = partNumber == 0 ? null : DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(141, 150));
		Integer toPort = partNumber == 0 ? null : DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(150, 156));
		Integer toStarboard = partNumber == 0 ? null : DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(156, 162));
		MMSI mothershipMmsi = partNumber == 0 ? null : MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(132, 162)));
		
		return new ClassBCSStaticDataReport(repeatIndicator, sourceMmsi, partNumber, shipName, shipType, vendorId, callsign, toBow, toStern, toStarboard, toPort, mothershipMmsi);
	}
	
	private final Integer partNumber;
	private final String shipName;
	private final ShipType shipType;
	private final String vendorId;
	private final String callsign;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final MMSI mothershipMmsi;
}
