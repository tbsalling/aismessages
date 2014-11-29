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

package dk.tbsalling.aismessages.messages;

import java.util.logging.Logger;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

/**
 * This message is used to pre-allocate TDMA slots within an AIS base station
 * network. It contains no navigational information, and is unlikely to be of
 * interest unless you are implementing or studying an AIS base station network.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class DataLinkManagement extends DecodedAISMessage {

    private static final Logger log = Logger.getLogger(DataLinkManagement.class.getName());

    public DataLinkManagement(
			Integer repeatIndicator,
			MMSI sourceMmsi,
			Integer offsetNumber1,
			Integer reservedSlots1,
			Integer timeout1, 
			Integer increment1,
			Integer offsetNumber2,
			Integer reservedSlots2,
			Integer timeout2,
			Integer increment2,
			Integer offsetNumber3,
			Integer reservedSlots3,
			Integer timeout3,
			Integer increment3,
			Integer offsetNumber4,
			Integer reservedSlots4,
			Integer timeout4,
			Integer increment4,
			NMEATagBlock nmeaTagBlock
			) {
		super(
				AISMessageType.DataLinkManagement,
				repeatIndicator,
				sourceMmsi, 
				nmeaTagBlock
				);
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

	public final Integer getOffsetNumber1() {
		return offsetNumber1;
	}

	public final Integer getReservedSlots1() {
		return reservedSlots1;
	}

	public final Integer getTimeout1() {
		return timeout1;
	}

	public final Integer getIncrement1() {
		return increment1;
	}

	public final Integer getOffsetNumber2() {
		return offsetNumber2;
	}

	public final Integer getReservedSlots2() {
		return reservedSlots2;
	}

	public final Integer getTimeout2() {
		return timeout2;
	}

	public final Integer getIncrement2() {
		return increment2;
	}

	public final Integer getOffsetNumber3() {
		return offsetNumber3;
	}

	public final Integer getReservedSlots3() {
		return reservedSlots3;
	}

	public final Integer getTimeout3() {
		return timeout3;
	}

	public final Integer getIncrement3() {
		return increment3;
	}

	public final Integer getOffsetNumber4() {
		return offsetNumber4;
	}

	public final Integer getReservedSlots4() {
		return reservedSlots4;
	}

	public final Integer getTimeout4() {
		return timeout4;
	}

	public final Integer getIncrement4() {
		return increment4;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
			.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
			.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
			.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
			.append("\"offset1\"").append(":").append(offsetNumber1).append(",")
			.append("\"slots1\"").append(":").append(reservedSlots1).append(",")
			.append("\"timeout1\"").append(":").append(timeout1).append(",")
			.append("\"increment1\"").append(":").append(increment1).append(",")
			.append("\"offset2\"").append(":").append(offsetNumber2).append(",")
			.append("\"slots2\"").append(":").append(reservedSlots2).append(",")
			.append("\"timeout2\"").append(":").append(timeout2).append(",")
			.append("\"increment2\"").append(":").append(increment2).append(",")
			.append("\"offset3\"").append(":").append(offsetNumber3).append(",")
			.append("\"slots3\"").append(":").append(reservedSlots3).append(",")
			.append("\"timeout3\"").append(":").append(timeout3).append(",")
			.append("\"increment3\"").append(":").append(increment3).append(",")
			.append("\"offset4\"").append(":").append(offsetNumber4).append(",")
			.append("\"slots4\"").append(":").append(reservedSlots4).append(",")
			.append("\"timeout4\"").append(":").append(timeout4).append(",")
			.append("\"increment4\"").append(":").append(increment4);
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}
	public static DataLinkManagement fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.DataLinkManagement))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		final int n = encodedMessage.getNumberOfBits();
		log.finest("n: " + n);
		
		Integer offsetNumber1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(40, 52));
		Integer reservedSlots1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		Integer timeout1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(56, 59));
		Integer increment1 = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(59, 70));
		Integer offsetNumber2 = n >= 100 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(70, 82)) : null;
		Integer reservedSlots2 = n >= 100 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(82, 86)) : null;
		Integer timeout2 = n >= 100 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(86, 89)) : null;
		Integer increment2 = n >= 100 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(89, 100)) : null;
		Integer offsetNumber3 = n >= 130 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(100, 112)) : null;
		Integer reservedSlots3 = n >= 130 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(112,116)) : null;
		Integer timeout3 = n >= 130 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(116, 119)) : null;
		Integer increment3 = n >= 130 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(119, 130)) : null;
		Integer offsetNumber4 = n >= 160 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(130, 142)) : null;
		Integer reservedSlots4 = n >= 160 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(142, 146)) : null;
		Integer timeout4 = n >= 160 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(146, 149)) : null;
		Integer increment4 = n >= 160 ? DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(149, 160)) : null;
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new DataLinkManagement(
				repeatIndicator,
				sourceMmsi,
				offsetNumber1,
				reservedSlots1,
				timeout1, 
				increment1,
				offsetNumber2,
				reservedSlots2, 
				timeout2, 
				increment2,
				offsetNumber3,
				reservedSlots3, 
				timeout3, 
				increment3,
				offsetNumber4, 
				reservedSlots4,
				timeout4, 
				increment4,
				nmeaTagBlock
				);
	}
	
	private final Integer offsetNumber1;
	private final Integer reservedSlots1;
	private final Integer timeout1;
	private final Integer increment1;
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
