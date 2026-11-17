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
 * Interface for AIS messages containing static ship data: vessel identification, dimensions and type, which
 * typically do not change during a voyage. Implemented by Message 5 ({@link ShipAndVoyageData}, ITU-R M.1371-6
 * Table 50) and Message 24 ({@link ClassBCSStaticDataReport}, Tables 76 and 77; each part fills only some of these
 * values). Dimensions are measured from the position reference point as in Fig. 38 (§ A7-3.3.3).
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
     * @return Distance from reference position to bow in meters; 0 = not available (Fig. 38), also for Message 24 Part A
     */
    int getToBow();

    /**
     * @return Distance from reference position to stern in meters; 0 = not available (Fig. 38), also for Message 24 Part A
     */
    int getToStern();

    /**
     * @return Distance from reference position to starboard side in meters; 0 = not available (Fig. 38), also for Message 24 Part A
     */
    int getToStarboard();

    /**
     * @return Distance from reference position to port side in meters; 0 = not available (Fig. 38), also for Message 24 Part A
     */
    int getToPort();

}
