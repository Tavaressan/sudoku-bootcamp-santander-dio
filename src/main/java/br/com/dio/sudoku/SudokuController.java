package br.com.dio.sudoku;

import br.com.dio.sudoku.model.Space;
import br.com.dio.sudoku.service.BoardService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class SudokuController {

    // Injeção dos componentes do FXML
    @FXML
    private GridPane sudokuGrid;
    @FXML
    private Button checkStatusButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button finishButton;

    // Nossos serviços e estado do jogo
    private BoardService boardService;
    // Mapeia cada célula do modelo a um campo de texto na UI
    private final Map<Space, TextField> spaceToTextFieldMap = new HashMap<>();

    /**
     * Este método é chamado automaticamente pelo JavaFX após o FXML ser carregado.
     * É o lugar perfeito para inicializar nossa UI.
     */
    @FXML
    public void initialize() {
        // 1. Carrega a configuração do nosso puzzle padrão
        Map<String, String> gameConfig = loadDefaultGameConfig();

        // 2. Inicializa o serviço do tabuleiro com a configuração carregada
        this.boardService = new BoardService(gameConfig);

        // 3. Desenha o tabuleiro na interface gráfica
        drawBoard();
    }

    /**
     * Cria a configuração para um puzzle de Sudoku padrão.
     * O formato do valor é "numero_esperado,eh_fixo".
     * Um número esperado 0 significa que o espaço está vazio.
     *
     * @return Um Map que representa a configuração do jogo.
     */
    private Map<String, String> loadDefaultGameConfig() {
        Map<String, String> gameConfig = new HashMap<>();
        // Puzzle de exemplo (nível fácil)
        int[][] puzzle = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = puzzle[row][col];
                boolean isFixed = value != 0;
                // A chave é "linha,coluna" e o valor é "numero,fixo"
                gameConfig.put(String.format("%d,%d", row, col), String.format("%d,%b", value, isFixed));
            }
        }
        return gameConfig;
    }

    private void drawBoard() {
        sudokuGrid.getChildren().clear(); // Limpa o grid antes de desenhar
        spaceToTextFieldMap.clear();

        var spaces = boardService.getSpaces();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Space space = spaces.get(row).get(col);
                // NOVO: Passamos as coordenadas para o método de criação
                TextField cell = createCell(space, row, col);
                sudokuGrid.add(cell, col, row); // Adiciona ao GridPane na coluna 'col', linha 'row'
                spaceToTextFieldMap.put(space, cell);
            }
        }
    }

    private TextField createCell(Space space, int row, int col) { // NOVO: Recebe row e col
        TextField textField = new TextField();
        textField.setPrefSize(40, 40);
        textField.setAlignment(Pos.CENTER);
        textField.getStyleClass().add("sudoku-cell"); // Classe CSS para estilização

        // NOVO: Adiciona classes de estilo para as bordas dos setores 3x3
        if ((col + 1) % 3 == 0 && col < 8) {
            textField.getStyleClass().add("cell-right-border");
        }
        if ((row + 1) % 3 == 0 && row < 8) {
            textField.getStyleClass().add("cell-bottom-border");
        }

        // Filtro para aceitar apenas 1 dígito (1-9)
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[1-9]?")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // Listener para atualizar o modelo quando o texto mudar
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!space.isFixed()) { // Garante que não tentamos mudar um valor fixo
                if (newVal.isEmpty()) {
                    space.clearSpace();
                } else {
                    space.setActual(Integer.parseInt(newVal));
                }
            }
        });

        if (space.isFixed()) {
            textField.setText(space.getActual().toString());
            textField.setEditable(false);
            textField.getStyleClass().add("fixed-cell"); // Classe CSS para células fixas
        }

        return textField;
    }

    @FXML
    protected void onCheckStatusClick() {
        var hasErrors = boardService.hasErrors();
        var gameStatus = boardService.getStatus();
        var message = switch (gameStatus) {
            case NOT_STARTED -> "O jogo não foi iniciado";
            case INCOMPLETE -> "O jogo está incompleto";
            case COMPLETE -> "O jogo está completo";
        };
        message += hasErrors ? " e contém erros" : " e não contém erros";

        showAlert(Alert.AlertType.INFORMATION, "Status do Jogo", message);
    }

    @FXML
    protected void onResetClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Limpar o jogo");
        alert.setHeaderText("Deseja realmente reiniciar o jogo?");
        alert.setContentText("Todo o seu progresso será perdido.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boardService.reset();
            // Atualiza a UI
            spaceToTextFieldMap.forEach((space, textField) -> {
                if (!space.isFixed()) {
                    textField.setText("");
                }
            });
        }
    }

    @FXML
    protected void onFinishClick() {
        if (boardService.gameIsFinished()) {
            showAlert(Alert.AlertType.INFORMATION, "Parabéns!", "Você concluiu o jogo!");
            setButtonsDisabled(true);
        } else {
            showAlert(Alert.AlertType.WARNING, "Jogo Incompleto", "Seu jogo tem alguma inconsistência. Ajuste e tente novamente.");
        }
    }

    private void setButtonsDisabled(boolean disabled) {
        checkStatusButton.setDisable(disabled);
        resetButton.setDisable(disabled);
        finishButton.setDisable(disabled);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}