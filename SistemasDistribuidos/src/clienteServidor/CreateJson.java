package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonObject;

public class CreateJson {
	
	 private String operation;
	 private String email;
	 private String password;
	 private String name;

	 public CreateJson(String operation, String email, String password, String name) {
	     this.operation = operation;
	     this.email = email;
	     this.password = password;
	     this.name = name;
	 }
	 
	 public CreateJson(String operation, String email, String password) {
		 this.operation = operation;
		 this.email = email;
		 this.password = password;
	 }
	 /*
	 public String toJsonString() {
       JsonObject jsonObject = new JsonObject();
       jsonObject.put("operation", operation);
       JsonObject dataObject = new JsonObject();
       dataObject.put("email", email);
       dataObject.put("password", password);
       jsonObject.put("data", dataObject);
       return jsonObject.toJson();
	 }
	 */
	 public JsonObject functLoginClient() {
	       JsonObject jsonObject = new JsonObject();
	       jsonObject.put("operation", operation);
	       JsonObject dataObject = new JsonObject();
	       dataObject.put("email", email);
	       dataObject.put("password", password);
	       jsonObject.put("data", dataObject);
	       return jsonObject;
		 }
	 
	 public JsonObject functRegistroClient(String operation, String email, String password, String name) {

	       JsonObject jsonObject = new JsonObject();
	       jsonObject.put("operation", operation);
	       JsonObject dataObject = new JsonObject();
	       dataObject.put("email", email);
	       dataObject.put("password", password);
	       dataObject.put("name", name);
	       jsonObject.put("data", dataObject);
	       return jsonObject;
		 }
	 
	 public static String sendRequest(JsonObject requestJson, PrintWriter out, BufferedReader in) throws IOException{

		 out.println(requestJson.toJson());
		 return in.readLine();
	 }
	 
	 public static String sendRequest(JsonObject requestJson, PrintWriter out) throws IOException{
		 out.println(requestJson.toString());
		 return null;
	 }
	 
	 public static String sendRequestLogout(JsonObject requestJson, PrintWriter out) throws IOException{
		 out.println(requestJson.toJson());
		 return null;
	 }
	 
	 public static JsonObject createRequest(String operation) {

		 JsonObject requestJson = new JsonObject();
		 requestJson.put("operation", operation);
		 return requestJson;
	 }
	 
	 
	 public static JsonObject createResponse(String operation, String status, String token) {

		 JsonObject responseJson = new JsonObject();
		 responseJson.put("operation", operation);
		 responseJson.put("status", status);
		 JsonObject dataObject = new JsonObject();
		 dataObject.put("token", token);
		 responseJson.put("data", dataObject);
		 return responseJson;
	 }
	 
	 public static JsonObject createResponse(String operation, String status, JsonObject dataResponse) {

		 JsonObject jsonResponse = new JsonObject();
		 jsonResponse.put("operation", operation);
		 jsonResponse.put("status", status);
		 jsonResponse.put("data", dataResponse);
		 return jsonResponse;
	 }
	 
	 public static String toJsonString(JsonObject jsonObject) {
		 return jsonObject.toJson();
	 }
	 

}
