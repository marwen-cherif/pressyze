package org.bytecoders.pressyze.common;

public class Comment {
	
	
	public static final int CONFIRMATION_COMMENT = 1;
	
	public static final int DENIAL_COMMENT  = 0;

	private String id;
	
	private String content;
	
	private int type;
	
	private Fact fact;
	
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Fact getFact() {
		return fact;
	}

	public void setFact(Fact fact) {
		this.fact = fact;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", type=" + type
				+ ", fact=" + fact + ", user=" + user + "]";
	}
	
	
}
