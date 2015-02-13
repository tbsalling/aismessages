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

@SuppressWarnings("serial")
public class SafetyRelatedAcknowledge extends AISMessage {

    public SafetyRelatedAcknowledge(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected SafetyRelatedAcknowledge(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.SafetyRelatedAcknowledge;
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
                return UNSIGNED_INTEGER_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(38, 40));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi1() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return mmsi1;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                mmsi1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(40, 70)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getSequence1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return sequence1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                sequence1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(70, 72));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi2() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return mmsi2;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                mmsi2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return SafetyRelatedAcknowledge.this.getNumberOfBits() > 72;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(72, 102)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getSequence2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return sequence2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                sequence2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return SafetyRelatedAcknowledge.this.getNumberOfBits() > 72;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(102, 104));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi3() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return mmsi3;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                mmsi3 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return SafetyRelatedAcknowledge.this.getNumberOfBits() > 104;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(104, 134)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getSequence3() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return sequence3;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                sequence3 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return SafetyRelatedAcknowledge.this.getNumberOfBits() > 104;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(134, 136));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getMmsi4() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return mmsi4;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                mmsi4 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return SafetyRelatedAcknowledge.this.getNumberOfBits() > 136;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(136, 166)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getSequence4() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return sequence4;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                sequence4 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return SafetyRelatedAcknowledge.this.getNumberOfBits() > 136;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(SafetyRelatedAcknowledge.this.getBits(166, 168));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getNumOfAcks() {
        if (numOfAcks == null) {
        	final int numberOfBits = getNumberOfBits();
            numOfAcks = 1;
            if(numberOfBits > 72) {
                numOfAcks ++;
            }
            if(numberOfBits > 104) {
                numOfAcks ++;
            }
            if(numberOfBits > 136) {
                numOfAcks ++;
            }
        }
		return numOfAcks;
	}

    @Override
    public String toString() {
        return "SafetyRelatedAcknowledge{" +
                "messageType=" + getMessageType() +
                ", spare=" + getSpare() +
                ", mmsi1=" + getMmsi1() +
                ", sequence1=" + getSequence1() +
                ", mmsi2=" + getMmsi2() +
                ", sequence2=" + getSequence2() +
                ", mmsi3=" + getMmsi3() +
                ", sequence3=" + getSequence3() +
                ", mmsi4=" + getMmsi4() +
                ", sequence4=" + getSequence4() +
                ", numOfAcks=" + getNumOfAcks() +
                "} " + super.toString();
    }

    private transient Integer spare;
	private transient MMSI mmsi1;
	private transient Integer sequence1;
	private transient MMSI mmsi2;
	private transient Integer sequence2;
	private transient MMSI mmsi3;
	private transient Integer sequence3;
	private transient MMSI mmsi4;
	private transient Integer sequence4;
	private transient Integer numOfAcks;
}
