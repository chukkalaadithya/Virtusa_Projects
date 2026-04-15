import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/view/login.fxml")
	    );

	    Scene scene = new Scene(loader.load(), 1200, 700);

	    stage.setScene(scene);
	    stage.setTitle("Banking System");

	    // Default window size
	    stage.setWidth(1200);
	    stage.setHeight(700);

	    // Allow maximize
	    stage.setMaximized(false);

	    stage.show();
	}

    public static void main(String[] args) {
        launch(args);
    }
}

