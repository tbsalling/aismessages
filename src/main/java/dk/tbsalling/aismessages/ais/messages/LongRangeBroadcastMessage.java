package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

@SuppressWarnings("serial")
public class LongRangeBroadcastMessage extends AISMessage implements DynamicDataReport {

    public LongRangeBroadcastMessage(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected LongRangeBroadcastMessage(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public AISMessageType getMessageType() {
        return AISMessageType.LongRangeBroadcastMessage;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A; // TODO this could also be type B (though expected to be very rare).
    }

    /**
     * @return true if position accuracy lte 10m; false otherwise.
     */
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
                return BOOLEAN_DECODER.apply(LongRangeBroadcastMessage.this.getBits(38, 39));
            }
        });
	}

    @SuppressWarnings("unused")
    public Boolean getRaim() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return raim;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                raim = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(LongRangeBroadcastMessage.this.getBits(39, 40));
            }
        });
	}

    @SuppressWarnings("unused")
	public NavigationStatus getNavigationalStatus() {
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
                return NavigationStatus.fromInteger(UNSIGNED_INTEGER_DECODER.apply(LongRangeBroadcastMessage.this.getBits(40, 44)));
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
                return FLOAT_DECODER.apply(LongRangeBroadcastMessage.this.getBits(44, 62))/600f;
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
                return FLOAT_DECODER.apply(LongRangeBroadcastMessage.this.getBits(62, 79))/600f;
            }
        });
	}

    /**
     * @return Knots (0-62); 63 = not available = default
     */
    @SuppressWarnings("unused")
	public Float getSpeedOverGround() {
        return Float.valueOf(getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return speed;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                speed = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(LongRangeBroadcastMessage.this.getBits(79, 85));
            }
        }));
	}

    /**
     * @return Degrees (0-359); 511 = not available = default
     */
    @SuppressWarnings("unused")
	public Float getCourseOverGround() {
        return Float.valueOf(getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return course;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                course = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(LongRangeBroadcastMessage.this.getBits(85, 94));
            }
        }));
	}

    /**
     * @return 0 if reported position latency is less than 5 seconds; 1 if reported position latency is greater than 5 seconds = default
     */
    @SuppressWarnings("unused")
	public Integer getPositionLatency() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return positionLatency;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                positionLatency = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(LongRangeBroadcastMessage.this.getBits(94, 95));
            }
        });
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
                return UNSIGNED_INTEGER_DECODER.apply(LongRangeBroadcastMessage.this.getBits(95, 96));
            }
        });
	}

    @Override
    public String toString() {
        return "LongRangeBroadcastMessage{" +
                "messageType=" + getMessageType() +
                ", positionAccuracy=" + getPositionAccuracy() +
                ", raim=" + getRaim() +
                ", status=" + getNavigationalStatus() +
                ", longitude=" + getLongitude() +
                ", latitude=" + getLatitude() +
                ", speed=" + getSpeedOverGround() +
                ", course=" + getCourseOverGround() +
                ", positionLatency=" + getPositionLatency() +
                ", spare=" + getSpare() +
                "} " + super.toString();
    }

    private transient Boolean positionAccuracy;
	private transient Boolean raim;
	private transient NavigationStatus navigationStatus;
	private transient Float longitude;
	private transient Float latitude;
	private transient Integer speed;
	private transient Integer course;
	private transient Integer positionLatency;
	private transient Integer spare;
}
