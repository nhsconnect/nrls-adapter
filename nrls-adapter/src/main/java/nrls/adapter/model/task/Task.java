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

@XStreamAlias("Task")
public class Task {
	@XStreamAlias("status")
	private String status;
	@XStreamAlias("type")
	private Type type;
	@XStreamAlias("subject")
	private Subject subject;
	@XStreamAlias("Author")
	private Author author;
	@XStreamAlias("Custodian")
	private Custodian custodian;
	@XStreamAlias("content")
	private Content content;
	@XStreamAlias("Action")
	private String action;
	@XStreamAlias("PointerMasterIdentifier")
	private String pointerMasterIdentifier;

	@Override
	public String toString() {
		return "Task [status=" + status + ", type=" + type + ", subject=" + subject + ", author=" + author
				+ ", custodian=" + custodian + ", content=" + content + ", action=" + action
				+ ", pointerMasterIdentifier=" + pointerMasterIdentifier + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Custodian getCustodian() {
		return custodian;
	}

	public void setCustodian(Custodian custodian) {
		this.custodian = custodian;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPointerMasterIdentifier() {
		return pointerMasterIdentifier;
	}

	public void setPointerMasterIdentifier(String pointerMasterIdentifier) {
		this.pointerMasterIdentifier = pointerMasterIdentifier;
	}

}
