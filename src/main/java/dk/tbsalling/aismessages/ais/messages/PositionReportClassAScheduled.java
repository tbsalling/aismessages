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

/**
 * 
 */
package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;


/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class PositionReportClassAScheduled extends PositionReport {
    public PositionReportClassAScheduled(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected PositionReportClassAScheduled(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    @Override
    public String toString() {
        return "PositionReportClassAScheduled{" +
                "messageType=" + getMessageType() +
                "} " + super.toString();
    }

    @Override
    public AISMessageType getMessageType() {
        return AISMessageType.PositionReportClassAScheduled;
    }
}
