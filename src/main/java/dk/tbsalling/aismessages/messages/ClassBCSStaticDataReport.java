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

import org.apache.commons.lang3.StringEscapeUtils;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.MMSIType;
import dk.tbsalling.aismessages.messages.types.PartNumberType;
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.messages.types.ShipType;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

@SuppressWarnings("serial")
public class ClassBCSStaticDataReport extends DecodedAISMessage {

	public ClassBCSStaticDataReport(
			Integer repeatIndicator, 
			MMSI sourceMmsi, 
			PartNumberType partNumber,
			String shipName,
			ShipType shipType,
			String vendorId,
			String callsign,
			Integer toBow, 
			Integer toStern,
			Integer toStarboard,
			Integer toPort,
			MMSI mothership,
			PositionFixingDevice positionFixingDevice, 
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.ClassBCSStaticDataReport, 
				repeatIndicator,
				sourceMmsi,
				nmeaTagBlock
				);
		this.partNumber = partNumber;
		this.shipName = shipName;
		this.shipType = shipType;
		this.vendorId = vendorId;
		this.callsign = callsign;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.mothership = mothership;
		this.positionFixingDevice = positionFixingDevice;
	}

	public final PartNumberType getPartNumber() {
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
	
	public final MMSI getMothership() {
		return mothership;
	}

	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
		.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
		.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
		.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
		.append("\"part\"").append(":").append(String.format("\"%s\"", partNumber.name())).append(",")
		.append("\"name\"").append(":").append(shipName == null ? null : String.format("\"%s\"", StringEscapeUtils.escapeJson(shipName))).append(",")
		.append("\"cargo\"").append(":").append(shipType == null ? null : String.format("\"%s\"", shipType)).append(",")
		.append("\"vendor\"").append(":").append(vendorId == null ? null : String.format("\"%s\"", StringEscapeUtils.escapeJson(vendorId))).append(",")
		.append("\"callsign\"").append(":").append(callsign == null ? null : String.format("\"%s\"", StringEscapeUtils.escapeJson(callsign))).append(",")
		.append("\"bow\"").append(":").append(toBow).append(",")
		.append("\"stern\"").append(":").append(toStern).append(",")
		.append("\"port\"").append(":").append(toPort).append(",")
		.append("\"starboard\"").append(":").append(toStarboard).append(",")
		.append("\"mothership\"").append(":").append(mothership == null ? null : String.format("\"%s\"", mothership.getMMSI())).append(",")
		.append("\"device\"").append(":").append(positionFixingDevice == null ? null : String.format("\"%s\"", positionFixingDevice.name()));
		if (this.getNMEATagBlock() != null) {
			builder.append(",").append(this.getNMEATagBlock().toString());
		}
		builder.append("}");
		return builder.toString();
	}

	public static ClassBCSStaticDataReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ClassBCSStaticDataReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		PartNumberType partNumber = PartNumberType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 40)));

		// Sometimes part numbers get mixed up. Size has already been checked as valid.
		if (partNumber == PartNumberType.A && encodedMessage.getNumberOfBits() == 168)			
			partNumber = PartNumberType.B;
		if (partNumber == PartNumberType.B && encodedMessage.getNumberOfBits() == 160)			
			partNumber = PartNumberType.A;

		String shipName = partNumber == PartNumberType.A ? DecoderImpl.convertToString(encodedMessage.getBits(40, 160)) : null;
		ShipType shipType = partNumber == PartNumberType.B ? ShipType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 48))) : null;
		String vendorId = partNumber == PartNumberType.B ? DecoderImpl.convertToString(encodedMessage.getBits(48, 90)) : null;
		String callsign = partNumber == PartNumberType.B ? DecoderImpl.convertToString(encodedMessage.getBits(90, 132)) : null;
		Integer toBow = partNumber == PartNumberType.B ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(132, 141)) : null;
		Integer toStern = partNumber == PartNumberType.B ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(141, 150)) : null;
		Integer toPort = partNumber == PartNumberType.B ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(150, 156)) : null;
		Integer toStarboard = partNumber == PartNumberType.B ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(156, 162)) : null;
		MMSI mothership = null; // Not in ITU-R M.1371-5 spec. Kept in for backwards compatibility 
		if (partNumber == PartNumberType.B) {
			if (MMSIType.fromMMSI(sourceMmsi) == MMSIType.Auxiliary) {
				mothership = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(132, 162)));
			}
		}		
		PositionFixingDevice positionFixingDevice = partNumber == PartNumberType.B ? PositionFixingDevice.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(162, 166))) : null;
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new ClassBCSStaticDataReport(
				repeatIndicator, 
				sourceMmsi, 
				partNumber, 
				shipName, 
				shipType,
				vendorId, 
				callsign, 
				toBow, 
				toStern, 
				toStarboard,
				toPort,
				mothership,
				positionFixingDevice, 
				nmeaTagBlock
				);
	}
	
	private final PartNumberType partNumber;
	private final String shipName;
	private final ShipType shipType;
	private final String vendorId;
	private final String callsign;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final MMSI mothership;
	private final PositionFixingDevice positionFixingDevice;
}
