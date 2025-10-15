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

import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

import static java.lang.String.format;

@SuppressWarnings("serial")
public class ExtendedClassBEquipmentPositionReport extends AISMessage implements ExtendedDynamicDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected ExtendedClassBEquipmentPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                                    String regionalReserved1, float speedOverGround, boolean positionAccuracy,
                                                    float latitude, float longitude, float courseOverGround, int trueHeading,
                                                    int second, String regionalReserved2, String shipName, ShipType shipType,
                                                    int toBow, int toStern, int toPort, int toStarboard,
                                                    PositionFixingDevice positionFixingDevice, boolean raimFlag,
                                                    boolean dataTerminalReady, boolean assigned, String regionalReserved3,
                                                    int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.regionalReserved1 = regionalReserved1;
        this.speedOverGround = speedOverGround;
        this.positionAccuracy = positionAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseOverGround = courseOverGround;
        this.trueHeading = trueHeading;
        this.second = second;
        this.regionalReserved2 = regionalReserved2;
        this.shipName = shipName;
        this.shipType = shipType;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toPort = toPort;
        this.toStarboard = toStarboard;
        this.positionFixingDevice = positionFixingDevice;
        this.raimFlag = raimFlag;
        this.dataTerminalReady = dataTerminalReady;
        this.assigned = assigned;
        this.regionalReserved3 = regionalReserved3;
        this.rawSpeedOverGround = rawSpeedOverGround;
        this.rawLatitude = rawLatitude;
        this.rawLongitude = rawLongitude;
        this.rawCourseOverGround = rawCourseOverGround;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();

        if (numberOfBits != 312)
            errorMessage.append(format("Message of type %s should be at exactly 312 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMMSI()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.ExtendedClassBEquipmentPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    @SuppressWarnings("unused")
	public String getRegionalReserved1() {
        return regionalReserved1;
	}

    @SuppressWarnings("unused")
    public float getSpeedOverGround() {
        return speedOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawSpeedOverGround() {
        return rawSpeedOverGround;
    }

    @SuppressWarnings("unused")
    public boolean getPositionAccuracy() {
        return positionAccuracy;
	}

    @SuppressWarnings("unused")
    public boolean getPositionAccurate() {
        return getPositionAccuracy();
    }

    @SuppressWarnings("unused")
    public float getLatitude() {
        return latitude;
	}

    @SuppressWarnings("unused")
    public int getRawLatitude() {
        return rawLatitude;
    }

    @SuppressWarnings("unused")
    public float getLongitude() {
        return longitude;
	}

    @SuppressWarnings("unused")
    public int getRawLongitude() {
        return rawLongitude;
    }

    @SuppressWarnings("unused")
    public float getCourseOverGround() {
        return courseOverGround;
	}

    @SuppressWarnings("unused")
    public int getRawCourseOverGround() {
        return rawCourseOverGround;
    }

    @SuppressWarnings("unused")
    public int getTrueHeading() {
        return trueHeading;
	}

    @SuppressWarnings("unused")
    public int getSecond() {
        return second;
	}

    @SuppressWarnings("unused")
	public String getRegionalReserved2() {
        return regionalReserved2;
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
    public int getToBow() {
        return toBow;
	}

    @SuppressWarnings("unused")
    public int getToStern() {
        return toStern;
	}

    @SuppressWarnings("unused")
    public int getToStarboard() {
        return toStarboard;
	}

    @SuppressWarnings("unused")
    public int getToPort() {
        return toPort;
	}

    @SuppressWarnings("unused")
	public PositionFixingDevice getPositionFixingDevice() {
        return positionFixingDevice;
	}

    @SuppressWarnings("unused")
    public boolean getRaimFlag() {
        return raimFlag;
	}

    @SuppressWarnings("unused")
    public boolean getDataTerminalReady() {
        return dataTerminalReady;
	}

    @SuppressWarnings("unused")
    public boolean getAssigned() {
        return assigned;
	}

    @SuppressWarnings("unused")
    public String getRegionalReserved3() {
        return regionalReserved3;
    }

    @Override
    public String toString() {
        return "ExtendedClassBEquipmentPositionReport{" +
                "messageType=" + getMessageType() +
                ", regionalReserved1='" + getRegionalReserved1() + '\'' +
                ", speedOverGround=" + getSpeedOverGround() +
                ", positionAccuracy=" + getPositionAccuracy() +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", courseOverGround=" + getCourseOverGround() +
                ", trueHeading=" + getTrueHeading() +
                ", second=" + getSecond() +
                ", regionalReserved2='" + getRegionalReserved2() + '\'' +
                ", shipName='" + getShipName() + '\'' +
                ", shipType=" + getShipType() +
                ", toBow=" + getToBow() +
                ", toStern=" + getToStern() +
                ", toStarboard=" + getToStarboard() +
                ", toPort=" + getToPort() +
                ", positionFixingDevice=" + getPositionFixingDevice() +
                ", raimFlag=" + getRaimFlag() +
                ", dataTerminalReady=" + getDataTerminalReady() +
                ", assigned=" + getAssigned() +
                "} " + super.toString();
    }

    private final String regionalReserved1;
    private final float speedOverGround;
    private final boolean positionAccuracy;
    private final float latitude;
    private final float longitude;
    private final float courseOverGround;
    private final int trueHeading;
    private final int second;
    private final String regionalReserved2;
    private final String shipName;
    private final ShipType shipType;
    private final int toBow;
    private final int toStern;
    private final int toStarboard;
    private final int toPort;
    private final PositionFixingDevice positionFixingDevice;
    private final boolean raimFlag;
    private final boolean dataTerminalReady;
    private final boolean assigned;
    private final String regionalReserved3;
    private final int rawSpeedOverGround;
    private final int rawLatitude;
    private final int rawLongitude;
    private final int rawCourseOverGround;
}
