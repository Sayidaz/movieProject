package model;

import java.util.ArrayList;
import java.util.List;

public class UserHistory {
    private List<Film> history;
    private List<Film> favorites;

    public UserHistory(){
        this.history = new ArrayList<>();
        this.favorites = new ArrayList<>();
    }

    public void addFilmToHistory(Film film) {
        if (!history.contains(film)) {
            history.add(film);
            System.out.println("Film ajouté à l'historique : " + film.getTitle());
        } else {
            System.out.println("Déjà dans l'historique : " + film.getTitle());
        }
    }

    public List<Film> getHistory() {
        return new ArrayList<>(history); // mieux de faire une copie pour pas toucher l'encapsulation
    }

    public void addToFavorites(Film film) {
        if (!favorites.contains(film)) {
            favorites.add(film);
            System.out.println("Film ajouté aux favoris : " + film.getTitle());

        } else {
            System.out.println("Déjà dans les favoris : " + film.getTitle());
        }
    }

    public List<Film> getFavorites() {
        return new ArrayList<>(favorites);
    }
}
