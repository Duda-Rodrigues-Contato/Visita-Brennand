/// <reference types="cypress" />

describe('Página de Agendamento de Visita', () => {

  beforeEach(() => {
    cy.intercept('POST', '/agendamento', (req) => {
      req.redirect('/agendamento/sucesso', 302);
    }).as('postAgendamento');

    cy.visit('/agendamento');
  });

  it('deve carregar a página com a aba "Grupo" ativa por padrão', () => {
    cy.contains('h1', 'Pré-Cadastramento de Visita').should('be.visible');
    cy.get('.tab-link[data-tab="grupo"]').should('have.class', 'active');
    cy.get('#grupo-form-section').should('not.have.class', 'hidden');
    cy.get('#individual-form-section').should('have.class', 'hidden');
    cy.get('#membros-section').should('not.have.class', 'hidden');
  });

  it('deve permitir trocar para a aba "Individual" e esconder/mostrar campos corretos', () => {
    cy.get('.tab-link[data-tab="individual"]').click();
    cy.get('.tab-link[data-tab="individual"]').should('have.class', 'active');
    cy.get('.tab-link[data-tab="grupo"]').should('not.have.class', 'active');

    cy.get('#grupo-form-section').should('have.class', 'hidden');
    cy.get('#membros-section').should('have.class', 'hidden');
    cy.get('#individual-form-section').should('not.have.class', 'hidden');

    cy.get('#responsavel-legend').should('contain.text', '2. Suas Informações de Contato');
    cy.get('#detalhes-legend').should('contain.text', '3. Detalhes da Visita');
  });

  it('deve preencher e enviar o formulário de grupo com sucesso', () => {
    cy.get('#nomeInstituicao').type('Escola Cypress Teste');
    cy.get('#cnpj').type('11.222.333/0001-44');
    cy.get('#tipoInstituicao').select('ESCOLA_PUBLICA');

    cy.get('#nomeResponsavel').type('Responsável Cypress');
    cy.get('#emailResponsavel').type('cypress.teste@valido.com');
    cy.get('#telefoneResponsavel').type('81999998888');

    cy.get('#nomeMembro').type('Aluno Cypress A');
    cy.get('#btnAddMembro').click();
    cy.get('#listaMembros').should('contain.text', 'Aluno Cypress A');
    cy.get('#contadorMembros').should('contain.text', '(1)');
    
    cy.get('#nomeMembro').type('Aluno Cypress B');
    cy.get('#btnAddMembro').click();
    cy.get('#listaMembros').should('contain.text', 'Aluno Cypress B');
    cy.get('#contadorMembros').should('contain.text', '(2)');

    cy.get('#origemVisitante').type('Recife/PE');

    const today = new Date();
    today.setMonth(today.getMonth() + 1);
    today.setDate(15);
    if (today.getDay() === 1) {
        today.setDate(today.getDate() + 1);
    }
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const futureDate = `${year}-${month}-${day}`;

    cy.get('#dataVisita').invoke('val', futureDate).trigger('change');
    
    cy.wait(500);

    cy.get('#horarioChegada').should('not.be.disabled');
    cy.get('#horarioChegada').select('10:30');

    cy.get('#incluiPcd').check();

    cy.get('form#formAgendamento').find('button.submit-btn').click();

    cy.wait('@postAgendamento');

    cy.url().should('include', '/agendamento/sucesso');
    cy.contains('h1', 'Agendamento Realizado com Sucesso!').should('be.visible');
  });

   it('deve preencher e enviar o formulário individual com sucesso', () => {
    cy.get('.tab-link[data-tab="individual"]').click();
    
    cy.get('#sexo').select('MASCULINO');

    cy.get('#nomeResponsavel').type('Visitante Individual Cypress');
    cy.get('#emailResponsavel').type('individual.cypress@valido.com');
    cy.get('#telefoneResponsavel').type('81988887777');

    cy.get('#origemVisitante').type('Olinda/PE');
    
    const today = new Date();
    today.setMonth(today.getMonth() + 1);
    today.setDate(16);
    if (today.getDay() === 1) { today.setDate(today.getDate() + 1); }
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const futureDate = `${year}-${month}-${day}`;

    cy.get('#dataVisita').invoke('val', futureDate).trigger('change');
    cy.wait(500);
    cy.get('#horarioChegada').select('14:00');

    cy.get('#incluiPcd').should('not.be.checked');

    cy.get('form#formAgendamento').find('button.submit-btn').click();

    cy.wait('@postAgendamento');
    cy.url().should('include', '/agendamento/sucesso');
    cy.contains('h1', 'Agendamento Realizado com Sucesso!').should('be.visible');
  });


  it('deve exibir erro se o Nome do Responsável não for preenchido', () => {
    cy.get('#emailResponsavel').type('teste@erro.com');
    cy.get('#telefoneResponsavel').type('11911112222');
    cy.get('#origemVisitante').type('Teste');

    const today = new Date();
    today.setDate(today.getDate() + 2);
    if (today.getDay() === 1) { today.setDate(today.getDate() + 1); }
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const futureDate = `${year}-${month}-${day}`;
    cy.get('#dataVisita').invoke('val', futureDate).trigger('change');
    cy.wait(500);
    cy.get('#horarioChegada').select('10:00');
    
    cy.get('form#formAgendamento').find('button.submit-btn').click();

    cy.get('#nomeResponsavel-error')
      .should('be.visible')
      .and('contain.text', 'O nome do responsável é obrigatório.');

    cy.url().should('not.include', '/agendamento/sucesso');
    cy.contains('h1', 'Agendamento Realizado com Sucesso!').should('not.exist');
  });
  
   it('deve exibir erro ao selecionar uma segunda-feira', () => {
     let proximaSegunda = new Date();
     proximaSegunda.setDate(proximaSegunda.getDate() + ( (1 + 7 - proximaSegunda.getDay()) % 7 || 7) );
     const year = proximaSegunda.getFullYear();
     const month = String(proximaSegunda.getMonth() + 1).padStart(2, '0');
     const day = String(proximaSegunda.getDate()).padStart(2, '0');
     const mondayDate = `${year}-${month}-${day}`;

     cy.get('#dataVisita').invoke('val', mondayDate).trigger('change');
    
     cy.get('#horarioChegada option:selected') 
     .should('be.disabled') 
     .and('contain.text', 'Data inválida'); 

    cy.get('#horarioChegada').should('be.disabled');
   });
});
