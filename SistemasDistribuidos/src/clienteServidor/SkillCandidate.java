package clienteServidor;

public class SkillCandidate {
	
	private String skill;
	private String experience;
	private int id;
	private int personId;
	
	public SkillCandidate(String skill, String experience, int personId ,int id) {
		this.skill = skill;
		this.experience = experience;
		this.personId = personId;
		this.id = id;
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

}
