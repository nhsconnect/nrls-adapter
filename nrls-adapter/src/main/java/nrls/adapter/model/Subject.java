package nrls.adapter.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Subject {

	@XStreamAlias("NHSNumber")
	private String nhsNumber;

	public String getNhsNumber() {
		return nhsNumber;
	}

	public void setNhsNumber(String nhsNumber) {
		this.nhsNumber = nhsNumber;
	}

}
