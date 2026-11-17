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

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import dk.tbsalling.aismessages.ais.messages.asm.ApplicationSpecificMessage;
import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.NMEAArmouring;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;

import java.time.Instant;

/**
 * Factory class that contains parsing logic for all AIS message types using BitString.
 * This class separates the parsing concerns from the immutable value objects.
 *
 * @author tbsalling
 */
public class AISMessageFactory {

    /**
     * Create proper type of AISMessage from 1..n NMEA messages, and attach metadata.
     */
    public static AISMessage create(Instant received, String source, NMEATagBlock nmeaTagBlock, NMEAMessage... nmeaMessages) {
        BitString bitString;
        try {
            bitString = decodePayloadToBitString(nmeaMessages);
        } catch (IllegalArgumentException e) {
            throw new dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage("Cannot decode payload of NMEA message: %s: %s".formatted(concatenate(nmeaMessages), e.getMessage()));
        }

        AISMessageType messageType = AISMessageType.fromInteger(bitString.getUnsignedInt(0, 6));
        if (messageType == null) {
            throw new dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage("Cannot extract message type from NMEA message: %s".formatted(concatenate(nmeaMessages)));
        }

        int repeatIndicator = bitString.getUnsignedInt(6, 8);
        MMSI sourceMmsi = new MMSI(bitString.getUnsignedInt(8, 38));

        try {
            return createByType(messageType, sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
        } catch (IllegalArgumentException e) {
            // A payload too short for its message type makes a field slice fail before the message's own length check can run.
            throw new dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage("Message of type %s has an invalid payload of %d bits: %s. Assumed sourceMmsi: %d.".formatted(messageType, bitString.length(), e.getMessage(), sourceMmsi.getMmsi()));
        }
    }

    private static String concatenate(NMEAMessage[] nmeaMessages) {
        StringBuilder sb = new StringBuilder();
        for (NMEAMessage nmeaMessage : nmeaMessages) {
            sb.append(nmeaMessage);
        }
        return sb.toString();
    }

    private static ShipAndVoyageData createShipAndVoyageData(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        AisVersionIndicator aisVersionIndicator = AisVersionIndicator.fromInteger(bitString.getUnsignedInt(38, 40));
        int rawImo = bitString.getUnsignedInt(40, 70);
        IMO imo = rawImo == 0 ? null : new IMO(rawImo);
        String callsign = FieldDecoder.text(AISText.decode(bitString, 70, 112));
        String shipName = FieldDecoder.text(AISText.decode(bitString, 112, 232));
        ShipType shipType = ShipType.fromInteger(bitString.getUnsignedInt(232, 240));
        int toBow = bitString.getUnsignedInt(240, 249);
        int toStern = bitString.getUnsignedInt(249, 258);
        int toPort = bitString.getUnsignedInt(258, 264);
        int toStarboard = bitString.getUnsignedInt(264, 270);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(bitString.getUnsignedInt(270, 274));
        Integer etaMonth = FieldDecoder.valueOrNull(bitString.getUnsignedInt(274, 278), 0);
        Integer etaDay = FieldDecoder.valueOrNull(bitString.getUnsignedInt(278, 283), 0);
        Integer etaHour = FieldDecoder.valueOrNull(bitString.getUnsignedInt(283, 288), 24);
        Integer etaMinute = FieldDecoder.valueOrNull(bitString.getUnsignedInt(288, 294), 60);
        int rawDraught = bitString.getUnsignedInt(294, 302);
        float draught = FieldDecoder.draught(rawDraught);
        DraughtIndicator draughtIndicator = FieldDecoder.draughtIndicator(rawDraught);
        String destination = FieldDecoder.text(AISText.decode(bitString, 302, 422));
        boolean dataTerminalReady = bitString.getBoolean(422, 423);

        return new ShipAndVoyageData(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                imo, callsign, shipName, shipType, toBow, toStern, toPort, toStarboard,
                positionFixingDevice, etaMonth, etaDay, etaHour, etaMinute, draught, draughtIndicator, destination, dataTerminalReady, aisVersionIndicator);
    }

    private static PositionReport createPositionReportClassAScheduled(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                              AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                messageType, PositionReportClassAScheduled::new);
    }

    private static PositionReport createPositionReportClassAAssignedSchedule(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                                     AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                messageType, PositionReportClassAAssignedSchedule::new);
    }

    private static PositionReport createPositionReportClassAResponseToInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                                            AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                messageType, PositionReportClassAResponseToInterrogation::new);
    }

    private static PositionReport createPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                                       AISMessageType messageType,
                                                       PositionReportConstructor constructor) {
        NavigationStatus navigationStatus = NavigationStatus.fromInteger(bitString.getUnsignedInt(38, 42));
        int rawRateOfTurn = bitString.getSignedInt(42, 50);
        RateOfTurnIndicator rateOfTurnIndicator = RateOfTurnIndicator.fromRawRateOfTurn(rawRateOfTurn);
        float rateOfTurn = FieldDecoder.rateOfTurn(rawRateOfTurn);
        int rawSpeedOverGround = bitString.getUnsignedInt(50, 60);
        float speedOverGround = FieldDecoder.speedOverGround(rawSpeedOverGround, 1022, 10f);
        SpeedOverGroundIndicator speedOverGroundIndicator = FieldDecoder.speedOverGroundIndicator(rawSpeedOverGround, 1022);
        boolean positionAccuracy = bitString.getBoolean(60, 61);
        int rawLongitude = bitString.getSignedInt(61, 89);
        double longitude = FieldDecoder.longitude(rawLongitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawLatitude = bitString.getSignedInt(89, 116);
        double latitude = FieldDecoder.latitude(rawLatitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawCourseOverGround = bitString.getUnsignedInt(116, 128);
        float courseOverGround = FieldDecoder.courseOverGroundTenths(rawCourseOverGround);
        Integer trueHeading = FieldDecoder.trueHeading(bitString.getUnsignedInt(128, 137));
        int rawSecond = bitString.getUnsignedInt(137, 143);
        Integer second = FieldDecoder.timeStampSecond(rawSecond);
        TimeStampIndicator timeStampIndicator = FieldDecoder.timeStampIndicator(rawSecond);
        ManeuverIndicator specialManeuverIndicator = ManeuverIndicator.fromInteger(bitString.getUnsignedInt(143, 145));
        boolean transmitPowerLow = bitString.getBoolean(147, 148);
        boolean raimFlag = bitString.getBoolean(148, 149);

        // Communication state depends on the message type
        CommunicationState communicationState = switch (messageType) {
            case PositionReportClassAScheduled, PositionReportClassAAssignedSchedule ->
                    SOTDMACommunicationState.fromBitString(bitString.slice(149, 168));
            case PositionReportClassAResponseToInterrogation ->
                    ITDMACommunicationState.fromBitString(bitString.slice(149, 168));
            default ->
                    throw new IllegalArgumentException("Unsupported message type for PositionReport: " + messageType);
        };

        return constructor.create(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                navigationStatus, rateOfTurn, rateOfTurnIndicator, speedOverGround, speedOverGroundIndicator, positionAccuracy, latitude, longitude,
                courseOverGround, trueHeading, second, timeStampIndicator, specialManeuverIndicator, raimFlag, communicationState, transmitPowerLow);
    }

    private static BaseStationReport createBaseStationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        Integer year = FieldDecoder.valueOrNull(bitString.getUnsignedInt(38, 52), 0);
        Integer month = FieldDecoder.valueOrNull(bitString.getUnsignedInt(52, 56), 0);
        Integer day = FieldDecoder.valueOrNull(bitString.getUnsignedInt(56, 61), 0);
        Integer hour = FieldDecoder.valueOrNull(bitString.getUnsignedInt(61, 66), 24);
        Integer minute = FieldDecoder.valueOrNull(bitString.getUnsignedInt(66, 72), 60);
        Integer second = FieldDecoder.valueOrNull(bitString.getUnsignedInt(72, 78), 60);
        boolean positionAccurate = bitString.getBoolean(78, 79);
        double longitude = FieldDecoder.longitude(bitString.getSignedInt(79, 107), FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        double latitude = FieldDecoder.latitude(bitString.getSignedInt(107, 134), FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(bitString.getUnsignedInt(134, 138));
        boolean satelliteBroadcastTransmissionControl = bitString.getBoolean(138, 139);
        boolean raimFlag = bitString.getBoolean(148, 149);
        SOTDMACommunicationState communicationState = SOTDMACommunicationState.fromBitString(bitString.slice(149, 168));

        return new BaseStationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                year, month, day, hour, minute, second, positionAccurate, latitude, longitude,
                positionFixingDevice, raimFlag, communicationState, satelliteBroadcastTransmissionControl);
    }

    private static AddressedBinaryMessage createAddressedBinaryMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int sequenceNumber = bitString.getUnsignedInt(38, 40);
        MMSI destinationMmsi = new MMSI(bitString.getUnsignedInt(40, 70));
        boolean retransmitFlag = bitString.getBoolean(70, 71);
        int spare = bitString.getUnsignedInt(71, 72);
        int designatedAreaCode = bitString.getUnsignedInt(72, 82);
        int functionalId = bitString.getUnsignedInt(82, 88);
        BitString binaryData = bitString.slice(88, bitString.length());
        ApplicationSpecificMessage applicationSpecificMessage = ApplicationSpecificMessage.create(designatedAreaCode, functionalId, binaryData);

        return new AddressedBinaryMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                sequenceNumber, destinationMmsi, retransmitFlag, spare, designatedAreaCode, functionalId, binaryData,
                applicationSpecificMessage);
    }

    private static BinaryAcknowledge createBinaryAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare = bitString.getUnsignedInt(38, 40);

        MMSI mmsi1 = new MMSI(bitString.getUnsignedInt(40, 70));
        int sequence1 = bitString.getUnsignedInt(70, 72);

        MMSI mmsi2 = null;
        Integer sequence2 = null;
        MMSI mmsi3 = null;
        Integer sequence3 = null;
        MMSI mmsi4 = null;
        Integer sequence4 = null;

        if (bitString.length() >= 104) {
            mmsi2 = new MMSI(bitString.getUnsignedInt(72, 102));
            sequence2 = bitString.getUnsignedInt(102, 104);
        }

        if (bitString.length() >= 136) {
            mmsi3 = new MMSI(bitString.getUnsignedInt(104, 134));
            sequence3 = bitString.getUnsignedInt(134, 136);
        }

        if (bitString.length() >= 168) {
            mmsi4 = new MMSI(bitString.getUnsignedInt(136, 166));
            sequence4 = bitString.getUnsignedInt(166, 168);
        }

        int numOfAcks = 1;
        if (bitString.length() > 72) numOfAcks++;
        if (bitString.length() > 104) numOfAcks++;
        if (bitString.length() > 136) numOfAcks++;

        return new BinaryAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, mmsi1, sequence1, mmsi2, sequence2, mmsi3, sequence3, mmsi4, sequence4, numOfAcks);
    }

    private static BinaryBroadcastMessage createBinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        Integer spare = bitString.getUnsignedInt(38, 40);
        Integer designatedAreaCode = bitString.getUnsignedInt(40, 50);
        Integer functionalId = bitString.getUnsignedInt(50, 56);
        BitString binaryData = bitString.length() > 56 ? bitString.slice(56, bitString.length()) : BitString.EMPTY;
        ApplicationSpecificMessage applicationSpecificMessage = ApplicationSpecificMessage.create(designatedAreaCode, functionalId, binaryData);

        return new BinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, designatedAreaCode, functionalId, binaryData, applicationSpecificMessage);
    }

    private static StandardSARAircraftPositionReport createStandardSARAircraftPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int rawAltitude = bitString.getUnsignedInt(38, 50);
        Integer altitude = FieldDecoder.valueOrNull(rawAltitude, 4095);
        AltitudeIndicator altitudeIndicator = FieldDecoder.altitudeIndicator(rawAltitude);
        int rawSpeedOverGround = bitString.getUnsignedInt(50, 60);
        float speed = FieldDecoder.speedOverGround(rawSpeedOverGround, 1022, 1f);
        SpeedOverGroundIndicator speedOverGroundIndicator = FieldDecoder.speedOverGroundIndicator(rawSpeedOverGround, 1022);
        boolean positionAccuracy = bitString.getBoolean(60, 61);
        int rawLongitude = bitString.getSignedInt(61, 89);
        double longitude = FieldDecoder.longitude(rawLongitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawLatitude = bitString.getSignedInt(89, 116);
        double latitude = FieldDecoder.latitude(rawLatitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawCourseOverGround = bitString.getUnsignedInt(116, 128);
        float courseOverGround = FieldDecoder.courseOverGroundTenths(rawCourseOverGround);
        int rawSecond = bitString.getUnsignedInt(128, 134);
        Integer second = FieldDecoder.timeStampSecond(rawSecond);
        TimeStampIndicator timeStampIndicator = FieldDecoder.timeStampIndicator(rawSecond);
        boolean barometricAltitude = bitString.getBoolean(134, 135);
        int regionalReserved = bitString.getUnsignedInt(134, 142);
        boolean dataTerminalReady = bitString.getBoolean(142, 143);
        boolean assigned = bitString.getBoolean(146, 147);
        boolean raimFlag = bitString.getBoolean(147, 148);
        int radioStatus = bitString.getUnsignedInt(148, 168);
        boolean commStateSelectorFlag = bitString.getBoolean(148, 149);
        CommunicationState communicationState = commStateSelectorFlag
                ? ITDMACommunicationState.fromBitString(bitString.slice(149, 168))
                : SOTDMACommunicationState.fromBitString(bitString.slice(149, 168));

        return new StandardSARAircraftPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                altitude, altitudeIndicator, speed, speedOverGroundIndicator, positionAccuracy, latitude, longitude, courseOverGround, second, timeStampIndicator,
                regionalReserved, dataTerminalReady, assigned, raimFlag, radioStatus,
                barometricAltitude, commStateSelectorFlag, communicationState);
    }

    private static UTCAndDateInquiry createUTCAndDateInquiry(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        MMSI destinationMmsi = new MMSI(bitString.getUnsignedInt(40, 70));

        return new UTCAndDateInquiry(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationMmsi);
    }

    private static UTCAndDateResponse createUTCAndDateResponse(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        Integer year = FieldDecoder.valueOrNull(bitString.getUnsignedInt(38, 52), 0);
        Integer month = FieldDecoder.valueOrNull(bitString.getUnsignedInt(52, 56), 0);
        Integer day = FieldDecoder.valueOrNull(bitString.getUnsignedInt(56, 61), 0);
        Integer hour = FieldDecoder.valueOrNull(bitString.getUnsignedInt(61, 66), 24);
        Integer minute = FieldDecoder.valueOrNull(bitString.getUnsignedInt(66, 72), 60);
        Integer second = FieldDecoder.valueOrNull(bitString.getUnsignedInt(72, 78), 60);
        boolean positionAccurate = bitString.getBoolean(78, 79);
        double longitude = FieldDecoder.longitude(bitString.getSignedInt(79, 107), FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        double latitude = FieldDecoder.latitude(bitString.getSignedInt(107, 134), FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(bitString.getUnsignedInt(134, 138));
        boolean satelliteBroadcastTransmissionControl = bitString.getBoolean(138, 139);
        boolean raimFlag = bitString.getBoolean(148, 149);
        SOTDMACommunicationState communicationState = SOTDMACommunicationState.fromBitString(bitString.slice(149, 168));

        return new UTCAndDateResponse(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                year, month, day, hour, minute, second, positionAccurate, latitude, longitude,
                positionFixingDevice, raimFlag, satelliteBroadcastTransmissionControl, communicationState);
    }

    private static AddressedSafetyRelatedMessage createAddressedSafetyRelatedMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int sequenceNumber = bitString.getUnsignedInt(38, 40);
        MMSI destinationMmsi = new MMSI(bitString.getUnsignedInt(40, 70));
        boolean retransmit = bitString.getBoolean(70, 71);
        int spare = bitString.getUnsignedInt(71, 72);
        String text = AISText.decode(bitString, 72, bitString.length());

        return new AddressedSafetyRelatedMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                sequenceNumber, destinationMmsi, retransmit, spare, text);
    }

    private static SafetyRelatedAcknowledge createSafetyRelatedAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare = bitString.getUnsignedInt(38, 40);

        MMSI mmsi1 = new MMSI(bitString.getUnsignedInt(40, 70));
        int sequence1 = bitString.getUnsignedInt(70, 72);

        MMSI mmsi2 = null;
        Integer sequence2 = null;
        MMSI mmsi3 = null;
        Integer sequence3 = null;
        MMSI mmsi4 = null;
        Integer sequence4 = null;

        if (bitString.length() >= 104) {
            mmsi2 = new MMSI(bitString.getUnsignedInt(72, 102));
            sequence2 = bitString.getUnsignedInt(102, 104);
        }

        if (bitString.length() >= 136) {
            mmsi3 = new MMSI(bitString.getUnsignedInt(104, 134));
            sequence3 = bitString.getUnsignedInt(134, 136);
        }

        if (bitString.length() >= 168) {
            mmsi4 = new MMSI(bitString.getUnsignedInt(136, 166));
            sequence4 = bitString.getUnsignedInt(166, 168);
        }

        int numOfAcks = 1;
        if (bitString.length() > 72) numOfAcks++;
        if (bitString.length() > 104) numOfAcks++;
        if (bitString.length() > 136) numOfAcks++;

        return new SafetyRelatedAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, mmsi1, sequence1, mmsi2, sequence2, mmsi3, sequence3, mmsi4, sequence4, numOfAcks);
    }

    private static SafetyRelatedBroadcastMessage createSafetyRelatedBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare = bitString.getUnsignedInt(38, 40);
        String text = AISText.decode(bitString, 40, bitString.length());

        return new SafetyRelatedBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, text);
    }

    private static Interrogation createInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare1 = bitString.getUnsignedInt(38, 40);
        MMSI interrogatedMmsi1 = new MMSI(bitString.getUnsignedInt(40, 70));
        AISMessageType type1_1 = AISMessageType.fromInteger(bitString.getUnsignedInt(70, 76));
        int offset1_1 = bitString.getUnsignedInt(76, 88);

        AISMessageType type1_2 = null;
        Integer offset1_2 = null;
        MMSI interrogatedMmsi2 = null;
        AISMessageType type2_1 = null;
        Integer offset2_1 = null;

        if (bitString.length() >= 110) {
            type1_2 = AISMessageType.fromInteger(bitString.getUnsignedInt(90, 96));
            offset1_2 = bitString.getUnsignedInt(96, 108);
        }

        if (bitString.length() >= 160) {
            interrogatedMmsi2 = new MMSI(bitString.getUnsignedInt(110, 140));
            type2_1 = AISMessageType.fromInteger(bitString.getUnsignedInt(140, 146));
            offset2_1 = bitString.getUnsignedInt(146, 158);
        }

        return new Interrogation(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                interrogatedMmsi1, type1_1, offset1_1, type1_2, offset1_2,
                interrogatedMmsi2, type2_1, offset2_1);
    }

    private static AssignedModeCommand createAssignedModeCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare = bitString.getUnsignedInt(38, 40);
        MMSI destinationMmsiA = new MMSI(bitString.getUnsignedInt(40, 70));
        Integer offsetA = bitString.getUnsignedInt(70, 82);
        Integer incrementA = bitString.getUnsignedInt(82, 92);

        MMSI destinationMmsiB = null;
        Integer offsetB = null;
        Integer incrementB = null;

        if (bitString.length() >= 144) {
            destinationMmsiB = new MMSI(bitString.getUnsignedInt(92, 122));
            offsetB = bitString.getUnsignedInt(122, 134);
            incrementB = bitString.getUnsignedInt(134, 144);
        }

        return new AssignedModeCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationMmsiA, offsetA, incrementA, destinationMmsiB, offsetB, incrementB);
    }

    private static GNSSBinaryBroadcastMessage createGNSSBinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare1 = bitString.getUnsignedInt(38, 40);
        double longitude = FieldDecoder.longitude(bitString.getSignedInt(40, 58), FieldDecoder.TENTHS_OF_MINUTE);
        double latitude = FieldDecoder.latitude(bitString.getSignedInt(58, 75), FieldDecoder.TENTHS_OF_MINUTE);
        int spare2 = bitString.getUnsignedInt(75, 80);

        Integer mType = null;
        Integer stationId = null;
        Integer zCount = null;
        Integer sequenceNumber = null;
        Integer numOfWords = null;
        Integer health = null;
        BitString binaryData = BitString.EMPTY;

        if (bitString.length() > 80) {
            mType = bitString.getUnsignedInt(80, 86);
            stationId = bitString.getUnsignedInt(86, 96);
            zCount = bitString.getUnsignedInt(96, 109);
            sequenceNumber = bitString.getUnsignedInt(109, 112);
            numOfWords = bitString.getUnsignedInt(112, 117);
            health = bitString.getUnsignedInt(117, 120);
            binaryData = bitString.slice(80, bitString.length());
        }

        return new GNSSBinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare1, latitude, longitude, spare2, mType, stationId, zCount, sequenceNumber, numOfWords, health, binaryData);
    }

    private static StandardClassBCSPositionReport createStandardClassBCSPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int regionalReserved1 = bitString.getUnsignedInt(38, 46);
        int rawSpeedOverGround = bitString.getUnsignedInt(46, 56);
        float speedOverGround = FieldDecoder.speedOverGround(rawSpeedOverGround, 1022, 10f);
        SpeedOverGroundIndicator speedOverGroundIndicator = FieldDecoder.speedOverGroundIndicator(rawSpeedOverGround, 1022);
        boolean positionAccuracy = bitString.getBoolean(56, 57);
        int rawLongitude = bitString.getSignedInt(57, 85);
        double longitude = FieldDecoder.longitude(rawLongitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawLatitude = bitString.getSignedInt(85, 112);
        double latitude = FieldDecoder.latitude(rawLatitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawCourseOverGround = bitString.getUnsignedInt(112, 124);
        float courseOverGround = FieldDecoder.courseOverGroundTenths(rawCourseOverGround);
        Integer trueHeading = FieldDecoder.trueHeading(bitString.getUnsignedInt(124, 133));
        int rawSecond = bitString.getUnsignedInt(133, 139);
        Integer second = FieldDecoder.timeStampSecond(rawSecond);
        TimeStampIndicator timeStampIndicator = FieldDecoder.timeStampIndicator(rawSecond);
        boolean transmitPowerLow = bitString.getBoolean(139, 140);
        int regionalReserved2 = bitString.getUnsignedInt(139, 141);
        boolean csUnit = bitString.getBoolean(141, 142);
        boolean display = bitString.getBoolean(142, 143);
        boolean dsc = bitString.getBoolean(143, 144);
        boolean band = bitString.getBoolean(144, 145);
        boolean message22 = bitString.getBoolean(145, 146);
        boolean assigned = bitString.getBoolean(146, 147);
        boolean raimFlag = bitString.getBoolean(147, 148);
        boolean commStateSelectorFlag = bitString.getBoolean(148, 149);

        CommunicationState commState = commStateSelectorFlag
                ? ITDMACommunicationState.fromBitString(bitString.slice(149, 168))
                : SOTDMACommunicationState.fromBitString(bitString.slice(149, 168));

        return new StandardClassBCSPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                regionalReserved1, speedOverGround, speedOverGroundIndicator, positionAccuracy, latitude, longitude, courseOverGround,
                trueHeading, second, timeStampIndicator, regionalReserved2, csUnit, display, dsc, band, message22, assigned,
                raimFlag, commStateSelectorFlag, commState, transmitPowerLow);
    }

    private static ExtendedClassBEquipmentPositionReport createExtendedClassBEquipmentPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int regionalReserved1 = bitString.getUnsignedInt(38, 46);
        int rawSpeedOverGround = bitString.getUnsignedInt(46, 56);
        float speedOverGround = FieldDecoder.speedOverGround(rawSpeedOverGround, 1022, 10f);
        SpeedOverGroundIndicator speedOverGroundIndicator = FieldDecoder.speedOverGroundIndicator(rawSpeedOverGround, 1022);
        boolean positionAccuracy = bitString.getBoolean(56, 57);
        int rawLongitude = bitString.getSignedInt(57, 85);
        double longitude = FieldDecoder.longitude(rawLongitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawLatitude = bitString.getSignedInt(85, 112);
        double latitude = FieldDecoder.latitude(rawLatitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawCourseOverGround = bitString.getUnsignedInt(112, 124);
        float courseOverGround = FieldDecoder.courseOverGroundTenths(rawCourseOverGround);
        Integer trueHeading = FieldDecoder.trueHeading(bitString.getUnsignedInt(124, 133));
        int rawSecond = bitString.getUnsignedInt(133, 139);
        Integer second = FieldDecoder.timeStampSecond(rawSecond);
        TimeStampIndicator timeStampIndicator = FieldDecoder.timeStampIndicator(rawSecond);
        int regionalReserved2 = bitString.getUnsignedInt(139, 143);
        String shipName = FieldDecoder.text(AISText.decode(bitString, 143, 263));
        ShipType shipType = ShipType.fromInteger(bitString.getUnsignedInt(263, 271));
        int toBow = bitString.getUnsignedInt(271, 280);
        int toStern = bitString.getUnsignedInt(280, 289);
        int toPort = bitString.getUnsignedInt(289, 295);
        int toStarboard = bitString.getUnsignedInt(295, 301);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(bitString.getUnsignedInt(301, 305));
        boolean raimFlag = bitString.getBoolean(305, 306);
        boolean dataTerminalReady = bitString.getBoolean(306, 307);
        boolean assigned = bitString.getBoolean(307, 308);
        int regionalReserved3 = bitString.getUnsignedInt(308, 312);

        return new ExtendedClassBEquipmentPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                regionalReserved1, speedOverGround, speedOverGroundIndicator, positionAccuracy, latitude, longitude, courseOverGround, trueHeading,
                second, timeStampIndicator, regionalReserved2, shipName, shipType, toBow, toStern, toPort, toStarboard,
                positionFixingDevice, raimFlag, dataTerminalReady, assigned, regionalReserved3);
    }

    private static DataLinkManagement createDataLinkManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare = bitString.getUnsignedInt(38, 40);
        int offsetNumber1 = bitString.getUnsignedInt(40, 52);
        int reservedSlots1 = bitString.getUnsignedInt(52, 56);
        int timeout1 = bitString.getUnsignedInt(56, 59);
        int increment1 = bitString.getUnsignedInt(59, 70);

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

        if (bitString.length() >= 100) {
            offsetNumber2 = bitString.getUnsignedInt(70, 82);
            reservedSlots2 = bitString.getUnsignedInt(82, 86);
            timeout2 = bitString.getUnsignedInt(86, 89);
            increment2 = bitString.getUnsignedInt(89, 100);
        }

        if (bitString.length() >= 130) {
            offsetNumber3 = bitString.getUnsignedInt(100, 112);
            reservedSlots3 = bitString.getUnsignedInt(112, 116);
            timeout3 = bitString.getUnsignedInt(116, 119);
            increment3 = bitString.getUnsignedInt(119, 130);
        }

        if (bitString.length() >= 160) {
            offsetNumber4 = bitString.getUnsignedInt(130, 142);
            reservedSlots4 = bitString.getUnsignedInt(142, 146);
            timeout4 = bitString.getUnsignedInt(146, 149);
            increment4 = bitString.getUnsignedInt(149, 160);
        }

        return new DataLinkManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                offsetNumber1, reservedSlots1, timeout1, increment1,
                offsetNumber2, reservedSlots2, timeout2, increment2,
                offsetNumber3, reservedSlots3, timeout3, increment3,
                offsetNumber4, reservedSlots4, timeout4, increment4);
    }

    private static AidToNavigationReport createAidToNavigationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        AidType aidType = AidType.fromInteger(bitString.getUnsignedInt(38, 43));
        String name = FieldDecoder.text(AISText.decode(bitString, 43, 163));
        boolean positionAccurate = bitString.getBoolean(163, 164);
        double longitude = FieldDecoder.longitude(bitString.getSignedInt(164, 192), FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        double latitude = FieldDecoder.latitude(bitString.getSignedInt(192, 219), FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int toBow = bitString.getUnsignedInt(219, 228);
        int toStern = bitString.getUnsignedInt(228, 237);
        int toPort = bitString.getUnsignedInt(237, 243);
        int toStarboard = bitString.getUnsignedInt(243, 249);
        PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(bitString.getUnsignedInt(249, 253));
        int rawSecond = bitString.getUnsignedInt(253, 259);
        Integer second = FieldDecoder.timeStampSecond(rawSecond);
        TimeStampIndicator timeStampIndicator = FieldDecoder.timeStampIndicator(rawSecond);
        Boolean offPosition = timeStampIndicator == TimeStampIndicator.Available ? bitString.getBoolean(259, 260) : null;
        int regionalUse = bitString.getUnsignedInt(260, 268);
        boolean raimFlag = bitString.getBoolean(268, 269);
        boolean virtualAid = bitString.getBoolean(269, 270);
        boolean assignedMode = bitString.getBoolean(270, 271);
        int spare1 = bitString.getUnsignedInt(271, 272);

        String nameExtension = null;
        boolean nameExtensionLabel = false;
        Integer spare2 = 0;

        if (bitString.length() > 272) {
            int extraBits = bitString.length() - 272;
            int extraChars = extraBits / 6;
            int extraBitsOfChars = extraChars * 6;
            if (extraBits > 0) {
                // ITU-R M.1371-6 Table 71: an extension preceded by "@@@" is to be portrayed as the AtoN's label, not as part of its name.
                int textStart = 272;
                if (extraChars >= 3 && bitString.getUnsignedInt(272, 290) == 0) {
                    nameExtensionLabel = true;
                    textStart = 290;
                }
                nameExtension = FieldDecoder.text(AISText.decode(bitString, textStart, 272 + extraBitsOfChars));
                spare2 = (extraBits == extraBitsOfChars) ? 0 : bitString.getUnsignedInt(272 + extraBitsOfChars, bitString.length());
            }
        }

        return new AidToNavigationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                aidType, name, positionAccurate, latitude, longitude, toBow, toStern, toPort, toStarboard,
                positionFixingDevice, second, timeStampIndicator, offPosition, regionalUse, raimFlag, virtualAid, assignedMode,
                spare1, nameExtension, spare2, nameExtensionLabel);
    }

    private static ChannelManagement createChannelManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare1 = bitString.getUnsignedInt(38, 40);
        int channelA = bitString.getUnsignedInt(40, 52);
        int channelB = bitString.getUnsignedInt(52, 64);
        TxRxMode transmitReceiveMode = TxRxMode.fromInteger(bitString.getUnsignedInt(64, 68));
        boolean power = bitString.getBoolean(68, 69);

        boolean addressed = bitString.getBoolean(139, 140);

        Double northEastLongitude = null;
        Double northEastLatitude = null;
        Double southWestLongitude = null;
        Double southWestLatitude = null;
        MMSI destinationMmsi1 = null;
        MMSI destinationMmsi2 = null;

        if (!addressed) {
            northEastLongitude = FieldDecoder.orNull(FieldDecoder.longitude(bitString.getSignedInt(69, 87), FieldDecoder.TENTHS_OF_MINUTE));
            northEastLatitude = FieldDecoder.orNull(FieldDecoder.latitude(bitString.getSignedInt(87, 104), FieldDecoder.TENTHS_OF_MINUTE));
            southWestLongitude = bitString.getSignedInt(104, 122) / 600.0;
            southWestLatitude = bitString.getSignedInt(122, 139) / 600.0;
        } else {
            destinationMmsi1 = new MMSI(bitString.getUnsignedInt(69, 99));
            destinationMmsi2 = new MMSI(bitString.getUnsignedInt(104, 134));
        }

        boolean bandA = bitString.getBoolean(140, 141);
        boolean bandB = bitString.getBoolean(141, 142);
        int zoneSize = bitString.getUnsignedInt(142, 145);

        return new ChannelManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                channelA, channelB, transmitReceiveMode, power, northEastLongitude, northEastLatitude,
                southWestLongitude, southWestLatitude, destinationMmsi1, destinationMmsi2, addressed,
                bandA, bandB, zoneSize);
    }

    private static GroupAssignmentCommand createGroupAssignmentCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int spare1 = bitString.getUnsignedInt(38, 40);
        double northEastLongitude = bitString.getSignedInt(40, 58) / 600.0;
        double northEastLatitude = bitString.getSignedInt(58, 75) / 600.0;
        double southWestLongitude = bitString.getSignedInt(75, 93) / 600.0;
        double southWestLatitude = bitString.getSignedInt(93, 110) / 600.0;
        StationType stationType = StationType.fromInteger(bitString.getUnsignedInt(110, 114));
        ShipType shipType = ShipType.fromInteger(bitString.getUnsignedInt(114, 122));
        int spare2 = bitString.getUnsignedInt(122, 144);
        TxRxMode transmitReceiveMode = TxRxMode.fromInteger(bitString.getUnsignedInt(144, 146));
        ReportingInterval reportingInterval = ReportingInterval.fromInteger(bitString.getUnsignedInt(146, 150));
        int quietTime = bitString.getUnsignedInt(150, 154);

        return new GroupAssignmentCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare1, northEastLongitude, northEastLatitude, southWestLongitude, southWestLatitude,
                stationType, shipType, spare2, transmitReceiveMode, reportingInterval, quietTime);
    }

    private static ClassBCSStaticDataReport createClassBCSStaticDataReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        int partNumber = bitString.getUnsignedInt(38, 40);

        String shipName = null;
        ShipType shipType = null;
        String vendorId = null;
        String manufacturerMnemonic = null;
        Integer unitModelCode = null;
        Integer unitSerialNumber = null;
        String callsign = null;
        int toBow = 0;
        int toStern = 0;
        int toStarboard = 0;
        int toPort = 0;
        MMSI mothershipMmsi = null;
        PositionFixingDevice positionFixingDevice = null;
        VdesCapabilities vdesCapabilities = null;

        switch (partNumber) {
            case 0 -> shipName = FieldDecoder.text(AISText.decode(bitString, 40, 160));
            case 1 -> {
                shipType = ShipType.fromInteger(bitString.getUnsignedInt(40, 48));
                vendorId = FieldDecoder.text(AISText.decode(bitString, 48, 90));
                // ITU-R M.1371-6 Table 78: the 42-bit manufacturer ID is a 3-character mnemonic, a 4-bit model code and a 20-bit serial number.
                manufacturerMnemonic = FieldDecoder.text(AISText.decode(bitString, 48, 66));
                unitModelCode = bitString.getUnsignedInt(66, 70);
                unitSerialNumber = bitString.getUnsignedInt(70, 90);
                callsign = FieldDecoder.text(AISText.decode(bitString, 90, 132));
                toBow = bitString.getUnsignedInt(132, 141);
                toStern = bitString.getUnsignedInt(141, 150);
                toPort = bitString.getUnsignedInt(150, 156);
                toStarboard = bitString.getUnsignedInt(156, 162);
                positionFixingDevice = PositionFixingDevice.fromInteger(bitString.getUnsignedInt(162, 166));
                vdesCapabilities = VdesCapabilities.fromInteger(bitString.getUnsignedInt(166, 168));

                int mmsiValue = bitString.getUnsignedInt(132, 162);
                if (mmsiValue != 0) {
                    mothershipMmsi = new MMSI(mmsiValue);
                }
            }
        }

        return new ClassBCSStaticDataReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                Message24Part.fromInteger(partNumber), shipName, shipType, vendorId, callsign, toBow, toStern, toStarboard, toPort, mothershipMmsi,
                manufacturerMnemonic, unitModelCode, unitSerialNumber, positionFixingDevice, vdesCapabilities);
    }

    private static BinaryMessageSingleSlot createBinaryMessageSingleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        // ITU-R M.1371-6 Table 79: destination ID (30 bits + 2 spare) only when addressed; application identifier (16 bits) only when the binary data flag is set.
        boolean destinationIndicator = bitString.getBoolean(38, 39);
        boolean binaryDataFlag = bitString.getBoolean(39, 40);
        int dataStart = 40;
        MMSI destinationMMSI = null;
        if (destinationIndicator && bitString.length() >= 72) {
            destinationMMSI = new MMSI(bitString.getUnsignedInt(40, 70));
            dataStart = 72;
        }
        Integer designatedAreaCode = null;
        Integer functionalId = null;
        if (binaryDataFlag && bitString.length() >= dataStart + 16) {
            designatedAreaCode = bitString.getUnsignedInt(dataStart, dataStart + 10);
            functionalId = bitString.getUnsignedInt(dataStart + 10, dataStart + 16);
            dataStart += 16;
        }
        BitString data = dataStart < bitString.length() ? bitString.slice(dataStart, bitString.length()) : BitString.EMPTY;
        ApplicationSpecificMessage applicationSpecificMessage = designatedAreaCode != null ? ApplicationSpecificMessage.create(designatedAreaCode, functionalId, data) : null;

        return new BinaryMessageSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationIndicator, binaryDataFlag, destinationMMSI, designatedAreaCode, functionalId, data, applicationSpecificMessage);
    }

    private static BinaryMessageMultipleSlot createBinaryMessageMultipleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        // ITU-R M.1371-6 Table 81: destination ID (30 bits + 2 spare) only when addressed; application identifier (16 bits) only when
        // structured; the message ends with 4 spare bits, a 1-bit communication state selector and a 19-bit communication state.
        boolean addressed = bitString.getBoolean(38, 39);
        boolean structured = bitString.getBoolean(39, 40);
        int length = bitString.length();
        int dataStart = 40;
        MMSI destinationMmsi = null;
        if (addressed && length >= 72) {
            destinationMmsi = new MMSI(bitString.getUnsignedInt(40, 70));
            dataStart = 72;
        }
        Integer designatedAreaCode = null;
        Integer functionalId = null;
        Integer applicationId = null;
        if (structured && length >= dataStart + 16) {
            designatedAreaCode = bitString.getUnsignedInt(dataStart, dataStart + 10);
            functionalId = bitString.getUnsignedInt(dataStart + 10, dataStart + 16);
            applicationId = bitString.getUnsignedInt(dataStart, dataStart + 16);
            dataStart += 16;
        }
        int dataEnd = length - 24;
        BitString data = dataEnd > dataStart ? bitString.slice(dataStart, dataEnd) : BitString.EMPTY;
        ApplicationSpecificMessage applicationSpecificMessage = designatedAreaCode != null ? ApplicationSpecificMessage.create(designatedAreaCode, functionalId, data) : null;
        boolean commStateSelectorFlag = false;
        CommunicationState communicationState = null;
        if (length >= 64) {
            commStateSelectorFlag = bitString.getBoolean(length - 20, length - 19);
            communicationState = commStateSelectorFlag ? ITDMACommunicationState.fromBitString(bitString.slice(length - 19, length)) : SOTDMACommunicationState.fromBitString(bitString.slice(length - 19, length));
        }

        return new BinaryMessageMultipleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                addressed, structured, destinationMmsi, applicationId, designatedAreaCode, functionalId, data, applicationSpecificMessage, commStateSelectorFlag, communicationState);
    }

    private static LongRangeBroadcastMessage createLongRangeBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        boolean positionAccuracy = bitString.getBoolean(38, 39);
        boolean raim = bitString.getBoolean(39, 40);
        NavigationStatus status = NavigationStatus.fromInteger(bitString.getUnsignedInt(40, 44));
        int rawLongitude = bitString.getSignedInt(44, 62);
        double longitude = FieldDecoder.longitude(rawLongitude, FieldDecoder.TENTHS_OF_MINUTE);
        int rawLatitude = bitString.getSignedInt(62, 79);
        double latitude = FieldDecoder.latitude(rawLatitude, FieldDecoder.TENTHS_OF_MINUTE);
        int rawSpeedOverGround = bitString.getUnsignedInt(79, 85);
        float speed = FieldDecoder.speedOverGround(rawSpeedOverGround, 62, 1f);
        SpeedOverGroundIndicator speedOverGroundIndicator = FieldDecoder.speedOverGroundIndicator(rawSpeedOverGround, 62);
        int rawCourseOverGround = bitString.getUnsignedInt(85, 94);
        float course = FieldDecoder.courseOverGroundDegrees(rawCourseOverGround);
        PositionLatency positionLatency = PositionLatency.fromInteger(bitString.getUnsignedInt(94, 95));
        int spare = bitString.getUnsignedInt(95, 96);

        return new LongRangeBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                positionAccuracy, raim, status, latitude, longitude, speed, speedOverGroundIndicator, course, positionLatency, spare);
    }

    private static AidToNavigationReportSingleSlot createAidToNavigationReportSingleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received) {
        // ITU-R M.1371-6 Table 84
        int rawSecond = bitString.getUnsignedInt(38, 44);
        Integer second = FieldDecoder.timeStampSecond(rawSecond);
        TimeStampIndicator timeStampIndicator = FieldDecoder.timeStampIndicator(rawSecond);
        int rawLongitude = bitString.getSignedInt(44, 72);
        double longitude = FieldDecoder.longitude(rawLongitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        int rawLatitude = bitString.getSignedInt(72, 99);
        double latitude = FieldDecoder.latitude(rawLatitude, FieldDecoder.TEN_THOUSANDTHS_OF_MINUTE);
        RestrictedUseIndicator restrictedUse = RestrictedUseIndicator.fromInteger(bitString.getUnsignedInt(99, 101));
        AtoNStationType stationType = AtoNStationType.fromInteger(bitString.getUnsignedInt(101, 104));
        AidType aidType = AidType.fromInteger(bitString.getUnsignedInt(104, 111));
        int ialaMrn = bitString.getUnsignedInt(111, 128);
        AtoNDimensionsType dimensionsType = AtoNDimensionsType.fromInteger(bitString.getUnsignedInt(128, 132));
        int dimensionA = bitString.getUnsignedInt(132, 141);
        int dimensionB = bitString.getUnsignedInt(141, 152);
        boolean multipleDimensionTypes = bitString.getBoolean(152, 153);
        boolean charted = bitString.getBoolean(153, 154);
        AtoNOnStationStatus onStationStatus = AtoNOnStationStatus.fromInteger(bitString.getUnsignedInt(154, 158));
        int atonStatusBits = bitString.getUnsignedInt(158, 166);
        boolean authenticated = bitString.getBoolean(167, 168);

        return new AidToNavigationReportSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                second, timeStampIndicator, latitude, longitude, restrictedUse, stationType, aidType, ialaMrn,
                dimensionsType, dimensionA, dimensionB, multipleDimensionTypes, charted, onStationStatus, atonStatusBits, authenticated);
    }

    private static AISMessage createByType(
            AISMessageType messageType,
            MMSI sourceMmsi,
            int repeatIndicator,
            NMEATagBlock nmeaTagBlock,
            NMEAMessage[] nmeaMessages,
            BitString bitString,
            String source,
            Instant received
    ) {
        return switch (messageType) {
            case ShipAndVoyageRelatedData ->
                    createShipAndVoyageData(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case PositionReportClassAScheduled ->
                    createPositionReportClassAScheduled(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, messageType);
            case PositionReportClassAAssignedSchedule ->
                    createPositionReportClassAAssignedSchedule(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, messageType);
            case PositionReportClassAResponseToInterrogation ->
                    createPositionReportClassAResponseToInterrogation(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, messageType);
            case BaseStationReport ->
                    createBaseStationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case AddressedBinaryMessage ->
                    createAddressedBinaryMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case BinaryAcknowledge ->
                    createBinaryAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case BinaryBroadcastMessage ->
                    createBinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case StandardSARAircraftPositionReport ->
                    createStandardSARAircraftPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case UTCAndDateInquiry ->
                    createUTCAndDateInquiry(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case UTCAndDateResponse ->
                    createUTCAndDateResponse(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case AddressedSafetyRelatedMessage ->
                    createAddressedSafetyRelatedMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case SafetyRelatedAcknowledge ->
                    createSafetyRelatedAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case SafetyRelatedBroadcastMessage ->
                    createSafetyRelatedBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case Interrogation ->
                    createInterrogation(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case AssignedModeCommand ->
                    createAssignedModeCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case GNSSBinaryBroadcastMessage ->
                    createGNSSBinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case StandardClassBCSPositionReport ->
                    createStandardClassBCSPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case ExtendedClassBEquipmentPositionReport ->
                    createExtendedClassBEquipmentPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case DataLinkManagement ->
                    createDataLinkManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case AidToNavigationReport ->
                    createAidToNavigationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case ChannelManagement ->
                    createChannelManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case GroupAssignmentCommand ->
                    createGroupAssignmentCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case ClassBCSStaticDataReport ->
                    createClassBCSStaticDataReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case BinaryMessageSingleSlot ->
                    createBinaryMessageSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case BinaryMessageMultipleSlot ->
                    createBinaryMessageMultipleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case LongRangeBroadcastMessage ->
                    createLongRangeBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            case AidToNavigationReportSingleSlot ->
                    createAidToNavigationReportSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received);
            default -> throw new dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType(messageType.getCode());
        };
    }

    private static BitString decodePayloadToBitString(NMEAMessage... nmeaMessages) {
        if (nmeaMessages == null || nmeaMessages.length == 0) {
            throw new IllegalArgumentException("nmeaMessages must contain at least one element");
        }
        StringBuilder sixBitEncodedPayload = new StringBuilder();
        int fillBits = -1;
        for (int i = 0; i < nmeaMessages.length; i++) {
            NMEAMessage m = nmeaMessages[i];
            if (m == null) {
                throw new IllegalArgumentException("nmeaMessages[" + i + "] is null");
            }
            sixBitEncodedPayload.append(m.getEncodedPayload());
            if (i == nmeaMessages.length - 1) {
                Integer fb = m.getFillBits();
                if (fb == null) {
                    throw new IllegalArgumentException("fillBits cannot be null on the last NMEAMessage");
                }
                fillBits = fb;
            }
        }
        if (fillBits < 0) {
            throw new IllegalArgumentException("fillBits not set");
        }
        return NMEAArmouring.decode(sixBitEncodedPayload.toString(), fillBits);
    }

    @FunctionalInterface
    private interface PositionReportConstructor {
        PositionReport create(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                              NavigationStatus navigationStatus, float rateOfTurn, RateOfTurnIndicator rateOfTurnIndicator, float speedOverGround,
                              SpeedOverGroundIndicator speedOverGroundIndicator, boolean positionAccuracy, double latitude, double longitude,
                              float courseOverGround, Integer trueHeading, Integer second, TimeStampIndicator timeStampIndicator,
                              ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState, boolean transmitPowerLow);
    }

}
