package model;

import exception.ValorInvalidoException;
import validator.ContaValidator;

/**
 * Representa uma Conta Poupança com rendimento.
 * Boa Prática: LSP - Não adiciona pré-condições mais restritivas.
 */
public final class ContaPoupanca extends Conta {

    /**
     * Construtor da Conta Poupança.
     *
     * @param numeroConta Número único da conta
     * @param cliente Cliente titular
     * @param saldoInicial Saldo inicial
     */
    public ContaPoupanca(int numeroConta, Cliente cliente, double saldoInicial) {
        super(numeroConta, cliente, saldoInicial);
    }

    /**
     * Aplica rendimento percentual ao saldo.
     * Programação Defensiva: Valida percentual e atualiza saldo com segurança.
     *
     * @param percentual Percentual de rendimento (ex: 2.5 para 2.5%)
     * @throws ValorInvalidoException se percentual for inválido
     */
    public void aplicarRendimento(double percentual) throws ValorInvalidoException {
        // Pré-condição
        validarInvariante();
        double saldoAnterior = saldo;

        // Validação
        ContaValidator.validarPercentualRendimento(percentual);

        // Cálculo do rendimento
        double valorRendimento = saldo * (percentual / 100.0);
        saldo += valorRendimento;

        // Assertiva: Saldo aumentou corretamente
        assert saldo > saldoAnterior : "Saldo deveria ter aumentado após rendimento";
        assert saldo == (saldoAnterior + valorRendimento) : "Cálculo de rendimento incorreto";

        // Pós-condição
        validarInvariante();
    }

    /**
     * Retorna o tipo da conta.
     *
     * @return "Conta Poupança"
     */
    @Override
    public String getTipo() {
        return "Conta Poupança";
    }
}