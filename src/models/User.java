package models;

import javax.sql.rowset.serial.SerialBlob;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    private String name;
    private String phoneNumber;
    private int userID;
    private String password;
    private byte[] salt;
    private boolean admin;

    public User(String name, String phoneNumber, String password, boolean admin) throws NoSuchAlgorithmException {
        setName(name);
        setPhoneNumber(phoneNumber);
        salt = PasswordGenerator.getSalt();
        this.password = PasswordGenerator.getSHA512Password(password, salt);
        setAdmin(admin);

    }

    /**
     * gets the admin
     * @return
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * sets the admin
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * gets the user id
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * sets the users id
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * gets the name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the phone number
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * sets the phone number
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * insert the instance varibables of this class into the database
     * @throws SQLException
     */
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie", "root", "root");

            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO users (firstName, phoneNumber, password, salt, admin)"
                    + "VALUES (?, ?, ?, ?, ?)";

            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);


            //5. Bind the values to the parameters
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, password);
            preparedStatement.setBlob(4, new javax.sql.rowset.serial.SerialBlob(salt));
            preparedStatement.setBoolean(5, admin);


            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();

            if (conn != null)
                conn.close();
        }

    }
    /**
     * This will update the users in the database
     */
    public void updateUserInDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie", "root", "root");

            //2.  create a String that holds our SQL update command with ? for user inputs
            String sql = "UPDATE users SET firstName = ?, phoneNumber=?, password = ?, salt = ?"
                    + "WHERE userID = ?";

            //3. prepare the query against SQL injection
            preparedStatement = conn.prepareStatement(sql);

            //5. bind the parameters
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, password);
            preparedStatement.setBlob(4, new javax.sql.rowset.serial.SerialBlob(salt));
            preparedStatement.setInt(5, userID);

            //6. run the command on the SQL server
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }

    }

    /**
     * returns a formatted string of this class
     * @return
     */
    public String toString()
    {
        return String.format("the users name is %s and phone number is %s",
                getName(),getPhoneNumber());
    }
}
