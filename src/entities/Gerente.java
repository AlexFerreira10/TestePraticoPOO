package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Gerente extends Funcionario{

    private static final double SALARIO_FIXO = 20000.00;
    private static final double SALARIO_VARIAVEL = 3000.00;

    public Gerente(String nome, LocalDate dataContratacao) {
        super(nome, dataContratacao);
    }

    // Não possui bonificação
    @Override
    public double salarioTotal(LocalDate dataSalario) {
        int anosTrabalhados = (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario);
        double salarioBase = SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;
        return salarioBase;
    }
}
