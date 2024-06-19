package clienteServidor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaLoginUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLoginUsuario frame = new TelaLoginUsuario();
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
	public TelaLoginUsuario() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(200, 10, 43, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel1 = new JLabel("Email");
		lblNewLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel1.setBounds(10, 69, 163, 23);
		contentPane.add(lblNewLabel1);
		
		textField = new JTextField();
		textField.setBounds(10, 90, 208, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel2 = new JLabel("Senha");
		lblNewLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel2.setBounds(10, 129, 163, 23);
		contentPane.add(lblNewLabel2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 151, 208, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Logar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(165, 213, 108, 29);
		contentPane.add(btnNewButton);
	}

	public JTextField getTextField() {
		return textField;
	}

	public JTextField getTextField_1() {
		return textField_1;
	}
	
	String auxiliar_email;
	String auxiliar_senha;

	public String getAuxiliar_email() {
		return auxiliar_email;
	}

	public void setAuxiliar_email(String auxiliar_email) {
		this.auxiliar_email = auxiliar_email;
	}

	public String getAuxiliar_senha() {
		return auxiliar_senha;
	}

	public void setAuxiliar_senha(String auxiliar_senha) {
		this.auxiliar_senha = auxiliar_senha;
	}
}
