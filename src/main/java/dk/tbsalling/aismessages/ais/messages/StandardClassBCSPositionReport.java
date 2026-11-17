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
 * Message 18: standard Class B equipment position report (ITU-R M.1371-6 § A7-3.16, Table 68). Transmitted by Class B
 * "SO" and Class B "CS" shipborne stations instead of Messages 1, 2 and 3, at the intervals of Table 2 (3 min at
 * ≤ 2 knots, otherwise 30, 15 or 5 s). 168 bits; omits navigational status and rate of turn, adds capability flags.
 * Class B "CS" stations always set the communication state selector to ITDMA with the fixed value
 * 1100000000000000110 (§ A6-4.3.3.5).
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StandardClassBCSPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected StandardClassBCSPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                             String regionalReserved1, float speedOverGround,
                                             boolean positionAccuracy, float latitude, float longitude,
                                             float courseOverGround, int trueHeading, int second,
                                             String regionalReserved2, boolean csUnit, boolean display, boolean dsc, boolean band,
                                             boolean message22, boolean assigned, boolean raimFlag, boolean commStateSelectorFlag,
                                             CommunicationState communicationState,
                                             int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround, boolean transmitPowerLow) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.regionalReserved1 = regionalReserved1;
        this.speedOverGround = speedOverGround;
        this.positionAccuracy = positionAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseOverGround = courseOverGround;
        this.trueHeading = trueHeading;
        this.second = second;
        this.regionalReserved2 = regionalReserved2;
        this.csUnit = csUnit;
        this.display = display;
        this.dsc = dsc;
        this.band = band;
        this.message22 = message22;
        this.assigned = assigned;
        this.raimFlag = raimFlag;
        this.commStateSelectorFlag = commStateSelectorFlag;
        this.communicationState = communicationState;
        this.rawSpeedOverGround = rawSpeedOverGround;
        this.rawLatitude = rawLatitude;
        this.rawLongitude = rawLongitude;
        this.rawCourseOverGround = rawCourseOverGround;
        this.transmitPowerLow = transmitPowerLow;
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
        return AISMessageType.StandardClassBCSPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    /** Bits 38-46 (Table 68) as a string of '0'/'1': 8 spare bits, should be zero. */
    String regionalReserved1;
    /** Speed over ground in knots, {@code rawSpeedOverGround} / 10 (Table 68); 102.2 = 102.2 knots or higher, 102.3 = not available. */
    float speedOverGround;
    /** Bit 56 (Table 68): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccuracy;
    /** Latitude in decimal degrees, {@code rawLatitude} / 600 000 (Table 68); north positive; 91 = not available. */
    float latitude;
    /** Longitude in decimal degrees, {@code rawLongitude} / 600 000 (Table 68); east positive; 181 = not available. */
    float longitude;
    /** Course over ground in degrees, {@code rawCourseOverGround} / 10 (Table 68); 360 = not available. */
    float courseOverGround;
    /** Bits 124-133 (Table 68): true heading in degrees, 0-359; 511 = not available. */
    int trueHeading;
    /** Bits 133-139 (Table 68): UTC second when the report was generated, 0-59; 60 = not available (default), 61 = manual input, 62 = dead reckoning, 63 = EPFS inoperative (61-63 not used by Class B "CS"). */
    int second;
    /** Bits 139-141 (Table 68) as a string of '0'/'1': the transmit power flag (see {@code transmitPowerLow}) followed by one spare bit. */
    String regionalReserved2;
    /** Bit 141 (Table 68): Class B unit flag, true = Class B "CS" (carrier sense) unit, false = Class B "SO" (SOTDMA) unit. */
    boolean csUnit;
    /** Bit 142 (Table 68): Class B display flag, true = equipped with an integrated display showing Messages 12 and 14, false = no display. */
    boolean display;
    /** Bit 143 (Table 68): Class B DSC flag, true = equipped with a DSC function (dedicated or time-shared). */
    boolean dsc;
    /** Bit 144 (Table 68): Class B band flag, true = capable of operating over the whole marine band, false = upper 525 kHz only; irrelevant when {@code message22} is false. */
    boolean band;
    /** Bit 145 (Table 68): Class B Message 22 flag, true = frequency management via Message 22 supported, false = operating on AIS 1 and AIS 2 only. */
    boolean message22;
    /** Bit 146 (Table 68): mode flag, true = station operating in assigned mode, false = autonomous and continuous mode (default). */
    boolean assigned;
    /** Bit 147 (Table 68): RAIM flag of the electronic position fixing device, true = RAIM in use; see Table 48. */
    boolean raimFlag;
    /** Bit 148 (Table 68): communication state selector flag, false = SOTDMA communication state follows, true = ITDMA (always true for Class B "CS"). */
    boolean commStateSelectorFlag;
    /** Bits 149-168 (Table 68): SOTDMA (Table 18) or ITDMA (Table 20) communication state according to {@code commStateSelectorFlag}. */
    CommunicationState communicationState;
    /** Bits 46-56 (Table 68): speed over ground in 1/10 knot steps, 0-1022; 1022 = 102.2 knots or higher, 1023 = not available. */
    int rawSpeedOverGround;
    /** Bits 85-112 (Table 68): latitude in 1/10 000 min, two's complement; 91° (0x3412140) = not available. */
    int rawLatitude;
    /** Bits 57-85 (Table 68): longitude in 1/10 000 min, two's complement; 181° (0x6791AC0) = not available. */
    int rawLongitude;
    /** Bits 112-124 (Table 68): course over ground in 1/10 degree, 0-3599; 3600 = not available. */
    int rawCourseOverGround;
    /** Transmit power flag at bit 139 (ITU-R M.1371-6 Table 68): false = high power (default), true = low power. */
    boolean transmitPowerLow;

}
