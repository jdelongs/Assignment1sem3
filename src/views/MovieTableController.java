package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Movie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MovieTableController implements Initializable {

    @FXML private TableView<Movie> movieTable;
    @FXML private TableColumn<Movie, Integer> movieIDColumn;
    @FXML private TableColumn<Movie, String> titleColumn;
    @FXML private TableColumn<Movie, Integer> lengthColumn;
    @FXML private TableColumn<Movie, Date> releaseDate;
    @FXML private TableColumn<Movie, Double> price;
    @FXML private Label inventoryValueLbl;

    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();
    /**
     * this method will switch to the NewMovie view
     */
    public void newMovieButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, "NewMovieView.fxml", "Create New ");
    }

    /**
     * initialize method
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        movieIDColumn.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("movieID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("length"));
        price.setCellValueFactory(new PropertyValueFactory<Movie, Double>("price"));
        releaseDate.setCellValueFactory(new PropertyValueFactory<Movie, Date>("releaseDate"));

        try {
            loadVolunteers();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

        /**
         * this method get the volunteers from the database and stores them in the table view
         */
        public void loadVolunteers() throws SQLException {
            ObservableList<Movie> movies = FXCollections.observableArrayList();
            Connection conn = null;
            Statement statement = null;
            ResultSet resultSet = null;
            try
            {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie?useSSL=false", "root", "root");

                statement = conn.createStatement();

                resultSet = statement.executeQuery("SELECT * FROM movies");

               while(resultSet.next())
               {
                   Movie newMovie = new Movie(resultSet.getString("title"),
                                                resultSet.getInt("lengthMins"),
                                                resultSet.getDate("releaseDate").toLocalDate(),
                                                resultSet.getInt("rating"),
                                                resultSet.getDouble("price"));

                   newMovie.setMovieID(resultSet.getInt("movieID"));
                   newMovie.setImageFile(new File(resultSet.getString("imageFile")));
                   movies.add(newMovie);
                   double inventoryValue = 0;
                   for(Movie movie : movies )
                   {
                       inventoryValue += movie.getPrice();

                   }
                   inventoryValueLbl.setText(currency.format(inventoryValue));

               }
               movieTable.getItems().addAll(movies);

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

}


