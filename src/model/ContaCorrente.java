package model;

/**
 * Representa uma Conta Corrente.
 * Boa Prática: Herança com Liskov Substitution Principle.
 */
public final class ContaCorrente extends Conta {

    /**
     * Construtor da Conta Corrente.
     *
     * @param numeroConta Número único da conta
     * @param cliente Cliente titular
     * @param saldoInicial Saldo inicial
     */
    public ContaCorrente(int numeroConta, Cliente cliente, double saldoInicial) {
        super(numeroConta, cliente, saldoInicial);
    }

    /**
     * Retorna o tipo da conta.
     *
     * @return "Conta Corrente"
     */
    @Override
    public String getTipo() {
        return "Conta Corrente";
    }
}
