package br.com.dio.sudoku.ui.custom.button;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class FinishedGameButton extends JButton {
    public FinishedGameButton(final ActionListener actionListener){
        this.setText("Concluir jogo");
        this.addActionListener(actionListener);
    }
}
