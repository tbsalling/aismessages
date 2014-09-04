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

import dk.tbsalling.aismessages.decoder.Decoder;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;

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
			Integer repeatIndicator, MMSI sourceMmsi, int spare, MMSI mmsi1, int sequence1, MMSI mmsi2,
			int sequence2, MMSI mmsi3, int sequence3, MMSI mmsi4, int sequence4, int numOfAcks) {
		super(AISMessageType.BinaryAcknowledge, repeatIndicator, sourceMmsi);
		this.spare = spare;
		this.mmsi1 = mmsi1;
		this.sequence1 = sequence1;
		this.mmsi2 = mmsi2;
		this.sequence2 = sequence2;
		this.mmsi3 = mmsi3;
		this.sequence3 = sequence3;
		this.mmsi4 = mmsi4;
		this.sequence4 = sequence4;
		this.numOfAcks = numOfAcks;
	}

    @SuppressWarnings("unused")
	public final int getSpare() {
		return spare;
	}

    @SuppressWarnings("unused")
	public final MMSI getMmsi1() {
		return mmsi1;
	}

    @SuppressWarnings("unused")
	public final int getSequence1() {
		return sequence1;
	}

    @SuppressWarnings("unused")
	public final MMSI getMmsi2() {
		return mmsi2;
	}

    @SuppressWarnings("unused")
	public final int getSequence2() {
		return sequence2;
	}

    @SuppressWarnings("unused")
	public final MMSI getMmsi3() {
		return mmsi3;
	}

    @SuppressWarnings("unused")
	public final int getSequence3() {
		return sequence3;
	}

    @SuppressWarnings("unused")
	public final MMSI getMmsi4() {
		return mmsi4;
	}

    @SuppressWarnings("unused")
	public final int getSequence4() {
		return sequence4;
	}

    @SuppressWarnings("unused")
	public final int getNumOfAcks() {
		return numOfAcks;
	}
	
	public static BinaryAcknowledge fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.BinaryAcknowledge))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		int spare = Decoder.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI mmsi1 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		int sequence1 = Decoder.convertToUnsignedInteger(encodedMessage.getBits(70, 72));
		int numOfAcks = 1;
		
		MMSI mmsi2 = null;
		int sequence2 = 0;
		if(encodedMessage.getNumberOfBits() > 72) {
			mmsi2 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(72, 102)));
			sequence2 = Decoder.convertToUnsignedInteger(encodedMessage.getBits(102, 104));
			numOfAcks ++;
		}
		
		MMSI mmsi3 = null;
		int sequence3 = 0;
		if(encodedMessage.getNumberOfBits() > 104) {
			mmsi3 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(104, 134)));
			sequence3 = Decoder.convertToUnsignedInteger(encodedMessage.getBits(134, 136));
			numOfAcks ++;
		}
		
		MMSI mmsi4 = null;
		int sequence4 = 0;
		if(encodedMessage.getNumberOfBits() > 136) {
			mmsi4 = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(136, 166)));
			sequence4 = Decoder.convertToUnsignedInteger(encodedMessage.getBits(166, 168));
			numOfAcks ++;
		}

		return new BinaryAcknowledge(repeatIndicator, sourceMmsi, spare, mmsi1, sequence1, mmsi2, sequence2, mmsi3, sequence3, mmsi4, sequence4, numOfAcks);
	}
	
	private final int spare;
	private final MMSI mmsi1;
	private final int sequence1;
	private final MMSI mmsi2;
	private final int sequence2;
	private final MMSI mmsi3;
	private final int sequence3;
	private final MMSI mmsi4;
	private final int sequence4;
	private final int numOfAcks;
}
