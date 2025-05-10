package view;

import controller.RecommendationManager;
import controller.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Film;
import model.repository.FilmRepository;

import java.util.List;

public class RecommendationsController {

    @FXML
    private ListView<String> recommendationsList;

    private FilmRepository filmRepository = new FilmRepository();
    private RecommendationManager recommendationManager = new RecommendationManager(filmRepository);
     private List<Film> sampleHistory;
     private ViewManager viewManager;

     @FXML

     public void initialize() {
         if (sampleHistory != null && !sampleHistory.isEmpty()) {
             List<Film> recommended = recommendationManager.generateRecommendations(sampleHistory);
             for (Film currentF : recommended) {
                 recommendationsList.getItems().add(currentF.getTitle());

             }
         } else {
             recommendationsList.getItems().add("Aucune recommendation disponible");
         }
     }

     public void setSampleHistory(List<Film> history) {
         this.sampleHistory = history;
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
