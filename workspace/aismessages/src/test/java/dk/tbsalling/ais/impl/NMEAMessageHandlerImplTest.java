/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.ais.impl;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.tbsalling.ais.AISMessageHandler;
import dk.tbsalling.ais.NMEAMessageHandler;
import dk.tbsalling.ais.internal.NMEAMessageHandlerImpl;
import dk.tbsalling.ais.messages.DecodedAISMessage;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.nmea.messages.NMEAMessage;
import dk.tbsalling.test.helpers.ArgumentCaptor;

public class NMEAMessageHandlerImplTest {
    private final static Mockery context = new JUnit4Mockery();

    private static AISMessageHandler aisMessageHandler;
	private static NMEAMessageHandler nmeaMessageHandler;

	@BeforeClass
	public static void setUp() {
		aisMessageHandler = context.mock(AISMessageHandler.class);
		nmeaMessageHandler = new NMEAMessageHandlerImpl(aisMessageHandler);
	}
	
	@Test
	public void canHandleUnfragmentedMessageReceived() {
		NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
		
		final ArgumentCaptor<DecodedAISMessage> decodedAISMessage = new ArgumentCaptor<DecodedAISMessage>();

		context.checking(new Expectations() {{
			one(aisMessageHandler).handleMessageReceived(with(decodedAISMessage.getMatcher()));
        }});
		
		nmeaMessageHandler.handleMessageReceived(unfragmentedNMEAMessage);

		assertEquals(AISMessageType.PositionReportClassAScheduled, decodedAISMessage.getCapturedObject().getMessageType());
	}

}
