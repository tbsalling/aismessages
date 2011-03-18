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

/**
 * 
 */
package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;
import dk.tbsalling.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.ais.messages.types.NavigationStatus;


/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class PositionReportClassAScheduled extends PositionReport {
	
	public PositionReportClassAScheduled(AISMessageType messageType,
			Integer repeatIndicator, MMSI sourceMmsi,
			NavigationStatus navigationStatus, Integer rateOfTurn,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, ManeuverIndicator maneuverIndicator,
			Boolean raimFlag) {
		super(messageType, repeatIndicator, sourceMmsi, navigationStatus, rateOfTurn,
				speedOverGround, positionAccurate, latitude, longitude,
				courseOverGround, trueHeading, second, maneuverIndicator,
				raimFlag);
	}

	public static PositionReportClassAScheduled fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.PositionReportClassAScheduled))
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
		
		return new PositionReportClassAScheduled(AISMessageType.PositionReportClassAScheduled, repeatIndicator,
				sourceMmsi, navigationStatus, rateOfTurn, speedOverGround,
				positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, maneuverIndicator, raimFlag);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PositionReportClassAScheduled [toString()=")
				.append(super.toString()).append("]");
		return builder.toString();
	}

}
