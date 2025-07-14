package br.com.dio.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Ponto de entrada principal para a aplicação Sudoku com interface gráfica JavaFX.
 * Esta classe é responsável por carregar a visão inicial (FXML), aplicar os estilos (CSS)
 * e exibir a janela principal do jogo.
 */
public class SudokuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("sudoku-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 500);

        // Carrega a folha de estilos de forma segura
        final URL cssUrl = getClass().getResource("styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            // Em vez de quebrar, a aplicação avisa sobre o arquivo ausente e continua.
            System.err.println("Aviso: A folha de estilos 'styles.css' não foi encontrada. A aplicação será executada com os estilos padrão.");
        }

        stage.setTitle("Sudoku com JavaFX");
        stage.setScene(scene);
        stage.setResizable(false); // Opcional: impede o redimensionamento
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}