import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        while (true) {

            try {
                Scanner scanner = new Scanner(System.in);

                Socket socket = new Socket("localhost", 1234);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                //
                System.out.println("==== Client Calculatrice Binaire ====");
                System.out.println("client > Entrez l'une des operations suivantes (+, -, *, /) : ");
                String operation = scanner.nextLine();

                //
                if (operation.equalsIgnoreCase("exit")) {
                    Operation exitOp = new Operation("exit", "", "", "");
                    output.writeObject(exitOp); // informer le serveur 
                    System.out.println("client > Deconnexion...");
                    break;
                }

                //
                System.out.println("client > Entrez l'operande 1 (decimal) : ");
                int op1 = scanner.nextInt();

                System.out.println("client > Entrez l'operande 2 (decimal) : ");
                int op2 = scanner.nextInt();

                String operationCode = EncodageOperation(operation); // transformation de l'operation en binaire
                String operand1Binary = Integer.toBinaryString(op1); // transformation de des operandes en binaire
                String operand2Binary = Integer.toBinaryString(op2);

                // Pour l'operation , je vais juste ajouter un bit de parite
                String BitParite = AjoutBitParite(operationCode);

                // Pour les operandes je vais utiliser la methode de checksum
                // Checksum doit etre fait pour chaque variable a part ou pour l'ensemble ?
                // Ici je vais faire Checksum pour chaque variable a part
                String checksum1 = CalculateChecksum(operand1Binary);
                String checksum2 = CalculateChecksum(operand2Binary);

                Operation operationToSend = new Operation(operationCode + BitParite, operand1Binary + checksum1,
                        operand2Binary + checksum2, "");
                operationToSend = NetworkSimulator.sendThroughNetwork(operationToSend); // Peut introduire erreur

                output.writeObject(operationToSend);

                Operation reponse = (Operation) input.readObject();
                BigInteger resultDecimal = new BigInteger(reponse.getResult(), 2);

                // response.getResult() → recupere une chaîne binaire.

                System.out.println("client > Le resultat de l'operation est : " + resultDecimal);

                // Fermer les flux et la socket
                socket.close();

            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public static String EncodageOperation(String operation) {
        switch (operation) {
            case "+":
                return "00";
            case "-":
                return "01";
            case "*":
                return "10";
            case "/":
                return "11";
            default:
                return "00"; // par defaut
        }
    }

    public static String AjoutBitParite(String operationCode) {
        int count = 0;
        for (int i = 0; i < operationCode.length(); i++) {
            if (operationCode.charAt(i) == '1') {
                count++;
            }
        }
        if (count % 2 == 0) { // Si le nombre de bits à 1 est pair on ajoute bit=0
            return "00";
        } else { // Si le nombre de bits à 1 est impair on ajoute bit=1
            return "11";
        }
    }

    public static String CalculateChecksum(String data) {
        int taille_mi = 2;
        if (data.length() % 2 != 0) {
            data = "0" + data; // Ajout d'un 0 pour rendre la longueur paire
        }
        int somme = 0;
        for (int i = 0; i < data.length(); i += 2) {
            String deuxBits = "" + data.charAt(i) + data.charAt(i + 1); // Regroupe 2 bits en une chaîne
            int valeur = Integer.parseInt(deuxBits, 2);// Conversion binaire vers decimal
            somme += valeur; // addition decimale
        }
        // On garde uniquement les 2 derniers bits car on a fait mi sur 2 bits (0x03 =
        // 00000011)
        somme = somme & 0x03;
        // Faire le complement à 1 sur 2 bits (inverser 2 bits) (^ = xor)
        int checksum = somme ^ 0x03;
        // Convertir le checksum en binaire sur 2 bits
        return String.format("%2s", Integer.toBinaryString(checksum)).replace(' ', '0');

    }
}
