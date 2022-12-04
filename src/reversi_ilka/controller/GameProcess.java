package reversi_ilka.controller;

import static reversi_ilka.util.ConsolePrinter.*;
import static reversi_ilka.util.Utils.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import reversi_ilka.model.Chip;
import reversi_ilka.model.Player;
import reversi_ilka.util.ConsolePrinter;
import reversi_ilka.view.Board;

public class GameProcess {

    final Player[] players;
    public static Chip playerChip = null;
    public static Chip PCChip = null;

    // Чтение входных запросов.
    BufferedReader reader;
    // Размер доски (8x8)
    private int boardSize;

    public GameProcess(boolean ifPlayerfirst) {
        // Если первый ход игрока (человека), то он ходит черными,
        // иначе белыми.
        if (ifPlayerfirst) {
            playerChip = Chip.Black;
            PCChip = Chip.White;
        } else {
            playerChip = Chip.White;
            PCChip = Chip.Black;
        }
        // Получаем информацию об игроке.
        players = getPlayers();
        // Открываем поток.
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Создаем доску.
     *
     * @param boardSize
     * @return возвращаем доску 8x8.
     */
    public Board createBoard(int boardSize) {
        this.boardSize = boardSize;
        return new Board(this.boardSize);
    }

    /**
     * Выводим начальные сообщения.
     */
    public void printStartMessage() {
        String number = fromNumbertoCircleNumber(boardSize);
        StringBuffer startMessage = new StringBuffer()
                .append("Игра начинается\n")
                .append("Вы играете " + playerChip.getState() + " фишками");
        printMessage(startMessage.toString());
        sleep(2000);
    }

    /**
     * Обработка начала игры.
     *
     * @param board Инициализируем доску.
     */
    public void startGame(Board board) {

        boolean flagPass = false;

        // Первый ход - это ход черных, корректируем значение.
        int whiteOrBlack = playerChip.isColor(Chip.White) ? 0 : 1;

        // Играем, пока кто-то не выиграет или доска не заполнится.
        while (true) {
            int columnNumber;
            int rowNumber;

            whiteOrBlack = whiteOrBlack ^ 1;
            Player player = players[whiteOrBlack];

            boolean skip = board.needsSkip(player);
            if (flagPass && skip) {
                // Если оба игрока пасуют, игра заканчивается.
                printMessage("Конец игры, нет возможности разместить фишку.");
                break;
            } else if (flagPass = skip) {
                // Пропуск хода.
                printMessage(String.format("Фишку некуда поместить, пропустите ход.", player.getName()));
            } else {
                // Если все ок.
                Chip chip = player.getChip();
                while (true) {
                    if (player.getPlay()) {
                        // Получение случайных чисел для столбцов и строк для компьютера.
                        columnNumber = getRandom(boardSize);
                        rowNumber = getRandom(boardSize);
                    } else {
                        // Запрос на ввод столбцов и строк
                        columnNumber = getInputColumnNumber(reader);
                        rowNumber = getInputRowNumber(reader);
                    }

                    // Проверка введенных данных.
                    if (columnNumber >= boardSize || rowNumber >= boardSize) {
                        printAlert("Введенное значение неверно, пожалуйста, укажите номер столбца и строки еще раз.");
                        continue;
                    }

                    // Проверяем свободна ли клетка.
                    if (!board.squareIsEmpty(columnNumber, rowNumber)) {
                        var notEmptyMessage = String.format("Клетка занята",
                                columnNumber, rowNumber, Chip.Empty.getState());
                        if (!player.getPlay()) {
                            printAlert(notEmptyMessage);
                        }
                        continue;
                    }

                    // Проверяем, можно ли разместить фишку.
                    if (!board.isSelectable(columnNumber, rowNumber, chip)) {
                        var notSelectableMessage = String.format("Данную клетку нельзя выбрать",
                                columnNumber, rowNumber);
                        if (!player.getPlay()) {
                            printAlert(notSelectableMessage);
                        }
                        continue;
                    }
                    break;
                }
                Chip tmpChip = player.getChip();

                board.putChip(columnNumber, rowNumber, tmpChip);

                board.turnOver(columnNumber, rowNumber, tmpChip);
                if (player.getPlay()) {
                    printInfo(String.format("[%s] поместил фишку на [%s %s].",
                            player.getName(), columnNumber, rowNumber, tmpChip.getDisplay()));
                }
                // Отображаем информацию.
                board.displayInfo();
            }

            sleep(1000);
            // Если доска заполнена.
            if (board.isFull()) {
                printMessage("Конец игры. Доска заполнена.");
                break;
            }
        }
    }

    /**
     * Конец игры.
     *
     * @param board
     */
    public void endGame(Board board) {
        // Пишем кто победил.
        printWinner(board);
    }

    /**
     * Запрашиваем номер столбца и извлекаем введенное число.
     *
     * @param reader
     * @return возвращаем номер столбца.
     */
    int getInputColumnNumber(BufferedReader reader) {
        return getInputNumber(reader, String.format("Введите номер столбца（0~%s）" +
                "на котором вы хотите разместить фишку.", boardSize - 1));
    }

    /**
     * Запрашиваем номер строки и извлекаем введенный номер.
     *
     * @param reader
     * @return возвращаем номер строки.
     */
    int getInputRowNumber(BufferedReader reader) {
        return getInputNumber(reader, String.format("Введите номер строки（0~%s）" +
                "на которой вы хотите разместить фишку", boardSize - 1));
    }

    private int getInputNumber(BufferedReader reader, String msg) {
        int inputValue;
        while (true) {
            String input = requestEntry(reader, msg);
            if (!checkValid(input, boardSize - 1)) {
                // Повтор ввода при некорректных значенияx.
                printAlert(String.format("Введите число（0～%s）", boardSize - 1));
                continue;
            }
            inputValue = Integer.parseInt(input);
            break;
        }
        return inputValue;
    }

    /**
     * @param number
     * @return
     */
    private boolean checkValid(String number) {
        return isValidValue(number, 4, 7);
    }

    /**
     * @param number Введенное число.
     * @param end    Допустимое значение конца диапазона,
     *               если оно больше или равно 0 и меньше или равно end.
     * @return Истинно, если находится в диапазоне.
     */
    private boolean checkValid(String number, int end) {
        return isValidValue(number, 0, end);
    }

    /**
     * Получение информации об игроке.
     *
     * @return [0] Информация об игроке, [1] Информация о компьютере.
     */
    private static Player[] getPlayers() {
        PlayerManager pm = new PlayerManager();
        return new Player[]{pm.getPlayer(), pm.getPC()};
    }

    /**
     * Количество фигур получаем из доски,
     * определяем победитель
     * и выводим результат.
     *
     * @param board
     */
    void printWinner(Board board) {

        int playerScore = 0;
        int cpuScore = 0;

        for (Chip[] chips : board.getBoard()) {
            for (Chip chip : chips) {
                if (chip.isColor(playerChip)) {
                    playerScore++;
                } else if (chip.isColor(PCChip)) {
                    cpuScore++;
                }
            }
        }
        printMessage(String.format("[%s：%s][%s очков]", players[0].getName(), playerChip.getState(), playerScore));
        printMessage(String.format("[%s：%s][%s очков]", players[1].getName(), PCChip.getState(), cpuScore));
        if (playerScore == cpuScore) {
            ConsolePrinter.changeColorToBlue(() -> printMessage("Ничья!"));
            return;
        }

        String winner = playerScore > cpuScore ? players[0].getName() : players[1].getName();
        ConsolePrinter.changeColorToRed(() -> printMessage(String.format("[%s Победил ]", winner)));
    }

    /**
     * Процесс временного приостановления обработки потока.
     */
    private static void sleep(int milliSec) {
        int stopMilliSec = milliSec;
        try {
            Thread.sleep(stopMilliSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
