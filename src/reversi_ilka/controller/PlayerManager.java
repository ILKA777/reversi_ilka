package reversi_ilka.controller;

import reversi_ilka.model.Player;

import static reversi_ilka.controller.GameProcess.*;

public class PlayerManager {

    Player player;
    Player PC;

    PlayerManager() {
        player = createPlayer("Игрок");
        PC = createPC("Компьютер");
    }

    Player createPlayer(String name) {
        player = new Player(name, playerChip, false);
        return player;
    }

    Player createPC(String name) {
        Player cpu = new Player(name, PCChip, true);
        return cpu;
    }

    Player getPlayer() {
        return player;
    }

    void setPlayer(Player player) {
        this.player = player;
    }

    Player getPC() {
        return PC;
    }

    void setPC(Player PC) {
        this.PC = PC;
    }
}
