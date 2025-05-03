import java.util.Random;

public class NetworkSimulator {
    // Introduire a network error aléatoirement
    public static Operation sendThroughNetwork(Operation operation) {
        Random random = new Random();
        int errorChance = random.nextInt(100);

        if (errorChance < 30) { // 30% chance d'erreur
            int field = random.nextInt(3); // Choisir un champ au hasard
            if (field == 0)
                operation = InverserBit(operation, "operation");
            else if (field == 1)
                operation = InverserBit(operation, "operand1");
            else
                operation = InverserBit(operation, "operand2");
        }

        return operation;
    }

    private static Operation InverserBit(Operation operation, String field) {
        String data = "";
        // data c le champ ou on desire introduire l'erreur
        if (field.equals("operation")) {
            data = operation.getOperation();
        } else if (field.equals("operand1")) {
            data = operation.getOperand1();
        } else if (field.equals("operand2")) {
            data = operation.getOperand2();
        }

        Random rand = new Random();
        int BitPos = rand.nextInt(data.length() - 2);
        // rand.nextInt(x) → génère un nombre aléatoire entre 0 et x-1.
        // Ici, data.length() est la longueur de la chaîne binaire (par exemple
        // "1010110011" a 10 caractères).
        // On fait data.length() - 2 car on ne veut PAS toucher aux 2 derniers bits (ils
        // contiennent le checksum).

        char[] bits = data.toCharArray();
        // On utilise .toCharArray() pour transformer la chaîne en un tableau de
        // caractères (char[]).
        // Pourquoi ?
        // Parce qu'on ne peut pas modifier directement un String en Java (il est
        // immuable).
        // En tableau char[], on peut facilement changer un bit.

        bits[BitPos] = (bits[BitPos] == '0') ? '1' : '0'; // C'est une opération ternaire (condition ? siOui : siNon).
        // On inverse le bit choisi au hasard (bitPos) :
        // Si le bit est '0', alors on le transforme en '1'.
        // Sinon (il est '1'), on le transforme en '0'.

        // Reconstruire l'objet Operation
        if (field.equals("operation"))
            operation = new Operation(new String(bits), operation.getOperand1(), operation.getOperand2(), "");
        if (field.equals("operand1"))
            operation = new Operation(operation.getOperation(), new String(bits), operation.getOperand2(), "");
        if (field.equals("operand2"))
            operation = new Operation(operation.getOperation(), operation.getOperand1(), new String(bits), "");

        return operation;
    }
}
