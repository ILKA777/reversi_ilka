package reversi_ilka.model;

public enum Chip {

    Black("Black", "○"),
    White("White", "●"),
    Empty("Empty", "　");

    private String state;
    private String display;

    private Chip(String state, String display) {
        this.state = state;
        this.display = display;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean isEmpty() {
        return this.name().equals(Empty.name());
    }

    public boolean isColor(Chip chip) {
        return this.equals(chip);
    }
}
