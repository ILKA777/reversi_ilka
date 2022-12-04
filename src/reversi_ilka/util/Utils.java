package reversi_ilka.util;

final public class Utils {

    private Utils() {
    }

    /**
     * Дизайнерим число в число в кружочке.
     *
     * @param number
     * @return example : 1 -> ①、 15 -> ⑮
     * Название метода крутое,да :)
     */
    public static String fromNumbertoCircleNumber(int number) {
        String numberInCircle;
        switch (number) {
            case 0:
                numberInCircle = "⓪";
                break;
            case 1:
                numberInCircle = "①";
                break;
            case 2:
                numberInCircle = "②";
                break;
            case 3:
                numberInCircle = "③";
                break;
            case 4:
                numberInCircle = "④";
                break;
            case 5:
                numberInCircle = "⑤";
                break;
            case 6:
                numberInCircle = "⑥";
                break;
            case 7:
                numberInCircle = "⑦";
                break;
            default:
                numberInCircle = "×";
        }
        return numberInCircle;
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Получаем случайные числа меньшие maxNumber
     *
     * @param maxNumber
     * @return возвращаем целое число больше или равноe 0 и меньше или равноe maxNumber.
     */
    public static int getRandom(int maxNumber) {
        return (int) (Math.random() * (maxNumber));
    }


    /**
     * Проверяем подходит ли введенный номер строки и столбца.
     * Возвращаем false, если число находится вне указанного диапазона.
     * Возвращает false, если во время преобразования произошла ошибка (значение не Int).
     *
     * @param number
     * @param start
     * @param end
     * @return Возвращаем true, если значением нам подходит.
     */
    public static boolean isValidValue(String number, int start, int end) {
        int inputValue = 0;
        try {
            inputValue = Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return (inputValue >= start) && (inputValue <= end);
    }

}
