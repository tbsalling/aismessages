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

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClassBCSStaticDataReport extends AISMessage implements StaticDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ClassBCSStaticDataReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                       int partNumber, String shipName, ShipType shipType,
                                       String vendorId, String callsign,
                                       int toBow, int toStern, int toStarboard, int toPort,
                                       MMSI mothershipMmsi, String manufacturerMnemonic, int unitModelCode, int unitSerialNumber,
                                       PositionFixingDevice positionFixingDevice, int vdesCapabilities) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
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

    /** 0 = Part A (name), 1 = Part B (static data). Fields of the other part are null or -1. */
    int partNumber;
    String shipName;
    ShipType shipType;
    /** The 42-bit manufacturer ID of Part B rendered as 7 six-bit characters; see {@link #getManufacturerMnemonic()} for the structured form. */
    String vendorId;
    String callsign;
    int toBow;
    int toStern;
    int toStarboard;
    int toPort;
    /**
     * The 30 bits of the Part B dimension fields read as one MMSI, following the convention of some auxiliary craft
     * transponders. Not part of ITU-R M.1371-6, which instead has craft associated with a parent vessel use a call sign
     * of "A" followed by the last 6 digits of the parent's MMSI. Null when the dimension bits are all zero or for Part A.
     */
    MMSI mothershipMmsi;
    /** Part B, bits 48-66 (ITU-R M.1371-6 Table 78): the manufacturer's three-character NMEA mnemonic code. Null for Part A. */
    String manufacturerMnemonic;
    /** Part B, bits 66-70 (Table 78): unit model code 1-15 (0 not used). -1 for Part A. */
    int unitModelCode;
    /** Part B, bits 70-90 (Table 78): manufacturer-traceable unit serial number. -1 for Part A. */
    int unitSerialNumber;
    /** Part B, bits 162-166 (Table 77): type of electronic position fixing device. Null for Part A. */
    PositionFixingDevice positionFixingDevice;
    /**
     * Part B, bits 166-168 (Table 77): 0 = AIS only, 1 = supports VDES ASM, 2 = supports VDES ASM/VDE-TER,
     * 3 = supports VDES ASM/VDE-TER/VDE-SAT. -1 for Part A.
     */
    int vdesCapabilities;

}
