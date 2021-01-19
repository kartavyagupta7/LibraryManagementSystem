import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import com.toedter.calendar.JCalendar;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.SystemColor;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import java.util.*;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
public class Main {

	JFrame frame;
	private JTextField bookLength;
	private JTextField totalIssuedBooks;
	private JTextField totalStu;
	private JTextField defaulterCount;
	private JTextField DBstatus;
	JLayeredPane layeredPane;
	private JTable table;
	private JTextField t_bookid_return;
	private JTextField t_bookid;
	private JTextField t_stu_id;
	private JTextField textField_10;
	JLabel book_img_label;
	//Map<String,Object> book_m;
	Map<String,Object> stu_m;
	Map<String,Object> book_m;
	private JLabel lib_id;
	private JTable table1;
	static LocalDateTime now;
	static String date;
	JLabel sideImg;
	String path;
	private JPasswordField ana_pass;
	private JPasswordField ana_cpass;
	JTextArea ana_id;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally{
					 now = LocalDateTime.now(); 
					 date=now.getYear()+""+now.getMonthValue()+""+now.getDayOfMonth();
				}
			}
		});
	}
	
	public void switchPanels(JPanel panel) {
		path="";
		layeredPane.removeAll();
		layeredPane.add(panel);
		layeredPane.repaint();
		layeredPane.revalidate();
	}
	static int insertRecordData(DefaultTableModel model) {
		int t;
		try {
			DataBase db=new DataBase();
			Statement st = DataBase.con.createStatement();
			 String query = ("SELECT * from records ORDER BY Record_id DESC");
			 ResultSet rs = st.executeQuery(query);
			 while(rs.next()) {
				 model.addRow(new Object[] {
						 rs.getInt("Record_id"),
						 rs.getObject("Stu_id").toString(),
						 rs.getString("Stu_name"),
						 rs.getString("Stu_Course"),
						 rs.getString("Stu_Branch"),
						 rs.getString("Stu_Semester"),
						 rs.getObject("Book_id").toString(),
						 rs.getString("Book_Title"),
						 rs.getString("Author"),
						 rs.getString("Lib_id"),
						 rs.getDate("Issue_Date"),
						 rs.getDate("Return_Date")
						 
				 });
			 }
			
		}
		catch(SQLException e){
			 e.printStackTrace();
			 //return "FAIL";
		}
		return 0;
	}
	int insertBookData(DefaultTableModel model) {
		ResultSet rs=DataBase.booksTable();
		ArrayList<Object> imgInfo=new ArrayList<>();
		try {
		 while(rs.next()) {
			imgInfo.add(rs.getBytes("Book_image"));
			 String status; 
			if(rs.getInt("Status") == 1)
				status = "Issued";
			else
				status = "Not_Issued";
			
			 model.addRow(new Object[] {
					 rs.getInt("Book_id"),
					 rs.getString("Title"),
					 rs.getString("Publisher"),
					 rs.getString("Price"),
					 rs.getString("Pages"),
					 status
				
			 });
	     }
		
		}
		catch(Exception e) {
			System.out.println("Exception insertBookData");
		}
		
		//event on table1
				ListSelectionModel m= table1.getSelectionModel();
				m.addListSelectionListener(new ListSelectionListener() {	 
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						if(!m.isSelectionEmpty()) {
							int row=m.getMinSelectionIndex();
							//JOptionPane.showMessageDialog(frame,"row is "+row);
							byte[] img=(byte[]) imgInfo.get(row);
				    		  ImageIcon image=new ImageIcon(img);
					    	  Image im=image.getImage();
					    	  Image myImg=im.getScaledInstance(sideImg.getWidth(),sideImg.getHeight(),Image.SCALE_SMOOTH);
					    	  ImageIcon newImage=new ImageIcon(myImg);
					    	  sideImg.setIcon(newImage);
						}
					}
				});
		//
				
		return 0;
	}
	boolean insertStudentData(DefaultTableModel model) {
		ResultSet rs=DataBase.studentTable();
		ArrayList<Object> imgInfo=new ArrayList<>();
		try {
		while(rs.next()) {
			imgInfo.add(rs.getBytes("Stu_image"));
			 model.addRow(new Object[] {
					 rs.getInt(1),
					 rs.getString(2),
					 rs.getString(3),
					 rs.getString(4),
					 rs.getString(6),
					 rs.getString(7),
					 rs.getString(8),
					 rs.getInt(9),
					 rs.getInt(10),
				
			 });
	    }
		}
		catch(Exception e) {
			System.out.println("exception insertStudentData");
		}
		//event on table_2
		ListSelectionModel m= table1.getSelectionModel();
		m.addListSelectionListener(new ListSelectionListener() {	 
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if(!m.isSelectionEmpty()) {
					int row=m.getMinSelectionIndex();
					//JOptionPane.showMessageDialog(frame,"row is "+row);
					byte[] img=(byte[]) imgInfo.get(row);
		    		  ImageIcon image=new ImageIcon(img);
			    	  Image im=image.getImage();
			    	  Image myImg=im.getScaledInstance(sideImg.getWidth(),sideImg.getHeight(),Image.SCALE_SMOOTH);
			    	  ImageIcon newImage=new ImageIcon(myImg);
			    	  sideImg.setIcon(newImage);
				}
			}
		});
//
		return false;
	}
	boolean insertDefaulterStudent(DefaultTableModel model) {
		ResultSet rs=DataBase.defaulterTable();
		ArrayList<Object> imgInfo=new ArrayList<>();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 //Calendar c = Calendar.getInstance();
		try {
		while(rs.next()) {
			 //String dt = rs.getDate("Date_of_issue").toString();  // Start date
			 
			// c.setTime(sdf.parse(dt));
			 //c.add(Calendar.DATE, 12);  // number of days to add
			 //dt = sdf.format(c.getTime());
			 
			 imgInfo.add(rs.getBytes("Stu_image"));
			 
			 model.addRow(new Object[] {
					 rs.getInt("Stu_id"),
					 rs.getInt("Book_id"),
					 rs.getString("Stu_Name"),
					 rs.getString("Last_Name"),
					 rs.getString("Ph_NO"),
					 rs.getString("Course"),
					 rs.getString("Branch"),
					 rs.getInt("Stu_Semester"),
					 rs.getInt("BooksCount"),	
					 rs.getDate("Issue_Date")
					
			 });
			// System.out.println(rs.getDate("Date_of_issue").compareTo(null));
	    }
		}
		catch(Exception e) {
			System.out.println("exception insertDefaulterStudent");
		}
		//event on table_2
				ListSelectionModel m= table1.getSelectionModel();
				m.addListSelectionListener(new ListSelectionListener() {	 
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						if(!m.isSelectionEmpty()) {
							int row=m.getMinSelectionIndex();
							//JOptionPane.showMessageDialog(frame,"row is "+row);
							byte[] img=(byte[]) imgInfo.get(row);
				    		  ImageIcon image=new ImageIcon(img);
					    	  Image im=image.getImage();
					    	  Image myImg=im.getScaledInstance(sideImg.getWidth(),sideImg.getHeight(),Image.SCALE_SMOOTH);
					    	  ImageIcon newImage=new ImageIcon(myImg);
					    	  sideImg.setIcon(newImage);
						}
					}
				});
		return false;
	}
	
	//static void updateTableLengths()
	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
		frame.getContentPane().setLayout(null);

		
		
		
	   // updateTableLengths();
		
		
		JLabel lblNewLabel = new JLabel("Library Management System");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel.setBounds(362, 26, 504, 49);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_8 = new JLabel("University/College : Chitkara University(Punjab)");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_8.setBounds(474, 77, 245, 14);
		frame.getContentPane().add(lblNewLabel_8);
		
		JCalendar calendar = new JCalendar();
		calendar.setBounds(1025, 82, 205, 135);
		frame.getContentPane().add(calendar);
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.setBounds(35, 96, 980, 106);
		frame.getContentPane().add(panel_8);
		
		//int libid=1;
		
				JLabel lib_img = new JLabel("           null");
				lib_img.setBounds(6, 6, 82, 94);
				panel_8.add(lib_img);
				lib_img.setBorder(new LineBorder(new Color(0, 0, 0)));
				//Map<String,Object> lib_m = DataBase.getLibrarien("1");
				
				if(LoginPage.lib_m.get("image")!=null) {
				byte[] img=(byte[]) LoginPage.lib_m.get("image");
					ImageIcon image=new ImageIcon(img);
			  	    Image im=image.getImage();
				  	Image myImg=im.getScaledInstance(lib_img.getWidth(),lib_img.getHeight(),Image.SCALE_SMOOTH);
				  	ImageIcon newImage=new ImageIcon(myImg);
			  	    lib_img.setIcon(newImage);
				}
		
		JButton btnDisconnectDB = new JButton("Writting Operations OFF");
		btnDisconnectDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataBase.offDB();
				DBstatus.setText("OFF");
			}
		});
		btnDisconnectDB.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnDisconnectDB.setBounds(706, 72, 165, 23);
		panel_8.add(btnDisconnectDB);
		
		JLabel lblNewLabel_2 = new JLabel("Connection Status  :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(673, 13, 139, 14);
		panel_8.add(lblNewLabel_2);
		
		JLabel lblNewLabel_4 = new JLabel("Total Books");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_4.setBounds(128, 50, 69, 14);
		panel_8.add(lblNewLabel_4);
		
		JLabel lblNewLabel_4_1 = new JLabel("Book Issued");
		lblNewLabel_4_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_4_1.setBounds(128, 75, 69, 14);
		panel_8.add(lblNewLabel_4_1);
		
		String l=DataBase.booksLength();
		bookLength = new JTextField();
		bookLength.setText(l);
		bookLength.setColumns(10);
		bookLength.setBounds(218, 47, 124, 20);
		panel_8.add(bookLength);
		
		
		totalIssuedBooks = new JTextField();
		totalIssuedBooks.setText(DataBase.issuedBooksLength());
		totalIssuedBooks.setColumns(10);
		totalIssuedBooks.setBounds(218, 72, 124, 20);
		panel_8.add(totalIssuedBooks);
		
		JLabel lblNewLabel_5 = new JLabel("Total Students");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_5.setBounds(389, 50, 82, 14);
		panel_8.add(lblNewLabel_5);
		
		totalStu = new JTextField();
		totalStu.setText(DataBase.stuLength());
		totalStu.setColumns(10);
		totalStu.setBounds(491, 42, 124, 20);
		panel_8.add(totalStu);
		
		JLabel lblNewLabel_5_1 = new JLabel("Current Defaulters");
		lblNewLabel_5_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_5_1.setBounds(389, 75, 94, 14);
		panel_8.add(lblNewLabel_5_1);
		
		defaulterCount = new JTextField();
		defaulterCount.setText(DataBase.defaultersLength());
		defaulterCount.setColumns(10);
		defaulterCount.setBounds(491, 72, 124, 20);
		panel_8.add(defaulterCount);
		
		JButton btnConnectDB = new JButton("Writting Operations ON");
		btnConnectDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataBase.dbConnected==false) {
					//take password
					JPanel popPassword = new JPanel();
					JLabel label = new JLabel("Enter a password:");
					JPasswordField pass = new JPasswordField(20);
					popPassword.add(label);
					popPassword.add(pass);
					String[] options = new String[]{"OK", "Cancel"};
					int option = JOptionPane.showOptionDialog(null, popPassword, "Authentication",
					                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
					                         null, options, options[1]);
					System.out.println(option);
					if(option==0 && pass.getText().equals("myproject")) {
					   DataBase.onDB();
					   DBstatus.setText("ON");
					}
					else {
						pass.setText("");
					}
				}
				
			}
		});
		btnConnectDB.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnConnectDB.setBounds(706, 41, 165, 23);
		panel_8.add(btnConnectDB);
		
		DBstatus = new JTextField();
		DBstatus.setText(" OFF");
		DBstatus.setColumns(10);
		DBstatus.setBounds(822, 10, 103, 20);
		panel_8.add(DBstatus);
		
		JButton btnNewButton = new JButton("Logout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage obj=new LoginPage();
				obj.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton.setBounds(881, 57, 89, 23);
		panel_8.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Librarian_id  : ");
		lblNewLabel_1.setBounds(146, 13, 94, 14);
		panel_8.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Name  : ");
		lblNewLabel_1_1.setBounds(430, 13, 59, 14);
		panel_8.add(lblNewLabel_1_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(123, 34, 500, 2);
		panel_8.add(separator);
		
		JLabel libid = new JLabel("New label");
		libid.setBounds(247, 13, 82, 14);
		panel_8.add(libid);
		
		JLabel lib_name = new JLabel("New label");
		lib_name.setBounds(491, 13, 82, 14);
		panel_8.add(lib_name);
				
		libid.setText(LoginPage.id);
		lib_name.setText(LoginPage.lib_m.get("name").toString());
		
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.control);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(95, 220, 1150, 410);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		layeredPane.setBounds(10, 34, 1123, 360);
		panel.add(layeredPane);
		layeredPane.setLayout(new CardLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), "ISSUE BOOK", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		panel_1.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_1, "name_18696114564500");
		panel_1.setLayout(null);
		
		
		
		JPanel panel_12_1_1 = new JPanel();
		panel_12_1_1.setLayout(null);
		panel_12_1_1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "BOOK_PANEL", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(109, 109, 109)));
		panel_12_1_1.setBackground(SystemColor.control);
		panel_12_1_1.setBounds(29, 50, 518, 270);
		panel_1.add(panel_12_1_1);
		
		
		
		
		book_img_label = new JLabel("                  null");
		//book_img_label.setHorizontalAlignment(SwingConstants.CENTER);
		book_img_label.setBorder(new LineBorder(new Color(0, 0, 0)));
		book_img_label.setBackground(SystemColor.controlShadow);
		book_img_label.setBounds(340, 66, 137, 171);
		panel_12_1_1.add(book_img_label);
		
		JLabel lblNewLabel_13_1_1_1_3 = new JLabel("Book_Titile ");
		lblNewLabel_13_1_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_3.setBounds(57, 80, 80, 20);
		panel_12_1_1.add(lblNewLabel_13_1_1_1_3);
		
		JLabel t_title = new JLabel("null");
		t_title.setBackground(SystemColor.control);
		t_title.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_title.setBounds(160, 74, 170, 32);
		panel_12_1_1.add(t_title);
		
		JLabel lblNewLabel_13_1_1_1_1_1 = new JLabel("Author");
		lblNewLabel_13_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_1_1.setBounds(57, 108, 80, 20);
		panel_12_1_1.add(lblNewLabel_13_1_1_1_1_1);
		
		JLabel lblNewLabel_13_1_1_3 = new JLabel("Published By ");
		lblNewLabel_13_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_3.setBounds(57, 139, 80, 20);
		panel_12_1_1.add(lblNewLabel_13_1_1_3);
		
		JLabel lblNewLabel_13_1_1_2_2 = new JLabel("Price");
		lblNewLabel_13_1_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_2_2.setBounds(57, 172, 80, 20);
		panel_12_1_1.add(lblNewLabel_13_1_1_2_2);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("Book Id        :");
		lblNewLabel_6_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_6_1_1.setBounds(66, 28, 109, 22);
		panel_12_1_1.add(lblNewLabel_6_1_1);
		
		JLabel lblNewLabel_13_1_1_2_1_1 = new JLabel("Total_Pages");
		lblNewLabel_13_1_1_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_2_1_1.setBounds(57, 199, 80, 20);
		panel_12_1_1.add(lblNewLabel_13_1_1_2_1_1);
		
		
		
		
		
		t_bookid = new JTextField();
		t_bookid.setBounds(168, 31, 122, 20);
		panel_12_1_1.add(t_bookid);
		t_bookid.setColumns(10);
		
		
		
		JLabel t_author = new JLabel("null");
		t_author.setBackground(SystemColor.control);
		t_author.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_author.setBounds(165, 110, 142, 18);
		panel_12_1_1.add(t_author);
		
		JLabel t_publisher = new JLabel("null");
		t_publisher.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_publisher.setBounds(165, 139, 165, 18);
		panel_12_1_1.add(t_publisher);
		
		JLabel t_price = new JLabel("null");
		t_price.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_price.setBounds(165, 170, 142, 18);
		panel_12_1_1.add(t_price);
		
		JLabel t_pages = new JLabel("null");
		t_pages.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_pages.setBounds(165, 199, 142, 18);
		panel_12_1_1.add(t_pages);
		
		JLabel lblNewLabel_13_5_1 = new JLabel(":");
		lblNewLabel_13_5_1.setBounds(147, 84, 10, 14);
		panel_12_1_1.add(lblNewLabel_13_5_1);
		
		JLabel lblNewLabel_13_5_2 = new JLabel(":");
		lblNewLabel_13_5_2.setBounds(145, 108, 10, 14);
		panel_12_1_1.add(lblNewLabel_13_5_2);
		
		JLabel lblNewLabel_13_5_3 = new JLabel(":");
		lblNewLabel_13_5_3.setBounds(145, 139, 10, 14);
		panel_12_1_1.add(lblNewLabel_13_5_3);
		
		JLabel lblNewLabel_13_5_4 = new JLabel(":");
		lblNewLabel_13_5_4.setBounds(147, 172, 10, 14);
		panel_12_1_1.add(lblNewLabel_13_5_4);
		
		JLabel lblNewLabel_13_5_5 = new JLabel(":");
		lblNewLabel_13_5_5.setBounds(147, 199, 10, 14);
		panel_12_1_1.add(lblNewLabel_13_5_5);
		
		JPanel panel_12_2 = new JPanel();
		panel_12_2.setLayout(null);
		panel_12_2.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "STUDENT_PANEL", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(100, 100, 100)));
		panel_12_2.setBackground(SystemColor.control);
		panel_12_2.setBounds(573, 50, 518, 270);
		panel_1.add(panel_12_2);
		
		JLabel lblNewLabel_13_1_1_1_2_3 = new JLabel("Roll_Number   :");
		lblNewLabel_13_1_1_1_2_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_13_1_1_1_2_3.setBounds(71, 25, 123, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_3);
		
		JLabel lblNewLabel_13_1_1_1_2_2_2_1 = new JLabel("Fathers_Name");
		lblNewLabel_13_1_1_1_2_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_2_1.setBounds(71, 115, 90, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_2_1);
		
		JLabel lblNewLabel_13_1_1_1_2_2_9 = new JLabel("Branch");
		lblNewLabel_13_1_1_1_2_2_9.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_9.setBounds(71, 195, 50, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_9);
		
		JLabel lblNewLabel_13_1_1_1_2_2_3_2 = new JLabel("Course");
		lblNewLabel_13_1_1_1_2_2_3_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_3_2.setBounds(71, 169, 50, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_3_2);
		
		JLabel lblNewLabel_13_1_1_1_2_2_1_2 = new JLabel("Semester");
		lblNewLabel_13_1_1_1_2_2_1_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_1_2.setBounds(71, 220, 60, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_1_2);
		
		JLabel lblNewLabel_13_1_1_1_2_2_3_1_1 = new JLabel("Student_Name");
		lblNewLabel_13_1_1_1_2_2_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_3_1_1.setBounds(71, 70, 90, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_3_1_1);
		
		JLabel lblNewLabel_13_5 = new JLabel(":");
		lblNewLabel_13_5.setBounds(171, 94, 10, 14);
		panel_12_2.add(lblNewLabel_13_5);
		
		JLabel lblNewLabel_13_1_2 = new JLabel(":");
		lblNewLabel_13_1_2.setBounds(171, 121, 10, 14);
		panel_12_2.add(lblNewLabel_13_1_2);
		
		JLabel lblNewLabel_13_2_1 = new JLabel(":");
		lblNewLabel_13_2_1.setBounds(171, 144, 10, 14);
		panel_12_2.add(lblNewLabel_13_2_1);
		
		JLabel lblNewLabel_13_3_1 = new JLabel(":");
		lblNewLabel_13_3_1.setBounds(171, 169, 10, 14);
		panel_12_2.add(lblNewLabel_13_3_1);
		
		JLabel lblNewLabel_13_4_2 = new JLabel(":");
		lblNewLabel_13_4_2.setBounds(171, 194, 10, 14);
		panel_12_2.add(lblNewLabel_13_4_2);
		
		JLabel t_name = new JLabel("null");
		t_name.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_name.setBounds(191, 70, 120, 20);
		panel_12_2.add(t_name);
		
		JLabel t_fname = new JLabel("null");
		t_fname.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_fname.setBounds(191, 115, 120, 20);
		panel_12_2.add(t_fname);
		
		JLabel t_course = new JLabel("null");
		t_course.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_course.setBounds(191, 169, 120, 20);
		panel_12_2.add(t_course);
		
		JLabel t_branch = new JLabel("null");
		t_branch.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_branch.setBounds(191, 195, 120, 20);
		panel_12_2.add(t_branch);
		
		JLabel t_sem = new JLabel("null");
		t_sem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_sem.setBounds(191, 220, 120, 20);
		panel_12_2.add(t_sem);
		
		JLabel lblNewLabel_13_1_1_1_2_2_1_1_1 = new JLabel("Last_Name");
		lblNewLabel_13_1_1_1_2_2_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_1_1_1.setBounds(71, 90, 80, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_1_1_1);
		
		JLabel lblNewLabel_13_4_1_1 = new JLabel(":");
		lblNewLabel_13_4_1_1.setBounds(171, 224, 10, 14);
		panel_12_2.add(lblNewLabel_13_4_1_1);
		
		JLabel t_ph = new JLabel("null");
		t_ph.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_ph.setBounds(191, 140, 120, 20);
		panel_12_2.add(t_ph);
		
		JLabel t_lname = new JLabel("null");
		t_lname.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_lname.setBounds(192, 90, 120, 20);
		panel_12_2.add(t_lname);
		
		t_stu_id = new JTextField();
		t_stu_id.setBackground(SystemColor.window);
		t_stu_id.setForeground(SystemColor.textText);
		t_stu_id.setBounds(187, 27, 118, 20);
		panel_12_2.add(t_stu_id);
		t_stu_id.setColumns(10);
		
		
		
		
		JLabel lblNewLabel_3 = new JLabel("Date of issue :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_3.setBounds(69, 325, 73, 14);
		panel_1.add(lblNewLabel_3);
		
		textField_10 = new JTextField();
		textField_10.setText("20/20/2020");
		textField_10.setBounds(172, 325, 81, 20);
		panel_1.add(textField_10);
		textField_10.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "<html><b>RETURN BOOK</b></html>", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		panel_2.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_2, "name_18701756387700");
		panel_2.setLayout(null);
		
		
		
		JLabel lblNewLabel_6 = new JLabel("Book Id    :");
		lblNewLabel_6.setBounds(114, 31, 71, 22);
		panel_2.add(lblNewLabel_6);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		t_bookid_return = new JTextField();
		t_bookid_return.setBounds(195, 32, 148, 20);
		panel_2.add(t_bookid_return);
		t_bookid_return.setFont(new Font("Tahoma", Font.PLAIN, 14));
		t_bookid_return.setColumns(10);
		
		
		
		JPanel panel_12_1 = new JPanel();
		panel_12_1.setLayout(null);
		panel_12_1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "BOOK_PANEL", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(109, 109, 109)));
		panel_12_1.setBackground(SystemColor.control);
		panel_12_1.setBounds(37, 65, 518, 270);
		panel_2.add(panel_12_1);
		
		JLabel img_book_return = new JLabel("                   null");
		img_book_return.setBorder(new LineBorder(new Color(0, 0, 0)));
		img_book_return.setBounds(319, 56, 137, 171);
		panel_12_1.add(img_book_return);
		
		JLabel lblNewLabel_13_1_1_1 = new JLabel("Book_Titile ");
		lblNewLabel_13_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1.setBounds(57, 79, 98, 20);
		panel_12_1.add(lblNewLabel_13_1_1_1);
		
		JLabel t_title_return = new JLabel("null");
		t_title_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_title_return.setBounds(164, 79, 160, 18);
		panel_12_1.add(t_title_return);
		
		JLabel lblNewLabel_13_1_1_1_1 = new JLabel("Author");
		lblNewLabel_13_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_1.setBounds(57, 108, 98, 20);
		panel_12_1.add(lblNewLabel_13_1_1_1_1);
		
		JLabel t_author_return = new JLabel("null");
		t_author_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_author_return.setBounds(164, 108, 137, 18);
		panel_12_1.add(t_author_return);
		
		JLabel lblNewLabel_13_1_1 = new JLabel("Published By ");
		lblNewLabel_13_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1.setBounds(57, 139, 98, 20);
		panel_12_1.add(lblNewLabel_13_1_1);
		
		JLabel t_publisher_return = new JLabel("null");
		t_publisher_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_publisher_return.setBounds(164, 139, 117, 18);
		panel_12_1.add(t_publisher_return);
		
		JLabel lblNewLabel_13_1_1_2 = new JLabel("Price");
		lblNewLabel_13_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_13_1_1_2.setBounds(57, 172, 98, 20);
		panel_12_1.add(lblNewLabel_13_1_1_2);
		
		JLabel t_price_return = new JLabel("null");
		t_price_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_price_return.setBounds(164, 172, 137, 18);
		panel_12_1.add(t_price_return);
		
		JLabel t_doi = new JLabel("Book was issued on : 10/10/2020");
		t_doi.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_doi.setBounds(57, 230, 212, 18);
		panel_12_1.add(t_doi);
		
		JLabel lblNewLabel_6_1 = new JLabel("Book Id");
		lblNewLabel_6_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_6_1.setBounds(57, 50, 74, 22);
		panel_12_1.add(lblNewLabel_6_1);
		
		JLabel bookid = new JLabel("null");
		bookid.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bookid.setBounds(164, 50, 104, 18);
		panel_12_1.add(bookid);
		
		JLabel lblNewLabel_13_1_1_2_1 = new JLabel("Total_Pages");
		lblNewLabel_13_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_13_1_1_2_1.setBounds(57, 199, 98, 20);
		panel_12_1.add(lblNewLabel_13_1_1_2_1);
		
		JLabel t_pages_return = new JLabel("null");
		t_pages_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_pages_return.setBounds(164, 199, 137, 18);
		panel_12_1.add(t_pages_return);
		
		
		
		JLabel lblNewLabel_12 = new JLabel(":");
		lblNewLabel_12.setBounds(145, 56, 10, 14);
		panel_12_1.add(lblNewLabel_12);
		
		JLabel lblNewLabel_12_1 = new JLabel(":");
		lblNewLabel_12_1.setBounds(145, 83, 10, 14);
		panel_12_1.add(lblNewLabel_12_1);
		
		JLabel lblNewLabel_12_3 = new JLabel(":");
		lblNewLabel_12_3.setBounds(145, 112, 10, 14);
		panel_12_1.add(lblNewLabel_12_3);
		
		JLabel lblNewLabel_12_4 = new JLabel(":");
		lblNewLabel_12_4.setBounds(145, 143, 10, 14);
		panel_12_1.add(lblNewLabel_12_4);
		
		JLabel lblNewLabel_12_5 = new JLabel(":");
		lblNewLabel_12_5.setBounds(145, 177, 10, 14);
		panel_12_1.add(lblNewLabel_12_5);
		
		JLabel lblNewLabel_12_6 = new JLabel(":");
		lblNewLabel_12_6.setBounds(145, 203, 10, 14);
		panel_12_1.add(lblNewLabel_12_6);
		
		JPanel panel_12 = new JPanel();
		panel_12.setLayout(null);
		panel_12.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "STUDENT_PANEL", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(100, 100, 100)));
		panel_12.setBackground(SystemColor.control);
		panel_12.setBounds(565, 65, 518, 270);
		panel_2.add(panel_12);
		
		JLabel lblNewLabel_13_1_1_1_2 = new JLabel("Roll_Number");
		lblNewLabel_13_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_13_1_1_1_2.setBounds(71, 53, 98, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2);
		
		JLabel lblNewLabel_13_1_1_1_2_2_2 = new JLabel("Fathers_Name");
		lblNewLabel_13_1_1_1_2_2_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_2.setBounds(71, 105, 90, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2_2_2);
		
		JLabel lblNewLabel_13_1_1_1_2_2 = new JLabel("Branch");
		lblNewLabel_13_1_1_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2.setBounds(71, 155, 50, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2_2);
		
		JLabel lblNewLabel_13_1_1_1_2_2_3 = new JLabel("Course");
		lblNewLabel_13_1_1_1_2_2_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_3.setBounds(71, 130, 50, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2_2_3);
		
		JLabel lblNewLabel_13_1_1_1_2_2_1 = new JLabel("Semester");
		lblNewLabel_13_1_1_1_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_1.setBounds(71, 180, 60, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2_2_1);
		
		JLabel lblNewLabel_13_1_1_1_2_2_3_1 = new JLabel("Student_Name");
		lblNewLabel_13_1_1_1_2_2_3_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_3_1.setBounds(71, 80, 90, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2_2_3_1);
		
		JLabel lblNewLabel_13 = new JLabel(":");
		lblNewLabel_13.setBounds(171, 84, 10, 14);
		panel_12.add(lblNewLabel_13);
		
		JLabel lblNewLabel_13_1 = new JLabel(":");
		lblNewLabel_13_1.setBounds(171, 111, 10, 14);
		panel_12.add(lblNewLabel_13_1);
		
		JLabel lblNewLabel_13_2 = new JLabel(":");
		lblNewLabel_13_2.setBounds(171, 134, 10, 14);
		panel_12.add(lblNewLabel_13_2);
		
		JLabel lblNewLabel_13_3 = new JLabel(":");
		lblNewLabel_13_3.setBounds(171, 159, 10, 14);
		panel_12.add(lblNewLabel_13_3);
		
		JLabel lblNewLabel_13_4 = new JLabel(":");
		lblNewLabel_13_4.setBounds(171, 184, 10, 14);
		panel_12.add(lblNewLabel_13_4);
		
		JLabel t_name_return = new JLabel("null");
		t_name_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_name_return.setBounds(191, 80, 96, 20);
		panel_12.add(t_name_return);
		
		JLabel t_fname_return = new JLabel("null");
		t_fname_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_fname_return.setBounds(191, 105, 96, 20);
		panel_12.add(t_fname_return);
		
		JLabel t_course_return = new JLabel("null");
		t_course_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_course_return.setBounds(191, 130, 96, 20);
		panel_12.add(t_course_return);
		
		JLabel t_branch_return = new JLabel("null");
		t_branch_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_branch_return.setBounds(191, 155, 96, 20);
		panel_12.add(t_branch_return);
		
		JLabel t_sem_return = new JLabel("null");
		t_sem_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_sem_return.setBounds(191, 180, 96, 20);
		panel_12.add(t_sem_return);
		
		JLabel lblNewLabel_13_1_1_1_2_2_1_1 = new JLabel("Last_Name");
		lblNewLabel_13_1_1_1_2_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_1_1.setBounds(71, 205, 80, 20);
		panel_12.add(lblNewLabel_13_1_1_1_2_2_1_1);
		
		JLabel lblNewLabel_13_4_1 = new JLabel(":");
		lblNewLabel_13_4_1.setBounds(171, 209, 10, 14);
		panel_12.add(lblNewLabel_13_4_1);
		
		JLabel t_lname_return = new JLabel("null");
		t_lname_return.setFont(new Font("Tahoma", Font.PLAIN, 13));
		t_lname_return.setBounds(191, 205, 96, 20);
		panel_12.add(t_lname_return);
		
		JLabel img_stu_return = new JLabel("                   null");
		img_stu_return.setBorder(new LineBorder(new Color(0, 0, 0)));
		img_stu_return.setBounds(315, 58, 137, 171);
		panel_12.add(img_stu_return);
		
		JLabel rollno = new JLabel("null");
		rollno.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rollno.setBounds(191, 53, 96, 20);
		panel_12.add(rollno);
		
		JLabel lblNewLabel_13_6 = new JLabel(":");
		lblNewLabel_13_6.setBounds(171, 58, 10, 14);
		panel_12.add(lblNewLabel_13_6);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_3, "name_18706776867200");
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_7 = new JLabel("Library  Records");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_7.setBounds(24, 5, 129, 26);
		panel_3.add(lblNewLabel_7);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 1101, 305);
		panel_3.add(scrollPane);
		
		
		
		
		
		
		
		
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_4.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_4, "name_18710112983700");
		panel_4.setLayout(null);
		
		JLabel bsd = new JLabel("Books ");
		bsd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		bsd.setBounds(10, 11, 129, 26);
		panel_4.add(bsd);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 70, 887, 279);
		panel_4.add(scrollPane_1);
		
		
		table1 = new JTable();
		scrollPane_1.setViewportView(table1);
		
		
		
		
		sideImg = new JLabel("                   null");
		sideImg.setBorder(new LineBorder(new Color(0, 0, 0)));
		sideImg.setBackground(SystemColor.textHighlightText);
		sideImg.setBounds(937, 109, 137, 179);
		panel_4.add(sideImg);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_5.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_5, "name_18716524046300");
		panel_5.setLayout(null);
		
		JLabel lblNewLabel_7_1_1 = new JLabel("Addnew Book");
		lblNewLabel_7_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_7_1_1.setBounds(20, 11, 129, 26);
		panel_5.add(lblNewLabel_7_1_1);
		
		JLabel addBookImg = new JLabel("                  null");
		addBookImg.setBorder(new LineBorder(new Color(0, 0, 0)));
		addBookImg.setBackground(SystemColor.controlShadow);
		addBookImg.setBounds(721, 78, 137, 171);
		panel_5.add(addBookImg);
		
		JButton btnNewButton_3 = new JButton("Attach Photo");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile=new JFileChooser();
				jfile.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter=new FileNameExtensionFilter("*.Image","jpg"); 
				jfile.addChoosableFileFilter(filter);
				
				int result=jfile.showSaveDialog(null);
				//System.out.print(result);
				File selectedFile=jfile.getSelectedFile();
				String filename=selectedFile.getName();
				System.out.println(filename);
				if(filename.endsWith(".jpg") || filename.endsWith(".png")) {
					if(result == JFileChooser.APPROVE_OPTION){
						path=selectedFile.getAbsolutePath();
						ImageIcon myImage=new ImageIcon(path);
						
						Image img=myImage.getImage();
						Image newImage=img.getScaledInstance(addBookImg.getWidth(), addBookImg.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon image=new ImageIcon(newImage);
						addBookImg.setIcon(image);
					}
				}
				else {
					System.out.println("please select img");
				}
			}
		});
		btnNewButton_3.setBounds(738, 272, 113, 23);
		panel_5.add(btnNewButton_3);
		
		
		
		JLabel lblNewLabel_9 = new JLabel("Title :");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9.setBounds(186, 80, 89, 26);
		panel_5.add(lblNewLabel_9);
		
		JTextArea titleField = new JTextArea();
		titleField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		titleField.setFont(new Font("Monospaced", Font.PLAIN, 15));
		titleField.setBounds(298, 80, 214, 23);
		panel_5.add(titleField);
		
		JLabel lblNewLabel_9_1 = new JLabel("Author :");
		lblNewLabel_9_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_1.setBounds(186, 120, 89, 26);
		panel_5.add(lblNewLabel_9_1);
		
		JLabel lblNewLabel_9_1_1 = new JLabel("Publisher :");
		lblNewLabel_9_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_1_1.setBounds(186, 160, 89, 26);
		panel_5.add(lblNewLabel_9_1_1);
		
		JLabel lblNewLabel_9_1_1_1 = new JLabel("Price :");
		lblNewLabel_9_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_1_1_1.setBounds(186, 200, 89, 26);
		panel_5.add(lblNewLabel_9_1_1_1);
		
		JLabel lblNewLabel_9_1_1_1_1 = new JLabel("Total Pages :");
		lblNewLabel_9_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_1_1_1_1.setBounds(176, 240, 99, 26);
		panel_5.add(lblNewLabel_9_1_1_1_1);
		
		JTextArea authorField = new JTextArea();
		authorField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		authorField.setFont(new Font("Monospaced", Font.PLAIN, 16));
		authorField.setBounds(298, 120, 214, 23);
		panel_5.add(authorField);
		
		JTextArea publisherField = new JTextArea();
		publisherField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		publisherField.setBounds(298, 160, 214, 23);
		panel_5.add(publisherField);
		
		JTextArea priceField = new JTextArea();
		priceField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		priceField.setBounds(298, 200, 214, 23);
		panel_5.add(priceField);
		
		JTextArea pageField = new JTextArea();
		pageField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pageField.setBounds(295, 240, 214, 23);
		panel_5.add(pageField);
		
		
		JButton btnNewButton_3_1 = new JButton("Add Book");
		btnNewButton_3_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
				String title=titleField.getText();
				String author=authorField.getText();
				String publisher=publisherField.getText();
				String price=priceField.getText();
				String pages=pageField.getText();
				int id=DataBase.insertBook(title, author, publisher, price, pages,path);
				if(id!=-1) {
					bookLength.setText(DataBase.booksLength());
					JOptionPane.showMessageDialog(frame,"Book successfully inserted with book id = "+id);
				}
				else {
					JOptionPane.showMessageDialog(frame,"Book not inserted, Something Went Wrong");
				}
			}
		});
		btnNewButton_3_1.setBounds(990, 313, 99, 23);
		panel_5.add(btnNewButton_3_1);
		
		
		
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_6.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_6, "name_18719393214500");
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_7_1_1_1 = new JLabel("Add new Student");
		lblNewLabel_7_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_7_1_1_1.setBounds(21, 11, 129, 26);
		panel_6.add(lblNewLabel_7_1_1_1);
		
		JLabel addStuImg = new JLabel("                  null");
		addStuImg.setBorder(new LineBorder(new Color(0, 0, 0)));
		addStuImg.setBackground(SystemColor.controlShadow);
		addStuImg.setBounds(741, 87, 137, 171);
		panel_6.add(addStuImg);
		
		JButton btnNewButton_3_2 = new JButton("Attach Photo");
		btnNewButton_3_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_3_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile=new JFileChooser();
				jfile.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter=new FileNameExtensionFilter("*.Image","jpg"); 
				jfile.addChoosableFileFilter(filter);
				
				int result=jfile.showSaveDialog(null);
				//System.out.print(result);
				File selectedFile=jfile.getSelectedFile();
				String filename=selectedFile.getName();
				System.out.println(filename);
				if(filename.endsWith(".jpg") || filename.endsWith(".png")) {
					if(result == JFileChooser.APPROVE_OPTION){
						path=selectedFile.getAbsolutePath();
						ImageIcon myImage=new ImageIcon(path);
						
						Image img=myImage.getImage();
						Image newImage=img.getScaledInstance(addStuImg.getWidth(), addStuImg.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon image=new ImageIcon(newImage);
						addStuImg.setIcon(image);
					}
				}
				else {
					System.out.println("please select img");
				}
			}
		});
		btnNewButton_3_2.setBounds(751, 276, 113, 23);
		panel_6.add(btnNewButton_3_2);
		
		
		JTextArea tstu_id = new JTextArea();
		tstu_id.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tstu_id.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tstu_id.setBounds(383, 39, 214, 23);
		panel_6.add(tstu_id);
		
		JLabel lblNewLabel_9_2 = new JLabel("Roll_Number :");
		lblNewLabel_9_2.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2.setBounds(232, 39, 113, 26);
		panel_6.add(lblNewLabel_9_2);
		
		JLabel lblNewLabel_9_2_1 = new JLabel("Name :");
		lblNewLabel_9_2_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1.setBounds(194, 100, 89, 26);
		panel_6.add(lblNewLabel_9_2_1);
		
		JTextArea tname = new JTextArea();
		tname.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tname.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tname.setBounds(342, 100, 214, 23);
		panel_6.add(tname);
		
		JLabel lblNewLabel_9_2_1_1 = new JLabel("Last Name :");
		lblNewLabel_9_2_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1_1.setBounds(194, 143, 89, 26);
		panel_6.add(lblNewLabel_9_2_1_1);
		
		JTextArea tlname = new JTextArea();
		tlname.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tlname.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tlname.setBounds(342, 143, 214, 23);
		panel_6.add(tlname);
		
		JLabel lblNewLabel_9_2_1_1_1 = new JLabel("Phone_No :");
		lblNewLabel_9_2_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1_1_1.setBounds(194, 177, 89, 26);
		panel_6.add(lblNewLabel_9_2_1_1_1);
		
		JTextArea tph = new JTextArea();
		tph.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tph.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tph.setBounds(342, 177, 214, 23);
		panel_6.add(tph);
		
		JLabel lblNewLabel_9_2_1_1_1_1 = new JLabel("Father's Name :");
		lblNewLabel_9_2_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1_1_1_1.setBounds(194, 220, 127, 26);
		panel_6.add(lblNewLabel_9_2_1_1_1_1);
		
		JTextArea tfname = new JTextArea();
		tfname.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tfname.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tfname.setBounds(342, 220, 214, 23);
		panel_6.add(tfname);
		
		JLabel lblNewLabel_9_2_1_1_1_1_1 = new JLabel("Course :");
		lblNewLabel_9_2_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1_1_1_1_1.setBounds(194, 251, 127, 26);
		panel_6.add(lblNewLabel_9_2_1_1_1_1_1);
		
		JLabel lblNewLabel_9_2_1_1_1_1_1_1 = new JLabel("Branch :");
		lblNewLabel_9_2_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1_1_1_1_1_1.setBounds(194, 285, 127, 26);
		panel_6.add(lblNewLabel_9_2_1_1_1_1_1_1);
		
		JLabel lblNewLabel_9_2_1_1_1_1_1_1_1 = new JLabel("Semester :");
		lblNewLabel_9_2_1_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_1_1_1_1_1_1_1.setBounds(194, 322, 127, 26);
		panel_6.add(lblNewLabel_9_2_1_1_1_1_1_1_1);
		
		JTextArea tcourse = new JTextArea();
		tcourse.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tcourse.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tcourse.setBounds(342, 254, 214, 23);
		panel_6.add(tcourse);
		
		JTextArea tbranch = new JTextArea();
		tbranch.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tbranch.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tbranch.setBounds(342, 288, 214, 23);
		panel_6.add(tbranch);
		
		JTextArea tsem = new JTextArea();
		tsem.setFont(new Font("Monospaced", Font.PLAIN, 15));
		tsem.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tsem.setBounds(342, 322, 214, 23);
		panel_6.add(tsem);
		
		
		JButton btnNewButton_3_1_1 = new JButton("Add Student");
		btnNewButton_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_3_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
				String roll_no = tstu_id.getText();
				if(roll_no.equals(""))
					JOptionPane.showMessageDialog(frame,"Please enter roll number");
				
				String name = tname.getText();
				String lname = tlname.getText();
				String ph = tph.getText();
				String father_name = tfname.getText();
				String course = tcourse.getText();
				String branch = tbranch.getText();
				int sem = Integer.parseInt(tsem.getText());
				boolean t = DataBase.insertStudent(roll_no, name,lname,ph, path, father_name, course, branch, sem);
				if(t) {
					totalStu.setText(DataBase.stuLength());
					JOptionPane.showMessageDialog(frame,"successfully inserted");
				}
				else
					JOptionPane.showMessageDialog(frame,"Something Went Wrong");
				
				
			}
		});
		btnNewButton_3_1_1.setBounds(997, 315, 99, 23);
		panel_6.add(btnNewButton_3_1_1);
		
		
		
		JButton btnNewStudent = new JButton("<html><center>Add<br>Newbook</center></html>");
		btnNewStudent.setMargin(new Insets(0,-30, 0,-30));
		btnNewStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("new book");
		    	//NewBook obj=new NewBook();
		    	//obj.frame.setVisible(true);
				titleField.setText("");
				authorField.setText("");
				publisherField.setText("");
				priceField.setText("");
				pageField.setText("");
				path="";
				addBookImg.setIcon(null);
				addBookImg.setText("                   null");
				switchPanels(panel_5);
			}
		});
		btnNewStudent.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewStudent.setBounds(23, 275, 60, 49);
		frame.getContentPane().add(btnNewStudent);
		
		
		
		JButton btnNewButton_1_1 = new JButton("<html><center>New<br>Student</center></html>");
		btnNewButton_1_1.setMargin(new Insets(0,-30, 0,-30));
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("new Student");
				path="";
			    tstu_id.setText("");
				tname.setText("");
				tlname.setText("");
				tph.setText("");
				tfname.setText("");
				tcourse.setText("");
				tbranch.setText("");
				addStuImg.setIcon(null);
				addStuImg.setText("                   null");
		    	
				//NewStudent stu=new NewStudent();
		    	//stu.frame.setVisible(true);
				switchPanels(panel_6);
			}
		});
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_1_1.setBounds(23, 358, 60, 49);
		frame.getContentPane().add(btnNewButton_1_1);
		
		
		
		JButton btnNewButton_2 = new JButton("Issue_Book");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchPanels(panel_1);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_2.setBounds(0, 0, 90, 23);
		panel.add(btnNewButton_2);
		
		
		
		JButton btnNewButton_2_1_1 = new JButton("Records");
		btnNewButton_2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table = new JTable();
				scrollPane.setViewportView(table);
				DefaultTableModel model = new DefaultTableModel();
				model.addColumn("R_id");//1
				model.addColumn("Stu_ID");//2
				model.addColumn("Name");//3
				model.addColumn("Course");//4
				model.addColumn("Branch");//5
				model.addColumn("Sem");//6
				model.addColumn("B_id");//7
				model.addColumn("Title");//8
				model.addColumn("Author");//9
				model.addColumn("Lib_id");
				model.addColumn("DateOfIssue");//10
				model.addColumn("ReturnDate");//11
				
				insertRecordData(model);
				table.setModel(model);
				table.setAutoResizeMode(1);
				switchPanels(panel_3);
			}
		});
		btnNewButton_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_2_1_1.setBounds(178, 0, 90, 23);
		panel.add(btnNewButton_2_1_1);
		
		JButton btnNewButton_2_1_1_1 = new JButton("Books_Details");
		btnNewButton_2_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bsd.setText("Book Details");
				sideImg.setIcon(null);
    			sideImg.setText("                   null");
    			
    			
				table1 = new JTable();
				scrollPane_1.setViewportView(table1);
				DefaultTableModel model = new DefaultTableModel();
				model.addColumn("Book_id");//1
				model.addColumn("Book_Title");//8
				model.addColumn("Book_Author");//9
				model.addColumn("Price");//11
				model.addColumn("Pages");//11
				model.addColumn("status");//10
				
				insertBookData(model);
				table1.setModel(model);
				table1.setAutoResizeMode(1);
				switchPanels(panel_4);
				
				
			}
		});
		btnNewButton_2_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_2_1_1_1.setBounds(267, 0, 95, 23);
		panel.add(btnNewButton_2_1_1_1);
		
		JButton btnNewButton_2_2 = new JButton("Defaulters");
		btnNewButton_2_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bsd.setText("Current Defaulters");
				sideImg.setIcon(null);
    			sideImg.setText("                   null");
				
				table1 = new JTable();
				scrollPane_1.setViewportView(table1);
				DefaultTableModel model = new DefaultTableModel();
				model.addColumn("Roll_Number");//1
				model.addColumn("Issued_Book_id");
				model.addColumn("Name");//8
				model.addColumn("Last_Name");//9
				model.addColumn("Phone_Number");//11
				
				model.addColumn("Course");//10
				model.addColumn("Branch");
				model.addColumn("Semester");
				model.addColumn("BookCount");
				model.addColumn("Issue Date");
				
				insertDefaulterStudent(model);
				table1.setModel(model);
				table1.setAutoResizeMode(1);
				switchPanels(panel_4);
			}
		});
		btnNewButton_2_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_2_2.setBounds(461, 0, 90, 23);
		panel.add(btnNewButton_2_2);
		
		JButton btnNewButton_2_2_1 = new JButton("Student_Details");
		btnNewButton_2_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bsd.setText("Students Details");
				sideImg.setIcon(null);
    			sideImg.setText("                   null");
    			
    			
				table1 = new JTable();
				scrollPane_1.setViewportView(table1);
				DefaultTableModel model = new DefaultTableModel();
				model.addColumn("Roll_Number");//1
				model.addColumn("Name");//8
				model.addColumn("Last_Name");//9
				model.addColumn("Phone_Number");//11
				model.addColumn("Fathers_Name");//11
				model.addColumn("Course");//10
				model.addColumn("Branch");
				model.addColumn("Semester");
				model.addColumn("Book_issued");
				
				insertStudentData(model);
				table1.setModel(model);
				table1.setAutoResizeMode(1);
				switchPanels(panel_4);
			}
		});
		btnNewButton_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_2_2_1.setBounds(361, 0, 101, 23);
		panel.add(btnNewButton_2_2_1);
		
		
		
		
		JLabel stu_img_label = new JLabel("                  null");
		stu_img_label.setBorder(new LineBorder(new Color(0, 0, 0)));
		stu_img_label.setToolTipText("hello");
		stu_img_label.setBackground(SystemColor.controlShadow);
		stu_img_label.setBounds(324, 66, 137, 171);
		panel_12_2.add(stu_img_label);
		
		JLabel lblNewLabel_13_1_1_1_2_2_2_1_1 = new JLabel("Phone_No.");
		lblNewLabel_13_1_1_1_2_2_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_13_1_1_1_2_2_2_1_1.setBounds(71, 140, 90, 20);
		panel_12_2.add(lblNewLabel_13_1_1_1_2_2_2_1_1);
		
		
		
		JLabel lblNewLabel_13_5_6 = new JLabel(":");
		lblNewLabel_13_5_6.setBounds(171, 74, 10, 14);
		panel_12_2.add(lblNewLabel_13_5_6);
		
		JLabel bid = new JLabel("null");
		bid.setFont(new Font("Tahoma", Font.PLAIN, 13));
		bid.setBounds(165, 228, 142, 18);
		panel_12_1_1.add(bid);
		
		JLabel sid = new JLabel("null");
		sid.setFont(new Font("Tahoma", Font.PLAIN, 13));
		sid.setBounds(191, 56, 120, 20);
		panel_12_2.add(sid);
		
		
		JButton btnNewButton_2_1 = new JButton("Return_Book");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookid.setText("null");
				t_title_return.setText("null");
				t_author_return.setText("null");
				t_publisher_return.setText("null");
				t_price_return.setText("null");
				t_pages_return.setText("null");
				rollno.setText("null");
				t_name_return.setText("null");
				t_lname_return.setText("null");
				t_fname_return.setText("null");
				t_course_return.setText("null");
				t_branch_return.setText("null");
				t_sem_return.setText("null");
				t_ph.setText("null");
				img_book_return.setIcon(null);
    			img_book_return.setText("                  null");
    			img_stu_return.setIcon(null);
    			img_stu_return.setText("                  null");
				switchPanels(panel_2);
			}
		});
		btnNewButton_2_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_2_1.setBounds(89, 0, 90, 23);
		panel.add(btnNewButton_2_1);
		
		
		////////////////////////////Issue
		
		JButton btn_search_book = new JButton("Search");
		btn_search_book.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(t_bookid.getText().equals("")) {
					JOptionPane.showMessageDialog(frame,"please enter Book_id.");
					return;
				}
				
				
				book_m = DataBase.searchBook(t_bookid.getText());
				if((int)book_m.get("status")==1) {
					JOptionPane.showMessageDialog(frame,"This Book is already issued");
					return;
				}
				bid.setText(book_m.get("tid").toString());
				t_title.setText(book_m.get("title").toString());
	    		t_author.setText(book_m.get("author").toString());
	    		t_publisher.setText(book_m.get("publisher").toString());
	    		t_price.setText(book_m.get("price").toString());
	    		t_pages.setText(book_m.get("pages").toString());
	    		if( book_m.get("image")!=null) {
	    		  byte[] img=(byte[]) book_m.get("image");
	    		  ImageIcon image=new ImageIcon(img);
		    	  Image im=image.getImage();
		    	  Image myImg=im.getScaledInstance(book_img_label.getWidth(),book_img_label.getHeight(),Image.SCALE_SMOOTH);
		    	  ImageIcon newImage=new ImageIcon(myImg);
		    	  book_img_label.setIcon(newImage);
	    		}
	    		else {
	    			book_img_label.setIcon(null);
	    			book_img_label.setText("         Not Available");
	    		}
			}
		});
		btn_search_book.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_search_book.setBounds(300, 30, 89, 23);
		panel_12_1_1.add(btn_search_book);
		
		JLabel lblNewLabel_15 = new JLabel("Book_id           :");
		lblNewLabel_15.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_15.setBounds(57, 230, 99, 14);
		panel_12_1_1.add(lblNewLabel_15);
		
		
		
		JButton btn_search_stu = new JButton("Search");
		btn_search_stu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(t_stu_id.getText().equals("")) {
					JOptionPane.showMessageDialog(frame,"please enter Student_id.");
					return;
				}
				stu_m = DataBase.searchStudent(t_stu_id.getText());
				sid.setText(stu_m.get("sid").toString());
				t_name.setText(stu_m.get("tname").toString());
				t_lname.setText(stu_m.get("lname").toString());
        		t_fname.setText(stu_m.get("fname").toString());
        		t_ph.setText(stu_m.get("ph").toString());
        		t_course.setText(stu_m.get("course").toString());
        		t_branch.setText(stu_m.get("branch").toString());
        		t_sem.setText(stu_m.get("sem").toString());
        		if(stu_m.get("image")!=null) {
        		 byte[] img=(byte[]) stu_m.get("image");
   	    		 ImageIcon image=new ImageIcon(img);
   		    	 Image im=image.getImage();
   		    	 Image myImg=im.getScaledInstance(stu_img_label.getWidth(),book_img_label.getHeight(),Image.SCALE_SMOOTH);
   		    	 ImageIcon newImage=new ImageIcon(myImg);
   		    	 stu_img_label.setIcon(newImage);
        		}
        		else {
        			 stu_img_label.setIcon(null);
        			stu_img_label.setText("        Not Available");
        		}
			}
		});
		btn_search_stu.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_search_stu.setBounds(324, 26, 89, 23);
		panel_12_2.add(btn_search_stu);
		
		JLabel lblNewLabel_16 = new JLabel("roll number        :");
		lblNewLabel_16.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_16.setBounds(71, 56, 110, 14);
		panel_12_2.add(lblNewLabel_16);
		
		
		
		
		JButton btnNewButton_5 = new JButton("Issue Book");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(bid.getText().equals("null") ) {
					JOptionPane.showMessageDialog(frame,"Please provide book_id");
					return;
				}
				else if(sid.getText().equals("null")) {
			    	JOptionPane.showMessageDialog(frame,"Please provide Student_id");
					return;
				}
				else if(sid.getText().equals("Not Available")) {
			    	JOptionPane.showMessageDialog(frame,"This student_id not exist");
					return;
			    }
				else if(bid.getText().equals("Not Available") ) {
					JOptionPane.showMessageDialog(frame,"This Book_id not exist");
					return;
				}
				else if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
				else if((int)book_m.get("status")==1) {
					JOptionPane.showMessageDialog(frame,"This book was already issued to someone"); 
					return;
				}
				boolean status = DataBase.T_issueBook(bid.getText(),sid.getText(),libid.getText(),book_m,stu_m);
				if(status) {
					System.out.println("Book successfully issued");
					int temp=Integer.parseInt(totalIssuedBooks.getText());
					totalIssuedBooks.setText(""+(temp+1));
					
					book_m.put("status", 1);
					JOptionPane.showMessageDialog(frame,"Book successfully issued"); 
				}
				else {
					System.out.println("Not Inserted ");
				}
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_5.setBounds(994, 325, 89, 23);
		panel_1.add(btnNewButton_5);
		
		
		////////////////////return book
		JButton btn_sbook_return = new JButton("Search");
		btn_sbook_return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(t_bookid_return.getText().equals("")) {
					JOptionPane.showMessageDialog(frame,"please enter Book_id.");
					return;
				}
				 Map<String,Object> book_m = DataBase.searchBook(t_bookid_return.getText());		
				 //book_m = DataBase.searchBook(t_bookid.getText());
				  
				    bookid.setText(book_m.get("tid").toString());
					t_title_return.setText("<html>"+book_m.get("title").toString());
		    		t_author_return.setText(book_m.get("author").toString());
		    		t_publisher_return.setText(book_m.get("publisher").toString());
		    		t_price_return.setText(book_m.get("price").toString());
		    		t_pages_return.setText(book_m.get("pages").toString());
		    		if(book_m.get("image")!=null) {
		    		 byte[] img=(byte[]) book_m.get("image");
		    		 ImageIcon image=new ImageIcon(img);
			    	 Image im=image.getImage();
			    	 Image myImg=im.getScaledInstance(img_book_return.getWidth(),img_book_return.getHeight(),Image.SCALE_SMOOTH);
			    	 ImageIcon newImage=new ImageIcon(myImg);
			    	 img_book_return.setIcon(newImage);
		    		}
		    		else {
		    			img_book_return.setIcon(null);
		    			img_book_return.setText("      Not Avaiable");
		    		}
	        		
					int stu_id=DataBase.T_getStudentIdForThisBook(t_bookid_return.getText());
					System.out.println("today = "+stu_id);
					//t_stu_id.setText(Integer.toString(stu_id));
					
					
					
					//rollno.setText(String.valueOf(stu_id));
					stu_m = DataBase.searchStudent(Integer.toString(stu_id));
					rollno.setText(stu_m.get("sid").toString());
					t_name_return.setText(stu_m.get("tname").toString());
					t_lname_return.setText(stu_m.get("lname").toString());
	        		t_fname_return.setText(stu_m.get("fname").toString());
	        		//t_ph_return.setText(stu_m.get("ph").toString());
	        		t_course_return.setText(stu_m.get("course").toString());
	        		t_branch_return.setText(stu_m.get("branch").toString());
	        		t_sem_return.setText(stu_m.get("sem").toString());
	        		if(stu_m.get("image")!=null) {
	        		 byte[] img=(byte[]) stu_m.get("image");
	        		ImageIcon image=new ImageIcon(img);
	   	    		Image im=image.getImage();
	   		    	Image myImg=im.getScaledInstance(img_stu_return.getWidth(),img_stu_return.getHeight(),Image.SCALE_SMOOTH);
	   		    	ImageIcon newImage=new ImageIcon(myImg);
	   		    	img_stu_return.setIcon(newImage);
	        		}
	        		else {
	        			img_stu_return.setIcon(null);
	        			img_stu_return.setText("       Not Available");
	        		}
	        		
			}
		});
		btn_sbook_return.setBounds(353, 33, 89, 23);
		panel_2.add(btn_sbook_return);
		btn_sbook_return.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		
		
		JButton btn_return = new JButton("Return");
		btn_return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rollno.getText()=="Not Available") {
					JOptionPane.showMessageDialog(frame,"This Book was not issued to anyone");
					return;
				}
				else if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
				
				boolean status= DataBase.T_returnBook(t_bookid_return.getText());
				
				if(status) {
				   System.out.println("book Successfully returned");
				   int temp=Integer.parseInt(totalIssuedBooks.getText());
				   totalIssuedBooks.setText(""+(temp-1));
				   defaulterCount.setText(DataBase.defaultersLength());
				   JOptionPane.showMessageDialog(frame,"Successfully returned"); 
				}
				else {
					System.out.println("not returned");
					JOptionPane.showMessageDialog(frame,"not returned");
				}
			}
		});
		btn_return.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btn_return.setBounds(943, 33, 89, 23);
		panel_2.add(btn_return);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_7.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_7, "name_20759932049300");
		panel_7.setLayout(null);
		
		JButton btnNewButton_1_1_1 = new JButton("<html><center>Remove<br>Book</center></html>");
		btnNewButton_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchPanels(panel_7);
			}
		});
		btnNewButton_1_1_1.setMargin(new Insets(0,-30, 0,-30));
		btnNewButton_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_1_1_1.setBounds(23, 435, 60, 49);
		frame.getContentPane().add(btnNewButton_1_1_1);
		
		JLabel lblNewLabel_19 = new JLabel("");
		lblNewLabel_19.setBounds(246, 52, 49, 14);
		panel_7.add(lblNewLabel_19);
		
		JLabel lblNewLabel_9_2_2 = new JLabel("Book Id :");
		lblNewLabel_9_2_2.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2.setBounds(256, 52, 95, 26);
		panel_7.add(lblNewLabel_9_2_2);
		
		JTextArea rtb_t_bookid = new JTextArea();
		rtb_t_bookid.setFont(new Font("Monospaced", Font.PLAIN, 15));
		rtb_t_bookid.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		rtb_t_bookid.setBounds(338, 52, 190, 23);
		panel_7.add(rtb_t_bookid);
		
		
		JLabel lblNewLabel_13_1_1_1_3_1 = new JLabel("Book_Titile  :");
		lblNewLabel_13_1_1_1_3_1.setBounds(204, 148, 80, 20);
		panel_7.add(lblNewLabel_13_1_1_1_3_1);
		lblNewLabel_13_1_1_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_13_1_1_1_1_1_1 = new JLabel("Author  :");
		lblNewLabel_13_1_1_1_1_1_1.setBounds(204, 187, 80, 20);
		panel_7.add(lblNewLabel_13_1_1_1_1_1_1);
		lblNewLabel_13_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_13_1_1_3_1 = new JLabel("Published By  :");
		lblNewLabel_13_1_1_3_1.setBounds(204, 222, 91, 20);
		panel_7.add(lblNewLabel_13_1_1_3_1);
		lblNewLabel_13_1_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_13_1_1_2_2_1 = new JLabel("Price  :");
		lblNewLabel_13_1_1_2_2_1.setBounds(204, 260, 80, 20);
		panel_7.add(lblNewLabel_13_1_1_2_2_1);
		lblNewLabel_13_1_1_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_13_1_1_2_1_1_1 = new JLabel("Total_Pages  :");
		lblNewLabel_13_1_1_2_1_1_1.setBounds(204, 293, 91, 20);
		panel_7.add(lblNewLabel_13_1_1_2_1_1_1);
		lblNewLabel_13_1_1_2_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_15_1 = new JLabel("Book_id  :");
		lblNewLabel_15_1.setBounds(204, 115, 99, 14);
		panel_7.add(lblNewLabel_15_1);
		lblNewLabel_15_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel rtb_img = new JLabel("                  null");
		rtb_img.setBorder(new LineBorder(new Color(0, 0, 0)));
		rtb_img.setBackground(SystemColor.controlShadow);
		rtb_img.setBounds(566, 113, 137, 171);
		panel_7.add(rtb_img);
		
		JLabel rtb_bookid = new JLabel("null");
		rtb_bookid.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rtb_bookid.setBounds(315, 116, 184, 14);
		panel_7.add(rtb_bookid);
		
		JLabel rtb_title = new JLabel("null");
		rtb_title.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rtb_title.setBounds(315, 152, 171, 14);
		panel_7.add(rtb_title);
		
		JLabel rtb_author = new JLabel("null");
		rtb_author.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rtb_author.setBounds(315, 191, 99, 14);
		panel_7.add(rtb_author);
		
		JLabel rtb_publisher = new JLabel("null");
		rtb_publisher.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rtb_publisher.setBounds(315, 225, 99, 14);
		panel_7.add(rtb_publisher);
		
		JLabel rtb_price = new JLabel("null");
		rtb_price.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rtb_price.setBounds(315, 264, 99, 14);
		panel_7.add(rtb_price);
		
		JLabel rtb_pages = new JLabel("null");
		rtb_pages.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rtb_pages.setBounds(315, 297, 99, 14);
		panel_7.add(rtb_pages);
		
		
		JButton btn_search_book_1 = new JButton("Search");
		btn_search_book_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rtb_t_bookid.getText().equals("")) {
					JOptionPane.showMessageDialog(frame,"please enter Book_id.");
					return;
				}
				
				
				book_m = DataBase.searchBook(rtb_t_bookid.getText());
				if((int)book_m.get("status")==1) {
					JOptionPane.showMessageDialog(frame,"Cannot remove book which is issued to some one ");
					return;
				}
				rtb_bookid.setText(book_m.get("tid").toString());
				rtb_title.setText(book_m.get("title").toString());
	    		rtb_author.setText(book_m.get("author").toString());
	    		rtb_publisher.setText(book_m.get("publisher").toString());
	    		rtb_price.setText(book_m.get("price").toString());
	    		rtb_pages.setText(book_m.get("pages").toString());
	    		if( book_m.get("image")!=null) {
	    		  byte[] img=(byte[]) book_m.get("image");
	    		  ImageIcon image=new ImageIcon(img);
		    	  Image im=image.getImage();
		    	  Image myImg=im.getScaledInstance(rtb_img.getWidth(),rtb_img.getHeight(),Image.SCALE_SMOOTH);
		    	  ImageIcon newImage=new ImageIcon(myImg);
		    	  rtb_img.setIcon(newImage);
	    		}
	    		else {
	    			rtb_img.setIcon(null);
	    			rtb_img.setText("         Not Available");
	    		}
			}
		});
		btn_search_book_1.setBounds(538, 52, 89, 23);
		panel_7.add(btn_search_book_1);
		btn_search_book_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		
		JLabel lblNewLabel_7_1_1_1_1 = new JLabel("Remove This Book");
		lblNewLabel_7_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_7_1_1_1_1.setBounds(23, 11, 147, 26);
		panel_7.add(lblNewLabel_7_1_1_1_1);
		
		
		JButton btnNewButton_1 = new JButton("Remove");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
				else if(rtb_bookid.getText().equals("null")) {
					JOptionPane.showMessageDialog(frame,"Please enter book Id");
				}
				else if(rtb_bookid.getText().equals("Not Available")) {
					JOptionPane.showMessageDialog(frame,"This bookid not exist");
				}
				else if((int)book_m.get("status")==1) {
					JOptionPane.showMessageDialog(frame,"Cannot remove this book which is already issued");
				}
				else {
					int id=Integer.parseInt(rtb_bookid.getText());
					int t=DataBase.deleteThishBook(id);
					if(t==1) {
						bookLength.setText(DataBase.booksLength());
						JOptionPane.showMessageDialog(frame,"Book Successfully deleted");
					}
					else {
						JOptionPane.showMessageDialog(frame,"Unsuccessful Delete");
					}
				}
			}
		});
		btnNewButton_1.setBounds(974, 326, 108, 23);
		panel_7.add(btnNewButton_1);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_9.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_9, "name_1751682274000");
		panel_9.setLayout(null);
		
		JButton btnNewButton_1_1_1_1 = new JButton("<html><center>Delete<br>Student</center></html>");
		btnNewButton_1_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchPanels(panel_9);
			}
		});
		btnNewButton_1_1_1_1.setMargin(new Insets(0,-30, 0,-30));
		btnNewButton_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_1_1_1_1.setBounds(23, 518, 60, 49);
		frame.getContentPane().add(btnNewButton_1_1_1_1);
		
		JTextArea ds_t_roll = new JTextArea();
		ds_t_roll.setFont(new Font("Monospaced", Font.PLAIN, 15));
		ds_t_roll.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ds_t_roll.setBounds(411, 47, 190, 23);
		panel_9.add(ds_t_roll);
		
		JLabel lblNewLabel_7_1_1_1_1_2 = new JLabel("Delete Student");
		lblNewLabel_7_1_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_7_1_1_1_1_2.setBounds(44, 15, 147, 26);
		panel_9.add(lblNewLabel_7_1_1_1_1_2);
		
		JLabel lblNewLabel_9_2_2_1 = new JLabel("Roll No.   :");
		lblNewLabel_9_2_2_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1.setBounds(302, 47, 95, 26);
		panel_9.add(lblNewLabel_9_2_2_1);
		
		JLabel ds_img = new JLabel("                  null");
		ds_img.setBorder(new LineBorder(new Color(0, 0, 0)));
		ds_img.setBackground(SystemColor.controlShadow);
		ds_img.setBounds(668, 99, 137, 171);
		panel_9.add(ds_img);
		
		JLabel lblNewLabel_9_2_2_1_1 = new JLabel("Roll No.   :");
		lblNewLabel_9_2_2_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1.setBounds(257, 110, 117, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1);
		
		JLabel lblNewLabel_9_2_2_1_1_1 = new JLabel("Name  :");
		lblNewLabel_9_2_2_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1_1.setBounds(257, 139, 117, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1_1);
		
		JLabel lblNewLabel_9_2_2_1_1_1_1 = new JLabel("Last Name  :");
		lblNewLabel_9_2_2_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1_1_1.setBounds(257, 169, 117, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1_1_1);
		
		JLabel lblNewLabel_9_2_2_1_1_1_1_1 = new JLabel("Father's Name  :");
		lblNewLabel_9_2_2_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1_1_1_1.setBounds(257, 206, 127, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1_1_1_1);
		
		JLabel lblNewLabel_9_2_2_1_1_1_1_1_1 = new JLabel("Course  :");
		lblNewLabel_9_2_2_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1_1_1_1_1.setBounds(257, 243, 127, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1_1_1_1_1);
		
		JLabel lblNewLabel_9_2_2_1_1_1_1_1_1_1 = new JLabel("Brach  :");
		lblNewLabel_9_2_2_1_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1_1_1_1_1_1.setBounds(257, 272, 127, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1_1_1_1_1_1);
		
		JLabel lblNewLabel_9_2_2_1_1_1_1_1_1_1_1 = new JLabel("Semester  :");
		lblNewLabel_9_2_2_1_1_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_9_2_2_1_1_1_1_1_1_1_1.setBounds(257, 309, 127, 26);
		panel_9.add(lblNewLabel_9_2_2_1_1_1_1_1_1_1_1);
		
		JLabel ds_roll = new JLabel("null");
		ds_roll.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_roll.setBounds(411, 110, 220, 26);
		panel_9.add(ds_roll);
		
		JLabel ds_name = new JLabel("null");
		ds_name.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_name.setBounds(411, 139, 220, 26);
		panel_9.add(ds_name);
		
		JLabel ds_lname = new JLabel("null");
		ds_lname.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_lname.setBounds(411, 169, 220, 26);
		panel_9.add(ds_lname);
		
		JLabel ds_fname = new JLabel("null");
		ds_fname.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_fname.setBounds(411, 206, 220, 26);
		panel_9.add(ds_fname);
		
		JLabel ds_course = new JLabel("null");
		ds_course.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_course.setBounds(411, 243, 220, 26);
		panel_9.add(ds_course);
		
		JLabel ds_branch = new JLabel("null");
		ds_branch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_branch.setBounds(411, 272, 220, 26);
		panel_9.add(ds_branch);
		
		JLabel ds_sem = new JLabel("null");
		ds_sem.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ds_sem.setBounds(411, 309, 220, 26);
		panel_9.add(ds_sem);
		
		JButton btnNewButton_8 = new JButton("Remove");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
			    else if(ds_roll.getText().equals("null")) {
					JOptionPane.showMessageDialog(frame,"please enter student roll number");
				}
				else if(ds_roll.getText().equals("not Available")) {
					JOptionPane.showMessageDialog(frame,"this roll number not exist");
				}
				else if(!stu_m.get("booksCount").equals(0)) {
					JOptionPane.showMessageDialog(frame,"Cannot remove this student(book count="+stu_m.get("booksCount")+")");
				}
				else {
					int id=Integer.parseInt(ds_roll.getText());
					if(DataBase.deleteThisStudent(id)==1) {
						totalStu.setText(DataBase.stuLength());
						JOptionPane.showMessageDialog(frame,"Student with Student_id = "+id+" is successfully deleted");
					}
					else {
						JOptionPane.showMessageDialog(frame,"Delete Unsuccessfull");
					}
				}
				
			}
		});
		btnNewButton_8.setBounds(998, 326, 89, 23);
		panel_9.add(btnNewButton_8);
		
		JButton btn_search_book_1_1 = new JButton("Search");
		btn_search_book_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ds_t_roll.getText().equals("")) {
					JOptionPane.showMessageDialog(frame,"please enter Student_id.");
					return;
				}
				stu_m = DataBase.searchStudent(ds_t_roll.getText());
				ds_roll.setText(stu_m.get("sid").toString());
				ds_name.setText(stu_m.get("tname").toString());
				ds_lname.setText(stu_m.get("lname").toString());
        		ds_fname.setText(stu_m.get("fname").toString());
        		//ds_ph.setText(stu_m.get("ph").toString());
        		ds_course.setText(stu_m.get("course").toString());
        		ds_branch.setText(stu_m.get("branch").toString());
        		ds_sem.setText(stu_m.get("sem").toString());
        		if(stu_m.get("image")!=null) {
        		 byte[] img=(byte[]) stu_m.get("image");
   	    		 ImageIcon image=new ImageIcon(img);
   		    	 Image im=image.getImage();
   		    	 Image myImg=im.getScaledInstance(ds_img.getWidth(),ds_img.getHeight(),Image.SCALE_SMOOTH);
   		    	 ImageIcon newImage=new ImageIcon(myImg);
   		    	 ds_img.setIcon(newImage);
        		}
        		else {
        			 ds_img.setIcon(null);
        			ds_img.setText("        Not Available");
        		}
			}
		});
		btn_search_book_1_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_search_book_1_1.setBounds(622, 51, 89, 23);
		panel_9.add(btn_search_book_1_1);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_10.setBackground(SystemColor.controlHighlight);
		layeredPane.add(panel_10, "name_1805154874700");
		panel_10.setLayout(null);
		
		JLabel ana_img = new JLabel("                  null");
		ana_img.setBorder(new LineBorder(new Color(0, 0, 0)));
		ana_img.setBackground(SystemColor.controlShadow);
		ana_img.setBounds(737, 103, 137, 171);
		panel_10.add(ana_img);
		
		JLabel lblNewLabel_10 = new JLabel("Librarien id  :");
		lblNewLabel_10.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_10.setBounds(314, 87, 94, 27);
		panel_10.add(lblNewLabel_10);
		
		ana_id = new JTextArea();
		ana_id.setFont(new Font("Monospaced", Font.PLAIN, 15));
		ana_id.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ana_id.setBounds(450, 90, 190, 23);
		panel_10.add(ana_id);
		
		JLabel lblNewLabel_10_1 = new JLabel("Name  :");
		lblNewLabel_10_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_10_1.setBounds(314, 135, 94, 27);
		panel_10.add(lblNewLabel_10_1);
		
		JTextArea ana_ph = new JTextArea();
		ana_ph.setFont(new Font("Monospaced", Font.PLAIN, 15));
		ana_ph.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ana_ph.setBounds(450, 215, 190, 23);
		panel_10.add(ana_ph);
		
		JLabel lblNewLabel_10_1_1 = new JLabel("Last Name  :");
		lblNewLabel_10_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_10_1_1.setBounds(314, 173, 94, 27);
		panel_10.add(lblNewLabel_10_1_1);
		
		JLabel lblNewLabel_10_1_1_1 = new JLabel("Password  :");
		lblNewLabel_10_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_10_1_1_1.setBounds(314, 260, 94, 27);
		panel_10.add(lblNewLabel_10_1_1_1);
		
		JLabel lblNewLabel_10_1_1_1_1 = new JLabel("Confirm Password  :");
		lblNewLabel_10_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_10_1_1_1_1.setBounds(314, 298, 127, 27);
		panel_10.add(lblNewLabel_10_1_1_1_1);
		
		JLabel lblNewLabel_10_1_1_2 = new JLabel("Phone No.   :");
		lblNewLabel_10_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_10_1_1_2.setBounds(314, 215, 94, 27);
		panel_10.add(lblNewLabel_10_1_1_2);
		
		JTextArea ana_lname = new JTextArea();
		ana_lname.setFont(new Font("Monospaced", Font.PLAIN, 15));
		ana_lname.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ana_lname.setBounds(450, 176, 190, 23);
		panel_10.add(ana_lname);
		
		JTextArea ana_name = new JTextArea();
		ana_name.setFont(new Font("Monospaced", Font.PLAIN, 15));
		ana_name.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ana_name.setBounds(450, 135, 190, 23);
		panel_10.add(ana_name);
		
		JButton btnNewButton_6 = new JButton("Attach Photo");
		btnNewButton_6.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile=new JFileChooser();
				jfile.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter=new FileNameExtensionFilter("*.Image","jpg"); 
				jfile.addChoosableFileFilter(filter);
				
				int result=jfile.showSaveDialog(null);
				//System.out.print(result);
				File selectedFile=jfile.getSelectedFile();
				String filename=selectedFile.getName();
				System.out.println(filename);
				if(filename.endsWith(".jpg") || filename.endsWith(".png")) {
					if(result == JFileChooser.APPROVE_OPTION){
						path=selectedFile.getAbsolutePath();
						ImageIcon myImage=new ImageIcon(path);
						
						Image img=myImage.getImage();
						Image newImage=img.getScaledInstance(ana_img.getWidth(), ana_img.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon image=new ImageIcon(newImage);
						ana_img.setIcon(image);
					}
				}
				else {
					System.out.println("please select img");
				}
			}
		});
		btnNewButton_6.setBounds(755, 285, 108, 23);
		panel_10.add(btnNewButton_6);
		
		JLabel lblNewLabel_7_1_1_1_1_1 = new JLabel("Add New Account");
		lblNewLabel_7_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_7_1_1_1_1_1.setBounds(25, 21, 147, 26);
		panel_10.add(lblNewLabel_7_1_1_1_1_1);
		
		ana_pass = new JPasswordField();
		ana_pass.setBounds(450, 257, 190, 23);
		ana_pass.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_10.add(ana_pass);
		
		ana_cpass = new JPasswordField();
		ana_cpass.setBounds(450, 303, 190, 23);
		ana_cpass.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_10.add(ana_cpass);
		
		JButton btnNewButton_4 = new JButton("Add");
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataBase.dbConnected==false) {
					JOptionPane.showMessageDialog(frame,"Writting operations are off.");
					return;
				}
				String id=ana_id.getText();
				if(id.equals("")) {
					
					return;
				}
				String name=ana_name.getText();
				String lname=ana_lname.getText();
				String ph=ana_ph.getText();
				String pass=ana_pass.getText();
				String cpass=ana_cpass.getText();
				if(!pass.equals(cpass)) {
					
					return;
				}
				
				int t=DataBase.insertLib(id,name,lname,ph,pass,path);
				if(t==1)
					JOptionPane.showMessageDialog(frame,"successfully inserted");
				else
					JOptionPane.showMessageDialog(frame,"Something Went Wrong");
			}
		});
		btnNewButton_4.setBounds(1013, 326, 89, 23);
		panel_10.add(btnNewButton_4);
		
		JButton btnNewButton_1_1_2 = new JButton("<html><center>Add<br>Account</center></html>\r\n");
		btnNewButton_1_1_2.setMargin(new Insets(0,-30, 0,-30));
		btnNewButton_1_1_2.setHorizontalTextPosition(SwingConstants.CENTER);
		btnNewButton_1_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchPanels(panel_10);
			}
		});
		btnNewButton_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton_1_1_2.setBounds(23, 591, 60, 49);
		frame.getContentPane().add(btnNewButton_1_1_2);
		
	}
	
	
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1297, 726);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
