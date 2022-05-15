package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Payment {
private Connection connect(){ 
		
		Connection con = null; 
		
		try{ 
				Class.forName("com.mysql.jdbc.Driver"); 

				//Provide the correct details: DBServer/DBName, username, password 
				//DB Connection
				con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/payment", "root", ""); 
		} 
		catch (Exception e) {
			e.printStackTrace();
			} 
		
		return con; 
} 

//Insert Function
public String insertPayment(String accno, String cusname, String paydate, String totamount){ 

	String output = ""; 
	
	try
	{ 
		Connection con = connect(); 
		
		if (con == null) 
		{
			return "Error while connecting to the database for inserting."; 
			
		} 
		// create a prepared statement
		
		String query = " insert into payments (`payID`,`accountNo`,`cusName`,`payDate`,`totAmount`)"+" values (?, ?, ?, ?, ?)"; 
		PreparedStatement preparedStmt = con.prepareStatement(query); 
		// binding values
		preparedStmt.setInt(1, 0); 
		preparedStmt.setString(2, accno); 
		preparedStmt.setString(3, cusname); 
		preparedStmt.setString(4, paydate); 
		preparedStmt.setString(5, totamount); 
		// execute the statement

		preparedStmt.execute(); 
		con.close(); 
		
		String newPayments = readPayments(); 
		output = "{\"status\":\"success\",\"data\":\""+newPayments+"\"}";
	} 
	
	catch (Exception e) 
	{ 
		output = "{\"status\":\"error\", \"data\":\"Error while inserting the Payment.\"}"; 
		System.err.println(e.getMessage()); 
	} 
	return output; 
} 


//Read Function
public String readPayments(){ 

	String output = ""; 
	
	try{ 
			Connection con = connect(); 
			if (con == null){
				
				return "Error while connecting to the database for reading."; 
				
				} 
			// Prepare the html table to be displayed
			output = "<table border=\"1\" class=\"table\"><tr><th>accountNo</th>" +
					"<th>cusName</th><th>payDate</th>" + 
					"<th>totAmount</th>" +
					"<th>Update</th><th>Remove</th></tr>"; 

			String query = "select * from payments"; 
			Statement stmt = con.createStatement(); 
			ResultSet rs = stmt.executeQuery(query); 
			// iterate through the rows in the result set
			while (rs.next()) 
			{ 
					String payID = Integer.toString(rs.getInt("payID")); 
					String accountNo = rs.getString("accountNo"); 
					String cusName = rs.getString("cusName"); 
					String payDate = rs.getString("payDate"); 
					String totAmount = rs.getString("totAmount"); 
					// Add into the html table
					output += "<tr><td><input id='hidPaymentIDUpdate' name='hidPaymentIDUpdate' type='hidden' value='"+payID+"'>"+accountNo+"</td>"; 
					output += "<td>" + cusName + "</td>"; 
					output += "<td>" + payDate + "</td>"; 
					output += "<td>" + totAmount + "</td>"; 
					// buttons
					 output += "<td><input name='btnUpdate' type='button' value='Update' "
							 + "class='btnUpdate btn btn-secondary' data-payid='" + payID + "'></td>"
							 + "<td><input name='btnRemove' type='button' value='Remove' "
							 + "class='btnRemove btn btn-danger' data-payid='" + payID + "'></td></tr>"; 
			} 
				con.close(); 
				// Complete the html table
				output += "</table>"; 
		} 
		catch (Exception e){ 
					output = "Error while reading the items."; 
					System.err.println(e.getMessage()); 
		} 
		return output; 
		} 


//Update Function
public String updatePayment(String ID, String accno, String cusname, String paydate, String totamount){ 

	String output = ""; 
	
	try{ 
			Connection con = connect(); 
			if (con == null){
				return "Error while connecting to the database for updating.";
				} 
			// create a prepared statement
			String query = "UPDATE payments SET accountNo=?,cusName=?,payDate=?,totAmount=? WHERE payID=?"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
			// binding values
			preparedStmt.setString(1, accno); 
			preparedStmt.setString(2, cusname); 
			preparedStmt.setString(3, paydate); 
			preparedStmt.setString(4, totamount); 
			preparedStmt.setInt(5, Integer.parseInt(ID)); 
			// execute the statement
			preparedStmt.execute(); 
			con.close(); 
			
			String newPayments = readPayments(); 
			output = "{\"status\":\"success\",\"data\":\""+newPayments+"\"}";
			
	} 
	
	catch (Exception e){ 
		
		output = "{\"status\":\"error\",\"data\":\"Error while updating the Payment.\"}"; 
		System.err.println(e.getMessage()); 
		
	} 
	
	return output; 
} 


//Delete Function
public String deletePayment(String payID){ 

	String output = ""; 
	
	try{ 
		Connection con = connect(); 
		
		if (con == null){
			return "Error while connecting to the database for deleting."; 
			} 
		// create a prepared statement
		String query = "delete from payments where payID=?"; 
		PreparedStatement preparedStmt = con.prepareStatement(query); 
		// binding values
		preparedStmt.setInt(1, Integer.parseInt(payID)); 
		// execute the statement
		preparedStmt.execute(); 
		con.close(); 
		
		String newPayments = readPayments(); 
		 output = "{\"status\":\"success\",\"data\":\""+newPayments+"\"}"; 
	} 
	
	catch (Exception e){ 
		output = "{\"status\":\"error\",\"data\":\"Error while deleting the Payment.\"}";
		System.err.println(e.getMessage()); 
	} 
	return output; 
} 


}
