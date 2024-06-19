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

public class SearchJobServer {
	
	private static final String DATABASE_FILE = "user_database.txt";
	private static final String DATABASE_FILE_JOB_CANIDATE = "jobCandidate_database.txt";
	private static final String DATABASE_FILE_DATA_SET = "skillDataSet.txt";
	private static final String DATABASE_FILE_SKILL_CANIDATE = "skillCandidate_database.txt";
	
	public void SearchJobSet(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
				
				} 
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			List<JobCandidate> listOfJobCandidates = readJobCandidateDatabase();
			List<JsonObject> resultsSkillsCandidates = new ArrayList<>();
			
			
			if(data.containsKey("experience") && !data.containsKey("skill")) {
				String experience = (String) data.get("experience");
				for(JobCandidate jobCandidate : listOfJobCandidates) {
						String auxJobExperience = jobCandidate.getExperience();
						Integer auxJobExperience2 = Integer.parseInt(auxJobExperience);
						Integer auxJobExperience3 = Integer.parseInt(experience);
						if(auxJobExperience2 <= auxJobExperience3){
							
							JsonObject dataRegistro = new JsonObject();
							dataRegistro.put("skill", jobCandidate.getSkill());
							dataRegistro.put("experience", jobCandidate.getExperience());
							
							
							Integer auxSkillset_size = jobCandidate.getId();
							
							dataRegistro.put("id", auxSkillset_size.toString());
							
							resultsSkillsCandidates.add(dataRegistro);
							//return;
						}
				}
				
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB", "SUCCESS", "");
				
				Integer auxNumbSkillsetSize = 0;
				JsonArray dataArray = new JsonArray();
		        for (JsonObject result : resultsSkillsCandidates) {
		            dataArray.add(result);
		            auxNumbSkillsetSize++;
		        }
		        
		        String Skillset_size = auxNumbSkillsetSize.toString();
		        
		        
		        JsonObject dataRegistroAux = new JsonObject();
		        dataRegistroAux.put("jobset_size", Skillset_size);
		        dataRegistroAux.put("jobset", dataArray);
		        jsonResponse.put("data", dataRegistroAux);
		        
		        System.out.println("Enviado para o cliente: " + jsonResponse);
		        out.println(jsonResponse.toJson());
		        out.flush();
		        return;
			}
			
			if(data.containsKey("skill") && !data.containsKey("experience")) {
				JsonArray skillList = (JsonArray) data.get("skill");

				for(JobCandidate jobCandidate : listOfJobCandidates) {
					String skill = (String) jobCandidate.getSkill();
					for(Object Auxskill : skillList) {
						String auxSkillString = Auxskill.toString();
						String skillValue = auxSkillString.replaceAll("\\{skill=", "").replaceAll("\\}", "");
						if(skillValue.equals(skill)){
							
							JsonObject dataRegistro = new JsonObject();
							dataRegistro.put("skill", jobCandidate.getSkill());
							dataRegistro.put("experience", jobCandidate.getExperience());
							
							
							Integer auxSkillset_size = jobCandidate.getId();
							
							dataRegistro.put("id", auxSkillset_size.toString());
							
							resultsSkillsCandidates.add(dataRegistro);
							//return;
						}
					}
				}
				
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB", "SUCCESS", "");
				
				Integer auxNumbSkillsetSize = 0;
				JsonArray dataArray = new JsonArray();
		        for (JsonObject result : resultsSkillsCandidates) {
		            dataArray.add(result);
		            auxNumbSkillsetSize++;
		        }
		        
		        String Skillset_size = auxNumbSkillsetSize.toString();
		        
		        
		        JsonObject dataRegistroAux = new JsonObject();
		        dataRegistroAux.put("jobset_size", Skillset_size);
		        dataRegistroAux.put("jobset", dataArray);
		        jsonResponse.put("data", dataRegistroAux);
		        
		        System.out.println("Enviado para o cliente: " + jsonResponse);
		        out.println(jsonResponse.toJson());
		        out.flush();
		        return;
				
				}
			
			String filter = (String) data.get("filter");
			
			if(!filter.equals("AND") && !filter.equals("OR")) {
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			if(data.containsKey("skill") && data.containsKey("experience")) {
				if(filter.equals("AND")) {
					JsonArray skillList = (JsonArray) data.get("skill");
					String experience = (String) data.get("experience");
					
					for(JobCandidate jobCandidate : listOfJobCandidates) {
						String skill = (String) jobCandidate.getSkill();
						for(Object Auxskill : skillList) {
							String auxSkillString = Auxskill.toString();
							String skillValue = auxSkillString.replaceAll("\\{skill=", "").replaceAll("\\}", "");
							String auxJobExperience = jobCandidate.getExperience();
							Integer auxJobExperience2 = Integer.parseInt(auxJobExperience);
							Integer auxJobExperience3 = Integer.parseInt(experience);
							if(skillValue.equals(skill) && auxJobExperience2 <= auxJobExperience3){
								
								JsonObject dataRegistro = new JsonObject();
								dataRegistro.put("skill", jobCandidate.getSkill());
								dataRegistro.put("experience", jobCandidate.getExperience());
								
								
								Integer auxSkillset_size = jobCandidate.getId();
								
								dataRegistro.put("id", auxSkillset_size.toString());
								
								resultsSkillsCandidates.add(dataRegistro);
								//return;
							}
						}
					}
					
					JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB", "SUCCESS", "");
					
					Integer auxNumbSkillsetSize = 0;
					JsonArray dataArray = new JsonArray();
					for (JsonObject result : resultsSkillsCandidates) {
						dataArray.add(result);
						auxNumbSkillsetSize++;
					}
					
					String Skillset_size = auxNumbSkillsetSize.toString();
					
					
					JsonObject dataRegistroAux = new JsonObject();
					dataRegistroAux.put("jobset_size", Skillset_size);
					dataRegistroAux.put("jobset", dataArray);
					jsonResponse.put("data", dataRegistroAux);
					
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(jsonResponse.toJson());
					out.flush();
					
				}
				
				if(filter.equals("OR")) {
					JsonArray skillList = (JsonArray) data.get("skill");
					String experience = (String) data.get("experience");
					
					for(JobCandidate jobCandidate : listOfJobCandidates) {
						String skill = (String) jobCandidate.getSkill();
						for(Object Auxskill : skillList) {
							String auxSkillString = Auxskill.toString();
							String skillValue = auxSkillString.replaceAll("\\{skill=", "").replaceAll("\\}", "");
							String auxJobExperience = jobCandidate.getExperience();
							Integer auxJobExperience2 = Integer.parseInt(auxJobExperience);
							Integer auxJobExperience3 = Integer.parseInt(experience);
							//System.out.println("SKill passada pelo cliente - " + skillValue);
							//System.out.println("SKill do banco - " + skill);
							//System.out.println("Experience passada pelo cliente - " + auxJobExperience3);
							//System.out.println("Experience do banco - " + auxJobExperience2);
							
							if(skillValue.equals(skill) || auxJobExperience2 <= auxJobExperience3){
								
								JsonObject dataRegistro = new JsonObject();
								dataRegistro.put("skill", jobCandidate.getSkill());
								dataRegistro.put("experience", jobCandidate.getExperience());
								
								
								Integer auxSkillset_size = jobCandidate.getId();
								
								dataRegistro.put("id", auxSkillset_size.toString());
								
								resultsSkillsCandidates.add(dataRegistro);
								//return;
							}
						}
					}
					
					JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB", "SUCCESS", "");
					
					Integer auxNumbSkillsetSize = 0;
					JsonArray dataArray = new JsonArray();
					for (JsonObject result : resultsSkillsCandidates) {
						dataArray.add(result);
						auxNumbSkillsetSize++;
					}
					
					String Skillset_size = auxNumbSkillsetSize.toString();
					
					
					JsonObject dataRegistroAux = new JsonObject();
					dataRegistroAux.put("jobset_size", Skillset_size);
					dataRegistroAux.put("jobset", dataArray);
					jsonResponse.put("data", dataRegistroAux);
					
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(jsonResponse.toJson());
					out.flush();
				}
			}
			
			
			
			
			} catch (JWTVerificationException e) {
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_JOB", "INVALID_TOKEN", "");
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
	
	private List<JobCandidate> readJobCandidateDatabase() throws IOException {
		List<JobCandidate> listOfJobCandidate = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_JOB_CANIDATE))){
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				JobCandidate jobCandidate = new JobCandidate(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				listOfJobCandidate.add(jobCandidate);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfJobCandidate;
	}

}
