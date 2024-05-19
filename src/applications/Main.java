package applications;

import entities.*;
import exceptions.FalhaEntradaDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        int opcao = 7;

        do {
            try {
                // Lista somente com funcionários que recebem bonificações
                List<Funcionario> listaRecebemBonificacao = new ArrayList<>(empresa.getQuadroFuncionarios().stream().filter(funcionario -> funcionario instanceof Vendedor || funcionario instanceof Secretario)
                        .toList());
                // Lista somente com Vendedores
                List<Vendedor> listaVendedores = empresa.getQuadroFuncionarios().stream()
                        .filter(funcionario -> funcionario instanceof Vendedor)
                        .map(funcionario -> (Vendedor) funcionario)
                        .collect(Collectors.toList());

                Main.menu();
                opcao = Integer.parseInt(sc.next());

                switch (opcao) {
                    case 0:
                        opcao = 0;
                        break;
                    case 1:
                        System.out.print("\nEscolha o ano da checagem (Padrão 4 dígitos): ");
                        String ano = sc.next();
                        System.out.print("Escolha o mês da checagem (Padrão 2 dígitos): ");
                        String mes = sc.next();
                        double valorTotal = Funcionario.folhaPagamentoTotal(empresa.getQuadroFuncionarios(), mes, ano);
                        System.out.println("Folha de Pagamento Mensal (Salário + Benefício) dos Funcionários: R$" + String.format("%.2f", valorTotal));
                        break;
                    case 2:
                        System.out.print("\nEscolha o ano da checagem (Padrão 4 dígitos): ");
                        ano = sc.next();
                        System.out.print("Escolha o mês da checagem (Padrão 2 dígitos): ");
                        mes = sc.next();
                        valorTotal = Funcionario.folhaPagamentoSalario(empresa.getQuadroFuncionarios(), mes, ano);
                        System.out.println("Folha de Pagamento Mensal (Somente salário) dos Funcionários: R$" + String.format("%.2f", valorTotal));
                        break;
                    case 3:
                        System.out.print("\nEscolha o ano da checagem (Padrão 4 dígitos): ");
                        ano = sc.next();
                        System.out.print("Escolha o mês da checagem (Padrão 2 dígitos): ");
                        mes = sc.next();
                        valorTotal = Funcionario.folhaPagamentoBonificacao(listaRecebemBonificacao, mes, ano);
                        System.out.println("Folha de Pagamento Mensal (Bonificação) dos Funcionários: R$" + String.format("%.2f", valorTotal));
                        break;
                    case 4:
                        System.out.print("\nEscolha o ano da checagem (Padrão 4 dígitos): ");
                        ano = sc.next();
                        System.out.print("Escolha o mês da checagem (Padrão 2 dígitos): ");
                        mes = sc.next();
                        System.out.println("Maior Salário Total do Mês:" + Funcionario.maiorPagamentoTotal(empresa.getQuadroFuncionarios(), mes, ano));
                        break;
                    case 5:
                        System.out.print("\nEscolha o ano da checagem (Padrão 4 dígitos): ");
                        ano = sc.next();
                        System.out.print("Escolha o mês da checagem (Padrão 2 dígitos): ");
                        mes = sc.next();
                        System.out.println("Maior Bonificação do Mês: " + Funcionario.maiorBonificao(listaRecebemBonificacao, mes, ano));
                        break;
                    case 6:
                        System.out.print("\nEscolha o ano da checagem (Padrão 4 dígitos): ");
                        ano = sc.next();
                        System.out.print("Escolha o mês da checagem (Padrão 2 dígitos): ");
                        mes = sc.next();
                        System.out.println("Maior Vendedor do Mês: " + Funcionario.melhorVendedor(listaVendedores, mes, ano));
                        break;
                    default:
                        System.out.println("Digite uma opção válida!");
                }
            } catch (FalhaEntradaDados e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
            Main.limparConsole();
        } while (opcao != 0);
    }

    public static void menu() {
        StringBuilder sb = new StringBuilder();
        sb.append("Avisos: Por conta de uma limitação na base de dados fornecida, \no sistema só funcionaŕa integralmente mediante as checagens do período entre 12/2021 e 04/2022.")
                .append("\n\n--------------------------------- Menu ---------------------------------")
                .append("\n Escolha uma opção...")
                .append("\n [0] Sair")
                .append("\n [1] Folha de Pagamento Mensal (Salário + Benefício) dos Funcionários.")
                .append("\n [2] Folha de Pagamento Mensal (Somente salário) dos Funcionários.")
                .append("\n [3] Folha de Pagamento Mensal (Bonificação) dos Funcionários.")
                .append("\n [4] Verificar Maior Salário Total Mensal.")
                .append("\n [5] Verificar Maior Bonificação Mensal.")
                .append("\n [6] Verificar Melhor Vendedor do Mês.")
                .append("\n Opção: ");
        System.out.print(sb);
    }

    public static void limparConsole() {
        System.out.print("\nDigite 'ok' para continuar... ");
        String continuar = sc.next();
        for (int i = 0; i < 50; ++i) System.out.println();
    }
}