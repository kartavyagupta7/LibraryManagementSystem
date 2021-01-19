import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import com.mysql.cj.jdbc.DatabaseMetaData;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;

public class LoginPage {

	JFrame frame;
	private JTextField libid;
	private JPasswordField loginPass;
	private JButton btn;
	static Map<String,Object> lib_m;
    static String id;
    private JButton btnCancel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 517, 280);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login Page");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(170, 11, 135, 34);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Lib_id");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(122, 82, 82, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(122, 140, 82, 15);
		frame.getContentPane().add(lblNewLabel_2);
		
		libid = new JTextField();
		libid.setBounds(220, 85, 149, 23);
		frame.getContentPane().add(libid);
		libid.setColumns(10);
		
		loginPass = new JPasswordField();
		loginPass.setBounds(220, 132, 149, 23);
		frame.getContentPane().add(loginPass);
		
		btn = new JButton("Login");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				id=libid.getText();
				String pass=loginPass.getText();
				
				DataBase.connect();
				      
				lib_m=DataBase.checkLoginCredentials(id,pass);
				if(lib_m.get("name")==null) {
					JOptionPane.showMessageDialog(frame,"Wrong username and password combination.");
					libid.setText("");
					loginPass.setText("");
					System.out.println("something wrongg");
				}
				else {
					Main obj=new Main();
					obj.frame.setVisible(true);
					frame.dispose();
					
				}
				
			}
		});
		btn.setBounds(385, 190, 89, 23);
		frame.getContentPane().add(btn);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     frame.dispose();
			}
		});
		btnCancel.setBounds(269, 190, 89, 23);
		frame.getContentPane().add(btnCancel);
	}
}
