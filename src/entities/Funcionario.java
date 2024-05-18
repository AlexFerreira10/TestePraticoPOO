package entities;

import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public abstract class Funcionario {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected String nome;
    protected LocalDate dataContratacao;

    public Funcionario(String nome, LocalDate dataContratacao) {
        this.nome = nome;
        this.dataContratacao = dataContratacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public abstract double salarioTotal(LocalDate dataSalario);

    public static double folhaPagamentoTotal(List<Funcionario> lista, String mes, String ano) {
        if(lista == null || mes == null || ano == null || mes.length() != 2 || ano.length() != 4) {
            throw new FalhaEntradaDados("Entrada de dados inválida");
        }
        LocalDate data = conversorStringLocalDate(mes, ano);
        if(data.isBefore(LocalDate.parse("01/12/2021",dtf)) || LocalDate.parse("01/04/2022",dtf).isBefore(data)) {
            System.out.println("Aviso: Nesta data não há registro de venda dos Vendedores");
        }
        return lista.stream()
                .filter(funcionario -> funcionario.getDataContratacao().isBefore(data))
                .mapToDouble(funcionario -> funcionario.salarioTotal(data))
                .sum();
    }

    public static double folhaPagamentoSalario(List<Funcionario> lista, String mes, String ano) {
        if(lista == null || mes == null || ano == null || mes.length() != 2 || ano.length() != 4) {
            throw new FalhaEntradaDados("Entrada de dados inválida");
        }
        LocalDate data = conversorStringLocalDate(mes, ano);
        if(data.isBefore(LocalDate.parse("01/12/2021",dtf)) || LocalDate.parse("01/04/2022",dtf).isBefore(data)) {
            System.out.println("Aviso: Nesta data não há registro de venda dos Vendedores");
        }

        double soma = 0.0;
        for(Funcionario funcionario: lista) {
            int anosTrabalhados = (int) ChronoUnit.YEARS.between(funcionario.dataContratacao, conversorStringLocalDate(mes, ano));
            if(funcionario instanceof Vendedor) {
                soma += Vendedor.SALARIO_FIXO + Vendedor.SALARIO_VARIAVEL * anosTrabalhados;
            }
            if(funcionario instanceof Secretario) {
                soma += Secretario.SALARIO_FIXO + Secretario.SALARIO_VARIAVEL * anosTrabalhados;
            }
            if(funcionario instanceof Gerente) {
                soma += Gerente.SALARIO_FIXO + Gerente.SALARIO_VARIAVEL * anosTrabalhados;
            }
        }
        return soma;
    }

    public static double folhaPagamentoBonificacao(List<Funcionario> lista, String mes, String ano) {
        if(lista == null || mes == null || ano == null || mes.length() != 2 || ano.length() != 4) {
            throw new FalhaEntradaDados("Entrada de dados inválida");
        }
        LocalDate data = conversorStringLocalDate(mes, ano);
        if(data.isBefore(LocalDate.parse("01/12/2021",dtf)) || LocalDate.parse("01/04/2022",dtf).isBefore(data)) {
            System.out.println("Aviso: Nesta data não há registro de venda dos Vendedores");
        }

        double soma = 0.0;
        for(Funcionario funcionario: lista) {
            int anosTrabalhados = (int) ChronoUnit.YEARS.between(funcionario.dataContratacao, conversorStringLocalDate(mes, ano));
            if(funcionario instanceof Vendedor) {
                soma += Vendedor.BONIFICACAO * ((Vendedor) funcionario).relatorioVendasPorMes.getOrDefault(data, 0.0);
            }
            if(funcionario instanceof Secretario) {
                double salario = Gerente.SALARIO_FIXO + Gerente.SALARIO_VARIAVEL * anosTrabalhados;
                soma += Secretario.BONIFICACAO * salario;
            }
        }
        return soma;
    }

    public static Funcionario maiorPagamentoTotal(List<Funcionario> lista, String mes, String ano) {
        Comparator<Funcionario> comparator = new SalarioComparator(mes, ano);
        lista.sort(comparator.reversed());
        //System.out.println(lista.get(0).salarioTotal(conversorStringLocalDate(mes, ano)));
        return lista.get(0);
    }

    public static String maiorPagamentoBonificao(List<Funcionario> lista, String mes, String ano) {
        Comparator<Funcionario> comparator = new SalarioComparator(mes, ano);
        lista.sort(comparator.reversed());
        String nome = lista.get(0).getNome();
        return nome;
    }

    public static Funcionario maiorVendedor(List<Funcionario> lista, String mes, String ano) {
        Comparator<Funcionario> comparator = new VendasComparator(mes, ano);
        lista.sort(comparator.reversed());

        return lista.get(0);
    }

    public static LocalDate conversorStringLocalDate(String mes, String ano) {
        String dataString = "01/" + mes + "/" + ano;
        return  LocalDate.parse(dataString, dtf);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", dataContratacao=" + dataContratacao +
                '}';
    }
}
