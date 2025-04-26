//côté Client et Serveur
public class Operation {
    public String operation; // Code binaire (00, 01, 10, 11)
    public String operand1; // Binaire
    public String operand2; // Binaire
    public String result; // Binaire ou vide
    //private String checksum;  // checksum pour vérifier

    public Operation(String operation, String operand1, String operand2, String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = "";  //initialisation
        //this.checksum = checksum;
    }

    //Getters et Setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
