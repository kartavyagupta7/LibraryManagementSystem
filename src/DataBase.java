import java.io.FileInputStream;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.JTextField;
/**
 * @author bhanu the murga man
 *
 */
public class DataBase {
	static Connection con=null;
	static PreparedStatement ps;
	static boolean dbConnected;
	
	
	static boolean connect(){
		try
		  {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagementsystem?serverTimezone=UTC","root","");
            offDB();
		    System.out.println("connection done");
		    return true;
		  }
		  catch(Exception e)
		  {
		      System.out.println(e);      
		  }
		  return false;
	}
	
	static void onDB() {
		dbConnected=true;
	}
	static void offDB() {
		dbConnected=false;
	}


	static int insertBook(String title,String author,String publisher,String price, String pages,String path) {
		int t=0;
		
		try {
			FileInputStream fis = new FileInputStream(path);
			
			//DataBase db=new DataBase();
			ps=con.prepareStatement("insert into books values(NULL,?, ?, ?, ?, ?, '0',?);");
			
			ps.setString(1, title);
			ps.setString(2, author);
			ps.setString(3, publisher);
			ps.setString(4, price);
			ps.setString(5,pages);
			ps.setBinaryStream(6,fis);
			t=ps.executeUpdate();
		}
		catch(SQLException e){
			System.out.print("sql exception");
		   //return "FAIL";
		}
		catch(Exception ex) {
			 System.out.print("file exception");
		}
		if(t==0)
			 return -1;
		try {
		 Statement st = con.createStatement();
		 String query = ("SELECT * from books ORDER BY Book_id DESC LIMIT 1;");
		 ResultSet rs = st.executeQuery(query);
		 
         if (rs.next()) 
            return rs.getInt("Book_id");
           
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	static boolean insertStudent(String rn,String name,String lname,String ph,String path,String father_name,String course,String branch,int sem){
		int t=0,roll_no=Integer.parseInt(rn);
		
		try {
		   //DataBase db = new DataBase();
		   FileInputStream fis = new FileInputStream(path);
		   ps=con.prepareStatement("INSERT INTO `students` VALUES (?,?,?,?,?,?,?,?,?,0);");
		   ps.setInt(1,roll_no);
		   ps.setString(2,name);
		   ps.setString(3,lname);
		   ps.setString(4,ph);
		   ps.setBinaryStream(5,fis);
		   ps.setString(6,father_name);
		   ps.setString(7,course);
		   ps.setString(8,branch);
		   ps.setInt(9,sem);
		   t=ps.executeUpdate();
		}
		catch(SQLException e){
			 e.printStackTrace();
		}
		catch(Exception ex) {
			 System.out.print("file exception");
		}
		
		
		if(t==1)
			 return true;
		else
			return false;	
	}
	
	static Map<String,Object> searchBook(String str) {
		
		int book_id = Integer.parseInt(str);
		Map<String,Object> m = new HashMap<>();
		try {
			//DataBase db = new DataBase();
		    Statement st=con.createStatement();
	        ResultSet rs=st.executeQuery("select *  from books where Book_id = "+Integer.parseInt(str));
	        if(rs.next()) {	
	        	    m.put("tid", str);
	        		m.put("title",rs.getString(2));
	        		m.put("author",rs.getString(3));
	        		m.put("publisher",rs.getString(4));
	        		m.put("price",rs.getString(5));
	        		m.put("pages",rs.getInt(6));		
	        		m.put("image",rs.getBytes("Book_image")); 
	        		m.put("status", rs.getInt(7));
	        }
	        else {
	        	m.put("tid","Not Available");
	        	m.put("title","Not Available");
	    		m.put("author","Not Available");
	    		m.put("publisher","Not Available");
	    		m.put("price","Not Available");
	    		m.put("pages","Not Available");
	    		m.put("image",null);
	    		m.put("status",0);
	        }
		}
		catch(SQLException e){
			 e.printStackTrace();
		}
		return m;
	}

	static Map<String,Object> searchStudent(String str) {
		int stu_id = Integer.parseInt(str);
		Map<String,Object> m = new HashMap<>();	
		try {
			//DataBase db = new DataBase();
		    Statement st=con.createStatement();
	        ResultSet rs=st.executeQuery("select *  from students where Roll_no ="+Integer.parseInt(str));
	        if(rs.next()) {
	        	    m.put("sid",str);
	        		m.put("tname",rs.getString(2));
	        		m.put("lname",rs.getString(3));
	        		m.put("ph",rs.getString(4));
	        		m.put("image",rs.getBytes(5));
	        		m.put("fname", rs.getString(6));
	        		m.put("course",rs.getString(7));
	        		m.put("branch",rs.getString(8));
	        		m.put("sem",rs.getString(9));
	        		m.put("booksCount",rs.getInt(10));
	        }
	        else {
	        	m.put("sid","Not Available");
	        	m.put("tname","Not Available");
	        	m.put("lname","Not Available");
	        	m.put("ph","Not Available");
	        	m.put("image",null);
	    		m.put("fname","Not Available");
	    		m.put("course","Not Available");
	    		m.put("branch","Not Available");
	    		m.put("sem","Not Available");
	    		m.put("booksCount","Not Available");
	        }
	        
		}
		catch(SQLException e){
			 e.printStackTrace();
		}
		return m;
	}
     	
	
	static boolean T_issueBook(String str1,String str2,String str3,Map<String,Object> book_m,Map<String,Object> stu_m ) {
		int t=0;
		int t_bookid = Integer.parseInt(str1);
		int t_stu_id = Integer.parseInt(str2);
		int issue_id=-1;
		Date date = new Date();
		java.sql.Date sqldate = new java.sql.Date(date.getTime()); 
		
		try {
			//DataBase db=new DataBase();
		    ps=con.prepareStatement("UPDATE books SET status="+ 1 +" WHERE Book_id = "+ t_bookid);
		    ps.executeUpdate();
		    ps=con.prepareStatement("UPDATE students SET BooksCount = BooksCount +"+ 1 +" WHERE Roll_no = "+ t_stu_id);
			t= t*ps.executeUpdate();
			
			ps=con.prepareStatement("insert into records VALUES (NULL,?,?,?,?,?,?,?,?,?,NULL,?);");
			ps.setInt(1,t_stu_id);
			ps.setString(2,stu_m.get("tname").toString());
			ps.setString(3,stu_m.get("course").toString());
			ps.setString(4,stu_m.get("branch").toString());
			ps.setInt(5,Integer.parseInt(stu_m.get("sem").toString()));
			System.out.println(stu_m.get("sem"));
			ps.setInt(6,t_bookid);
			ps.setString(7,book_m.get("title").toString());
			ps.setString(8,book_m.get("author").toString());
			
			ps.setDate(9,sqldate);
			ps.setString(10,str3);
			t= ps.executeUpdate();
			if(t==1)
				return true;
			else {
				System.out.println("Record not inserted");
			}
				
		}
		catch(SQLException e){
			   e.printStackTrace();
		}
		return false;
	   
	}
	
	
	//###//
	static boolean issueBook(String str1,String str2,Map<String,Object> book_m,Map<String,Object> stu_m ) {
		int t=0;
		int t_bookid = Integer.parseInt(str1);
		int t_stu_id = Integer.parseInt(str2);
		int issue_id=-1;
		try {
			//DataBase db=new DataBase();
			
			Date date = new Date();
			java.sql.Date sqldate = new java.sql.Date(date.getTime()); 
			
			ps=con.prepareStatement("INSERT INTO `issue_book` VALUES (NULL,?,?,?,null);",Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1,t_bookid);
			ps.setInt(2,t_stu_id);
			ps.setDate(3,sqldate);
			//ps.setDate(3,sqldate);
			t=ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				 issue_id = rs.getInt(1);
			}
			if(t==1) {
				ps=con.prepareStatement("UPDATE books SET status="+ 1 +" WHERE Book_id = "+ t_bookid);
				t= t*ps.executeUpdate();
				if(t==1) {
					ps=con.prepareStatement("UPDATE students SET BooksCount = BooksCount +"+ 1 +" WHERE Roll_no = "+ t_stu_id);
					t= t*ps.executeUpdate();
					if(t==1) {
						 if(insertRecord(issue_id,t_bookid,t_stu_id,book_m,stu_m,sqldate) == 0)
							  System.out.println("Errore: only record is not inserted");
						 	
						 return true;
					}
					else
						System.out.println("Errore: issued and status = 1 but Books count not incremented");
				}
				else {
					System.out.println("Errore: issued but status flag not becoming 1");
				}
				
			}
			else {
				System.out.println("Errore: didn't insert");
			}
			
		}
		catch(SQLException e){
		   e.printStackTrace();
		}
		return false;
	}
	
	
	static int T_getStudentIdForThisBook(String str) {
		try {
			//DataBase db = new DataBase();
		    Statement st=con.createStatement();
	        ResultSet rs=st.executeQuery("select Stu_id from records where Return_Date is null and Book_id ="+str);
	        if(rs.next()) {
	        	return rs.getInt("Stu_id");
	        }
		}
	    catch(SQLException e){
				 e.printStackTrace();
		}
		return -1;
	}
	
	//###//
	static int getStudentIdForThisBook(String str) {
		int book_id = Integer.parseInt(str);
		
		try {
			//DataBase db = new DataBase();
		    Statement st=con.createStatement();
	        ResultSet rs=st.executeQuery("select *  from issue_book ");
	        while(rs.next()) {
	        	if(rs.getInt(2) == book_id) {
	        		return rs.getInt(3);
	        	}
	        }
		}
		catch(SQLException e){
			 e.printStackTrace();
		}
		 return -1;
	}
	
	
	static boolean T_returnBook(String str1) {
		int stu_id=T_getStudentIdForThisBook(str1);
		if(stu_id==-1) {
			System.out.println("Book Already issued");
			return false;
		}
		int book_id = Integer.parseInt(str1);
		int issue_id=-1;
		int t=0;
		try {
			//DataBase db=new DataBase();
			
		    ps=con.prepareStatement("update books set status = 0 where Book_id = "+ book_id);
		    t= ps.executeUpdate();
		    if(t==0)
		    	System.out.println("status not set to zero");
		    
		    ps=con.prepareStatement("update students set BooksCount = BooksCount - "+ 1 +" where Roll_no = "+ stu_id);
			t= ps.executeUpdate();
			if(t==0)
		    	System.out.println("stu bookCount not reduced");
		    
			if(T_updateRecord(book_id) == 0)
				System.out.println("ERRORE : record not updated");
			return true;
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.out.print("exception");
		}
      return false;	
	}
	
	//###//
	static boolean returnBook(String str1,String str2) {
		
		int stu_id = Integer.parseInt(str2);
		int book_id = Integer.parseInt(str1);
		int issue_id=-1;
		int t=0;
		
		try {
			//DataBase db=new DataBase();
			Statement st=con.createStatement();
		     ResultSet rs=st.executeQuery("select *  from issue_book where Book_id = "+book_id);
		     if(rs.next()) {
		    		 issue_id = rs.getInt(1);  	 
		     }
			ps=con.prepareStatement("DELETE FROM `issue_book` WHERE Book_id = "+book_id);
			t= ps.executeUpdate();
			if(t==1) {
				ps=con.prepareStatement("update books set status = 0 where Book_id = "+ book_id);
				t= t*ps.executeUpdate();
				if(t==1) {
					ps=con.prepareStatement("update students set BooksCount = BooksCount - "+ 1 +" where Roll_no = "+ stu_id);
					t= t * ps.executeUpdate();
					if(t==1) {
						if(updateRecord(issue_id) == 0)
							System.out.println("ERRORE : record not updated");
						return true;
					}
					else {
						System.out.println("Errore : row deleted from issue table and status is 0 but count not decremented");
					}
					
				}else {
					System.out.println("Errore : row deleted from issue table but status not becoming 0");
				}
				
			}
			else {
				System.out.println("Errore : row not deleted from issue table");
			}
			
		}
		catch(SQLException e){
			   e.printStackTrace();
			   System.out.print("exception");
		}		
		return false;
	
	}
	
	//###//
	static int insertRecord(int record_id,int bookid,int stu_id,Map<String,Object> book_m,Map<String,Object> stu_m,java.sql.Date sqldate) {
		
		try {
			//DataBase db=new DataBase();
			ps=con.prepareStatement("insert into records VALUES (?,?,?,?,?,?,?,?,?,?, NULL);");
			
			ps.setInt(1,record_id);
			ps.setInt(2,stu_id);
			ps.setString(3,stu_m.get("tname").toString());
			ps.setString(4,stu_m.get("course").toString());
			ps.setString(5,stu_m.get("branch").toString());
			ps.setInt(6,Integer.parseInt(stu_m.get("sem").toString()));
			System.out.println(stu_m.get("sem"));
			ps.setInt(7,bookid);
			ps.setString(8,book_m.get("title").toString());
			ps.setString(9,book_m.get("author").toString());
			
			ps.setDate(10,sqldate);
			
			return ps.executeUpdate();
			
			//System.out.println("BOOK MAP"+ book_m);
            //System.out.println("STUDENT MAP" + stu_m);
		}
		catch(SQLException e){
			   e.printStackTrace();
		}
		return 0;
	}
	
	static int T_updateRecord(int book_id) {
		try {
			//DataBase db=new DataBase();
			Date date = new Date();
			java.sql.Date sqldate = new java.sql.Date(date.getTime());
			
			ps=con.prepareStatement("update records set Return_Date = ? where Return_Date is null and Book_id ="+ book_id);
			ps.setDate(1,sqldate);
			return ps.executeUpdate();
		}
		catch(SQLException e){
			   e.printStackTrace();
		}
		return 0;
	}
	
	//###//
	static int updateRecord(int issue_id) {
		try {
			//DataBase db=new DataBase();
			Date date = new Date();
			java.sql.Date sqldate = new java.sql.Date(date.getTime());
			ps=con.prepareStatement("update records set Return_Date = ? where Record_id = "+ issue_id);
			ps.setDate(1,sqldate);
			return ps.executeUpdate();
		}
		catch(SQLException e){
			   e.printStackTrace();
		}
		return 0;
	}
	
	
	static Map<String,Object> getLibrarien(String libid){
		Map<String,Object> m = new HashMap<>();	
		 
		 try{
				//DataBase db = new DataBase();
			    Statement st=con.createStatement();
		        ResultSet rs=st.executeQuery("SELECT * FROM `login` WHERE User_id = "+Integer.parseInt(libid));
		        if(rs.next()) {
		        	   m.put("uname",rs.getString(2));
		        	   m.put("image",rs.getBytes("image")); 
		        }
		        else {
		        	 m.put("uname","not availabe");
		        	   m.put("image",null);
		        }
		        return m;
			}
			catch(SQLException e){
				 e.printStackTrace();
			}
		 
		    System.out.println("not working");
			return null;
	}
	
	
	
	static String booksLength() {
		try{
			//DataBase db = new DataBase();
			ps=con.prepareStatement("SELECT COUNT(Book_id) from books");
	        ResultSet rs=ps.executeQuery();
	        if(rs.next()) {
	         System.out.println( rs.getString("COUNT(Book_id)"));
	         return rs.getString("COUNT(Book_id)");
	        }
		}
		catch(Exception e) {
			System.out.println("bookLength exception");
		}
		return "null";
	}
	
	
	static String stuLength() {
		try{
			//DataBase db = new DataBase();
			ps=con.prepareStatement("SELECT COUNT(Roll_no) from students");
	        ResultSet rs=ps.executeQuery();
	        if(rs.next()) {
	         System.out.println( rs.getString("COUNT(Roll_no)"));
	         return rs.getString("COUNT(Roll_no)");
	        }
		}
		catch(Exception e) {
			System.out.println("stuLength exception");
		}
		return "null";
	}
	
	static String issuedBooksLength() {
		try {
			ps=con.prepareStatement("Select count(Book_id) from records where Return_Date is null");
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				System.out.println( rs.getString("count(Book_id)"));
				return rs.getString("count(Book_id)");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return "null";
	}
	
	//###//
	static String issuedLength() {
		try{
			//DataBase db = new DataBase();
			ps=con.prepareStatement("SELECT COUNT(Issue_id) from issue_book");
	        ResultSet rs=ps.executeQuery();
	        if(rs.next()) {
	         System.out.println( rs.getString(1));
	         return rs.getString(1);
	        }
		}
		catch(Exception e) {
			System.out.println("stuLength exception");
		}
		return "null";
	}
	
	static String defaultersLength() {
		try{
			LocalDateTime now = LocalDateTime.now(); 
			String date=now.getYear()+""+now.getMonthValue()+""+now.getDayOfMonth();
			ps=con.prepareStatement("SELECT COUNT(Book_id) from records where Return_Date is null and "+date+" - 'Issue_Date'  > 2;");
	        ResultSet rs=ps.executeQuery();
	        if(rs.next()) {
	          System.out.println( rs.getString(1));
	          return rs.getString(1);
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "null";
	}
	
	static ResultSet booksTable() {
		try {
			//DataBase db=new DataBase();
			Statement st = con.createStatement();
			 String query = ("SELECT * from books");
			 ResultSet rs = st.executeQuery(query);
		     return rs;
		}
		catch(Exception e) {
			System.out.println("booksTableException");
			return null;
		}
	}
	
	
	static ResultSet studentTable() {
		try {
			// DataBase db=new DataBase();
			 Statement st = DataBase.con.createStatement();
			 String query = ("SELECT * from students");
			 ResultSet rs = st.executeQuery(query);
		     return rs;
		}
		catch(Exception e) {
			System.out.println("studentTableExceptionn");
			return null;
		}
	}

	static ResultSet defaulterTable() {
		try {
			LocalDateTime now = LocalDateTime.now(); 
			String date=now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth();
			ps=con.prepareStatement("SELECT * from records JOIN students  where Return_Date is null and Roll_no = Stu_id and DATE_ADD(Issue_Date, INTERVAL 2 DAY) - CURDATE() < 0 ;");
	        ResultSet rs=ps.executeQuery();
			System.out.println("good");
		    return rs;
		    	 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		 return null;
	}
	
	
	static int deleteThisStudent(int id) {
		try {
			//DataBase db=new DataBase();
			ps=con.prepareStatement("DELETE FROM students WHERE Roll_no=?");
			ps.setInt(1,id);
			return ps.executeUpdate();
		}
		catch(SQLException e){
		    e.printStackTrace();
			return 0;
		}
		
	}
	
	
	static int deleteThishBook(int id) {
		try {
			//DataBase db=new DataBase();
			ps=con.prepareStatement("DELETE FROM `books` WHERE Book_id =?");
			ps.setInt(1,id);
			return ps.executeUpdate();
		}
		catch(SQLException e){
		    e.printStackTrace();
			return 0;
		}
	}
	
	
    static Map<String,Object> checkLoginCredentials(String id,String pass) {
    	Map<String,Object> m=new HashMap<>();
    	try {
			// DataBase db=new DataBase();
			 Statement st = DataBase.con.createStatement();
			 String query = ("SELECT * from login where User_id="+Integer.parseInt(id)+" and Password= '"+pass+"';");
			 ResultSet rs = st.executeQuery(query);
			 if(rs.next()) {
				 m.put("name",rs.getString(2));
				 m.put("lname",rs.getString(3));
				 m.put("image",rs.getBytes(6));
			 }
			 else
				 System.out.println("495");
			 
		    
		}
		catch(Exception e) {
			System.out.println("checkLoginCredentials Exceptionn");
		
		}
    	 return m;
	}
    
    
	static int insertLib(String id,String name,String lname,String  ph,String pass,String path) {
		
		try {
			FileInputStream fis = new FileInputStream(path);
			
			//DataBase db=new DataBase();
			ps=con.prepareStatement("insert into login VALUES (?,?,?,?,?,?);");
			ps.setInt(1,Integer.parseInt(id));
			ps.setString(2,name);
			ps.setString(3,lname);
			ps.setString(4,pass);
			ps.setString(5,ph);
			ps.setBinaryStream(6,fis);	
			return ps.executeUpdate();
		}
		catch(SQLException e){
			   e.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	
	
	
}
