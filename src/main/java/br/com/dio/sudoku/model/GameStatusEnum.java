package br.com.dio.sudoku.model;

import lombok.Getter;

public enum GameStatusEnum {


    NOT_STARTED("não iniciado"),
    INCOMPLETE("incompleto"),
    COMPLETE("completo");

    @Getter
    private String label;

    GameStatusEnum(final String label){
        this.label = label;
    }
}
