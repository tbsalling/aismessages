package dk.tbsalling.ais.messages;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import dk.tbsalling.ais.messages.types.AISMessageType;

public class EncodedAISMessage {

    private static final Logger log = Logger.getLogger(EncodedAISMessage.class.getName());

	/**
	 * A container class for ITU 1371 encoded AIS messages.
	 * 
	 * Example: Two related NMEA strings like these:
	 * !AIVDM,2,1,4,A,55NGH8P2?qMiL@GKOO04lE8T<62222222222220l20>574U:h=?UDp4P,0*7D
	 * !AIVDM,2,2,4,A,j80Dlm888888880,2*07
	 * - will have their encoded payloads concatenated and the EncodedAISMessage objects is then constructed like this:
	 * EncodedAISMessage encodedMessage = new EncodedAISMessage("55NGH8P2?qMiL@GKOO04lE8T<62222222222220l20>574U:h=?UDp4Pj80Dlm888888880", 2);
	 * @param encodedPayload
	 * @param paddingBits
	 */
	public EncodedAISMessage(String encodedPayload, Integer paddingBits) {
		this.bitString = toBitString(encodedPayload, paddingBits);
	}

	public final AISMessageType getMessageType() {
		return AISMessageType.fromInteger(Integer.parseInt(bitString.substring(0,6), 2));
	}

	public final String getBits(Integer beginIndex, Integer endIndex) {
		return bitString.substring(beginIndex, endIndex);
	}

	public final Integer getNumberOfBits() {
		return bitString.length();
	}
	
	public Boolean isValid() {
		if (bitString.length() < 6) {
			log.warning("Message is too short: " + bitString.length() + " bits.");
			return Boolean.FALSE;
		}
		
		int messageType = Integer.parseInt(bitString.substring(0,6), 2);
		if (messageType < 1 || messageType > 26) {
			log.warning("Unsupported message type: " + messageType);
			return Boolean.FALSE;
		}
		
		int actualMessageLength = bitString.length();
		switch (messageType) {
		case 1: 
			if (actualMessageLength != 168) {
				log.warning("Message type 1: Illegal message length: " + bitString.length() + " bits.");
				return Boolean.FALSE;
			}
			break;
		case 2: 
			if (actualMessageLength != 168) {
				log.warning("Message type 2: Illegal message length: " + bitString.length() + " bits.");
				return Boolean.FALSE;
			}
			break;
		case 3: 
			if (actualMessageLength != 168) {
				log.warning("Message type 3: Illegal message length: " + bitString.length() + " bits.");
				return Boolean.FALSE;
			}
			break;
		case 4: 
			if (actualMessageLength != 168) return Boolean.FALSE;
			break;
		case 5: 
			if (actualMessageLength != 424) {
				log.warning("Message type 5: Illegal message length: " + bitString.length() + " bits.");
				return Boolean.FALSE;
			}
			break;
		case 6: 
			break;
		case 7: 
			break;
		case 8: 
			break;
		case 9: 
			break;
		case 10: 
			break;
		case 11: 
			break;
		case 12: 
			break;
		case 13: 
			break;
		case 14: 
			break;
		case 15: 
			if (actualMessageLength != 88 && actualMessageLength != 110 && actualMessageLength != 112 && actualMessageLength != 160) return Boolean.FALSE;
			break;
		case 16: 
			break;
		case 17: 
			break;
		case 18: 
			break;
		case 19: 
			break;
		case 20: 
			break;
		case 21: 
			break;
		case 22: 
			break;
		case 23: 
			break;
		case 24: 
			break;
		case 25: 
			break;
		case 26: 
			break;
		default: 
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	private final String bitString;
	
	private static String toBitString(String encodedString, Integer paddingBits) {
		StringBuffer bitString = new StringBuffer();
		int n = encodedString.length();
		for (int i=0; i<n; i++) {
			String c = encodedString.substring(i, i+1);
			bitString.append(charToSixBit.get(c));
		}
		return bitString.substring(0, bitString.length() - paddingBits);
	}

	private final static Map<String, String> charToSixBit = new HashMap<String, String>();
	static {
		charToSixBit.put("0", "000000"); // 0
		charToSixBit.put("1", "000001"); // 1
		charToSixBit.put("2", "000010"); // 2
		charToSixBit.put("3", "000011"); // 3
		charToSixBit.put("4", "000100"); // 4
		charToSixBit.put("5", "000101"); // 5
		charToSixBit.put("6", "000110"); // 6
		charToSixBit.put("7", "000111"); // 7
		charToSixBit.put("8", "001000"); // 8
		charToSixBit.put("9", "001001"); // 9
		charToSixBit.put(":", "001010"); // 10
		charToSixBit.put(";", "001011"); // 11
		charToSixBit.put("<", "001100"); // 12
		charToSixBit.put("=", "001101"); // 13
		charToSixBit.put(">", "001110"); // 14
		charToSixBit.put("?", "001111"); // 15
		charToSixBit.put("@", "010000"); // 16
		charToSixBit.put("A", "010001"); // 17
		charToSixBit.put("B", "010010"); // 18
		charToSixBit.put("C", "010011"); // 19
		charToSixBit.put("D", "010100"); // 20
		charToSixBit.put("E", "010101"); // 21
		charToSixBit.put("F", "010110"); // 22
		charToSixBit.put("G", "010111"); // 23 
		charToSixBit.put("H", "011000"); // 24
		charToSixBit.put("I", "011001"); // 25
		charToSixBit.put("J", "011010"); // 26 
		charToSixBit.put("K", "011011"); // 27
		charToSixBit.put("L", "011100"); // 28
		charToSixBit.put("M", "011101"); // 29
		charToSixBit.put("N", "011110"); // 30
		charToSixBit.put("O", "011111"); // 31
		charToSixBit.put("P", "100000"); // 32
		charToSixBit.put("Q", "100001"); // 33
		charToSixBit.put("R", "100010"); // 34
		charToSixBit.put("S", "100011"); // 35
		charToSixBit.put("T", "100100"); // 36
		charToSixBit.put("U", "100101"); // 37
		charToSixBit.put("V", "100110"); // 38
		charToSixBit.put("W", "100111"); // 39
		charToSixBit.put("`", "101000"); // 40
		charToSixBit.put("a", "101001"); // 41
		charToSixBit.put("b", "101010"); // 42
		charToSixBit.put("c", "101011"); // 43 
		charToSixBit.put("d", "101100"); // 44
		charToSixBit.put("e", "101101"); // 45
		charToSixBit.put("f", "101110"); // 46
		charToSixBit.put("g", "101111"); // 47
		charToSixBit.put("h", "110000"); // 48
		charToSixBit.put("i", "110001"); // 49
		charToSixBit.put("j", "110010"); // 50
		charToSixBit.put("k", "110011"); // 51
		charToSixBit.put("l", "110100"); // 52
		charToSixBit.put("m", "110101"); // 53
		charToSixBit.put("n", "110110"); // 54
		charToSixBit.put("o", "110111"); // 55
		charToSixBit.put("p", "111000"); // 56
		charToSixBit.put("q", "111001"); // 57
		charToSixBit.put("r", "111010"); // 58
		charToSixBit.put("s", "111011"); // 59
		charToSixBit.put("t", "111100"); // 60
		charToSixBit.put("u", "111101"); // 61
		charToSixBit.put("v", "111110"); // 62
		charToSixBit.put("w", "111111"); // 63
	}
}
