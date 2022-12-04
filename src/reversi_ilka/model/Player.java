package reversi_ilka.model;


public class Player {
    private String name;
    private Chip chip;
    private boolean play;

    public Player(String name, Chip chip, boolean play) {
        this.name = name;
        this.chip = chip;
        this.play = play;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public Chip getChip() {
        return chip;
    }

    void setChip(Chip chip) {
        this.chip = chip;
    }

    public boolean getPlay() {
        return play;
    }

    void setPlay(boolean play) {
        this.play = play;
    }

}
