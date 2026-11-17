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
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 19: extended Class B equipment position report (ITU-R M.1371-6 § A7-3.17, Table 69). Legacy Class B
 * message combining a position report with static data (name, type, dimensions), transmitted every 6 min in two slots
 * allocated through Message 18's ITDMA communication state. M.1371-6 states that future equipment does not need this
 * message, since Message 18 together with Messages 24A and 24B carries the same content. 312 bits.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedClassBEquipmentPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ExtendedClassBEquipmentPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                    int regionalReserved1, float speedOverGround, SpeedOverGroundIndicator speedOverGroundIndicator, boolean positionAccuracy,
                                                    double latitude, double longitude, float courseOverGround, Integer trueHeading,
                                                    Integer second, TimeStampIndicator timeStampIndicator, int regionalReserved2, String shipName, ShipType shipType,
                                                    int toBow, int toStern, int toPort, int toStarboard,
                                                    PositionFixingDevice positionFixingDevice, boolean raimFlag,
                                                    boolean dataTerminalReady, boolean assigned, int regionalReserved3) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.regionalReserved1 = regionalReserved1;
        this.speedOverGround = speedOverGround;
        this.speedOverGroundIndicator = speedOverGroundIndicator;
        this.positionAccuracy = positionAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseOverGround = courseOverGround;
        this.trueHeading = trueHeading;
        this.second = second;
        this.timeStampIndicator = timeStampIndicator;
        this.regionalReserved2 = regionalReserved2;
        this.shipName = shipName;
        this.shipType = shipType;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toPort = toPort;
        this.toStarboard = toStarboard;
        this.positionFixingDevice = positionFixingDevice;
        this.raimFlag = raimFlag;
        this.dataTerminalReady = dataTerminalReady;
        this.assigned = assigned;
        this.regionalReserved3 = regionalReserved3;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 312)
            errorMessage.append(format("Message of type %s should be at exactly 312 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.ExtendedClassBEquipmentPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    /** Bits 38-46 (Table 69) as an unsigned integer, first bit most significant: 8 spare bits, should be zero. */
    int regionalReserved1;
    /** Speed over ground in knots, transmitted in 1/10 knot (Table 69); 102.2 when 102.2 knots or higher and {@code NaN} when not available, as told by {@code speedOverGroundIndicator}. */
    float speedOverGround;
    /** Meaning of {@code speedOverGround}: measured, at least the field's maximum, or not available (see {@link SpeedOverGroundIndicator}). */
    SpeedOverGroundIndicator speedOverGroundIndicator;
    /** Bit 56 (Table 69): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccuracy;
    /** Latitude in decimal degrees, transmitted in 1/10 000 min (Table 69); north positive; {@code NaN} when not available (transmitted as 91°). */
    double latitude;
    /** Longitude in decimal degrees, transmitted in 1/10 000 min (Table 69); east positive; {@code NaN} when not available (transmitted as 181°). */
    double longitude;
    /** Course over ground in degrees, transmitted in 1/10° (Table 69); {@code NaN} when not available (transmitted as 360°). */
    float courseOverGround;
    /** Bits 124-133 (Table 69): true heading in degrees, 0-359; null when not available. */
    Integer trueHeading;
    /** Bits 133-139 (Table 69): UTC second when the report was generated, 0-59; null unless {@code timeStampIndicator} is {@link TimeStampIndicator#Available}. */
    Integer second;
    /** What the time stamp field carries: the UTC second in {@code second}, or the state of the position fixing system (see {@link TimeStampIndicator}). */
    TimeStampIndicator timeStampIndicator;
    /** Bits 139-143 (Table 69) as an unsigned integer, first bit most significant: 4 spare bits, should be zero. */
    int regionalReserved2;
    /** Bits 143-263 (Table 69): name, up to 20 six-bit ASCII characters (Table 45); null when not available. */
    String shipName;
    /** Bits 263-271 (Table 69, Table 51): type of ship and cargo type; null for the regional (100-199) and future-use (200-255) codes. */
    ShipType shipType;
    /** Bits 271-280 (Table 69, Fig. 38): dimension A, distance from the reference point to the bow in metres, 0-511. */
    int toBow;
    /** Bits 280-289 (Table 69, Fig. 38): dimension B, distance from the reference point to the stern in metres, 0-511. */
    int toStern;
    /** Bits 295-301 (Table 69, Fig. 38): dimension D, distance from the reference point to the starboard side in metres, 0-63. */
    int toStarboard;
    /** Bits 289-295 (Table 69, Fig. 38): dimension C, distance from the reference point to the port side in metres, 0-63. */
    int toPort;
    /** Bits 301-305 (Table 69): type of electronic position fixing device. */
    PositionFixingDevice positionFixingDevice;
    /** Bit 305 (Table 69): RAIM flag of the electronic position fixing device, true = RAIM in use; see Table 48. */
    boolean raimFlag;
    /** Bit 306 (Table 69, § A7-3.3.1): data terminal equipment ready, true = available (bit 0), false = not available (bit 1, default). */
    boolean dataTerminalReady;
    /** Bit 307 (Table 69): assigned mode flag, true = station operating in assigned mode, false = autonomous and continuous mode (default). */
    boolean assigned;
    /** Bits 308-312 (Table 69) as an unsigned integer, first bit most significant: 4 spare bits, should be zero. */
    int regionalReserved3;
}
