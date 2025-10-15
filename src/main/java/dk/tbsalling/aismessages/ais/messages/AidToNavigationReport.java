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
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 *
 * @author tbsalling
 */
public class AidToNavigationReport extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AidToNavigationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                    AidType aidType, String name, boolean positionAccurate,
                                    float latitude, float longitude,
                                    int toBow, int toStern, int toPort, int toStarboard,
                                    PositionFixingDevice positionFixingDevice, int second,
                                    boolean offPosition, String regionalUse, boolean raimFlag,
                                    boolean virtualAid, boolean assignedMode, int spare1,
                                    String nameExtension, Integer spare2) {
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().intValue()));

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
    public boolean getPositionAccurate() {
        return positionAccurate;
    }

    @SuppressWarnings("unused")
    public float getLatitude() {
        return latitude;
    }

    @SuppressWarnings("unused")
    public float getLongitude() {
        return longitude;
    }

    @SuppressWarnings("unused")
    public int getToBow() {
        return toBow;
    }

    @SuppressWarnings("unused")
    public int getToStern() {
        return toStern;
    }

    @SuppressWarnings("unused")
    public int getToStarboard() {
        return toStarboard;
    }

    @SuppressWarnings("unused")
    public int getToPort() {
        return toPort;
    }

    @SuppressWarnings("unused")
    public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
    }

    @SuppressWarnings("unused")
    public int getSecond() {
        return second;
    }

    @SuppressWarnings("unused")
    public boolean getOffPosition() {
        return offPosition;
    }

    @SuppressWarnings("unused")
    public String getAtoNStatus() {
        return regionalUse;
    }

    @SuppressWarnings("unused")
    public boolean getRaimFlag() {
        return raimFlag;
    }

    @SuppressWarnings("unused")
    public boolean getVirtualAid() {
        return virtualAid;
    }

    @SuppressWarnings("unused")
    public boolean getAssignedMode() {
        return assignedMode;
    }

    @SuppressWarnings("unused")
    public int getSpare1() {
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
    private final boolean positionAccurate;
    private final float latitude;
    private final float longitude;
    private final int toBow;
    private final int toStern;
    private final int toPort;
    private final int toStarboard;
    private final PositionFixingDevice positionFixingDevice;
    private final int second;
    private final boolean offPosition;
    private final String regionalUse;
    private final boolean raimFlag;
    private final boolean virtualAid;
    private final boolean assignedMode;
    private final int spare1;
    private final String nameExtension;
    private final Integer spare2;
}
