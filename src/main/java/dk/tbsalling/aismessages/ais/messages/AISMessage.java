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
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import dk.tbsalling.aismessages.version.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

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
@Getter
@ToString
@EqualsAndHashCode
@Log
public abstract sealed class AISMessage permits AddressedBinaryMessage, AddressedSafetyRelatedMessage, AidToNavigationReport, AssignedModeCommand, BaseStationReport, BinaryAcknowledge, BinaryBroadcastMessage, BinaryMessageMultipleSlot, BinaryMessageSingleSlot, ChannelManagement, ClassBCSStaticDataReport, DataLinkManagement, Error, ExtendedClassBEquipmentPositionReport, GNSSBinaryBroadcastMessage, GroupAssignmentCommand, Interrogation, LongRangeBroadcastMessage, PositionReport, SafetyRelatedAcknowledge, SafetyRelatedBroadcastMessage, ShipAndVoyageData, StandardClassBCSPositionReport, StandardSARAircraftPositionReport, UTCAndDateInquiry, UTCAndDateResponse {

    static {
        log.info("""

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
    private final int repeatIndicator;
    private final MMSI sourceMmsi;

    @ToString.Exclude
    private final int numberOfBits;

    protected AISMessage() {
        this.numberOfBits = -1;
        this.metadata = null;
        this.repeatIndicator = -1;
        this.sourceMmsi = null;
    }

    public abstract AISMessageType getMessageType();

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

        this.numberOfBits = bitString.length();
        this.metadata = new Metadata(received, nmeaTagBlock, nmeaMessages, Version.VERSION, bitString, source);
        this.repeatIndicator = repeatIndicator;
        this.sourceMmsi = sourceMmsi;

        checkAISMessage();
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
     * This method performs a rudimentary sanity check of the AIS data payload contained in the NMEA sentence(s).
     * These tests are mainly based on bitwise message length, even though other types of tests may occur.
     *
     * @throws InvalidMessage if the AIS payload of the NMEAmessage(s) is invalid.
     */
    protected void checkAISMessage() {
        StringBuilder message = new StringBuilder();

        final String bitString = metadata.bitString();

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

}
