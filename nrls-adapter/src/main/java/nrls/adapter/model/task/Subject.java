/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

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
