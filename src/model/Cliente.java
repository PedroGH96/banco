package model;

import exception.CpfInvalidoException;
import exception.DadosInvalidosException;
import validator.ClienteValidator;

import java.util.Objects;

/**
 * Representa um cliente do banco.
 * Boa Prática: Imutabilidade e Encapsulamento.
 */
public final class Cliente {
    private final String nome;
    private final String cpf;

    /**
     * Construtor com validação defensiva.
     * Programação Defensiva: Valida todos os parâmetros.
     *
     * @param nome Nome do cliente
     * @param cpf CPF do cliente
     * @throws DadosInvalidosException se nome for inválido
     * @throws CpfInvalidoException se CPF for inválido
     */
    public Cliente(String nome, String cpf) throws DadosInvalidosException, CpfInvalidoException {
        // Validações defensivas
        ClienteValidator.validarNome(nome);
        ClienteValidator.validarCpf(cpf);

        // Invariante: nome e CPF nunca serão nulos ou inválidos
        assert nome != null && !nome.trim().isEmpty() : "Nome inválido após validação";
        assert cpf != null && !cpf.trim().isEmpty() : "CPF inválido após validação";

        this.nome = nome.trim();
        this.cpf = ClienteValidator.normalizarCpf(cpf);

        // Pós-condição
        validarInvariante();
    }

    /**
     * Valida invariantes da classe.
     * Assertiva: Garante que o estado do objeto é sempre válido.
     */
    private void validarInvariante() {
        assert nome != null && !nome.isEmpty() : "Nome não pode ser nulo ou vazio";
        assert cpf != null && cpf.length() == 11 : "CPF deve ter 11 dígitos";
    }

    /**
     * Retorna o nome do cliente.
     * Lei de Demeter: Não expõe estrutura interna.
     *
     * @return Nome do cliente
     */
    public String getNome() {
        validarInvariante();
        return nome;
    }

    /**
     * Retorna o CPF do cliente (apenas números).
     *
     * @return CPF do cliente
     */
    public String getCpf() {
        validarInvariante();
        return cpf;
    }

    /**
     * Retorna o CPF formatado (XXX.XXX.XXX-XX).
     * Boa Prática: Método de comportamento em vez de expor dado bruto.
     *
     * @return CPF formatado
     */
    public String getCpfFormatado() {
        validarInvariante();
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11)
        );
    }

    /**
     * Verifica se dois clientes são iguais (pelo CPF).
     * Boa Prática: Equals baseado em identificador único.
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Cliente cliente = (Cliente) objeto;
        return cpf.equals(cliente.cpf);
    }

    /**
     * HashCode baseado no CPF.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    /**
     * Representação em String do cliente.
     */
    @Override
    public String toString() {
        return String.format("Cliente{nome='%s', cpf='%s'}", nome, getCpfFormatado());
    }
}