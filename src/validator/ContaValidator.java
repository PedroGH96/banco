package validator;

import constants.Constantes;
import exception.DadosInvalidosException;
import exception.ValorInvalidoException;

/**
 * Validador para operações de Conta.
 * Boa Prática: SRP - Responsabilidade única de validação.
 */
public final class ContaValidator {

    /**
     * Construtor privado para prevenir instanciação.
     */
    private ContaValidator() {
        throw new AssertionError("Classe ContaValidator não deve ser instanciada");
    }

    /**
     * Valida valor de saldo inicial.
     * Programação Defensiva: Verifica limites e valores especiais.
     *
     * @param saldo Saldo a ser validado
     * @throws DadosInvalidosException se saldo for inválido
     */
    public static void validarSaldoInicial(double saldo) throws DadosInvalidosException {
        validarValorNumerico(saldo, "Saldo");

        if (saldo < Constantes.SALDO_MINIMO) {
            throw new DadosInvalidosException(
                    String.format("Saldo inicial não pode ser menor que R$ %.2f", Constantes.SALDO_MINIMO)
            );
        }

        if (saldo > Constantes.SALDO_MAXIMO) {
            throw new DadosInvalidosException(
                    String.format("Saldo inicial não pode exceder R$ %.2f", Constantes.SALDO_MAXIMO)
            );
        }
    }

    /**
     * Valida valor de operação (depósito, saque, transferência).
     *
     * @param valor Valor a ser validado
     * @param tipoOperacao Tipo da operação (para mensagem de erro)
     * @throws ValorInvalidoException se valor for inválido
     */
    public static void validarValorOperacao(double valor, String tipoOperacao)
            throws ValorInvalidoException {
        try {
            validarValorNumerico(valor, tipoOperacao);
        } catch (DadosInvalidosException e) {
            throw new ValorInvalidoException(e.getMessage());
        }

        if (valor < Constantes.VALOR_MINIMO_OPERACAO) {
            throw new ValorInvalidoException(
                    String.format("%s deve ser no mínimo R$ %.2f",
                            tipoOperacao, Constantes.VALOR_MINIMO_OPERACAO)
            );
        }

        if (valor > Constantes.VALOR_MAXIMO_OPERACAO) {
            throw new ValorInvalidoException(
                    String.format("%s não pode exceder R$ %.2f",
                            tipoOperacao, Constantes.VALOR_MAXIMO_OPERACAO)
            );
        }
    }

    /**
     * Valida percentual de rendimento.
     *
     * @param percentual Percentual a ser validado
     * @throws ValorInvalidoException se percentual for inválido
     */
    public static void validarPercentualRendimento(double percentual)
            throws ValorInvalidoException {
        try {
            validarValorNumerico(percentual, "Percentual");
        } catch (DadosInvalidosException e) {
            throw new ValorInvalidoException(e.getMessage());
        }

        if (percentual < Constantes.RENDIMENTO_MINIMO) {
            throw new ValorInvalidoException(
                    String.format("Percentual deve ser no mínimo %.2f%%", Constantes.RENDIMENTO_MINIMO)
            );
        }

        if (percentual > Constantes.RENDIMENTO_MAXIMO) {
            throw new ValorInvalidoException(
                    String.format("Percentual não pode exceder %.2f%%", Constantes.RENDIMENTO_MAXIMO)
            );
        }
    }

    /**
     * Valida se valor numérico é válido (não é NaN, Infinito, etc).
     * Programação Defensiva: Protege contra valores especiais de double.
     *
     * @param valor Valor a ser validado
     * @param nomeCampo Nome do campo (para mensagem de erro)
     * @throws DadosInvalidosException se valor for inválido
     */
    private static void validarValorNumerico(double valor, String nomeCampo)
            throws DadosInvalidosException {
        if (Double.isNaN(valor)) {
            throw new DadosInvalidosException(nomeCampo + " não é um número válido");
        }

        if (Double.isInfinite(valor)) {
            throw new DadosInvalidosException(nomeCampo + " não pode ser infinito");
        }

        if (valor < 0) {
            throw new DadosInvalidosException(nomeCampo + " não pode ser negativo");
        }
    }

}
