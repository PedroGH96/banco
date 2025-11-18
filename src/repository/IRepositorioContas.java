package repository;

import model.Conta;

import java.util.List;
import java.util.Optional;

/**
 * Interface para repositório de contas.
 * Boa Prática: ISP - Interface específica e coesa.
 */
public interface IRepositorioContas {

    /**
     * Adiciona uma conta ao repositório.
     *
     * @param conta Conta a ser adicionada
     */
    void adicionar(Conta conta);

    /**
     * Busca conta por número.
     *
     * @param numeroConta Número da conta
     * @return Optional contendo conta se encontrada
     */
    Optional<Conta> buscarPorNumero(int numeroConta);

    /**
     * Verifica se conta existe.
     *
     * @param numeroConta Número da conta
     * @return true se existe
     */
    boolean existe(int numeroConta);

    /**
     * Retorna todas as contas.
     * Boa Prática: Retorna cópia defensiva.
     *
     * @return Lista de contas
     */
    List<Conta> listarTodas();

    /**
     * Retorna contas ordenadas por saldo (decrescente).
     *
     * @return Lista ordenada de contas
     */
    List<Conta> listarOrdenadasPorSaldo();

    /**
     * Retorna apenas contas poupança.
     *
     * @return Lista de contas poupança
     */
    List<Conta> listarContasPoupanca();
}