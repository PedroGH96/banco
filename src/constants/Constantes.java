package constants;

/**
 * Classe que centraliza todas as constantes do sistema.
 * Boa Prática: Evita números mágicos e strings hardcoded.
 */
public final class Constantes {

    // ============= CONSTANTES DE CONTA =============
    public static final int NUMERO_CONTA_INICIAL = 1001;
    public static final double SALDO_MINIMO = 0.0;
    public static final double SALDO_MAXIMO = 1_000_000_000.0; // 1 bilhão

    // ============= CONSTANTES DE CLIENTE =============
    public static final int CPF_TAMANHO = 11;
    public static final int NOME_TAMANHO_MINIMO = 3;
    public static final int NOME_TAMANHO_MAXIMO = 100;

    // ============= TIPOS DE CONTA =============
    public static final String TIPO_CONTA_CORRENTE = "corrente";
    public static final String TIPO_CONTA_POUPANCA = "poupanca";

    // ============= MENSAGENS DE ERRO =============
    public static final String ERRO_CPF_NULO = "CPF não pode ser nulo";
    public static final String ERRO_NOME_NULO = "Nome não pode ser nulo";

    // ============= LIMITES DE OPERAÇÃO =============
    public static final double VALOR_MINIMO_OPERACAO = 0.01;
    public static final double VALOR_MAXIMO_OPERACAO = 100_000.0;

    // ============= PERCENTUAIS =============
    public static final double RENDIMENTO_MINIMO = 0.01;
    public static final double RENDIMENTO_MAXIMO = 50.0;

    /**
     * Construtor privado para prevenir instanciação.
     * Boa Prática: Classe utilitária não deve ser instanciada.
     */
    private Constantes() {
        throw new AssertionError("Classe Constantes não deve ser instanciada");
    }
}