package Model;

import java.util.Date;

public class Comment {

	private int id;
	private String text;
	private Date date;
	private User user;
	private EmergencySituation emergencySituation;
	
	public Comment() {
		
	}
	public Comment(int id, String text, Date date, User user, EmergencySituation emergencySituation) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.user = user;
		this.emergencySituation = emergencySituation;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public EmergencySituation getEmergencySituation() {
		return emergencySituation;
	}
	public void setEmergencySituation(EmergencySituation emergencySituation) {
		this.emergencySituation = emergencySituation;
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
