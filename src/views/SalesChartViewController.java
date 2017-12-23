package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SalesChartViewController implements Initializable {

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private CategoryAxis months;

    @FXML
    private Button backButton;

    @FXML
    private NumberAxis sales;

    private XYChart.Series currentYearSeries, previousYearSeries;

    /**
     * initialize method for the class
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        previousYearSeries = new XYChart.Series<>();
        currentYearSeries = new XYChart.Series<>();

        barChart.setTitle("Sales");
        months.setLabel("Months");

        sales.setLabel("Sales Made");

        currentYearSeries.setName(Integer.toString(LocalDate.now().getYear()));
        previousYearSeries.setName(Integer.toString(LocalDate.now().getYear()-1));
        try {
            populateSeriesFromDB();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        barChart.getData().addAll(previousYearSeries);
        barChart.getData().addAll(currentYearSeries);
    }

    /**
     * this method will read the user data form the database and populate the series
     */
    public void populateSeriesFromDB() throws SQLException {
        //get results from db

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie?useSSL=false", "root", "root");

            statement = conn.createStatement();

            String sql = "SELECT YEAR(dateSold), MONTHNAME(dateSold), SUM(priceSold) " +
                    "FROM sales " +
                    "GROUP BY YEAR(dateSold), MONTH(dateSold) " +
                    "ORDER BY YEAR(dateSold), MONTH(dateSold);";
            resultSet = statement.executeQuery(sql);

            while(resultSet.next())
            {
                if(resultSet.getInt(1) == LocalDate.now().getYear())
                currentYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getDouble(3)));
                else if(resultSet.getInt(1) == LocalDate.now().getYear()-1)
                    previousYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getDouble(3)));
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally {
            if(conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }

    /**
     * this method will exit the current view and return to the movietableview
     * @param actionEvent
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent actionEvent) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(actionEvent, "MovieTableView.fxml", "User Table");
    }
}
