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
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

/**
 * an addressed point-to-point message with unspecified binary payload. The St.
 * Lawrence Seaway AIS system and the USG PAWSS system use this payload for
 * local extension messages. It is variable in length up to a maximum of 1008
 * bits (up to 5 AIVDM sentence payloads).
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class AddressedBinaryMessage extends AISMessage {

    public AddressedBinaryMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AddressedBinaryMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AddressedBinaryMessage;
    }

    @SuppressWarnings("unused")
    public Integer getSequenceNumber() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return sequenceNumber;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer ref) {
                sequenceNumber = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AddressedBinaryMessage.this.getBits(38, 40));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return destinationMmsi;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI ref) {
                destinationMmsi = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(AddressedBinaryMessage.this.getBits(40, 70)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getRetransmit() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return retransmit;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean ref) {
                retransmit = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(AddressedBinaryMessage.this.getBits(70, 71));
            }
        });
	}

    @SuppressWarnings("unused")
	public int getSpare() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return spare;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer ref) {
                spare = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AddressedBinaryMessage.this.getBits(71, 72));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getDesignatedAreaCode() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return designatedAreaCode;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer ref) {
                designatedAreaCode = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AddressedBinaryMessage.this.getBits(72, 82));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getFunctionalId() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return functionalId;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer ref) {
                functionalId = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AddressedBinaryMessage.this.getBits(82, 88));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return getDecodedValueByWeakReference(new Supplier<WeakReference<String>>() {
            @Override
            public WeakReference<String> get() {
                return binaryData;
            }
        }, new Consumer<WeakReference<String>>() {
            @Override
            public void accept(WeakReference<String> ref) {
                binaryData = ref;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return BIT_DECODER.apply(AddressedBinaryMessage.this.getBits(88, getNumberOfBits()));
            }
        });
	}

    @Override
    public String toString() {
        return "AddressedBinaryMessage{" +
                "messageType=" + getMessageType() +
                ", sequenceNumber=" + getSequenceNumber() +
                ", destinationMmsi=" + getDestinationMmsi() +
                ", retransmit=" + getRetransmit() +
                ", spare=" + getSpare() +
                ", designatedAreaCode=" + getDesignatedAreaCode() +
                ", functionalId=" + getFunctionalId() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private transient Integer sequenceNumber;
    private transient MMSI destinationMmsi;
    private transient Boolean retransmit;
    private transient Integer spare;
    private transient Integer designatedAreaCode;
    private transient Integer functionalId;
    private transient WeakReference<String> binaryData;
}
