package clienteServidor;

import com.github.cliftonlabs.json_simple.JsonObject;

public class CreateJson {
	
	 private String operation;
	 private String email;
	 private String password;

	 public CreateJson(String operation, String email, String password) {
	     this.operation = operation;
	     this.email = email;
	     this.password = password;
	 }
	 public String toJsonString() {
       JsonObject jsonObject = new JsonObject();
       jsonObject.put("operation", operation);
       JsonObject dataObject = new JsonObject();
       dataObject.put("email", email);
       dataObject.put("password", password);
       jsonObject.put("data", dataObject);
       return jsonObject.toJson();
	 }

}
