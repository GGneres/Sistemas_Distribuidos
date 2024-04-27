package clienteServidor;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnNewButton;
	private int aux_PortNumber;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogin frame = new ClientLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(131, 59, 173, 29);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Digite a porta");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(131, 36, 163, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblDigiteOIp = new JLabel("Digite o IP");
		lblDigiteOIp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDigiteOIp.setBounds(131, 98, 163, 23);
		contentPane.add(lblDigiteOIp);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(131, 124, 173, 29);
		contentPane.add(textField_1);
		
		
		btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Porta: " + textField.getText() + " Ip: " + textField_1.getText());				
				setVisible(false);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton.setBounds(166, 175, 105, 38);
		contentPane.add(btnNewButton);
		
		//int PortNumber = Integer.parseInt(textField.getText());
		//aux_PortNumber = PortNumber;
		
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextField getTextField_1() {
		return textField_1;
	}

	public void setTextField_1(JTextField textField_1) {
		this.textField_1 = textField_1;
	}
	
	public int getPortNumber() {
		return aux_PortNumber;
	}
	
	String auxiliar_ip;
	public void setAuxiliar_IP(String auxiliar) {
		this.auxiliar_ip = auxiliar;
		return;
	}
	
	int auxiliar_port;
	public void setAuxiliar_Port(int auxiliar2) {
		this.auxiliar_port = auxiliar2;
		return;
	}
	
	public String getAuxiliar_IP() {
		return auxiliar_ip;
	}
	
	public int getAuxiliar_Port() {
		return auxiliar_port;
	}
	
	
	
}
