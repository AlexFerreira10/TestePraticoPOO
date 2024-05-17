package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Secretario extends Funcionario {

    private static final double SALARIO_FIXO = 7000.00;
    private static final double SALARIO_VARIAVEL = 1000.00;
    private static final double BONIFICACAO = 0.2;

    public Secretario(String nome, LocalDate dataContratacao) {
        super(nome, dataContratacao);
    }

    @Override
    public double salarioTotal(LocalDate dataSalario) {
        int anosTrabalhados = (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario);
        double salarioBase = SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;
        return salarioBase + (salarioBase * BONIFICACAO);
    }
}
