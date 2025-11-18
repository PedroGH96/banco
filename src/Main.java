import repository.IRepositorioClientes;
import repository.IRepositorioContas;
import repository.RepositorioClientes;
import repository.RepositorioContas;
import service.BancoServico;
import service.IOperacoesBancarias;
import service.RelatorioServico;
import ui.Menu;

/**
 * Classe principal do sistema bancário.
 * Boa Prática: Responsabilidade única de inicializar sistema.
 * Boas Práticas Aplicadas:
 * Clean Code - Código limpo e legível
 * Nomes Significativos - Nomes descritivos
 * Funções Pequenas - Métodos coesos
 * Tratamento de Erros - Exceções personalizadas
 * Limites - Validações e constantes
 * Lei de Demeter - Evita cadeias de chamadas
 * SOLID - Todos os 5 princípios
 * Programação Defensiva - Validações robustas
 * Assertivas - Validação de invariantes
 *
 * @author Pedro Henrique de Araujo Alves
 * @author Samuel Evaristo de Fontes
 */
public class Main {

    /**
     * Método principal - Bootstrap da aplicação.
     * Boa Prática: Injeção de Dependências manual (DIP).
     */
    public static void main(String[] args) {
        try {
            // Inicializa sistema
            Main aplicacao = new Main();
            aplicacao.executar();

        } catch (Exception e) {
            System.err.println("Erro fatal no sistema: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Executa a aplicação.
     * Boa Prática: Separação de responsabilidades.
     */
    private void executar() {
        // Cria dependências (camada de infraestrutura)
        IRepositorioClientes repositorioClientes = new RepositorioClientes();
        IRepositorioContas repositorioContas = new RepositorioContas();

        // Cria serviços (camada de negócio) com injeção de dependências
        IOperacoesBancarias operacoesBancarias = new BancoServico(
                repositorioClientes,
                repositorioContas
        );
        RelatorioServico relatorioServico = new RelatorioServico();

        // Cria UI (camada de apresentação) com injeção de dependências
        Menu menuUI = new Menu(operacoesBancarias, relatorioServico);

        // Inicia aplicação
        menuUI.iniciar();
    }
}