package ufrn.model;

public class Deposito {
    String data;
    double valor = -1;

    public Deposito(String data, double valor) {
        this.data = data;
        this.valor = valor;
    }

    public double valor() {
        return valor;
    }

    public String data() {
        return data;
    }

    @Override
    public String toString() {
        return data() + "\tDeposito\tR$ " + valor();
    }
}
