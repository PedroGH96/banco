package exception;

/**
 * Lançada quando valor de operação é inválido.
 */
public class ValorInvalidoException extends BancoException {
    private static final long serialVersionUID = 1L;
    public ValorInvalidoException(String mensagem) {
        super(mensagem);
    }
}