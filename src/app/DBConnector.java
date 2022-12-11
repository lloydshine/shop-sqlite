package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnector {
	private static Connection c = null;
	
	public static Connection connect() {
		String url = "jdbc:sqlite:shop.db";
		try {
			if (c != null) {
				c.close();
			}	
			c = (Connection) DriverManager.getConnection(url);
		} catch(SQLException e) {}
	    return c;
	}
	
	public static void createTable() {
		try {
			c = connect();
			String createTableSQL = "CREATE TABLE Items " +
					"(" +
					"id integer," +
					"itemname varchar(20)," +
					"price float" +
					");";
			Statement stat = c.createStatement();
			stat.execute(createTableSQL);
			stat.close();
			c.close();
		} catch (SQLException e) { }
	}
	
	public static void insertItem(int id, String name, float price) throws SQLException {
		c = null;
		c = connect();
		String insertSQL = "INSERT INTO Items(id, itemname, price) VALUES(?,?,?)";
		PreparedStatement pst = c.prepareStatement(insertSQL);
		pst.setInt(1, id);
		pst.setString(2, name);
		pst.setFloat(3, price);
		pst.executeUpdate();
		c.close();
		pst.close();
	}
	
	public static void removeItem(int id) {
    	try {
    		c = connect();
    		String q = "DELETE FROM Items WHERE (id = ?)";
    		PreparedStatement ps = c.prepareStatement(q);
    		ps.setInt(1, id);
    		ps.executeUpdate();
    		c.close();
    		ps.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
	
	public static void editItem(int id,int newid,String name,float price) {
    	try {
    		String q = "UPDATE Items SET id =?,itemname = ?,price = ? WHERE (id = ?);";
    		c = connect();
        	PreparedStatement ps = c.prepareStatement(q);
        	ps.setInt(1, newid);
   			ps.setString(2, name);
   			ps.setFloat(3, price);
   			ps.setInt(4, id);
   			ps.executeUpdate();
   			c.close();
   			ps.close();
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
        
    }
	
	public static ArrayList<Item> getItems() {
		ArrayList<Item> list = new ArrayList<>();
		try {
			c = connect();
        	PreparedStatement ps = c.prepareStatement("SELECT * from items");
        	ResultSet rs = ps.executeQuery();
        	while(rs.next()) {
        	    list.add(new Item(rs.getInt("id"),rs.getString("itemname"),rs.getFloat("price")));
        	}
        	c.close();
        	ps.close();
        	rs.close();
        } catch (SQLException e) { }
        
		return list;
	}
	
	public static boolean findDuplicate(int id) {
        try {
            c = connect();
            PreparedStatement ps = c.prepareStatement("SELECT id FROM Items WHERE id = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) { return true; }
            ps.close();
            resultSet.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
   }
	
	public static void deleteTable() throws SQLException {
		c = connect();
		String deleteSQL = "DROP TABLE Person";
		Statement stat = c.createStatement();
		stat.execute(deleteSQL);
		c.close();
		stat.close();
	}
}
