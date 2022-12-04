package reversi_ilka.view;

import reversi_ilka.model.Chip;
import reversi_ilka.model.Player;

import static reversi_ilka.controller.GameProcess.*;
import static reversi_ilka.util.Utils.*;

import java.util.StringJoiner;

public class Board {
    private Chip[][] board;
    // Размерность доски.
    private int boardSize;

    /**
     * Конструктор создания доски.
     * Есть задел под создание доски другого размера,
     * отличающегося от 8x8.
     */
    public Board(int boardSize) {
        this.boardSize = boardSize;

        this.board = new Chip[boardSize][boardSize];
        for (Chip[] row : board) {
            for (int index = 0; index < row.length; index++) {
                row[index] = Chip.Empty;
            }
        }
    }

    /**
     * Инициализация доски.
     */
    public void init() {
        // Определяем центр доски и размещаем фишки.
        int centerSquare = (boardSize / 2) - 1;
        board[centerSquare][centerSquare] = PCChip;
        board[centerSquare + 1][centerSquare + 1] = PCChip;
        board[centerSquare + 1][centerSquare] = playerChip;
        board[centerSquare][centerSquare + 1] = playerChip;
    }

    /**
     * Отрисовка доски и фишек.
     */
    public void displayInfo() {
        String border = "  --";
        for (int i = 0; i < boardSize; i++) {
            border += "-----";
        }
        border += "---";

        String verticalLine = "｜";

        printMessage(border);
        {
            StringJoiner joiner = new StringJoiner(verticalLine, verticalLine, verticalLine);
            joiner.add("　");
            for (int i = 0; i < boardSize; i++) {
                joiner.add(fromNumbertoCircleNumber(i));
            }
            printMessage(joiner.toString());
        }
        {
            int count = 0;
            for (Chip[] chips : board) {
                StringJoiner joiner = new StringJoiner(verticalLine, verticalLine, verticalLine);
                joiner.add(fromNumbertoCircleNumber(count++));
                for (Chip chip : chips) {
                    joiner.add(chip.getDisplay());
                }
                printMessage(joiner.toString());
            }
        }
        printMessage(border);
    }

    /**
     * @param column
     * @param row
     * @return
     */
    public boolean squareIsEmpty(int column, int row) {
        return this.board[row][column].isEmpty();
    }

    /**
     * @param column
     * @param row
     * @param chip
     * @return
     */
    public boolean isSelectable(int column, int row, Chip chip) {
        // Фишка противника.
        Chip opponentsChip = chip.isColor(playerChip) ? PCChip : playerChip;

        // Расстановка фишек.
        if (checkLeft(column, row, opponentsChip)
                || checkRight(column, row, opponentsChip)
                || checkCenter(column, row, opponentsChip)) {
            if (canTurnOver(column, row, opponentsChip)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeft(int column, int row, Chip opponentsChip) {
        boolean isSelectable = false;
        int leftColumn = column - 1;
        if (leftColumn < 0) {
            return isSelectable;
        }
        // Есть ли фигура противника в одной из левых колонок?
        if (row > 0 && board[row - 1][leftColumn].isColor(opponentsChip)) {
            isSelectable = true;
        }
        if (board[row][leftColumn].isColor(opponentsChip)) {
            isSelectable = true;
        }
        if (row + 1 < boardSize && board[row + 1][leftColumn].isColor(opponentsChip)) {
            isSelectable = true;
        }
        return isSelectable;
    }

    private boolean checkRight(int column, int row, Chip opponentsChip) {
        boolean isSelectable = false;
        int rightColumn = column + 1;
        if (rightColumn > board.length - 1) {
            return isSelectable;
        }
        // Есть ли фигура противника в одной из правых колонок?
        if (row > 0 && board[row - 1][rightColumn].isColor(opponentsChip)) {
            isSelectable = true;
        }
        if (board[row][rightColumn].isColor(opponentsChip)) {
            isSelectable = true;
        }
        if (row + 1 < boardSize && board[row + 1][rightColumn].isColor(opponentsChip)) {
            isSelectable = true;
        }
        return isSelectable;
    }

    private boolean checkCenter(int column, int row, Chip opponentsChip) {

        boolean isSelectable = false;
        if ((row - 1) >= 0 && board[row - 1][column].isColor(opponentsChip)) {
            isSelectable = true;
        }
        if (row < board.length - 1 && board[row + 1][column].isColor(opponentsChip)) {
            isSelectable = true;
        }
        return isSelectable;
    }

    /**
     * @param column
     * @param row
     * @param opponentsChip
     * @return
     */
    boolean canTurnOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        if (
            // Проверяем вверх.
                upperDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем низ.
                        || lowerDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем лево.
                        || leftDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем право.
                        || rightDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем верхние левые диагональные.
                        || upperLeftDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем верхние правые диагональные.
                        || upperRightDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем нижние левые диагональные.
                        || lowerLeftDirectionTurnedOver(column, row, opponentsChip)
                        // Проверяем нижние правые диагональные.
                        || lowerRightDirectionTurnedOver(column, row, opponentsChip)) {
            canTurn = true;
        }

        return canTurn;
    }

    boolean upperDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurnOver = false;
        boolean turnOverFlag = false;
        boolean firstSkip = true;

        for (int index = row; index >= 0; index--) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[index][column].isColor(opponentsChip)) {
                turnOverFlag = true;
                continue;
            } else {
                if (!board[index][column].isEmpty() && turnOverFlag) {
                    canTurnOver = true;
                }
                break;
            }
        }
        return canTurnOver;
    }

    boolean lowerDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int index = row; index < boardSize; index++) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[index][column].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[index][column].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    boolean leftDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int index = column; index > 0; index--) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[row][index].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[row][index].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    boolean rightDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int index = column; index < boardSize; index++) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[row][index].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[row][index].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    boolean upperLeftDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int i = 0; column - i >= 0 && row - i >= 0; i++) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[row - i][column - i].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[row - i][column - i].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    boolean upperRightDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int i = 0; column + i < boardSize && row - i > 0; i++) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[row - i][column + i].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[row - i][column + i].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    boolean lowerLeftDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int i = 0; column - i >= 0 && row + i < boardSize; i++) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[row + i][column - i].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[row + i][column - i].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    boolean lowerRightDirectionTurnedOver(int column, int row, Chip opponentsChip) {
        boolean canTurn = false;
        boolean turnFlag = false;
        boolean firstSkip = true;

        for (int i = 0; column + i < boardSize && row + i < boardSize; i++) {
            if (firstSkip) {
                firstSkip = false;
                continue;
            }
            if (board[row + i][column + i].isColor(opponentsChip)) {
                turnFlag = true;
                continue;
            } else {
                if (!board[row + i][column + i].isEmpty() && turnFlag) {
                    canTurn = true;
                }
                break;
            }
        }
        return canTurn;
    }

    /**
     * @param column
     * @param row
     * @param chip
     */
    public void putChip(int column, int row, Chip chip) {
        board[row][column] = chip;
    }

    public void turnOver(int column, int row, Chip chip) {
        Chip opponent = chip.isColor(playerChip) ? PCChip : playerChip;
        if (upperDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row - i][column].isColor(opponent)) {
                    board[row - i][column] = chip;
                } else {
                    break;
                }
            }
        }
        if (lowerDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row + i][column].isColor(opponent)) {
                    board[row + i][column] = chip;
                } else {
                    break;
                }
            }
        }
        if (leftDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row][column - i].isColor(opponent)) {
                    board[row][column - i] = chip;
                } else {
                    break;
                }
            }
        }
        if (rightDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row][column + i].isColor(opponent)) {
                    board[row][column + i] = chip;
                } else {
                    break;
                }
            }
        }
        if (upperLeftDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row - i][column - i].isColor(opponent)) {
                    board[row - i][column - i] = chip;
                } else {
                    break;
                }
            }
        }
        if (upperRightDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row - i][column + i].isColor(opponent)) {
                    board[row - i][column + i] = chip;
                } else {
                    break;
                }
            }
        }
        if (lowerLeftDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row + i][column - i].isColor(opponent)) {
                    board[row + i][column - i] = chip;
                } else {
                    break;
                }
            }
        }
        if (lowerRightDirectionTurnedOver(column, row, opponent)) {
            for (int i = 1; i < boardSize; i++) {
                if (board[row + i][column + i].isColor(opponent)) {
                    board[row + i][column + i] = chip;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * @param player
     * @return
     */
    public boolean needsSkip(Player player) {
        boolean isSkip = true;
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (!squareIsEmpty(column, row)) {
                    continue;
                }
                if (isSelectable(column, row, player.getChip())) {
                    isSkip = false;
                    break;
                }
            }
        }
        return isSkip;
    }

    /**
     * Если доска заполнилась.
     * @return
     */
    public boolean isFull() {
        for (Chip[] chips : board) {
            for (Chip chip : chips) {
                if (chip.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Chip[][] getBoard() {
        return board;
    }

    void setBoard(Chip[][] board) {
        this.board = board;
    }

    int getBoardSize() {
        return boardSize;
    }

    void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

}
