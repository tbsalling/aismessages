package dk.tbsalling.nmea.messages;

public class NMEAMessage {
	
	public NMEAMessage(String messageType, int numberOfFragments,
			int fragmentNumber, char radioChannelCode, String encodedPayload,
			byte fillBits, byte checksum) {
		this.messageType = messageType;
		this.numberOfFragments = numberOfFragments;
		this.fragmentNumber = fragmentNumber;
		this.radioChannelCode = radioChannelCode;
		this.encodedPayload = encodedPayload;
		this.fillBits = fillBits;
		this.checksum = checksum;
	}

	public final String getEncodedPayload() {
		return encodedPayload;
	}

	public final char getRadioChannelCode() {
		return radioChannelCode;
	}

	public final byte getFillBits() {
		return fillBits;
	}

	public final byte getChecksum() {
		return checksum;
	}

	public String toString() {
		final StringBuffer nmeaString = new StringBuffer();
		nmeaString.append("!");
		nmeaString.append(messageType);
		nmeaString.append(",");
		nmeaString.append(numberOfFragments);
		nmeaString.append(",");
		nmeaString.append(fragmentNumber);
		nmeaString.append(",");
		nmeaString.append(",");
		nmeaString.append(radioChannelCode);
		nmeaString.append(",");
		nmeaString.append(encodedPayload);
		nmeaString.append(",");
		nmeaString.append(fillBits);
		nmeaString.append("*");
		nmeaString.append(checksum);
		return nmeaString.toString();
	}
	
	private final String messageType;
	private final int numberOfFragments;
	private final int fragmentNumber;
	private final char radioChannelCode;
	private final String encodedPayload;
	private final byte fillBits;
	private final byte checksum;
}
