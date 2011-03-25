package dk.tbsalling.aismessages.messages;

import java.util.Date;

public class Metadata {

	public Metadata() {
	}

	public Date getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(Date processedAt) {
		this.processedAt = processedAt;
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

	private final static String decoderVersion = "1.0";
	private final static String category = "AIS";
	private Date processedAt;
	private Short processedIn;
	private String source;
}
