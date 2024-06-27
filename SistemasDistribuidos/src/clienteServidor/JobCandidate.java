package clienteServidor;

public class JobCandidate {

	private String skill;
	private String experience;
	private int id;
	private int personId;
	private String searchable;
	private String available;
	
	public JobCandidate(String skill, String experience, int personId ,int id, String searchable, String available) {
		this.skill = skill;
		this.experience = experience;
		this.personId = personId;
		this.id = id;
		this.searchable = searchable;
		this.available = available;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getSearchable() {
		return searchable;
	}

	public void setSearchable(String searchable) {
		this.searchable = searchable;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}
}
