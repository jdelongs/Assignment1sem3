package models;

public class Video {

    private String title;
    private int length;
    //constructor
    public Video(String title, int length) {
        setTitle(title);
        setLength(length);
    }

    /**
     * get the title
     * @return
     */
    public String getTitle() {
        return title;
    }


    /**
     * validate the there has been a title provided and that there are no special characters in it
     * @param title
     */
    public void setTitle(String title) {
        if(!title.isEmpty() && title.matches("[A-Za-z0-9]+"))
            this.title = title;
        else
            throw new IllegalArgumentException("Missing Title and only letters and numbers");

    }

    /**
     * get the lengthMins
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * validate that the movie is above zero minutes long and below 500
     *  @param length
     */
    public void setLength(int length) {
        if(length > 0 && length < 500)
            this.length = length;
        else
            throw new IllegalArgumentException("Length of the movie must be between 1-500 minutes long");
    }

}
