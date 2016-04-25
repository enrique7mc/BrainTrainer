package com.enrique7mc.braintrainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by enrique.munguia on 19/04/2016.
 */
public class Operation {
    final int left;
    final int right;
    Operator operator;

    private static final Random random = new Random();
    private static final Operator[] operators = Operator.values();

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

    public static Operation generateOperation() {
        int left = random.nextInt(20);
        int right = random.nextInt(20);
        int operatorIndex = random.nextInt(3);
        Operation op = new Operation(left, right, operators[operatorIndex]);
        return op;
    }

    public Integer[] generateAnswers() {
        int result = this.getResult();
        List<Integer> answers = new ArrayList<>();
        answers.add(result);

        while(answers.size() < 4) {
            boolean isPositive = random.nextBoolean();
            int offset = random.nextInt(Math.abs(result) + 4);
            int answer = isPositive ? result + offset : result - offset;
            if (!answers.contains(answer)) {
                answers.add(answer);
            }
        }

        Collections.shuffle(answers);
        return answers.toArray(new Integer[answers.size()]);
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


