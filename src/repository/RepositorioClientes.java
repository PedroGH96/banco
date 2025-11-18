package repository;

import model.Cliente;
import exception.ClienteJaExisteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementação do repositório de clientes.
 * Boa Prática: SRP - Responsabilidade única de armazenar clientes.
 */
public class RepositorioClientes implements IRepositorioClientes {
    private final List<Cliente> clientes;

    public RepositorioClientes() {
        this.clientes = new ArrayList<>();
    }

    @Override
    public void adicionar(Cliente cliente) throws ClienteJaExisteException {
        Objects.requireNonNull(cliente, "Cliente não pode ser nulo");

        if (existe(cliente.getCpf())) {
            throw new ClienteJaExisteException(cliente.getCpf());
        }

        clientes.add(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        Objects.requireNonNull(cpf, "CPF não pode ser nulo");

        return clientes.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst();
    }

    @Override
    public boolean existe(String cpf) {
        return buscarPorCpf(cpf).isPresent();
    }

    @Override
    public List<Cliente> listarTodos() {
        // Cópia defensiva - Programação Defensiva
        return new ArrayList<>(clientes);
    }
}
