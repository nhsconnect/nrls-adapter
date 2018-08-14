package nrls.adapter.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Author {

	@XStreamAlias("OdsCode")
	private String odsCode;

	public String getOdsCode() {
		return odsCode;
	}

	public void setOdsCode(String odsCode) {
		this.odsCode = odsCode;
	}

}
