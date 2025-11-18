package exception;

/**
 * Lançada quando cliente não é encontrado.
 */
public class ClienteNaoEncontradoException extends BancoException {
    private static final long serialVersionUID = 1L;
    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente com CPF " + cpf + " não encontrado");
    }
}
