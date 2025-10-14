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
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.lang.String.format;


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

    protected DataLinkManagement(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock);

        // Always decode mandatory fields (bits 40-70)
        this.offsetNumber1 = UNSIGNED_INTEGER_DECODER.apply(getBits(40, 52));
        this.reservedSlots1 = UNSIGNED_INTEGER_DECODER.apply(getBits(52, 56));
        this.timeout1 = UNSIGNED_INTEGER_DECODER.apply(getBits(56, 59));
        this.increment1 = UNSIGNED_INTEGER_DECODER.apply(getBits(59, 70));

        // Conditional fields set 2 (>= 100 bits)
        if (getNumberOfBits() >= 100) {
            this.offsetNumber2 = UNSIGNED_INTEGER_DECODER.apply(getBits(70, 82));
            this.reservedSlots2 = UNSIGNED_INTEGER_DECODER.apply(getBits(82, 86));
            this.timeout2 = UNSIGNED_INTEGER_DECODER.apply(getBits(86, 89));
            this.increment2 = UNSIGNED_INTEGER_DECODER.apply(getBits(89, 100));
        } else {
            this.offsetNumber2 = null;
            this.reservedSlots2 = null;
            this.timeout2 = null;
            this.increment2 = null;
        }

        // Conditional fields set 3 (>= 130 bits)
        if (getNumberOfBits() >= 130) {
            this.offsetNumber3 = UNSIGNED_INTEGER_DECODER.apply(getBits(100, 112));
            this.reservedSlots3 = UNSIGNED_INTEGER_DECODER.apply(getBits(112, 116));
            this.timeout3 = UNSIGNED_INTEGER_DECODER.apply(getBits(116, 119));
            this.increment3 = UNSIGNED_INTEGER_DECODER.apply(getBits(119, 130));
        } else {
            this.offsetNumber3 = null;
            this.reservedSlots3 = null;
            this.timeout3 = null;
            this.increment3 = null;
        }

        // Conditional fields set 4 (>= 160 bits)
        if (getNumberOfBits() >= 160) {
            this.offsetNumber4 = UNSIGNED_INTEGER_DECODER.apply(getBits(130, 142));
            this.reservedSlots4 = UNSIGNED_INTEGER_DECODER.apply(getBits(142, 146));
            this.timeout4 = UNSIGNED_INTEGER_DECODER.apply(getBits(146, 149));
            this.increment4 = UNSIGNED_INTEGER_DECODER.apply(getBits(149, 160));
        } else {
            this.offsetNumber4 = null;
            this.reservedSlots4 = null;
            this.timeout4 = null;
            this.increment4 = null;
        }
    }

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected DataLinkManagement(NMEAMessage[] nmeaMessages, String bitString, Metadata metadata, NMEATagBlock nmeaTagBlock,
                                 int repeatIndicator, MMSI sourceMmsi,
                                 int offsetNumber1, int reservedSlots1, int timeout1, int increment1,
                                 Integer offsetNumber2, Integer reservedSlots2, Integer timeout2, Integer increment2,
                                 Integer offsetNumber3, Integer reservedSlots3, Integer timeout3, Integer increment3,
                                 Integer offsetNumber4, Integer reservedSlots4, Integer timeout4, Integer increment4) {
        super(nmeaMessages, bitString, metadata, nmeaTagBlock, repeatIndicator, sourceMmsi);
        this.offsetNumber1 = offsetNumber1;
        this.reservedSlots1 = reservedSlots1;
        this.timeout1 = timeout1;
        this.increment1 = increment1;
        this.offsetNumber2 = offsetNumber2;
        this.reservedSlots2 = reservedSlots2;
        this.timeout2 = timeout2;
        this.increment2 = increment2;
        this.offsetNumber3 = offsetNumber3;
        this.reservedSlots3 = reservedSlots3;
        this.timeout3 = timeout3;
        this.increment3 = increment3;
        this.offsetNumber4 = offsetNumber4;
        this.reservedSlots4 = reservedSlots4;
        this.timeout4 = timeout4;
        this.increment4 = increment4;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 72 || numberOfBits > 160)
            errorMessage.append(format("Message of type %s should be 72-160 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.DataLinkManagement;
    }

    @SuppressWarnings("unused")
    public int getOffsetNumber1() {
        return offsetNumber1;
	}

    @SuppressWarnings("unused")
    public int getReservedSlots1() {
        return reservedSlots1;
	}

    @SuppressWarnings("unused")
    public int getTimeout1() {
        return timeout1;
	}

    @SuppressWarnings("unused")
    public int getIncrement1() {
        return increment1;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber2() {
        return offsetNumber2;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots2() {
        return reservedSlots2;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout2() {
        return timeout2;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement2() {
        return increment2;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber3() {
        return offsetNumber3;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots3() {
        return reservedSlots3;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout3() {
        return timeout3;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement3() {
        return increment3;
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber4() {
        return offsetNumber4;
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots4() {
        return reservedSlots4;
	}

    @SuppressWarnings("unused")
	public Integer getTimeout4() {
        return timeout4;
	}

    @SuppressWarnings("unused")
	public Integer getIncrement4() {
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

    private final int offsetNumber1;
    private final int reservedSlots1;
    private final int timeout1;
    private final int increment1;
    private final Integer offsetNumber2;
    private final Integer reservedSlots2;
    private final Integer timeout2;
    private final Integer increment2;
    private final Integer offsetNumber3;
    private final Integer reservedSlots3;
    private final Integer timeout3;
    private final Integer increment3;
    private final Integer offsetNumber4;
    private final Integer reservedSlots4;
    private final Integer timeout4;
    private final Integer increment4;
}
