package entities;

import java.time.LocalDate;

public abstract class Funcionario {

    protected String nome;
    protected LocalDate dataContratacao;

    public Funcionario(String nome, LocalDate dataContratacao) {
        this.nome = nome;
        this.dataContratacao = dataContratacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public abstract double salarioTotal(LocalDate dataSalario);
}
