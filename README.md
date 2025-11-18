# Sistema Bancário Simples

## Integrantes da Equipe

**Pedro Henrique de Araujo Alves**

**Samuel Evaristo de Fontes**

---

## Sobre o Sistema

Sistema bancário desenvolvido em Java aplicando boas práticas de programação, incluindo Clean Code, SOLID, Lei de Demeter, Programação Defensiva e Assertivas. O sistema simula operações financeiras básicas de um banco com arquitetura robusta e código profissional.

### Objetivo Geral

Implementar um sistema bancário seguindo rigorosamente princípios de engenharia de software e boas práticas de programação, demonstrando conhecimento em:

- Clean Code e Code Smells
- Princípios SOLID
- Design Patterns (Factory, Repository)
- Tratamento robusto de exceções
- Programação defensiva
- Testes de invariantes com assertivas

---

## Funcionalidades

- Cadastro de Clientes com validação completa de CPF
- Cadastro de Contas (Corrente e Poupança)
- Depósito com validações de limite
- Saque com verificação de saldo
- Transferência entre contas
- Consulta de Saldo
- Aplicação de Rendimento em contas poupança
- Listagem de Contas ordenada por saldo
- Relatório de Consolidação detalhado

---

## Arquitetura do Projeto

```
sistema-bancario-v2/
│
├── constants/
│   └── Constantes.java              # Centraliza constantes (evita números mágicos)
│
├── exception/                        # Exceções personalizadas
│   ├── BancoException.java          # Exceção base
│   ├── ClienteJaExisteException.java
│   ├── ContaNaoEncontradaException.java
│   ├── SaldoInsuficienteException.java
│   ├── ClienteNaoEncontradoException.java
│   ├── CpfInvalidoException.java
│   ├── DadosInvalidosException.java
│   ├── ValorInvalidoException.java
│   └── TipoContaInvalidoException.java
│
├── model/                            # Entidades do domínio
│   ├── Cliente.java                 # Classe imutável com validações
│   ├── Conta.java                   # Classe abstrata com Template Method
│   ├── ContaCorrente.java           # Herança LSP-compliant
│   └── ContaPoupanca.java           # Com aplicação de rendimento
│
├── validator/                        # Validadores (SRP)
│   ├── ClienteValidator.java        # Valida nome e CPF
│   └── ContaValidator.java          # Valida operações
│
├── factory/
│   └── ContaFactory.java            # Factory Pattern (OCP)
│
├── repository/                       # Camada de persistência
│   ├── IRepositorioClientes.java    # Interface (ISP + DIP)
│   ├── IRepositorioContas.java      # Interface (ISP + DIP)
│   ├── RepositorioClientes.java     # Implementação
│   └── RepositorioContas.java       # Implementação
│
├── service/                          # Lógica de negócio
│   ├── IOperacoesBancarias.java     # Interface (ISP + DIP)
│   ├── BancoServico.java            # Orquestrador principal
│   └── RelatorioServico.java        # Geração de relatórios (SRP)
│
├── ui/
│   └── Menu.java                    # Interface do usuário (SRP)
│
└── Main.java                        # Bootstrap da aplicação
```

---

## Boas Práticas Implementadas

### 1. Clean Code

#### 1.1 Código Limpo

- Classes pequenas e coesas (< 200 linhas)
- Métodos pequenos (< 20 linhas)
- Sem duplicação de código
- Comentários JavaDoc em todas as classes públicas

#### 1.2 Nomes Significativos

```java
// ANTES (ruim)
int n;
double v;
String t;

// DEPOIS (bom)
int numeroConta;
double valorDeposito;
String tipoConta;
```

#### 1.3 Funções

- Cada função faz UMA coisa
- Máximo 3 parâmetros por função
- Sem efeitos colaterais
- Separação entre comandos e consultas

#### 1.4 Tratamento de Erros

```java
// Exceções personalizadas em vez de códigos de erro
public void sacar(double valor) throws SaldoInsuficienteException {
    if (saldo < valor) {
        throw new SaldoInsuficienteException(numeroConta, saldo, valor);
    }
    saldo -= valor;
}
```

#### 1.5 Limites

```java
// Constantes para limites
public static final int CPF_TAMANHO = 11;
public static final double SALDO_MAXIMO = 1_000_000_000.0;
public static final double VALOR_MINIMO_OPERACAO = 0.01;
```

---

### 2. Lei de Demeter

**Princípio:** "Não fale com estranhos"

```java
// ANTES (violação)
String nome = conta.getCliente().getNome();

// DEPOIS (correto)
String nome = conta.getNomeCliente(); // Método delegado
```

**Implementação:**
- `Conta.getNomeCliente()` encapsula acesso ao cliente
- Nenhuma cadeia de chamadas exposta
- Objetos conhecem apenas seus vizinhos diretos

---

### 3. SOLID

#### S - Single Responsibility Principle

```java
// Cada classe tem UMA responsabilidade
ClienteValidator     → Valida clientes
ContaValidator       → Valida operações de conta
Menu                → Interface do usuário
BancoServico        → Orquestra operações
RelatorioServico    → Gera relatórios
```

#### O - Open/Closed Principle

```java
// Factory Pattern - Aberto para extensão, fechado para modificação
public static Conta criarConta(int numero, Cliente cliente, String tipo, double saldo) {
    switch (tipo) {
        case "corrente": return new ContaCorrente(...);
        case "poupanca": return new ContaPoupanca(...);
        // Fácil adicionar novos tipos sem modificar código existente
    }
}
```

#### L - Liskov Substitution Principle 

```java
// ContaCorrente e ContaPoupanca podem substituir Conta sem quebrar
Conta conta = new ContaPoupanca(...);  // Funciona perfeitamente
conta.depositar(100);                   // Comportamento consistente
```

#### I - Interface Segregation Principle 

```java
// Interfaces específicas e coesas
interface IRepositorioClientes { ... }  // Apenas operações de clientes
interface IRepositorioContas { ... }    // Apenas operações de contas
interface IOperacoesBancarias { ... }   // Apenas operações bancárias
```

#### D - Dependency Inversion Principle 

```java
// Dependência de abstrações (interfaces), não de implementações
public class BancoServico implements IOperacoesBancarias {
    private final IRepositorioClientes repositorioClientes;
    private final IRepositorioContas repositorioContas;
    
    // Injeção de dependências via construtor
    public BancoServico(IRepositorioClientes clientes, IRepositorioContas contas) {
        this.repositorioClientes = clientes;
        this.repositorioContas = contas;
    }
}
```

---

### 4. Programação Defensiva

#### Validação de Parâmetros

```java
// Validação completa de entradas
public Cliente(String nome, String cpf) throws DadosInvalidosException, CpfInvalidoException {
    Objects.requireNonNull(nome, "Nome não pode ser nulo");
    ClienteValidator.validarNome(nome);
    ClienteValidator.validarCpf(cpf);
    // ...
}
```

#### Proteção contra Valores Especiais

```java
// Valida NaN, Infinito, negativos
private static void validarValorNumerico(double valor, String nomeCampo) {
    if (Double.isNaN(valor)) {
        throw new DadosInvalidosException(nomeCampo + " não é um número válido");
    }
    if (Double.isInfinite(valor)) {
        throw new DadosInvalidosException(nomeCampo + " não pode ser infinito");
    }
    // ...
}
```

#### Cópias Defensivas

```java
// Retorna cópia, não referência original
public List<Cliente> listarTodos() {
    return new ArrayList<>(clientes);
}
```

#### Validação de CPF com Algoritmo Oficial

```java
// Valida dígitos verificadores do CPF
private static boolean validarDigitosVerificadores(String cpf) {
    // Implementa algoritmo oficial de validação de CPF
    // ...
}
```

---

### 5. Assertivas

#### Invariantes de Classe

```java
// Garante que estado do objeto é sempre válido
protected void validarInvariante() {
    assert numeroConta > 0 : "Número da conta deve ser positivo";
    assert cliente != null : "Cliente não pode ser nulo";
    assert saldo >= 0 : "Saldo não pode ser negativo: " + saldo;
    assert !Double.isNaN(saldo) : "Saldo não pode ser NaN";
}
```

#### Pré e Pós-condições

```java
// Valida antes e depois de operações críticas
public void sacar(double valor) throws ... {
    validarInvariante();  // Pré-condição
    double saldoAnterior = saldo;
    
    // Operação
    saldo -= valor;
    
    // Assertiva: Saldo foi atualizado corretamente
    assert saldo == (saldoAnterior - valor) : "Cálculo incorreto";
    
    validarInvariante();  // Pós-condição
}
```

---

## 🔧 Como Compilar

### Pré-requisitos

- Java JDK 8 ou superior
- Terminal/Prompt de Comando
---

### 🧱 Compilar o projeto (Windows)

Abra o **Prompt de Comando (cmd)** na pasta do projeto e execute:

```cmd
dir /s /b *.java > sources.txt
javac -d bin @sources.txt
```

## ▶Como Executar

### Execute:
```cmd
java -cp bin Main
```

### Usando assertivas:

```cmd
java -ea -cp bin Main
```

---

## Guia de Uso

### Fluxo Recomendado

#### Cadastrar Clientes (Opção 1)
- Nome: mínimo 3 caracteres
- CPF: 11 dígitos (validação algorítmica)

#### Cadastrar Contas (Opção 2)
- Informar CPF do cliente
- Escolher tipo: 1 - corrente ou 2 - poupança
- Saldo inicial: R$ 0,00 a R$ 1.000.000.000,00

#### Realizar Operações (Opções 3-7)
- Valores entre R$ 0,01 e R$ 100.000,00
- Todas as operações validadas

#### Consultar e Relatar (Opções 8-9)
- Listar contas ordenadas por saldo
- Relatório consolidado por tipo

---

### Validações Implementadas

- **Nome:** 3-100 caracteres, apenas letras  
- **CPF:** 11 dígitos + validação de dígitos verificadores  
- **Saldo:** R$ 0,00 a R$ 1.000.000.000,00  
- **Valores de operação:** R$ 0,01 a R$ 100.000,00  
- **Rendimento:** 0,01% a 50%  
- **Números especiais:** Rejeita NaN e Infinito  

---

## Testes de Invariantes

As assertivas verificam:
- Saldo nunca negativo
- CPF sempre com 11 dígitos
- Número de conta sempre positivo
- Valores numéricos válidos (não NaN/Infinito)

O sistema utiliza assertivas para garantir que o estado dos objetos seja sempre válido:

```bash
# Executar com assertivas habilitadas
java -ea -cp out Main
```

---

## Princípios de Design Aplicados

| Princípio | Aplicação | Benefício |
|-----------|-----------|-----------|
| **SRP** | Cada classe tem uma responsabilidade | Manutenibilidade |
| **OCP** | Factory Pattern para contas | Extensibilidade |
| **LSP** | Herança correta de Conta | Polimorfismo seguro |
| **ISP** | Interfaces segregadas | Coesão |
| **DIP** | Injeção de dependências | Testabilidade |
| **Lei de Demeter** | Sem cadeias de chamadas | Baixo acoplamento |
| **Imutabilidade** | Cliente é final | Thread-safety |
| **Fail-Fast** | Validações no construtor | Segurança |

---

## Conceitos Acadêmicos Demonstrados

### Clean Code
- Nomes significativos
- Funções pequenas
- Comentários JavaDoc
- Formatação consistente
- Sem duplicação

### Tratamento de Erros
- Hierarquia de exceções
- Exceções checked para erros recuperáveis
- Mensagens descritivas
- Try-catch em UI

### Programação Defensiva
- Validação de todos os parâmetros
- Proteção contra null
- Proteção contra valores especiais
- Cópias defensivas
- Validação de CPF

### SOLID
- Todos os 5 princípios aplicados
- Arquitetura em camadas
- Injeção de dependências
- Interfaces bem definidas

### Design Patterns
- Factory Pattern
- Repository Pattern
- Template Method Pattern
- Dependency Injection
