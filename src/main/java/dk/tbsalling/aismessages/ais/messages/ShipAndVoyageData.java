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

import dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.IMO;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.TIME_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * Message has a total of 424 bits, occupying two AIVDM sentences. In practice,
 * the information in these fields (especially ETA and destination) is not
 * reliable, as it has to be hand-updated by humans rather than gathered
 * automatically from sensors.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class ShipAndVoyageData extends AISMessage implements StaticDataReport {

    public ShipAndVoyageData(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ShipAndVoyageData(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    @Override
    protected void checkAISMessage() {
        final AISMessageType messageType = getMessageType();
        if (messageType != AISMessageType.ShipAndVoyageRelatedData) {
            throw new UnsupportedMessageType(messageType.getCode());
        }
        final int numberOfBits = getNumberOfBits();
        if (numberOfBits != 424 && numberOfBits != 422) {
            throw new InvalidMessage("Message of type " + messageType + " expected to be 424 bits long; not " + numberOfBits);
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ShipAndVoyageRelatedData;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A;
    }

    @SuppressWarnings("unused")
	public IMO getImo() {
        return getDecodedValue(new Supplier<IMO>() {
            @Override
            public IMO get() {
                return imo;
            }
        }, new Consumer<IMO>() {
            @Override
            public void accept(IMO value) {
                imo = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<IMO>() {
            @Override
            public IMO get() {
                return IMO.valueOf(UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(40, 70)));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return callsign;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                callsign = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(ShipAndVoyageData.this.getBits(70, 112));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return shipName;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                shipName = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(ShipAndVoyageData.this.getBits(112, 232));
            }
        });
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return getDecodedValue(new Supplier<ShipType>() {
            @Override
            public ShipType get() {
                return shipType;
            }
        }, new Consumer<ShipType>() {
            @Override
            public void accept(ShipType value) {
                shipType = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<ShipType>() {
            @Override
            public ShipType get() {
                return ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(232, 240)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return toBow;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                toBow = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(240, 249));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return toStern;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                toStern = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(249, 258));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return toStarboard;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                toStarboard = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(264, 270));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return toPort;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                toPort = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(258, 264));
            }
        });
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return getDecodedValue(new Supplier<PositionFixingDevice>() {
            @Override
            public PositionFixingDevice get() {
                return positionFixingDevice;
            }
        }, new Consumer<PositionFixingDevice>() {
            @Override
            public void accept(PositionFixingDevice value) {
                positionFixingDevice = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<PositionFixingDevice>() {
            @Override
            public PositionFixingDevice get() {
                return PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(ShipAndVoyageData.this.getBits(270, 274)));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getEta() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return eta;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                eta = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return TIME_DECODER.apply(ShipAndVoyageData.this.getBits(274, 294));
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getDraught() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return draught;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                draught = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return UNSIGNED_FLOAT_DECODER.apply(ShipAndVoyageData.this.getBits(294, 302))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public String getDestination() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return destination;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                destination = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(ShipAndVoyageData.this.getBits(302, 422));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getDataTerminalReady() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return dataTerminalReady;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                dataTerminalReady = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ShipAndVoyageData.this.getBits(422, 423));
            }
        });
	}

    @Override
    public String toString() {
        return "ShipAndVoyageData{" +
                "messageType=" + getMessageType() +
                ", imo=" + getImo() +
                ", callsign='" + getCallsign() + '\'' +
                ", shipName='" + getShipName() + '\'' +
                ", shipType=" + getShipType() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toStarboard=" + getToStarboard() +
                ", toPort=" + getToPort() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", eta='" + getEta() + '\'' +
                ", draught=" + getDraught() +
                ", destination='" + getDestination() + '\'' +
                ", dataTerminalReady=" + getDataTerminalReady() +
                "} " + super.toString();
    }

    private transient IMO imo;
    private transient String callsign;
    private transient String shipName;
    private transient ShipType shipType;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toStarboard;
    private transient Integer toPort;
    private transient PositionFixingDevice positionFixingDevice;
    private transient String eta;
    private transient Float draught;
    private transient String destination;
    private transient Boolean dataTerminalReady;
}
