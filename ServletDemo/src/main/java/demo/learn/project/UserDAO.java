package demo.learn.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import demo.learn.project.utils.DBConnection;

public class UserDAO{
public void registerUser(String username, String email, String password) throws Exception {

	Connection con=DBConnection.getConnection();
	String sql="Insert into users2(username,email,password) values(?,?,?);";
	PreparedStatement ps=con.prepareStatement(sql);
	ps.setString(1,username);
	ps.setString(2,email);
	ps.setString(3,password);
	ps.executeUpdate();
	ps.close();
}
 
 
public User login(String username,String password)throws Exception {
	Connection con = null;
	try {
		con=DBConnection.getConnection();
		System.out.println("Connction success");
	}catch(Exception e) {
		System.out.println("Error "+e.getMessage());
	}
	String sql="select * from users2 where username=? AND password=?";
	PreparedStatement ps=con.prepareStatement(sql);
	ps.setString(1,username);
	ps.setString(2,password);
	ResultSet rs=ps.executeQuery();
	User user=new User();
	if(rs.next()) {
	user.setId(rs.getInt("id"));
	user.setUsername(rs.getString("username"));
	return user;
	}
	return null;
}



}
