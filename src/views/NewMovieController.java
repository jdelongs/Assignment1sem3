package views;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


import models.Movie;

public class NewMovieController implements Initializable {
    @FXML private TextField titletxt;
    @FXML private TextField lengthMintxt;
    @FXML private DatePicker releaseDateDP;
    @FXML private ComboBox<Integer> rating = new ComboBox<Integer>();
    @FXML private TextField pricetxt;
    @FXML private Label errorLabell;
    @FXML private ImageView imageView;
    private Movie movie;
    private File imageFile;
    private boolean imageFileChanged;


    /**
     * this method will be triggered when the save button is pushed and adds the new user to the database
     * @param event
     */
    public void saveMovieButtonPushed(ActionEvent event) {

        try
            {

            if(imageFileChanged)
            {
                movie = new Movie(titletxt.getText(), Integer.parseInt(lengthMintxt.getText()),
                        releaseDateDP.getValue(),rating.getValue(), Double.parseDouble(pricetxt .getText()),  imageFile);
            }
            else
            {
                movie = new Movie(titletxt.getText(), Integer.parseInt(lengthMintxt.getText()),
                        releaseDateDP.getValue(),rating.getValue(), Double.parseDouble(pricetxt .getText()));
            }

            errorLabell.setText("");
            movie.insertIntoDB();
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.changeScenes(event, "MovieTableView.fxml", "Movies");
        }
        catch (IllegalArgumentException e)
        {
            if(titletxt.getText().isEmpty())
            {
                errorLabell.setText("Title cannot be empty");
            }

            else if(lengthMintxt.getText().isEmpty()) {
                errorLabell.setText("length Cannot be empty");
            }

            else if (pricetxt.getText().isEmpty())
            {
                errorLabell.setText("Price cannot be empty");
            }
        }
        catch (Exception e)
        {
            errorLabell.setText(e.getMessage());
        }


    }


    /**
     * this method will exit out of the newTableMovieVIew and return to the MovieTableView
     */
    public void cancelButtonPushed(ActionEvent actionEvent) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(actionEvent, "MovieTableView.fxml", "Movies");
    }

    /**
     * this method allows the user to select an image off of their computer
     * @param event
     */
    public void chooseImageButtonPushed(ActionEvent event)
    {
        //get the stage to open a new window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        //instantiate a filechooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");

        //filter out all files except .jpg and .png files
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFiler = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");

        //adds to observable list
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFiler);

        //set the users picture directory
        String userDirectoryString = System.getProperty("user.home")+ "\\Pictures";
        File userDirectory = new File(userDirectoryString);

        //validate that pictures is a directory on the users machine if no picture directory navigate home
        if(!userDirectory.canRead())

            userDirectory = new File(System.getProperty("user.home"));

            fileChooser.setInitialDirectory(userDirectory);
            File tempFile = fileChooser.showOpenDialog(stage);

        //check to see if the user selected a image
        if(tempFile != null) {
            imageFile = tempFile;

            //update the image view with the new image
            if (imageFile.isFile()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);

                    imageView.setImage(img);
                    imageFileChanged = true;
                } catch (IOException e) {
                    System.err.println(e.getMessage());

                }
            }
        }
    }

    /**
     * initialize method for the newMovieView
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        //set that if the image file has been changed to false
        imageFileChanged = false;

        //defualt value for the error label
        errorLabell.setText("");

        //add all the integers to the combo box
        rating.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        //sets the default selection to the first item in the combo box
        rating.getSelectionModel().selectFirst();

        releaseDateDP.setValue(LocalDate.now());



        //load default image
        try {
            imageFile = new File("./src/images/defaultImage.png");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
