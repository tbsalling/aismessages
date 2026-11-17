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

import dk.tbsalling.aismessages.ais.messages.types.AltitudeIndicator;
import dk.tbsalling.aismessages.ais.messages.types.DraughtIndicator;
import dk.tbsalling.aismessages.ais.messages.types.SpeedOverGroundIndicator;
import dk.tbsalling.aismessages.ais.messages.types.TimeStampIndicator;

/**
 * Converts transmitted field values to the decoded values of the public API, mapping the "not available" codes of
 * ITU-R M.1371-6 to {@code NaN}. Keeps every such code in one place; not part of the public API.
 */
final class FieldDecoder {

    /** Units of 1/10 000 min per degree (Tables 46, 49, 57, 68, 69, 71 and 84). */
    static final double TEN_THOUSANDTHS_OF_MINUTE = 600_000.0;
    /** Units of 1/10 min per degree (Tables 66, 73, 74 and 83). */
    static final double TENTHS_OF_MINUTE = 600.0;

    private FieldDecoder() {
    }

    /**
     * @param raw            the transmitted latitude.
     * @param unitsPerDegree {@link #TEN_THOUSANDTHS_OF_MINUTE} or {@link #TENTHS_OF_MINUTE}.
     * @return the latitude in degrees, or NaN for 91° (not available) and any other value outside ±90°.
     */
    static double latitude(int raw, double unitsPerDegree) {
        double degrees = raw / unitsPerDegree;
        return degrees >= -90.0 && degrees <= 90.0 ? degrees : Double.NaN;
    }

    /**
     * @param raw            the transmitted longitude.
     * @param unitsPerDegree {@link #TEN_THOUSANDTHS_OF_MINUTE} or {@link #TENTHS_OF_MINUTE}.
     * @return the longitude in degrees, or NaN for 181° (not available) and any other value outside ±180°.
     */
    static double longitude(int raw, double unitsPerDegree) {
        double degrees = raw / unitsPerDegree;
        return degrees >= -180.0 && degrees <= 180.0 ? degrees : Double.NaN;
    }

    /**
     * @param raw the transmitted course in 1/10° (Tables 46, 57, 68, 69).
     * @return the course in degrees, or NaN for 3600 (not available) and the unused values 3601-4095.
     */
    static float courseOverGroundTenths(int raw) {
        return raw < 3600 ? raw / 10f : Float.NaN;
    }

    /**
     * @param raw the transmitted course in whole degrees (Table 83).
     * @return the course in degrees, or NaN for 511 (not available) and the unused values 360-510.
     */
    static float courseOverGroundDegrees(int raw) {
        return raw < 360 ? raw : Float.NaN;
    }

    /**
     * @param raw     the transmitted speed.
     * @param maximum the code for "maximum or higher": 1022 (Tables 46, 57, 68, 69) or 62 (Table 83).
     * @return the indicator for the transmitted speed; the code above {@code maximum} means not available.
     */
    static SpeedOverGroundIndicator speedOverGroundIndicator(int raw, int maximum) {
        return raw < maximum ? SpeedOverGroundIndicator.Measured : raw == maximum ? SpeedOverGroundIndicator.AtLeastMaximum : SpeedOverGroundIndicator.NotAvailable;
    }

    /**
     * @param raw      the transmitted speed.
     * @param maximum  the code for "maximum or higher", see {@link #speedOverGroundIndicator(int, int)}.
     * @param perKnot  units per knot: 10 for Tables 46, 68 and 69, 1 for Tables 57 and 83.
     * @return the speed in knots (the lower bound for "maximum or higher"), or NaN when not available.
     */
    static float speedOverGround(int raw, int maximum, float perKnot) {
        return raw <= maximum ? raw / perKnot : Float.NaN;
    }

    /**
     * @param raw the transmitted rate of turn ROT_AIS, -128 to +127 (Table 46).
     * @return the rate of turn in degrees per minute (negative = left), inverting ROT_AIS = 4.733 × √ROT_sensor,
     * or NaN for the special codes ±127 and -128.
     */
    static float rateOfTurn(int raw) {
        return raw >= -126 && raw <= 126 ? (float) (Math.signum(raw) * Math.pow(raw / 4.733, 2)) : Float.NaN;
    }

    /**
     * @param raw the transmitted draught in 1/10 m (Table 50).
     * @return the indicator for the transmitted draught.
     */
    static DraughtIndicator draughtIndicator(int raw) {
        return raw == 0 ? DraughtIndicator.NotAvailable : raw >= 255 ? DraughtIndicator.AtLeast25Point5Metres : DraughtIndicator.Measured;
    }

    /**
     * @param raw the transmitted draught in 1/10 m (Table 50).
     * @return the draught in metres (25.5 for "25.5 m or greater"), or NaN for 0 (not available).
     */
    static float draught(int raw) {
        return raw == 0 ? Float.NaN : raw / 10f;
    }

    /**
     * @param value a decoded decimal value.
     * @return the value boxed, or null when it is NaN; for boxed fields, where "not available" is null.
     */
    static Double orNull(double value) {
        return Double.isNaN(value) ? null : value;
    }

    /**
     * @param raw          a transmitted integer value.
     * @param notAvailable the field's "not available" code.
     * @return the value, or null when it is the "not available" code.
     */
    static Integer valueOrNull(int raw, int notAvailable) {
        return raw == notAvailable ? null : raw;
    }

    /**
     * @param raw the transmitted true heading (Table 46).
     * @return the heading in degrees 0-359, or null for 511 (not available) and the unused values 360-510.
     */
    static Integer trueHeading(int raw) {
        return raw < 360 ? raw : null;
    }

    /**
     * @param raw the transmitted time stamp (Tables 46, 57, 68, 69, 71, 84).
     * @return the UTC second 0-59, or null for the special codes 60-63.
     */
    static Integer timeStampSecond(int raw) {
        return raw < 60 ? raw : null;
    }

    /**
     * @param raw the transmitted time stamp (Tables 46, 57, 68, 69, 71, 84).
     * @return what the time stamp field carries.
     */
    static TimeStampIndicator timeStampIndicator(int raw) {
        return switch (raw) {
            case 60 -> TimeStampIndicator.NotAvailable;
            case 61 -> TimeStampIndicator.ManualInputMode;
            case 62 -> TimeStampIndicator.DeadReckoningMode;
            case 63 -> TimeStampIndicator.PositioningSystemInoperative;
            default -> TimeStampIndicator.Available;
        };
    }

    /**
     * @param raw the transmitted altitude in metres (Table 57).
     * @return the indicator for the transmitted altitude.
     */
    static AltitudeIndicator altitudeIndicator(int raw) {
        return raw == 4095 ? AltitudeIndicator.NotAvailable : raw == 4094 ? AltitudeIndicator.AtLeast4094Metres : AltitudeIndicator.Measured;
    }
}
