package view;

import controller.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Film;

public class InsightsController {
    @FXML private Label titleLabel;
    @FXML private Label directorLabel;
    @FXML private Label genreLabel;
    @FXML private Label releaseDateLabel;
    @FXML private Label trailerURLLabel;

    private ViewManager viewManager;

    public void setFilm(Film film) {
        titleLabel.setText("Titre : " + film.getTitle());
        directorLabel.setText("Réalisateur : " + film.getDirector());
        genreLabel.setText("Genre : " + film.getGenre());
        releaseDateLabel.setText("Date de sortie : " + film.getReleaseDate());
        trailerURLLabel.setText("Trailer : " + film.getTrailerURL());

    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    public void handleBack() {
        if (viewManager != null) {
            viewManager.showView("ScanView.fxml");
        }
    }

}
