package br.com.dio.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Esta classe executa a aplicação com a GUI Java FX
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 500);

        // Carrega a folha de estilos CSS
        String css = this.getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Sudoku com JavaFX");
        stage.setScene(scene);
        stage.setResizable(false); // Opcional: impede o redimensionamento
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}