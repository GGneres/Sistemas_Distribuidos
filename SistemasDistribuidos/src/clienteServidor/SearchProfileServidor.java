package clienteServidor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import clienteServidor.EchoServer.JWTValidator;

public class SearchProfileServidor {

	private static final String DATABASE_FILE = "user_database.txt";
	private static final String DATABASE_FILE_JOB_CANIDATE = "jobCandidate_database.txt";
	private static final String DATABASE_FILE_DATA_SET = "skillDataSet.txt";
	private static final String DATABASE_FILE_SKILL_CANIDATE = "skillCandidate_database.txt";
	private static final String DATABASE_FILE_RECRUITER = "recruiter_database.txt";
	
	public void SearchProfile(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfPersons = readRecruiterDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
				
				} 
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
			List<JsonObject> resultsSkillsCandidates = new ArrayList<>();
			
			
			if(data.containsKey("experience") && !data.containsKey("skill")) {
				String experience = (String) data.get("experience");
				for(SkillCandidate skillCandidate : listOfSkillCandidates) {
						String auxJobExperience = skillCandidate.getExperience();
						Integer auxJobExperience2 = Integer.parseInt(auxJobExperience);
						Integer auxJobExperience3 = Integer.parseInt(experience);
						if(auxJobExperience2 <= auxJobExperience3){	
							JsonObject dataRegistro = new JsonObject();
							dataRegistro.put("skill", skillCandidate.getSkill());
							dataRegistro.put("experience", skillCandidate.getExperience());
							
							
							Integer auxSkillset_size = skillCandidate.getId();
							Integer auxSkillsetperson_size = skillCandidate.getPersonId();
							
							dataRegistro.put("id", auxSkillset_size.toString());
							dataRegistro.put("id_user", auxSkillsetperson_size.toString());
							
							resultsSkillsCandidates.add(dataRegistro);
								//return;
							
							
						}
				}
				
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "SUCCESS", "");
				
				Integer auxNumbSkillsetSize = 0;
				JsonArray dataArray = new JsonArray();
		        for (JsonObject result : resultsSkillsCandidates) {
		            dataArray.add(result);
		            auxNumbSkillsetSize++;
		        }
		        
		        String Skillset_size = auxNumbSkillsetSize.toString();
		        
		        
		        JsonObject dataRegistroAux = new JsonObject();
		        dataRegistroAux.put("profile_size", Skillset_size);
		        dataRegistroAux.put("profile", dataArray);
		        jsonResponse.put("data", dataRegistroAux);
		        
		        System.out.println("Enviado para o cliente: " + jsonResponse);
		        out.println(jsonResponse.toJson());
		        out.flush();
		        return;
			}
			
			if(data.containsKey("skill") && !data.containsKey("experience")) {
				JsonArray skillList = (JsonArray) data.get("skill");

				for(SkillCandidate skillCandidate : listOfSkillCandidates) {
					String skill = (String) skillCandidate.getSkill();
					for(Object Auxskill : skillList) {
						String auxSkillString = Auxskill.toString();
						String skillValue = auxSkillString.replaceAll("\\{skill=", "").replaceAll("\\}", "");
						if(skillValue.equals(skill)){
												
							JsonObject dataRegistro = new JsonObject();
							dataRegistro.put("skill", skillCandidate.getSkill());
							dataRegistro.put("experience", skillCandidate.getExperience());
							
							
							Integer auxSkillset_size = skillCandidate.getId();
							Integer auxSkillsetperson_size = skillCandidate.getPersonId();
							
							dataRegistro.put("id", auxSkillset_size.toString());
							dataRegistro.put("id_user", auxSkillsetperson_size.toString());
							
							resultsSkillsCandidates.add(dataRegistro);
								//return;
							
							
						}
					}
				}
				
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "SUCCESS", "");
				
				Integer auxNumbSkillsetSize = 0;
				JsonArray dataArray = new JsonArray();
		        for (JsonObject result : resultsSkillsCandidates) {
		            dataArray.add(result);
		            auxNumbSkillsetSize++;
		        }
		        
		        String Skillset_size = auxNumbSkillsetSize.toString();
		        
		        
		        JsonObject dataRegistroAux = new JsonObject();
		        dataRegistroAux.put("profile_size", Skillset_size);
		        dataRegistroAux.put("profile", dataArray);
		        jsonResponse.put("data", dataRegistroAux);
		        
		        System.out.println("Enviado para o cliente: " + jsonResponse);
		        out.println(jsonResponse.toJson());
		        out.flush();
		        return;
				
				}
			
			String filter = (String) data.get("filter");
			
			if(!filter.equals("AND") && !filter.equals("OR")) {
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			if(data.containsKey("skill") && data.containsKey("experience")) {
				if(filter.equals("AND")) {
					JsonArray skillList = (JsonArray) data.get("skill");
					String experience = (String) data.get("experience");
					
					for(SkillCandidate skillCandidate : listOfSkillCandidates) {
						String skill = (String) skillCandidate.getSkill();
						for(Object Auxskill : skillList) {
							String auxSkillString = Auxskill.toString();
							String skillValue = auxSkillString.replaceAll("\\{skill=", "").replaceAll("\\}", "");
							String auxJobExperience = skillCandidate.getExperience();
							Integer auxJobExperience2 = Integer.parseInt(auxJobExperience);
							Integer auxJobExperience3 = Integer.parseInt(experience);
							if(skillValue.equals(skill) && auxJobExperience2 <= auxJobExperience3){
								
								
								JsonObject dataRegistro = new JsonObject();
								dataRegistro.put("skill", skillCandidate.getSkill());
								dataRegistro.put("experience", skillCandidate.getExperience());
								
								
								Integer auxSkillset_size = skillCandidate.getId();
								Integer auxSkillsetperson_size = skillCandidate.getPersonId();
								
								dataRegistro.put("id", auxSkillset_size.toString());
								dataRegistro.put("id_user", auxSkillsetperson_size.toString());
								
								resultsSkillsCandidates.add(dataRegistro);
									//return;
									
								
								
							}
						}
					}
					
					JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "SUCCESS", "");
					
					Integer auxNumbSkillsetSize = 0;
					JsonArray dataArray = new JsonArray();
					for (JsonObject result : resultsSkillsCandidates) {
						dataArray.add(result);
						auxNumbSkillsetSize++;
					}
					
					String Skillset_size = auxNumbSkillsetSize.toString();
					
					
					JsonObject dataRegistroAux = new JsonObject();
					dataRegistroAux.put("profile_size", Skillset_size);
					dataRegistroAux.put("profile", dataArray);
					jsonResponse.put("data", dataRegistroAux);
					
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(jsonResponse.toJson());
					out.flush();
					
				}
				
				if(filter.equals("OR")) {
					JsonArray skillList = (JsonArray) data.get("skill");
					String experience = (String) data.get("experience");
					
					for(SkillCandidate skillCandidate : listOfSkillCandidates) {
						String skill = (String) skillCandidate.getSkill();
						for(Object Auxskill : skillList) {
							String auxSkillString = Auxskill.toString();
							String skillValue = auxSkillString.replaceAll("\\{skill=", "").replaceAll("\\}", "");
							String auxJobExperience = skillCandidate.getExperience();
							Integer auxJobExperience2 = Integer.parseInt(auxJobExperience);
							Integer auxJobExperience3 = Integer.parseInt(experience);
							//System.out.println("SKill passada pelo cliente - " + skillValue);
							//System.out.println("SKill do banco - " + skill);
							//System.out.println("Experience passada pelo cliente - " + auxJobExperience3);
							//System.out.println("Experience do banco - " + auxJobExperience2);
							
							if(skillValue.equals(skill) || auxJobExperience2 <= auxJobExperience3){						
								JsonObject dataRegistro = new JsonObject();
								dataRegistro.put("skill", skillCandidate.getSkill());
								dataRegistro.put("experience", skillCandidate.getExperience());
								
								
								Integer auxSkillset_size = skillCandidate.getId();
								Integer auxSkillsetperson_size = skillCandidate.getPersonId();
								
								dataRegistro.put("id", auxSkillset_size.toString());
								dataRegistro.put("id_user", auxSkillsetperson_size.toString());
								
								resultsSkillsCandidates.add(dataRegistro);
									//return;
								
								
							}
						}
					}
					
					JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "SUCCESS", "");
					
					Integer auxNumbSkillsetSize = 0;
					JsonArray dataArray = new JsonArray();
					for (JsonObject result : resultsSkillsCandidates) {
						dataArray.add(result);
						auxNumbSkillsetSize++;
					}
					
					String Skillset_size = auxNumbSkillsetSize.toString();
					
					
					JsonObject dataRegistroAux = new JsonObject();
					dataRegistroAux.put("profile_size", Skillset_size);
					dataRegistroAux.put("profile", dataArray);
					jsonResponse.put("data", dataRegistroAux);
					
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(jsonResponse.toJson());
					out.flush();
				}
			}
			
			
			
			
			} catch (JWTVerificationException e) {
				JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "INVALID_TOKEN", "");
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
				JobCandidate jobCandidate = new JobCandidate(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[3], parts[4]);
				listOfJobCandidate.add(jobCandidate);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfJobCandidate;
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
	
	private List<Recruiter> readRecruiterDatabase() throws IOException {
		List<Recruiter> listOfRecruiters = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_RECRUITER))){
			String line;
			//System.out.println("AQUI!");
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Recruiter Recruiter = new Recruiter(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
				listOfRecruiters.add(Recruiter);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfRecruiters;
	}
	
}
