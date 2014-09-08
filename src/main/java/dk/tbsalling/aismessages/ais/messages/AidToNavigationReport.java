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
 * @author tbsalling
 *
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
        if (aidType == null) {
            aidType = AidType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(38, 43)));
        }
        return aidType;
	}

    @SuppressWarnings("unused")
	public String getName() {
        if (name == null) {
            name = STRING_DECODER.apply(getBits(43, 163));
        }
        return name;
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        if (positionAccurate == null) {
            positionAccurate = BOOLEAN_DECODER.apply(getBits(163, 164));
        }
        return positionAccurate;
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        if (latitude == null) {
            latitude = FLOAT_DECODER.apply(getBits(192, 219)) / 600000f;
        }
        return latitude;
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        if (longitude == null) {
            longitude = FLOAT_DECODER.apply(getBits(164, 192)) / 600000f;
        }
        return longitude;
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        if (toBow == null) {
            toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(219, 228));
        }
        return toBow;
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        if (toStern == null) {
            toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(228, 237));
        }
        return toStern;
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        if (toStarboard == null) {
            toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(243, 249));
        }
        return toStarboard;
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        if (toPort == null) {
            toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(237, 243));
        }
        return toPort;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        if (positionFixingDevice == null) {
            positionFixingDevice = PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(249, 253)));
        }
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        if (second == null) {
            second = UNSIGNED_INTEGER_DECODER.apply(getBits(253, 259));
        }
        return second;
	}

    @SuppressWarnings("unused")
	public Boolean getOffPosition() {
        if (offPosition == null) {
            offPosition = BOOLEAN_DECODER.apply(getBits(259, 260));
        }
        return offPosition;
	}

    @SuppressWarnings("unused")
	public String getRegionalUse() {
        if (regionalUse == null) {
            regionalUse = BIT_DECODER.apply(getBits(260, 268));
        }
        return regionalUse;
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        if (raimFlag == null) {
            raimFlag = BOOLEAN_DECODER.apply(getBits(268, 269));
        }
        return raimFlag;
	}

    @SuppressWarnings("unused")
	public Boolean getVirtualAid() {
        if (virtualAid == null) {
            virtualAid = BOOLEAN_DECODER.apply(getBits(269, 270));
        }
        return virtualAid;
	}

    @SuppressWarnings("unused")
	public Boolean getAssignedMode() {
        if (assignedMode == null) {
            assignedMode = BOOLEAN_DECODER.apply(getBits(270, 271));
        }
        return assignedMode;
	}

    @SuppressWarnings("unused")
	public int getSpare1() {
        if (spare1 == null) {
            spare1 = UNSIGNED_INTEGER_DECODER.apply(getBits(271, 272));
        }
        return spare1;
	}

    @SuppressWarnings("unused")
	public String getNameExtension() {
        if(nameExtension == null && getNumberOfBits() > 272) {
            int extraBits = getNumberOfBits() - 272;
            int extraChars = extraBits / 6;
            int extraBitsOfChars = extraChars * 6;
            nameExtension = STRING_DECODER.apply(getBits(272, 272 + extraBitsOfChars));
        }
        return nameExtension;
	}

    @SuppressWarnings("unused")
	public int getSpare2() {
        if(spare2 == null && getNumberOfBits() > 272) {
            int extraBits = getNumberOfBits() - 272;
            int extraChars = extraBits / 6;
            int extraBitsOfChars = extraChars * 6;
            spare2 = (extraBits == extraBitsOfChars) ? 0 : UNSIGNED_INTEGER_DECODER.apply(getBits(272 + extraBitsOfChars, getNumberOfBits()));
        }
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
                ", regionalUse='" + getRegionalUse() + '\'' +
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
