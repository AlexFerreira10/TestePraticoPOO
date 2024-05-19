package entities;

import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Vendedor extends Funcionario{

    protected static final double SALARIO_FIXO = 12000.00;
    protected static final double SALARIO_VARIAVEL = 1000.00;
    protected static final double BONIFICACAO = 0.3;

    // Map com a vendas por mes
    protected Map<LocalDate,Double> relatorioVendasPorMes = new HashMap<>();

    public Vendedor(String nome, LocalDate dataContratacao) {
        super(nome, dataContratacao);
    }

    public void adicionarVenda(LocalDate data, Double valor) {
        relatorioVendasPorMes.put(data, valor);
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

            // Caso nao tenha o mes de vendas registrado, consideramos somente o salario base
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
            // Considero 0.0 os meses que não possuem vendas registradas
            return BONIFICACAO * relatorioVendasPorMes.getOrDefault(dataSalario, 0.0);
        }
        else {
            return 0.0;
        }
    }
}