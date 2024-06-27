package clienteServidor;

public class ChooseCandidateDatabase {
	private String id_user;
	private String id_recruiter;
	
	public ChooseCandidateDatabase(String id_user, String id_recruiter) {
		this.id_recruiter = id_recruiter;
		this.id_user = id_user;
	}

	public String getId_user() {
		return id_user;
	}

	public void setId_user(String id_user) {
		this.id_user = id_user;
	}

	public String getId_recruiter() {
		return id_recruiter;
	}

	public void setId_recruiter(String id_recruiter) {
		this.id_recruiter = id_recruiter;
	}

}
