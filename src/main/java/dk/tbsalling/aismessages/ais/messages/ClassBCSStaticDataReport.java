/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.ShipType;
import dk.tbsalling.aismessages.ais.messages.types.TransponderClass;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.util.stream.IntStream;

import static dk.tbsalling.aismessages.ais.Decoders.STRING_DECODER;
import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.lang.String.format;

@SuppressWarnings("serial")
public class ClassBCSStaticDataReport extends AISMessage implements StaticDataReport {

    public ClassBCSStaticDataReport(NMEAMessage[] nmeaMessages) {
        super(nmeaMessages);
    }

    protected ClassBCSStaticDataReport(NMEAMessage[] nmeaMessages, String bitString) {
        super(nmeaMessages, bitString);
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (IntStream.of(158, 160, 166, 168).noneMatch(l -> numberOfBits == l))
            errorMessage.append(format("Message of type %s should be exactly 158, 160, 166 or 168 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
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
        return getDecodedValue(() -> partNumber, value -> partNumber = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(38, 40)));
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        return getDecodedValue(() -> shipName, value -> shipName = value, () -> getPartNumber() == 0, () -> STRING_DECODER.apply(getBits(40, 160)));
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return getDecodedValue(() -> shipType, value -> shipType = value, () -> getPartNumber() == 1, () -> ShipType.fromInteger(UNSIGNED_INTEGER_DECODER.apply(getBits(40, 48))));
	}

    @SuppressWarnings("unused")
	public String getVendorId() {
        return getDecodedValue(() -> vendorId, value -> vendorId = value, () -> getPartNumber() == 1, () -> STRING_DECODER.apply(getBits(48, 90)));
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        return getDecodedValue(() -> callsign, value -> callsign = value, () -> getPartNumber() == 1, () -> STRING_DECODER.apply(getBits(90, 132)));
	}

    @SuppressWarnings("unused")
	public Integer getToBow() {
        return getDecodedValue(() -> toBow, value -> toBow = value, () -> getPartNumber() == 1, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(132, 141)));
	}

    @SuppressWarnings("unused")
	public Integer getToStern() {
        return getDecodedValue(() -> toStern, value -> toStern = value, () -> getPartNumber() == 1, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(141, 150)));
	}

    @SuppressWarnings("unused")
	public Integer getToStarboard() {
        return getDecodedValue(() -> toStarboard, value -> toStarboard = value, () -> getPartNumber() == 1, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(156, 162)));
	}

    @SuppressWarnings("unused")
	public Integer getToPort() {
        return getDecodedValue(() -> toPort, value -> toPort = value, () -> getPartNumber() == 1, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(150, 156)));
	}

    @SuppressWarnings("unused")
	public MMSI getMothershipMmsi() {
        return getDecodedValue(() -> mothershipMmsi, value -> mothershipMmsi = value, () -> getPartNumber() == 1, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(132, 162))));
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
