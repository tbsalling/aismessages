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
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class SafetyRelatedBroadcastMessage extends AISMessage {

    public SafetyRelatedBroadcastMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected SafetyRelatedBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.SafetyRelatedBroadcastMessage;
    }

    @SuppressWarnings("unused")
	public Integer getSpare() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return spare;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                spare = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(SafetyRelatedBroadcastMessage.this.getBits(38, 40));
            }
        });
	}

    @SuppressWarnings("unused")
	public final String getText() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return text;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                text = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                int extraBitsOfChars = ((SafetyRelatedBroadcastMessage.this.getNumberOfBits() - 40)/6)*6;
                return STRING_DECODER.apply(SafetyRelatedBroadcastMessage.this.getBits(40, 40 + extraBitsOfChars));
            }
        });
	}

    @Override
    public String toString() {
        return "SafetyRelatedBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", spare=" + getSpare() +
                ", text='" + getText() + '\'' +
                "} " + super.toString();
    }

    private transient Integer spare;
	private transient String text;
}
