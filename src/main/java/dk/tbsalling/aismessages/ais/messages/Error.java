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
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Placeholder message representing an NMEA sentence that could not be decoded into an AIS message. It is not an ITU-R
 * M.1371 message type ({@link AISMessageType#Error} has code -1) and carries no metadata, repeat indicator or source
 * MMSI; it only holds the raw input and a description of the problem.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Error extends AISMessage {

    protected void checkAISMessage() {
    }

    public AISMessageType getMessageType() {
        return AISMessageType.Error;
    }

    public Error(String rawMessage, String errorDescription) {
        super();
        this.rawMessage = rawMessage;
		this.errorDescription = errorDescription;
	}

    /** The raw NMEA input that failed to decode. */
    String rawMessage;
    /** Human-readable description of why decoding failed. */
    String errorDescription;
}
