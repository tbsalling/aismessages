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

/**
 * Interface for AIS messages containing dynamic ship data.
 * Dynamic data includes position, speed, and course information that changes frequently.
 */
package dk.tbsalling.aismessages.ais.messages;

/**
 * Interface for AIS messages containing dynamic ship data such as position, speed, and course.
 * This data typically changes frequently during vessel operation.
 */
public interface DynamicDataReport extends DataReport {
    /**
     * @return Latitude in decimal degrees
     */
    float getLatitude();

    /**
     * @return Raw latitude value before conversion to decimal degrees
     */
    int getRawLatitude();

    /**
     * @return Longitude in decimal degrees
     */
    float getLongitude();

    /**
     * @return Raw longitude value before conversion to decimal degrees
     */
    int getRawLongitude();

    /**
     * @return Speed over ground in knots
     */
    float getSpeedOverGround();

    /**
     * @return Raw speed over ground value before conversion to knots
     */
    int getRawSpeedOverGround();

    /**
     * @return Course over ground in degrees (0-359)
     */
    float getCourseOverGround();

    /**
     * @return Raw course over ground value before conversion to degrees
     */
    int getRawCourseOverGround();

    /**
     * @return True if position accuracy is high (DGPS quality), false otherwise
     */
    boolean isPositionAccuracy();
}
