/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

/**
 * 
 */
package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

/**
 * @author tbsalling
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositionReportClassAScheduled extends PositionReport {
    protected PositionReportClassAScheduled(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                            NavigationStatus navigationStatus, int rateOfTurn, float speedOverGround,
                                            boolean positionAccuracy, float latitude, float longitude,
                                            float courseOverGround, int trueHeading, int second,
                                            ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState,
                                            int rawRateOfTurn, int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround) {
        super(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                navigationStatus, rateOfTurn, speedOverGround, positionAccuracy, latitude, longitude,
                courseOverGround, trueHeading, second, specialManeuverIndicator, raimFlag, communicationState,
                rawRateOfTurn, rawSpeedOverGround, rawLatitude, rawLongitude, rawCourseOverGround);
    }

    @Override
    public AISMessageType getMessageType() {
        return AISMessageType.PositionReportClassAScheduled;
    }
}
