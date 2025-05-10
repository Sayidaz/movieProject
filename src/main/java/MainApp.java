import controller.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewManager viewManager = new ViewManager(primaryStage);
        viewManager.showView("LoginView.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
