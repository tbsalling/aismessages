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
 * *
 */

package dk.tbsalling.aismessages.ais.messages;

import dk.tbsalling.aismessages.ais.messages.types.ShipType;

/**
 * Interface for AIS messages containing static ship data.
 * Static data includes vessel identification, dimensions, and type information
 * that typically does not change during a voyage.
 */
public interface StaticDataReport extends DataReport {

    /**
     * @return The vessel's callsign
     */
	String getCallsign();

    /**
     * @return The vessel's name
     */
	String getShipName();

    /**
     * @return The type of ship
     */
	ShipType getShipType();

    /**
     * @return Distance from reference position to bow in meters
     */
    int getToBow();

    /**
     * @return Distance from reference position to stern in meters
     */
    int getToStern();

    /**
     * @return Distance from reference position to starboard side in meters
     */
    int getToStarboard();

    /**
     * @return Distance from reference position to port side in meters
     */
    int getToPort();

}
