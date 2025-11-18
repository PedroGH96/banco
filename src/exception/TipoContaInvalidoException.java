package exception;

/**
 * Lançada quando tipo de conta é inválido.
 */
public class TipoContaInvalidoException extends BancoException {
    private static final long serialVersionUID = 1L;
    public TipoContaInvalidoException(String tipo) {
        super("Tipo de conta inválido: " + tipo + ". Use 'corrente' ou 'poupanca'");
    }
}
