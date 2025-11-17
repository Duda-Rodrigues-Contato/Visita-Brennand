// <reference types="cypress" />

describe('Dashboard de Avaliações (H12)', () => {

    beforeEach(() => {
        cy.visit('/dashboard/avaliacoes');
    });

    it('Deve carregar a página e exibir o título correto', () => {
        cy.contains('h2', 'Dashboard de Satisfação').should('be.visible');
        cy.get('.filter-card').should('be.visible');
    });

    it('Deve exibir os cards de métricas quando houver dados', () => {
      
        
        cy.get('body').then(($body) => {
            if ($body.find('.stats-grid').length > 0) {
               
                cy.get('.stat-card').should('have.length', 4); 
                cy.contains('.stat-label', 'Satisfação Geral');
                cy.contains('.stat-label', 'NPS (Recomendação)');
            } else {
              
                cy.contains('.empty-message', 'Nenhuma avaliação encontrada').should('be.visible');
            }
        });
    });

    it('Deve permitir filtrar por data', () => {
       
        cy.get('#dataInicio').type('2025-01-01');
        cy.get('#dataFim').type('2025-12-31');
        
        cy.get('form.filter-form').submit();

        
        cy.url().should('include', 'dataInicio=2025-01-01');
        cy.url().should('include', 'dataFim=2025-12-31');
    });
});