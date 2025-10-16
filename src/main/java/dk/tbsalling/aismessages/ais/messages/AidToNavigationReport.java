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
import dk.tbsalling.aismessages.ais.messages.types.AidType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.ais.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

import static java.lang.String.format;

/**
 * Identification and location message to be emitted by aids to navigation such as buoys and lighthouses.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AidToNavigationReport extends AISMessage {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     */
    protected AidToNavigationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                                    AidType aidType, String name, boolean positionAccurate,
                                    float latitude, float longitude,
                                    int toBow, int toStern, int toPort, int toStarboard,
                                    PositionFixingDevice positionFixingDevice, int second,
                                    boolean offPosition, String regionalUse, boolean raimFlag,
                                    boolean virtualAid, boolean assignedMode, int spare1,
                                    String nameExtension, Integer spare2) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.aidType = aidType;
        this.name = name;
        this.positionAccurate = positionAccurate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toPort = toPort;
        this.toStarboard = toStarboard;
        this.positionFixingDevice = positionFixingDevice;
        this.second = second;
        this.offPosition = offPosition;
        this.regionalUse = regionalUse;
        this.raimFlag = raimFlag;
        this.virtualAid = virtualAid;
        this.assignedMode = assignedMode;
        this.spare1 = spare1;
        this.nameExtension = nameExtension;
        this.spare2 = spare2;
    }

    @Override
    protected void checkAISMessage() {
        super.checkAISMessage();

        final StringBuilder errorMessage = new StringBuilder();

        final int numberOfBits = getNumberOfBits();
        if (numberOfBits < 272 || numberOfBits > 360)
            errorMessage.append(format("Message of type %s should be 272-360 bits long; not %d.", getMessageType(), numberOfBits));

        if (errorMessage.length() > 0) {
            if (numberOfBits >= 38)
                errorMessage.append(format(" Assumed sourceMmsi: %d.", getSourceMmsi().getMmsi()));

            throw new InvalidMessage(errorMessage.toString());
        }
    }

    public final AISMessageType getMessageType() {
        return AISMessageType.AidToNavigationReport;
    }

    AidType aidType;
    String name;
    boolean positionAccurate;
    float latitude;
    float longitude;
    int toBow;
    int toStern;
    int toPort;
    int toStarboard;
    PositionFixingDevice positionFixingDevice;
    int second;
    boolean offPosition;
    String regionalUse;
    boolean raimFlag;
    boolean virtualAid;
    boolean assignedMode;
    int spare1;
    String nameExtension;
    int spare2;
}
