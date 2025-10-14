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

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.*;
import static java.lang.String.format;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 *
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class AidToNavigationReport extends AISMessage {

    protected AidToNavigationReport(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Eagerly decode all fields
        this.aidType = AidType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(38, 43)));
        this.name = STRING_DECODER.apply(getBits(43, 163));
        this.positionAccurate = BOOLEAN_DECODER.apply(getBits(163, 164));
        this.longitude = FLOAT_DECODER.apply(getBits(164, 192)) / 600000f;
        this.latitude = FLOAT_DECODER.apply(getBits(192, 219)) / 600000f;
        this.toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(219, 228));
        this.toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(228, 237));
        this.toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(237, 243));
        this.toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(243, 249));
        this.positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(249, 253)));
        this.second = UNSIGNED_INTEGER_DECODER.apply(getBits(253, 259));
        this.offPosition = BOOLEAN_DECODER.apply(getBits(259, 260));
        this.regionalUse = BIT_DECODER.apply(getBits(260, 268));
        this.raimFlag = BOOLEAN_DECODER.apply(getBits(268, 269));
        this.virtualAid = BOOLEAN_DECODER.apply(getBits(269, 270));
        this.assignedMode = BOOLEAN_DECODER.apply(getBits(270, 271));
        this.spare1 = UNSIGNED_INTEGER_DECODER.apply(getBits(271, 272));

        // Optional fields for messages >= 272 bits
        if (getNumberOfBits() >= 272) {
            int extraBits = getNumberOfBits() - 272;
            int extraChars = extraBits / 6;
            int extraBitsOfChars = extraChars * 6;
            if (extraBits > 0) {
                this.nameExtension = STRING_DECODER.apply(getBits(272, 272 + extraBitsOfChars));
                this.spare2 = (extraBits == extraBitsOfChars) ? 0 : UNSIGNED_INTEGER_DECODER.apply(getBits(272 + extraBitsOfChars, getNumberOfBits()));
            } else {
                // Exactly 272 bits - no extension
                this.nameExtension = null;
                this.spare2 = 0;
            }
        } else {
            this.nameExtension = null;
            this.spare2 = null;
        }
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AidToNavigationReport;
    }

    @SuppressWarnings("unused")
    public AidType getAidType() {
        return aidType;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public Boolean getPositionAccurate() {
        return positionAccurate;
    }

    @SuppressWarnings("unused")
    public Float getLatitude() {
        return latitude;
    }

    @SuppressWarnings("unused")
    public Float getLongitude() {
        return longitude;
    }

    @SuppressWarnings("unused")
    public Integer getToBow() {
        return toBow;
    }

    @SuppressWarnings("unused")
    public Integer getToStern() {
        return toStern;
    }

    @SuppressWarnings("unused")
    public Integer getToStarboard() {
        return toStarboard;
    }

    @SuppressWarnings("unused")
    public Integer getToPort() {
        return toPort;
    }

    @SuppressWarnings("unused")
    public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
    }

    @SuppressWarnings("unused")
    public Integer getSecond() {
        return second;
    }

    @SuppressWarnings("unused")
    public Boolean getOffPosition() {
        return offPosition;
    }

    @SuppressWarnings("unused")
    public String getAtoNStatus() {
        return regionalUse;
    }

    @SuppressWarnings("unused")
    public Boolean getRaimFlag() {
        return raimFlag;
    }

    @SuppressWarnings("unused")
    public Boolean getVirtualAid() {
        return virtualAid;
    }

    @SuppressWarnings("unused")
    public Boolean getAssignedMode() {
        return assignedMode;
    }

    @SuppressWarnings("unused")
    public Integer getSpare1() {
        return spare1;
    }

    @SuppressWarnings("unused")
    public String getNameExtension() {
        return nameExtension;
    }

    @SuppressWarnings("unused")
    public Integer getSpare2() {
        return spare2;
    }

    @Override
    public String toString() {
        return "AidToNavigationReport{" +
                "messageType=" + getMessageType() +
                ", aidType=" + getAidType() +
                ", name='" + getName() + '\'' +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toPort=" + getToPort() +
                ", toStarboard=" + getToStarboard() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", second=" + getSecond() +
                ", offPosition=" + getOffPosition() +
                ", regionalUse='" + getAtoNStatus() + '\'' +
                ", raimFlag=" + getRaimFlag() +
                ", virtualAid=" + getVirtualAid() +
                ", assignedMode=" + getAssignedMode() +
                ", spare1=" + getSpare1() +
                ", nameExtension='" + getNameExtension() + '\'' +
                ", spare2=" + getSpare2() +
                "} " + super.toString();
    }

    private final AidType aidType;
    private final String name;
    private final Boolean positionAccurate;
    private final Float latitude;
    private final Float longitude;
    private final Integer toBow;
    private final Integer toStern;
    private final Integer toPort;
    private final Integer toStarboard;
    private final PositionFixingDevice positionFixingDevice;
    private final Integer second;
    private final Boolean offPosition;
    private final String regionalUse;
    private final Boolean raimFlag;
    private final Boolean virtualAid;
    private final Boolean assignedMode;
    private final Integer spare1;
    private final String nameExtension;
    private final Integer spare2;
}
