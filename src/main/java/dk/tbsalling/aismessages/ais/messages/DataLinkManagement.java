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

import java.util.logging.Logger;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;


/**
 * This message is used to pre-allocate TDMA slots within an AIS base station
 * network. It contains no navigational information, and is unlikely to be of
 * interest unless you are implementing or studying an AIS base station network.
 * 
 * @author tbsalling
 * 
 */

@SuppressWarnings("serial")
public class DataLinkManagement extends AISMessage {

    private static final Logger log = Logger.getLogger(DataLinkManagement.class.getName());

    public DataLinkManagement(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected DataLinkManagement(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.DataLinkManagement;
    }

    @SuppressWarnings("unused")
	public Integer getOffsetNumber1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offsetNumber1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offsetNumber1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(40, 52));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return reservedSlots1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                reservedSlots1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(52, 56));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getTimeout1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return timeout1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                timeout1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(56, 59));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getIncrement1() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return increment1;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                increment1 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(59, 70));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offsetNumber2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offsetNumber2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 100;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(70, 82));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return reservedSlots2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                reservedSlots2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 100;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(82, 86));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getTimeout2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return timeout2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                timeout2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 100;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(86, 89));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getIncrement2() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return increment2;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                increment2 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 100;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(89, 100));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber3() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offsetNumber3;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offsetNumber3 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 130;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(100, 112));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots3() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return reservedSlots3;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                reservedSlots3 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 130;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(112, 116));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getTimeout3() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return timeout3;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                timeout3 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 130;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(116, 119));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getIncrement3() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return increment3;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                increment3 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 130;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(119, 130));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getOffsetNumber4() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offsetNumber4;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offsetNumber4 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 160;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(130, 142));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getReservedSlots4() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return reservedSlots4;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                reservedSlots4 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 160;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(142, 146));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getTimeout4() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return timeout4;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                timeout4 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 160;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(146, 149));
            }
        });
	}

    @SuppressWarnings("unused")
	public Integer getIncrement4() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return increment4;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                increment4 = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return DataLinkManagement.this.getNumberOfBits() >= 160;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(DataLinkManagement.this.getBits(149, 160));
            }
        });
	}

    @Override
    public String toString() {
        return "DataLinkManagement{" +
                "messageType=" + getMessageType() +
                ", offsetNumber1=" + getOffsetNumber1() +
                ", reservedSlots1=" + getReservedSlots1() +
                ", timeout1=" + getTimeout1() +
                ", increment1=" + getIncrement1() +
                ", offsetNumber2=" + getOffsetNumber2() +
                ", reservedSlots2=" + getReservedSlots2() +
                ", timeout2=" + getTimeout2() +
                ", increment2=" + getIncrement2() +
                ", offsetNumber3=" + getOffsetNumber3() +
                ", reservedSlots3=" + getReservedSlots3() +
                ", timeout3=" + getTimeout3() +
                ", increment3=" + getIncrement3() +
                ", offsetNumber4=" + getOffsetNumber4() +
                ", reservedSlots4=" + getReservedSlots4() +
                ", timeout4=" + getTimeout4() +
                ", increment4=" + getIncrement4() +
                "} " + super.toString();
    }

    private transient Integer offsetNumber1;
    private transient Integer reservedSlots1;
    private transient Integer timeout1;
    private transient Integer increment1;
    private transient Integer offsetNumber2;
    private transient Integer reservedSlots2;
    private transient Integer timeout2;
    private transient Integer increment2;
    private transient Integer offsetNumber3;
    private transient Integer reservedSlots3;
    private transient Integer timeout3;
    private transient Integer increment3;
    private transient Integer offsetNumber4;
    private transient Integer reservedSlots4;
    private transient Integer timeout4;
    private transient Integer increment4;
}
