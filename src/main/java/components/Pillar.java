package components;

import java.util.ArrayDeque;

/**
 * Representation of Pillar in Pillar Russian checkers
 */
public class Pillar {
    private ArrayDeque<Checker> checkers = new ArrayDeque<Checker>();

    /**
     * Changes checker on the top of the pillar to king
     */
    public void changeToKing() {
        Checker last = checkers.removeLast();
        last.setKing(true);
        checkers.addLast(last);
    }
    /**
     * Answers if this Pillar is empty or not
     * @return answer ^
     */
    public boolean isEmpty() {
        return checkers.isEmpty();
    }
    /**
     * Removes checker from the top of the Pillar
     * @return felled checker
     */
    public Checker cutDown() {
        return checkers.pollLast();
    }

    /**
     * Puts a checker under the bottom of the Pillar
     * @param checker - Checker to be added
     */
    public void pickUp(Checker checker) {
        checkers.addFirst(checker);
    }

    /**
     *
     * @return String representation of Pillar
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Checker checker: checkers) {
            str.append(checker);
        }
        return str.reverse().toString();
    }

    /**
     * Answers if Pillar is a king or not
     * @return answer ^
     */
    public boolean isKing() {
        return checkers.getLast().isKing();
    }

    /**
     *
     * @return color of the Pillar
     */
    public CheckerColor getColor() {
        return checkers.getLast().getColor();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pillar)) return false;
        Pillar pillar = (Pillar) o;
        ArrayDeque<Checker> copy = pillar.checkers.clone();
        if (copy.size() != checkers.size()) return false;
        for (Checker checker: checkers) {
            if (!copy.pollFirst().equals(checker)) {
                return false;
            }
        }
        return true;
    }
}

