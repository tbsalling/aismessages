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
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.messages.types.ShipType;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends DecodedAISMessage {

	public ExtendedClassBEquipmentPositionReport(
			Integer repeatIndicator, MMSI sourceMmsi, String regionalReserved1,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, String regionalReserved2, String shipName,
			ShipType shipType, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort,
			PositionFixingDevice positionFixingDevice, Boolean raimFlag,
			Boolean dataTerminalReady, Boolean assigned) {
		super(AISMessageType.ExtendedClassBEquipmentPositionReport, repeatIndicator, sourceMmsi);
		this.regionalReserved1 = regionalReserved1;
		this.speedOverGround = speedOverGround;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.trueHeading = trueHeading;
		this.second = second;
		this.regionalReserved2 = regionalReserved2;
		this.shipName = shipName;
		this.shipType = shipType;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.raimFlag = raimFlag;
		this.dataTerminalReady = dataTerminalReady;
		this.assigned = assigned;
	}

    @SuppressWarnings("unused")
	public final String getRegionalReserved1() {
		return regionalReserved1;
	}

    @SuppressWarnings("unused")
	public final Float getSpeedOverGround() {
		return speedOverGround;
	}

    @SuppressWarnings("unused")
	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

    @SuppressWarnings("unused")
	public final Float getLatitude() {
		return latitude;
	}

    @SuppressWarnings("unused")
	public final Float getLongitude() {
		return longitude;
	}

    @SuppressWarnings("unused")
	public final Float getCourseOverGround() {
		return courseOverGround;
	}

    @SuppressWarnings("unused")
	public final Integer getTrueHeading() {
		return trueHeading;
	}

    @SuppressWarnings("unused")
	public final Integer getSecond() {
		return second;
	}

    @SuppressWarnings("unused")
	public final String getRegionalReserved2() {
		return regionalReserved2;
	}

    @SuppressWarnings("unused")
	public final String getShipName() {
		return shipName;
	}

    @SuppressWarnings("unused")
	public final ShipType getShipType() {
		return shipType;
	}

    @SuppressWarnings("unused")
	public final Integer getToBow() {
		return toBow;
	}

    @SuppressWarnings("unused")
	public final Integer getToStern() {
		return toStern;
	}

    @SuppressWarnings("unused")
	public final Integer getToStarboard() {
		return toStarboard;
	}

    @SuppressWarnings("unused")
	public final Integer getToPort() {
		return toPort;
	}

    @SuppressWarnings("unused")
	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}

    @SuppressWarnings("unused")
	public final Boolean getRaimFlag() {
		return raimFlag;
	}

    @SuppressWarnings("unused")
	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
	}

    @SuppressWarnings("unused")
	public final Boolean getAssigned() {
		return assigned;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				"ExtendedClassBEquipmentPositionReport [regionalReserved1=")
				.append(regionalReserved1).append(", speedOverGround=")
				.append(speedOverGround).append(", positionAccurate=")
				.append(positionAccurate).append(", latitude=")
				.append(latitude).append(", longitude=").append(longitude)
				.append(", courseOverGround=").append(courseOverGround)
				.append(", trueHeading=").append(trueHeading)
				.append(", second=").append(second)
				.append(", regionalReserved2=").append(regionalReserved2)
				.append(", shipName=").append(shipName).append(", shipType=")
				.append(shipType).append(", toBow=").append(toBow)
				.append(", toStern=").append(toStern).append(", toStarboard=")
				.append(toStarboard).append(", toPort=").append(toPort)
				.append(", positionFixingDevice=").append(positionFixingDevice)
				.append(", raimFlag=").append(raimFlag)
				.append(", dataTerminalReady=").append(dataTerminalReady)
				.append(", assigned=").append(assigned).append("]");
		return builder.toString();
	}

	public static ExtendedClassBEquipmentPositionReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ExtendedClassBEquipmentPositionReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		String regionalReserved1 = Decoder.convertToBitString(encodedMessage.getBits(38, 46));
		Float speedOverGround = Decoder.convertToUnsignedFloat(encodedMessage.getBits(46, 55)) / 10f;
		Boolean positionAccurate = Decoder.convertToBoolean(encodedMessage.getBits(56, 57));
		Float longitude = Decoder.convertToFloat(encodedMessage.getBits(57, 85)) / 600000f;
		Float latitude = Decoder.convertToFloat(encodedMessage.getBits(85, 112)) / 600000f;
		Float courseOverGround = Decoder.convertToUnsignedFloat(encodedMessage.getBits(112, 124)) / 10f;
		Integer trueHeading = Decoder.convertToUnsignedInteger(encodedMessage.getBits(124, 133));
		Integer second = Decoder.convertToUnsignedInteger(encodedMessage.getBits(133, 139));
		String regionalReserved2 = Decoder.convertToBitString(encodedMessage.getBits(139, 143));
		String shipName = Decoder.convertToString(encodedMessage.getBits(143, 263));
		ShipType shipType = ShipType.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(263, 271)));
		Integer toBow = Decoder.convertToUnsignedInteger(encodedMessage.getBits(271, 280));
		Integer toStern = Decoder.convertToUnsignedInteger(encodedMessage.getBits(280, 289));
		Integer toPort = Decoder.convertToUnsignedInteger(encodedMessage.getBits(289, 295));
		Integer toStarboard = Decoder.convertToUnsignedInteger(encodedMessage.getBits(295, 301));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(301, 305)));
		Boolean raimFlag = Decoder.convertToBoolean(encodedMessage.getBits(305, 306));
		Boolean dataTerminalReady = Decoder.convertToBoolean(encodedMessage.getBits(306, 307));
		Boolean assigned = Decoder.convertToBoolean(encodedMessage.getBits(307, 308));
		
		return new ExtendedClassBEquipmentPositionReport(repeatIndicator,
				sourceMmsi, regionalReserved1, speedOverGround,
				positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, regionalReserved2, shipName, shipType,
				toBow, toStern, toStarboard, toPort, positionFixingDevice,
				raimFlag, dataTerminalReady, assigned);
	}
	
	private final String regionalReserved1;
	private final Float speedOverGround;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer trueHeading;
	private final Integer second;
	private final String regionalReserved2;
	private final String shipName;
	private final ShipType shipType;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final Boolean raimFlag;
	private final Boolean dataTerminalReady;
	private final Boolean assigned;
}
