package exception;

/**
 * Lançada quando CPF é inválido.
 */
public class CpfInvalidoException extends BancoException {
    private static final long serialVersionUID = 1L;
    public CpfInvalidoException(String mensagem) {
        super(mensagem);
    }
}
