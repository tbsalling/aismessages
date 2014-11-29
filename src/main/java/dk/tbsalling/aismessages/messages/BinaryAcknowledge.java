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

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.messages.NMEATagBlock;

/**
 * a receipt acknowledgement to the senders of a previous messages of type 6.
 * Total length varies between 72 and 168 bits by 32-bit increments, depending
 * on the number of destination MMSIs included.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class BinaryAcknowledge extends DecodedAISMessage {

	public BinaryAcknowledge(
			Integer repeatIndicator,
			MMSI sourceMmsi,
			MMSI mmsi1,
			Integer sequence1,
			MMSI mmsi2,
			Integer sequence2,
			MMSI mmsi3,
			Integer sequence3,
			MMSI mmsi4,
			Integer sequence4,
			NMEATagBlock nmeaTagBlock) {
		super(
				AISMessageType.BinaryAcknowledge,
				repeatIndicator,
				sourceMmsi,
				nmeaTagBlock
				);
		this.mmsi1 = mmsi1;
		this.sequence1 = sequence1;
		this.mmsi2 = mmsi2;
		this.sequence2 = sequence2;
		this.mmsi3 = mmsi3;
		this.sequence3 = sequence3;
		this.mmsi4 = mmsi4;
		this.sequence4 = sequence4;
	}

	public final MMSI getMmsi1() {
		return mmsi1;
	}

	public final Integer getSequence1() {
		return sequence1;
	}
	
	public final MMSI getMmsi2() {
		return mmsi2;
	}
	
	public final Integer getSequence2() {
		return sequence2;
	}

	public final MMSI getMmsi3() {
		return mmsi3;
	}
	
	public final Integer getSequence3() {
		return sequence3;
	}

	public final MMSI getMmsi4() {
		return mmsi4;
	}
	
	public final Integer getSequence4() {
		return sequence4;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{")
			.append("\"messageId\"").append(":").append(getMessageType().getCode()).append(",")
			.append("\"repeat\"").append(":").append(getRepeatIndicator()).append(",")
			.append("\"mmsi\"").append(":").append(String.format("\"%s\"", getSourceMmsi().getMMSI())).append(",")
			.append("\"destination1\"").append(":").append(mmsi1 == null ? null : String.format("\"%s\"", mmsi1.getMMSI())).append(",")
			.append("\"sequence1\"").append(":").append(sequence1).append(",")
			.append("\"destination2\"").append(":").append(mmsi2 == null ? null : String.format("\"%s\"", mmsi2.getMMSI())).append(",")
			.append("\"sequence2\"").append(":").append(sequence2).append(",")
			.append("\"destination3\"").append(":").append(mmsi3 == null ? null : String.format("\"%s\"", mmsi3.getMMSI())).append(",")
			.append("\"sequence3\"").append(":").append(sequence3).append(",")
			.append("\"destination4\"").append(":").append(mmsi4 == null ? null : String.format("\"%s\"", mmsi4.getMMSI())).append(",")
			.append("\"sequence4\"").append(":").append(sequence4);
			if (this.getNMEATagBlock() != null) {
				builder.append(",").append(this.getNMEATagBlock().toString());
			}
			builder.append("}");
		return builder.toString();
	}

	public static BinaryAcknowledge fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryAcknowledge))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		
		MMSI mmsi1 = null;
		Integer sequence1 = null;
		MMSI mmsi2 = null;
		Integer sequence2 = null;
		MMSI mmsi3 = null;
		Integer sequence3 = null;
		MMSI mmsi4 = null;
		Integer sequence4 = null;
		
		String encodedMessageStuffed = encodedMessage.getBits();
		if ((encodedMessage.getNumberOfBits() - 40) % 32 != 0) {
			int pad = (32 - (encodedMessage.getNumberOfBits() - 40) % 32);
			for (int i = 0; i < pad; i++)
				encodedMessageStuffed = encodedMessageStuffed.concat("0");
		}
		
		if (encodedMessage.getNumberOfBits() <= 72) {
			mmsi1 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(40, 70)));
			sequence1 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(70, 72));
		}
		else if (encodedMessage.getNumberOfBits() <= 104) {
			mmsi1 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(40, 70)));
			sequence1 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(70, 72));
			mmsi2 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(72, 102)));
			sequence2 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(102, 104));
		}
		else if (encodedMessage.getNumberOfBits() <= 136) {
			mmsi1 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(40, 70)));
			sequence1 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(70, 72));
			mmsi2 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(72, 102)));
			sequence2 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(102, 104));
			mmsi3 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(104, 134)));
			sequence3 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(134, 136));
		}
		else if (encodedMessage.getNumberOfBits() <= 168) {
			mmsi1 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(40, 70)));
			sequence1 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(70, 72));
			mmsi2 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(72, 102)));
			sequence2 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(102, 104));
			mmsi3 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(104, 134)));
			sequence3 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(134, 136));
			mmsi4 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessageStuffed.substring(136, 166)));
			sequence4 = DecoderImpl.convertToUnsignedInteger(encodedMessageStuffed.substring(166, 168));
		}
		
		NMEATagBlock nmeaTagBlock = encodedMessage.getNMEATagBlock();
		
		return new BinaryAcknowledge(
				repeatIndicator,
				sourceMmsi,
				mmsi1,
				sequence1,
				mmsi2,
				sequence2,
				mmsi3,
				sequence3,
				mmsi4,
				sequence4,
				nmeaTagBlock
				);
	}
	
	private final MMSI mmsi1;
	private final Integer sequence1;
	private final MMSI mmsi2;
	private final Integer sequence2;
	private final MMSI mmsi3;
	private final Integer sequence3;
	private final MMSI mmsi4;
	private final Integer sequence4;
}
