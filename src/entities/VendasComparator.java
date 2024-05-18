package entities;

import java.util.Comparator;

public class VendasComparator implements Comparator<Funcionario> {

    private String mes;
    private String ano;

    public VendasComparator(String mes, String ano) {
        this.mes = mes;
        this.ano = ano;
    }

    @Override
    public int compare(Funcionario f1, Funcionario f2) {

        double vendedor1 = ((Vendedor) f1).relatorioVendasPorMes.get(Funcionario.conversorStringLocalDate(mes,ano));
        double vendedor2 = ((Vendedor) f2).relatorioVendasPorMes.get(Funcionario.conversorStringLocalDate(mes,ano));

        return Double.compare(vendedor1, vendedor2);
    }
}
