package service;

import exception.*;
import model.Cliente;
import model.Conta;

import java.util.List;

/**
 * Interface para operações bancárias.
 * Boa Prática: ISP + DIP - Interface segregada e inversão de dependência.
 */
public interface IOperacoesBancarias {

    /**
     * Cadastra um novo cliente.
     *
     * @param nome Nome do cliente
     * @param cpf CPF do cliente
     * @return Cliente cadastrado
     * @throws DadosInvalidosException se dados forem inválidos
     * @throws CpfInvalidoException se CPF for inválido
     * @throws ClienteJaExisteException se cliente já existe
     */
    Cliente cadastrarCliente(String nome, String cpf)
            throws DadosInvalidosException, CpfInvalidoException, ClienteJaExisteException;

    /**
     * Cadastra uma nova conta.
     *
     * @param cpfCliente CPF do cliente
     * @param tipoConta Tipo da conta
     * @param saldoInicial Saldo inicial
     * @return Conta cadastrada
     * @throws ClienteNaoEncontradoException se cliente não existe
     * @throws TipoContaInvalidoException se tipo for inválido
     * @throws DadosInvalidosException se dados forem inválidos
     */
    Conta cadastrarConta(String cpfCliente, String tipoConta, double saldoInicial)
            throws ClienteNaoEncontradoException, TipoContaInvalidoException, DadosInvalidosException;

    /**
     * Realiza depósito em uma conta.
     *
     * @param numeroConta Número da conta
     * @param valor Valor do depósito
     * @throws ContaNaoEncontradaException se conta não existe
     * @throws ValorInvalidoException se valor for inválido
     */
    void depositar(int numeroConta, double valor)
            throws ContaNaoEncontradaException, ValorInvalidoException;

    /**
     * Realiza saque de uma conta.
     *
     * @param numeroConta Número da conta
     * @param valor Valor do saque
     * @throws ContaNaoEncontradaException se conta não existe
     * @throws ValorInvalidoException se valor for inválido
     * @throws SaldoInsuficienteException se saldo for insuficiente
     */
    void sacar(int numeroConta, double valor)
            throws ContaNaoEncontradaException, ValorInvalidoException, SaldoInsuficienteException;

    /**
     * Realiza transferência entre contas.
     *
     * @param numeroContaOrigem Número da conta origem
     * @param numeroContaDestino Número da conta destino
     * @param valor Valor da transferência
     * @throws ContaNaoEncontradaException se alguma conta não existe
     * @throws ValorInvalidoException se valor for inválido
     * @throws SaldoInsuficienteException se saldo for insuficiente
     */
    void transferir(int numeroContaOrigem, int numeroContaDestino, double valor)
            throws ContaNaoEncontradaException, ValorInvalidoException, SaldoInsuficienteException;

    /**
     * Consulta saldo de uma conta.
     *
     * @param numeroConta Número da conta
     * @return Saldo da conta
     * @throws ContaNaoEncontradaException se conta não existe
     */
    double consultarSaldo(int numeroConta) throws ContaNaoEncontradaException;

    /**
     * Aplica rendimento em todas as contas poupança.
     *
     * @param percentual Percentual de rendimento
     * @return Quantidade de contas atualizadas
     * @throws ValorInvalidoException se percentual for inválido
     */
    int aplicarRendimentoPoupancas(double percentual) throws ValorInvalidoException;

    /**
     * Lista todas as contas ordenadas por saldo.
     *
     * @return Lista de contas
     */
    List<Conta> listarContasOrdenadasPorSaldo();

    /**
     * Lista todos os clientes.
     *
     * @return Lista de clientes
     */
    List<Cliente> listarClientes();

    /**
     * Retorna o repositório de contas (para relatórios).
     * Boa Prática: Expõe apenas a interface, não a implementação.
     *
     * @return Repositório de contas
     */
    repository.IRepositorioContas getRepositorioContas();
}