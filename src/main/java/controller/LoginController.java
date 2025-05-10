package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.UserSession;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final UserManager userManager = UserManager.getInstance();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userManager.login(username, password)) {
            // Store the logged-in user's username in the session
            UserSession.getInstance().setUsername(username);

            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
            // Navigate to the main application view here
            // Example: viewManager.showView("MainView.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleCreateAccount() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userManager.createAccount(username, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Account Created", "Your account has been successfully created. Please log in.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Account Creation Failed", "An error occurred. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}