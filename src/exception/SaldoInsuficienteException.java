package exception;

/**
 * Lançada quando há saldo insuficiente para operação.
 */
public class SaldoInsuficienteException extends BancoException {
    private static final long serialVersionUID = 1L;
    public SaldoInsuficienteException(int numeroConta, double saldoAtual, double valorSolicitado) {
        super(String.format("Saldo insuficiente na conta %d. Saldo: R$ %.2f, Solicitado: R$ %.2f",
                numeroConta, saldoAtual, valorSolicitado));
    }
}
