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
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.Message24Part;
import dk.tbsalling.aismessages.ais.messages.types.VdesCapabilities;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
import java.util.stream.IntStream;

import static java.lang.String.format;

/**
 * Message 24: static data report (ITU-R M.1371-6 § A7-3.22, Tables 76 to 78). Part A (160 bits) carries the name of
 * the MMSI-registered vessel and may be used by any station; Part B (168 bits) carries type of ship, manufacturer ID,
 * call sign, dimensions, EPFD type and VDES capabilities. Class B "CS" and "SO" stations transmit both parts every
 * 6 min, Part B within 1 min of Part A; Class A stations transmit Part B within 12 min of start-up and every 24 h.
 * Which part an instance holds is given by {@code part}; the other part's fields are null, or 0 for the dimensions.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClassBCSStaticDataReport extends AISMessage implements StaticDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ClassBCSStaticDataReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                       Message24Part part, String shipName, ShipType shipType,
                                       String vendorId, String callsign,
                                       int toBow, int toStern, int toStarboard, int toPort,
                                       MMSI mothershipMmsi, String manufacturerMnemonic, Integer unitModelCode, Integer unitSerialNumber,
                                       PositionFixingDevice positionFixingDevice, VdesCapabilities vdesCapabilities) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.part = part;
        this.shipName = shipName;
        this.shipType = shipType;
        this.vendorId = vendorId;
        this.callsign = callsign;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toStarboard = toStarboard;
        this.toPort = toPort;
        this.mothershipMmsi = mothershipMmsi;
        this.manufacturerMnemonic = manufacturerMnemonic;
        this.unitModelCode = unitModelCode;
        this.unitSerialNumber = unitSerialNumber;
        this.positionFixingDevice = positionFixingDevice;
        this.vdesCapabilities = vdesCapabilities;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(158, 160, 166, 168).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 158, 160, 166 or 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.ClassBCSStaticDataReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    /** Bits 38-40 (Tables 76 and 77): which part this report holds. Fields of the other part are null, or 0 for the dimensions. */
    Message24Part part;
    /** Part A, bits 40-160 (Table 76): name of the MMSI-registered vessel, up to 20 six-bit ASCII characters (Table 45); null when not available; "SAR AIRCRAFT NNNNNNN" for SAR aircraft. Null for Part B. */
    String shipName;
    /** Part B, bits 40-48 (Table 77, Table 51): type of ship and cargo type; 0 = not available; not applicable to SAR aircraft. Null for Part A. */
    ShipType shipType;
    /** Part B, bits 48-90 (Table 77): the 42-bit manufacturer ID rendered as 7 six-bit ASCII characters, as older decoders did; see {@code manufacturerMnemonic}, {@code unitModelCode} and {@code unitSerialNumber} for the Table 78 structure. Null for Part A. */
    String vendorId;
    /** Part B, bits 90-132 (Table 77): call sign, 7 six-bit ASCII characters; null when not available. Craft associated with a parent vessel use "A" plus the last 6 digits of the parent's MMSI. Null for Part A. */
    String callsign;
    /** Part B, bits 132-141 (Table 77, Fig. 38): dimension A, distance from the reference point to the bow in metres, 0-511. 0 (not available) for Part A. */
    int toBow;
    /** Part B, bits 141-150 (Table 77, Fig. 38): dimension B, distance from the reference point to the stern in metres, 0-511. 0 (not available) for Part A. */
    int toStern;
    /** Part B, bits 156-162 (Table 77, Fig. 38): dimension D, distance from the reference point to the starboard side in metres, 0-63. 0 (not available) for Part A. */
    int toStarboard;
    /** Part B, bits 150-156 (Table 77, Fig. 38): dimension C, distance from the reference point to the port side in metres, 0-63. 0 (not available) for Part A. */
    int toPort;
    /**
     * The 30 bits of the Part B dimension fields read as one MMSI, following the convention of some auxiliary craft
     * transponders. Not part of ITU-R M.1371-6, which instead has craft associated with a parent vessel use a call sign
     * of "A" followed by the last 6 digits of the parent's MMSI. Null when the dimension bits are all zero or for Part A.
     */
    MMSI mothershipMmsi;
    /** Part B, bits 48-66 (ITU-R M.1371-6 Table 78): the manufacturer's three-character NMEA mnemonic code. Null for Part A. */
    String manufacturerMnemonic;
    /** Part B, bits 66-70 (Table 78): unit model code 1-15 (0 not used). Null for Part A. */
    Integer unitModelCode;
    /** Part B, bits 70-90 (Table 78): manufacturer-traceable unit serial number. Null for Part A. */
    Integer unitSerialNumber;
    /** Part B, bits 162-166 (Table 77): type of electronic position fixing device. Null for Part A. */
    PositionFixingDevice positionFixingDevice;
    /**
     * Part B, bits 166-168 (Table 77): VDES capabilities of the station. Null for Part A.
     */
    VdesCapabilities vdesCapabilities;

}
