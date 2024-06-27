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

public class CRUDVagasServidor {
	private static final String DATABASE_FILE = "user_database.txt";
	private static final String DATABASE_FILE_JOB_CANIDATE = "jobCandidate_database.txt";
	private static final String DATABASE_FILE_DATA_SET = "skillDataSet.txt";
	private static final String DATABASE_FILE_RECRUITER = "recruiter_database.txt";
	
	
	public void IncludeJob(JsonObject jsonRequest, PrintWriter out)throws IOException{
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
				JsonObject data = (JsonObject) jsonRequest.get("data");
				String skill = (String) data.get("skill");
				String experience = (String) data.get("experience");
				String searchable = (String) data.get("searchable");
				String available = (String) data.get("available");
				
				
				if(skill.isEmpty() || experience.isEmpty() || searchable.isEmpty() || available.isEmpty()) {
					System.out.println("AQUI4");
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
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
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "SKILL_NOT_EXIST", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				if(!(searchable.equals("NO") || searchable.equals("YES"))) {
					System.out.println("AQUI1");
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				if(!(available.equals("YES") || available.equals("NO"))) {
					System.out.println("AQUI2");
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				String regex = "[0-9]+";
				if(!experience.matches(regex)) {
					System.out.println("AQUI3");
					JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				
				List<JobCandidate> listOfJobCandidates = readJobCandidateDatabase();
				if(listOfJobCandidates.size() == 0) {
					id = 1;
				}
				else {
					JobCandidate Ultima = listOfJobCandidates.get(listOfJobCandidates.size() - 1);
					id = Ultima.getId() + 1;
				}
				
				
				JobCandidate createJobCandidate = new JobCandidate(skill, experience, recruterId, id, searchable, available);
				listOfJobCandidates.add(createJobCandidate);
				writeJobCandidateDatabase(listOfJobCandidates);
				
				JsonObject dataResponse = new JsonObject();
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "SUCCESS", "");
				jsonResponse.remove("data");
				jsonResponse.put("data", dataResponse);
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));

			} catch (JWTVerificationException e) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_TOKEN", "");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
	}
	
	
	public void LookupJob(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOB","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String id = (String) data.get("id");
			
			if(id.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			//int id = Integer.parseInt(skillId);
			
			List<JobCandidate> listOfJobCandidates = readJobCandidateDatabase();
			
			
			for(JobCandidate jobCandidate : listOfJobCandidates) {
				if(jobCandidate.getPersonId() == recruterId) {
					Integer auxIdDoJob = jobCandidate.getId();
					String stringIdJob = auxIdDoJob.toString();
					if(stringIdJob.equals(id)) {
						JsonObject dataRegistro = new JsonObject();
						dataRegistro.put("skill", jobCandidate.getSkill());
						dataRegistro.put("experience", jobCandidate.getExperience());
						dataRegistro.put("searchable", jobCandidate.getSearchable());
						dataRegistro.put("available", jobCandidate.getAvailable());
						Integer auxSkillset_size = jobCandidate.getId();
						
						dataRegistro.put("id", auxSkillset_size.toString());
						JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOB", "SUCCESS", "");
						jsonResponse.put("data", dataRegistro);
						
						System.out.println("Enviado para o cliente: " + jsonResponse);
						out.println(CreateJson.toJsonString(jsonResponse));
						out.flush();
						return;
					}
				}
			}
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOB", "SKILL_NOT_FOUND", "");
			System.out.println("Mandando para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
			
			
			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOB", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	
	public void lookupJobSet(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOBSET","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			List<JobCandidate> listOfJobCandidates = readJobCandidateDatabase();
			List<JsonObject> resultsSkillsCandidates = new ArrayList<>();
			
			for(JobCandidate jobCandidate : listOfJobCandidates) {
				if(jobCandidate.getPersonId() == recruterId) {				
					JsonObject dataRegistro = new JsonObject();
					dataRegistro.put("skill", jobCandidate.getSkill());
					dataRegistro.put("experience", jobCandidate.getExperience());
					dataRegistro.put("searchable", jobCandidate.getSearchable());
					dataRegistro.put("available", jobCandidate.getAvailable());
					
					Integer auxSkillset_size = jobCandidate.getId();
							
					dataRegistro.put("id", auxSkillset_size.toString());
					
					resultsSkillsCandidates.add(dataRegistro);
					//return;
				}
			}
			
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOBSET", "SUCCESS", "");
			
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
			

			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_JOBSET", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	public void deleteJobCandidate(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_JOB","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String id = (String) data.get("id");
			
			if(id.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("INCLUDE_JOB", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			List<JobCandidate> listOfJobCandidates = readJobCandidateDatabase();
			
			for(JobCandidate jobCandidate : listOfJobCandidates) {
				if(jobCandidate.getPersonId() == recruterId) {	
					Integer auxIdDoJob = jobCandidate.getId();
					String stringIdJob = auxIdDoJob.toString();
					if(stringIdJob.equals(id)) {
						listOfJobCandidates.remove(jobCandidate);
						writeJobCandidateDatabase(listOfJobCandidates);
						JsonObject dataResponse = new JsonObject();
						JsonObject jsonResponse = CreateJson.createResponse("DELETE_JOB", "SUCCESS", "");
						jsonResponse.remove("data");
						jsonResponse.put("data", dataResponse);
						System.out.println("Enviado para o cliente: " + jsonResponse);
						out.println(CreateJson.toJsonString(jsonResponse));
						return;
					}
				}
			}
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_JOB", "SKILL_NOT_FOUND", "");
			System.out.println("Mandando para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
			
			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_JOB", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	
	public void updateJobCandidate(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String id = (String) data.get("id");
			String skill = (String) data.get("skill");
			String experience = (String) data.get("experience");
			
			if(skill.isEmpty() || experience.isEmpty() || id.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("UPADE_JOB", "INVALID_FIELD", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			boolean changes = false;
			List<JobCandidate> listOfJobCandidates = readJobCandidateDatabase();
			for(JobCandidate jobCandidate : listOfJobCandidates) {
				if(jobCandidate.getPersonId() == recruterId) {					
					Integer auxIdDoJob = jobCandidate.getId();
					String stringIdJob = auxIdDoJob.toString();
					if(stringIdJob.equals(id)) {
						
						if(data.containsKey("skill")) {
							String changedSkill = (String) data.get("skill");
							if(changedSkill.isEmpty()) {
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "INVALID_FIELD", "");
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
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "SKILL_NOT_EXIST", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
								
							jobCandidate.setSkill(changedSkill);
							changes = true;
						}
						
						if(data.containsKey("experience")) {
							String changedExperience = (String) data.get("experience");
							if(changedExperience.isEmpty()) {
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
							
							String regex = "[0-9]+";
							if(!changedExperience.matches(regex)) {
								JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
								jobCandidate.setExperience(changedExperience);
								changes = true;
						}
						
					}
				}
				}
				if(changes == true){
					writeJobCandidateDatabase(listOfJobCandidates);
					token = "";
					JsonObject dataResponse = new JsonObject();
					JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "SUCCESS", "");
					jsonResponse.remove("data");
					jsonResponse.put("data", dataResponse);
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));		
				}else {
					JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "SKILL_NOT_EXIST", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				

			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("UPDATE_JOB", "INVALID_TOKEN", "");
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
				JobCandidate jobCandidate = new JobCandidate(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4], parts[5]);
				listOfJobCandidate.add(jobCandidate);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfJobCandidate;
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
	
	private void writeJobCandidateDatabase(List<JobCandidate> listOfJobCandidates) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE_JOB_CANIDATE))){
			for (JobCandidate jobCandidate : listOfJobCandidates) {
				String jobString = jobCandidate.getSkill() + "," + jobCandidate.getExperience() + "," + jobCandidate.getPersonId() + "," + jobCandidate.getId() + "," + jobCandidate.getSearchable() + "," + jobCandidate.getAvailable();
				bw.write(jobString);
				bw.newLine();
			}
		}
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


