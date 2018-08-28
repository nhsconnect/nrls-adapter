package nrls.adapter.model.task;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Subject {

	@XStreamAlias("NHSNumber")
	private String nhsNumber;

	public String getNhsNumber() {
		return nhsNumber.replace(" ", "");
	}

	public void setNhsNumber(String nhsNumber) {
		this.nhsNumber = nhsNumber.replace(" ", "");
	}

	@Override
	public String toString() {
		return "Subject [nhsNumber=" + nhsNumber + "]";
	}

}
