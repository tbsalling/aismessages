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

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * used to broadcast differential corrections for GPS. The data in the payload
 * is intended to be passed directly to GPS receivers capable of accepting such
 * corrections.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class GNSSBinaryBroadcastMessage extends AISMessage {

    public GNSSBinaryBroadcastMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected GNSSBinaryBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.GNSSBinaryBroadcastMessage;
    }

    @SuppressWarnings("unused")
	public Integer getSpare1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return spare1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                spare1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(38, 40));
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getLatitude() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return latitude;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                latitude = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return FLOAT_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(58, 75))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getLongitude() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return longitude;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                longitude = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return FLOAT_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(40, 58))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getSpare2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return spare2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                spare2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(75, 80));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getMType() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return mType;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                mType = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(80, 86));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getStationId() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return stationId;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                stationId = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(86, 96));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getZCount() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return zCount;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                zCount = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(96, 109));
            }
        });
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
            public void accept(Integer value) {
                sequenceNumber = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(109, 112));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getNumOfWords() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return numOfWords;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                numOfWords = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(112, 117));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getHealth() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return health;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                health = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(117, 120));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getBinaryData() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return binaryData;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                binaryData = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return GNSSBinaryBroadcastMessage.this.getNumberOfBits() > 80;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return BIT_DECODER.apply(GNSSBinaryBroadcastMessage.this.getBits(80, getNumberOfBits()));
            }
        });
	}

    @Override
    public String toString() {
        return "GNSSBinaryBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", spare1=" + getSpare1() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", spare2=" + getSpare2() +
                ", mType=" + getMType() +
                ", stationId=" + getStationId() +
                ", zCount=" + getZCount() +
                ", sequenceNumber=" + getSequenceNumber() +
                ", numOfWords=" + getNumOfWords() +
                ", health=" + getHealth() +
                ", binaryData='" + getBinaryData() + '\'' +
                "} " + super.toString();
    }

    private transient Integer spare1;
    private transient Float latitude;
    private transient Float longitude;
    private transient Integer spare2;
    private transient Integer mType;
    private transient Integer stationId;
    private transient Integer zCount;
    private transient Integer sequenceNumber;
    private transient Integer numOfWords;
    private transient Integer health;
    private transient String binaryData;
}
