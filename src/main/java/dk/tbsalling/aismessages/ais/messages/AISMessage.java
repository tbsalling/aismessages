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

import dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import dk.tbsalling.aismessages.version.Version;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.WARNING;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Objects.requireNonNull;

/**
 * The AISMessage class represents an AIS message, which is a standardized format for transmitting maritime data.
 * It contains methods for creating, manipulating, and decoding AIS messages.
 *
 * AIS messages can be created from NMEA messages or directly from bit strings. They can also be serialized to
 * a printable string format.  If the AISMessage was created from received NMEA strings, then the original
 * NMEA strings are cached, together with the decoded values of the message.
 *
 * This class provides methods for checking the validity of the AIS message payload, as well as computing message
 * digests for filtering and doublet discovery.
 *
 * The class also includes methods for getting and setting various fields of the AIS message, such as metadata,
 * tag block, repeat indicator, and source MMSI.
 *
 * Subclasses of AISMessage represent specific types of AIS messages, providing additional methods and fields
 * specific to that message type.
 */
@SuppressWarnings("serial")
public abstract class AISMessage implements Serializable {

    private transient static final System.Logger LOG = System.getLogger(AISMessage.class.getName());

    public transient static final String VERSION = Version.VERSION;

    static {
        System.err.print("""

                AISMessages v%s // Copyright (c) 2011- by S-Consult ApS, Denmark, CVR DK31327490. http://tbsalling.dk.
                
                This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
                this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 171 Second Street,
                Suite 300, San Francisco, California, 94105, USA.
                
                NOT FOR COMMERCIAL USE!
                Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain commercially licensed software.
                
                Compiled %s for Java %s.
                
                """.formatted(Version.VERSION, Version.BUILD_TIMESTAMP, Version.JAVA_VERSION));
    }

    private final Metadata metadata;

    /**
     * Length of bitString
     */
    private final int numberOfBits;

    private final int repeatIndicator;
    private final MMSI sourceMmsi;

    protected AISMessage() {
        this.numberOfBits = -1;
        this.metadata = null;
        this.repeatIndicator = -1;
        this.sourceMmsi = null;
    }


    /**
     * Constructor that accepts pre-parsed values, enabling true immutability.
     * This constructor eliminates the need for subclasses to call getBits() during construction.
     *
     * @param received        the metadata received timestamp (can be null)
     * @param nmeaTagBlock    the NMEA tag block
     * @param nmeaMessages    the NMEA messages
     * @param bitString       the binary string representation
     * @param source          the metadata source (can be null)
     * @param sourceMmsi      the pre-parsed source MMSI
     * @param repeatIndicator the pre-parsed repeat indicator
     */
    protected AISMessage(Instant received, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, String bitString, String source, MMSI sourceMmsi, int repeatIndicator) {
        requireNonNull(nmeaMessages);
        requireNonNull(bitString);
        check(nmeaMessages);

        this.numberOfBits = bitString.length();
        this.metadata = new Metadata(received, nmeaTagBlock, nmeaMessages, Version.VERSION, bitString, source);
        this.repeatIndicator = repeatIndicator;
        this.sourceMmsi = sourceMmsi;

        checkAISMessage();
    }

    private static void check(NMEAMessage[] nmeaMessages) {
        // TODO sanity check NMEA messages
    }

    /**
     * Compute a SHA-1 message digest of this AISmessage. Suitable for e.g. doublet discovery and filtering.
     *
     * @return Message digest
     * @throws NoSuchAlgorithmException if SHA-1 algorithm is not accessible
     */
    public byte[] digest() throws NoSuchAlgorithmException {
        MessageDigest messageDigester = MessageDigest.getInstance("SHA");
        for (NMEAMessage nmeaMessage : metadata.nmeaMessages()) {
            messageDigester.update(nmeaMessage.getRawMessage().getBytes());
        }
        return messageDigester.digest();
    }

    /**
     * @return a map of data field name and values.
     */
    public Map<String, Object> dataFields() {
        Map<String, Object> map = new TreeMap<>();
        callGetters(map, null, this);
        return map;
    }

    /**
     * Retrieves the values of all getter methods for the given object and stores them in a map.
     *
     * @param getterValues the map to store the getter values in
     * @param prefix       the prefix to be added to the property name in the map
     * @param o            the object to retrieve the getter values from
     */
    private void callGetters(Map getterValues, String prefix, Object o) {
        Method[] methods = o.getClass().getMethods();

        Stream.of(methods)
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> !m.getName().equals("getClass"))
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> isPublic(m.getModifiers()))
                .forEach(m -> {
                    try {
                        String propertyName = addPrefix(prefix, decapitalize(m.getName().substring(3)));
                        Class<?> returnType = m.getReturnType();

                        if (isComplexType(returnType)) {
                            var nestedObject = m.invoke(o);
                            if (nestedObject == null)
                                getterValues.put(propertyName, null);
                            else
                                callGetters(getterValues, propertyName, nestedObject);
                        } else if (Class.class.equals(returnType)) {
                            Object value = m.invoke(o);
                            getterValues.put(propertyName, ((Class) value).getSimpleName());
                        } else {
                            Object value = m.invoke(o);
                            if (value != null) {
                                if (returnType.isEnum())
                                    getterValues.put(propertyName, value.toString());
                                else
                                    getterValues.put(propertyName, value);
                            } else {
                                getterValues.put(propertyName, null);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Checks if the given class is a complex type.
     *
     * @param clazz the class to be checked
     * @return true if the class is a complex type, false otherwise
     */
    private boolean isComplexType(Class<?> clazz) {
        if (clazz.isArray() || clazz.isEnum())
            return false;
        String classname = clazz.getName();
        if (!classname.contains(".") || !classname.startsWith("dk.tbsalling.aismessages.ais.messages.types.")) {
            return false;
        }

        return Stream.of(clazz.getMethods())
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> isPublic(m.getModifiers()))
                .findAny()
                .isPresent();
    }

    /**
     * Converts the first character of the input string to lowercase.
     *
     * @param string the string to be decapitalized
     * @return the decapitalized string
     */
    private static String decapitalize(String string) {
        if (string != null) {
            if (!string.equals(string.toUpperCase())) {
                char c[] = string.toCharArray();
                c[0] = Character.toLowerCase(c[0]);
                string = new String(c);
            }
        }

        return string;
    }

    /**
     * Adds a prefix to a given string.
     *
     * @param prefix the prefix to be added
     * @param string the string to add the prefix to
     * @return the string with the prefix added, or the original string if the prefix is empty or null
     */
    private static String addPrefix(String prefix, String string) {
        if (prefix == null || prefix.isEmpty() || prefix.trim().isEmpty())
            return string;
        else
            return String.format("%s.%s", prefix, string);
    }

    /**
     * This method performs a rudimentary sanity check of the AIS data payload contained in the NMEA sentence(s).
     * These tests are mainly based on bitwise message length, even though other types of tests may occur.
     *
     * @throws InvalidMessage if the AIS payload of the NMEAmessage(s) is invalid.
     */
    protected void checkAISMessage() {
        StringBuilder message = new StringBuilder();

        final String bitString = getBitString();

        if (bitString.length() < 6)
            message.append(String.format("Message is too short to determine message type: %d bits.", bitString.length()));

        final int messageType = Integer.parseInt(bitString.substring(0, 6), 2);
        if (messageType < AISMessageType.MINIMUM_CODE || messageType > AISMessageType.MAXIMUM_CODE)
            message.append(String.format("Unsupported message type: %d.", messageType));
        else if (messageType != getMessageType().getCode())
            message.append(String.format("Expected message type: %d, not %d.", getMessageType().getCode(), messageType));

        if (message.length() > 0)
            throw new InvalidMessage(message.toString());
    }

    public NMEAMessage[] getNmeaMessages() {
        return metadata != null ? metadata.nmeaMessages() : null;
    }

    public abstract AISMessageType getMessageType();

    @SuppressWarnings("unused")
    public final Metadata getMetadata() {
        return metadata;
    }

    @SuppressWarnings("unused")
    public final NMEATagBlock getNmeaTagBlock() {
        return metadata != null ? metadata.nmeaTagBlock() : null;
    }


    /**
     * Retrieves the repeat indicator value of the AIS message.
     *
     * @return The repeat indicator value.
     */
    @SuppressWarnings("unused")
    public final int getRepeatIndicator() {
        return repeatIndicator;
    }

    /**
     * Retrieves the source MMSI (Maritime Mobile Service Identity) of the AIS message.
     * The source MMSI uniquely identifies the transmitting vessel or maritime entity.
     *
     * @return The source MMSI.
     */
    @SuppressWarnings("unused")
    public final MMSI getSourceMmsi() {
        return sourceMmsi;
    }

    @Override
    public String toString() {
        return "AISMessage{" +
                "nmeaMessages=" + Arrays.toString(getNmeaMessages()) +
                ", metadata=" + metadata +
                ", repeatIndicator=" + getRepeatIndicator() +
                ", sourceMmsi=" + getSourceMmsi() +
                '}' + tagBlockToString();
    }

    /**
     * Converts the NMEA tag block to a string representation.
     *
     * @return The string representation of the NMEA tag block, or an empty string if it is null.
     */
    public String tagBlockToString() {
        NMEATagBlock tagBlock = getNmeaTagBlock();
        String tagBlockMessage;
        if (tagBlock != null) {
            tagBlockMessage = String.valueOf(tagBlock);

        } else {
            tagBlockMessage = "";
        }
        return tagBlockMessage;
    }

    /**
     * Retrieves the bit string representation of the AIS message payload.
     *
     * @return The bit string representation of the AIS message payload.
     */
    protected String getBitString() {
        return metadata != null ? metadata.bitString() : null;
    }


    /**
     * Retrieves the number of bits in the AIS message payload.
     *
     * @return The number of bits in the AIS message payload.
     */
    protected int getNumberOfBits() {
        return numberOfBits;
    }

    /**
     * Decodes the payload of the given NMEAMessages into a binary string representation.
     *
     * @param nmeaMessages the NMEAMessages to decode. Can be one or more messages.
     * @return the binary string representation of the decoded payload.
     */
    protected static String decodePayloadToBitString(NMEAMessage... nmeaMessages) {
        StringBuilder sixBitEncodedPayload = new StringBuilder();
        int fillBits = -1;
        for (int i = 0; i < nmeaMessages.length; i++) {
            NMEAMessage m = nmeaMessages[i];
            sixBitEncodedPayload.append(m.getEncodedPayload());
            if (i == nmeaMessages.length - 1) {
                fillBits = m.getFillBits();
            }
        }

        // The AIS message payload stored as a string of 0's and 1's
        return toBitString(sixBitEncodedPayload.toString(), fillBits);
    }

    /**
     * Create proper type of AISMessage from 1..n NMEA messages, and
     * attach metadata.
     *
     * @param nmeaTagBlock NMEA Tag Block
     * @param nmeaMessages NMEA messages
     * @return AISMessage the AIS message
     * @throws InvalidMessage if the AIS payload of the NMEAmessage(s) is invalid
     */
    public static AISMessage create(Instant received, String source, NMEATagBlock nmeaTagBlock, NMEAMessage... nmeaMessages) {
        String bitString = decodePayloadToBitString(nmeaMessages);
        AISMessageType messageType = AISMessageType.fromInteger(Integer.parseInt(bitString.substring(0, 6), 2));

        if (messageType == null) {
            StringBuilder sb = new StringBuilder();
            for (NMEAMessage nmeaMessage : nmeaMessages) {
                sb.append(nmeaMessage);
            }
            throw new InvalidMessage("Cannot extract message type from NMEA message: %s".formatted(sb.toString()));
        }

        // Use BitStringParser to parse the message
        BitStringParser parser = new BitStringParser(bitString);

        // Parse common fields from all messages
        int repeatIndicator = parser.getUnsignedInt(6, 8);
        MMSI sourceMmsi = MMSI.valueOf(parser.getUnsignedInt(8, 38));

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
            default -> throw new UnsupportedMessageType(messageType.getCode());
        };
    }

    /**
     * Checks if the message is valid.
     *
     * @return true if the message is valid, false otherwise
     */
    public boolean isValid() {
        final String bitString = getBitString();

        if (bitString.length() < 6) {
            LOG.log(WARNING, "Message is too short: %d bits.".formatted(bitString.length()));
            return Boolean.FALSE;
        }

        int messageType = Integer.parseInt(bitString.substring(0, 6), 2);
        if (messageType < AISMessageType.MINIMUM_CODE || messageType > AISMessageType.MAXIMUM_CODE) {
            LOG.log(WARNING, "Unsupported message type: %d".formatted(messageType));
            return Boolean.FALSE;
        }

        int actualMessageLength = bitString.length();
        switch (messageType) {
            case 1:
                if (actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 1: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 2:
                if (actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 2: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 3:
                if (actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 3: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 4:
                if (actualMessageLength != 168) return Boolean.FALSE;
                break;
            case 5:
                if (actualMessageLength != 424 && actualMessageLength != 422) {
                    LOG.log(WARNING, "Message type 5: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 6:
                if (actualMessageLength > 1008) {
                    LOG.log(WARNING, "Message type 6: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 7:
                if (actualMessageLength != 72 && actualMessageLength != 104 && actualMessageLength != 136 && actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 7: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 8:
                if (actualMessageLength > 1008) {
                    LOG.log(WARNING, "Message type 8: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 9:
                if (actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 9: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 10:
                if (actualMessageLength != 72) {
                    LOG.log(WARNING, "Message type 10: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 11:
            	if (actualMessageLength != 168) return Boolean.FALSE;
                break;
            case 12:
                if (actualMessageLength > 1008) {
                    LOG.log(WARNING, "Message type 12: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 13:
                if (actualMessageLength != 72 && actualMessageLength != 104 && actualMessageLength != 136 && actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 13: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 14:
                if (actualMessageLength > 1008) {
                    LOG.log(WARNING, "Message type 14: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 15:
                if (actualMessageLength != 88 && actualMessageLength != 110 && actualMessageLength != 112 && actualMessageLength != 160) return Boolean.FALSE;
                break;
            case 16:
                if (actualMessageLength != 96 && actualMessageLength != 144) {
                    LOG.log(WARNING, "Message type 16: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 17:
                if (actualMessageLength < 80 || actualMessageLength > 816) {
                    LOG.log(WARNING, "Message type 17: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 18:
                if (actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 18: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 19:
                if (actualMessageLength != 312) {
                    LOG.log(WARNING, "Message type 19: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 20:
                if (actualMessageLength < 72 || actualMessageLength > 160) {
                    LOG.log(WARNING, "Message type 20: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 21:
                if (actualMessageLength < 272  || actualMessageLength > 360) {
                    LOG.log(WARNING, "Message type 21: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 22:
                if (actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 22: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 23:
                if (actualMessageLength != 160) {
                    LOG.log(WARNING, "Message type 23: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 24:
                if (actualMessageLength != 160 && actualMessageLength != 168 && actualMessageLength != 158 ) {
                    LOG.log(WARNING, "Message type 24: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 25:
                if (actualMessageLength > 168) {
                    LOG.log(WARNING, "Message type 25: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            case 26:
            	// ??
                break;
            case 27:
                if (actualMessageLength != 96 && actualMessageLength != 168) {
                    LOG.log(WARNING, "Message type 27: Illegal message length: %d bits.".formatted(bitString.length()));
                    return Boolean.FALSE;
                }
                break;
            default:
                return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Converts a six-bit encoded string to a binary string representation.
     *
     * @param encodedString the six-bit encoded string to convert
     * @param paddingBits   the number of padding bits in the string
     * @return the binary string representation of the six-bit encoded string
     */
    private static String toBitString(String encodedString, Integer paddingBits) {
        StringBuilder bitString = new StringBuilder();
        int n = encodedString.length();
        for (int i = 0; i < n; i++) {
            String c = encodedString.substring(i, i + 1);
            bitString.append(charToSixBit.get(c));
        }
        return bitString.substring(0, bitString.length() - paddingBits);
    }

    private final static Map<String, String> charToSixBit = new TreeMap<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AISMessage)) return false;

        AISMessage that = (AISMessage) o;

        if (!getBitString().equals(that.getBitString())) return false;
        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = metadata != null ? metadata.hashCode() : 0;
        result = 31 * result + getBitString().hashCode();
        return result;
    }
}
