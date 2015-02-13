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
import dk.tbsalling.aismessages.ais.messages.types.CommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.ITDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * A less detailed report than types 1-3 for vessels using Class B transmitters.
 * Omits navigational status and rate of turn.
 * 
 * @author tbsalling
 * 
 */
@SuppressWarnings("serial")
public class StandardClassBCSPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    public StandardClassBCSPositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected StandardClassBCSPositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.StandardClassBCSPositionReport;
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
                return BIT_DECODER.apply(StandardClassBCSPositionReport.this.getBits(38, 46));
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
                return UNSIGNED_FLOAT_DECODER.apply(StandardClassBCSPositionReport.this.getBits(46, 56))/10f;
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
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(56, 57));
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
                return FLOAT_DECODER.apply(StandardClassBCSPositionReport.this.getBits(85, 112))/600000f;
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
                return FLOAT_DECODER.apply(StandardClassBCSPositionReport.this.getBits(57, 85))/600000f;
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
                courseOverGround = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Float>() {
            @Override
            public Float get() {
                return UNSIGNED_FLOAT_DECODER.apply(StandardClassBCSPositionReport.this.getBits(112, 124))/10f;
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
                return UNSIGNED_INTEGER_DECODER.apply(StandardClassBCSPositionReport.this.getBits(124, 133));
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
                return UNSIGNED_INTEGER_DECODER.apply(StandardClassBCSPositionReport.this.getBits(133, 139));
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
                return BIT_DECODER.apply(StandardClassBCSPositionReport.this.getBits(139, 141));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getCsUnit() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return csUnit;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                csUnit = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(141, 142));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getDisplay() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return display;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                display = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(142, 143));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getDsc() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return dsc;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                dsc = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(143, 144));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getBand() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return band;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                band = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(144, 145));
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getMessage22() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return message22;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                message22 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(145, 146));
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
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(146, 147));
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
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(147, 148));
            }
        });
	}

    public Boolean getCommunicationStateSelectorFlag() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return commStateSelectorFlag;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                commStateSelectorFlag = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(StandardClassBCSPositionReport.this.getBits(148, 149));
            }
        });
    }

    @SuppressWarnings("unused")
    public CommunicationState getCommunicationState() {
        if (getCommunicationStateSelectorFlag() == Boolean.FALSE)
            return getDecodedValueByWeakReference(new Supplier<WeakReference<CommunicationState>>() {
                @Override
                public WeakReference<CommunicationState> get() {
                    return communicationState;
                }
            }, new Consumer<WeakReference<CommunicationState>>() {
                @Override
                public void accept(WeakReference<CommunicationState> value) {
                    communicationState = value;
                }
            }, new Supplier<Boolean>() {
                @Override
                public Boolean get() {
                    return Boolean.TRUE;
                }
            }, new Supplier<CommunicationState>() {
                @Override
                public CommunicationState get() {
                    return SOTDMACommunicationState.fromBitString(StandardClassBCSPositionReport.this.getBits(149, 168));
                }
            });
        else
            return getDecodedValueByWeakReference(new Supplier<WeakReference<CommunicationState>>() {
                @Override
                public WeakReference<CommunicationState> get() {
                    return communicationState;
                }
            }, new Consumer<WeakReference<CommunicationState>>() {
                @Override
                public void accept(WeakReference<CommunicationState> value) {
                    communicationState = value;
                }
            }, new Supplier<Boolean>() {
                @Override
                public Boolean get() {
                    return Boolean.TRUE;
                }
            }, new Supplier<CommunicationState>() {
                @Override
                public CommunicationState get() {
                    return ITDMACommunicationState.fromBitString(StandardClassBCSPositionReport.this.getBits(149, 168));
                }
            });
    }

    @Override
    public String toString() {
        return "StandardClassBCSPositionReport{" +
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
                ", csUnit=" + getCsUnit() +
                ", display=" + getDisplay() +
                ", dsc=" + getDsc() +
                ", band=" + getBand() +
                ", message22=" + getMessage22() +
                ", assigned=" + getAssigned() +
                ", raimFlag=" + getRaimFlag() +
                ", commStateSelectorFlag=" + getCommunicationStateSelectorFlag() +
                ", commState=" + getCommunicationState() +
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
	private transient Boolean csUnit;
	private transient Boolean display;
	private transient Boolean dsc;
	private transient Boolean band;
	private transient Boolean message22;
	private transient Boolean assigned;
	private transient Boolean raimFlag;
	private transient Boolean commStateSelectorFlag;
	private transient WeakReference<CommunicationState> communicationState;

}
