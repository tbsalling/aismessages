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

import dk.tbsalling.aismessages.ais.BitStringParser;
import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

/**
 * Factory class that contains parsing logic for all AIS message types using BitStringParser.
 * This class separates the parsing concerns from the immutable value objects.
 *
 * @author tbsalling
 */
public class AISMessageFactory {

    static ShipAndVoyageData createShipAndVoyageData(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        IMO imo = new IMO(parser.getUnsignedInt(40, 70));
        String callsign = parser.getString(70, 112);
        String shipName = parser.getString(112, 232);
        ShipType shipType = ShipType.fromInteger(parser.getUnsignedInt(232, 240));
        int toBow = parser.getUnsignedInt(240, 249);
        int toStern = parser.getUnsignedInt(249, 258);
        int toPort = parser.getUnsignedInt(258, 264);
        int toStarboard = parser.getUnsignedInt(264, 270);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(parser.getUnsignedInt(270, 274));
        int etaMonth = parser.getUnsignedInt(274, 278);
        int etaDay = parser.getUnsignedInt(278, 283);
        int etaHour = parser.getUnsignedInt(283, 288);
        int etaMinute = parser.getUnsignedInt(288, 294);
        float draught = parser.getUnsignedFloat(294, 302) / 10f;
        String destination = parser.getString(302, 422);
        boolean dataTerminalReady = parser.getBoolean(422, 423);
        int rawDraught = parser.getUnsignedInt(294, 302);

        return new ShipAndVoyageData(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                imo, callsign, shipName, shipType, toBow, toStern, toPort, toStarboard,
                positionFixingDevice, etaMonth, etaDay, etaHour, etaMinute, draught, destination, dataTerminalReady, rawDraught);
    }

    static PositionReport createPositionReportClassAScheduled(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                              AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser,
                messageType, PositionReportClassAScheduled::new);
    }

    static PositionReport createPositionReportClassAAssignedSchedule(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                                     AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser,
                messageType, PositionReportClassAAssignedSchedule::new);
    }

    static PositionReport createPositionReportClassAResponseToInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                                            AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser,
                messageType, PositionReportClassAResponseToInterrogation::new);
    }

    @FunctionalInterface
    private interface PositionReportConstructor {
        PositionReport create(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                              NavigationStatus navigationStatus, int rateOfTurn, float speedOverGround,
                              boolean positionAccuracy, float latitude, float longitude,
                              float courseOverGround, int trueHeading, int second,
                              ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState,
                              int rawRateOfTurn, int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround);
    }

    private static PositionReport createPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                       AISMessageType messageType,
                                                       PositionReportConstructor constructor) {
        NavigationStatus navigationStatus = NavigationStatus.fromInteger(parser.getUnsignedInt(38, 42));
        int rawRateOfTurn = parser.getSignedInt(42, 50);
        int rateOfTurn = (int) (Math.signum(rawRateOfTurn) * Math.pow(rawRateOfTurn / 4.733, 2));
        int rawSpeedOverGround = parser.getUnsignedInt(50, 60);
        float speedOverGround = rawSpeedOverGround / 10f;
        boolean positionAccuracy = parser.getBoolean(60, 61);
        int rawLongitude = parser.getSignedInt(61, 89);
        float longitude = rawLongitude / 600000f;
        int rawLatitude = parser.getSignedInt(89, 116);
        float latitude = rawLatitude / 600000f;
        int rawCourseOverGround = parser.getUnsignedInt(116, 128);
        float courseOverGround = rawCourseOverGround / 10f;
        int trueHeading = parser.getUnsignedInt(128, 137);
        int second = parser.getUnsignedInt(137, 143);
        ManeuverIndicator specialManeuverIndicator = ManeuverIndicator.fromInteger(parser.getUnsignedInt(143, 145));
        boolean raimFlag = parser.getBoolean(148, 149);

        // Communication state depends on the message type
        CommunicationState communicationState;
        if (messageType == AISMessageType.PositionReportClassAScheduled || messageType == AISMessageType.PositionReportClassAAssignedSchedule) {
            communicationState = SOTDMACommunicationState.fromBitString(parser.getBitPattern(149, 168));
        } else {  // PositionReportClassAResponseToInterrogation
            communicationState = ITDMACommunicationState.fromBitString(parser.getBitPattern(149, 168));
        }

        return constructor.create(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                navigationStatus, rateOfTurn, speedOverGround, positionAccuracy, latitude, longitude,
                courseOverGround, trueHeading, second, specialManeuverIndicator, raimFlag, communicationState,
                rawRateOfTurn, rawSpeedOverGround, rawLatitude, rawLongitude, rawCourseOverGround);
    }

    static BaseStationReport createBaseStationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int year = parser.getUnsignedInt(38, 52);
        int month = parser.getUnsignedInt(52, 56);
        int day = parser.getUnsignedInt(56, 61);
        int hour = parser.getUnsignedInt(61, 66);
        int minute = parser.getUnsignedInt(66, 72);
        int second = parser.getUnsignedInt(72, 78);
        boolean positionAccurate = parser.getBoolean(78, 79);
        float longitude = parser.getSignedFloat(79, 107) / 600000f;
        float latitude = parser.getSignedFloat(107, 134) / 600000f;
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(parser.getUnsignedInt(134, 138));
        boolean raimFlag = parser.getBoolean(148, 149);
        SOTDMACommunicationState communicationState = SOTDMACommunicationState.fromBitString(parser.getBits(149, 168));

        return new BaseStationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                year, month, day, hour, minute, second, positionAccurate, latitude, longitude,
                positionFixingDevice, raimFlag, communicationState);
    }

    static AddressedBinaryMessage createAddressedBinaryMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int sequenceNumber = parser.getUnsignedInt(38, 40);
        MMSI destinationMmsi = new MMSI(parser.getUnsignedInt(40, 70));
        boolean retransmitFlag = parser.getBoolean(70, 71);
        int spare = parser.getUnsignedInt(71, 72);
        int designatedAreaCode = parser.getUnsignedInt(72, 82);
        int functionalId = parser.getUnsignedInt(82, 88);
        String binaryData = parser.getBitPattern(88, parser.getLength());
        ApplicationSpecificMessage applicationSpecificMessage = ApplicationSpecificMessage.create(designatedAreaCode, functionalId, binaryData);

        return new AddressedBinaryMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                sequenceNumber, destinationMmsi, retransmitFlag, spare, designatedAreaCode, functionalId, binaryData,
                applicationSpecificMessage);
    }

    static BinaryAcknowledge createBinaryAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);

        MMSI mmsi1 = new MMSI(parser.getUnsignedInt(40, 70));
        int sequence1 = parser.getUnsignedInt(70, 72);

        MMSI mmsi2 = null;
        Integer sequence2 = null;
        MMSI mmsi3 = null;
        Integer sequence3 = null;
        MMSI mmsi4 = null;
        Integer sequence4 = null;

        if (parser.getLength() >= 104) {
            mmsi2 = new MMSI(parser.getUnsignedInt(72, 102));
            sequence2 = parser.getUnsignedInt(102, 104);
        }

        if (parser.getLength() >= 136) {
            mmsi3 = new MMSI(parser.getUnsignedInt(104, 134));
            sequence3 = parser.getUnsignedInt(134, 136);
        }

        if (parser.getLength() >= 168) {
            mmsi4 = new MMSI(parser.getUnsignedInt(136, 166));
            sequence4 = parser.getUnsignedInt(166, 168);
        }

        // Calculate numOfAcks
        int numOfAcks = 1;
        if (parser.getLength() > 72) numOfAcks++;
        if (parser.getLength() > 104) numOfAcks++;
        if (parser.getLength() > 136) numOfAcks++;

        return new BinaryAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, mmsi1, sequence1, mmsi2, sequence2, mmsi3, sequence3, mmsi4, sequence4, numOfAcks);
    }

    static BinaryBroadcastMessage createBinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        Integer spare = parser.getUnsignedInt(38, 40);
        Integer designatedAreaCode = parser.getUnsignedInt(40, 50);
        Integer functionalId = parser.getUnsignedInt(50, 56);
        String binaryData = parser.getLength() > 56 ? parser.getBitPattern(56, parser.getLength()) : "";
        ApplicationSpecificMessage applicationSpecificMessage = ApplicationSpecificMessage.create(designatedAreaCode, functionalId, binaryData);

        return new BinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, designatedAreaCode, functionalId, binaryData, applicationSpecificMessage);
    }

    static StandardSARAircraftPositionReport createStandardSARAircraftPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int altitude = parser.getUnsignedInt(38, 50);
        int rawSpeedOverGround = parser.getUnsignedInt(50, 60);
        int speed = rawSpeedOverGround;
        boolean positionAccuracy = parser.getBoolean(60, 61);
        int rawLongitude = parser.getSignedInt(61, 89);
        float longitude = rawLongitude / 600000f;
        int rawLatitude = parser.getSignedInt(89, 116);
        float latitude = rawLatitude / 600000f;
        int rawCourseOverGround = parser.getUnsignedInt(116, 128);
        float courseOverGround = rawCourseOverGround / 10f;
        int second = parser.getUnsignedInt(128, 134);
        String regionalReserved = parser.getBitPattern(134, 142);
        boolean dataTerminalReady = parser.getBoolean(142, 143);
        boolean assigned = parser.getBoolean(146, 147);
        boolean raimFlag = parser.getBoolean(147, 148);
        String radioStatus = parser.getBitPattern(148, 168);

        return new StandardSARAircraftPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                altitude, speed, positionAccuracy, latitude, longitude, courseOverGround, second,
                regionalReserved, dataTerminalReady, assigned, raimFlag, radioStatus,
                rawSpeedOverGround, rawLongitude, rawLatitude, rawCourseOverGround);
    }

    static UTCAndDateInquiry createUTCAndDateInquiry(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        MMSI destinationMmsi = new MMSI(parser.getUnsignedInt(40, 70));

        return new UTCAndDateInquiry(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationMmsi);
    }

    static UTCAndDateResponse createUTCAndDateResponse(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int year = parser.getUnsignedInt(38, 52);
        int month = parser.getUnsignedInt(52, 56);
        int day = parser.getUnsignedInt(56, 61);
        int hour = parser.getUnsignedInt(61, 66);
        int minute = parser.getUnsignedInt(66, 72);
        int second = parser.getUnsignedInt(72, 78);
        boolean positionAccurate = parser.getBoolean(78, 79);
        float longitude = parser.getSignedFloat(79, 107) / 600000f;
        float latitude = parser.getSignedFloat(107, 134) / 600000f;
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(parser.getUnsignedInt(134, 138));
        boolean raimFlag = parser.getBoolean(148, 149);

        return new UTCAndDateResponse(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                year, month, day, hour, minute, second, positionAccurate, latitude, longitude,
                positionFixingDevice, raimFlag);
    }

    static AddressedSafetyRelatedMessage createAddressedSafetyRelatedMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int sequenceNumber = parser.getUnsignedInt(38, 40);
        MMSI destinationMmsi = new MMSI(parser.getUnsignedInt(40, 70));
        boolean retransmit = parser.getBoolean(70, 71);
        int spare = parser.getUnsignedInt(71, 72);
        String text = parser.getString(72, parser.getLength());

        return new AddressedSafetyRelatedMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                sequenceNumber, destinationMmsi, retransmit, spare, text);
    }

    static SafetyRelatedAcknowledge createSafetyRelatedAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);

        MMSI mmsi1 = new MMSI(parser.getUnsignedInt(40, 70));
        int sequence1 = parser.getUnsignedInt(70, 72);

        MMSI mmsi2 = null;
        int sequence2 = -1;
        MMSI mmsi3 = null;
        int sequence3 = -1;
        MMSI mmsi4 = null;
        int sequence4 = -1;

        if (parser.getLength() >= 104) {
            mmsi2 = new MMSI(parser.getUnsignedInt(72, 102));
            sequence2 = parser.getUnsignedInt(102, 104);
        }

        if (parser.getLength() >= 136) {
            mmsi3 = new MMSI(parser.getUnsignedInt(104, 134));
            sequence3 = parser.getUnsignedInt(134, 136);
        }

        if (parser.getLength() >= 168) {
            mmsi4 = new MMSI(parser.getUnsignedInt(136, 166));
            sequence4 = parser.getUnsignedInt(166, 168);
        }

        // Calculate numOfAcks
        int numOfAcks = 1;
        if (parser.getLength() > 72) numOfAcks++;
        if (parser.getLength() > 104) numOfAcks++;
        if (parser.getLength() > 136) numOfAcks++;

        return new SafetyRelatedAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, mmsi1, sequence1, mmsi2, sequence2, mmsi3, sequence3, mmsi4, sequence4, numOfAcks);
    }

    static SafetyRelatedBroadcastMessage createSafetyRelatedBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);
        String text = parser.getString(40, parser.getLength());

        return new SafetyRelatedBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, text);
    }

    static Interrogation createInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare1 = parser.getUnsignedInt(38, 40);
        MMSI interrogatedMmsi1 = new MMSI(parser.getUnsignedInt(40, 70));
        int type1_1 = parser.getUnsignedInt(70, 76);
        int offset1_1 = parser.getUnsignedInt(76, 88);

        int type1_2 = -1;
        int offset1_2 = -1;
        MMSI interrogatedMmsi2 = null;
        int type2_1 = -1;
        int offset2_1 = -1;

        if (parser.getLength() >= 110) {
            type1_2 = parser.getUnsignedInt(90, 96);
            offset1_2 = parser.getUnsignedInt(96, 108);
        }

        if (parser.getLength() >= 160) {
            interrogatedMmsi2 = new MMSI(parser.getUnsignedInt(120, 150));
            type2_1 = parser.getUnsignedInt(150, 156);
            offset2_1 = parser.getUnsignedInt(156, 162);
        }

        return new Interrogation(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                interrogatedMmsi1, type1_1, offset1_1, type1_2, offset1_2,
                interrogatedMmsi2, type2_1, offset2_1);
    }

    static AssignedModeCommand createAssignedModeCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);
        MMSI destinationMmsiA = new MMSI(parser.getUnsignedInt(40, 70));
        Integer offsetA = parser.getUnsignedInt(70, 82);
        Integer incrementA = parser.getUnsignedInt(82, 92);

        MMSI destinationMmsiB = null;
        Integer offsetB = null;
        Integer incrementB = null;

        if (parser.getLength() >= 144) {
            destinationMmsiB = new MMSI(parser.getUnsignedInt(92, 122));
            offsetB = parser.getUnsignedInt(122, 134);
            incrementB = parser.getUnsignedInt(134, 144);
        }

        return new AssignedModeCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationMmsiA, offsetA, incrementA, destinationMmsiB, offsetB, incrementB);
    }

    static GNSSBinaryBroadcastMessage createGNSSBinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare1 = parser.getUnsignedInt(38, 40);
        float longitude = parser.getSignedFloat(40, 58) / 10f;
        float latitude = parser.getSignedFloat(58, 75) / 10f;
        int spare2 = parser.getUnsignedInt(75, 80);

        Integer mType = null;
        Integer stationId = null;
        Integer zCount = null;
        Integer sequenceNumber = null;
        Integer numOfWords = null;
        Integer health = null;
        String binaryData = "";

        if (parser.getLength() > 80) {
            mType = parser.getUnsignedInt(80, 86);
            stationId = parser.getUnsignedInt(86, 96);
            zCount = parser.getUnsignedInt(96, 109);
            sequenceNumber = parser.getUnsignedInt(109, 112);
            numOfWords = parser.getUnsignedInt(112, 117);
            health = parser.getUnsignedInt(117, 120);
            binaryData = parser.getBitPattern(80, parser.getLength());
        }

        return new GNSSBinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare1, latitude, longitude, spare2, mType, stationId, zCount, sequenceNumber, numOfWords, health, binaryData);
    }

    static StandardClassBCSPositionReport createStandardClassBCSPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        String regionalReserved1 = parser.getBitPattern(38, 46);
        int rawSpeedOverGround = parser.getUnsignedInt(46, 56);
        float speedOverGround = rawSpeedOverGround / 10f;
        boolean positionAccuracy = parser.getBoolean(56, 57);
        int rawLongitude = parser.getSignedInt(57, 85);
        float longitude = rawLongitude / 600000f;
        int rawLatitude = parser.getSignedInt(85, 112);
        float latitude = rawLatitude / 600000f;
        int rawCourseOverGround = parser.getUnsignedInt(112, 124);
        float courseOverGround = rawCourseOverGround / 10f;
        int trueHeading = parser.getUnsignedInt(124, 133);
        int second = parser.getUnsignedInt(133, 139);
        String regionalReserved2 = parser.getBitPattern(139, 141);
        boolean csUnit = parser.getBoolean(141, 142);
        boolean display = parser.getBoolean(142, 143);
        boolean dsc = parser.getBoolean(143, 144);
        boolean band = parser.getBoolean(144, 145);
        boolean message22 = parser.getBoolean(145, 146);
        boolean assigned = parser.getBoolean(146, 147);
        boolean raimFlag = parser.getBoolean(147, 148);
        boolean commStateSelectorFlag = parser.getBoolean(148, 149);

        CommunicationState commState = commStateSelectorFlag
                ? ITDMACommunicationState.fromBitString(parser.getBits(149, 168))
                : SOTDMACommunicationState.fromBitString(parser.getBits(149, 168));

        return new StandardClassBCSPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                regionalReserved1, speedOverGround, positionAccuracy, latitude, longitude, courseOverGround,
                trueHeading, second, regionalReserved2, csUnit, display, dsc, band, message22, assigned,
                raimFlag, commStateSelectorFlag, commState,
                rawSpeedOverGround, rawLatitude, rawLongitude, rawCourseOverGround);
    }

    static ExtendedClassBEquipmentPositionReport createExtendedClassBEquipmentPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        String regionalReserved1 = parser.getBitPattern(38, 46);
        int rawSpeedOverGround = parser.getUnsignedInt(46, 56);
        float speedOverGround = rawSpeedOverGround / 10f;
        boolean positionAccuracy = parser.getBoolean(56, 57);
        int rawLongitude = parser.getSignedInt(57, 85);
        float longitude = rawLongitude / 600000f;
        int rawLatitude = parser.getSignedInt(85, 112);
        float latitude = rawLatitude / 600000f;
        int rawCourseOverGround = parser.getUnsignedInt(112, 124);
        float courseOverGround = rawCourseOverGround / 10f;
        int trueHeading = parser.getUnsignedInt(124, 133);
        int second = parser.getUnsignedInt(133, 139);
        String regionalReserved2 = parser.getBitPattern(139, 143);
        String shipName = parser.getString(143, 263);
        ShipType shipType = ShipType.fromInteger(parser.getUnsignedInt(263, 271));
        int toBow = parser.getUnsignedInt(271, 280);
        int toStern = parser.getUnsignedInt(280, 289);
        int toPort = parser.getUnsignedInt(289, 295);
        int toStarboard = parser.getUnsignedInt(295, 301);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(parser.getUnsignedInt(301, 305));
        boolean raimFlag = parser.getBoolean(305, 306);
        boolean dataTerminalReady = parser.getBoolean(306, 307);
        boolean assigned = parser.getBoolean(307, 308);
        String regionalReserved3 = parser.getBitPattern(308, 312);

        return new ExtendedClassBEquipmentPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                regionalReserved1, speedOverGround, positionAccuracy, latitude, longitude, courseOverGround, trueHeading,
                second, regionalReserved2, shipName, shipType, toBow, toStern, toPort, toStarboard,
                positionFixingDevice, raimFlag, dataTerminalReady, assigned, regionalReserved3,
                rawSpeedOverGround, rawLatitude, rawLongitude, rawCourseOverGround);
    }

    static DataLinkManagement createDataLinkManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);
        int offsetNumber1 = parser.getUnsignedInt(40, 52);
        int reservedSlots1 = parser.getUnsignedInt(52, 56);
        int timeout1 = parser.getUnsignedInt(56, 59);
        int increment1 = parser.getUnsignedInt(59, 70);

        Integer offsetNumber2 = null;
        Integer reservedSlots2 = null;
        Integer timeout2 = null;
        Integer increment2 = null;
        Integer offsetNumber3 = null;
        Integer reservedSlots3 = null;
        Integer timeout3 = null;
        Integer increment3 = null;
        Integer offsetNumber4 = null;
        Integer reservedSlots4 = null;
        Integer timeout4 = null;
        Integer increment4 = null;

        if (parser.getLength() >= 98) {
            offsetNumber2 = parser.getUnsignedInt(72, 84);
            reservedSlots2 = parser.getUnsignedInt(84, 88);
            timeout2 = parser.getUnsignedInt(88, 91);
            increment2 = parser.getUnsignedInt(91, 98);
        }

        if (parser.getLength() >= 126) {
            offsetNumber3 = parser.getUnsignedInt(100, 112);
            reservedSlots3 = parser.getUnsignedInt(112, 116);
            timeout3 = parser.getUnsignedInt(116, 119);
            increment3 = parser.getUnsignedInt(119, 126);
        }

        if (parser.getLength() >= 154) {
            offsetNumber4 = parser.getUnsignedInt(128, 140);
            reservedSlots4 = parser.getUnsignedInt(140, 144);
            timeout4 = parser.getUnsignedInt(144, 147);
            increment4 = parser.getUnsignedInt(147, 154);
        }

        return new DataLinkManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                offsetNumber1, reservedSlots1, timeout1, increment1,
                offsetNumber2, reservedSlots2, timeout2, increment2,
                offsetNumber3, reservedSlots3, timeout3, increment3,
                offsetNumber4, reservedSlots4, timeout4, increment4);
    }

    static AidToNavigationReport createAidToNavigationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        AidType aidType = AidType.fromInteger(parser.getUnsignedInt(38, 43));
        String name = parser.getString(43, 163);
        boolean positionAccurate = parser.getBoolean(163, 164);
        float longitude = parser.getSignedFloat(164, 192) / 600000f;
        float latitude = parser.getSignedFloat(192, 219) / 600000f;
        int toBow = parser.getUnsignedInt(219, 228);
        int toStern = parser.getUnsignedInt(228, 237);
        int toPort = parser.getUnsignedInt(237, 243);
        int toStarboard = parser.getUnsignedInt(243, 249);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(parser.getUnsignedInt(249, 253));
        int second = parser.getUnsignedInt(253, 259);
        boolean offPosition = parser.getBoolean(259, 260);
        String regionalUse = parser.getBitPattern(260, 268);
        boolean raimFlag = parser.getBoolean(268, 269);
        boolean virtualAid = parser.getBoolean(269, 270);
        boolean assignedMode = parser.getBoolean(270, 271);
        int spare1 = parser.getUnsignedInt(271, 272);

        String nameExtension = null;
        Integer spare2 = 0;

        if (parser.getLength() > 272) {
            int extraBits = parser.getLength() - 272;
            int extraChars = extraBits / 6;
            int extraBitsOfChars = extraChars * 6;
            if (extraBits > 0) {
                nameExtension = parser.getString(272, 272 + extraBitsOfChars);
                spare2 = (extraBits == extraBitsOfChars) ? 0 : parser.getUnsignedInt(272 + extraBitsOfChars, parser.getLength());
            }
        }

        return new AidToNavigationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                aidType, name, positionAccurate, latitude, longitude, toBow, toStern, toPort, toStarboard,
                positionFixingDevice, second, offPosition, regionalUse, raimFlag, virtualAid, assignedMode,
                spare1, nameExtension, spare2);
    }

    static ChannelManagement createChannelManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare1 = parser.getUnsignedInt(38, 40);
        int channelA = parser.getUnsignedInt(40, 52);
        int channelB = parser.getUnsignedInt(52, 64);
        TxRxMode transmitReceiveMode = TxRxMode.fromInteger(parser.getUnsignedInt(64, 68));
        boolean power = parser.getBoolean(68, 69);

        // Check if addressed or broadcast mode
        boolean addressed = parser.getBoolean(139, 140);

        Float northEastLongitude = null;
        Float northEastLatitude = null;
        Float southWestLongitude = null;
        Float southWestLatitude = null;
        MMSI destinationMmsi1 = null;
        MMSI destinationMmsi2 = null;

        if (!addressed) {
            // Broadcast mode - geographic coordinates
            northEastLongitude = parser.getSignedFloat(69, 87) / 10f;
            northEastLatitude = parser.getSignedFloat(87, 104) / 10f;
            southWestLongitude = parser.getSignedFloat(104, 122) / 10f;
            southWestLatitude = parser.getSignedFloat(122, 139) / 10f;
        } else {
            // Addressed mode - MMSIs
            destinationMmsi1 = new MMSI(parser.getUnsignedInt(69, 99));
            destinationMmsi2 = new MMSI(parser.getUnsignedInt(104, 134));
        }

        boolean bandA = parser.getBoolean(140, 141);
        boolean bandB = parser.getBoolean(141, 142);
        int zoneSize = parser.getUnsignedInt(142, 145);

        return new ChannelManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                channelA, channelB, transmitReceiveMode, power, northEastLongitude, northEastLatitude,
                southWestLongitude, southWestLatitude, destinationMmsi1, destinationMmsi2, addressed,
                bandA, bandB, zoneSize);
    }

    static GroupAssignmentCommand createGroupAssignmentCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        String spare1 = parser.getString(38, 40);
        float northEastLongitude = parser.getSignedFloat(40, 58) / 10f;
        float northEastLatitude = parser.getSignedFloat(58, 75) / 10f;
        float southWestLongitude = parser.getSignedFloat(75, 93) / 10f;
        float southWestLatitude = parser.getSignedFloat(93, 110) / 10f;
        StationType stationType = StationType.fromInteger(parser.getUnsignedInt(110, 114));
        ShipType shipType = ShipType.fromInteger(parser.getUnsignedInt(114, 122));
        String spare2 = parser.getString(122, 160);
        TxRxMode transmitReceiveMode = TxRxMode.fromInteger(parser.getUnsignedInt(166, 168));
        ReportingInterval reportingInterval = ReportingInterval.fromInteger(parser.getUnsignedInt(168, 172));
        int quietTime = parser.getUnsignedInt(172, 176);

        return new GroupAssignmentCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare1, northEastLongitude, northEastLatitude, southWestLongitude, southWestLatitude,
                stationType, shipType, spare2, transmitReceiveMode, reportingInterval, quietTime);
    }

    static ClassBCSStaticDataReport createClassBCSStaticDataReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);
        int partNumber = parser.getUnsignedInt(38, 40);

        // Fields vary depending on part number
        String shipName = null;
        ShipType shipType = null;
        String vendorId = null;
        String callsign = null;
        int toBow = -1;
        int toStern = -1;
        int toStarboard = -1;
        int toPort = -1;
        MMSI mothershipMmsi = null;

        if (partNumber == 0) {
            shipName = parser.getString(40, 160);
        } else if (partNumber == 1) {
            shipType = ShipType.fromInteger(parser.getUnsignedInt(40, 48));
            vendorId = parser.getString(48, 90);
            callsign = parser.getString(90, 132);
            toBow = parser.getUnsignedInt(132, 141);
            toStern = parser.getUnsignedInt(141, 150);
            toPort = parser.getUnsignedInt(150, 156);
            toStarboard = parser.getUnsignedInt(156, 162);

            int mmsiValue = parser.getUnsignedInt(132, 162);
            if (mmsiValue != 0) {
                mothershipMmsi = new MMSI(mmsiValue);
            }
        }

        return new ClassBCSStaticDataReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                partNumber, shipName, shipType, vendorId, callsign, toBow, toStern, toStarboard, toPort, mothershipMmsi);
    }

    static BinaryMessageSingleSlot createBinaryMessageSingleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        boolean destinationIndicator = parser.getBoolean(38, 39);
        boolean binaryDataFlag = parser.getBoolean(39, 40);
        MMSI destinationMMSI = new MMSI(parser.getUnsignedInt(40, 70));
        String binaryData = parser.getBitPattern(40, 168);

        return new BinaryMessageSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationIndicator, binaryDataFlag, destinationMMSI, binaryData);
    }

    static BinaryMessageMultipleSlot createBinaryMessageMultipleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        boolean addressed = parser.getBoolean(38, 39);
        boolean structured = parser.getBoolean(39, 40);

        MMSI destinationMmsi = new MMSI(parser.getUnsignedInt(40, 70));

        int applicationId = parser.getUnsignedInt(70, 86);

        // Extract data and pad with zeros to match expected length (1005 bits)
        int maxDataBits = 1004 + 1;  // 86 to 1091
        int availableBits = Math.max(0, parser.getLength() - 86);
        String data = parser.getBitPattern(86, 86 + Math.min(availableBits, maxDataBits));
        // Pad with zeros if message is shorter than max
        if (data.length() < maxDataBits) {
            data = data + "0".repeat(maxDataBits - data.length());
        }

        return new BinaryMessageMultipleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                addressed, structured, destinationMmsi, applicationId, data);
    }

    static LongRangeBroadcastMessage createLongRangeBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        boolean positionAccuracy = parser.getBoolean(38, 39);
        boolean raim = parser.getBoolean(39, 40);
        NavigationStatus status = NavigationStatus.fromInteger(parser.getUnsignedInt(40, 44));
        int rawLongitude = parser.getSignedInt(44, 62);
        float longitude = rawLongitude / 600f;
        int rawLatitude = parser.getSignedInt(62, 79);
        float latitude = rawLatitude / 600f;
        int rawSpeedOverGround = parser.getUnsignedInt(79, 85);
        int speed = rawSpeedOverGround;
        int rawCourseOverGround = parser.getUnsignedInt(85, 94);
        int course = rawCourseOverGround;
        int positionLatency = parser.getUnsignedInt(94, 95);
        int spare = parser.getUnsignedInt(95, 96);

        return new LongRangeBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                positionAccuracy, raim, status, latitude, longitude, speed, course, positionLatency, spare,
                rawLongitude, rawLatitude, rawSpeedOverGround, rawCourseOverGround);
    }
}
