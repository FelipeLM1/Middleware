package ufrn.model;


public class Historico {
    private String data;
    private String tipo;
    private double valor;

    public Historico(String data, String tipo, double valor) {
        this.data = data;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\tR$ %.2f", getData(), getTipo(), getValor());
    }
}
