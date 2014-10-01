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
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.util.logging.Logger;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;


/**
 * This message is used to pre-allocate TDMA slots within an AIS base station
 * network. It contains no navigational information, and is unlikely to be of
 * interest unless you are implementing or studying an AIS base station network.
 * 
 * @author tbsalling
 * 
 */

@SuppressWarnings("serial")
public class DataLinkManagement extends AISMessage {

    private static final Logger log = Logger.getLogger(DataLinkManagement.class.getName());

    public DataLinkManagement(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected DataLinkManagement(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.DataLinkManagement;
    }

    @SuppressWarnings("unused")
	public Integer getOffsetNumber1() {
        return getDecodedValue(() -> offsetNumber1, value -> offsetNumber1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(40, 52)));
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots1() {
        return getDecodedValue(() -> reservedSlots1, value -> reservedSlots1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(52, 56)));
	}

    @SuppressWarnings("unused")
	public Integer getTimeout1() {
        return getDecodedValue(() -> timeout1, value -> timeout1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(56, 59)));
	}

    @SuppressWarnings("unused")
	public Integer getIncrement1() {
        return getDecodedValue(() -> increment1, value -> increment1 = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(59, 70)));
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber2() {
        return getDecodedValue(() -> offsetNumber2, value -> offsetNumber2 = value, () -> getNumberOfBits() >= 100, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(70, 82)));
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots2() {
        return getDecodedValue(() -> reservedSlots2, value -> reservedSlots2 = value, () -> getNumberOfBits() >= 100, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(82, 86)));
	}

    @SuppressWarnings("unused")
	public Integer getTimeout2() {
        return getDecodedValue(() -> timeout2, value -> timeout2 = value, () -> getNumberOfBits() >= 100, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(86, 89)));
	}

    @SuppressWarnings("unused")
	public Integer getIncrement2() {
        return getDecodedValue(() -> increment2, value -> increment2 = value, () -> getNumberOfBits() >= 100, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(89, 100)));
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber3() {
        return getDecodedValue(() -> offsetNumber3, value -> offsetNumber3 = value, () -> getNumberOfBits() >= 130, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(100, 112)));
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots3() {
        return getDecodedValue(() -> reservedSlots3, value -> reservedSlots3 = value, () -> getNumberOfBits() >= 130, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(112, 116)));
	}

    @SuppressWarnings("unused")
	public Integer getTimeout3() {
        return getDecodedValue(() -> timeout3, value -> timeout3 = value, () -> getNumberOfBits() >= 130, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(116, 119)));
	}

    @SuppressWarnings("unused")
	public Integer getIncrement3() {
        return getDecodedValue(() -> increment3, value -> increment3 = value, () -> getNumberOfBits() >= 130, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(119, 130)));
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber4() {
        return getDecodedValue(() -> offsetNumber4, value -> offsetNumber4 = value, () -> getNumberOfBits() >= 160, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(130, 142)));
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots4() {
        return getDecodedValue(() -> reservedSlots4, value -> reservedSlots4 = value, () -> getNumberOfBits() >= 160, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(142, 146)));
	}

    @SuppressWarnings("unused")
	public Integer getTimeout4() {
        return getDecodedValue(() -> timeout4, value -> timeout4 = value, () -> getNumberOfBits() >= 160, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(146, 149)));
	}

    @SuppressWarnings("unused")
	public Integer getIncrement4() {
        return getDecodedValue(() -> increment4, value -> increment4 = value, () -> getNumberOfBits() >= 160, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(149, 160)));
	}

    @Override
    public String toString() {
        return "DataLinkManagement{" +
                "messageType=" + getMessageType() +
                ", offsetNumber1=" + getOffsetNumber1() +
                ", reservedSlots1=" + getReservedSlots1() +
                ", timeout1=" + getTimeout1() +
                ", increment1=" + getIncrement1() +
                ", offsetNumber2=" + getOffsetNumber2() +
                ", reservedSlots2=" + getReservedSlots2() +
                ", timeout2=" + getTimeout2() +
                ", increment2=" + getIncrement2() +
                ", offsetNumber3=" + getOffsetNumber3() +
                ", reservedSlots3=" + getReservedSlots3() +
                ", timeout3=" + getTimeout3() +
                ", increment3=" + getIncrement3() +
                ", offsetNumber4=" + getOffsetNumber4() +
                ", reservedSlots4=" + getReservedSlots4() +
                ", timeout4=" + getTimeout4() +
                ", increment4=" + getIncrement4() +
                "} " + super.toString();
    }

    private transient Integer offsetNumber1;
    private transient Integer reservedSlots1;
    private transient Integer timeout1;
    private transient Integer increment1;
    private transient Integer offsetNumber2;
    private transient Integer reservedSlots2;
    private transient Integer timeout2;
    private transient Integer increment2;
    private transient Integer offsetNumber3;
    private transient Integer reservedSlots3;
    private transient Integer timeout3;
    private transient Integer increment3;
    private transient Integer offsetNumber4;
    private transient Integer reservedSlots4;
    private transient Integer timeout4;
    private transient Integer increment4;
}
