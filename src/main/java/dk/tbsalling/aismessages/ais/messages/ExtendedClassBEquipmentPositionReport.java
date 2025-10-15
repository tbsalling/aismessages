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
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
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
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().intValue()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.ExtendedClassBEquipmentPositionReport;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.B;
    }

    String regionalReserved1;
    float speedOverGround;
    boolean positionAccuracy;
    float latitude;
    float longitude;
    float courseOverGround;
    int trueHeading;
    int second;
    String regionalReserved2;
    String shipName;
    ShipType shipType;
    int toBow;
    int toStern;
    int toStarboard;
    int toPort;
    PositionFixingDevice positionFixingDevice;
    boolean raimFlag;
    boolean dataTerminalReady;
    boolean assigned;
    String regionalReserved3;
    int rawSpeedOverGround;
    int rawLatitude;
    int rawLongitude;
    int rawCourseOverGround;
}
