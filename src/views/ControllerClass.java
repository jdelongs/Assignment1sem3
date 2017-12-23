package views;

import models.Movie;
import models.User;

/**
 * interface to preload data
 */
public interface ControllerClass {
    public abstract void preloadData(Movie movie);
}
