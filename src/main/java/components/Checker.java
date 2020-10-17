package components;

import java.util.Objects;

enum CheckerColor {
    WHITE,
    BLACK
}

/**
 * Representation of Checker
 */
public class Checker {
    /**
     * Constructor of Checker
     * @param color - side the checker belongs to
     * @param is_king - is a checker a king
     */
    Checker(CheckerColor color, boolean is_king) {
        color_ = color;
        is_king_ = is_king;
    }
    CheckerColor color_;
    boolean is_king_;

    /**
     *
     * @return String representation of Checker
     */
    @Override
    public String toString() {
        return (color_ == CheckerColor.WHITE? (is_king_? "W" : "w") : (is_king_? "B" : "b"));
    }

    /**
     *
     * @param o - Checker to compare our checker to
     * @return result of comparsion
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Checker)) return false;
        Checker checker = (Checker) o;
        return is_king_ == checker.is_king_ &&
                color_ == checker.color_;
    }
}
