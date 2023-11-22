package ufrn.model;

import java.util.ArrayList;
import java.util.List;

public class ContaCorrente {

    private Long id;
    private String nome;
    private double saldo = 0;
    private List<Historico> historico = new ArrayList<>();

    public ContaCorrente(String nome) {
        this.nome = nome;
        this.saldo = 0;
        this.historico = new ArrayList<>();
    }

    public String extrato() {
        return String.format("Conta de %s\n" +
                        "Saldo Inicial R$ %.2f\n" +
                        "Saldo Final R$ %.2f\n" +
                        "%s",
                getNome(),
                historico.isEmpty() ? 0 : historico.get(0).getValor(),
                getSaldo(),
                getHistorico()
        );
    }

    public List<Historico> getHistorico() {
        return this.historico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo += saldo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
