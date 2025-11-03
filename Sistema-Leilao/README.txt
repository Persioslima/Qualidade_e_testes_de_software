Sistema de Leilão — Testes de Software com JUnit 5
📘 Capítulo 2 — Princípios, Estratégias, Níveis e Tipos de Teste

Tema: Sistema de Leilão com regras básicas de negócio
Ferramentas: Java 17 + JUnit 5 + Maven + JaCoCo
IDE Recomendada: IntelliJ IDEA Community

🎯 Objetivo do Projeto

Aplicar na prática os conceitos de testes de software, com foco em:

Testes de Unidade (validação de métodos isolados);

Testes de Integração (interação entre classes);

Cobertura de Código (usando o plugin JaCoCo);

Exploração dos Princípios e Estratégias de Teste.

🧱 Estrutura do Projeto
sistema-leilao/
 ├── pom.xml
 ├── src/
 │   ├── main/
 │   │   └── java/br/com/persio/
 │   │       ├── Usuario.java
 │   │       ├── Lance.java
 │   │       ├── Leilao.java
 │   │       └── Avaliador.java
 │   └── test/
 │       └── java/br/com/persio/
 │           ├── UsuarioTest.java
 │           ├── LanceTest.java
 │           ├── LeilaoTest.java
 │           └── AvaliadorTest.java
 └── target/
     └── site/jacoco/index.html

⚙️ Configuração e Execução
1️⃣ Compilar e testar o projeto

No terminal, dentro da pasta do projeto:

mvn clean test

2️⃣ Gerar o relatório de cobertura JaCoCo
mvn clean verify


Após a execução, abra o relatório no navegador:

target/site/jacoco/index.html

🧩 Classes Principais
Classe	Responsabilidade
Usuario	Representa o participante do leilão.
Lance	Representa a oferta feita por um usuário com determinado valor.
Leilao	Armazena a lista de lances e aplica as regras de negócio.
Avaliador	Analisa os lances de um leilão e determina o maior e o menor valor.
🧠 Regras de Negócio

Cada usuário pode realizar no máximo 5 lances.

O sistema não aceita lances decrescentes (valor menor que o último).

O sistema não aceita valores nulos ou negativos.

O avaliador deve identificar corretamente o maior e menor lance.

Lançamentos nulos ou listas vazias devem gerar exceção.

🧪 Estratégia de Teste

JUnit 5: framework de teste automatizado.

Maven Surefire Plugin: executa os testes automaticamente.

JaCoCo: mede a cobertura dos testes sobre o código.

Cada classe possui um teste dedicado para garantir comportamento correto, validação de erros e cobertura completa.

📊 Relatório de Cobertura (JaCoCo)

O relatório mostra:

Percentual de linhas, métodos e classes testadas;

O que ainda não foi coberto pelos testes;

Visualização em gráfico vermelho/verde.

Meta de qualidade: cobertura mínima recomendada ≥ 85%.

💬 Questões Propostas
1️⃣ Relembre um bug que você encontrou.

Exemplo: um erro em um formulário que aceitava CPF inválido.
O Princípio “Testar cedo” teria ajudado a detectar esse problema nas primeiras fases, evitando retrabalho.

2️⃣ Estratégia de teste para um Caixa Eletrônico (ATM)

Eu utilizaria a estratégia baseada em casos de uso reais, cobrindo os fluxos principais (saque, depósito, saldo, erro de cartão).
Aplicaria testes de integração e sistema, simulando as interações com banco de dados e sensores físicos.

3️⃣ Níveis de teste para “Transferência Bancária”
Nível	Descrição
Unitário	Testar método que realiza a transferência.
Integração	Testar comunicação entre classes Conta e Banco.
Sistema	Testar o fluxo completo da transferência.
Aceitação	Validar o processo final sob o ponto de vista do cliente.
🧾 Conclusão

O projeto Sistema de Leilão consolida:

Princípios dos testes de software;

Aplicação prática de JUnit 5 e JaCoCo;

Compreensão de regras de negócio e cobertura.

✅ Resultado: sistema simples, testado, validado e pronto para avaliação de qualidade.