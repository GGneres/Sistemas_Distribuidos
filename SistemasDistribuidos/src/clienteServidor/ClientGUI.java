package clienteServidor;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ClientGUI extends JFrame {
    private JTextArea connectedClientsArea;
    private Set<String> connectedClients;

    public ClientGUI() {
        setTitle("Clientes Conectados");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        connectedClients = new HashSet<>();
        connectedClientsArea = new JTextArea();
        connectedClientsArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(connectedClientsArea);
        scrollPane.setPreferredSize(new Dimension(380, 280));

        add(scrollPane);
        setVisible(true);
    }

    public void addClient(String clientInfo) {
        connectedClients.add(clientInfo);
        updateClientList();
    }

    public void removeClient(String clientInfo) {
        connectedClients.remove(clientInfo);
        updateClientList();
    }

    private void updateClientList() {
        StringBuilder clientList = new StringBuilder();
        for (String client : connectedClients) {
            clientList.append(client).append("\n");
        }
        connectedClientsArea.setText(clientList.toString());
    }
}
