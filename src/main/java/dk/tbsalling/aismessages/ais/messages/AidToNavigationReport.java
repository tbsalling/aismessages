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
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.FLOAT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 *
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class AidToNavigationReport extends AISMessage {

    public AidToNavigationReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AidToNavigationReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AidToNavigationReport;
    }

    @SuppressWarnings("unused")
    public AidType getAidType() {
        return getDecodedValue(new Supplier<AidType>() {
            @Override
            public AidType get() {
                return aidType;
            }
        }, new Consumer<AidType>() {
            @Override
            public void accept(AidType value) {
                aidType = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<AidType>() {
            @Override
            public AidType get() {
                return AidType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(38, 43)));
            }
        });
    }

    @SuppressWarnings("unused")
    public String getName() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return name;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                name = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return STRING_DECODER.apply(AidToNavigationReport.this.getBits(43, 163));
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
                return BOOLEAN_DECODER.apply(AidToNavigationReport.this.getBits(163, 164));
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
                return FLOAT_DECODER.apply(AidToNavigationReport.this.getBits(192, 219))/600000f;
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
                return FLOAT_DECODER.apply(AidToNavigationReport.this.getBits(164, 192))/600000f;
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
                return UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(219, 228));
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
                return UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(228, 237));
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
                return UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(243, 249));
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
                return UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(237, 243));
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
                return PositionFixingDevice.fromInteger(UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(249, 253)));
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
                return UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(253, 259));
            }
        });
    }

    @SuppressWarnings("unused")
    public Boolean getOffPosition() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return offPosition;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                offPosition = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(AidToNavigationReport.this.getBits(259, 260));
            }
        });
    }

    @SuppressWarnings("unused")
    public String getRegionalUse() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return regionalUse;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                regionalUse = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return BIT_DECODER.apply(AidToNavigationReport.this.getBits(260, 268));
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
                return BOOLEAN_DECODER.apply(AidToNavigationReport.this.getBits(268, 269));
            }
        });
    }

    @SuppressWarnings("unused")
    public Boolean getVirtualAid() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return virtualAid;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                virtualAid = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(AidToNavigationReport.this.getBits(269, 270));
            }
        });
    }

    @SuppressWarnings("unused")
    public Boolean getAssignedMode() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return assignedMode;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                assignedMode = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(AidToNavigationReport.this.getBits(270, 271));
            }
        });
    }

    @SuppressWarnings("unused")
    public int getSpare1() {
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
                return UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(271, 272));
            }
        });
    }

    @SuppressWarnings("unused")
    public String getNameExtension() {
        getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return nameExtension;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                nameExtension = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return AidToNavigationReport.this.getNumberOfBits() > 272;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                int extraBits = AidToNavigationReport.this.getNumberOfBits() - 272;
                int extraChars = extraBits/6;
                int extraBitsOfChars = extraChars*6;
                return STRING_DECODER.apply(AidToNavigationReport.this.getBits(272, 272 + extraBitsOfChars));
            }
        });
        return nameExtension;
    }

    @SuppressWarnings("unused")
    public int getSpare2() {
        getDecodedValue(new Supplier<Integer>() {
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
                return AidToNavigationReport.this.getNumberOfBits() >= 272;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                int extraBits = AidToNavigationReport.this.getNumberOfBits() - 272;
                int extraChars = extraBits/6;
                int extraBitsOfChars = extraChars*6;
                return (extraBits == extraBitsOfChars) ? 0 : UNSIGNED_INTEGER_DECODER.apply(AidToNavigationReport.this.getBits(272 + extraBitsOfChars, getNumberOfBits()));
            }
        });
        return spare2;
    }

    @Override
    public String toString() {
        return "AidToNavigationReport{" +
                "messageType=" + getMessageType() +
                ", aidType=" + getAidType() +
                ", name='" + getName() + '\'' +
                ", positionAccurate=" + getPositionAccurate() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toPort=" + getToPort() +
                ", toStarboard=" + getToStarboard() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", second=" + getSecond() +
                ", offPosition=" + getOffPosition() +
                ", regionalUse='" + getRegionalUse() + '\'' +
                ", raimFlag=" + getRaimFlag() +
                ", virtualAid=" + getVirtualAid() +
                ", assignedMode=" + getAssignedMode() +
                ", spare1=" + getSpare1() +
                ", nameExtension='" + getNameExtension() + '\'' +
                ", spare2=" + getSpare2() +
                "} " + super.toString();
    }

    private transient AidType aidType;
    private transient String name;
    private transient Boolean positionAccurate;
    private transient Float latitude;
    private transient Float longitude;
    private transient Integer toBow;
    private transient Integer toStern;
    private transient Integer toPort;
    private transient Integer toStarboard;
    private transient PositionFixingDevice positionFixingDevice;
    private transient Integer second;
    private transient Boolean offPosition;
    private transient String regionalUse;
    private transient Boolean raimFlag;
    private transient Boolean virtualAid;
    private transient Boolean assignedMode;
    private transient Integer spare1;
    private transient String nameExtension;
    private transient Integer spare2;
}
