package nrls.adapter.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Attachment {

	@XStreamAlias("ContentType")
	private String contentType;
	private String url;
	private String title;
	@XStreamAlias("Creation")
	private String creation;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreation() {
		return creation;
	}

	public void setCreation(String creation) {
		this.creation = creation;
	}

}
