package view;

import controller.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Film;
import model.UserHistory;

public class FavoritesController {

    @FXML
    private ListView<String> favoritesList;

    private UserHistory userHistory = new UserHistory();
    private ViewManager viewManager;

    @FXML
    public void initialize() {
        for (Film currentF : userHistory.getFavorites()) {
            favoritesList.getItems().add(currentF.getTitle());
        }
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
