module br.com.dio.sudoku {
    requires javafx.controls;
    requires javafx.fxml;

    // Mantendo as dependências de UI que você já usava
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    // Lombok é uma dependência de compilação, então 'static' é apropriado
    requires static lombok;
    requires java.desktop;

    opens br.com.dio.sudoku to javafx.fxml;
    exports br.com.dio.sudoku;
    exports br.com.dio.sudoku.model;
    opens br.com.dio.sudoku.model to javafx.fxml;
}