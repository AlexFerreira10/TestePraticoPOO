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
        double salarioBase = SALARIO_FIXO + SALARIO_VARIAVEL * anosTrabalhados;

        // Caso nao tenha o mes de vdendas registrado, consideramos 0
        return salarioBase + calculadorBonificacao(dataSalario);
    }

    public double calculadorBonificacao(LocalDate dataSalario) {
        return BONIFICACAO * relatorioVendasPorMes.getOrDefault(dataSalario, 0.0);
    }
}