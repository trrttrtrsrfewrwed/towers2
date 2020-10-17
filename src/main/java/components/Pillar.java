package components;

import java.util.ArrayDeque;
import java.util.Objects;

/**
 * Representation of Pillar in Pillar Russian checkers
 */
public class Pillar {
    private ArrayDeque<Checker> checkers = new ArrayDeque<Checker>();

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
        return "{" + checkers +
                "top}";
    }

    /**
     * Answers if Pillar is a king or not
     * @return answer ^
     */
    public boolean isKing() {
        return checkers.getLast().is_king_;
    }

    /**
     *
     * @return color of the Pillar
     */
    public CheckerColor getColor() {
        return checkers.getLast().color_;
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

