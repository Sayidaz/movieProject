package model.repository;

import model.Film;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class FilmRepository {
    private static FilmRepository instance;
    private Connection databaseConnection;
    private Map<String, Film> filmCache;

    public FilmRepository() {
        this.filmCache = new HashMap<>();
        try {
            String url = "jdbc:sqlite:C:/Users/arzin/OneDrive/Documents/Esi Cours/PRJ4/4-prj-1-d-d-131-63797/externeData/data.db";
            this.databaseConnection = DriverManager.getConnection(url);
            System.out.println("Connexion réussie à la base de données SQLite !");
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    public static synchronized FilmRepository getInstance() {
        if (instance == null) {
            instance = new FilmRepository();
        }
        return instance;
    }

    public void initializeDatabase() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Films (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    director TEXT,
                    genre TEXT,
                    releaseDate TEXT,
                    trailerURL TEXT
                );
                """;
        String createTableUser = """
                CREATE TABLE Users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL
                );
                """;
        try (Statement stmt = databaseConnection.createStatement()) {
            stmt.execute(createTableSQL);
            stmt.execute(createTableUser);
            System.out.println("Tables 'Films' et 'Users' initialisée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables : " + e.getMessage());
        }
    }


    public Film getFilmByTitle(String title) {
        String normalizedTitle = utils.TitleNormalizer.normalize(title);
        System.out.println("Titre scanné normalisé : " + normalizedTitle);

        for (Map.Entry<String, Film> entry : filmCache.entrySet()) {
            String keyNormalized = utils.TitleNormalizer.normalize(entry.getKey());
            if (utils.TitleNormalizer.areTitlesMatching(normalizedTitle, keyNormalized)) {
                return entry.getValue();
            }
        }

        String sql = "SELECT * FROM Films";
        try (PreparedStatement prepStat = databaseConnection.prepareStatement(sql)) {
            try (ResultSet res = prepStat.executeQuery()) {
                while (res.next()) {
                    Film film = mapResultSetToFilm(res);
                    String filmNormalized = utils.TitleNormalizer.normalize(film.getTitle());
                    System.out.println("Comparaison avec film en base (normalisé): " + filmNormalized);
                    if (utils.TitleNormalizer.areTitlesMatching(normalizedTitle, filmNormalized)) {
                        filmCache.put(film.getTitle(), film);
                        return film;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du film : " + e.getMessage());
        }
        return null;

    }

    private Film mapResultSetToFilm(ResultSet res) throws SQLException {
        Film film = new Film();
        film.setId(res.getInt("id"));
        film.setTitle(res.getString("title"));
        film.setDirector(res.getString("director"));
        film.setGenre(res.getString("genre"));
        film.setReleaseDate(java.time.LocalDate.parse(res.getString("releaseDate")));
        film.setTrailerURL(res.getString("trailerURL"));
        return film;
    }

    public void saveSearchHistory(Film film) {

        filmCache.put(film.getTitle(), film);

        String sql = "INSERT INTO Films (title, director, genre, releaseDate, trailerURL) VALUES (?, ?, ?, ?, ?)";
        // pas besoin d'insert d'id parce qu'il se fait automatiquement
        try (PreparedStatement prepStat = databaseConnection.prepareStatement(sql)) {
            prepStat.setString(1, film.getTitle());
            prepStat.setString(2, film.getDirector());
            prepStat.setString(3, film.getGenre());
            prepStat.setString(4, film.getReleaseDate().toString()); // Conversion LocalDate en String
            prepStat.setString(5, film.getTrailerURL());
            // les 1 2 3 etc représentent l'index du point d'interrogation dans la requête
            // sql au dessus, genre 3 c'est le 3eme point d'interro. et dcp on remplace
            // ce point par une valeur pour que la requête s'exécute

            prepStat.executeUpdate();
            System.out.println("Film ajouté à l'historique : " + film.getTitle());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du film dans l'historique : " + e.getMessage());
        }
    }


    public List<Film> getRecommendations(Film film) {
        List<Film> recommendations = new ArrayList<>();
        String sql = "SELECT * FROM Films WHERE genre = ? AND title != ?";

        try (PreparedStatement prepStat = databaseConnection.prepareStatement(sql)) {
            prepStat.setString(1, film.getGenre());
            prepStat.setString(2, film.getTitle());
            try (ResultSet res = prepStat.executeQuery()) {
                while (res.next()) {
                    Film recoFilm = mapResultSetToFilm(res);
                    recommendations.add(recoFilm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des recommandations pour " + film.getTitle() + " : " + e.getMessage());
        }
        return recommendations;
    }
    public boolean createUser(String username, String password) {
        String insertQuery = "INSERT INTO Users (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement pstmt = databaseConnection.prepareStatement(insertQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password)); // Hash the password before storing it
            pstmt.executeUpdate();
            System.out.println("User account created successfully: " + username);
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to create user account: " + e.getMessage());
            return false;
        }
    }
    public boolean loginUser(String username, String password) {
        String selectQuery = "SELECT * FROM Users WHERE username = ? AND password_hash = ?";
        try (PreparedStatement pstmt = databaseConnection.prepareStatement(selectQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password)); // Hash the password for comparison
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("User login successful: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Failed to log in user: " + e.getMessage());
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }


}
