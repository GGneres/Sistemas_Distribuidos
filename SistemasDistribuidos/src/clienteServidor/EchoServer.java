package clienteServidor;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.net.*; 
import java.io.*; 


public class EchoServer extends Thread{
	
	 private Socket clientSocket;
	 private BufferedWriter fileWriter;

	 public EchoServer(Socket clientSoc, BufferedWriter writer) {
		 clientSocket = clientSoc;
		 fileWriter = writer;
		 start();
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

	                    if ("LOGIN_CANDIDATE".equals(operation)) {
	                        JsonObject data = (JsonObject) jsonCreate.get("data");
	                        String email = (String) data.get("email");
	                        String password = (String) data.get("password");

	                        if ("candidate@example.com".equals(email) && "password123".equals(password)) {
	                            String token = "example_token";
	                            StatusVerification successResponse = new StatusVerification(operation, "SUCCESS", token);
	                            out.println(successResponse.toJsonString());
	                        } else if ("candidate@example.com".equals(email)) {
	                        	StatusVerification invalidPasswordResponse = new StatusVerification(operation, "INVALID_PASSWORD", "");
	                            out.println(invalidPasswordResponse.toJsonString());
	                        } else {
	                        	StatusVerification userNotFoundResponse = new StatusVerification(operation, "USER_NOT_FOUND", "");
	                            out.println(userNotFoundResponse.toJsonString());
	                        }
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