package exception;

/**
 * Lançada quando tenta cadastrar cliente com CPF já existente.
 */
public class ClienteJaExisteException extends BancoException {
    private static final long serialVersionUID = 1L;
    public ClienteJaExisteException(String cpf) {
        super("Cliente com CPF " + cpf + " já existe no sistema");
    }
}
