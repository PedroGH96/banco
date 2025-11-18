package exception;

/**
 * Lançada quando dados fornecidos são inválidos.
 */
public class DadosInvalidosException extends BancoException {
    private static final long serialVersionUID = 1L;
    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
