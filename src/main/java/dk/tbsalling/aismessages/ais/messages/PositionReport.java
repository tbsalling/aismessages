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

/**
 * 
 */
package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.CommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.ITDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public abstract class PositionReport extends AISMessage implements ExtendedDynamicDataReport {

    public PositionReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected PositionReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A;
    }

    @SuppressWarnings("unused")
	public NavigationStatus getNavigationStatus() {
        return getDecodedValue(new Supplier<NavigationStatus>() {
            @Override
            public NavigationStatus get() {
                return navigationStatus;
            }
        }, new Consumer<NavigationStatus>() {
            @Override
            public void accept(NavigationStatus value) {
                navigationStatus = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<NavigationStatus>() {
            @Override
            public NavigationStatus get() {
                return NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(PositionReport.this.getBits(38, 42)));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getRateOfTurn() {
        // TODO Square-root
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return rateOfTurn;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                rateOfTurn = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return INTEGER_DECODER.apply(PositionReport.this.getBits(42, 50));
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
                return UNSIGNED_FLOAT_DECODER.apply(PositionReport.this.getBits(50, 60))/10f;
            }
        });
	}

    @SuppressWarnings("unused")
	public Boolean getPositionAccuracy() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return positionAccuracy;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                positionAccuracy = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(PositionReport.this.getBits(60, 61));
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
                return FLOAT_DECODER.apply(PositionReport.this.getBits(89, 116))/600000f;
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
                return FLOAT_DECODER.apply(PositionReport.this.getBits(61, 89))/600000f;
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
                return UNSIGNED_FLOAT_DECODER.apply(PositionReport.this.getBits(116, 128))/10f;
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
                return UNSIGNED_INTEGER_DECODER.apply(PositionReport.this.getBits(128, 137));
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
                return UNSIGNED_INTEGER_DECODER.apply(PositionReport.this.getBits(137, 143));
            }
        });
	}

    @SuppressWarnings("unused")
	public ManeuverIndicator getSpecialManeuverIndicator() {
        return getDecodedValue(new Supplier<ManeuverIndicator>() {
            @Override
            public ManeuverIndicator get() {
                return specialManeuverIndicator;
            }
        }, new Consumer<ManeuverIndicator>() {
            @Override
            public void accept(ManeuverIndicator value) {
                specialManeuverIndicator = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<ManeuverIndicator>() {
            @Override
            public ManeuverIndicator get() {
                return ManeuverIndicator.fromInteger(UNSIGNED_INTEGER_DECODER.apply(PositionReport.this.getBits(143, 145)));
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
                return BOOLEAN_DECODER.apply(PositionReport.this.getBits(148, 149));
            }
        });
	}

    @SuppressWarnings("unused")
    public CommunicationState getCommunicationState() {
        if (this instanceof PositionReportClassAScheduled || this instanceof PositionReportClassAAssignedSchedule)
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
                    return SOTDMACommunicationState.fromBitString(PositionReport.this.getBits(149, 168));
                }
            });
        else if (this instanceof PositionReportClassAResponseToInterrogation)
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
                    return ITDMACommunicationState.fromBitString(PositionReport.this.getBits(149, 168));
                }
            });
        else
            return null;
    }

    @Override
    public String toString() {
        return "PositionReport{" +
                "navigationStatus=" + getNavigationStatus() +
                ", rateOfTurn=" + getRateOfTurn() +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccuracy=" + getPositionAccuracy() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", trueHeading=" + getTrueHeading() +
                ", second=" + getSecond() +
                ", specialManeuverIndicator=" + getSpecialManeuverIndicator() +
                ", raimFlag=" + getRaimFlag() +
                "} " + super.toString();
    }

    private transient NavigationStatus navigationStatus;
	private transient Integer rateOfTurn;
	private transient Float speedOverGround;
	private transient Boolean positionAccuracy;
	private transient Float latitude;
	private transient Float longitude;
	private transient Float courseOverGround;
	private transient Integer trueHeading;
	private transient Integer second;
	private transient ManeuverIndicator specialManeuverIndicator;
	private transient Boolean raimFlag;
	private transient WeakReference<CommunicationState> communicationState;
}
