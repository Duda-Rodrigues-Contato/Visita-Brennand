/// <reference types="cypress" />

describe('Fluxo Completo: Agendamento -> Gestão (H10) -> Ticket (H11)', () => {

  it('Deve agendar, confirmar a visita e gerar o ticket com QR Code', () => {
    cy.visit('/agendamento');
    cy.get('#nomeInstituicao').type('Cypress Automated Test');
    cy.get('#nomeResponsavel').type('Tester Bot');
    cy.get('#emailResponsavel').type('test@cypress.io');
    cy.get('#telefoneResponsavel').type('81999999999');
    cy.get('#tipoInstituicao').select('EMPRESA');
    cy.get('#origemVisitante').type('Internet');
    
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    if (tomorrow.getDay() === 1) tomorrow.setDate(tomorrow.getDate() + 1);
    const dateStr = tomorrow.toISOString().split('T')[0];
    
    cy.get('#dataVisita').invoke('val', dateStr).trigger('change');
    
    cy.get('#horarioChegada').should('not.be.disabled').select(1);
    
    cy.get('form#formAgendamento').find('button.submit-btn').click();
    cy.url().should('include', '/sucesso');

    cy.request('/api/test/last-token').then((response) => {
      const token = response.body;
      expect(token).to.not.equal('NENHUMA_VISITA');

      const linkGestao = `/gerenciar-visita?token=${token}`;
      cy.visit(linkGestao);

      cy.contains('h1', 'Gerencie seu Agendamento');
      cy.contains('strong', 'Tester Bot');
      cy.contains('strong', 'AGENDADO');

      cy.contains('button', 'Confirmar Agendamento').click();

      cy.url().should('include', '/ticket');
      cy.contains('h2', 'Ticket de Entrada');
      
      cy.get('.qr-code-img')
        .should('be.visible')
        .and('have.attr', 'src')
        .and('include', 'data:image/png;base64,');
        
      cy.contains('.info-row', 'Tester Bot');
    });
  });

  it('Deve permitir cancelar uma visita (H10)', () => {
    cy.visit('/agendamento');
    cy.get('#nomeInstituicao').type('Cancel Test');
    cy.get('#nomeResponsavel').type('Cancelador');
    cy.get('#emailResponsavel').type('cancel@test.com');
    cy.get('#telefoneResponsavel').type('81888888888');
    cy.get('#tipoInstituicao').select('OUTRO');
    cy.get('#origemVisitante').type('Local');

    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    if (tomorrow.getDay() === 1) tomorrow.setDate(tomorrow.getDate() + 1);
    const dateStr = tomorrow.toISOString().split('T')[0];

    cy.get('#dataVisita').invoke('val', dateStr).trigger('change');

    cy.get('#horarioChegada').should('not.be.disabled').select(1);

    cy.get('form#formAgendamento').find('button.submit-btn').click();

    cy.request('/api/test/last-token').then((response) => {
        const token = response.body;
        cy.visit(`/gerenciar-visita?token=${token}`);

        cy.contains('button', 'Cancelar Visita').click();

        cy.contains('.mensagem-sucesso', 'Sua visita foi cancelada com sucesso.').should('be.visible');
        
        cy.contains('button', 'Confirmar Agendamento').should('not.exist');
    });
  });
});