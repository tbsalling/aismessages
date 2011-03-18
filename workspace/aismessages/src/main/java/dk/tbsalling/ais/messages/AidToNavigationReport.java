package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.AidType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.PositionFixingDevice;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class AidToNavigationReport extends DecodedAISMessage {

	public AidToNavigationReport(
			Integer repeatIndicator, MMSI sourceMmsi, AidType aidType,
			String name, Boolean positionAccurate, Float latitude,
			Float longitude, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort,
			PositionFixingDevice positionFixingDevice, Integer second,
			Boolean offPosition, String regionalUse, Boolean raimFlag,
			Boolean virtualAid, Boolean assignedMode, String nameExtension) {
		super(AISMessageType.AidToNavigationReport, repeatIndicator, sourceMmsi);
		this.aidType = aidType;
		this.name = name;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.second = second;
		this.offPosition = offPosition;
		this.regionalUse = regionalUse;
		this.raimFlag = raimFlag;
		this.virtualAid = virtualAid;
		this.assignedMode = assignedMode;
		this.nameExtension = nameExtension;
	}

	public final AidType getAidType() {
		return aidType;
	}

	public final String getName() {
		return name;
	}

	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
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

	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}

	public final Integer getSecond() {
		return second;
	}

	public final Boolean getOffPosition() {
		return offPosition;
	}

	public final String getRegionalUse() {
		return regionalUse;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	public final Boolean getVirtualAid() {
		return virtualAid;
	}

	public final Boolean getAssignedMode() {
		return assignedMode;
	}

	public final String getNameExtension() {
		return nameExtension;
	}

	public static AidToNavigationReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.AidToNavigationReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
		
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		AidType aidType = AidType.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 43)));
		String name = Decoder.convertToString(encodedMessage.getBits(43, 163));
		Boolean positionAccurate = Decoder.convertToBoolean(encodedMessage.getBits(163, 164));
		Float longitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(164, 192)) / 10000);
		Float latitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(192, 219)) / 10000);
		Integer toBow = Decoder.convertToUnsignedInteger(encodedMessage.getBits(219, 228));
		Integer toStern = Decoder.convertToUnsignedInteger(encodedMessage.getBits(228, 237));
		Integer toPort = Decoder.convertToUnsignedInteger(encodedMessage.getBits(237, 243));
		Integer toStarboard = Decoder.convertToUnsignedInteger(encodedMessage.getBits(243, 249));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(249, 253)));
		Integer second = Decoder.convertToUnsignedInteger(encodedMessage.getBits(253, 259));
		Boolean offPosition = Decoder.convertToBoolean(encodedMessage.getBits(259, 260));
		String regionalUse = Decoder.convertToBitString(encodedMessage.getBits(260,268));
		Boolean raimFlag = Decoder.convertToBoolean(encodedMessage.getBits(268, 269));
		Boolean virtualAid = Decoder.convertToBoolean(encodedMessage.getBits(269, 270));
		Boolean assignedMode = Decoder.convertToBoolean(encodedMessage.getBits(270, 271));
		String nameExtension = Decoder.convertToString(encodedMessage.getBits(272, 361));

		return new AidToNavigationReport(repeatIndicator, sourceMmsi, aidType,
				name, positionAccurate, latitude, longitude, toBow, toStern,
				toStarboard, toPort, positionFixingDevice, second, offPosition,
				regionalUse, raimFlag, virtualAid, assignedMode, nameExtension);
	}
	
	private final AidType aidType;
	private final String name;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final Integer second;
	private final Boolean offPosition;
	private final String regionalUse;
	private final Boolean raimFlag;
	private final Boolean virtualAid;
	private final Boolean assignedMode;
	private final String nameExtension;
}
