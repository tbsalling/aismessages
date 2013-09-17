package dk.tbsalling.aismessages.messages;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Metadata implements Serializable {

	public Metadata() {
	}

	public Date getProcessedAt() {
		return new Date(processedAt.getTime());
	}

	public void setProcessedAt(Date processedAt) {
		this.processedAt = new Date(processedAt.getTime());
	}

	public Short getProcessedIn() {
		return processedIn;
	}

	public void setProcessedIn(Short processedIn) {
		this.processedIn = processedIn;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCategory() {
		return category;
	}

	public String getDecoderVersion() {
		return decoderVersion;
	}

	// TODO Add decoder version from maven
	// TODO Add decode status and error descriptions
	private final static String decoderVersion = "1.0";
	private final static String category = "AIS";
	private Date processedAt;
	private Short processedIn;
	private String source;
}
