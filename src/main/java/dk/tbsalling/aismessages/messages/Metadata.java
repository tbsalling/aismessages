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

	public Long getProcessedIn() {
		return processedIn;
	}

	public void setProcessedIn(Long processedIn) {
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

	private String category = "AIS";
	private Date processedAt;
	private Long processedIn;
	private String source;
}
