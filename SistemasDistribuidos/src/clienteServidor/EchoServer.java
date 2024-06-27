package clienteServidor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.net.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.io.*; 


public class EchoServer extends Thread {

    private Socket clientSocket;
    private BufferedWriter fileWriter;
    private static final String DATABASE_FILE = "user_database.txt";
    private static ClientGUI clientGUI;

    public EchoServer(Socket clientSoc, BufferedWriter writer) {
        clientSocket = clientSoc;
        fileWriter = writer;
        start();
    }	

    public class JWTValidator {
        private static final String TOKEN_KEY = "DISTRIBUIDOS";
        private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
        private static final JWTVerifier verifier = JWT.require(algorithm).build();

        public static String generateToken(int id, String role) {
            return JWT.create()
                .withClaim("id", id)
                .withClaim("role", role)
                .sign(algorithm);
        }

        public static int getIdClaim(String token) throws JWTVerificationException {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("id").asInt();
        }

        public static String getRoleClaim(String token) throws JWTVerificationException {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("role").asString();
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            clientGUI.addClient(clientSocket.toString());

            boolean running = true;
            while (running) {
                String jsonMenssage = null;

                try {
                    jsonMenssage = in.readLine();
                } catch (SocketTimeoutException e) {
                    System.out.println("Tempo limite de leitura do socket atingido, desconectando cliente: " + clientSocket);
                    break;
                } catch (SocketException e) {
                    System.out.println("Conexão resetada pelo cliente: " + clientSocket);
                    break;
                } catch (IOException e) {
                    System.out.println("Erro de IO na leitura do socket: " + e.getMessage());
                    break;
                }

                if (jsonMenssage == null) {
                    System.out.println("Cliente desconectado: " + clientSocket);
                    break;
                }

                if (jsonMenssage.equalsIgnoreCase("sair")) {
                    running = false;
                    continue;
                }

                fileWriter.write(jsonMenssage);
                fileWriter.newLine();
                fileWriter.flush();

                try {
                    JsonObject jsonCreate = (JsonObject) Jsoner.deserialize(jsonMenssage);
                    String operation = (String) jsonCreate.get("operation");
                    System.out.println("--------------------------------------------------------------------------------------------");

                    switch (operation) {
                        case "LOGIN_CANDIDATE":
                            CRUDServer loginServer = new CRUDServer();
                            loginServer.handleLogin(jsonCreate, out);
                            break;
                        case "SIGNUP_CANDIDATE":
                            CRUDServer RegistrarServer = new CRUDServer();
                            RegistrarServer.handleRegistrar(jsonCreate, out);
                            break;
                        case "UPDATE_ACCOUNT_CANDIDATE":
                            CRUDServer atualizarServer = new CRUDServer();
                            atualizarServer.atualizarContaServer(jsonCreate, out);
                            break;
                        case "DELETE_ACCOUNT_CANDIDATE":
                            CRUDServer deletarServer = new CRUDServer();
                            deletarServer.deletarServer(jsonCreate, out);
                            break;
                        case "LOGOUT_CANDIDATE":
                            CRUDServer logoutServer = new CRUDServer();
                            logoutServer.logout(jsonCreate, out);
                            break;
                        case "LOOKUP_ACCOUNT_CANDIDATE":
                            CRUDServer verificarDadosServer = new CRUDServer();
                            verificarDadosServer.verificarDadosServer(jsonCreate, out);
                            break;
                        case "LOGIN_RECRUITER":
                            CRUDServerEmpresa loginServerEmpresa = new CRUDServerEmpresa();
                            loginServerEmpresa.handleLogin(jsonCreate, out);
                            break;
                        case "SIGNUP_RECRUITER":
                            CRUDServerEmpresa RegistrarServerEmpresa = new CRUDServerEmpresa();
                            RegistrarServerEmpresa.handleRegistrar(jsonCreate, out);
                            break;
                        case "UPDATE_ACCOUNT_RECRUITER":
                            CRUDServerEmpresa atualizarServerEmpresa = new CRUDServerEmpresa();
                            atualizarServerEmpresa.atualizarContaServer(jsonCreate, out);
                            break;
                        case "DELETE_ACCOUNT_RECRUITER":
                            CRUDServerEmpresa deletarDadosEmpresa = new CRUDServerEmpresa();
                            deletarDadosEmpresa.deletarEmpresa(jsonCreate, out);
                            break;
                        case "LOGOUT_RECRUITER":
                            CRUDServerEmpresa logoutServerEmpresa = new CRUDServerEmpresa();
                            logoutServerEmpresa.logout(jsonCreate, out);
                            break;
                        case "LOOKUP_ACCOUNT_RECRUITER":
                            CRUDServerEmpresa verificarDadosEmpresa = new CRUDServerEmpresa();
                            verificarDadosEmpresa.verificarDadosServer(jsonCreate, out);
                            break;
                        case "INCLUDE_SKILL":
                            CRUDCompetenciasServidor adicionarSkillCandidate = new CRUDCompetenciasServidor();
                            adicionarSkillCandidate.IncludeSkill(jsonCreate, out);
                            break;
                        case "LOOKUP_SKILL":
                            CRUDCompetenciasServidor verificarSkillCandidate = new CRUDCompetenciasServidor();
                            verificarSkillCandidate.LookupSkill(jsonCreate, out);
                            break;
                        case "LOOKUP_SKILLSET":
                            CRUDCompetenciasServidor verifciarListaSkillCandidate = new CRUDCompetenciasServidor();
                            verifciarListaSkillCandidate.lookupSkillSet(jsonCreate, out);
                            break;
                        case "DELETE_SKILL":
                            CRUDCompetenciasServidor deletarSkillCandidate = new CRUDCompetenciasServidor();
                            deletarSkillCandidate.deleteSkillCandidate(jsonCreate, out);
                            break;
                        case "UPDATE_SKILL":
                            CRUDCompetenciasServidor updateSkillCandidate = new CRUDCompetenciasServidor();
                            updateSkillCandidate.updateSkillCandidate(jsonCreate, out);
                            break;
                        case "INCLUDE_JOB":
                            CRUDVagasServidor includeJobEmpresa = new CRUDVagasServidor();
                            includeJobEmpresa.IncludeJob(jsonCreate, out);
                            break;
                        case "LOOKUP_JOB":
                            CRUDVagasServidor lookupJobEmpresa = new CRUDVagasServidor();
                            lookupJobEmpresa.LookupJob(jsonCreate, out);
                            break;
                        case "LOOKUP_JOBSET":
                            CRUDVagasServidor lookupJobSetEmpresa = new CRUDVagasServidor();
                            lookupJobSetEmpresa.lookupJobSet(jsonCreate, out);
                            break;
                        case "UPDATE_JOB":
                            CRUDVagasServidor updateJobEmpresa = new CRUDVagasServidor();
                            updateJobEmpresa.updateJobCandidate(jsonCreate, out);
                            break;
                        case "DELETE_JOB":
                            CRUDVagasServidor deleteJobEmpresa = new CRUDVagasServidor();
                            deleteJobEmpresa.deleteJobCandidate(jsonCreate, out);
                            break;
                        case "SEARCH_JOB":
                            SearchJobServer searchJobServer = new SearchJobServer();
                            searchJobServer.SearchJobSet(jsonCreate, out);
                            break;
                        case "SET_JOB_AVAILABLE":
                            SetAvaiableServidor setAvaiable = new SetAvaiableServidor();
                            setAvaiable.setAvaiable(jsonCreate, out);
                            break;
                        case "SET_JOB_SEARCHABLE":
                            SetSearchableServidor setSearchable = new SetSearchableServidor();
                            setSearchable.setSearchable(jsonCreate, out);
                            break;
                        case "SEARCH_CANDIDATE":
                            SearchProfileServidor searchProfile = new SearchProfileServidor();
                            searchProfile.SearchProfile(jsonCreate, out);
                            break;
                        case "CHOOSE_CANDIDATE":
                            ChooseCandidateServer chooseCandidate = new ChooseCandidateServer();
                            chooseCandidate.ChooseCandidate(jsonCreate, out);
                            break;
                        case "GET_COMPANY":
                            GetCompanyServidor getCompany = new GetCompanyServidor();
                            getCompany.getCompany(jsonCreate, out);
                            break;
                        case "NAO_EXISTE":
                            System.out.println("Recebido do cliente: " + jsonCreate.get("operation"));
                            JsonObject jsonResponse = CreateJson.createResponse("NAO_EXISTE", "INVALID_OPERATION", "");
                            System.out.println("Mandando para o cliente: " + jsonResponse);
                            out.println(CreateJson.toJsonString(jsonResponse));
                            break;
                    }
                } catch (JsonException e) {
                    System.err.println("Erro ao desserializar mensagem JSON: " + e.getMessage());
                }
            }
        } catch (SocketException e) {
            System.out.println("Conexão resetada ou encerrada pelo cliente: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        } finally {
            clientGUI.removeClient(clientSocket.toString());

            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int serverPort = 0;
        boolean validPort = false;

        while (!validPort) {
            try {
                System.out.println("Digite o número da porta para iniciar o servidor:");
                String portInput = reader.readLine();
                serverPort = Integer.parseInt(portInput);

                if (serverPort > 20000 && serverPort < 25000) {
                    validPort = true;
                } else {
                    System.out.println("Por favor, insira uma porta válida (20000 - 25000).");
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println(e.getMessage());
            }
        }

        BufferedWriter fileWriter = null;

        try {
            fileWriter = new BufferedWriter(new FileWriter("server_log.txt", true));
            clientGUI = new ClientGUI();

            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                System.out.println("Servidor iniciado na porta " + serverPort);

                while (true) {
                    System.out.println("Aguardando conexão...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clientSocket);
                    new EchoServer(clientSocket, fileWriter);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
