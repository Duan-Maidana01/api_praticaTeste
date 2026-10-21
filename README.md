# Trabalho Acadêmico-Prático: Estratégias de Teste em APIs REST com Java e Spring Boot

**Disciplina:** Garantia de Qualidade e Testes de Software  
**Modalidade:** Individual / Duplas  
**Tecnologias:** Java 21, Spring Boot, JUnit 5, Mockito, MockMvc, H2 Database, JaCoCo  

---

## Sumário

* **Parte I: Fundamentação Teórica e Mapeamento Spring Boot**
  1. Introdução aos Testes de Software
  2. Testes Unitários (Unit Testing)
  3. Testes de Sistema e Integração (System/Integration Testing)
  4. Teste de Caixa Preta (Black-box Testing)
  5. Teste de Caixa Branca (White-box Testing)
* **Parte II: Execução Prática e Evidências**
  * Como Executar os Testes
  * Relatório de Cobertura JaCoCo
  * Comandos para Sincronização Git

---

## Parte I: Fundamentação Teórica e Mapeamento Spring Boot

### 1. Introdução aos Testes de Software

#### 1.1. Defeito, Erro e Falha
Para entender o impacto de um problema no software, é necessário diferenciar três conceitos centrais:
* **Erro:** É o engano cometido pelo desenvolvedor durante a escrita ou planejamento do código (ex.: esquecer de validar se um objeto é `null` ou inverter uma condição num bloco `if`).
* **Defeito (Bug):** É a presença do erro diretamente no código-fonte da aplicação (ex.: a linha de código incorreta salva no repositório).
* **Falha:** É o comportamento incorreto observado quando o sistema está em execução e o defeito é ativado (ex.: a API retornar um status HTTP `500 Internal Server Error` ou calcular um valor incorreto ao receber uma requisição).

#### 1.2. Pirâmide de Testes
A Pirâmide de Testes orienta como os testes automatizados devem ser distribuídos em um projeto:
* **Base (Testes Unitários):** Devem representar a maior parte da suíte. Executam rapidamente, possuem baixo custo de manutenção e rodam em isolamento total.
* **Meio (Testes de Integração):** Validam a comunicação entre diferentes componentes (ex.: a camada de serviço conversando com o banco de dados).
* **Topo (Testes de Sistema / E2E):** Testam o fluxo completo de ponta a ponta. Oferecem alta fidelidade, mas são mais lentos e custosos para manter.

#### 1.3. Ciclo de Feedback Rápido e CI/CD
A execução automatizada de testes integrada em esteiras de Integração Contínua (CI/CD) garante que qualquer regressão seja detectada assim que o código é enviado ao repositório. O feedback rápido reduz significativamente o custo de correção, pois o desenvolvedor identifica e resolve a falha imediatamente após a alteração.

---

### 2. Testes Unitários (Unit Testing)

#### 2.1. Conceito e Isolamento
O objetivo do teste unitário é validar a regra de negócio da menor unidade de código (um método específico de uma classe) sem depender de recursos externos como banco de dados, rede ou serviços de terceiros. Para garantir essa independência, as dependências são substituídas por objetos simulados (*mocks*).

#### 2.2. Aplicação no Spring Boot
Nos testes da camada de serviço (`@Service`), o contexto completo do Spring não é carregado, permitindo que a suíte execute em milissegundos:
* **JUnit 5:** Framework que gerencia o ciclo de vida dos testes e executa as asserções (`assertEquals`, `assertThrows`, `assertNotNull`).
* **Mockito:** Framework responsável por criar objetos simulados e definir comportamentos esperados.

**Anotações Principais:**
* `@ExtendWith(MockitoExtension.class)`: Habilita a integração entre JUnit 5 e Mockito.
* `@Mock` / `@MockitoBean`: Cria a versão simulada de uma dependência.
* `@InjectMocks`: Cria a instância da classe sob teste (ex.: `TaskService`) e injeta nela os mocks criados.

---

### 3. Testes de Sistema e Integração (System/Integration Testing)

#### 3.1. Escopo e Comparativo
Diferente dos testes unitários, os testes de integração validam o funcionamento conjunto dos componentes da aplicação (Controller -> Service -> Repository -> Banco de Dados).

**Comparativo:** Embora sejam mais lentos por exigirem a inicialização do ecossistema da aplicação, oferecem maior confiabilidade quanto à integração dos componentes e configurações de infraestrutura.

#### 3.2. Aplicação no Spring Boot
* `@SpringBootTest`: Carrega o contexto de aplicação Spring completo para execução dos testes.
* `@AutoConfigureMockMvc`: Prepara a estrutura do `MockMvc` para simular requisições HTTP sem a necessidade de subir um servidor web real.
* **Banco de Dados em Memória (H2):** Substitui o banco de produção por uma instância leve em memória, garantindo um ambiente isolado e limpo a cada execução da suíte de testes.

---

### 4. Teste de Caixa Preta (Black-box Testing)

#### 4.1. Abordagem Baseada na Especificação
O teste de caixa preta avalia a aplicação focando apenas nas entradas e saídas (contratos de interface), sem considerar a implementação lógica interna.

#### 4.2. Aplicação no Spring Boot
Na arquitetura REST, a caixa preta é aplicada diretamente na camada de controladores (`@RestController`):
* Validação do envio de DTOs e das respostas em formato JSON.
* Validação dos códigos de status HTTP retornados (ex.: `200 OK` em buscas, `201 Created` para cadastro, `400 Bad Request` em dados inválidos ou `404 Not Found` para registros ausentes).
* Utilização da ferramenta `MockMvc` para efetuar requisições simuladas (`GET`, `POST`, `PUT`, `DELETE`) e inspecionar os retornos via expressões `JSONPath`.

---

### 5. Teste de Caixa Branca (White-box Testing)

#### 5.1. Abordagem Baseada na Estrutura Interna
O teste de caixa branca analisa o código-fonte por dentro, com o objetivo de exercitar os diferentes caminhos de execução, incluindo condicionais (`if/else`), estruturas de repetição e cenários de lançamento de exceções.

#### 5.2. Medição de Cobertura com JaCoCo
No Spring Boot, a cobertura de caixa branca é aferida pelo plugin JaCoCo (*Java Code Coverage*):
* O plugin analisa os arquivos compilados durante a execução da suíte de testes e registra quais instruções e ramificações foram cobertas.
* Ao final da execução, gera um relatório visual em HTML localizado em `target/site/jacoco/index.html`, destacando o percentual de cobertura das classes da aplicação.

---

## Parte II: Execução Prática e Evidências

### Como Executar os Testes

Para compilar o projeto, executar a suíte completa de testes unitários e de integração e gerar o relatório do JaCoCo, execute no terminal na raiz do projeto:

```bash
./mvnw clean test

![Relatório de Cobertura JaCoCo]
(JaCoCo.png)