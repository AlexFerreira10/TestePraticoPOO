# Teste Prático Java - Orientação a Objetos

## Tabela de Conteúdos
   * [Estrutura do Projeto](#estrutura-do-projeto)
   * [Funcionalidades](#funcionalidades)
   * [Testes](#testes)
   * [Como Usar](#como-usar)
   * [Tecnologias](#tecnologias)
   * [Autor](#autor)

## Estrutura do Projeto
![Diagrama UML](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/8faaea58-df35-45dd-a0bf-939f2bfa6c11)

O projeto está organizado da seguinte maneira:

- **application**: Contém a classe principal `Main`, responsável pela interação com o usuário.
- **entities**: Contém as classes que representam as entidades do sistema:
  - `Empresa`: Representa uma empresa.
  - `Funcionario`: Superclasse para outras subclasses de funcionários.
    - `Vendedor`: Representa um vendedor, subclasse de `Funcionario`.
    - `Secretario`: Representa um secretário, subclasse de `Funcionario`.
    - `Gerente`: Representa um gerente, subclasse de `Funcionario`.
  - `VendasComparator`: Implementa um comparador para ordenar funcionários por vendas.
  - `BonificacaoComparator`: Implementa um comparador para ordenar funcionários por bonificação.
  - `SalarioComparator`: Implementa um comparador para ordenar funcionários por salário.
- **exceptions**: Contém a classe `FalhaEntradaDados`, responsável por representar uma exceção relacionada à entrada de dados.

## Funcionalidades

O sistema oferece as seguintes funcionalidades:

1. **Um método que receba uma lista de funcionários, mês e ano e retorne o valor total pago (salário e benefício) a esses funcionários no mês.**
     ##### Classe Funcionario
     ```java
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
     ```

2. **Um método que receba uma lista de funcionários, mês e ano e retorne o total pago somente em salários no mês.**
    ##### Classe Funcionario
   ```java
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
   ```

3. **Um método que receba uma lista somente com os funcionários que recebem benefícios, mês e ano e retorne o total pago em benefícios no mês.**
   ##### Lista passada como argumento para o método - Classe Main
   ```java
    // Lista somente com funcionários que recebem bonificações
    List<Funcionario> listaRecebemBonificacao = new ArrayList<>(empresa.getQuadroFuncionarios().stream().filter(funcionario -> funcionario instanceof Vendedor || funcionario instanceof Secretario)
            .toList());
   ```
   ##### Classe Funcionario
   ```java
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
   ```

4. **Um método que receba uma lista de funcionários, mês e ano e retorne o que recebeu o valor mais alto no mês.**
    ##### Classe Funcionario
   ```java
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
   ```
   ##### Método de ordenção - Classe SalarioComparator
   ```java
   @Override
    public int compare(Funcionario f1, Funcionario f2) {

        double salarioTotalF1 = f1.salarioTotal(Funcionario.conversorStringLocalDate(mes,ano));
        double salarioTotalF2 = f2.salarioTotal(Funcionario.conversorStringLocalDate(mes,ano));

        return Double.compare(salarioTotalF1, salarioTotalF2);
    }
   ```

5. **Um método que receba uma lista somente com os funcionários que recebem benefícios, mês e ano e retorne o nome do funcionário que recebeu o valor mais alto em benefícios no mês** s.
   ##### Lista passada como argumento para o método - Classe Main
   ```java
   // Lista somente com funcionários que recebem bonificações
    List<Funcionario> listaRecebemBonificacao = new ArrayList<>(empresa.getQuadroFuncionarios().stream().filter(funcionario -> funcionario instanceof Vendedor || funcionario instanceof Secretario)
            .toList());
   ```
   ##### Classe Funcionario
   ```java
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
   ```
   ##### Método de ordenção - Classe BonificacaoComparator
   ```java
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
   ```
   
6. **Um método que receba uma lista de vendedores, mês e ano e retorne o que mais vendeu no mês.** 
   ##### Lista passada como argumento para o método - Classe Main
   ```java
   // Lista somente com Vendedores
    List<Vendedor> listaVendedores = empresa.getQuadroFuncionarios().stream()
            .filter(funcionario -> funcionario instanceof Vendedor)
            .map(funcionario -> (Vendedor) funcionario)
            .collect(Collectors.toList());
   ```
   ##### Classe Funcionario
   ```java
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
   ```
   ##### Método de ordenção - Classe VendasComparator
   ```java
    @Override
    public int compare(Funcionario f1, Funcionario f2) {
        LocalDate data = Funcionario.conversorStringLocalDate(mes, ano);

        double vendedor1 = ((Vendedor) f1).relatorioVendasPorMes.getOrDefault(data, 0.0);
        double vendedor2 = ((Vendedor) f2).relatorioVendasPorMes.getOrDefault(data, 0.0);

        return Double.compare(vendedor1, vendedor2);
    }
   ```
   
## Avisos

Por conta de uma limitação na base de dados fornecida, o sistema só funcionará integralmente mediante as checagens do período entre 12/2021 e 04/2022.

## Testes
1. Testando a primeira funcionalidade
   
   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/5125dfe6-fd91-48b2-bfa0-3a8d8b81cbd2)

2. Testando a segunda funcionalidade
   
   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/c1a93116-40bb-484c-8f50-51e3033abd1b)

3. Testando a terceira funcionalidade
   
   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/da39d297-7872-468e-9565-ae5c8e078ffd)

4. Testando a quarta funcionalidade

   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/1cb718d9-6190-4881-be90-29a775fb34af)

5. Testando a quinta funcionalidade

   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/353a87de-3f75-4fe6-a2f7-971fff35783d)

6. Testando a sexta funcionalidade

   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/62cf77f0-fa4c-4950-8851-00cfd17b43cc)
   ![image](https://github.com/AlexFerreira10/TestePraticoPOO/assets/143446470/19543b48-884e-4b25-9d3a-09da1ca47f0c)

## Como Usar

Para utilizar este projeto localmente, siga estas etapas:

### 1. Pré-requisitos

- Certifique-se de ter o ambiente Java configurado corretamente em sua máquina. Você pode baixar e instalar o JDK (Java Development Kit) mais recente em [java.com](https://www.java.com/).
- Certifique-se de ter o Git instalado em sua máquina. Você pode encontrar mais informações sobre como instalar o Git em [git-scm.com](https://git-scm.com/).

### 2. Clonar o Repositório

Para clonar o repositório para sua máquina local, abra um terminal e execute o seguinte comando:

```bash
git clone <URL_DO_REPOSITORIO>
```
Substitua <URL_DO_REPOSITORIO> pela URL do repositório Git onde o projeto está hospedado.

### 3. Compilação

Após clonar o repositório, navegue até o diretório raiz do projeto onde está localizado o arquivo Main.java. Em seguida,
execute o seguinte comando para compilar o código Java e gerar os arquivos de classe correspondentes:

```bash
javac application/Main.java
```

### 4. Execução

Depois de compilar o projeto, você pode executá-lo com o seguinte comando:

```bash
java application.Main
```
Isso iniciará a aplicação e você poderá interagir com ela através do console.

### 4. Utilização da Aplicação

Siga as instruções apresentadas no console para utilizar as funcionalidades disponíveis. 

## Tecnologias
- Java

## Autor

- Douglas Alexsander Ferreira Corrêa
- www.linkedin.com/in/alexferreira92
