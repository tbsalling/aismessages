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

import dk.tbsalling.aismessages.ais.messages.types.SpeedOverGroundIndicator;

/**
 * Interface for AIS messages containing dynamic data such as position, speed and course, which changes frequently
 * during operation. Implemented by Messages 1, 2, 3, 18 and 19 (through {@link ExtendedDynamicDataReport}) and by
 * Messages 9 ({@link StandardSARAircraftPositionReport}) and 27 ({@link LongRangeBroadcastMessage}). Units and
 * not-available values are those of the respective ITU-R M.1371-6 message tables.
 */
public interface DynamicDataReport extends DataReport {
    /**
     * @return Latitude in decimal degrees
     */
    double getLatitude();

    /**
     * @return Longitude in decimal degrees
     */
    double getLongitude();

    /**
     * @return Speed over ground in knots
     */
    float getSpeedOverGround();

    /**
     * @return Whether {@link #getSpeedOverGround()} is a measured speed, the lower bound of "maximum or higher", or not available
     */
    SpeedOverGroundIndicator getSpeedOverGroundIndicator();

    /**
     * @return Course over ground in degrees (0-359)
     */
    float getCourseOverGround();

    /**
     * @return True if position accuracy is high (DGPS quality), false otherwise
     */
    boolean isPositionAccuracy();
}
