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
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_LONG_DECODER;

@SuppressWarnings("serial")
public class ClassBCSStaticDataReport extends AISMessage {

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

    @SuppressWarnings("unused")
	public Integer getPartNumber() {
        if (partNumber == null) {
            partNumber = UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40));
        }
        return partNumber;
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        if (getPartNumber() == 0 && shipName == null) {
            shipName = STRING_DECODER.apply(getBits(40, 160));
        }
        return shipName;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        if (getPartNumber() != 0 && shipType == null) {
            shipType = ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 48)));
        }
        return shipType;
	}

    @SuppressWarnings("unused")
	public String getVendorId() {
        if (getPartNumber() != 0 && vendorId == null) {
            vendorId = STRING_DECODER.apply(getBits(48, 90));
        }
        return vendorId;
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        if (getPartNumber() != 0 && callsign == null) {
            callsign = STRING_DECODER.apply(getBits(90, 132));
        }
        return callsign;
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        if (getPartNumber() != 0 && toBow == null) {
            toBow = UNSIGNED_INTEGER_DECODER.apply(getBits(132, 141));
        }
        return toBow;
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        if (getPartNumber() != 0 && toStern == null) {
            toStern = UNSIGNED_INTEGER_DECODER.apply(getBits(141, 150));
        }
        return toStern;
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        if (getPartNumber() != 0 && toStarboard == null) {
            toStarboard = UNSIGNED_INTEGER_DECODER.apply(getBits(156, 162));
        }
        return toStarboard;
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        if (getPartNumber() != 0 && toPort == null) {
            toPort = UNSIGNED_INTEGER_DECODER.apply(getBits(150, 156));
        }
        return toPort;
	}

    @SuppressWarnings("unused")
	public MMSI getMothershipMmsi() {
        if (getPartNumber() != 0 && mothershipMmsi == null) {
            mothershipMmsi = MMSI.valueOf(UNSIGNED_LONG_DECODER.apply(getBits(132, 162)));
        }
        return mothershipMmsi;
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
