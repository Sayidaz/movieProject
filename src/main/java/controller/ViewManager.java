package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.UserSession;
import view.FavoritesController;
import view.InsightsController;
import view.RecommendationsController;
import view.ScanController;

import java.io.IOException;

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
    @FXML
    private Label loggedInUserLabel;
    @FXML
    private javafx.scene.control.Button logoutButton;

    @FXML
    public void initialize() {
        // Display the logged-in user's username
        String username = UserSession.getInstance().getUsername();
        if (username != null) {
            loggedInUserLabel.setText("Logged in as: " + username);
        } else {
            loggedInUserLabel.setText("Not logged in");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleLogout() {
        // Clear the user session
        UserSession.getInstance().clearSession();

        showAlert(Alert.AlertType.INFORMATION, "Logout Successful", "You have been logged out.");
        // Navigate back to the login view
        navigateToLoginView();
    }

    private void navigateToLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
