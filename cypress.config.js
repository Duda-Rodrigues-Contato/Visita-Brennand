// Este é o código COMPLETO para o arquivo cypress.config.js

const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
   
    baseUrl: "http://localhost:8080",

    setupNodeEvents(on, config) {
      
    },
  },
});