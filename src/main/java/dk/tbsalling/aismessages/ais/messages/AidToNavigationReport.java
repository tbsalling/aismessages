/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 *
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class AidToNavigationReport extends AISMessage {

    public AidToNavigationReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AidToNavigationReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AidToNavigationReport;
    }

    @SuppressWarnings("unused")
    public AidType getAidType() {
        return getDecodedValue(() -> aidType, value -> aidType = value, () -> Boolean.TRUE, () -> AidType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(38, 43))));
    }

    @SuppressWarnings("unused")
    public String getName() {
        return getDecodedValue(() -> name, value -> name = value, () -> Boolean.TRUE, () -> STRING_DECODER.apply(getBits(43, 163)));
    }

    @SuppressWarnings("unused")
    public Boolean getPositionAccurate() {
        return getDecodedValue(() -> positionAccurate, value -> positionAccurate = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(163, 164)));
    }

    @SuppressWarnings("unused")
    public Float getLatitude() {
        return getDecodedValue(() -> latitude, value -> latitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(192, 219))/600000f);
    }

    @SuppressWarnings("unused")
    public Float getLongitude() {
        return getDecodedValue(() -> longitude, value -> longitude = value, () -> Boolean.TRUE, () -> FLOAT_DECODER.apply(getBits(164, 192))/600000f);
    }

    @SuppressWarnings("unused")
    public Integer getToBow() {
        return getDecodedValue(() -> toBow, value -> toBow = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(219, 228)));
    }

    @SuppressWarnings("unused")
    public Integer getToStern() {
        return getDecodedValue(() -> toStern, value -> toStern = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(228, 237)));
    }

    @SuppressWarnings("unused")
    public Integer getToStarboard() {
        return getDecodedValue(() -> toStarboard, value -> toStarboard = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(243, 249)));
    }

    @SuppressWarnings("unused")
    public Integer getToPort() {
        return getDecodedValue(() -> toPort, value -> toPort = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(237, 243)));
    }

    @SuppressWarnings("unused")
    public PositionFixingDevice getPositionFixingDevice() {
        return getDecodedValue(() -> positionFixingDevice, value -> positionFixingDevice = value, () -> Boolean.TRUE, () -> PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(249, 253))));
    }

    @SuppressWarnings("unused")
    public Integer getSecond() {
        return getDecodedValue(() -> second, value -> second = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(253, 259)));
    }

    @SuppressWarnings("unused")
    public Boolean getOffPosition() {
        return getDecodedValue(() -> offPosition, value -> offPosition = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(259, 260)));
    }

    @SuppressWarnings("unused")
    public String getAtoNStatus() {
        return getDecodedValue(() -> regionalUse, value -> regionalUse = value, () -> Boolean.TRUE, () -> BIT_DECODER.apply(getBits(260, 268)));
    }

    @SuppressWarnings("unused")
    public Boolean getRaimFlag() {
        return getDecodedValue(() -> raimFlag, value -> raimFlag = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(268, 269)));
    }

    @SuppressWarnings("unused")
    public Boolean getVirtualAid() {
        return getDecodedValue(() -> virtualAid, value -> virtualAid = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(269, 270)));
    }

    @SuppressWarnings("unused")
    public Boolean getAssignedMode() {
        return getDecodedValue(() -> assignedMode, value -> assignedMode = value, () -> Boolean.TRUE, () -> BOOLEAN_DECODER.apply(getBits(270, 271)));
    }

    @SuppressWarnings("unused")
    public Integer getSpare1() {
        return getDecodedValue(() -> spare1, value -> spare1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(271, 272)));
    }

    @SuppressWarnings("unused")
    public String getNameExtension() {
        getDecodedValue(() -> nameExtension, value -> nameExtension = value, () -> getNumberOfBits() > 272, () -> {
            int extraBits = getNumberOfBits() - 272;
            int extraChars = extraBits/6;
            int extraBitsOfChars = extraChars*6;
            return STRING_DECODER.apply(getBits(272, 272 + extraBitsOfChars));
        });
        return nameExtension;
    }

    @SuppressWarnings("unused")
    public Integer getSpare2() {
        getDecodedValue(() -> spare2, value -> spare2 = value, () -> getNumberOfBits() >= 272, () -> {
            int extraBits = getNumberOfBits() - 272;
            int extraChars = extraBits/6;
            int extraBitsOfChars = extraChars*6;
            return (extraBits == extraBitsOfChars) ? 0 : UNSIGNED_INTEGER_DECODER.apply(getBits(272 + extraBitsOfChars, getNumberOfBits()));
        });
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

    private transient AidType aidType;
    private transient String name;
    private transient Boolean positionAccurate;
    private transient Float latitude;
    private transient Float longitude;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toPort;
    private transient Integer toStarboard;
    private transient PositionFixingDevice positionFixingDevice;
    private transient Integer second;
    private transient Boolean offPosition;
    private transient String regionalUse;
    private transient Boolean raimFlag;
    private transient Boolean virtualAid;
    private transient Boolean assignedMode;
    private transient Integer spare1;
    private transient String nameExtension;
    private transient Integer spare2;
}
