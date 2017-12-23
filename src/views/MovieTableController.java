package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Movie;
import models.User;

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
    @FXML private Button sellItemButton;
    @FXML private Button salesReportButton;
    @FXML private Button userButton;
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();


    /**
     * this method will switch to the NewMovie view
     */
    public void newMovieButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, "NewMovieView.fxml", "Create New Movie ");
    }

    /**
     * this method will show the chart for the sales
     * @param actionEvent
     * @throws IOException
     */
    public void salesChartPushed(ActionEvent actionEvent) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(actionEvent, "SalesChartView.fxml", "User Table");
    }

    /**
     * this method will switch to the user view
     */
    public void allUsersButtonPushed(ActionEvent event) throws IOException {

        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, "UserTableView.fxml", "All Users");
    }

    /**
     * this method will switch to the sell item view
     */
    public void sellItemButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();

        Movie movie= this.movieTable.getSelectionModel().getSelectedItem();
        if(movie == null)
            return;
        SalesViewController sv = new SalesViewController();
        sceneChanger.changeScenes(event, "SalesView.fxml", "Record A Sale", movie, sv);
    }

    /**
     * initialize method
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!SceneChanger.getLoggedIn().isAdmin())
        {
            salesReportButton.setVisible(false);
            userButton.setVisible(false);

        }
        sellItemButton.setDisable(true);
        movieIDColumn.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("movieID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("length"));
        price.setCellValueFactory(new PropertyValueFactory<Movie, Double>("price"));
        releaseDate.setCellValueFactory(new PropertyValueFactory<Movie, Date>("releaseDate"));

        try {
            loadMovies();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

        /**
         * this method get the users from the database and stores them in the table view
         */
        public void loadMovies() throws SQLException {
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

    /**
     * disable the sellbutton unless something in the table is selected
     */
    public void movieSelected()

        {
            sellItemButton.setDisable(false);
        }

}


