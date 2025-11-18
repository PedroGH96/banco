package exception;

/**
 * Exceção base para o sistema bancário.
 * Boa Prática: Hierarquia de exceções personalizadas.
 */
public class BancoException extends Exception {
    private static final long serialVersionUID = 1L;
    public BancoException(String mensagem) {
        super(mensagem);
    }

    public BancoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
