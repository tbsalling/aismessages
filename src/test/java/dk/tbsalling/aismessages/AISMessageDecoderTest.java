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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.tbsalling.aismessages.NMEAMessageReceiver;
import dk.tbsalling.aismessages.DecodedAISMessageHandler;
import dk.tbsalling.aismessages.messages.DecodedAISMessage;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.nmea.messages.NMEAMessage;
import dk.tbsalling.test.helpers.ArgumentCaptor;

public class AISMessageDecoderTest {
    private final static Mockery context = new JUnit4Mockery();

    private static DecodedAISMessageHandler aisMessageHandler;
	private static NMEAMessageReceiver aisMessageReceiver;

	@BeforeClass
	public static void setUp() {
		aisMessageHandler = context.mock(DecodedAISMessageHandler.class);
		aisMessageReceiver = new NMEAMessageReceiver("TEST", aisMessageHandler);
	}
	
	@Test
	public void canHandleUnfragmentedMessageReceived() {
		NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
		
		final ArgumentCaptor<DecodedAISMessage> decodedAISMessage = new ArgumentCaptor<DecodedAISMessage>();

		context.checking(new Expectations() {{
			oneOf(aisMessageHandler).handleMessageReceived(with(decodedAISMessage.getMatcher()));
        }});
		
		aisMessageReceiver.handleMessageReceived(unfragmentedNMEAMessage);

		assertEquals(AISMessageType.PositionReportClassAScheduled, decodedAISMessage.getCapturedObject().getMessageType());
	}

	@Test
	public void canHandleFragmentedMessageReceived() {
		NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
		NMEAMessage fragmentedNMEAMessage2 = NMEAMessage.fromString("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");
		
		final ArgumentCaptor<DecodedAISMessage> decodedAISMessage = new ArgumentCaptor<DecodedAISMessage>();

		context.checking(new Expectations() {{
			oneOf(aisMessageHandler).handleMessageReceived(with(decodedAISMessage.getMatcher()));
        }});
		
		aisMessageReceiver.handleMessageReceived(fragmentedNMEAMessage1);
		aisMessageReceiver.handleMessageReceived(fragmentedNMEAMessage2);

		assertEquals(AISMessageType.ShipAndVoyageRelatedData, decodedAISMessage.getCapturedObject().getMessageType());
	}

	@Test
	public void canFlushEmpty() {
		NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
		NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");
		NMEAMessage fragmentedNMEAMessage2 = NMEAMessage.fromString("!AIVDM,2,2,3,B,p=Mh00000000000,2*4C");

		final ArgumentCaptor<DecodedAISMessage> decodedAISMessage = new ArgumentCaptor<DecodedAISMessage>();

		context.checking(new Expectations() {{
			exactly(3).of(aisMessageHandler).handleMessageReceived(with(decodedAISMessage.getMatcher()));
        }});
		
		aisMessageReceiver.handleMessageReceived(unfragmentedNMEAMessage);
		aisMessageReceiver.handleMessageReceived(fragmentedNMEAMessage1);
		aisMessageReceiver.handleMessageReceived(fragmentedNMEAMessage2);

		ArrayList<NMEAMessage> flush = aisMessageReceiver.flush();

		assertNotNull(flush);
		assertEquals(0, flush.size());
	}

	@Test
	public void canFlushUnhandled() {
		NMEAMessage unfragmentedNMEAMessage = NMEAMessage.fromString("!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A");
		NMEAMessage fragmentedNMEAMessage1 = NMEAMessage.fromString("!AIVDM,2,1,3,B,55DA><02=6wpPuID000qTf059@DlU<00000000171lMDD4q20LmDp3hB,0*27");

		final ArgumentCaptor<DecodedAISMessage> decodedAISMessage = new ArgumentCaptor<DecodedAISMessage>();

		context.checking(new Expectations() {{
			exactly(2).of(aisMessageHandler).handleMessageReceived(with(decodedAISMessage.getMatcher()));
        }});
		
		aisMessageReceiver.handleMessageReceived(unfragmentedNMEAMessage);
		aisMessageReceiver.handleMessageReceived(fragmentedNMEAMessage1);

		ArrayList<NMEAMessage> flush = aisMessageReceiver.flush();

		assertNotNull(flush);
		assertEquals(1, flush.size());
		assertEquals(fragmentedNMEAMessage1, flush.get(0));

	}
}
