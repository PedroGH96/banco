package repository;

import model.Cliente;
import exception.ClienteJaExisteException;

import java.util.List;
import java.util.Optional;

/**
 * Interface para repositório de clientes.
 * Boa Prática: Interface Segregation Principle (ISP).
 * Dependency Inversion Principle (DIP) - Depender de abstração.
 */
public interface IRepositorioClientes {

    /**
     * Adiciona um cliente ao repositório.
     *
     * @param cliente Cliente a ser adicionado
     * @throws ClienteJaExisteException se cliente já existe
     */
    void adicionar(Cliente cliente) throws ClienteJaExisteException;

    /**
     * Busca cliente por CPF.
     *
     * @param cpf CPF do cliente
     * @return Optional contendo cliente se encontrado
     */
    Optional<Cliente> buscarPorCpf(String cpf);

    /**
     * Verifica se cliente existe.
     *
     * @param cpf CPF do cliente
     * @return true se existe
     */
    boolean existe(String cpf);

    /**
     * Retorna todos os clientes.
     * Boa Prática: Retorna cópia defensiva.
     *
     * @return Lista de clientes
     */
    List<Cliente> listarTodos();
}
