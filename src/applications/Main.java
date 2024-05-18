package applications;

import entities.*;
import exceptions.FalhaEntradaDados;

import java.time.LocalDate;
import java.util.Scanner;


public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        int opcao = 7;

        do {
            try {
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
                        System.out.println("Folha de Pagamento Total: R$" + String.format("%.2f", valorTotal));
                        break;
                    case 2:
                        //Funcao
                        break;
                    case 3:
                        //Funcao
                        break;
                    case 4:
                        //Funcao
                        break;
                    case 5:
                        //Funcao
                        break;
                    case 6:
                        //Funcao
                        break;
                    default:
                        System.out.println("Digite uma opção válida!");
                }
            } catch (FalhaEntradaDados e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (NumberFormatException e) {
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
                .append("\n [1] Folha de Pagamento Mensal (Salário + Benefício) dos Funcionário.")
                .append("\n [2] Folha de Pagamento Mensal (Somente salário) dos Funcionário.")
                .append("\n [3] Folha de Pagamento Mensal (Benefícios) dos Funcioários.")
                .append("\n [4] Verificar Maior Salário Total Mensal.")
                .append("\n [5] Verificar Maior Beneficiário Mensal.")
                .append("\n [6] Verificar Maior Vendedor do Mês")
                .append("\n Opção: ");
        System.out.print(sb);
    }

    public static void limparConsole() {
        System.out.print("\nDigite 'ok' para continuar... ");
        String continuar = sc.next();
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    public static LocalDate entradaDeData() {
        System.out.println("Escolha o ano da checagem");
        String mes = sc.nextLine();
        System.out.println("Escolha o mes da checagem");
        String ano = sc.nextLine();
        sc.nextLine();
        return  Funcionario.conversorStringLocalDate(mes,ano);
    }
}
 /*
        System.out.println("Olá mundo!");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Funcionario f1 = new Secretario("Alex", LocalDate.parse("01/02/2004", dtf));
        Funcionario f2 = new Vendedor("Joao", LocalDate.parse("01/08/2021", dtf));
        Funcionario f3 = new Gerente("Maria", LocalDate.parse("01/01/2009", dtf));

        Double a = f1.salarioTotal(LocalDate.parse("01/03/2005", dtf));
        Double b = f2.salarioTotal(LocalDate.parse("01/01/2025", dtf));
        Double c = f3.salarioTotal(LocalDate.parse("01/03/2010", dtf));

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);*/