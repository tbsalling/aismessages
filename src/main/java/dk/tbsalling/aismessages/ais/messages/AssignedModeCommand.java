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

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

/**
 * used by a base station with control authority to configure the scheduling of
 * AIS informational messages from subordinate stations, either as a frequency
 * per 10-minute interval or by specifying the TDMA slot(s) offset on which
 * those messages should be transmitted.
 *
 * @author tbsalling
 */
public class AssignedModeCommand extends AISMessage {

    private transient MMSI destinationMmsiA;
    private transient Integer offsetA;
    private transient Integer incrementA;
    private transient MMSI destinationMmsiB;
    private transient Integer offsetB;
    private transient Integer incrementB;

    public AssignedModeCommand(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected AssignedModeCommand(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    protected void checkAISMessage() {
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AssignedModeCommand;
    }

    @SuppressWarnings("unused")
    public MMSI getDestinationMmsiA() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return destinationMmsiA;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                destinationMmsiA = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(AssignedModeCommand.this.getBits(40, 70)));
            }
        });
    }

    @SuppressWarnings("unused")
    public Integer getOffsetA() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offsetA;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offsetA = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AssignedModeCommand.this.getBits(70, 82));
            }
        });
    }

    @SuppressWarnings("unused")
    public Integer getIncrementA() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return incrementA;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                incrementA = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AssignedModeCommand.this.getBits(82, 92));
            }
        });
    }

    @SuppressWarnings("unused")
    public MMSI getDestinationMmsiB() {
        return getDecodedValue(new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return destinationMmsiB;
            }
        }, new Consumer<MMSI>() {
            @Override
            public void accept(MMSI value) {
                destinationMmsiB = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return AssignedModeCommand.this.getNumberOfBits() >= 144;
            }
        }, new Supplier<MMSI>() {
            @Override
            public MMSI get() {
                return MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(AssignedModeCommand.this.getBits(92, 122)));
            }
        });
    }

    @SuppressWarnings("unused")
    public Integer getOffsetB() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return offsetB;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                offsetB = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return AssignedModeCommand.this.getNumberOfBits() >= 144;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AssignedModeCommand.this.getBits(122, 134));
            }
        });
    }

    @SuppressWarnings("unused")
    public Integer getIncrementB() {
        return getDecodedValue(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return incrementB;
            }
        }, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                incrementB = value;
            }
        }, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return AssignedModeCommand.this.getNumberOfBits() >= 144;
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return UNSIGNED_INTEGER_DECODER.apply(AssignedModeCommand.this.getBits(134, 144));
            }
        });
    }

    @Override
    public String toString() {
        return "AssignedModeCommand{" +
                "messageType=" + getMessageType() +
                ", destinationMmsiA=" + getDestinationMmsiA() +
                ", offsetA=" + getOffsetA() +
                ", incrementA=" + getIncrementA() +
                ", destinationMmsiB=" + getDestinationMmsiB() +
                ", offsetB=" + getOffsetB() +
                ", incrementB=" + getIncrementB() +
                "} " + super.toString();
    }
}
