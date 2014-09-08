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
        if (offsetNumber1 == null) {
            offsetNumber1 = UNSIGNED_INTEGER_DECODER.apply(getBits(40, 52));
        }
        return offsetNumber1;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots1() {
        if (reservedSlots1 == null) {
            reservedSlots1 = UNSIGNED_INTEGER_DECODER.apply(getBits(52, 56));
        }
        return reservedSlots1;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout1() {
        if (timeout1 == null) {
            timeout1 = UNSIGNED_INTEGER_DECODER.apply(getBits(56, 59));
        }
        return timeout1;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement1() {
        if (increment1 == null) {
            increment1 = UNSIGNED_INTEGER_DECODER.apply(getBits(59, 70));
        }
        return increment1;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber2() {
        if (getNumberOfBits() >= 100 && offsetNumber2 == null) {
            offsetNumber2 = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 82));
        }
        return offsetNumber2;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots2() {
        if (getNumberOfBits() >= 100 && reservedSlots2 == null) {
            reservedSlots2 = UNSIGNED_INTEGER_DECODER.apply(getBits(82, 86));
        }
        return reservedSlots2;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout2() {
        if (getNumberOfBits() >= 100 && timeout2 == null) {
            timeout2 = UNSIGNED_INTEGER_DECODER.apply(getBits(86, 89));
        }
        return timeout2;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement2() {
        if (getNumberOfBits() >= 100 && increment2 == null) {
            increment2 = UNSIGNED_INTEGER_DECODER.apply(getBits(89, 100));
        }
        return increment2;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber3() {
        if (getNumberOfBits() >= 130 && offsetNumber3 == null) {
            offsetNumber3 = UNSIGNED_INTEGER_DECODER.apply(getBits(100, 112));
        }
        return offsetNumber3;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots3() {
        if (getNumberOfBits() >= 130 && reservedSlots3 == null) {
            reservedSlots3 = UNSIGNED_INTEGER_DECODER.apply(getBits(112, 116));
        }
        return reservedSlots3;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout3() {
        if (getNumberOfBits() >= 130 && timeout3 == null) {
            timeout3 = UNSIGNED_INTEGER_DECODER.apply(getBits(116, 119));
        }
        return timeout3;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement3() {
        if (getNumberOfBits() >= 130 && increment3 == null) {
            increment3 = UNSIGNED_INTEGER_DECODER.apply(getBits(119, 130));
        }
        return increment3;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber4() {
        if (getNumberOfBits() >= 160 && offsetNumber4 == null) {
            offsetNumber4 = UNSIGNED_INTEGER_DECODER.apply(getBits(130, 142));
        }
        return offsetNumber4;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots4() {
        if (getNumberOfBits() >= 160 && reservedSlots4 == null) {
            reservedSlots4 = UNSIGNED_INTEGER_DECODER.apply(getBits(142, 146));
        }
        return reservedSlots4;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout4() {
        if (getNumberOfBits() >= 160 && timeout4 == null) {
            timeout4 = UNSIGNED_INTEGER_DECODER.apply(getBits(146, 149));
        }
        return timeout4;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement4() {
        if (getNumberOfBits() >= 160 && increment4 == null) {
            increment4 = UNSIGNED_INTEGER_DECODER.apply(getBits(149, 160));
        }
        return increment4;
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
