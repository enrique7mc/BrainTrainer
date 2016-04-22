package com.enrique7mc.braintrainer;

/**
 * Created by enrique.munguia on 19/04/2016.
 */
public class Operation {
    final int left;
    final int right;
    Operator operator;

    public Operation(int left, int right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Operation(int left, int right) {
        this.left = left;
        this.right = right;
        this.operator = Operator.SUM;
    }

    public int getResult() {
        switch (operator) {
            case SUM:
                return left + right;
            case SUBS:
                return left - right;
            case MULT:
                return left * right;
            default:
                throw new IllegalArgumentException("Not supported operation");
        }
    }

    @Override
    public String toString() {
        String op = null;
        switch (operator) {
            case SUM:
                op = "+";
                break;
            case SUBS:
                op = "-";
                break;
            case MULT:
                op = "x";
                break;
            default:
                throw new IllegalArgumentException("Not supported operation");
        }

        return String.format("%d %s %d", left, op, right);
    }
}


