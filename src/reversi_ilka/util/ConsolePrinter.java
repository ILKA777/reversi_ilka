package reversi_ilka.util;

import static reversi_ilka.util.Utils.*;

import java.io.BufferedReader;
import java.io.IOException;

final public class ConsolePrinter {


    public ConsolePrinter() {
    }

    /**
     * Сообщение с просьбой ввести значения.
     *
     * @param reader
     * @param message
     * @return возвращаем введенное значение.
     */
    public static String requestEntry(BufferedReader reader, String message) {
        printGuidance(message);
        String input = "";
        try {
            input = reader.readLine();
        } catch (IOException except) {
            except.printStackTrace();
        }
        return input;
    }

    /**
     * Предупреждение.
     *
     * @param message
     */
    public static void printAlert(String message) {
        changeColorToRed(() -> printMessage(message));
    }

    /**
     * @param message
     */
    public static void printGuidance(String message) {
        changeColorToBlue(() -> printMessage(message));
    }

    /**
     * @param message
     */
    public static void printInfo(String message) {
        changeColorToBlue(() -> printMessage(message));
    }

    /**
     * Изменение цвета текста на красный при ошибке.
     *
     * @param func
     */
    public static void changeColorToRed(IExFunction func) {
        System.out.print((char) 27 + "[31m");
        func.execute();
        System.out.print((char) 27 + "[00m");
    }

    /**
     * Изменение цвета текста на синий при запросе.
     *
     * @param func
     */
    public static void changeColorToBlue(IExFunction func) {
        System.out.print((char) 27 + "[34m");
        func.execute();
        System.out.print((char) 27 + "[00m");
    }

    @FunctionalInterface
    public interface IExFunction {
        void execute();
    }

}
