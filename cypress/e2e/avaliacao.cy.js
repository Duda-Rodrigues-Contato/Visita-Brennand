/// <reference types="cypress" />

describe('Página de Avaliação de Visita', () => {
  
    beforeEach(() => {
      cy.intercept('POST', '/avaliacao', (req) => {
        req.redirect('/avaliacao-agradecimento', 302);
      }).as('postAvaliacao');
  
      cy.visit('/avaliacao?visitaId=123');
    });
  
    it('Carrega a página e exibe todos os campos corretamente', () => {
      cy.contains('h1', 'Conte-nos sobre sua visita!').should('be.visible');
      
      cy.contains('label', 'Qual sua satisfação geral com a visita ao Parque de Esculturas? *')
        .should('be.visible');
        
      cy.get('button.submit-btn').contains('ENVIAR AVALIAÇÃO').should('be.visible');
    });
  
    it('Deve exibir um erro de validação se a Avaliação Geral (obrigatória) não for preenchida', () => {
      cy.get('form#formAvaliacao').find('button.submit-btn').click();
  
      cy.get('#geral-error')
        .should('be.visible')
        .and('contain.text', 'Por favor, selecione uma avaliação geral.');
        
      cy.url().should('include', '/avaliacao?visitaId=123');
      cy.contains('h1', 'Obrigado!').should('not.exist');
    });
  
    it('Deve preencher todos os campos e enviar o formulário com sucesso', () => {
      cy.get('label[for="geral-star5"]').click();
      
      cy.get('label[for="recepcao-star4"]').click();
      cy.get('label[for="limpeza-star3"]').click();
      cy.get('label[for="acessibilidade-star5"]').click();
      cy.get('label[for="esculturas-star2"]').click();
      cy.get('label[for="sinalizacao-star1"]').click();
  
      cy.get('label[for="rec-10"]').click();
  
      cy.get('#comentariosSugestoes').type('A visita foi excelente, mas poderia ter mais bebedouros.');
  
      cy.contains('label.radio-option', 'Em família').click();
  
      cy.get('form#formAvaliacao').find('button.submit-btn').click();
  
      cy.wait('@postAvaliacao');
  
      cy.url().should('include', '/avaliacao-agradecimento');
      cy.contains('h1', 'Obrigado!').should('be.visible');
      cy.contains('p', 'Sua avaliação foi enviada com sucesso').should('be.visible');
    });
    
    it('Deve permitir a seleção e mudança das estrelas de avaliação', () => {
      cy.get('#geral-star3').should('not.be.checked');
      cy.get('#geral-star5').should('not.be.checked');
      
      cy.get('label[for="geral-star3"]').click();
      
      cy.get('#geral-star3').should('be.checked');
      cy.get('#geral-star5').should('not.be.checked');
      
      cy.get('label[for="geral-star5"]').click();
      
      cy.get('#geral-star3').should('not.be.checked');
      cy.get('#geral-star5').should('be.checked');
    });
  
  });