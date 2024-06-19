package clienteServidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import clienteServidor.EchoServer.JWTValidator;

public class CRUDCompetenciasServidor {
	private static final String DATABASE_FILE = "user_database.txt";
	private static final String DATABASE_FILE_SKILL_CANIDATE = "skillCandidate_database.txt";
	private static final String DATABASE_FILE_DATA_SET = "skillDataSet.txt";
	
	public void IncludeSkill(JsonObject jsonRequest, PrintWriter out)throws IOException{
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
				}
			
				JsonObject data = (JsonObject) jsonRequest.get("data");
				String skill = (String) data.get("skill");
				String experience = (String) data.get("experience");
				
				if(skill.isEmpty() || experience.isEmpty()) {
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "INVALID_FIELD", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				int id;
				
				List<SkillDataSet> listOfSkillDataSet = readSkillDataSetDatabase();
				boolean auxiliarDoIfDoDataBaseNaoExiste = false;
				for(SkillDataSet skillDataSet : listOfSkillDataSet) {
					//System.out.println("Qual string:" + skillDataSet.getSkill());
						if(skillDataSet.getSkill().equals(skill)) {
							//System.out.println("ENTROU NO IF PORRA, ACHOU ESSA MERDA");
							auxiliarDoIfDoDataBaseNaoExiste = true;
						}			
				}
				
				if(auxiliarDoIfDoDataBaseNaoExiste == false){
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "SKILL_NOT_EXIST", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				String regex = "[0-9]+";
				if(!experience.matches(regex)) {
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "INVALID_FIELD", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				
				List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
				if(listOfSkillCandidates.size() == 0) {
					id = 1;
				}
				else {
					SkillCandidate Ultima = listOfSkillCandidates.get(listOfSkillCandidates.size() - 1);
					id = Ultima.getId() + 1;
				}
				
				for(SkillCandidate skillCandidate : listOfSkillCandidates) {
					if(skillCandidate.getPersonId() == personId) {
						if(skillCandidate.getSkill().equals(skill)) {
							JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "SKILL_EXISTS", "");
							System.out.println("Mandando para o cliente: " + jsonResponse);
							out.println(CreateJson.toJsonString(jsonResponse));
							return;
						}
					}
				}
				
				SkillCandidate createSkillCandidate = new SkillCandidate(skill, experience, personId, id);
				listOfSkillCandidates.add(createSkillCandidate);
				writeSkillCandidateDatabase(listOfSkillCandidates);
				
				JsonObject dataResponse = new JsonObject();
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "SUCCESS", "");
				jsonResponse.remove("data");
				jsonResponse.put("data", dataResponse);
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));

			} catch (JWTVerificationException e) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "INVALID_TOKEN", "");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
	}
	
	public void LookupSkill(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_SKILL","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String skill = (String) data.get("skill");
			
			if(skill.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			//int id = Integer.parseInt(skillId);
			
			List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
			
			for(SkillCandidate skillCandidate : listOfSkillCandidates) {
				if(skillCandidate.getPersonId() == personId) {
					if(skillCandidate.getSkill().equals(skill)) {
						JsonObject dataRegistro = new JsonObject();
						dataRegistro.put("skill", skillCandidate.getSkill());
						dataRegistro.put("experience", skillCandidate.getExperience());
						Integer auxSkillset_size = skillCandidate.getId();
						
						dataRegistro.put("id", auxSkillset_size.toString());
						JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_SKILL", "SUCCESS", "");
						jsonResponse.put("data", dataRegistro);
						
						System.out.println("Enviado para o cliente: " + jsonResponse);
						out.println(CreateJson.toJsonString(jsonResponse));
						out.flush();
						return;
					}
				}
			}
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_SKILL", "SKILL_NOT_FOUND", "");
			System.out.println("Mandando para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
			
			
			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("LOOJUP_SKILL", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	public void lookupSkillSet(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_SKILLSET","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
			List<JsonObject> resultsSkillsCandidates = new ArrayList<>();
			
			for(SkillCandidate skillCandidate : listOfSkillCandidates) {
				if(skillCandidate.getPersonId() == personId) {				
					JsonObject dataRegistro = new JsonObject();
					dataRegistro.put("skill", skillCandidate.getSkill());
					dataRegistro.put("experience", skillCandidate.getExperience());
					
					
					Integer auxSkillset_size = skillCandidate.getId();
							
					dataRegistro.put("id", auxSkillset_size.toString());
					
					resultsSkillsCandidates.add(dataRegistro);
					//return;
				}
			}
			
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_SKILLSET", "SUCCESS", "");
			
			Integer auxNumbSkillsetSize = 0;
			JsonArray dataArray = new JsonArray();
	        for (JsonObject result : resultsSkillsCandidates) {
	            dataArray.add(result);
	            auxNumbSkillsetSize++;
	        }
	        
	        String Skillset_size = auxNumbSkillsetSize.toString();
	        
	        
	        JsonObject dataRegistroAux = new JsonObject();
	        dataRegistroAux.put("skillset_size", Skillset_size);
	        dataRegistroAux.put("skillset", dataArray);
	        jsonResponse.put("data", dataRegistroAux);
	        
	        System.out.println("Enviado para o cliente: " + jsonResponse);
	        out.println(jsonResponse.toJson());
	        out.flush();
			

			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_SKILLSET", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	public void deleteSkillCandidate(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_SKILL","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String skill = (String) data.get("skill");
			
			if(skill.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
			
			for(SkillCandidate skillCandidate : listOfSkillCandidates) {
				if(skillCandidate.getPersonId() == personId) {	
					if(skillCandidate.getSkill().equals(skill)) {
						listOfSkillCandidates.remove(skillCandidate);
						writeSkillCandidateDatabase(listOfSkillCandidates);
						JsonObject dataResponse = new JsonObject();
						JsonObject jsonResponse = CreateJson.createResponse("DELETE_SKILL", "SUCCESS", "");
						jsonResponse.remove("data");
						jsonResponse.put("data", dataResponse);
						System.out.println("Enviado para o cliente: " + jsonResponse);
						out.println(CreateJson.toJsonString(jsonResponse));
						return;
					}
				}
			}
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_SKILL", "SKILL_NOT_FOUND", "");
			System.out.println("Mandando para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
			
			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_SKILL", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	public void updateSkillCandidate(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try{
			
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String skill = (String) data.get("skill");
			
			if(skill.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			
			List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
			for(SkillCandidate skillCandidate : listOfSkillCandidates) {
				if(skillCandidate.getPersonId() == personId) {					
					if(skillCandidate.getSkill().equals(skill)) {
						
						if(data.containsKey("newSkill")) {
							String changedSkill = (String) data.get("newSkill");
							if(changedSkill.isEmpty()) {
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
							
							List<SkillDataSet> listOfSkillDataSet = readSkillDataSetDatabase();
							boolean auxiliarDoIfDoDataBaseNaoExiste = false;
							for(SkillDataSet skillDataSet : listOfSkillDataSet) {
								//System.out.println("Qual string:" + skillDataSet.getSkill());
									if(skillDataSet.getSkill().equals(changedSkill)) {
										//System.out.println("ENTROU NO IF PORRA, ACHOU ESSA MERDA");
										auxiliarDoIfDoDataBaseNaoExiste = true;

									}			
							}
							
							if(auxiliarDoIfDoDataBaseNaoExiste == false){
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "SKILL_NOT_EXIST", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
							
							for(SkillCandidate skillCandidate2 : listOfSkillCandidates) {
								if(skillCandidate2.getPersonId() == personId) {
									if(skillCandidate2.getSkill().equals(changedSkill)) {
										JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_SKILL", "SKILL_EXISTS", "");
										System.out.println("Mandando para o cliente: " + jsonResponse);
										out.println(CreateJson.toJsonString(jsonResponse));
										return;
									}
								}
							}
							
							
								
							skillCandidate.setSkill(changedSkill);

						}
						
						if(data.containsKey("experience")) {
							String changedExperience = (String) data.get("experience");
							if(changedExperience.isEmpty()) {
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
							
							String regex = "[0-9]+";
							if(!changedExperience.matches(regex)) {
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
								skillCandidate.setExperience(changedExperience);
						}
						
					}
				}
				}
			
			writeSkillCandidateDatabase(listOfSkillCandidates);
			token = "";
			JsonObject dataResponse = new JsonObject();
			JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "SUCCESS", "");
			jsonResponse.remove("data");
			jsonResponse.put("data", dataResponse);
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));		

			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("UPDATE_SKILL", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	private List<Person> readPersonDatabase() throws IOException {
		List<Person> listOfPersons = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE))){
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Person person = new Person(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
				listOfPersons.add(person);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfPersons;
	}
	
	private List<SkillCandidate> readSkillCandidateDatabase() throws IOException {
		List<SkillCandidate> listOfSkillCandidate = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_SKILL_CANIDATE))){
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				SkillCandidate skillCandidate = new SkillCandidate(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				listOfSkillCandidate.add(skillCandidate);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfSkillCandidate;
	}
	
	private List<SkillDataSet> readSkillDataSetDatabase() throws IOException {
		List<SkillDataSet> listOfSkillDataSet = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_DATA_SET))){
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				SkillDataSet skillDataSet = new SkillDataSet(parts[0]);
				listOfSkillDataSet.add(skillDataSet);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfSkillDataSet;
	}
	
	private void writeSkillCandidateDatabase(List<SkillCandidate> listOfSkillCandidates) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE_SKILL_CANIDATE))){
			for (SkillCandidate skillCandidate : listOfSkillCandidates) {
				String skillString = skillCandidate.getSkill() + "," + skillCandidate.getExperience() + "," + skillCandidate.getPersonId() + "," + skillCandidate.getId();
				bw.write(skillString);
				bw.newLine();
			}
		}
	}
}
	
	
