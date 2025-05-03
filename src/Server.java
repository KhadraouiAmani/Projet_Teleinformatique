import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234); // ✅ Moved outside the loop
            System.out.println("serveur > Serveur en attente de connexions...");
    
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("serveur > Client connecte");
    
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
    
                    Operation received = (Operation) input.readObject();
                    if (received.getOperation().equals("exit")) {
                        System.out.println("serveur > Client a demande la fermeture du serveur.");
                        clientSocket.close();
                        serverSocket.close();
                        break; // Exit while loop
                    }
    
                    // On enleve les bits de detection pour traiter les vraies valeurs
                    String opCode = received.getOperation().substring(0, 2);
                    String op1 = received.getOperand1().substring(0, received.getOperand1().length() - 2);
                    String op2 = received.getOperand2().substring(0, received.getOperand2().length() - 2);
    
                    // on convertit en decimal pas pour calculer le resultat!!! mais juste pour
                    // afficher l'operation complete désirée!
                    int num1 = (int) Long.parseUnsignedLong(op1, 2);
                    int num2 = (int) Long.parseUnsignedLong(op2, 2);
    
                    System.out.println(
                            "serveur > Operation a faire : " + num1 + " " + BinaryOperations.opType(opCode) + " " + num2);
    
                    // Verification de parite sur le code de l'operation
                    if (!checkParity(received.getOperation())) {
                        System.out.println("serveur > Erreur detectee sur le champ operation !");
                    }
    
                    // Verification de checksum pour les operandes
                    if (!checkChecksum(received.getOperand1())) {
                        System.out.println("serveur > Erreur detectee sur l'operande 1 !");
                    }
                    if (!checkChecksum(received.getOperand2())) {
                        System.out.println("serveur > Erreur detectee sur l'operande 2 !");
                    }
    
                    String resultBinary = BinaryOperations.calculate(BinaryOperations.opType(opCode), op1, op2);
    
                    Operation response = new Operation(received.getOperation(), received.getOperand1(),
                            received.getOperand2(), resultBinary);
    
                    output.writeObject(response);
    
                    clientSocket.close();
                } catch (Exception e) {
                    System.out.println("Erreur du serveur : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'initialisation du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    // Verifie la parite (2 bits de parite à la fin du champ operation)
    public static boolean checkParity(String operationCode) {
        String code = operationCode.substring(0, 2);
        String parity = operationCode.substring(2);
        int count = 0;
        for (char c : code.toCharArray()) {
            if (c == '1')
                count++;
        }
        if (count % 2 == 0)
            return parity.equals("00");
        else
            return parity.equals("11");
    }

    // Verifie le checksum sur les 2 derniers bits
    public static boolean checkChecksum(String dataWithChecksum) {
        String data = dataWithChecksum.substring(0, dataWithChecksum.length() - 2);
        String checksum = dataWithChecksum.substring(dataWithChecksum.length() - 2);
        return checksum.equals(Client.CalculateChecksum(data)); // si c'est le meme nombre , on doit avoir le meme
    }
}
