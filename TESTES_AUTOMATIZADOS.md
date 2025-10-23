1. Testes de Backend (API) com Rest Assured
Estes testes validam a lógica de negócio principal da nossa API (backend). Eles usam a biblioteca Rest Assured para simular requisições HTTP (como POST) e verificar se o servidor responde corretamente, incluindo se os dados foram salvos no banco de dados de teste.

** Onde estão?**
Os arquivos de teste estão localizados em: src/test/java/com/example/Gestao_Viva/

** Cenários de Teste**
Valida a funcionalidade de administrador de alterar o status do parque para ABERTO, FECHADO e MANUTENCAO.
Na pasta
StatusParqueAdminPostTest.java


Como Executar
Pré-requisitos:

Java 17 (ou superior) instalado.

Maven instalado.

Estes testes são executados automaticamente pelo Maven. Eles iniciam um servidor de teste temporário em uma porta aleatória, executam as chamadas de API e depois o desligam. Você não pode estar com a aplicação principal rodando.

Bash

mvn test
Para rodar um arquivo de teste específico (ex: StatusParqueAdminPostTest):

Bash

mvn test -Dtest=StatusParqueAdminPostTest

2. Testes de Frontend (E2E) com Cypress
Estes testes validam a experiência do usuário (frontend). Eles usam o Cypress para abrir um navegador de verdade, navegar pelas páginas como um usuário real, clicar em botões, preencher formulários e verificar se o que o usuário vê na tela está correto.

** Onde estão?**
Os arquivos de teste estão localizados em: cypress/e2e/

** Cenários de Teste**
Atualmente, cobrimos as seguintes funcionalidades:

avaliacao.cy.js

Valida o preenchimento e envio com sucesso do formulário de avaliação.

Garante que uma mensagem de erro é exibida se o usuário tentar enviar o formulário sem preencher o campo obrigatório (Avaliação Geral).

Testa a interatividade dos componentes, como a seleção das estrelas de nota.

** Como Executar**
 Pré-requisitos IMPORTANTES:

Node.js e NPM instalados.

O servidor backend (Java/Spring Boot) DEVE ESTAR RODANDO em http://localhost:8080.

Passo 1: Instalar as dependências (só na primeira vez) Na raiz do projeto, rode:

Bash

npm install
Passo 2: Abrir o Cypress (Modo Interativo) Rode o comando:

Bash

npx cypress open
Isso abrirá a interface gráfica do Cypress. A partir dela, você pode escolher seu navegador e clicar no teste avaliacao.cy.js para executá-lo visualmente.