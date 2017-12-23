package views;

import com.sun.org.apache.regexp.internal.RE;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.PasswordGenerator;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML private PasswordField passwordTextField;

    @FXML private Label errrorLabel;

    @FXML private TextField nameTextField;

    /**
     * when the login button is pushed validate that they are a user
     * @param event
     * @throws SQLException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void loginButtonPushed(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {
        //query the database for the input in the text field
        //get the salt and encryped psw in the db

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int userNum = Integer.parseInt(nameTextField.getText());
        try
        {
            //connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie", "root", "root");

            //create the sql query
            String sql = "SELECT * FROM users WHERE userID = ?";

            //prepare the statement
            preparedStatement = conn.prepareStatement(sql);

            //bind the user id to the question
            preparedStatement.setInt(1, userNum);

            //execute the query
            rs = preparedStatement.executeQuery();

            String dbPassword = "";
            byte[] salt = null;
            boolean admin = false;
            User user = null;
            //extract the password and salt for the result set
            while (rs.next())
            {
                dbPassword = rs.getString("password");
                Blob blob = rs.getBlob("salt");

                //convert blob into a byte
                int blobLength = (int) blob.length();
                salt = blob.getBytes(1, blobLength);
                admin = rs.getBoolean("admin");
                user = new User(rs.getString("firstName"), rs.getString("phoneNumber"), rs.getString("password"),
                        rs.getBoolean("admin"));
                user.setUserID(rs.getInt("userID"));

            }

            //convert the password given by the user into an encrypted password
            //using the salt form the db
            String userPassword = PasswordGenerator.
                    getSHA512Password(passwordTextField.getText(), salt);

            SceneChanger sceneChanger = new SceneChanger();

            if(userPassword.equals(dbPassword))
            SceneChanger.setLoggedIn(user);

            if(userPassword.equals(dbPassword))
            {
                sceneChanger.changeScenes(event, "MovieTableView.fxml", "User Table");
            }
            else
            {
                errrorLabel.setText("Name or password is incorrect");
            }
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * empty initialize method
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
