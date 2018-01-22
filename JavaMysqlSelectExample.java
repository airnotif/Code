import java.sql.*;

/**
 * A Java MySQL SELECT statement example.
 * Demonstrates the use of a SQL SELECT statement against a
 * MySQL database, called from a Java program.
 * 
 * Created by Alvin Alexander, http://alvinalexander.com
 */
public class JavaMysqlSelectExample
{

  public static void main(String[] args)
  {
   

    try 
    {
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection("jdbc:mysql://192.168.217.236:3306/test?useSSL=false","cedric","CEDRIC03.");
        System.out.println("Database connected!");

        // our SQL SELECT query. 
        // if you only need a few columns, specify them by name instead of using "*"
        String query = "SELECT * FROM system";

        // create the java statement
        Statement st = con.createStatement();
        
        // execute the query, and get a java resultset
        ResultSet rs = st.executeQuery(query);
        
        // iterate through the java resultset
        while (rs.next())
        {
          int id = rs.getInt("id_personne");
          String firstName = rs.getString("first_name");
          String lastName = rs.getString("last_name");
          
          // print the results
          System.out.format("%s, %s, %s\n", id, firstName, lastName);
        }
        
        String query2 = "INSERT INTO personne (first_name,last_name) VALUES('Michel','Cai')";
        int rs2 = st.executeUpdate(query2);
        st.close();


    }
    catch (Exception e)
    {
        throw new IllegalStateException("Cannot connect the database!", e);
    }
  }
}