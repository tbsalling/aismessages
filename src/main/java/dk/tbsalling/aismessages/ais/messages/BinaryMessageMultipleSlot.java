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
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Consumer;
import dk.tbsalling.aismessages.dk.tbsalling.util.function.Supplier;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.BIT_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.BOOLEAN_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class BinaryMessageMultipleSlot extends AISMessage {

    public BinaryMessageMultipleSlot(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected BinaryMessageMultipleSlot(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.BinaryMessageMultipleSlot;
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
                return BOOLEAN_DECODER.apply(BinaryMessageMultipleSlot.this.getBits(38, 39));
            }
        });
    }

    @SuppressWarnings("unused")
    public Boolean getStructured() {
        return getDecodedValue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return structured;
            }
        }, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean value) {
                structured = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return BOOLEAN_DECODER.apply(BinaryMessageMultipleSlot.this.getBits(39, 40));
            }
        });
    }

    @SuppressWarnings("unused")
    public MMSI getDestinationMmsi() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return destinationMmsi;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                destinationMmsi = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(BinaryMessageMultipleSlot.this.getBits(40, 70)));
            }
        });
    }

    @SuppressWarnings("unused")
    public Integer getApplicationId() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return applicationId;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                applicationId = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(BinaryMessageMultipleSlot.this.getBits(70, 86));
            }
        });
    }

    @SuppressWarnings("unused")
    public String getData() {
        return getDecodedValue(new Supplier<String>() {
            @Override
            public String get() {
                return data;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String value) {
                data = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return BIT_DECODER.apply(BinaryMessageMultipleSlot.this.getBits(86, 86 + 1004 + 1));
            }
        });
    }

    @SuppressWarnings("unused")
    public String getRadioStatus() {
        return null; // BIT_DECODER.apply(getBits(6, 8));
    }

    @Override
    public String toString() {
        return "BinaryMessageMultipleSlot{" +
                "messageType=" + getMessageType() +
                ", addressed=" + getAddressed() +
                ", structured=" + getStructured() +
                ", destinationMmsi=" + getDestinationMmsi() +
                ", applicationId=" + getApplicationId() +
                ", data='" + getData() + '\'' +
                ", radioStatus='" + getRadioStatus() + '\'' +
                "} " + super.toString();
    }

    private transient Boolean addressed;
    private transient Boolean structured;
    private transient MMSI destinationMmsi;
    private transient Integer applicationId;
    private transient String data;
    // private transient String radioStatus;
}
