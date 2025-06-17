// Pacote: projeto
package projeto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/Login.fxml"));
            Parent root = fxmlLoader.load();
            
            Scene scene = new Scene(root);
            
            stage.setTitle("EzPlace - Login");
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
            System.err.println("Ocorreu um erro grave ao iniciar a aplicação:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}