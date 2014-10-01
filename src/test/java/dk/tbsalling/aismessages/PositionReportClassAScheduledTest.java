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

package dk.tbsalling.aismessages;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.tbsalling.aismessages.messages.PositionReportClassAScheduled;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.messages.types.NavigationStatus;
import dk.tbsalling.aismessages.messages.types.SOTDMA;

public class PositionReportClassAScheduledTest {
	
	private static PositionReportClassAScheduled decodedMessage;

	@BeforeClass
	public static void setUp() {
	}
	
	@Before
	public void setUpTestObject() {
		decodedMessage = new PositionReportClassAScheduled(
				AISMessageType.PositionReportClassAScheduled,
				0,
				MMSI.valueOf(1L),
				NavigationStatus.NotDefined,
				1,
				(float) 55,
				true,
				(float) 11,
				(float) 1,
				(float) 1,
				1,
				1,
				ManeuverIndicator.NoSpecialManeuver,
				Boolean.FALSE,
				SOTDMA.fromEncodedString("00000000000000000101110100000100110111111100000101011001111011101010110100000000000011111111111011100101110011100000000000000110"),
				null);
	}
	
	@Test
	public void isSerializable() {
		assertTrue(isSerializable(decodedMessage));
	}

	private boolean isSerializable(Object object) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(stream);
			oos.writeObject(object);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
