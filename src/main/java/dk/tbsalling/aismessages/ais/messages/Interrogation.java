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

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

/**
 * Used by a base station to query one or two other AIS transceivers for status messages of specified types.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class Interrogation extends AISMessage {

    public Interrogation(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected Interrogation(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.Interrogation;
    }

    @SuppressWarnings("unused")
	public final MMSI getInterrogatedMmsi1() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return interrogatedMmsi1;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                interrogatedMmsi1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(Interrogation.this.getBits(40, 70)));
            }
        });
	}

    @SuppressWarnings("unused")
	public final Integer getType1_1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return type1_1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                type1_1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(Interrogation.this.getBits(70, 76));
            }
        });
	}

    @SuppressWarnings("unused")
	public final Integer getOffset1_1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offset1_1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offset1_1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(Interrogation.this.getBits(76, 88));
            }
        });
	}

    @SuppressWarnings("unused")
	public final Integer getType1_2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return type1_2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                type1_2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Interrogation.this.getNumberOfBits() > 88;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(Interrogation.this.getBits(90, 96));
            }
        });
	}

    @SuppressWarnings("unused")
	public final Integer getOffset1_2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offset1_2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offset1_2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Interrogation.this.getNumberOfBits() > 88;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(Interrogation.this.getBits(96, 108));
            }
        });
	}

    @SuppressWarnings("unused")
	public final MMSI getInterrogatedMmsi2() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return interrogatedMmsi2;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                interrogatedMmsi2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Interrogation.this.getNumberOfBits() >= 110;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(Interrogation.this.getBits(110, 140)));
            }
        });
	}

    @SuppressWarnings("unused")
	public final Integer getType2_1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return type2_1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                type2_1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Interrogation.this.getNumberOfBits() >= 110;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(Interrogation.this.getBits(140, 146));
            }
        });
	}

    @SuppressWarnings("unused")
	public final Integer getOffset2_1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offset2_1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offset2_1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Interrogation.this.getNumberOfBits() >= 110;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(Interrogation.this.getBits(146, 158));
            }
        });
	}

    @Override
    public String toString() {
        return "Interrogation{" +
                "messageType=" + getMessageType() +
                ", interrogatedMmsi1=" + getInterrogatedMmsi1() +
                ", type1_1=" + getType1_1() +
                ", offset1_1=" + getOffset1_1() +
                ", type1_2=" + getType1_2() +
                ", offset1_2=" + getOffset1_2() +
                ", interrogatedMmsi2=" + getInterrogatedMmsi2() +
                ", type2_1=" + getType2_1() +
                ", offset2_1=" + getOffset2_1() +
                "} " + super.toString();
    }

    private transient MMSI interrogatedMmsi1;
	private transient Integer type1_1;
	private transient Integer offset1_1;
	private transient Integer type1_2;
	private transient Integer offset1_2;
	private transient MMSI interrogatedMmsi2;
	private transient Integer type2_1;
	private transient Integer offset2_1;
}
