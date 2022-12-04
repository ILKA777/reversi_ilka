package reversi_ilka;

import java.util.Random;
import reversi_ilka.view.Board;
import reversi_ilka.controller.GameProcess;

public class MainApplication {
    public static void main(String[] args) {
        // Первый/второй ход определяется случайным образом.
        boolean isPlayerFirst = new Random().nextBoolean();

        GameProcess gameProcess = new GameProcess(isPlayerFirst);

        // Количество квадратов по вертикали (горизонтали), у нас игра 8x8,
        // поэтому 8.
        int boardSquareCount = 8;

        // Создаем доску.
        Board board = gameProcess.createBoard(boardSquareCount);

        // Начальное сообщение.
        gameProcess.printStartMessage();

        // Инициализация доски.
        board.init();

        // Отображение доски.
        board.displayInfo();

        // Начинаем игру.
        gameProcess.startGame(board);

        // Конец игры.
        gameProcess.endGame(board);

    }
}
