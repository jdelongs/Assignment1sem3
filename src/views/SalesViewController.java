package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Movie;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SalesViewController implements Initializable, ControllerClass{

    @FXML private DatePicker dateSoldDatePicker;

    @FXML private Label titleLabel;

    @FXML private TextField sellingPriceTextField;

    @FXML private Label movieIDLabel;

    @FXML private Label errorLabel;

    @FXML private Button backButton;

    private Movie movie;

    /**
     * initizlize method for the class
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    /**
     * interface preloaddatat method
     * @param movie
     */
    @Override
    public void preloadData(Movie movie) {
        this.movie = movie;
        movieIDLabel.setText(Integer.toString(movie.getMovieID()));
        titleLabel.setText(movie.getTitle());
        dateSoldDatePicker.setValue(LocalDate.now());
        errorLabel.setText("");
    }



    /**
     * this method will validate the inputs and store the information in our db
     */
    public void saveButtonPushed(ActionEvent event) throws IOException {
        try
        {
            movie.itemSales(dateSoldDatePicker.getValue(),
                    Double.parseDouble(sellingPriceTextField.getText()));
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.changeScenes(event, "MovieTableView.fxml", "User Table");
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            errorLabel.setText(e.getMessage());
        }
    }
    /**
     * this method will exit out of the newMovieVIew and return to the MovieTableView
     */
    public void cancelButtonPushed(ActionEvent actionEvent) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(actionEvent, "MovieTableView.fxml", "Movies");
    }
}
