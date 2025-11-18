package exception;

/**
 * Lançada quando conta não é encontrada.
 */
public class ContaNaoEncontradaException extends BancoException {
    private static final long serialVersionUID = 1L;
    public ContaNaoEncontradaException(int numeroConta) {
        super("Conta número " + numeroConta + " não encontrada");
    }
}
