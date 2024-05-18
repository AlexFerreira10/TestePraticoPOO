package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Empresa {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected List<Funcionario> quadroFuncionarios = new ArrayList<>();

    public Empresa() {
        addFuncionario();
    }

    public List<Funcionario> getQuadroFuncionarios() {
        return quadroFuncionarios;
    }

    private void addFuncionario() {

        Secretario f1 = new Secretario("Jorge Carvalho ", LocalDate.parse("01/01/2018", dtf));
        Secretario f2 = new Secretario("Maria Souza", LocalDate.parse("01/12/2015", dtf));

        Gerente f5 = new Gerente("Juliana Alves", LocalDate.parse("01/07/2017", dtf));
        Gerente f6 = new Gerente("Bento Albino ", LocalDate.parse("01/03/2014", dtf));

        Vendedor f3 = new Vendedor("Ana Silva", LocalDate.parse("01/12/2021", dtf));
        f3.adicionarVenda(LocalDate.parse("01/12/2021", dtf), 5200.00);
        f3.adicionarVenda(LocalDate.parse("01/01/2022", dtf), 4000.00);
        f3.adicionarVenda(LocalDate.parse("01/02/2022", dtf), 4200.00);
        f3.adicionarVenda(LocalDate.parse("01/03/2022", dtf), 5850.00);
        f3.adicionarVenda(LocalDate.parse("01/04/2022", dtf), 7000.00);

        Vendedor f4 = new Vendedor("João Mendes ", LocalDate.parse("01/12/2021", dtf));
        f4.adicionarVenda(LocalDate.parse("01/12/2021", dtf), 3400.00 );
        f4.adicionarVenda(LocalDate.parse("01/01/2022", dtf), 7700.00 );
        f4.adicionarVenda(LocalDate.parse("01/02/2022", dtf), 5000.00 );
        f4.adicionarVenda(LocalDate.parse("01/03/2022", dtf), 5900.00);
        f4.adicionarVenda(LocalDate.parse("01/04/2022", dtf), 6500.00);

        quadroFuncionarios.addAll(List.of(f1, f2, f3, f4, f5, f6));
    }
}
