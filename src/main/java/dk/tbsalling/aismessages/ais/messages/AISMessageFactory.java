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
import java.util.Map;
import java.util.TreeMap;

/**
 * Factory class that contains parsing logic for all AIS message types using BitStringParser.
 * This class separates the parsing concerns from the immutable value objects.
 *
 * @author tbsalling
 */
public class AISMessageFactory {

    private static final Map<String, String> charToSixBit = new TreeMap<>();

    static {
        charToSixBit.put("0", "000000"); // 0
        charToSixBit.put("1", "000001"); // 1
        charToSixBit.put("2", "000010"); // 2
        charToSixBit.put("3", "000011"); // 3
        charToSixBit.put("4", "000100"); // 4
        charToSixBit.put("5", "000101"); // 5
        charToSixBit.put("6", "000110"); // 6
        charToSixBit.put("7", "000111"); // 7
        charToSixBit.put("8", "001000"); // 8
        charToSixBit.put("9", "001001"); // 9
        charToSixBit.put(":", "001010"); // 10
        charToSixBit.put(";", "001011"); // 11
        charToSixBit.put("<", "001100"); // 12
        charToSixBit.put("=", "001101"); // 13
        charToSixBit.put(">", "001110"); // 14
        charToSixBit.put("?", "001111"); // 15
        charToSixBit.put("@", "010000"); // 16
        charToSixBit.put("A", "010001"); // 17
        charToSixBit.put("B", "010010"); // 18
        charToSixBit.put("C", "010011"); // 19
        charToSixBit.put("D", "010100"); // 20
        charToSixBit.put("E", "010101"); // 21
        charToSixBit.put("F", "010110"); // 22
        charToSixBit.put("G", "010111"); // 23
        charToSixBit.put("H", "011000"); // 24
        charToSixBit.put("I", "011001"); // 25
        charToSixBit.put("J", "011010"); // 26
        charToSixBit.put("K", "011011"); // 27
        charToSixBit.put("L", "011100"); // 28
        charToSixBit.put("M", "011101"); // 29
        charToSixBit.put("N", "011110"); // 30
        charToSixBit.put("O", "011111"); // 31
        charToSixBit.put("P", "100000"); // 32
        charToSixBit.put("Q", "100001"); // 33
        charToSixBit.put("R", "100010"); // 34
        charToSixBit.put("S", "100011"); // 35
        charToSixBit.put("T", "100100"); // 36
        charToSixBit.put("U", "100101"); // 37
        charToSixBit.put("V", "100110"); // 38
        charToSixBit.put("W", "100111"); // 39
        charToSixBit.put("`", "101000"); // 40
        charToSixBit.put("a", "101001"); // 41
        charToSixBit.put("b", "101010"); // 42
        charToSixBit.put("c", "101011"); // 43
        charToSixBit.put("d", "101100"); // 44
        charToSixBit.put("e", "101101"); // 45
        charToSixBit.put("f", "101110"); // 46
        charToSixBit.put("g", "101111"); // 47
        charToSixBit.put("h", "110000"); // 48
        charToSixBit.put("i", "110001"); // 49
        charToSixBit.put("j", "110010"); // 50
        charToSixBit.put("k", "110011"); // 51
        charToSixBit.put("l", "110100"); // 52
        charToSixBit.put("m", "110101"); // 53
        charToSixBit.put("n", "110110"); // 54
        charToSixBit.put("o", "110111"); // 55
        charToSixBit.put("p", "111000"); // 56
        charToSixBit.put("q", "111001"); // 57
        charToSixBit.put("r", "111010"); // 58
        charToSixBit.put("s", "111011"); // 59
        charToSixBit.put("t", "111100"); // 60
        charToSixBit.put("u", "111101"); // 61
        charToSixBit.put("v", "111110"); // 62
        charToSixBit.put("w", "111111"); // 63
    }

    // Public methods

    /**
     * Converts a six-bit encoded ASCII payload to a binary string representation (sequence of '0' and '1').
     *
     * @param encodedString the six-bit encoded payload
     * @param paddingBits   number of pad bits to discard from the end (0..5)
     * @return the binary string
     * @throws IllegalArgumentException if inputs are invalid
     */
    public static String toBitString(String encodedString, Integer paddingBits) {
        if (encodedString == null) {
            throw new IllegalArgumentException("encodedString cannot be null");
        }
        if (paddingBits == null || paddingBits < 0 || paddingBits > 5) {
            throw new IllegalArgumentException("paddingBits must be in range 0..5");
        }
        StringBuilder bitString = new StringBuilder(encodedString.length() * 6);
        for (int i = 0; i < encodedString.length(); i++) {
            String c = encodedString.substring(i, i + 1);
            String sixBits = charToSixBit.get(c);
            if (sixBits == null) {
                throw new IllegalArgumentException("Invalid six-bit character: '" + c + "'");
            }
            bitString.append(sixBits);
        }
        return bitString.substring(0, bitString.length() - paddingBits);
    }

    /**
     * Create proper type of AISMessage from 1..n NMEA messages, and attach metadata.
     */
    public static AISMessage create(Instant received, String source, NMEATagBlock nmeaTagBlock, NMEAMessage... nmeaMessages) {
        // Decode payload into bit string
        String bitString = decodePayloadToBitString(nmeaMessages);

        // Determine message type
        AISMessageType messageType = AISMessageType.fromInteger(Integer.parseInt(bitString.substring(0, 6), 2));
        if (messageType == null) {
            StringBuilder sb = new StringBuilder();
            for (NMEAMessage nmeaMessage : nmeaMessages) {
                sb.append(nmeaMessage);
            }
            throw new dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage("Cannot extract message type from NMEA message: %s".formatted(sb.toString()));
        }

        // Use BitStringParser to parse the message
        BitStringParser parser = new BitStringParser(bitString);

        // Parse common fields from all messages
        int repeatIndicator = parser.getUnsignedInt(6, 8);
        MMSI sourceMmsi = new MMSI(parser.getUnsignedInt(8, 38));

        // Delegate directly to the AISMessageFactory to construct the correct subtype
        return AISMessageFactory.createByType(
                messageType,
                sourceMmsi,
                repeatIndicator,
                nmeaTagBlock,
                nmeaMessages,
                bitString,
                source,
                received,
                parser
        );
    }

    private static ShipAndVoyageData createShipAndVoyageData(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static PositionReport createPositionReportClassAScheduled(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                              AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser,
                messageType, PositionReportClassAScheduled::new);
    }

    private static PositionReport createPositionReportClassAAssignedSchedule(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                                     AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser,
                messageType, PositionReportClassAAssignedSchedule::new);
    }

    private static PositionReport createPositionReportClassAResponseToInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser,
                                                                            AISMessageType messageType) {
        return createPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser,
                messageType, PositionReportClassAResponseToInterrogation::new);
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
        CommunicationState communicationState = switch (messageType) {
            case PositionReportClassAScheduled, PositionReportClassAAssignedSchedule ->
                    SOTDMACommunicationState.fromBitString(parser.getBitPattern(149, 168));
            case PositionReportClassAResponseToInterrogation ->
                    ITDMACommunicationState.fromBitString(parser.getBitPattern(149, 168));
            default ->
                    throw new IllegalArgumentException("Unsupported message type for PositionReport: " + messageType);
        };

        return constructor.create(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                navigationStatus, rateOfTurn, speedOverGround, positionAccuracy, latitude, longitude,
                courseOverGround, trueHeading, second, specialManeuverIndicator, raimFlag, communicationState,
                rawRateOfTurn, rawSpeedOverGround, rawLatitude, rawLongitude, rawCourseOverGround);
    }

    private static BaseStationReport createBaseStationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static AddressedBinaryMessage createAddressedBinaryMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static BinaryAcknowledge createBinaryAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static BinaryBroadcastMessage createBinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        Integer spare = parser.getUnsignedInt(38, 40);
        Integer designatedAreaCode = parser.getUnsignedInt(40, 50);
        Integer functionalId = parser.getUnsignedInt(50, 56);
        String binaryData = parser.getLength() > 56 ? parser.getBitPattern(56, parser.getLength()) : "";
        ApplicationSpecificMessage applicationSpecificMessage = ApplicationSpecificMessage.create(designatedAreaCode, functionalId, binaryData);

        return new BinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, designatedAreaCode, functionalId, binaryData, applicationSpecificMessage);
    }

    private static StandardSARAircraftPositionReport createStandardSARAircraftPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static UTCAndDateInquiry createUTCAndDateInquiry(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        MMSI destinationMmsi = new MMSI(parser.getUnsignedInt(40, 70));

        return new UTCAndDateInquiry(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationMmsi);
    }

    private static UTCAndDateResponse createUTCAndDateResponse(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static AddressedSafetyRelatedMessage createAddressedSafetyRelatedMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int sequenceNumber = parser.getUnsignedInt(38, 40);
        MMSI destinationMmsi = new MMSI(parser.getUnsignedInt(40, 70));
        boolean retransmit = parser.getBoolean(70, 71);
        int spare = parser.getUnsignedInt(71, 72);
        String text = parser.getString(72, parser.getLength());

        return new AddressedSafetyRelatedMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                sequenceNumber, destinationMmsi, retransmit, spare, text);
    }

    private static SafetyRelatedAcknowledge createSafetyRelatedAcknowledge(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static SafetyRelatedBroadcastMessage createSafetyRelatedBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        int spare = parser.getUnsignedInt(38, 40);
        String text = parser.getString(40, parser.getLength());

        return new SafetyRelatedBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                spare, text);
    }

    private static Interrogation createInterrogation(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static AssignedModeCommand createAssignedModeCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static GNSSBinaryBroadcastMessage createGNSSBinaryBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static StandardClassBCSPositionReport createStandardClassBCSPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static ExtendedClassBEquipmentPositionReport createExtendedClassBEquipmentPositionReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static DataLinkManagement createDataLinkManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static AidToNavigationReport createAidToNavigationReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static ChannelManagement createChannelManagement(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static GroupAssignmentCommand createGroupAssignmentCommand(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static ClassBCSStaticDataReport createClassBCSStaticDataReport(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static BinaryMessageSingleSlot createBinaryMessageSingleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
        boolean destinationIndicator = parser.getBoolean(38, 39);
        boolean binaryDataFlag = parser.getBoolean(39, 40);
        MMSI destinationMMSI = new MMSI(parser.getUnsignedInt(40, 70));
        String binaryData = parser.getBitPattern(40, 168);

        return new BinaryMessageSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received,
                destinationIndicator, binaryDataFlag, destinationMMSI, binaryData);
    }

    private static BinaryMessageMultipleSlot createBinaryMessageMultipleSlot(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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

    private static LongRangeBroadcastMessage createLongRangeBroadcastMessage(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received, BitStringParser parser) {
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


    @FunctionalInterface
    private interface PositionReportConstructor {
        PositionReport create(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, Instant received,
                              NavigationStatus navigationStatus, int rateOfTurn, float speedOverGround,
                              boolean positionAccuracy, float latitude, float longitude,
                              float courseOverGround, int trueHeading, int second,
                              ManeuverIndicator specialManeuverIndicator, boolean raimFlag, CommunicationState communicationState,
                              int rawRateOfTurn, int rawSpeedOverGround, int rawLatitude, int rawLongitude, int rawCourseOverGround);
    }

    // Private helper methods (placed last)
    private static AISMessage createByType(
            AISMessageType messageType,
            MMSI sourceMmsi,
            int repeatIndicator,
            NMEATagBlock nmeaTagBlock,
            NMEAMessage[] nmeaMessages,
            String bitString,
            String source,
            Instant received,
            BitStringParser parser
    ) {
        return switch (messageType) {
            case ShipAndVoyageRelatedData ->
                    AISMessageFactory.createShipAndVoyageData(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case PositionReportClassAScheduled ->
                    AISMessageFactory.createPositionReportClassAScheduled(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser, messageType);
            case PositionReportClassAAssignedSchedule ->
                    AISMessageFactory.createPositionReportClassAAssignedSchedule(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser, messageType);
            case PositionReportClassAResponseToInterrogation ->
                    AISMessageFactory.createPositionReportClassAResponseToInterrogation(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser, messageType);
            case BaseStationReport ->
                    AISMessageFactory.createBaseStationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case AddressedBinaryMessage ->
                    AISMessageFactory.createAddressedBinaryMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case BinaryAcknowledge ->
                    AISMessageFactory.createBinaryAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case BinaryBroadcastMessage ->
                    AISMessageFactory.createBinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case StandardSARAircraftPositionReport ->
                    AISMessageFactory.createStandardSARAircraftPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case UTCAndDateInquiry ->
                    AISMessageFactory.createUTCAndDateInquiry(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case UTCAndDateResponse ->
                    AISMessageFactory.createUTCAndDateResponse(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case AddressedSafetyRelatedMessage ->
                    AISMessageFactory.createAddressedSafetyRelatedMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case SafetyRelatedAcknowledge ->
                    AISMessageFactory.createSafetyRelatedAcknowledge(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case SafetyRelatedBroadcastMessage ->
                    AISMessageFactory.createSafetyRelatedBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case Interrogation ->
                    AISMessageFactory.createInterrogation(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case AssignedModeCommand ->
                    AISMessageFactory.createAssignedModeCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case GNSSBinaryBroadcastMessage ->
                    AISMessageFactory.createGNSSBinaryBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case StandardClassBCSPositionReport ->
                    AISMessageFactory.createStandardClassBCSPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case ExtendedClassBEquipmentPositionReport ->
                    AISMessageFactory.createExtendedClassBEquipmentPositionReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case DataLinkManagement ->
                    AISMessageFactory.createDataLinkManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case AidToNavigationReport ->
                    AISMessageFactory.createAidToNavigationReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case ChannelManagement ->
                    AISMessageFactory.createChannelManagement(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case GroupAssignmentCommand ->
                    AISMessageFactory.createGroupAssignmentCommand(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case ClassBCSStaticDataReport ->
                    AISMessageFactory.createClassBCSStaticDataReport(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case BinaryMessageSingleSlot ->
                    AISMessageFactory.createBinaryMessageSingleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case BinaryMessageMultipleSlot ->
                    AISMessageFactory.createBinaryMessageMultipleSlot(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            case LongRangeBroadcastMessage ->
                    AISMessageFactory.createLongRangeBroadcastMessage(sourceMmsi, repeatIndicator, nmeaTagBlock, nmeaMessages, bitString, source, received, parser);
            default -> throw new dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType(messageType.getCode());
        };
    }

    private static String decodePayloadToBitString(NMEAMessage... nmeaMessages) {
        if (nmeaMessages == null || nmeaMessages.length == 0) {
            throw new IllegalArgumentException("nmeaMessages must contain at least one element");
        }
        StringBuilder sixBitEncodedPayload = new StringBuilder();
        Integer fillBits = null;
        for (int i = 0; i < nmeaMessages.length; i++) {
            NMEAMessage m = nmeaMessages[i];
            if (m == null) {
                throw new IllegalArgumentException("nmeaMessages[" + i + "] is null");
            }
            sixBitEncodedPayload.append(m.getEncodedPayload());
            if (i == nmeaMessages.length - 1) {
                fillBits = m.getFillBits();
            }
        }
        return toBitString(sixBitEncodedPayload.toString(), fillBits);
    }
    
}
