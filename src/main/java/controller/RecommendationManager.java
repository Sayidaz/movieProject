package controller;
import model.Film;
import model.repository.FilmRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationManager {
    private FilmRepository filmRepo;

    public RecommendationManager(FilmRepository filmRepo) {
        this.filmRepo = filmRepo;
    }


    public List<Film> generateRecommendations(List<Film> history) {

        Map<Film, Integer> scoredRecommendations = new HashMap<>();
        int baseScore = 10;

        for (Film historyFilm : history) {
            List<Film> recommendations = filmRepo.getRecommendations(historyFilm);
             for (Film recoFilm : recommendations) {
                int currentScore = scoredRecommendations.getOrDefault(recoFilm, 0);
                scoredRecommendations.put(recoFilm, currentScore + baseScore);
            }
        }

        List<Map.Entry<Film, Integer>> sortedEntries = new ArrayList<>(scoredRecommendations.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<Film> finalRecommendations = new ArrayList<>();

        for (Map.Entry<Film, Integer> entry : sortedEntries) {
            finalRecommendations.add(entry.getKey());
        }

        return finalRecommendations;
    }
}
