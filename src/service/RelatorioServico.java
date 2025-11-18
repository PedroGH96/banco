package service;

import model.Conta;
import repository.IRepositorioContas;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço para geração de relatórios.
 * Boa Prática: SRP - Responsabilidade única de gerar relatórios.
 */
public class RelatorioServico {

    /**
     * Gera relatório de consolidação do banco.
     *
     * @param repositorioContas Repositório de contas
     */
    public void gerarRelatorioConsolidacao(IRepositorioContas repositorioContas) {
        List<Conta> contas = repositorioContas.listarTodas();

        if (contas.isEmpty()) {
            System.out.println("\n=== RELATÓRIO DE CONSOLIDAÇÃO ===");
            System.out.println("Nenhuma conta cadastrada.");
            System.out.println("==================================\n");
            return;
        }

        // Agrupa contas por tipo
        Map<String, List<Conta>> contasPorTipo = contas.stream()
                .collect(Collectors.groupingBy(Conta::getTipo));

        System.out.println("\n=== RELATÓRIO DE CONSOLIDAÇÃO ===");

        // Exibe informações por tipo
        contasPorTipo.forEach((tipo, contasDoTipo) -> {
            double saldoTotalTipo = calcularSaldoTotal(contasDoTipo);
            System.out.printf("%-15s | Quantidade: %3d | Saldo Total: R$ %12.2f%n",
                    tipo, contasDoTipo.size(), saldoTotalTipo);
        });

        // Exibe totais gerais
        double saldoTotalBanco = calcularSaldoTotal(contas);
        System.out.println("-".repeat(60));
        System.out.printf("%-15s | Quantidade: %3d | Saldo Total: R$ %12.2f%n",
                "TOTAL GERAL", contas.size(), saldoTotalBanco);
        System.out.println("==================================\n");
    }

    /**
     * Calcula saldo total de uma lista de contas.
     * Boa Prática: Método privado reutilizável.
     *
     * @param contas Lista de contas
     * @return Saldo total
     */
    private double calcularSaldoTotal(List<Conta> contas) {
        return contas.stream()
                .mapToDouble(Conta::getSaldo)
                .sum();
    }
}