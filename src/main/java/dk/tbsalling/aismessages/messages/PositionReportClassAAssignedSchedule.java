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
import dk.tbsalling.aismessages.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.messages.types.NavigationStatus;

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
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		NavigationStatus navigationStatus = NavigationStatus.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(38, 42)));
		Integer rateOfTurn = DecoderImpl.convertToSignedInteger(encodedMessage.getBits(42, 50));
		Float speedOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(50, 60)) / 10f;
		Boolean positionAccurate = DecoderImpl.convertToBoolean(encodedMessage.getBits(60, 61));
		Float longitude = DecoderImpl.convertToFloat(encodedMessage.getBits(61, 89)) / 600000f;
		Float latitude = DecoderImpl.convertToFloat(encodedMessage.getBits(89, 116)) / 600000f;
		Float courseOverGround = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(116, 128)) / 10f;
		Integer trueHeading = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		Integer second = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(137, 143));
		ManeuverIndicator maneuverIndicator = ManeuverIndicator.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Boolean raimFlag = DecoderImpl.convertToBoolean(encodedMessage.getBits(148, 149));
		
		return new PositionReportClassAAssignedSchedule(AISMessageType.PositionReportClassAAssignedSchedule, repeatIndicator,
				sourceMmsi, navigationStatus, rateOfTurn, speedOverGround,
				positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, maneuverIndicator, raimFlag);
	}
}
