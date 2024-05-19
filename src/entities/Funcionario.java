package entities;

import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Funcionario {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String nome;
    protected LocalDate dataContratacao;

    public Funcionario(String nome, LocalDate dataContratacao) {
        this.nome = nome;
        this.dataContratacao = dataContratacao;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    // Cada cargo terá sua forma singular de calcular seu salário
    public abstract double salarioTotal(LocalDate dataSalario);

    // Retorna o somatório do salário total(Salário + Bonificação) de todos os funcionários
    public static double folhaPagamentoTotal(List<Funcionario> lista, String mes, String ano) {

        validarEntradaDados(lista, mes, ano);

        LocalDate data = conversorStringLocalDate(mes, ano);
        // Aviso para caso a data escolhida não esteja no relátorio de vendas fornecido pela questão
        avisoDataForaPeriodoVendas(data);

        double soma = lista.stream()
                .filter(funcionario -> funcionario.getDataContratacao().isBefore(data))
                .mapToDouble(funcionario -> funcionario.salarioTotal(data))
                .sum();

        return soma;
    }

    // Retorna o somatório do salário parcial(Sem bonificação) de todos os funcionários
    public static double folhaPagamentoSalario(List<Funcionario> lista, String mes, String ano) {

        validarEntradaDados(lista, mes, ano);

        LocalDate data = conversorStringLocalDate(mes, ano);
        // Aviso para caso a data escolhida não esteja no relátorio de vendas fornecido pela questão
        if(data.isBefore(LocalDate.parse("01/12/2021",dtf)) || LocalDate.parse("01/04/2022",dtf).isBefore(data)) {
            System.out.println("Aviso: Nesta data não há registro de venda dos Vendedores");
        }

        // Acumula o somatório dos salarios
        double soma = 0.0;

        for(Funcionario funcionario : lista) {
            int anosTrabalhados = (int) ChronoUnit.YEARS.between(funcionario.dataContratacao, conversorStringLocalDate(mes, ano));

            if(anosTrabalhados >= 0) {
                //Necessário fazer essa diferenciação pois cada cargo tem suas remunerações singulares
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
            else {
                soma = 0.0;
            }
        }
        return soma;
    }

    // Retorna o somatório da bonificação de todos os funcionarios (gerentes não estão inclusos)
    public static double folhaPagamentoBonificacao(List<Funcionario> lista, String mes, String ano) {

        validarEntradaDados(lista, mes, ano);

        LocalDate data = conversorStringLocalDate(mes, ano);
        // Aviso para caso a data escolhida não esteja no relátorio de vendas fornecido pela questão
        avisoDataForaPeriodoVendas(data);

        // Acumula o somatório dos benefícios
        double soma = 0.0;

        for(Funcionario funcionario : lista) {
            int anosTrabalhados = (int) ChronoUnit.YEARS.between(funcionario.dataContratacao, conversorStringLocalDate(mes, ano));
            if(anosTrabalhados >= 0) {
              //Não há gerentes pois eles foram filtrados antes de serem passados como argumento
                if(funcionario instanceof Vendedor) {
                    soma += Vendedor.BONIFICACAO * ((Vendedor) funcionario).relatorioVendasPorMes.getOrDefault(data, 0.0);
                }
                if(funcionario instanceof Secretario) {
                    double salario = Secretario.SALARIO_FIXO + Secretario.SALARIO_VARIAVEL * anosTrabalhados;
                    soma += Secretario.BONIFICACAO * salario;
                }
            }
            else {
                soma = 0.0;
            }
        }
        return soma;
    }

    // Retorna o funcionário com o maior salário total (salário + bonificação)
    public static Funcionario maiorPagamentoTotal(List<Funcionario> lista, String mes, String ano) {

        validarEntradaDados(lista, mes, ano);

        LocalDate data = conversorStringLocalDate(mes, ano);
        // Aviso para caso a data escolhida não esteja no relátorio de vendas fornecido pela questão
        avisoDataForaPeriodoVendas(data);

        // Filtrar os funcionários contratados até a data especificada
        List<Funcionario> funcionariosContratadosAteData = filtrarFuncionariosContratados(lista, data);

        if(funcionariosContratadosAteData.isEmpty()) {
            throw new FalhaEntradaDados("Não há funcionários contratados até a data especificada");
        }

        // Ordeno a lista filtrada com base no maior salário em ordem decrescente, para o maior ser o primeiro
        Comparator<Funcionario> comparator = new SalarioComparator(mes, ano);
        funcionariosContratadosAteData.sort(comparator.reversed());

        return funcionariosContratadosAteData.get(0);
    }

    // Retorna o nome do funcionario com maior bonificação (os gerentes não estão inclusos na listagem)
    public static String maiorBonificacao(List<Funcionario> lista, String mes, String ano) {

        // Evitar inconsistências de dados
        validarEntradaDados(lista, mes, ano);

        LocalDate data = conversorStringLocalDate(mes, ano);
        // Aviso para caso a data escolhida não esteja no relátorio de vendas fornecido pela questão
        avisoDataForaPeriodoVendas(data);

        // Filtra os funcionários contratados até a data especificada
        List<Funcionario> funcionariosContratadosAteData = filtrarFuncionariosContratados(lista, data);

        if(funcionariosContratadosAteData.isEmpty()) {
            throw new FalhaEntradaDados("Não há funcionários contratados até a data especificada");
        }

        // Ordeno a lista de forma em que o funcionário com a maior bonificação seja o primeiro
        Comparator<Funcionario> comparator = new BonificacaoComparator(mes, ano);
        funcionariosContratadosAteData.sort(comparator.reversed());

        return funcionariosContratadosAteData.get(0).getNome();
    }

    // Retorna o vendedor com maior venda
    public static Funcionario melhorVendedor(List<Vendedor> lista, String mes, String ano) {

        validarEntradaDados(lista, mes, ano);

        LocalDate data = conversorStringLocalDate(mes, ano);

        // Aviso para caso a data escolhida não esteja no relátorio de vendas fornecido pela questão
        avisoDataForaPeriodoVendas(data);

        // Filtrar os funcionários contratados até a data especificada
        List<Vendedor> funcionariosContratadosAteData = new ArrayList<>();
        for(Vendedor vendedor : lista) {
            if(vendedor.dataContratacao.isBefore(data) || vendedor.dataContratacao.isEqual(data)) {
                funcionariosContratadosAteData.add(vendedor);
            }
        }

        // Vendedores não contrados
        if(funcionariosContratadosAteData.isEmpty()) {
            throw new FalhaEntradaDados("Não há funcionários contratados até a data especificada");
        }

        // Ordeno a lista de forma em que o funcionário com a maior venda  seja o primeiro
        Comparator<Funcionario> comparator = new VendasComparator(mes, ano);
        funcionariosContratadosAteData.sort(comparator.reversed());

        // Vendedores sem registro de vendas
        if(funcionariosContratadosAteData.get(0).relatorioVendasPorMes.get(data) == null) {
            throw new FalhaEntradaDados("Não há registro de vendas na data especificada");
        }

        return funcionariosContratadosAteData.get(0);
    }

    // Converte a "data" passada como String para o padrão LocalDate
    public static LocalDate conversorStringLocalDate(String mes, String ano) {
        String dataString = "01/" + mes + "/" + ano;

        if (Integer.parseInt(mes) < 1 || Integer.parseInt(mes)  > 12) {
            throw new IllegalArgumentException("O mês fornecido não é válido! ");
        }

        // Como o exercício não especifíca, escolhi para deixar o exercício mais real
        if (Integer.parseInt(ano) < 2000 || Integer.parseInt(ano) > 2024) {
            throw new IllegalArgumentException("O ano fornecido não é válido! ");
        }

        return  LocalDate.parse(dataString, dtf);
    }

    // Verifica se as informações passadas são válidas
    private static void validarEntradaDados(List<?> lista, String mes, String ano) {
        if (lista == null || mes == null || ano == null || mes.length() != 2 || ano.length() != 4) {
            throw new FalhaEntradaDados("Entrada de dados inválida");
        }
    }

    // Retorna uma lista só com os funcionários contratados até a data fornecida
    private static List<Funcionario> filtrarFuncionariosContratados(List<Funcionario> lista, LocalDate data) {
        return lista.stream()
                .filter(funcionario -> funcionario.dataContratacao.isBefore(data) || funcionario.dataContratacao.isEqual(data))
                .collect(Collectors.toList());
    }

    // Deixa um aviso caso a data fornecida esteja fora do relatório de vendas passada na questão
    private static void avisoDataForaPeriodoVendas(LocalDate data) {
        LocalDate dataMinima = LocalDate.parse("01/12/2021", dtf);
        LocalDate dataMaxima = LocalDate.parse("01/04/2022", dtf);
        if (data.isBefore(dataMinima) || dataMaxima.isBefore(data)) {
            System.out.println("Aviso: Nesta data não há registro de venda dos Vendedores");
        }
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", dataContratacao=" + dataContratacao +
                '}';
    }
}
