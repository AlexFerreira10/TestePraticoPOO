package entities;

import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Gerente extends Funcionario{

    protected static final double SALARIO_FIXO = 20000.00;
    protected static final double SALARIO_VARIAVEL = 3000.00;

    public Gerente(String nome, LocalDate dataContratacao) {
        super(nome, dataContratacao);
    }

    // Não possui bonificação
    @Override
    public double salarioTotal(LocalDate dataSalario) {

        if(dataSalario == null) {
            throw new FalhaEntradaDados("Data de pesquisa invalida! ");
        }

        int anosTrabalhados = (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario);
        // Funcionario precisa ter sido contratado antes da dataSalario
        if(anosTrabalhados > 0) {
            return SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;
        }
        else {
            return 0.0;
        }
    }
}
