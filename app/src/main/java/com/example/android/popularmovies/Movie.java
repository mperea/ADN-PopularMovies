package com.example.android.popularmovies;

/**
 * Custom class for a Movie object with the needed data.
 */
public class Movie {

    private String id;          // The movie ID assigned by TMDB.com.
    private String title;       // Title of the movie.
    private String overview;    // Synopsis of the movie.
    private String releaseDate; // Movie release date.
    private String posterPath;  // Movie poster path from TMDB.com.
    private String voteAverage; // TMDB.com's user average note for the movie.

    // Setters and Getters.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    // Constructors.
    public Movie() {
    }

    public Movie(String id, String title, String overview, String releaseDate, String posterPath, String voteAverage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }
}
