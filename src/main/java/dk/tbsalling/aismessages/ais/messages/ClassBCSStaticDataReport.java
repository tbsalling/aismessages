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
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class ClassBCSStaticDataReport extends AISMessage implements StaticDataReport {

    public ClassBCSStaticDataReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ClassBCSStaticDataReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ClassBCSStaticDataReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    @SuppressWarnings("unused")
	public Integer getPartNumber() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return partNumber;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                partNumber = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ClassBCSStaticDataReport.this.getBits(38, 40));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 0;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(ClassBCSStaticDataReport.this.getBits(40, 160));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<ShipType>() {
            @Override
            public ShipType get() {
                return ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(ClassBCSStaticDataReport.this.getBits(40, 48)));
            }
        });
	}

    @SuppressWarnings("unused")
	public String getVendorId() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return vendorId;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                vendorId = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(ClassBCSStaticDataReport.this.getBits(48, 90));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(ClassBCSStaticDataReport.this.getBits(90, 132));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ClassBCSStaticDataReport.this.getBits(132, 141));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ClassBCSStaticDataReport.this.getBits(141, 150));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ClassBCSStaticDataReport.this.getBits(156, 162));
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
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(ClassBCSStaticDataReport.this.getBits(150, 156));
            }
        });
	}

    @SuppressWarnings("unused")
	public MMSI getMothershipMmsi() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return mothershipMmsi;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                mothershipMmsi = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return ClassBCSStaticDataReport.this.getPartNumber() == 1;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(ClassBCSStaticDataReport.this.getBits(132, 162)));
            }
        });
	}

    @Override
    public String toString() {
        return "ClassBCSStaticDataReport{" +
                "messageType=" + getMessageType() +
                ", partNumber=" + getPartNumber() +
                ", shipName='" + getShipName() + '\'' +
                ", shipType=" + getShipType() +
                ", vendorId='" + getVendorId() + '\'' +
                ", callsign='" + getCallsign() + '\'' +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toStarboard=" + getToStarboard() +
                ", toPort=" + getToPort() +
                ", mothershipMmsi=" + getMothershipMmsi() +
                "} " + super.toString();
    }

    private transient Integer partNumber;
    private transient String shipName;
    private transient ShipType shipType;
    private transient String vendorId;
    private transient String callsign;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toStarboard;
    private transient Integer toPort;
    private transient MMSI mothershipMmsi;

}
