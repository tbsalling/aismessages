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

package dk.tbsalling.aismessages.ais.messages.types;

import java.io.Serializable;
import java.util.logging.Logger;

import static dk.tbsalling.aismessages.ais.Decoders.INTEGER_DECODER;
import static java.util.Objects.requireNonNull;

public class SOTDMACommunicationState extends CommunicationState implements Serializable {

	private transient static final Logger LOG = Logger.getLogger(SOTDMACommunicationState.class.getName());

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

		SyncState syncState = SyncState.fromInteger(INTEGER_DECODER.apply(bitString.substring(0, 2)));
		final int slotTimeout = INTEGER_DECODER.apply(bitString.substring(2, 5));
		Integer numberOfReceivedStations=null, slotNumber=null, utcHour=null, utcMinute=null, slotOffset=null;

		if (slotTimeout == 3 || slotTimeout == 5 || slotTimeout == 7) {
			numberOfReceivedStations = INTEGER_DECODER.apply(bitString.substring(5, 19));
			if (numberOfReceivedStations < 0 || numberOfReceivedStations > 16383)
				LOG.warning("numberOfReceivedStations: " + numberOfReceivedStations + ": Out of range.");
		} else if (slotTimeout == 2 || slotTimeout == 4 || slotTimeout == 6) {
			slotNumber = INTEGER_DECODER.apply(bitString.substring(5, 19));
			if (slotNumber < 0 || slotNumber > 2249)
				LOG.warning("slotNumber: " + slotNumber + ": Out of range.");
		}  else if (slotTimeout == 1) {
			utcHour = INTEGER_DECODER.apply(bitString.substring(5, 10));
			if (utcHour < 0 || utcHour > 23)
				LOG.warning("utcHour: " + utcHour + ": Out of range.");
			utcMinute = INTEGER_DECODER.apply(bitString.substring(10, 17));
			if (utcMinute < 0 || utcMinute > 59)
				LOG.warning("utcMinute: " + utcMinute + ": Out of range.");
		}  else if (slotTimeout == 0) {
			slotOffset = INTEGER_DECODER.apply(bitString.substring(5, 19));
		}

		return new SOTDMACommunicationState(syncState, slotTimeout, numberOfReceivedStations, slotNumber, utcHour, utcMinute, slotOffset);
	}

	@SuppressWarnings("unused")
	public Integer getSlotTimeout() {
		return slotTimeout;
	}

	@SuppressWarnings("unused")
	public Integer getNumberOfReceivedStations() {
		return numberOfReceivedStations;
	}

	@SuppressWarnings("unused")
	public Integer getSlotNumber() {
		return slotNumber;
	}

	@SuppressWarnings("unused")
	public Integer getUtcHour() {
		return utcHour;
	}

	@SuppressWarnings("unused")
	public Integer getUtcMinute() {
		return utcMinute;
	}

	@SuppressWarnings("unused")
	public Integer getSlotOffset() {
		return slotOffset;
	}

	private Integer slotTimeout;
	private Integer numberOfReceivedStations;
	private Integer slotNumber;
	private Integer utcHour;
	private Integer utcMinute;
	private Integer slotOffset;
}
