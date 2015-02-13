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
import dk.tbsalling.aismessages.ais.messages.types.SOTDMACommunicationState;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.lang.ref.WeakReference;

import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * This message is to be used by fixed-location base stations to periodically report a position and time reference.
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public class BaseStationReport extends AISMessage {

    public BaseStationReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BaseStationReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BaseStationReport;
    }

    @SuppressWarnings("unused")
	public Integer getYear() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return year;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                year = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(38, 52));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getMonth() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return month;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                month = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(52, 56));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getDay() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return day;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                day = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(56, 61));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getHour() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return hour;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                hour = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(61, 66));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getMinute() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return minute;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                minute = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(66, 72));
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
                return UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(72, 78));
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
                return BOOLEAN_DECODER.apply(BaseStationReport.this.getBits(78, 79));
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
                return FLOAT_DECODER.apply(BaseStationReport.this.getBits(107, 134))/600000f;
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
                return FLOAT_DECODER.apply(BaseStationReport.this.getBits(79, 107))/600000f;
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
                return PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(BaseStationReport.this.getBits(134, 138)));
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
                return BOOLEAN_DECODER.apply(BaseStationReport.this.getBits(148, 149));
            }
        });
	}

    @SuppressWarnings("unused")
    public SOTDMACommunicationState getCommunicationState() {
        return getDecodedValueByWeakReference(new Supplier<WeakReference<SOTDMACommunicationState>>() {
            @Override
            public WeakReference<SOTDMACommunicationState> get() {
                return communicationState;
            }
        }, new Consumer<WeakReference<SOTDMACommunicationState>>() {
            @Override
            public void accept(WeakReference<SOTDMACommunicationState> value) {
                communicationState = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<SOTDMACommunicationState>() {
            @Override
            public SOTDMACommunicationState get() {
                return SOTDMACommunicationState.fromBitString(BaseStationReport.this.getBits(149, 168));
            }
        });
    }

    @Override
    public String toString() {
        return "BaseStationReport{" +
                "messageType=" + getMessageType() +
                ", year=" + getYear() +
                ", month=" + getMonth() +
                ", day=" + getDay() +
                ", hour=" + getHour() +
                ", minute=" + getMinute() +
                ", second=" + getSecond() +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", raimFlag=" + getRaimFlag() +
                ", communicationState=" + getCommunicationState() +
                "} " + super.toString();
    }

    private transient Integer year;
    private transient Integer month;
    private transient Integer day;
    private transient Integer hour;
    private transient Integer minute;
    private transient Integer second;
    private transient Boolean positionAccurate;
    private transient Float latitude;
    private transient Float longitude;
    private transient PositionFixingDevice positionFixingDevice;
    private transient Boolean raimFlag;
    private transient WeakReference<SOTDMACommunicationState> communicationState;
}
