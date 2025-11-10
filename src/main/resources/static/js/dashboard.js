document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('visitantesChart');

    if (ctx && typeof labelsDoGrafico !== 'undefined' && typeof dadosDoGrafico !== 'undefined') {
        new Chart(ctx, {
            type: 'bar', 
            data: {
                labels: labelsDoGrafico,
                datasets: [{
                    label: 'Nº de Visitantes',
                    data: dadosDoGrafico,
                    backgroundColor: 'rgba(0, 150, 125, 0.6)', 
                    borderColor: 'rgba(0, 150, 125, 1)',  
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                         
                            precision: 0
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false 
                    }
                }
            }
        });
    }
});