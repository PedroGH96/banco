package service;

import constants.Constantes;
import exception.*;
import factory.ContaFactory;
import model.Cliente;
import model.Conta;
import model.ContaPoupanca;
import repository.IRepositorioClientes;
import repository.IRepositorioContas;

import java.util.List;
import java.util.Objects;

/**
 * Serviço bancário que implementa operações bancárias.
 * Boa Prática: SRP, DIP, Programação Defensiva.
 */
public class BancoServico implements IOperacoesBancarias {
    private final IRepositorioClientes repositorioClientes;
    private final IRepositorioContas repositorioContas;
    private int proximoNumeroConta;

    /**
     * Construtor com injeção de dependências.
     * Boa Prática: DIP - Depende de abstrações (interfaces).
     *
     * @param repositorioClientes Repositório de clientes
     * @param repositorioContas Repositório de contas
     */
    public BancoServico(IRepositorioClientes repositorioClientes,
                        IRepositorioContas repositorioContas) {
        this.repositorioClientes = Objects.requireNonNull(repositorioClientes,
                "Repositório de clientes não pode ser nulo");
        this.repositorioContas = Objects.requireNonNull(repositorioContas,
                "Repositório de contas não pode ser nulo");
        this.proximoNumeroConta = Constantes.NUMERO_CONTA_INICIAL;
    }

    @Override
    public Cliente cadastrarCliente(String nome, String cpf)
            throws DadosInvalidosException, CpfInvalidoException, ClienteJaExisteException {
        // Programação Defensiva: Validações já estão no construtor de Cliente
        Cliente novoCliente = new Cliente(nome, cpf);
        repositorioClientes.adicionar(novoCliente);

        return novoCliente;
    }

    @Override
    public Conta cadastrarConta(String cpfCliente, String tipoConta, double saldoInicial)
            throws ClienteNaoEncontradoException, TipoContaInvalidoException, DadosInvalidosException {
        // Busca cliente
        Cliente cliente = repositorioClientes.buscarPorCpf(cpfCliente)
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpfCliente));

        // Gera número único e cria conta usando Factory
        int numeroConta = gerarProximoNumeroConta();
        Conta novaConta = ContaFactory.criarConta(numeroConta, cliente, tipoConta, saldoInicial);

        // Adiciona ao repositório
        repositorioContas.adicionar(novaConta);

        return novaConta;
    }

    @Override
    public void depositar(int numeroConta, double valor)
            throws ContaNaoEncontradaException, ValorInvalidoException {
        Conta conta = buscarContaOuLancarExcecao(numeroConta);
        conta.depositar(valor);
    }

    @Override
    public void sacar(int numeroConta, double valor)
            throws ContaNaoEncontradaException, ValorInvalidoException, SaldoInsuficienteException {
        Conta conta = buscarContaOuLancarExcecao(numeroConta);
        conta.sacar(valor);
    }

    @Override
    public void transferir(int numeroContaOrigem, int numeroContaDestino, double valor)
            throws ContaNaoEncontradaException, ValorInvalidoException, SaldoInsuficienteException {
        // Validação adicional
        if (numeroContaOrigem == numeroContaDestino) {
            throw new IllegalArgumentException("Conta origem e destino não podem ser iguais");
        }

        Conta contaOrigem = buscarContaOuLancarExcecao(numeroContaOrigem);
        Conta contaDestino = buscarContaOuLancarExcecao(numeroContaDestino);

        contaOrigem.transferir(contaDestino, valor);
    }

    @Override
    public double consultarSaldo(int numeroConta) throws ContaNaoEncontradaException {
        Conta conta = buscarContaOuLancarExcecao(numeroConta);
        return conta.getSaldo();
    }

    @Override
    public int aplicarRendimentoPoupancas(double percentual) throws ValorInvalidoException {
        List<Conta> contasPoupanca = repositorioContas.listarContasPoupanca();

        int contasAtualizadas = 0;
        for (Conta conta : contasPoupanca) {
            if (conta instanceof ContaPoupanca) {
                ContaPoupanca poupanca = (ContaPoupanca) conta;
                poupanca.aplicarRendimento(percentual);
                contasAtualizadas++;
            }
        }

        return contasAtualizadas;
    }

    @Override
    public List<Conta> listarContasOrdenadasPorSaldo() {
        return repositorioContas.listarOrdenadasPorSaldo();
    }

    @Override
    public List<Cliente> listarClientes() {
        return repositorioClientes.listarTodos();
    }

    /**
     * Busca conta ou lança exceção se não encontrada.
     * Boa Prática: Método privado para evitar duplicação.
     *
     * @param numeroConta Número da conta
     * @return Conta encontrada
     * @throws ContaNaoEncontradaException se não encontrada
     */
    private Conta buscarContaOuLancarExcecao(int numeroConta)
            throws ContaNaoEncontradaException {
        return repositorioContas.buscarPorNumero(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));
    }

    /**
     * Gera próximo número de conta de forma thread-safe.
     *
     * @return Próximo número de conta
     */
    private synchronized int gerarProximoNumeroConta() {
        return proximoNumeroConta++;
    }

    /**
     * Retorna repositório de contas (para relatórios).
     * Boa Prática: Implementação da interface, expõe apenas a abstração.
     *
     * @return Repositório de contas
     */
    @Override
    public IRepositorioContas getRepositorioContas() {
        return repositorioContas;
    }
}