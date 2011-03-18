package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.ais.messages.types.NavigationStatus;

@SuppressWarnings("serial")
public class PositionReportClassAAssignedSchedule extends PositionReport {

	public PositionReportClassAAssignedSchedule(AISMessageType messageType,
			Integer repeatIndicator, MMSI mmsi,
			NavigationStatus navigationStatus, Integer rateOfTurn,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, ManeuverIndicator maneuverIndicator,
			Boolean raimFlag) {
		super(messageType, repeatIndicator, mmsi, navigationStatus, rateOfTurn,
				speedOverGround, positionAccurate, latitude, longitude,
				courseOverGround, trueHeading, second, maneuverIndicator,
				raimFlag);
	}

	public static PositionReportClassAAssignedSchedule fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.PositionReportClassAAssignedSchedule))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		NavigationStatus navigationStatus = NavigationStatus.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 42)));
		Integer rateOfTurn = Decoder.convertToSignedInteger(encodedMessage.getBits(42, 50));
		Float speedOverGround = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(50, 60)) / 10);
		Boolean positionAccurate = Decoder.convertToBoolean(encodedMessage.getBits(60, 61));
		Float longitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(61, 89)) / 10000);
		Float latitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(89, 116)) / 10000);
		Float courseOverGround = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(116, 128)) / 10);
		Integer trueHeading = Decoder.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		Integer second = Decoder.convertToUnsignedInteger(encodedMessage.getBits(137, 143));
		ManeuverIndicator maneuverIndicator = ManeuverIndicator.fromInteger(Decoder.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Boolean raimFlag = Decoder.convertToBoolean(encodedMessage.getBits(148, 149));
		
		return new PositionReportClassAAssignedSchedule(AISMessageType.PositionReportClassAAssignedSchedule, repeatIndicator,
				sourceMmsi, navigationStatus, rateOfTurn, speedOverGround,
				positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, maneuverIndicator, raimFlag);
	}
}
