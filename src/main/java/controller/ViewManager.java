package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.FavoritesController;
import view.InsightsController;
import view.RecommendationsController;
import view.ScanController;

public class ViewManager {

    private Stage primaryStage;

    public ViewManager(Stage stage) {
        this.primaryStage = stage;
    }

    public void showView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if(controller instanceof ScanController) {
                ((ScanController)controller).setViewManager(this);
            } else if (controller instanceof InsightsController) {
                ((InsightsController)controller).setViewManager(this);
            } else if (controller instanceof FavoritesController) {
                ((FavoritesController)controller).setViewManager(this);
            } else if (controller instanceof RecommendationsController) {
                ((RecommendationsController)controller).setViewManager(this);
            }

            Scene scene = new Scene(root);
            primaryStage.setTitle("Movinsights");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
