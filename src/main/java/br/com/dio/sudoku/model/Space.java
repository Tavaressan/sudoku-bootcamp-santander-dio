package br.com.dio.sudoku.model;

import lombok.Getter;

@Getter
public class Space {
    private Integer actual;
    private final boolean fixed;
    private final int expected;

    public Space(int expected, boolean fixed) {
        this.fixed = fixed;
        this.expected = expected;
        if (fixed) {
            actual = expected;
        }
    }


    public void setActual(Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }

    public void clearSpace() {
        setActual(null);
    }
}
