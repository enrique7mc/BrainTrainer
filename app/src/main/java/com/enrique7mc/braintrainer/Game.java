package com.enrique7mc.braintrainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enrique.munguia on 01/05/2016.
 */
public class Game {
    private int correctAnwers;
    private int currentIndex;
    private Operation currentOperation;
    private List<Operation> operationList;

    public Game() {
        operationList = new ArrayList<>();
        currentIndex = 0;
    }

    public Operation generateNextOperation() {
        currentOperation = Operation.generateRandomOperation();
        operationList.add(currentOperation);
        return currentOperation;
    }

    public Operation getCurrentOperation() {
        if(currentOperation == null) {
            throw new UnsupportedOperationException("Operation not generated");
        }

        return currentOperation;
    }

    public boolean isCorrectAnswer(int answer) {
        if (answer == currentOperation.getResult()) {
            correctAnwers++;
            return true;
        }

        return false;
    }

    public int getCorrectAnwers() {
        return correctAnwers;
    }

    public int getTotalQuestions() {
        return operationList.size();
    }

    public List<Operation> getOperationList() {
        return operationList;
    }
}
