/// <reference types="cypress" />

describe('Página de Relatório (Dashboard)', () => {

  beforeEach(() => {
    cy.intercept('GET', '/dashboard/api/chart-data*', {
      statusCode: 200,
      body: [
        { data: '2025-10-01', totalVisitantes: 150 },
        { data: '2025-10-02', totalVisitantes: 180 }
      ]
    }).as('getChartData');

    cy.visit('/dashboard?dataInicio=2025-09-01&dataFim=2025-10-31');
  });

  it('deve carregar e exibir os componentes principais da página', () => {
    cy.get('.header .brand').should('contain.text', 'Gestão Viva');
    cy.get('h2').contains('Relatório por Período').should('be.visible');

    cy.get('label[for="dataInicio"]').should('be.visible');
    cy.get('label[for="dataFim"]').should('be.visible');
    cy.get('button.btn-primary').contains('Aplicar Filtros').should('be.visible');

    cy.get('.metrics-grid').should('be.visible');
    cy.get('h3').contains('Total de Visitas no Período').should('be.visible');
    cy.get('h3').contains('Visitas Concluídas').should('be.visible');

    cy.get('h2').contains('Detalhes dos Agendamentos no Período').should('be.visible');
    cy.get('table th').contains('Data da Visita').should('be.visible');
    cy.get('table th').contains('Responsável').should('be.visible');
  });

  it('deve permitir filtrar por data e atualizar a URL', () => {
    const dataInicio = '2025-10-01';
    const dataFim = '2025-10-31';

    cy.get('#dataInicio').type(dataInicio);
    cy.get('#dataFim').type(dataFim);

    cy.get('button.btn-primary').contains('Aplicar Filtros').click();

    cy.url().should('include', `dataInicio=${dataInicio}`);
    cy.url().should('include', `dataFim=${dataFim}`);

    cy.get('#dataInicio').should('have.value', dataInicio);
    cy.get('#dataFim').should('have.value', dataFim);
  });

  it('deve permitir alterar a ordenação da tabela', () => {
    cy.get('th a[href*="sort=dataVisita,desc"]').click();

    cy.url().should('include', 'sort=dataVisita,desc');
    
    cy.get('th a span').contains('▼').should('be.visible');
  });

  it('deve permitir alterar a quantidade de itens por página', () => {
    cy.get('select[name="size"]').select('20');

    cy.url().should('include', 'size=20');
    cy.url().should('include', 'page=0');
  });

});