package components;

/**
 * Representation of Checker
 */
public class Checker {
    /**
     * Constructor of Checker
     * @param color - side the checker belongs to
     * @param isKing - is a checker a king
     */
    public Checker(CheckerColor color, boolean isKing) {
        this.color = color;
        this.isKing = isKing;
    }
    private CheckerColor color;
    private boolean isKing;

    /**
     *
     * @return String representation of Checker
     */
    @Override
    public String toString() {
        return (color == CheckerColor.WHITE? (isKing ? "W" : "w") : (isKing ? "B" : "b"));
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
        return isKing == checker.isKing &&
                color == checker.color;
    }

    public CheckerColor getColor() {
        return color;
    }

    public void setColor(CheckerColor color) {
        this.color = color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }
}
