/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
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

@SuppressWarnings("serial")
public class SafetyRelatedAcknowledge extends DecodedAISMessage {

	public SafetyRelatedAcknowledge(
			Integer repeatIndicator, MMSI sourceMmsi, MMSI mmsi1, MMSI mmsi2,
			MMSI mmsi3, MMSI mmsi4) {
		super(AISMessageType.SafetyRelatedAcknowledge, repeatIndicator, sourceMmsi);
		this.mmsi1 = mmsi1;
		this.mmsi2 = mmsi2;
		this.mmsi3 = mmsi3;
		this.mmsi4 = mmsi4;
	}

	public final MMSI getMmsi1() {
		return mmsi1;
	}

	public final MMSI getMmsi2() {
		return mmsi2;
	}

	public final MMSI getMmsi3() {
		return mmsi3;
	}

	public final MMSI getMmsi4() {
		return mmsi4;
	}

	public static BinaryAcknowledge fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.SafetyRelatedAcknowledge))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		MMSI mmsi1 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		MMSI mmsi2 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(72, 102)));
		MMSI mmsi3 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(104, 134)));
		MMSI mmsi4 = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(136, 166)));

		return new BinaryAcknowledge(repeatIndicator, sourceMmsi, mmsi1, mmsi2, mmsi3, mmsi4);
	}
	
	private final MMSI mmsi1;
	private final MMSI mmsi2;
	private final MMSI mmsi3;
	private final MMSI mmsi4;
}
