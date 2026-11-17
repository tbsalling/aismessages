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
import dk.tbsalling.aismessages.ais.messages.types.TimeStampIndicator;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.AtoNDimensionsType;
import dk.tbsalling.aismessages.ais.messages.types.AtoNOnStationStatus;
import dk.tbsalling.aismessages.ais.messages.types.AtoNStationType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.MobileAtoNCourseIndicator;
import dk.tbsalling.aismessages.ais.messages.types.RestrictedUseIndicator;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Message 28: aid-to-navigation report, single-slot message (ITU-R M.1371-6 Table 84).
 * <p>
 * Provides similar information as Message 21 in one slot instead of two, and can additionally report a mobile AtoN's
 * direction and speed, or extended information about the AtoN (such as its height) and what it is marking (such as a
 * hazardous area). {@code getDimensionsType()} tells which of the decoded dimension getters apply; the others are
 * null. It may be accompanied by a Message 24 Part A carrying the charted name of the AtoN.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AidToNavigationReportSingleSlot extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AidToNavigationReportSingleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                              Integer second, TimeStampIndicator timeStampIndicator, double latitude, double longitude,
                                              RestrictedUseIndicator restrictedUse, AtoNStationType stationType, AidType aidType, int ialaMrn,
                                              AtoNDimensionsType dimensionsType, int dimensionA, int dimensionB, boolean multipleDimensionTypes,
                                              boolean charted, AtoNOnStationStatus onStationStatus, int atonStatusBits, boolean authenticated) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.second = second;
        this.timeStampIndicator = timeStampIndicator;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restrictedUse = restrictedUse;
        this.stationType = stationType;
        this.aidType = aidType;
        this.ialaMrn = ialaMrn;
        this.dimensionsType = dimensionsType;
        int type = dimensionsType == null ? -1 : dimensionsType.getCode();
        this.dimensionAMetres = type == 0 ? (Integer) dimensionA : null;
        this.dimensionBMetres = type == 0 ? (Integer) dimensionB : null;
        this.radiusMetres = switch (type) {
            case 1 -> dimensionA;
            case 2 -> dimensionB <= 100 ? dimensionA * 100 + dimensionB : null;
            case 5 -> dimensionA <= 99 ? dimensionA + dimensionB * 100 : null;
            default -> null;
        };
        this.heightMetres = type == 1 ? dimensionB / 10f : null;
        this.mobileCourseIndicator = type != 3 ? null : switch (dimensionA) {
            case 360 -> MobileAtoNCourseIndicator.Unreported;
            case 361 -> MobileAtoNCourseIndicator.DynamicallyPositionedOnStation;
            case 362 -> MobileAtoNCourseIndicator.PurposelyAdrift;
            case 363 -> MobileAtoNCourseIndicator.Tethered;
            case 364 -> MobileAtoNCourseIndicator.Unknown;
            default -> dimensionA < 360 ? MobileAtoNCourseIndicator.Reported : null;
        };
        this.mobileCourseOverGround = type == 3 && dimensionA < 360 ? (Integer) dimensionA : null;
        this.mobileSpeedOverGround = type == 3 && dimensionB < 60 ? (Integer) dimensionB : null;
        this.polygonVertexNumber = type == 4 && dimensionA <= 8 ? (Integer) dimensionA : null;
        this.polygonLineCount = type == 4 && dimensionB <= 8 ? (Integer) dimensionB : null;
        this.orientation = type >= 6 && type <= 13 && dimensionA < 360 ? (Integer) dimensionA : null;
        this.lineLengthMetres = type >= 6 && type <= 9 ? (Integer) (dimensionB * 10) : type >= 10 && type <= 13 ? (Integer) (dimensionB * 200) : null;
        this.multipleDimensionTypes = multipleDimensionTypes;
        this.charted = charted;
        this.onStationStatus = onStationStatus;
        this.atonStatusBits = atonStatusBits;
        this.authenticated = authenticated;
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
        return AISMessageType.AidToNavigationReportSingleSlot;
    }

    /** UTC second when the report was generated, 0-59; null unless {@code timeStampIndicator} is {@link TimeStampIndicator#Available}. */
    Integer second;
    /** What the time stamp field carries: the UTC second in {@code second}, or the state of the position fixing system (see {@link TimeStampIndicator}). */
    TimeStampIndicator timeStampIndicator;
    /** Latitude in decimal degrees; {@code NaN} when not available (transmitted as 91°). */
    double latitude;
    /** Longitude in decimal degrees; {@code NaN} when not available (transmitted as 181°). */
    double longitude;
    /** Where the AtoN may be operated. */
    RestrictedUseIndicator restrictedUse;
    /** Type of AIS AtoN station (physical, synthetic, virtual, mobile). */
    AtoNStationType stationType;
    /** Type of aid to navigation (7 bits: Table 72 codes 0-31 and Table 85 codes 32-50); null for reserved or regional codes. */
    AidType aidType;
    /** National identification number of the AtoN's IALA Marine Resource Name (urn:mrn:iala:aton:&lt;country&gt;:&lt;number&gt;); 0 = not available. */
    int ialaMrn;
    /** Which decoded dimension getters apply (Table 84): radius and height, swing circle, mobile AtoN course and speed, polygon, circle, or boundary line and sector. */
    AtoNDimensionsType dimensionsType;
    /** Dimension type 0 (Table 84, see Message 21 and § A7-3.19.1): dimension A in metres; null for other dimension types. */
    Integer dimensionAMetres;
    /** Dimension type 0 (Table 84, see Message 21 and § A7-3.19.1): dimension B in metres; null for other dimension types. */
    Integer dimensionBMetres;
    /**
     * Radius in metres (Table 84): of the structural area (type 1, 511 when 511 m or greater), of the swing circle
     * (type 2) or of the circular area (type 5); null for other dimension types and reserved values.
     */
    Integer radiusMetres;
    /** Dimension type 1 (Table 84): height of the AtoN above sea level in metres, 204.7 when 204.7 m or greater; null for other dimension types. */
    Float heightMetres;
    /** Dimension type 3 (Table 84): meaning of the mobile AtoN's course over ground; null for other dimension types and reserved values. */
    MobileAtoNCourseIndicator mobileCourseIndicator;
    /** Dimension type 3 (Table 84): course over ground of the mobile AtoN in true degrees, 0-359; null unless {@code mobileCourseIndicator} is {@link MobileAtoNCourseIndicator#Reported}. */
    Integer mobileCourseOverGround;
    /** Dimension type 3 (Table 84): speed over ground of the mobile AtoN in knots, 0-59; null when unreported, reserved or for other dimension types. */
    Integer mobileSpeedOverGround;
    /** Dimension type 4 (Table 84): sequence number of this polygon vertex, 1-8, or 0 when this vertex closes the polygon to vertex 1; null for other dimension types and reserved values. */
    Integer polygonVertexNumber;
    /** Dimension type 4 (Table 84): total number of lines in the polygon, 0-8; null for other dimension types and reserved values. */
    Integer polygonLineCount;
    /** Dimension types 6-13 (Table 84): orientation of the boundary line, sector or quadrilateral, clockwise from true north in degrees, 0-359; null for other dimension types and reserved values. */
    Integer orientation;
    /** Dimension types 6-13 (Table 84): length of the line or diagonal in metres (10 m steps for types 6-9, 200 m steps for types 10-13); null for other dimension types. */
    Integer lineLengthMetres;
    /** True when this AtoN broadcasts reports with several dimension types (e.g. physical dimensions plus swing circle, or polygon vertices). */
    boolean multipleDimensionTypes;
    /** True when the AtoN is charted. */
    boolean charted;
    /** Whether the AtoN is on station. */
    AtoNOnStationStatus onStationStatus;
    /** The 8 AtoN status bits reserved for IALA Recommendation R0126; 0 = default. */
    int atonStatusBits;
    /** True when this message has been authenticated per IALA G1192. */
    boolean authenticated;
}
