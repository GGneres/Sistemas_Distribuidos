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
import com.github.cliftonlabs.json_simple.JsonObject;

import clienteServidor.EchoServer.JWTValidator;

public class SetSearchableServidor {
	
	private static final String DATABASE_FILE_JOB_CANIDATE = "jobCandidate_database.txt";
	private static final String DATABASE_FILE_DATA_SET = "skillDataSet.txt";
	private static final String DATABASE_FILE_RECRUITER = "recruiter_database.txt";

	
	public void setSearchable(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String id = (String) data.get("id");
			String searchable = (String) data.get("searchable");
			
			if(searchable.isEmpty() || id.isEmpty()) {
				JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "INVALID_FIELD", "");
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
						
						if(data.containsKey("searchable")) {
							String changedAvaiable = (String) data.get("searchable");
							if(changedAvaiable.isEmpty()) {
								JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
						
							if(!(changedAvaiable.equals("YES") || changedAvaiable.equals("NO"))) {
								JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "INVALID_FIELD", "");
								System.out.println("Mandando para o cliente: " + jsonResponse);
								out.println(CreateJson.toJsonString(jsonResponse));
								return;
							}
								jobCandidate.setSearchable(searchable);
								changes = true;
								
						} else {
							JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "INVALID_FIELD", "");
							System.out.println("Mandando para o cliente: " + jsonResponse);
							out.println(CreateJson.toJsonString(jsonResponse));
							return;
						}
						
					}
				}
				}
				if(changes == true){
					writeJobCandidateDatabase(listOfJobCandidates);
					token = "";
					JsonObject dataResponse = new JsonObject();
					JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "SUCCESS", "");
					jsonResponse.remove("data");
					jsonResponse.put("data", dataResponse);
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));		
				}else {
					JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "SKILL_NOT_EXIST", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				

			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("SET_JOB_SEARCHABLE", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
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
	
	
	private void writeJobCandidateDatabase(List<JobCandidate> listOfJobCandidates) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE_JOB_CANIDATE))){
			for (JobCandidate jobCandidate : listOfJobCandidates) {
				String jobString = jobCandidate.getSkill() + "," + jobCandidate.getExperience() + "," + jobCandidate.getPersonId() + "," + jobCandidate.getId() + "," + jobCandidate.getSearchable() + "," + jobCandidate.getAvailable();
				bw.write(jobString);
				bw.newLine();
			}
		}
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

}
