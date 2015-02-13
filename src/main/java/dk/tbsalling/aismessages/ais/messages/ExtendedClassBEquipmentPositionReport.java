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
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    public ExtendedClassBEquipmentPositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ExtendedClassBEquipmentPositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ExtendedClassBEquipmentPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    @SuppressWarnings("unused")
	public String getRegionalReserved1() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return regionalReserved1;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                regionalReserved1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return BIT_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(38, 46));
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return speedOverGround;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {
                speedOverGround = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return UNSIGNED_FLOAT_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(46, 55))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccurate() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return positionAccurate;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                positionAccurate = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(56, 57));
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
                return FLOAT_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(85, 112))/600000f;
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
                return FLOAT_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(57, 85))/600000f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return getDecodedValue(new Supplier<Float>() {
            @Override
            public Float get() {
                return courseOverGround;
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float value) {

            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return UNSIGNED_FLOAT_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(112, 124))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getTrueHeading() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return trueHeading;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                trueHeading = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(124, 133));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getSecond() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return second;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                second = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(133, 139));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved2() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return regionalReserved2;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                regionalReserved2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return BIT_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(139, 143));
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
                return STRING_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(143, 263));
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
                return ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(263, 271)));
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
                return UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(271, 280));
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
                return UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(280, 289));
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
                return UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(295, 301));
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
                return UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(289, 295));
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
                return PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(301, 305)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getRaimFlag() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return raimFlag;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                raimFlag = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(305, 306));
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
                return BOOLEAN_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(306, 307));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getAssigned() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return assigned;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                assigned = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(ExtendedClassBEquipmentPositionReport.this.getBits(307, 308));
            }
        });
	}

    @Override
    public String toString() {
        return "ExtendedClassBEquipmentPositionReport{" +
                "messageType=" + getMessageType() +
                ", regionalReserved1='" + getRegionalReserved1() + '\'' +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", trueHeading=" + getTrueHeading() +
                ", second=" + getSecond() +
                ", regionalReserved2='" + getRegionalReserved2() + '\'' +
                ", shipName='" + getShipName() + '\'' +
                ", shipType=" + getShipType() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toStarboard=" + getToStarboard() +
                ", toPort=" + getToPort() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", raimFlag=" + getRaimFlag() +
                ", dataTerminalReady=" + getDataTerminalReady() +
                ", assigned=" + getAssigned() +
                "} " + super.toString();
    }

    private transient String regionalReserved1;
    private transient Float speedOverGround;
    private transient Boolean positionAccurate;
    private transient Float latitude;
    private transient Float longitude;
    private transient Float courseOverGround;
    private transient Integer trueHeading;
    private transient Integer second;
    private transient String regionalReserved2;
    private transient String shipName;
    private transient ShipType shipType;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toStarboard;
    private transient Integer toPort;
    private transient PositionFixingDevice positionFixingDevice;
    private transient Boolean raimFlag;
    private transient Boolean dataTerminalReady;
    private transient Boolean assigned;
}
