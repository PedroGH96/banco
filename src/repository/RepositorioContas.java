package repository;

import model.Conta;
import model.ContaPoupanca;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do repositório de contas.
 * Boa Prática: SRP - Responsabilidade única de armazenar contas.
 */
public class RepositorioContas implements IRepositorioContas {
    private final List<Conta> contas;

    public RepositorioContas() {
        this.contas = new ArrayList<>();
    }

    @Override
    public void adicionar(Conta conta) {
        Objects.requireNonNull(conta, "Conta não pode ser nula");
        contas.add(conta);
    }

    @Override
    public Optional<Conta> buscarPorNumero(int numeroConta) {
        return contas.stream()
                .filter(c -> c.getNumero() == numeroConta)
                .findFirst();
    }

    @Override
    public boolean existe(int numeroConta) {
        return buscarPorNumero(numeroConta).isPresent();
    }

    @Override
    public List<Conta> listarTodas() {
        // Cópia defensiva
        return new ArrayList<>(contas);
    }

    @Override
    public List<Conta> listarOrdenadasPorSaldo() {
        return contas.stream()
                .sorted((c1, c2) -> Double.compare(c2.getSaldo(), c1.getSaldo()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Conta> listarContasPoupanca() {
        return contas.stream()
                .filter(c -> c instanceof ContaPoupanca)
                .collect(Collectors.toList());
    }
}
