import java.math.BigInteger;

public class BinaryOperations {

    public static String calculate(String operationCode, String bin1, String bin2) {
        String bnum1 = String.format("%32s", bin1).replace(' ', '0');
        String bnum2 = String.format("%32s", bin1).replace(' ', '0');
        // on va utiliser l'objet BigInteger, which will perform operations while taking
        // in considerations the two's-complement of a number, in case of a negative
        // number

        BigInteger b1 = new BigInteger(bin1, 2);// binaire = base 2
        BigInteger b2 = new BigInteger(bin2, 2);

        String result = "";
        BigInteger b;

        switch (operationCode) {
            case "+":
                b = b1.add(b2);
                break;
            case "-":
                b = b1.subtract(b2);
                break;
            case "*":
                b = b1.multiply(b2);
                break;
            case "/":
                b = (b2.compareTo(BigInteger.ZERO) == 0) ? BigInteger.ZERO : b1.divide(b2);
                break;
            default:
                b = b1.add(b2);
                break;
        }

        result = b.toString(2);
        return result;

    }

    public static String opType(String operationCode) {
        String result = "";
        switch (operationCode) {
            case "00":
                result = "+";
                break;// Addition
            case "01":
                result = "-";
                break; // Subtraction
            case "10":
                result = "*";
                break; // Multiplication
            case "11":
                result = "/";
                break;
            default:
                result = "+";
        }
        return result;
    }
}
