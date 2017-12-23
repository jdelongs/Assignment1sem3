package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args)
    {
        //source code here
        launch(args);
    }

    /**
     * this is the stage that will be shown when the user launches the application
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("New Volunteer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}