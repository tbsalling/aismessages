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
import dk.tbsalling.aismessages.ais.messages.types.TxRxMode;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class ChannelManagement extends AISMessage {

    public ChannelManagement(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ChannelManagement(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ChannelManagement;
    }

    @SuppressWarnings("unused")
	public Integer getChannelA() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return channelA;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                channelA = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ChannelManagement.this.getBits(40, 52));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getChannelB() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return channelB;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                channelB = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ChannelManagement.this.getBits(52, 64));
            }
        });
	}

    @SuppressWarnings("unused")
	public TxRxMode getTransmitReceiveMode() {
        return getDecodedValue(new Supplier<TxRxMode>() {
            @Override
            public TxRxMode get() {
                return transmitReceiveMode;
            }
        }, new Consumer<TxRxMode>() {
            @Override
            public void accept(TxRxMode value) {
                transmitReceiveMode = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<TxRxMode>() {
            @Override
            public TxRxMode get() {
                return TxRxMode.fromInteger(UNSIGNED_INTEGER_DECODER.apply(ChannelManagement.this.getBits(64, 68)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getPower() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return power;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                power = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ChannelManagement.this.getBits(68, 69));
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getNorthEastLongitude() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return northEastLongitude;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                northEastLongitude = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return !ChannelManagement.this.getAddressed();
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return FLOAT_DECODER.apply(ChannelManagement.this.getBits(69, 87))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getNorthEastLatitude() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return northEastLatitude;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                northEastLatitude = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return !ChannelManagement.this.getAddressed();
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return FLOAT_DECODER.apply(ChannelManagement.this.getBits(87, 104))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLongitude() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return southWestLongitude;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                southWestLongitude = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return !ChannelManagement.this.getAddressed();
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return FLOAT_DECODER.apply(ChannelManagement.this.getBits(104, 122))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getSouthWestLatitude() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return southWestLatitude;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                southWestLatitude = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return !ChannelManagement.this.getAddressed();
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return FLOAT_DECODER.apply(ChannelManagement.this.getBits(122, 138))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi1() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return destinationMmsi1;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                destinationMmsi1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return ChannelManagement.this.getAddressed();
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(ChannelManagement.this.getBits(69, 99)));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getDestinationMmsi2() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return destinationMmsi2;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                destinationMmsi2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return ChannelManagement.this.getAddressed();
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(ChannelManagement.this.getBits(104, 134)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getAddressed() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return addressed;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                addressed = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ChannelManagement.this.getBits(139, 140));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getBandA() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return bandA;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                bandA = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ChannelManagement.this.getBits(140, 141));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getBandB() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return bandB;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                bandB = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ChannelManagement.this.getBits(141, 142));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getZoneSize() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return zoneSize;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                zoneSize = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ChannelManagement.this.getBits(142, 145));
            }
        });
	}

    @Override
    public String toString() {
        return "ChannelManagement{" +
                "messageType=" + getMessageType() +
                ", channelA=" + getChannelA() +
                ", channelB=" + getChannelB() +
                ", transmitReceiveMode=" + getTransmitReceiveMode() +
                ", power=" + getPower() +
                ", northEastLongitude=" + getNorthEastLongitude() +
                ", northEastLatitude=" + getNorthEastLatitude() +
                ", southWestLongitude=" + getSouthWestLongitude() +
                ", southWestLatitude=" + getSouthWestLatitude() +
                ", destinationMmsi1=" + getDestinationMmsi1() +
                ", destinationMmsi2=" + getDestinationMmsi2() +
                ", addressed=" + getAddressed() +
                ", bandA=" + getBandA() +
                ", bandB=" + getBandB() +
                ", zoneSize=" + getZoneSize() +
                "} " + super.toString();
    }

    private transient Integer channelA;
    private transient Integer channelB;
    private transient TxRxMode transmitReceiveMode;
    private transient Boolean power;
    private transient Float northEastLongitude;
    private transient Float northEastLatitude;
    private transient Float southWestLongitude;
    private transient Float southWestLatitude;
    private transient MMSI destinationMmsi1;
    private transient MMSI destinationMmsi2;
    private transient Boolean addressed;
    private transient Boolean bandA;
    private transient Boolean bandB;
    private transient Integer zoneSize;
}
