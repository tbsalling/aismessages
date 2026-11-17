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
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 21: aids-to-navigation report (ITU-R M.1371-6 § A7-3.19, Table 71). Transmitted by AIS AtoN stations
 * (physical, virtual or integrated into a base station) every 3 min, when assigned, or when a parameter changes, to
 * report the position and status of an aid to navigation. 272 to 360 bits in two slots, the variable part being the
 * name extension. Dimensions follow § A7-3.19.1: floating aids report a circle (A = B = C = D), virtual aids report zero.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AidToNavigationReport extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AidToNavigationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                    AidType aidType, String name, boolean positionAccurate,
                                    float latitude, float longitude,
                                    int toBow, int toStern, int toPort, int toStarboard,
                                    PositionFixingDevice positionFixingDevice, int second,
                                    boolean offPosition, int regionalUse, boolean raimFlag,
                                    boolean virtualAid, boolean assignedMode, int spare1,
                                    String nameExtension, Integer spare2, boolean nameExtensionLabel) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.aidType = aidType;
        this.name = name;
        this.positionAccurate = positionAccurate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toPort = toPort;
        this.toStarboard = toStarboard;
        this.positionFixingDevice = positionFixingDevice;
        this.second = second;
        this.offPosition = offPosition;
        this.regionalUse = regionalUse;
        this.raimFlag = raimFlag;
        this.virtualAid = virtualAid;
        this.assignedMode = assignedMode;
        this.spare1 = spare1;
        this.nameExtension = nameExtension;
        this.spare2 = spare2;
        this.nameExtensionLabel = nameExtensionLabel;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 272 || numberOfBits > 360)
            errorMessage.append(format("Message of type %s should be 272-360 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AidToNavigationReport;
    }

    /** Bits 38-43 (Table 71, Table 72): type of aid to navigation, 0 = not available (default). */
    AidType aidType;
    /** Bits 43-163 (Table 71): name of the aid to navigation, up to 20 six-bit ASCII characters (Table 45); all "@" = not available. Extended by {@code nameExtension}. */
    String name;
    /** Bit 163 (Table 71): position accuracy flag, true = high (≤ 10 m), false = low (&gt; 10 m, default); see Table 48. */
    boolean positionAccurate;
    /** Bits 192-219 (Table 71): latitude in decimal degrees (1/10 000 min / 600 000); north positive; 91 = not available. */
    float latitude;
    /** Bits 164-192 (Table 71): longitude in decimal degrees (1/10 000 min / 600 000); east positive; 181 = not available. */
    float longitude;
    /** Bits 219-228 (Table 71, Fig. 39, § A7-3.19.1): dimension A in metres, 0-511; points to true north for fixed aids; A = B = C = D for floating aids; 1 for objects ≤ 2 m × 2 m; 0 for virtual aids. */
    int toBow;
    /** Bits 228-237 (Table 71, Fig. 39): dimension B in metres, 0-511. */
    int toStern;
    /** Bits 237-243 (Table 71, Fig. 39): dimension C in metres, 0-63. */
    int toPort;
    /** Bits 243-249 (Table 71, Fig. 39): dimension D in metres, 0-63. */
    int toStarboard;
    /** Bits 249-253 (Table 71): type of electronic position fixing device; 7 = surveyed position is typical for fixed aids. */
    PositionFixingDevice positionFixingDevice;
    /** Bits 253-259 (Table 71): UTC second when the report was generated, 0-59; 60 = not available (default), 61 = manual input, 62 = dead reckoning, 63 = EPFS inoperative. */
    int second;
    /** Bit 259 (Table 71): off-position indicator, true = off position (floating aid outside its zone, or fixed aid with suspected GNSS anomaly); only meaningful when {@code second} ≤ 59. */
    boolean offPosition;
    /** Bits 260-268 (Table 71) as an unsigned integer, first bit most significant: the 8 AtoN status bits reserved for IALA Recommendation R0126; all zero = default. */
    int regionalUse;
    /** Bit 268 (Table 71): RAIM flag of the electronic position fixing device, true = RAIM in use; see Table 48. */
    boolean raimFlag;
    /** Bit 269 (Table 71): virtual AtoN flag, true = virtual aid that does not physically exist, false = physical aid at the indicated position (default). */
    boolean virtualAid;
    /** Bit 270 (Table 71): assigned mode flag, true = station operating in assigned mode, false = autonomous and continuous mode (default). */
    boolean assignedMode;
    /** Bit 271 (Table 71): spare, should be zero. */
    int spare1;
    /**
     * Bits 272 to end (Table 71): up to 14 additional six-bit characters extending {@link #getName()}, or, when {@link #isNameExtensionIsLabel()}
     * is true, the AtoN's label (e.g. "LB1" for Lighted Buoy 1) with the leading "@@@" marker removed. Null when absent.
     */
    String nameExtension;
    /** Bits after the name extension (Table 71): 0, 2, 4 or 6 spare bits inserted to reach a byte boundary; 0 when no extension is present. */
    int spare2;
    /**
     * True when the name extension was preceded by "@@@", which per ITU-R M.1371-6 Table 71 means the text is to be
     * portrayed as the AtoN's label rather than as part of its name.
     */
    boolean nameExtensionLabel;
}
