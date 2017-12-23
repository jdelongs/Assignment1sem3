package models;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;

public class Movie extends Video {

    private LocalDate releaseDate;
    private int rating;
    private double price;
    private File imageFile;
    private int movieID;

    //constructor 1
    public Movie(String title, int length, LocalDate releaseDate, int rating, double price) {
        super(title, length);
        setReleaseDate(releaseDate);
        setRating(rating);
        setPrice(price);
        setImageFile(new File("./src/images/defaultImage.png"));
    }
    //constructor 2 for the image
    public Movie(String title, int length, LocalDate releaseDate, int rating, double price, File imageFile) throws IOException {
        super(title, length);
        setReleaseDate(releaseDate);
        setRating(rating);
        setPrice(price);
        setImageFile(imageFile);
        copyImageFile();
    }

    /**
     * gets the movie ID
     * @return
     */
    public int getMovieID() {
        return movieID;
    }

    /**
     * sets the movieID and validates that it is greater than zero
     * @param movieID
     */
    public void setMovieID(int movieID) {
        if(movieID>= 0)
            this.movieID = movieID;
        else
            throw new IllegalArgumentException("MovieID cannot be less than zero");

    }

    /**
     * get the release date of the movie
     * @return
     */
    public LocalDate getReleaseDate() {
        return releaseDate;

    }

    /**
     * validate the the release date is not past 5 years into the future
     * @param releaseDate
     */
    public void setReleaseDate(LocalDate releaseDate) {
        if(releaseDate.getYear() < LocalDate.now().getYear()+5)
            this.releaseDate = releaseDate;
        else
            throw new IllegalArgumentException("Date cannot be greater than 5 years into the future");
    }
    /**
     * gets the rating
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     * validates the rating is between 1-10
     * @param rating
     */
    public void setRating(int rating) {
        if(rating >= 1 && rating < 11 || rating == 0)
            this.rating = rating;
        else
            throw new IllegalArgumentException("rating must be between 1-10");

    }
    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * gets the image file
     * @return
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * set the image file
     * @param imageFile
     */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * this method copys the new unique file to our directory
     */
    public void copyImageFile() throws IOException
    {
        //create a new Path to copy the image into a local directory
        Path sourcePath = imageFile.toPath();

        String uniqueFileName = getUniqueFileName(imageFile.getName());

        Path targetPath = Paths.get("./src/images/"+uniqueFileName);

        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        //update the imageFile to point to the new File
        imageFile = new File(targetPath.toString());
    }


    /**
     * this method will give the old file name and string of random characters appended in front of it
     */
    private String getUniqueFileName(String oldFileName)
    {
        String newName;

        //create a Random Number Generator
        SecureRandom rng = new SecureRandom();

        //loop until we have a unique file name
        do
        {
            newName = "";

            //generate 32 random characters
            for (int count=1; count <=32; count++)
            {
                int nextChar;

                do
                {
                    nextChar = rng.nextInt(123);
                } while(!validCharacterValue(nextChar));

                newName = String.format("%s%c", newName, nextChar);
            }
            newName += oldFileName;

        } while (!uniqueFileInDirectory(newName));

        return newName;
    }


    /**
     * this method validates that there is no image in the directory with the name in the
     * getUniqueFileName() method
     */
    public boolean uniqueFileInDirectory(String fileName)
    {
        File directory = new File("./src/images/");

        File[] dir_contents = directory.listFiles();

        for (File file: dir_contents)
        {
            if (file.getName().equals(fileName))
                return false;
        }
        return true;
    }

    /**
     * this method will make sure that only alphanumeric characters can be appended on the new file name
     */
    public boolean validCharacterValue(int asciiValue)
    {

        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57)
            return true;

        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90)
            return true;

        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122)
            return true;

        return false;
    }
    /**
     * returns a formatted string of this class
     * @return
     */
    public String toString()
    {
        return String.format("the movie title is %s, it is %d minutes long and the year released was %d, has a rating of %d, and price of %f",
                getTitle(),getLength(),  releaseDate.getYear(), rating, price);
    }

    /**
     * this method inserts the values of Movie and Video into a database as well as the unique file name
     */
    public void insertIntoDB() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie?useSSL=false", "root", "root");

            //2. create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO movies(title, lengthMins, releaseDate, rating, price, imageFile)"
                    + "VALUES (?,?,?,?,?,?)";

            //3. Prepare the query
            preparedStatement = conn.prepareStatement(sql);

            //4. convert the birthdate into a sql date
            Date rd = Date.valueOf(releaseDate);

            //5. bind the values to the parameters
            preparedStatement.setString(1, getTitle());
            preparedStatement.setInt(2, getLength());
            preparedStatement.setDate(3, rd);
            preparedStatement.setInt(4, rating);
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, imageFile.getName());

            //6. execute the query
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            //close the connection if its not equal to null
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * this method will record the sales of each item in the database
     */
    public void itemSales(LocalDate dateSold, double priceSold) throws SQLException {
        if(dateSold.isAfter(LocalDate.now()))
        {
            throw new IllegalArgumentException("Date sold cannot be in the future");
        }
        if(dateSold.isBefore(LocalDate.now().minusYears(1)))
        {
            throw new IllegalArgumentException("Date sold must be within the last 12 months");
        }

        if(priceSold < 0 || priceSold > 40)
        {
            throw new IllegalArgumentException("Movie must be in the range of 0-40 dollars");
        }

        Connection conn = null;
        PreparedStatement ps =null;
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie?useSSL=false", "root","root");

            String sql = "INSERT INTO sales (movieID, priceSold, dateSold) VALUES (?,?,?);";
            ps = conn.prepareStatement(sql);

            //convert into a date objcet
            Date dateS = Date.valueOf(dateSold);

            ps.setInt(1, movieID);
            ps.setDouble(2, priceSold);
            ps.setDate(3, dateS);

            //run the update
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
          System.err.println(e.getMessage());
        }
        finally {
            if(conn != null)
                conn.close();

            if(ps != null)
                ps.close();
        }
    }
}