package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Movie;
import models.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;

public class UserTableViewController implements Initializable{
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIDColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> phoneNumberColumn;
    @FXML private Button editUserBtn;

    @FXML private Button backBtn;

    /**
     * this method will exit out of the current view and return to the movie table view
     * @param event
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, "MovieTableView.fxml", "Create New ");
    }

    /**
     * this method doesnt work properly for the edit user
     * @param event
     * @throws IOException
     */
    public void editButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        User user = this.userTable.getSelectionModel().getSelectedItem();
        NewUserViewController npvc = new NewUserViewController();

        sc.changeScenes(event, "NewUserView.fxml", "Edit User");
    }

    /**
     * if the newuserbutton is pushed then return to the user view
     * @param event
     * @throws IOException
     */
    public void newUserButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, "NewUserView.fxml", "Create New User");
    }

    /**
     * initilize method for the class
     * @param url
     * @param resourceBundle
     */
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle)
   {

       editUserBtn.setDisable(true);
       userIDColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userID"));
       nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
       phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));

       try {
           loadUsers();
       } catch (SQLException e) {
           System.err.println(e.getMessage());
       }
   }

    /**
     * lades all the users into a table view
     * @throws SQLException
     */
    public void loadUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie?useSSL=false", "root", "root");

            statement = conn.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM users");

            while(resultSet.next())
            {
                User newUser = new User(resultSet.getString("firstName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("admin"));

                newUser.setUserID(resultSet.getInt("userID"));
                users.add(newUser);

            }
            userTable.getItems().addAll(users);

        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if(conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }

    /**
     * enable the edit user button only if a column is selected
     */
    public void userSelect()
    {
        editUserBtn.setDisable(false);
    }

}
