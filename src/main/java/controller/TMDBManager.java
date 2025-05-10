package controller;

import model.Film;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TMDBManager {

    private static final String API_KEY = "4532ed0f01d0ac211ca39b4b11925940";
    private final String SEARCH_ENDPOINT = "https://api.themoviedb.org/3/search/movie";

    public Film searchFilm(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String urlString = SEARCH_ENDPOINT + "?api_key=" + API_KEY + "&query=" + encodedQuery;
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line=reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
            String jsonResponse = responseBuilder.toString();

            Gson gson = new Gson();
            TMDBResponse response = gson.fromJson(jsonResponse, TMDBResponse.class);

            if (response.results != null && !response.results.isEmpty()) {
                TMDBResult result = response.results.get(0);
                LocalDate releaseDate = LocalDate.now();
                if (result.release_date != null && !result.release_date.isEmpty()) {
                    try {
                        releaseDate = LocalDate.parse(result.release_date, DateTimeFormatter.ISO_DATE);
                    }catch (Exception e) {
                        releaseDate = LocalDate.now();
                    }
                }

                Film film = new Film();
                film.setTitle(result.title);
                //film.setDirector(result.original_title);
                film.setGenre(parseGenres(result.genre_ids));
                film.setReleaseDate(releaseDate);
                //film.setTrailerURL("");
                film.setActors(List.of());

                if (result.id != 0) {
                    String director = fetchDirector(result.id);
                    film.setDirector(director);

                    String trailerUrl = fetchTrailerURL(result.id);
                    film.setTrailerURL(trailerUrl);
                } else {
                    film.setDirector(result.filmDirector);
                    film.setTrailerURL("");
                }


                if (result.poster_path != null && !result.poster_path.isEmpty()) {
                    film.setPosterUrl("https://image.tmdb.org/t/p/w500" + result.poster_path);

                } else {
                    film.setPosterUrl("");
                }




                return film;
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return null;
    }

    private String parseGenres(List<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return "Inconnu";
        }

        Map<Integer, String> genresMap = new HashMap<>();
        genresMap.put(28, "Action");
        genresMap.put(12, "Aventure");
        genresMap.put(16, "Animation");
        genresMap.put(35, "Comédie");
        genresMap.put(80, "Crime");
        genresMap.put(99, "Documentaire");
        genresMap.put(18, "Drame");
        genresMap.put(10751, "Familial");
        genresMap.put(14, "Fantastique");
        genresMap.put(36, "Histoire");
        genresMap.put(27, "Horreur");
        genresMap.put(10402, "Musique");
        genresMap.put(9648, "Mystère");
        genresMap.put(10749, "Romance");
        genresMap.put(878, "Science-fiction");
        genresMap.put(10770, "Téléfilm");
        genresMap.put(53, "Thriller");
        genresMap.put(10752, "Guerre");
        genresMap.put(37, "Western");

        List<String> genreNames = new ArrayList<>();
        for (Integer id : genreIds) {
            if (genresMap.containsKey(id)) {
                genreNames.add(genresMap.get(id));
            }
        }
        return String.join(", ", genreNames);
    }

    private String fetchDirector(int movieId) {
        try {
            String creditsUrl = String.format("https://api.themoviedb.org/3/movie/%d/credits?api_key=%s", movieId, API_KEY);
            URL url = new URL(creditsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
            String jsonResponse = responseBuilder.toString();

            Gson gson = new Gson();
            TMDBCreditsResponse creditsResponse = gson.fromJson(jsonResponse, TMDBCreditsResponse.class);
            if (creditsResponse.crew != null) {
                for (TMDBCrew crew : creditsResponse.crew) {
                    if ("Director".equalsIgnoreCase(crew.job)) {
                        return crew.name;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Inconnu";
    }

    private String fetchTrailerURL(int movieId) {
        try {
            String videosUrl = String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=%s", movieId, API_KEY);
            URL url = new URL(videosUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
            String jsonResponse = responseBuilder.toString();

            Gson gson = new Gson();
            TMDBVideoResponse videoResponse = gson.fromJson(jsonResponse, TMDBVideoResponse.class);
            if (videoResponse.results != null) {
                for (TMDBVideo video : videoResponse.results) {
                    if ("Trailer".equalsIgnoreCase(video.type) && "YouTube".equalsIgnoreCase(video.site)) {
                        return "https://www.youtube.com/watch?v=" + video.key;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static class TMDBResponse {
        List<TMDBResult> results;
    }

    private static class TMDBResult {
        int id;
        String title;
        String filmDirector;
        List<Integer> genre_ids;
        String release_date;
        String poster_path;
    }

    private static class TMDBCreditsResponse {
        List<TMDBCrew> crew;
    }
    private static class TMDBCrew {
        String job;
        String name;
    }

    private static class TMDBVideoResponse {
        List<TMDBVideo> results;
    }
    private static class TMDBVideo {
        String key;
        String site;
        String type;
    }


}
