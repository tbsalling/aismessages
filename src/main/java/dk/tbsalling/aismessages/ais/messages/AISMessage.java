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

import dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.ais.messages.types.AISMessageType;
import dk.tbsalling.aismessages.ais.messages.types.MMSI;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.logging.Logger;

import static dk.tbsalling.aismessages.ais.Decoders.UNSIGNED_INTEGER_DECODER;
import static java.util.Objects.requireNonNull;

/**
 * The AISMessage class models a complete and self-contained AIS message.
 *
 * If the AISMessage was created from received NMEA strings, then the original
 * NMEA strings are cached, together with the decoded values of the message.
 *
 * Lazy extraction of values.
 *
 * @author tbsalling
 */
@SuppressWarnings("serial")
public abstract class AISMessage implements Serializable, CachedDecodedValues {

    private transient static final Logger LOG = Logger.getLogger(AISMessage.class.getName());

    public transient static final String VERSION = "2.2.2";

    static {
        System.err.print("\n" + "AISMessages v" + VERSION + " // Copyright (c) 2011- by S-Consult ApS, Denmark, CVR DK31327490. http://s-consult.dk.\n" + "\n" + "This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of\n" + "this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 171 Second Street,\n" + "Suite 300, San Francisco, California, 94105, USA.\n" + "\n" + "NOT FOR COMMERCIAL USE!\n" + "Contact sales@s-consult.dk to obtain commercially licensed software.\n" + "\n");
    }

    /** The NMEA messages which represent this AIS message */
    private NMEAMessage[] nmeaMessages;

    private Metadata metadata;

    /** Payload expanded to string of 0's and 1's. Use weak reference to allow GC anytime. */
    private transient WeakReference<String> bitString = new WeakReference<>(null);

    /** Length of bitString */
    private transient int numberOfBits = -1;

    private transient Integer repeatIndicator;
    private transient MMSI sourceMmsi;

    protected AISMessage() {
    }

    protected AISMessage(NMEAMessage[] nmeaMessages) {
        requireNonNull(nmeaMessages);
        check(nmeaMessages);
        this.nmeaMessages = nmeaMessages;

        AISMessageType nmeaMessageType = decodeMessageType();
        if (getMessageType() != nmeaMessageType)
            throw new UnsupportedMessageType(nmeaMessageType.getCode());

        checkAISMessage();
    }

    protected AISMessage(NMEAMessage[] nmeaMessages, String bitString) {
        requireNonNull(nmeaMessages);
        check(nmeaMessages);
        this.nmeaMessages = nmeaMessages;
        this.bitString = new WeakReference<>(bitString);
        AISMessageType nmeaMessageType = decodeMessageType();
        if (getMessageType() != nmeaMessageType) {
            throw new UnsupportedMessageType(nmeaMessageType.getCode());
        }
        checkAISMessage();
    }

    private static void check(NMEAMessage[] nmeaMessages) {
        // TODO sanity check NMEA messages
    }

    /**
     * Compute a SHA-1 message digest of this AISmessage. Suitable for e.g. doublet discovery and filtering.
     * @return Message digest
     * @throws NoSuchAlgorithmException if SHA-1 algorithm is not accessible
     */
    public byte[] digest() throws NoSuchAlgorithmException {
        MessageDigest messageDigester = MessageDigest.getInstance("SHA");
        for (NMEAMessage nmeaMessage : nmeaMessages) {
            messageDigester.update(nmeaMessage.getRawMessage().getBytes());
        }
        return messageDigester.digest();
    }

    /**
     * @return a map of data field name and values.
     */
    public  Map<String, Object> dataFields() {
        HashMap<String,Object> map = new HashMap<>();
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(this.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (!"class".equals(propertyDescriptor.getName())) {
                    Method readMethod = propertyDescriptor.getReadMethod();

                    Class<?> returnType = readMethod.getReturnType();
                    if (isComplexType(returnType)) {
                        Object complexValue = readMethod.invoke(this);
                        if (complexValue != null) {
                            PropertyDescriptor[] propertyDescriptors2 = Introspector.getBeanInfo(returnType).getPropertyDescriptors();
                            for (PropertyDescriptor pd2 : propertyDescriptors2) {
                                if (!"class".equals(pd2.getName()))
                                    map.put(propertyDescriptor.getName() + "." + pd2.getName(), pd2.getReadMethod().invoke(complexValue));
                            }
                        }
                    } else if (Class.class.equals(returnType)) {
                        Object value = readMethod.invoke(this);
                        map.put(propertyDescriptor.getName(), ((Class) value).getSimpleName());
                    } else {
                        Object value = readMethod.invoke(this);
                        if (value != null)
                            if (returnType.isEnum())
                                map.put(propertyDescriptor.getName(), value.toString());
                            else
                                map.put(propertyDescriptor.getName(), value);
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    private boolean isComplexType(Class<?> clazz) {
        if (clazz.isArray() || clazz.isEnum())
            return false;
        String classname = clazz.getName();
        if (!classname.contains(".") || !classname.startsWith("dk.tbsalling.aismessages.ais.messages.types.")) {
          return false;
        }

        boolean hasGetters = false;
        try {
            hasGetters = Introspector.getBeanInfo(clazz).getPropertyDescriptors().length > 0;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return hasGetters;
    }

    /**
     * This method performs a rudimentary sanity check of the AIS data payload contained in the NMEA sentence(s).
     * These tests are mainly based on bitwise message length, even though other types of tests may occur.
     *
     * @throws InvalidMessage if the AIS payload of the NMEAmessage(s) is invalid.
     */
    protected void checkAISMessage() {
        StringBuffer message = new StringBuffer();

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
        return nmeaMessages;
    }

    public abstract AISMessageType getMessageType();

    @SuppressWarnings("unused")
	public final Metadata getMetadata() {
		return metadata;
	}

    @SuppressWarnings("unused")
	public final void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	private AISMessageType decodeMessageType() {
        return AISMessageType.fromInteger(Integer.parseInt(getBits(0, 6), 2));
	}

    @SuppressWarnings("unused")
	public final Integer getRepeatIndicator() {
        return getDecodedValue(() -> repeatIndicator, value -> repeatIndicator = value, () -> Boolean.TRUE, () -> UNSIGNED_INTEGER_DECODER.apply(getBits(6, 8)));
	}

    @SuppressWarnings("unused")
	public final MMSI getSourceMmsi() {
        return getDecodedValue(() -> sourceMmsi, value -> sourceMmsi = value, () -> Boolean.TRUE, () -> MMSI.valueOf(UNSIGNED_INTEGER_DECODER.apply(getBits(8, 38))));
	}

    @Override
    public String toString() {
        return "AISMessage{" +
                "nmeaMessages=" + Arrays.toString(nmeaMessages) +
                ", metadata=" + metadata +
                ", repeatIndicator=" + getRepeatIndicator() +
                ", sourceMmsi=" + getSourceMmsi() +
                '}';
    }

    protected String getBitString() {
        String b = bitString.get();
        if (b == null) {
            b = decodePayloadToBitString(nmeaMessages);
            bitString = new WeakReference<>(b);
        }
        return b;
    }
    
    protected String getZeroBitStuffedString(int endIndex) {
        String b = getBitString();
		if (b.length()-endIndex < 0){
	        StringBuffer c = new StringBuffer(b);
			for (int i = b.length()-endIndex; i < 0; i++) {
				c  = c.append("0");
			}
			b = c.toString();
		}
        return b;
    }

    public String getBits(int beginIndex, int endIndex) {
        return getZeroBitStuffedString(endIndex).substring(beginIndex, endIndex);
    }

    protected int getNumberOfBits() {	
        if (numberOfBits < 0) {
            numberOfBits = getBitString().length();
        }
        return numberOfBits;
    }

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
     * @param metadata Meta data
     * @param nmeaMessages NMEA messages
     * @throws InvalidMessage if the AIS payload of the NMEAmessage(s) is invalid
     * @return AISMessage the AIS message
     */
    public static AISMessage create(Metadata metadata, NMEAMessage... nmeaMessages) {
        AISMessage aisMessage = create(nmeaMessages);
        aisMessage.setMetadata(metadata);
        return aisMessage;
    }

    /**
     * Create proper type of AISMessage from 1..n NMEA messages.
     * @param nmeaMessages NMEA messages
     * @throws InvalidMessage if the AIS payload of the NMEAmessage(s) is invalid
     * @return AISMessage the AIS message
     */
    public static AISMessage create(NMEAMessage... nmeaMessages) {
        BiFunction<NMEAMessage[], String, AISMessage> aisMessageConstructor;

        String bitString = decodePayloadToBitString(nmeaMessages);
        AISMessageType messageType = AISMessageType.fromInteger(Integer.parseInt(bitString.substring(0, 6), 2));

        if (messageType != null) {
            switch (messageType) {
                case ShipAndVoyageRelatedData:
                    aisMessageConstructor = ShipAndVoyageData::new;
                    break;
                case PositionReportClassAScheduled:
                    aisMessageConstructor = PositionReportClassAScheduled::new;
                    break;
                case PositionReportClassAAssignedSchedule:
                    aisMessageConstructor = PositionReportClassAAssignedSchedule::new;
                    break;
                case PositionReportClassAResponseToInterrogation:
                    aisMessageConstructor = PositionReportClassAResponseToInterrogation::new;
                    break;
                case BaseStationReport:
                    aisMessageConstructor = BaseStationReport::new;
                    break;
                case AddressedBinaryMessage:
                    aisMessageConstructor = AddressedBinaryMessage::new;
                    break;
                case BinaryAcknowledge:
                    aisMessageConstructor = BinaryAcknowledge::new;
                    break;
                case BinaryBroadcastMessage:
                    aisMessageConstructor = BinaryBroadcastMessage::new;
                    break;
                case StandardSARAircraftPositionReport:
                    aisMessageConstructor = StandardSARAircraftPositionReport::new;
                    break;
                case UTCAndDateInquiry:
                    aisMessageConstructor = UTCAndDateInquiry::new;
                    break;
                case UTCAndDateResponse:
                    aisMessageConstructor = UTCAndDateResponse::new;
                    break;
                case AddressedSafetyRelatedMessage:
                    aisMessageConstructor = AddressedSafetyRelatedMessage::new;
                    break;
                case SafetyRelatedAcknowledge:
                    aisMessageConstructor = SafetyRelatedAcknowledge::new;
                    break;
                case SafetyRelatedBroadcastMessage:
                    aisMessageConstructor = SafetyRelatedBroadcastMessage::new;
                    break;
                case Interrogation:
                    aisMessageConstructor = Interrogation::new;
                    break;
                case AssignedModeCommand:
                    aisMessageConstructor = AssignedModeCommand::new;
                    break;
                case GNSSBinaryBroadcastMessage:
                    aisMessageConstructor = GNSSBinaryBroadcastMessage::new;
                    break;
                case StandardClassBCSPositionReport:
                    aisMessageConstructor = StandardClassBCSPositionReport::new;
                    break;
                case ExtendedClassBEquipmentPositionReport:
                    aisMessageConstructor = ExtendedClassBEquipmentPositionReport::new;
                    break;
                case DataLinkManagement:
                    aisMessageConstructor = DataLinkManagement::new;
                    break;
                case AidToNavigationReport:
                    aisMessageConstructor = AidToNavigationReport::new;
                    break;
                case ChannelManagement:
                    aisMessageConstructor = ChannelManagement::new;
                    break;
                case GroupAssignmentCommand:
                    aisMessageConstructor = GroupAssignmentCommand::new;
                    break;
                case ClassBCSStaticDataReport:
                    aisMessageConstructor = ClassBCSStaticDataReport::new;
                    break;
                case BinaryMessageSingleSlot:
                    aisMessageConstructor = BinaryMessageSingleSlot::new;
                    break;
                case BinaryMessageMultipleSlot:
                    aisMessageConstructor = BinaryMessageMultipleSlot::new;
                    break;
                case LongRangeBroadcastMessage:
                    aisMessageConstructor = LongRangeBroadcastMessage::new;
                    break;
                default:
                    throw new UnsupportedMessageType(messageType.getCode());
            }
        } else {
            StringBuffer sb = new StringBuffer();
            for (NMEAMessage nmeaMessage : nmeaMessages) {
                sb.append(nmeaMessage);
            }
            throw new InvalidMessage("Cannot extract message type from NMEA message: " + sb.toString());
        }

        return aisMessageConstructor.apply(nmeaMessages, bitString);
    }

    /** Decode an encoded six-bit string into a binary string of 0's and 1's */
    private static String toBitString(String encodedString, Integer paddingBits) {
        StringBuilder bitString = new StringBuilder();
        int n = encodedString.length();
        for (int i=0; i<n; i++) {
            String c = encodedString.substring(i, i+1);
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
        result = 31*result + getBitString().hashCode();
        return result;
    }
}
