package entities;

import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Funcionario {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    public static double folhaPagamentoTotal(List<Funcionario> lista, String mes, String ano) {
        if(lista == null || mes == null || ano == null || mes.length() != 2 || ano.length() != 4) {
            throw new FalhaEntradaDados("Entrada de dados inválida");
        }

        LocalDate data = conversorStringLocalDate(mes, ano);

        return lista.stream()
                .filter(funcionario -> funcionario.getDataContratacao().isBefore(data))
                .mapToDouble(funcionario -> funcionario.salarioTotal(data))
                .sum();
    }

    public static LocalDate conversorStringLocalDate(String mes, String ano) {
        String dataString = "01/" + mes + "/" + ano;
        return  LocalDate.parse(dataString, dtf);
    }
}
