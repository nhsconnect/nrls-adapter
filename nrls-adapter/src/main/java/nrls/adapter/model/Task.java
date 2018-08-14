package nrls.adapter.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Task")
public class Task {
	@XStreamAlias("status")
	private String status;
	@XStreamAlias("type")
	private Type type;
	@XStreamAlias("subject")
	private Subject subject;
	@XStreamAlias("author")
	private Author author;
	@XStreamAlias("custodian")
	private Custodian custodian;
	@XStreamAlias("content")
	private Content content;
	@XStreamAlias("Action")
	private String Action;
	@XStreamAlias("PointerMasterIdentifier")
	private String PointerMasterIdentifier;

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
		return Action;
	}

	public void setAction(String Action) {
		this.Action = Action;
	}

	public String getPointerMasterIdentifier() {
		return PointerMasterIdentifier;
	}

	public void setPointerMasterIdentifier(String PointerMasterIdentifier) {
		this.PointerMasterIdentifier = PointerMasterIdentifier;
	}

}
