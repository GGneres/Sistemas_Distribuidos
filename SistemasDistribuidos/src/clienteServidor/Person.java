package clienteServidor;

public class Person {
	private String email;
	private String password;
	private String name;
	private int id;
	
	public Person(String email, String password, String name, int id) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
