package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Movie;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class NewUserViewController implements Initializable, ControllerClass {

    @FXML private TextField nameTxt;
    @FXML private TextField phoneNumberTxt;
    @FXML private PasswordField pwField;
    @FXML private PasswordField confirmPwField;
    @FXML private CheckBox adminCheckBox;
    @FXML private Label errorLabel;
    @FXML private Label headerLabel;

    private User user;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * when the save button is pushed insert the user into the database and navigate back to the usertableview
     * @param event
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void saveButtonPushed(ActionEvent event) throws SQLException, NoSuchAlgorithmException, IOException {

        if(validPassword()) {

            try
            {
                if(user != null)
                {
                    updateMovie();
                   user.updateUserInDB();
                }
                else
                {
                    user = new User(nameTxt.getText(), phoneNumberTxt.getText(), pwField.getText(), adminCheckBox.isSelected());
                }

                errorLabel.setText("");
                user.insertIntoDB();
                SceneChanger sc = new SceneChanger();
                sc.changeScenes(event, "UserTableView.fxml", "All Users");


            }catch (Exception e)
            {
                errorLabel.setText(e.getMessage());
            }


        }
    }

    /**
     * This method will validate that the passwords match
     *
     */
    public boolean validPassword()
    {
        if (pwField.getText().length() < 5)
        {
            errorLabel.setText("Passwords must be greater than 5 characters in length");
            return false;
        }

        if (pwField.getText().equals(confirmPwField.getText()))
            return true;
        else
            return false;
    }




    /**
     * This method will read from the GUI fields and update the user object
     */
    public void updateMovie() throws IOException
    {
        user.setName(nameTxt.getText());
        user.setPhoneNumber(phoneNumberTxt.getText());

    }

    /**
     * this method will exit the current view and return to the usertableview
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonPushed(ActionEvent actionEvent) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(actionEvent, "UserTableView.fxml", "User Table");
    }

    /**
     * interface preload data method
     * @param movie
     */
    @Override
    public void preloadData(Movie movie) {
        this.user = user;
        this.nameTxt.setText(user.getName());
        this.phoneNumberTxt.setText(user.getPhoneNumber());
        this.pwField.setText("");
        this.headerLabel.setText("Edit Volunteer");
    }
}

