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

package dk.tbsalling.aismessages.ais.messages.types;

import dk.tbsalling.aismessages.ais.BitDecoder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.java.Log;

import static java.util.Objects.requireNonNull;

@Value
@EqualsAndHashCode(callSuper = true)
@Log
public class SOTDMACommunicationState extends CommunicationState {

	private SOTDMACommunicationState(SyncState syncState, Integer slotTimeout, Integer numberOfReceivedStations, Integer slotNumber, Integer utcHour, Integer utcMinute, Integer slotOffset) {
		super(syncState);
		this.slotTimeout = slotTimeout;
		this.numberOfReceivedStations = numberOfReceivedStations;
		this.slotNumber = slotNumber;
		this.utcHour = utcHour;
		this.utcMinute = utcMinute;
		this.slotOffset = slotOffset;
	}

	public static SOTDMACommunicationState fromBitString(String bitString) {
		requireNonNull(bitString);
		bitString = bitString.trim();

		if (bitString.length() != 19 || !bitString.matches("(0|1)*"))
			return null;

        SyncState syncState = SyncState.fromInteger(BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(0, 2)));
        final int slotTimeout = BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(2, 5));
		Integer numberOfReceivedStations=null, slotNumber=null, utcHour=null, utcMinute=null, slotOffset=null;

		if (slotTimeout == 3 || slotTimeout == 5 || slotTimeout == 7) {
            numberOfReceivedStations = BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(5, 19));
			if (numberOfReceivedStations > 16383)
                log.warning("numberOfReceivedStations: " + numberOfReceivedStations + ": Out of range.");
		} else if (slotTimeout == 2 || slotTimeout == 4 || slotTimeout == 6) {
            slotNumber = BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(5, 19));
			if (slotNumber > 2249)
                log.warning("slotNumber: " + slotNumber + ": Out of range.");
		}  else if (slotTimeout == 1) {
            utcHour = BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(5, 10));
			if (utcHour > 23)
                log.warning("utcHour: " + utcHour + ": Out of range.");
            utcMinute = BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(10, 17));
			if (utcMinute > 59)
                log.warning("utcMinute: " + utcMinute + ": Out of range.");
		}  else if (slotTimeout == 0) {
            slotOffset = BitDecoder.INSTANCE.decodeUnsignedInt(bitString.substring(5, 19));
		}

		return new SOTDMACommunicationState(syncState, slotTimeout, numberOfReceivedStations, slotNumber, utcHour, utcMinute, slotOffset);
	}

    Integer slotTimeout;
    Integer numberOfReceivedStations;
    Integer slotNumber;
    Integer utcHour;
    Integer utcMinute;
    Integer slotOffset;

}
