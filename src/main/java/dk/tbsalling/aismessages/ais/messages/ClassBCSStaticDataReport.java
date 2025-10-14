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

import java.util.stream.IntStream;

import static java.lang.String.format;

@SuppressWarnings("serial")
public class ClassBCSStaticDataReport extends AISMessage implements StaticDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ClassBCSStaticDataReport(Metadata metadata, int repeatIndicator, MMSI sourceMmsi,
                                       int partNumber, String shipName, ShipType shipType,
                                       String vendorId, String callsign,
                                       Integer toBow, Integer toStern, Integer toStarboard, Integer toPort,
                                       MMSI mothershipMmsi) {
        super(metadata, repeatIndicator, sourceMmsi);
        this.partNumber = partNumber;
        this.shipName = shipName;
        this.shipType = shipType;
        this.vendorId = vendorId;
        this.callsign = callsign;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toStarboard = toStarboard;
        this.toPort = toPort;
        this.mothershipMmsi = mothershipMmsi;
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
    public int getPartNumber() {
        return partNumber;
	}

    @SuppressWarnings("unused")
	public String getShipName() {
        return shipName;
	}

    @SuppressWarnings("unused")
	public ShipType getShipType() {
        return shipType;
	}

    @SuppressWarnings("unused")
	public String getVendorId() {
        return vendorId;
	}

    @SuppressWarnings("unused")
	public String getCallsign() {
        return callsign;
	}

    @SuppressWarnings("unused")
    public int getToBow() {
        return toBow != null ? toBow : 0;
	}

    @SuppressWarnings("unused")
    public int getToStern() {
        return toStern != null ? toStern : 0;
	}

    @SuppressWarnings("unused")
    public int getToStarboard() {
        return toStarboard != null ? toStarboard : 0;
	}

    @SuppressWarnings("unused")
    public int getToPort() {
        return toPort != null ? toPort : 0;
	}

    @SuppressWarnings("unused")
	public MMSI getMothershipMmsi() {
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

    private final int partNumber;
    private final String shipName;
    private final ShipType shipType;
    private final String vendorId;
    private final String callsign;
    private final Integer toBow;
    private final Integer toStern;
    private final Integer toStarboard;
    private final Integer toPort;
    private final MMSI mothershipMmsi;

}
