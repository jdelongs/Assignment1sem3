package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Movie;
import models.User;

import java.io.IOException;

public class SceneChanger {

    private static User loggedIn;
    /**
     * this method will get the title new scene and the fxml file for the view
     */
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

    }
    public void changeScenes(ActionEvent event, String viewName, String title, Movie movie, ControllerClass controllerClass) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        //access the controller class and preloaded the volunteer data
        controllerClass = loader.getController();
        controllerClass.preloadData(movie);

        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    public static User getLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(User loggedIn) {
        SceneChanger.loggedIn = loggedIn;
    }

}
