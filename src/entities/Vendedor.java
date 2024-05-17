package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Vendedor extends Funcionario{

    private static final double SALARIO_FIXO = 12000.00;
    private static final double SALARIO_VARIAVEL = 1000.00;
    private static final double BONIFICACAO = 0.2;

    Map<LocalDate,Double> relatorioVendasPorMes = new HashMap<>();

    public Vendedor(String nome, LocalDate dataContratacao) {
        super(nome, dataContratacao);
    }

    @Override
    public double salarioTotal(LocalDate dataSalario) {
        int anosTrabalhados = (int) ChronoUnit.YEARS.between(dataContratacao, dataSalario);
        double salarioBase = SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;
        return salarioBase + relatorioVendasPorMes.get(dataSalario) * BONIFICACAO;
    }
}
