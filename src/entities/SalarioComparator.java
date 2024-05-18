package entities;

import java.util.Comparator;


public class SalarioComparator implements Comparator<Funcionario> {

    private String mes;
    private String ano;

    public SalarioComparator(String mes, String ano) {
        this.mes = mes;
        this.ano = ano;
    }

    @Override
    public int compare(Funcionario f1, Funcionario f2) {

        double salarioTotalF1 = f1.salarioTotal(Funcionario.conversorStringLocalDate(mes,ano));
        double salarioTotalF2 = f2.salarioTotal(Funcionario.conversorStringLocalDate(mes,ano));

        return Double.compare(salarioTotalF1, salarioTotalF2);
    }
}