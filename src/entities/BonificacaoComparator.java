package entities;

import java.util.Comparator;

public class BonificacaoComparator implements Comparator<Funcionario> {

    private String mes;
    private String ano;

    public BonificacaoComparator(String mes, String ano) {
        this.mes = mes;
        this.ano = ano;
    }

    @Override
    public int compare(Funcionario f1, Funcionario f2) {

        double bonificacaoF1;
        double bonificacaoF2;

        if(f1 instanceof Vendedor) {
            bonificacaoF1 = ((Vendedor) f1).calculadorBonificacao(Funcionario.conversorStringLocalDate(mes,ano));
        } else {
            bonificacaoF1 = ((Secretario) f1).calculadorBonificacao(Funcionario.conversorStringLocalDate(mes,ano));
        }
        if(f2 instanceof Vendedor) {
            bonificacaoF2 = ((Vendedor) f2).calculadorBonificacao(Funcionario.conversorStringLocalDate(mes,ano));
        } else {
            bonificacaoF2 = ((Secretario) f2).calculadorBonificacao(Funcionario.conversorStringLocalDate(mes,ano));
        }

        return Double.compare(bonificacaoF1, bonificacaoF2);
    }
}
