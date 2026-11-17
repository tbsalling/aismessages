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

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.SpeedOverGroundIndicator;
import dk.tbsalling.aismessages.ais.messages.types.CommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 9: standard SAR aircraft position report (ITU-R M.1371-6 § A7-3.7, Table 57). Transmitted by aircraft
 * stations in the maritime mobile service involved in search and rescue operations, every 10 s (2 s when changing
 * course, speed or altitude, Table 2). 168 bits; SOTDMA, RATDMA or ITDMA access with a SOTDMA or ITDMA communication
 * state selected by a flag (Table 44).
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StandardSARAircraftPositionReport extends AISMessage implements DynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected StandardSARAircraftPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                int altitude, float speedOverGround, SpeedOverGroundIndicator speedOverGroundIndicator, boolean positionAccuracy,
                                                double latitude, double longitude, float courseOverGround,
                                                int second, int regionalReserved, boolean dataTerminalReady,
                                                boolean assigned, boolean raimFlag, int radioStatus,
                                                boolean barometricAltitude, boolean commStateSelectorFlag, CommunicationState communicationState) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.altitude = altitude;
        this.speedOverGround = speedOverGround;
        this.speedOverGroundIndicator = speedOverGroundIndicator;
        this.positionAccuracy = positionAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseOverGround = courseOverGround;
        this.second = second;
        this.regionalReserved = regionalReserved;
        this.dataTerminalReady = dataTerminalReady;
        this.assigned = assigned;
        this.raimFlag = raimFlag;
        this.radioStatus = radioStatus;
        this.barometricAltitude = barometricAltitude;
        this.commStateSelectorFlag = commStateSelectorFlag;
        this.communicationState = communicationState;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 168)
            errorMessage.append(format("Message of type %s should be at exactly 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.StandardSARAircraftPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.SAR;
    }

    /** Bits 38-50 (Table 57): altitude in metres from GNSS or barometric sensor (see {@code barometricAltitude}), 0-4094; 4094 = 4094 m or higher, 4095 = not available. */
    int altitude;
    /** Speed over ground in knots, transmitted in 1-knot steps (Table 57); 1022 when 1 022 knots or higher and {@code NaN} when not available, as told by {@code speedOverGroundIndicator}. */
    float speedOverGround;
    /** Meaning of {@code speedOverGround}: measured, at least the field's maximum, or not available (see {@link SpeedOverGroundIndicator}). */
    SpeedOverGroundIndicator speedOverGroundIndicator;
    /** Bit 60 (Table 57): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccuracy;
    /** Latitude in decimal degrees, transmitted in 1/10 000 min (Table 57); north positive; {@code NaN} when not available (transmitted as 91°). */
    double latitude;
    /** Longitude in decimal degrees, transmitted in 1/10 000 min (Table 57); east positive; {@code NaN} when not available (transmitted as 181°). */
    double longitude;
    /** Course over ground in degrees, transmitted in 1/10° (Table 57); {@code NaN} when not available (transmitted as 360°). */
    float courseOverGround;
    /** Bits 128-134 (Table 57): UTC second when the report was generated, 0-59; 60 = not available (default), 61 = manual input, 62 = dead reckoning, 63 = EPFS inoperative. */
    int second;
    /** Bits 134-142 (Table 57) as an unsigned integer, first bit most significant: the altitude sensor bit followed by 7 spare bits. See {@code barometricAltitude} for the typed value. */
    int regionalReserved;
    /** Bit 142 (Table 57, § A7-3.3.1): data terminal equipment ready, true = available (bit 0), false = not available (bit 1, default). */
    boolean dataTerminalReady;
    /** Bit 146 (Table 57): assigned mode flag, true = station operating in assigned mode, false = autonomous and continuous mode (default). */
    boolean assigned;
    /** Bit 147 (Table 57): RAIM flag of the electronic position fixing device, true = RAIM in use; see Table 48. */
    boolean raimFlag;
    /** Bits 148-168 (Table 57) as an unsigned integer, first bit most significant: the communication state selector flag followed by the 19-bit communication state. See {@code communicationState} for the typed value. */
    int radioStatus;
    /** Altitude sensor, bit 134 (ITU-R M.1371-6 Table 57): false = GNSS, true = barometric source. */
    boolean barometricAltitude;
    /** Communication state selector flag, bit 148: false = SOTDMA communication state follows, true = ITDMA. */
    boolean commStateSelectorFlag;
    /** The communication state in bits 149-168, typed according to {@link #isCommStateSelectorFlag()}. */
    CommunicationState communicationState;

}
