package dk.tbsalling.ais.messages;

import dk.tbsalling.ais.Decoder;
import dk.tbsalling.ais.exceptions.InvalidEncodedMessage;
import dk.tbsalling.ais.exceptions.UnsupportedMessageType;
import dk.tbsalling.ais.messages.types.AISMessageType;
import dk.tbsalling.ais.messages.types.MMSI;

/**
 * used to broadcast differential corrections for GPS. The data in the payload
 * is intended to be passed directly to GPS receivers capable of accepting such
 * corrections.
 * 
 * @author tbsalling
 * @copyright S-Consult ApS, Denmark. All rights reserved. See license.txt file.	
 * 
 */
@SuppressWarnings("serial")
public class GNSSBinaryBroadcastMessage extends DecodedAISMessage {

	public GNSSBinaryBroadcastMessage(Integer repeatIndicator, MMSI sourceMmsi, Float latitude, Float longitude, String binaryData) {
		super(AISMessageType.GNSSBinaryBroadcastMessage, repeatIndicator, sourceMmsi);
		this.latitude = latitude;
		this.longitude = longitude;
		this.binaryData = binaryData;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}

	public final String getBinaryData() {
		return binaryData;
	}
	
	public static GNSSBinaryBroadcastMessage fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.GNSSBinaryBroadcastMessage))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = Decoder.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(Decoder.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Float longitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(40, 58)) / 10);
		Float latitude = (float) (Decoder.convertToSignedInteger(encodedMessage.getBits(58, 75)) / 10);
		String binaryData = Decoder.convertToBitString(encodedMessage.getBits(80, 816));

		return new GNSSBinaryBroadcastMessage(repeatIndicator, sourceMmsi, latitude, longitude, binaryData);
	}
	
	private final Float latitude;
	private final Float longitude;
	private final String binaryData;
}
