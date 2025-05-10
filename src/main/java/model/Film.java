package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Film {

    private int id;
    private String title;
    private String director;
    private List<String> actors;
    private String genre;
    private LocalDate releaseDate;
    private String trailerURL;
    private String posterUrl;

    public Film() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film(String title, String director, List<String> actors, String genre, LocalDate releaseDate, String trailerURL) {
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.trailerURL = trailerURL;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "Film {" +
                "title ='" + title + '\'' +
                ", director ='" + director + '\'' +
                ", actors =" + actors +
                ", genre ='" + genre + '\'' +
                ", releaseDate =" + releaseDate +
                ", trailerURL ='" + trailerURL + '\'' +
                ", posterUrl ='" + posterUrl + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id != 0 ? id == film.id : Objects.equals(title, film.title) && Objects.equals(releaseDate, film.releaseDate);
    }

    @Override
    public int hashCode() {
        return id != 0 ? Objects.hash(id) : Objects.hash(title, releaseDate);
    }
}
