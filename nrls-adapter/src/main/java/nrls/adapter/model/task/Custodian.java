package nrls.adapter.model.task;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Custodian {

	@XStreamAlias("OdsCode")
	private String odsCode;

	public String getOdsCode() {
		return odsCode;
	}

	public void setOdsCode(String odsCode) {
		this.odsCode = odsCode;
	}

}
