package exceptions;

public class FalhaEntradaDados extends RuntimeException{

    public FalhaEntradaDados(String mensagem) {
        super(mensagem);
    }
}
