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

public class GetCompanyServidor {
	
	private static final String DATABASE_FILE = "user_database.txt";
	private static final String DATABASE_FILE_JOB_CANIDATE = "jobCandidate_database.txt";
	private static final String DATABASE_FILE_DATA_SET = "skillDataSet.txt";
	private static final String DATABASE_FILE_RECRUITER = "recruiter_database.txt";
	private static final String DATABASE_FILE_SKILL_CANIDATE = "skillCandidate_database.txt";
	private static final String DATABASE_FILE_CHOOSE_CANDIDATE = "choose_candidate_database.txt";
	
	public void getCompany(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("GET_COMPANY","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			List<SkillCandidate> listOfSkillCandidates = readSkillCandidateDatabase();
			List<Recruiter> listOfRecruiters = readRecruiterDatabase();
			List<ChooseCandidateDatabase> listOfChooseCandidate = readCooseCandidateDatabase();
			List<JsonObject> resultsSkillsCandidates = new ArrayList<>();
			
			for(ChooseCandidateDatabase chooseCandidato : listOfChooseCandidate) {
				Integer personIdToInteger = JWTValidator.getIdClaim(token);
				String personIdToString = (String) personIdToInteger.toString();
				if(chooseCandidato.getId_user().equals(personIdToString)) {	
					for(Recruiter recruiter : listOfRecruiters) {
						Integer recruiterIdToInteger = recruiter.getId();
						String recruiterIdToString = (String) recruiterIdToInteger.toString();
						if(chooseCandidato.getId_recruiter().equals(recruiterIdToString)) {
							
							JsonObject dataRegistro = new JsonObject();
							dataRegistro.put("name", recruiter.getName());
							dataRegistro.put("industry", recruiter.getIndustry());
							dataRegistro.put("email", recruiter.getEmail());
							dataRegistro.put("description", recruiter.getDescription());
							
							
							//dataRegistro.put("id", auxSkillset_size.toString());
							
							resultsSkillsCandidates.add(dataRegistro);
							//return;
						}
					}
				}
			}
			
			JsonObject jsonResponse = CreateJson.createResponse("GET_COMPANY", "SUCCESS", "");
			
			Integer auxNumbSkillsetSize = 0;
			JsonArray dataArray = new JsonArray();
	        for (JsonObject result : resultsSkillsCandidates) {
	            dataArray.add(result);
	            auxNumbSkillsetSize++;
	        }
	        
	        String Skillset_size = auxNumbSkillsetSize.toString();
	        
	        
	        JsonObject dataRegistroAux = new JsonObject();
	        dataRegistroAux.put("company_size", Skillset_size);
	        dataRegistroAux.put("company", dataArray);
	        jsonResponse.put("data", dataRegistroAux);
	        
	        System.out.println("Enviado para o cliente: " + jsonResponse);
	        out.println(jsonResponse.toJson());
	        out.flush();
			

			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("GET_COMPANY", "INVALID_TOKEN", "");
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
	

	
	private void writeSkillCandidateDatabase(List<SkillCandidate> listOfSkillCandidates) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE_SKILL_CANIDATE))){
			for (SkillCandidate skillCandidate : listOfSkillCandidates) {
				String skillString = skillCandidate.getSkill() + "," + skillCandidate.getExperience() + "," + skillCandidate.getPersonId() + "," + skillCandidate.getId();
				bw.write(skillString);
				bw.newLine();
			}
		}
	}
	
	private List<ChooseCandidateDatabase> readCooseCandidateDatabase() throws IOException {
		List<ChooseCandidateDatabase> listOfCooseCandidate = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_CHOOSE_CANDIDATE))){
			String line;
			//System.out.println("AQUI!");
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				ChooseCandidateDatabase ChooseCandidate = new ChooseCandidateDatabase(parts[0], parts[1]);
				listOfCooseCandidate.add(ChooseCandidate);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfCooseCandidate;
	}
	
	

}
