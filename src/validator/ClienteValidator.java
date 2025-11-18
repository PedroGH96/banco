package validator;

import constants.Constantes;
import exception.CpfInvalidoException;
import exception.DadosInvalidosException;

import java.util.Objects;

/**
 * Validador para dados de Cliente.
 * Boa Prática: Separação de responsabilidades (SRP) e Programação Defensiva.
 */
public final class ClienteValidator {

    /**
     * Construtor privado para prevenir instanciação.
     */
    private ClienteValidator() {
        throw new AssertionError("Classe ClienteValidator não deve ser instanciada");
    }

    /**
     * Valida o nome do cliente.
     * Programação Defensiva: Valida nulo, vazio e tamanho.
     *
     * @param nome Nome a ser validado
     * @throws DadosInvalidosException se nome for inválido
     */
    public static void validarNome(String nome) throws DadosInvalidosException {
        Objects.requireNonNull(nome, Constantes.ERRO_NOME_NULO);

        String nomeTrimmed = nome.trim();

        if (nomeTrimmed.isEmpty()) {
            throw new DadosInvalidosException("Nome não pode ser vazio");
        }

        if (nomeTrimmed.length() < Constantes.NOME_TAMANHO_MINIMO) {
            throw new DadosInvalidosException(
                    String.format("Nome deve ter no mínimo %d caracteres", Constantes.NOME_TAMANHO_MINIMO)
            );
        }

        if (nomeTrimmed.length() > Constantes.NOME_TAMANHO_MAXIMO) {
            throw new DadosInvalidosException(
                    String.format("Nome deve ter no máximo %d caracteres", Constantes.NOME_TAMANHO_MAXIMO)
            );
        }

        // Verifica se contém apenas letras e espaços
        if (!nomeTrimmed.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new DadosInvalidosException("Nome deve conter apenas letras e espaços");
        }
    }

    /**
     * Valida o CPF do cliente.
     * Programação Defensiva: Valida formato e dígitos verificadores.
     *
     * @param cpf CPF a ser validado
     * @throws CpfInvalidoException se CPF for inválido
     */
    public static void validarCpf(String cpf) throws CpfInvalidoException {
        Objects.requireNonNull(cpf, Constantes.ERRO_CPF_NULO);

        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        // Verifica tamanho
        if (cpfLimpo.length() != Constantes.CPF_TAMANHO) {
            throw new CpfInvalidoException(
                    String.format("CPF deve ter %d dígitos", Constantes.CPF_TAMANHO)
            );
        }

        // Verifica se todos os dígitos são iguais
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new CpfInvalidoException("CPF não pode ter todos os dígitos iguais");
        }

        // Valida dígitos verificadores
        if (!validarDigitosVerificadores(cpfLimpo)) {
            throw new CpfInvalidoException("CPF inválido: dígitos verificadores incorretos");
        }
    }

    /**
     * Valida os dígitos verificadores do CPF usando o algoritmo oficial.
     *
     * @param cpf CPF com 11 dígitos numéricos
     * @return true se dígitos verificadores estão corretos
     */
    private static boolean validarDigitosVerificadores(String cpf) {
        try {
            // Calcula primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;

            // Calcula segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            // Verifica se os dígitos calculados conferem
            return Character.getNumericValue(cpf.charAt(9)) == primeiroDigito
                    && Character.getNumericValue(cpf.charAt(10)) == segundoDigito;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Normaliza o CPF removendo caracteres especiais.
     *
     * @param cpf CPF a ser normalizado
     * @return CPF apenas com números
     */
    public static String normalizarCpf(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("[^0-9]", "");
    }
}
