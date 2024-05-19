package entities;

import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Secretario extends Funcionario {

    protected static final double SALARIO_FIXO = 7000.00;
    protected static final double SALARIO_VARIAVEL = 1000.00;
    protected static final double BONIFICACAO = 0.2;

    public Secretario(String nome, LocalDate dataContratacao) {
        super(nome, dataContratacao);
    }

    @Override
    public double salarioTotal(LocalDate dataSalario) {

        if(dataSalario == null) {
            throw new FalhaEntradaDados("Data de pesquisa invalida! ");
        }

        int anosTrabalhados = (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario);

        // Funcionario precisa ter sido contratado antes da dataSalario
        if(anosTrabalhados > 0) {
            double salarioBase = SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;
            return salarioBase + calculadorBonificacao(dataSalario);
        }
        else {
            return 0.0;
        }

    }

    public double calculadorBonificacao(LocalDate dataSalario) {

        if(dataSalario == null) {
            throw new FalhaEntradaDados("Data de pesquisa invalida! ");
        }

        int anosTrabalhados = (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario);
        // Funcionario precisa ter sido contratado antes da dataSalario
        if(anosTrabalhados > 0) {
            double salarioBase = SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;
            return BONIFICACAO * (SALARIO_FIXO + SALARIO_VARIAVEL * (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario) * BONIFICACAO);
        }
        else {
            return 0.0;
        }
    }
}