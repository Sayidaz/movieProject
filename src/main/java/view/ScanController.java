package view;

import controller.TMDBManager;
import controller.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Film;
import model.OCR.OCRManager;
import model.repository.FilmRepository;

import java.io.File;
import java.io.IOException;

public class ScanController {
    @FXML
    private TextArea scanResultArea;

    @FXML
    private TextField imagePathField;

    @FXML
    private ImageView filmPosterView;

    private OCRManager ocrManager = new OCRManager();
    private FilmRepository filmRepository = FilmRepository.getInstance();
    private ViewManager viewManager;
    private File selectedImage;
    private Film scannedFilm;

    @FXML
    public void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une affiche");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg", "*.jpg", "*.bmp"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImage = file;
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleScan() {
        if (selectedImage == null) {
            scanResultArea.setText("Veuillez choisir une affiche avant de scanner");
            return;
        }

        model.Image image = new model.Image (selectedImage.getAbsolutePath());
        String extractedText = ocrManager.extractText(image);

        if (extractedText != null && !extractedText.isEmpty()) {
            scanResultArea.setText("Texte extrait :\n" + extractedText);

            Film film = filmRepository.getFilmByTitle(extractedText);
            if (film != null) {
                scanResultArea.appendText("\nFilm trouvé : " + film.getTitle());
                scannedFilm = film;
                filmRepository.saveSearchHistory(film);
            } else {
                scanResultArea.appendText("\nFilm non trouvé en base de donnée\nInterrogation de TMDB...");
                controller.TMDBManager tmdbManager = new TMDBManager();
                film = tmdbManager.searchFilm(extractedText);
                if (film != null) {
                    scanResultArea.appendText("\nFilm récupéré depuis TMDb : " + film.getTitle());
                    scannedFilm = film;
                    filmRepository.saveSearchHistory(film);
                } else {
                    scanResultArea.appendText("\nFilm introuvable sur TMDB");
                }
            }
            if (scannedFilm != null && scannedFilm.getPosterUrl() != null && !scannedFilm.getPosterUrl().isEmpty()) {
                javafx.scene.image.Image posterImage = new javafx.scene.image.Image(scannedFilm.getPosterUrl(), true);
                filmPosterView.setImage(posterImage);
            }
        } else {
            scanResultArea.setText("Aucun texte détecté dans l'image");
        }
    }
    @FXML
    public void navigateToInsights() {

        if (scannedFilm == null) {
            scanResultArea.appendText("\nAucun film scanné pour afficher les détails.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InsightsView.fxml"));
            Parent root = loader.load();

            InsightsController insightsController = loader.getController();
            insightsController.setFilm(scannedFilm);
            insightsController.setViewManager(viewManager);

            Stage stage = (Stage) scanResultArea.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void navigateToFavorites() {
        if (viewManager != null) {
            viewManager.showView("FavoritesView.fxml");
        }
    }

    @FXML
    public void navigateToRecommendations() {
        if(viewManager != null) {
            viewManager.showView("RecommendationsView.fxml");

        }
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
