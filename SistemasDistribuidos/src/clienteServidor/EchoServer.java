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
import java.io.*; 


public class EchoServer extends Thread{
	
	 private Socket clientSocket;
	 private BufferedWriter fileWriter;
	 private static final String DATABASE_FILE = "user_database.txt";

	 public EchoServer(Socket clientSoc, BufferedWriter writer) {
		 clientSocket = clientSoc;
		 fileWriter = writer;
		 start();
	 }	
	 
	 public class JWTValidator{
		 private static final String TOKEN_KEY = "DISTRIBUIDOS";
		 private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
		 private static final JWTVerifier verifier = JWT.require(algorithm).build();
		 
		 public static String generateToken(String id, String role) {
			 return JWT.create()
					 .withClaim("id", id)
					 .withClaim("role", role)
					 .sign(algorithm);
		 }
		 
		 public static String getIdClaim(String token) throws JWTVerificationException{
			 DecodedJWT jwt = verifier.verify(token);
			 return jwt.getClaim("id").asString();
		 }
		 
		 public static String getRoleClaim(String token) throws JWTVerificationException{
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

	            boolean running = true;
	            while (running) {
	                String jsonMenssage = in.readLine();

	                if (jsonMenssage != null && jsonMenssage.equalsIgnoreCase("sair")) {
	                    running = false;
	                    continue;
	                }

	                if (jsonMenssage != null) {

	                    fileWriter.write(jsonMenssage);
	                    fileWriter.newLine();
	                    fileWriter.flush();

	                    JsonObject jsonCreate = (JsonObject) Jsoner.deserialize(jsonMenssage);
	                    String operation = (String) jsonCreate.get("operation");

	                    switch(operation) {
	                    case "LOGIN_CANDIDATE":
	                    	CRUDServer loginServer = new CRUDServer();
	                    	loginServer.handleLogin(jsonCreate, out);
	                    	break;
	                    case "SIGNUP_CANDIDATE":
	                    	CRUDServer RegistrarServer = new CRUDServer();
	                    	RegistrarServer.handleRegistrar(jsonCreate, out);
	                    	break;
	                    case "UPDATE_ACCOUNT_CANDIDATE":
	                    	//
	                    	break;
	                    case "DELETE_ACCOUNT_CANDIDATE":
	                    	//
	                    	break;
	                    }
	                }
	            }
	        } catch (IOException | JsonException e) {
	            System.err.println("Erro no servidor: " + e.getMessage());
	        } finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	                if (out != null) {
	                    out.close();
	                }
	                if (fileWriter != null) {
	                    fileWriter.close();
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