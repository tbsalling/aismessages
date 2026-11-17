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

import dk.tbsalling.aismessages.ais.BitString;

import dk.tbsalling.aismessages.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.ais.messages.types.*;
import dk.tbsalling.aismessages.nmea.exceptions.InvalidMessage;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.aismessages.nmea.tagblock.NMEATagBlock;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Message 5: ship static and voyage related data (ITU-R M.1371-6 § A7-3.3, Table 50). Transmitted by Class A
 * shipborne stations every 6 min, on request and when the data changes (§ A1-4); 424 bits in two slots. In practice
 * the voyage related fields (especially ETA and destination) are not reliable, as they are entered by hand rather than
 * gathered from sensors.
 *
 * @author tbsalling
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShipAndVoyageData extends AISMessage implements StaticDataReport {

    /**
     * Constructor accepting pre-parsed values for true immutability.
     *
     * @param sourceMmsi           the pre-parsed source MMSI
     * @param repeatIndicator      the pre-parsed repeat indicator
     * @param nmeaTagBlock         the NMEA tag block
     * @param nmeaMessages         the NMEA messages
     * @param bitString            the bit string
     * @param imo                  the IMO number
     * @param callsign             the callsign
     * @param shipName             the ship name
     * @param shipType             the ship type
     * @param toBow                distance to bow
     * @param toStern              distance to stern
     * @param toPort               distance to port
     * @param toStarboard          distance to starboard
     * @param positionFixingDevice the position fixing device
     * @param etaMonth             the ETA month
     * @param etaDay               the ETA day
     * @param etaHour              the ETA hour
     * @param etaMinute            the ETA minute
     * @param draught              the draught
     * @param destination          the destination
     * @param dataTerminalReady    the data terminal ready flag
     * @param rawDraught           the raw draught value
     * @param aisVersionIndicator  the AIS version indicator
     */
    protected ShipAndVoyageData(MMSI sourceMmsi, int repeatIndicator, NMEATagBlock nmeaTagBlock, NMEAMessage[] nmeaMessages, BitString bitString, String source, Instant received,
                                IMO imo, String callsign, String shipName, ShipType shipType,
                                int toBow, int toStern, int toPort, int toStarboard,
                                PositionFixingDevice positionFixingDevice,
                                int etaMonth, int etaDay, int etaHour, int etaMinute,
                                float draught, String destination, boolean dataTerminalReady, int rawDraught, int aisVersionIndicator) {
        super(received, nmeaTagBlock, nmeaMessages, bitString, source, sourceMmsi, repeatIndicator);
        this.imo = imo;
        this.callsign = callsign;
        this.shipName = shipName;
        this.shipType = shipType;
        this.toBow = toBow;
        this.toStern = toStern;
        this.toPort = toPort;
        this.toStarboard = toStarboard;
        this.positionFixingDevice = positionFixingDevice;
        this.etaMonth = etaMonth;
        this.etaDay = etaDay;
        this.etaHour = etaHour;
        this.etaMinute = etaMinute;
        this.draught = draught;
        this.destination = destination;
        this.dataTerminalReady = dataTerminalReady;
        this.rawDraught = rawDraught;
        this.aisVersionIndicator = aisVersionIndicator;
    }

    @Override
    protected void checkAISMessage() {
        final AISMessageType messageType = getMessageType();
        if (messageType != AISMessageType.ShipAndVoyageRelatedData) {
            throw new UnsupportedMessageType(messageType.getCode());
        }
        final int numberOfBits = getNumberOfBits();
        if (numberOfBits != 424 && numberOfBits != 422 && numberOfBits != 426) {
            throw new InvalidMessage("Message of type %s expected to be 422, 424 or 426 bits long; not %d. Assumed sourceMmsi: %d.".formatted(messageType, numberOfBits, getSourceMmsi().getMmsi()));
        }
    }

    public AISMessageType getMessageType() {
        return AISMessageType.ShipAndVoyageRelatedData;
    }

    @Override
    public TransponderClass getTransponderClass() {
        return TransponderClass.A;
    }

    /**
     * @return The vessel AIS ETA, with the year filled in based on the metadata received time.
     * If the AIS Month and day are the received date or later, the first calendar date after the received is used for the year.
     * Unavailable hour (24) or minute (60) will be treated as 0
     * Examples:
     * Received on 2018-06-01 and ETA AIS is 06-03, this will return 2018-06-03
     * Received on 2018-12-31 and ETA AIS is 01-02, this will return 2019-01-02
     * Received on 2018-06-01 and ETA AIS is 05-31, this will return 2019-05-31
     * Received on 2018-06-01 and ETA AIS is 06-03 24:00, this will return 2018-06-01 00:00
     * Received on 2018-06-01 and ETA AIS is 06-03 12:60, this will return 2018-06-03 12:00
     */
	public Optional<ZonedDateTime> getEtaAfterReceived() {
        Metadata meta = this.getMetadata();
        ZonedDateTime received = (meta == null || meta.received() == null) ? null : meta.received().atZone(ZoneOffset.UTC);
        if(received == null) {
            return Optional.empty();
        }
        int month = etaMonth;
        int day = etaDay;
        int hour = etaHour == 24 ? 0 : etaHour;
        int minute = etaMinute == 60 ? 0 : etaMinute;
        if(month <= 0 || month > 12 || day <= 0 || day > 31 || hour < 0 || hour >= 24 || minute < 0 || minute >= 60) {
            return Optional.empty();
        } else {
            // determine year according to rules from the javadoc
            int year;
            if(month < received.getMonthValue() || (month == received.getMonthValue() && day < received.getDayOfMonth())) {
                year = received.getYear() + 1;
            } else {
                year = received.getYear();
            }
            return Optional.of(ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneOffset.UTC));
        }
    }

    /** Bits 40-70 (Table 50): IMO number per IMO Resolution A.1117(30), 1 000 000-9 999 999; 0 = not available; 10 000 000 and above = official flag state number. */
    IMO imo;
    /** Bits 70-112 (Table 50): call sign, 7 six-bit ASCII characters (Table 45); "@@@@@@@" = not available. Craft associated with a parent vessel use "A" plus the last 6 digits of the parent's MMSI. */
    String callsign;
    /** Bits 112-232 (Table 50): name as on the station radio licence, up to 20 six-bit ASCII characters; all "@" = not available. */
    String shipName;
    /** Bits 232-240 (Table 50, Table 51): type of ship and cargo type; null for the regional (100-199) and future-use (200-255) codes. */
    ShipType shipType;
    /** Bits 240-249 (Table 50, Fig. 38): dimension A, distance from the reference point to the bow in metres, 0-511; 511 = 511 m or greater. */
    int toBow;
    /** Bits 249-258 (Table 50, Fig. 38): dimension B, distance from the reference point to the stern in metres, 0-511. */
    int toStern;
    /** Bits 264-270 (Table 50, Fig. 38): dimension D, distance from the reference point to the starboard side in metres, 0-63; 63 = 63 m or greater. */
    int toStarboard;
    /** Bits 258-264 (Table 50, Fig. 38): dimension C, distance from the reference point to the port side in metres, 0-63. */
    int toPort;
    /** Bits 270-274 (Table 50): type of electronic position fixing device. */
    PositionFixingDevice positionFixingDevice;
    /** Bits 274-278 (Table 50): ETA month (UTC), 1-12; 0 = not available. */
    int etaMonth;
    /** Bits 278-283 (Table 50): ETA day (UTC), 1-31; 0 = not available. */
    int etaDay;
    /** Bits 283-288 (Table 50): ETA hour (UTC), 0-23; 24 = not available. */
    int etaHour;
    /** Bits 288-294 (Table 50): ETA minute (UTC), 0-59; 60 = not available. */
    int etaMinute;
    /** Maximum present static draught in metres, {@code rawDraught} / 10 (Table 50); 25.5 = 25.5 m or greater; 0 = not available. */
    float draught;
    /** Bits 302-422 (Table 50): destination, up to 20 six-bit ASCII characters; all "@" = not available. */
    String destination;
    /** Bit 422 (Table 50, § A7-3.3.1): data terminal equipment ready, true = available (bit 0), false = not available (bit 1, default). */
    boolean dataTerminalReady;
    /** Bits 294-302 (Table 50): maximum present static draught in 1/10 m, 0-255; 255 = 25.5 m or greater; 0 = not available. */
    int rawDraught;
    /**
     * AIS version indicator, bits 38-40 (ITU-R M.1371-6 Table 50): 0 = station compliant with M.1371-1,
     * 1 = compliant with M.1371-3, 2 = compliant with M.1371-5, 3 = compliant with M.1371-6 or later.
     */
    int aisVersionIndicator;
}
