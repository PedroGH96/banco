package model;

import java.util.Objects;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;
import validator.ContaValidator;


/**
 * Classe abstrata que representa uma conta bancária.
 * Boa Prática: Abstração e Template Method Pattern.
 */
public abstract class Conta {
    private final int numeroConta;
    private final Cliente cliente;
    protected double saldo;

    /**
     * Construtor protegido com validação defensiva.
     *
     * @param numeroConta Número único da conta
     * @param cliente Cliente titular da conta
     * @param saldoInicial Saldo inicial da conta
     * @throws IllegalArgumentException se parâmetros forem inválidos
     */
    protected Conta(int numeroConta, Cliente cliente, double saldoInicial) {
        // Programação Defensiva: Validação de parâmetros
        Objects.requireNonNull(cliente, "Cliente não pode ser nulo");

        if (numeroConta <= 0) {
            throw new IllegalArgumentException("Número da conta deve ser positivo");
        }

        try {
            ContaValidator.validarSaldoInicial(saldoInicial);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        this.numeroConta = numeroConta;
        this.cliente = cliente;
        this.saldo = saldoInicial;

        // Assertiva: Estado inicial válido
        validarInvariante();
    }

    /**
     * Valida invariantes da classe.
     * Assertiva: Garante que o estado do objeto é sempre consistente.
     */
    protected void validarInvariante() {
        assert numeroConta > 0 : "Número da conta deve ser positivo";
        assert cliente != null : "Cliente não pode ser nulo";
        assert saldo >= 0 : "Saldo não pode ser negativo: " + saldo;
        assert !Double.isNaN(saldo) : "Saldo não pode ser NaN";
        assert !Double.isInfinite(saldo) : "Saldo não pode ser infinito";
    }

    /**
     * Retorna o número da conta.
     *
     * @return Número da conta
     */
    public final int getNumero() {
        validarInvariante();
        return numeroConta;
    }

    /**
     * Retorna o saldo atual.
     *
     * @return Saldo da conta
     */
    public final double getSaldo() {
        validarInvariante();
        return saldo;
    }

    /**
     * Retorna o nome do cliente.
     * Lei de Demeter: Evita que chamadores conheçam estrutura interna.
     *
     * @return Nome do cliente
     */
    public final String getNomeCliente() {
        validarInvariante();
        return cliente.getNome();
    }

    /**
     * Retorna o tipo da conta (Template Method Pattern).
     *
     * @return Tipo da conta
     */
    public abstract String getTipo();

    /**
     * Realiza depósito na conta.
     * Programação Defensiva: Valida valor antes de alterar estado.
     *
     * @param valor Valor a ser depositado
     * @throws ValorInvalidoException se valor for inválido
     */
    public final void depositar(double valor) throws ValorInvalidoException {
        // Pré-condição
        validarInvariante();

        // Validação
        ContaValidator.validarValorOperacao(valor, "Valor do depósito");

        // Operação
        saldo += valor;

        // Pós-condição
        validarInvariante();
    }

    /**
     * Realiza saque da conta.
     * Programação Defensiva: Verifica saldo antes de sacar.
     *
     * @param valor Valor a ser sacado
     * @throws ValorInvalidoException se valor for inválido
     * @throws SaldoInsuficienteException se saldo for insuficiente
     */
    public final void sacar(double valor)
            throws ValorInvalidoException, SaldoInsuficienteException {
        // Pré-condição
        validarInvariante();
        double saldoAnterior = saldo;

        // Validações
        ContaValidator.validarValorOperacao(valor, "Valor do saque");

        if (saldo < valor) {
            throw new SaldoInsuficienteException(numeroConta, saldo, valor);
        }

        // Operação
        saldo -= valor;

        // Assertiva: Saldo foi reduzido corretamente
        assert saldo == (saldoAnterior - valor) : "Saldo não foi atualizado corretamente";

        // Pós-condição
        validarInvariante();
    }

    /**
     * Realiza transferência para outra conta.
     * Boa Prática: Operação atômica (ou ambas acontecem ou nenhuma).
     *
     * @param contaDestino Conta de destino
     * @param valor Valor a ser transferido
     * @throws ValorInvalidoException se valor for inválido
     * @throws SaldoInsuficienteException se saldo for insuficiente
     */
    public final void transferir(Conta contaDestino, double valor)
            throws ValorInvalidoException, SaldoInsuficienteException {
        // Pré-condições
        validarInvariante();
        Objects.requireNonNull(contaDestino, "Conta destino não pode ser nula");

        if (this.equals(contaDestino)) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        // Validação
        ContaValidator.validarValorOperacao(valor, "Valor da transferência");

        // Operação atômica
        double saldoOrigemAnterior = this.saldo;
        double saldoDestinoAnterior = contaDestino.saldo;

        this.sacar(valor);          // Pode lançar exceção
        contaDestino.depositar(valor);  // Pode lançar exceção

        // Assertivas: Transferência foi realizada corretamente
        assert this.saldo == (saldoOrigemAnterior - valor) : "Saldo origem incorreto";
        assert contaDestino.saldo == (saldoDestinoAnterior + valor) : "Saldo destino incorreto";

        // Pós-condições
        validarInvariante();
        contaDestino.validarInvariante();
    }

    /**
     * Verifica se duas contas são iguais (pelo número).
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Conta conta = (Conta) objeto;
        return numeroConta == conta.numeroConta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroConta);
    }

    @Override
    public String toString() {
        return String.format("Conta{numero=%d, tipo='%s', cliente='%s', saldo=R$ %.2f}",
                numeroConta, getTipo(), cliente.getNome(), saldo);
    }
}