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

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.BitString;

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

/**
 * Message 3: special position report of a Class A station (ITU-R M.1371-6 § A7-3.1, Table 46). Sent in response to an
 * interrogation (Message 15), during network entry and the first frame phase, and when the reporting interval changes
 * (§ A2-3.3.5); transmitted with RATDMA, ITDMA or SOTDMA access and carries an ITDMA communication state (Table 44).
 * See {@link PositionReport} for the field layout.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositionReportClassAResponseToInterrogation extends PositionReport {
    protected PositionReportClassAResponseToInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                          NavigationStatus navigationStatus, float rateOfTurn, RateOfTurnIndicator rateOfTurnIndicator, float speedOverGround, SpeedOverGroundIndicator speedOverGroundIndicator,
                                                          boolean positionAccuracy, double latitude, double longitude,
                                                          float courseOverGround, int trueHeading, int second,
                                                          ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState, boolean transmitPowerLow) {
        super(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                navigationStatus, rateOfTurn, rateOfTurnIndicator, speedOverGround, speedOverGroundIndicator, positionAccuracy, latitude, longitude,
                courseOverGround, trueHeading, second, specialManeuverIndicator, raimFlag, communicationState, transmitPowerLow);
    }

    @Override
    public AISMessageType getMessageType() {
        return AISMessageType.PositionReportClassAResponseToInterrogation;
    }
}
