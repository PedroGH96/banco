package factory;

import constants.Constantes;
import exception.TipoContaInvalidoException;
import model.Cliente;
import model.Conta;
import model.ContaCorrente;
import model.ContaPoupanca;

/**
 * Factory para criação de contas.
 * Boa Prática: Factory Pattern - Open/Closed Principle (OCP).
 * Facilita adicionar novos tipos de conta sem modificar código existente.
 */
public final class ContaFactory {

    /**
     * Construtor privado para prevenir instanciação.
     */
    private ContaFactory() {
        throw new AssertionError("Classe ContaFactory não deve ser instanciada");
    }

    /**
     * Cria uma conta do tipo especificado.
     * Boa Prática: Centraliza lógica de criação.
     *
     * @param numeroConta Número único da conta
     * @param cliente Cliente titular
     * @param tipoConta Tipo da conta ("corrente" ou "poupanca")
     * @param saldoInicial Saldo inicial
     * @return Nova conta criada
     * @throws TipoContaInvalidoException se tipo for inválido
     */
    public static Conta criarConta(int numeroConta, Cliente cliente, String tipoConta, double saldoInicial)
            throws TipoContaInvalidoException {

        // Validação defensiva
        if (tipoConta == null || tipoConta.trim().isEmpty()) {
            throw new TipoContaInvalidoException("Tipo de conta não pode ser nulo ou vazio");
        }

        String tipoNormalizado = tipoConta.toLowerCase().trim();

        // Factory Method Pattern
        switch (tipoNormalizado) {
            case Constantes.TIPO_CONTA_CORRENTE:
                return new ContaCorrente(numeroConta, cliente, saldoInicial);

            case Constantes.TIPO_CONTA_POUPANCA:
                return new ContaPoupanca(numeroConta, cliente, saldoInicial);

            default:
                throw new TipoContaInvalidoException(tipoConta);
        }
    }

}
