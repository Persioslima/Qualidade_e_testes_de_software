🧾 Projeto: Calculadora de Frete — Testes Automatizados com JUnit e JaCoCo
📘 Descrição geral

Este projeto demonstra o uso de testes de software automatizados em Java, aplicando os conceitos estudados no Capítulo 2 – Princípios, Estratégias, Níveis e Tipos de Teste, conforme orientação do professor Alessandro Ferreira Paz Lima.

A aplicação consiste em uma Calculadora de Frete, com regras de negócio simples para cálculo de valores de envio com base em peso e região de destino.
Foram desenvolvidos testes unitários utilizando JUnit 5, e o nível de cobertura de código foi medido com o JaCoCo.

⚙️ Ferramentas utilizadas

Java 17 (LTS)

Apache Maven 3.9.6

JUnit 5.10.2

JaCoCo 0.8.10

IntelliJ IDEA Community 2023.3

🧱 Estrutura do projeto
frete-teste/
 ├── pom.xml
 ├── src/
 │   ├── main/java/br/com/persio/CalculadoraFrete.java
 │   └── test/java/br/com/persio/CalculadoraFreteTest.java
 └── target/
     └── site/jacoco/index.html

🚚 Funcionalidade principal

Classe: CalculadoraFrete

Regras de negócio:

Calcula o frete conforme o peso (kg) e a região de destino;

Taxas básicas por região:

SUL → R$ 10,00

SUDESTE → R$ 12,00

CENTRO-OESTE → R$ 15,00

NORDESTE → R$ 18,00

NORTE → R$ 20,00

Valor final:
frete = taxaBase + (peso × 2.5)

Validações:

Peso ≤ 0 → lança exceção

Região nula, vazia ou inválida → lança exceção

🧪 Testes automatizados

Classe de teste: CalculadoraFreteTest

Casos testados:

Cálculo válido para região SUL;

Peso inválido (zero ou negativo);

Região inválida;

Região nula ou vazia;

(opcional) Cobertura adicional para NORTE e NORDESTE.

📊 Relatório de cobertura (JaCoCo)

Após rodar os testes, o relatório é gerado automaticamente em:

target/site/jacoco/index.html

Como executar:
mvn clean verify


Após a execução:

O Maven compila e executa os testes do JUnit;

O JaCoCo mede a cobertura e gera o relatório;

O arquivo HTML pode ser aberto diretamente no navegador.

🎯 Resultados esperados

Todos os testes devem passar (BUILD SUCCESS);

O relatório JaCoCo deve exibir cobertura acima de 85 % (idealmente 100 %);

O projeto comprova o domínio de:

Princípios de teste de software;

Testes unitários e de exceção;

Uso de ferramentas de cobertura (JaCoCo).

👨‍🏫 Créditos

Autor: Persio Lima
Disciplina: Teste e Qualidade de Software
Professor: Alessandro Ferreira Paz Lima
Instituição: FATEC — Aula 8: Testes Unitários com Java (JUnit + JaCoCo)