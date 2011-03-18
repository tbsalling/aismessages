package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.ShipType;

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
	
	public static ClassBCSStaticDataReport fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ClassBCSStaticDataReport))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer partNumber = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 40));

		String shipName = partNumber == 1 ? null : Decoder.convertToString(encodedMessage.getBits(40, 160));
		ShipType shipType = partNumber == 0 ? null : ShipType.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(40, 48)));
		String vendorId = partNumber == 0 ? null : Decoder.convertToString(encodedMessage.getBits(48, 90));
		String callsign = partNumber == 0 ? null : Decoder.convertToString(encodedMessage.getBits(90, 132));
		Integer toBow = partNumber == 0 ? null : Decoder.convertToUnsignedInteger(encodedMessage.getBits(132, 141));
		Integer toStern = partNumber == 0 ? null : Decoder.convertToUnsignedInteger(encodedMessage.getBits(141, 150));
		Integer toPort = partNumber == 0 ? null : Decoder.convertToUnsignedInteger(encodedMessage.getBits(150, 156));
		Integer toStarboard = partNumber == 0 ? null : Decoder.convertToUnsignedInteger(encodedMessage.getBits(156, 162));
		MMSI mothershipMmsi = partNumber == 0 ? null : MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(132, 162)));
		
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
