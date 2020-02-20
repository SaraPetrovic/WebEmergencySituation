package Model;

import java.util.Comparator;
import java.util.Date;

public class EmergencySituation {

	private int id;
	private String placeName;
	private String township;
	private String description;
	private Date datetime;
	private String address;
	private Area area;
	private Urgency urgency;
	private String photo;
	private boolean active;
	private User volunteer;
	
	public EmergencySituation() {
		
	}

	public EmergencySituation(int id, String placeName, String township, String description, Date datetime,
			String address, Area area, Urgency urgency, String photo, boolean active, User volunteer) {
		super();
		this.id = id;
		this.placeName = placeName;
		this.township = township;
		this.description = description;
		this.datetime = datetime;
		this.address = address;
		this.area = area;
		this.urgency = urgency;
		this.photo = photo;
		this.active = active;
		this.volunteer = volunteer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Urgency getUrgency() {
		return urgency;
	}

	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public User getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(User volunteer) {
		this.volunteer = volunteer;
	}
	
	public static Comparator<EmergencySituation> EmergencySituationComparator = new Comparator<EmergencySituation>() {

		public int compare(EmergencySituation em1, EmergencySituation em2) {
		
			Date date1 = em1.getDatetime();
			Date date2 = em2.getDatetime();
			
			//ascending order
			return date1.compareTo(date2);
			
			//descending order
			//return date2.compareTo(date1);
			}
		
		};
}
