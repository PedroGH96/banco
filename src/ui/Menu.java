package ui;

import constants.Constantes;
import exception.*;
import model.Cliente;
import model.Conta;
import repository.IRepositorioContas;
import service.IOperacoesBancarias;
import service.RelatorioServico;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsável pela interface do usuário.
 * Boa Prática: SRP - Responsabilidade única de UI.
 */
public class Menu {
    private final IOperacoesBancarias operacoesBancarias;
    private final RelatorioServico relatorioServico;
    private final Scanner scanner;

    /**
     * Construtor com injeção de dependências.
     *
     * @param operacoesBancarias Serviço de operações bancárias
     * @param relatorioServico Serviço de relatórios
     */
    public Menu(IOperacoesBancarias operacoesBancarias, RelatorioServico relatorioServico) {
        this.operacoesBancarias = operacoesBancarias;
        this.relatorioServico = relatorioServico;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia o menu principal.
     */
    public void iniciar() {
        exibirBanner();

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcaoMenu();

            if (opcao != 0) {
                processarOpcao(opcao);
                aguardarContinuacao();
            }

        } while (opcao != 0);

        System.out.println("\nSaindo do sistema... Até logo!");
        scanner.close();
    }

    private void exibirBanner() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       SISTEMA BANCÁRIO SIMPLES         ║");
        System.out.println("║     Implementado com Boas Práticas     ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private void exibirMenu() {
        System.out.println("\n┌─────── MENU PRINCIPAL ────────┐");
        System.out.println("│ 1. Cadastrar Cliente          │");
        System.out.println("│ 2. Cadastrar Conta            │");
        System.out.println("│ 3. Realizar Depósito          │");
        System.out.println("│ 4. Realizar Saque             │");
        System.out.println("│ 5. Realizar Transferência     │");
        System.out.println("│ 6. Consultar Saldo            │");
        System.out.println("│ 7. Aplicar Rendimento         │");
        System.out.println("│ 8. Listar Contas              │");
        System.out.println("│ 9. Relatório Consolidado      │");
        System.out.println("│ 0. Sair                       │");
        System.out.println("└───────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Lê opção do menu com tratamento de erro.
     * Programação Defensiva: Try-catch para InputMismatchException.
     */
    private int lerOpcaoMenu() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa buffer
            return opcao;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpa buffer
            System.out.println("Erro: Digite um número válido!");
            return -1;
        }
    }

    private void processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1: cadastrarCliente(); break;
                case 2: cadastrarConta(); break;
                case 3: realizarDeposito(); break;
                case 4: realizarSaque(); break;
                case 5: realizarTransferencia(); break;
                case 6: consultarSaldo(); break;
                case 7: aplicarRendimento(); break;
                case 8: listarContas(); break;
                case 9: exibirRelatorio(); break;
                default: System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // ========== MÉTODOS DE CADASTRO ==========

    private void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");

        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("CPF (apenas números): ");
            String cpf = scanner.nextLine();

            Cliente cliente = operacoesBancarias.cadastrarCliente(nome, cpf);
            System.out.println("Cliente cadastrado: " + cliente.getNome());

        } catch (BancoException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    private void cadastrarConta() {
        System.out.println("\n=== CADASTRAR CONTA ===");

        try {
            // Seleção de cliente via menu numérico
            String cpf = selecionarCliente();
            if (cpf == null) {
                System.out.println("Operação cancelada.");
                return;
            }

            // Seleção de tipo de conta via menu numérico
            String tipo = selecionarTipoConta();
            if (tipo == null) {
                System.out.println("Operação cancelada.");
                return;
            }

            System.out.print("Saldo inicial: R$ ");
            double saldo = lerDouble();

            Conta conta = operacoesBancarias.cadastrarConta(cpf, tipo, saldo);
            System.out.printf("Conta %s criada! Número: %d%n",
                    conta.getTipo(), conta.getNumero());

        } catch (BancoException | IllegalArgumentException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    /**
     * Exibe lista de clientes e permite seleção por número.
     * Boa Prática: Interface mais robusta, evita erros de digitação de CPF.
     *
     * @return CPF do cliente selecionado ou null se opção inválida
     */
    private String selecionarCliente() {
        List<Cliente> clientes = operacoesBancarias.listarClientes();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado. Cadastre um cliente primeiro.");
            return null;
        }

        System.out.println("\nSelecione o cliente:");
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            System.out.printf("%d. %s - CPF: %s%n",
                    (i + 1), cliente.getNome(), cliente.getCpfFormatado());
        }
        System.out.print("\nOpção: ");

        try {
            int opcao = lerInt();

            if (opcao < 1 || opcao > clientes.size()) {
                System.out.printf("Opção inválida! Selecione um número entre 1 e %d.%n", clientes.size());
                return null;
            }

            return clientes.get(opcao - 1).getCpf();
        } catch (IllegalArgumentException e) {
            System.out.println("Erro " + e.getMessage());
            return null;
        }
    }

    /**
     * Exibe menu de seleção de tipo de conta e retorna o tipo selecionado.
     * Boa Prática: Interface mais robusta, evita erros de digitação.
     *
     * @return Tipo de conta selecionado ou null se opção inválida
     */
    private String selecionarTipoConta() {
        System.out.println("\nSelecione o tipo de conta:");
        System.out.println("1. Conta Corrente");
        System.out.println("2. Conta Poupança");
        System.out.print("Opção: ");

        try {
            int opcao = lerInt();
            
            switch (opcao) {
                case 1:
                    return Constantes.TIPO_CONTA_CORRENTE;
                case 2:
                    return Constantes.TIPO_CONTA_POUPANCA;
                default:
                    System.out.println("Opção inválida! Selecione 1 ou 2.");
                    return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro " + e.getMessage());
            return null;
        }
    }

    // ========== MÉTODOS DE OPERAÇÃO ==========

    private void realizarDeposito() {
        System.out.println("\n=== REALIZAR DEPÓSITO ===");

        try {
            System.out.print("Número da conta: ");
            int numero = lerInt();

            System.out.print("Valor: R$ ");
            double valor = lerDouble();

            operacoesBancarias.depositar(numero, valor);
            System.out.printf("Depósito de R$ %.2f realizado!%n", valor);

        } catch (BancoException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    private void realizarSaque() {
        System.out.println("\n=== REALIZAR SAQUE ===");

        try {
            System.out.print("Número da conta: ");
            int numero = lerInt();

            System.out.print("Valor: R$ ");
            double valor = lerDouble();

            operacoesBancarias.sacar(numero, valor);
            System.out.printf("Saque de R$ %.2f realizado!%n", valor);

        } catch (BancoException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    private void realizarTransferencia() {
        System.out.println("\n=== REALIZAR TRANSFERÊNCIA ===");

        try {
            System.out.print("Conta origem: ");
            int origem = lerInt();

            System.out.print("Conta destino: ");
            int destino = lerInt();

            System.out.print("Valor: R$ ");
            double valor = lerDouble();

            operacoesBancarias.transferir(origem, destino, valor);
            System.out.printf("Transferência de R$ %.2f realizada!%n", valor);

        } catch (BancoException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    private void consultarSaldo() {
        System.out.println("\n=== CONSULTAR SALDO ===");

        try {
            System.out.print("Número da conta: ");
            int numero = lerInt();

            double saldo = operacoesBancarias.consultarSaldo(numero);
            System.out.printf("Saldo atual: R$ %.2f%n", saldo);

        } catch (BancoException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    private void aplicarRendimento() {
        System.out.println("\n=== APLICAR RENDIMENTO ===");

        try {
            System.out.print("Taxa de rendimento (%): ");
            double taxa = lerDouble();

            int contasAtualizadas = operacoesBancarias.aplicarRendimentoPoupancas(taxa);
            System.out.printf("Rendimento de %.2f%% aplicado em %d conta(s)!%n",
                    taxa, contasAtualizadas);

        } catch (BancoException e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    // ========== MÉTODOS DE CONSULTA ==========

    private void listarContas() {
        List<Conta> contas = operacoesBancarias.listarContasOrdenadasPorSaldo();

        if (contas.isEmpty()) {
            System.out.println("\nNenhuma conta cadastrada.");
            return;
        }

        System.out.println("\n╔═══ CONTAS CADASTRADAS (ordenadas por saldo) ═══╗");
        for (int i = 0; i < contas.size(); i++) {
            Conta conta = contas.get(i);
            System.out.printf("│ %2d │ %-15s │ Nº %4d │ %-20s │ R$ %10.2f │%n",
                    (i + 1), conta.getTipo(), conta.getNumero(),
                    conta.getNomeCliente(), conta.getSaldo());
        }
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Exibe relatório consolidado do banco.
     * Boa Prática: Evita instanceof/cast usando interface.
     */
    private void exibirRelatorio() {
        try {
            repository.IRepositorioContas repositorioContas = operacoesBancarias.getRepositorioContas();
            relatorioServico.gerarRelatorioConsolidacao(repositorioContas);
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Lê inteiro com tratamento de erro.
     * Programação Defensiva: Try-catch.
     */
    private int lerInt() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Valor inválido. Digite um número inteiro.");
        }
    }

    /**
     * Lê double com tratamento de erro.
     * Programação Defensiva: Try-catch.
     */
    private double lerDouble() {
        try {
            double valor = scanner.nextDouble();
            scanner.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Valor inválido. Digite um número decimal.");
        }
    }

    private void aguardarContinuacao() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
