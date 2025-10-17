document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('visitantesChart');

    if (ctx && typeof labelsDoGrafico !== 'undefined' && typeof dadosDoGrafico !== 'undefined') {
        new Chart(ctx, {
            type: 'bar', // Tipo de gráfico (barras)
            data: {
                labels: labelsDoGrafico,
                datasets: [{
                    label: 'Nº de Visitantes',
                    data: dadosDoGrafico,
                    backgroundColor: 'rgba(0, 150, 125, 0.6)', // Cor verde principal com transparência
                    borderColor: 'rgba(0, 150, 125, 1)',   // Cor verde principal sólida
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            // Garante que o eixo Y só tenha números inteiros
                            precision: 0
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false // Esconde a legenda para um visual mais limpo
                    }
                }
            }
        });
    }
});