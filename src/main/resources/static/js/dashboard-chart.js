document.addEventListener('DOMContentLoaded', () => {

    const ctx = document.getElementById('visitantesChart').getContext('2d');
    let visitantesChart;

    
    const renderizarGrafico = async () => {
      
        const dataInicio = document.getElementById('dataInicio').value;
        const dataFim = document.getElementById('dataFim').value;

        try {
          
            const response = await fetch(`/dashboard/api/chart-data?dataInicio=${dataInicio}&dataFim=${dataFim}`);
            if (!response.ok) {
                throw new Error('Falha ao buscar dados para o gráfico');
            }
            const dados = await response.json();

          
            const labels = dados.map(d => new Date(d.data).toLocaleDateString('pt-BR', { timeZone: 'UTC' }));
            const dataPoints = dados.map(d => d.totalVisitantes);

           
            if (visitantesChart) {
                visitantesChart.destroy();
            }

            visitantesChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Nº de Visitantes',
                        data: dataPoints,
                        backgroundColor: 'rgba(0, 0, 0, 0.8)',
                        borderColor: 'rgba(0, 0, 0, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    plugins: {
                        legend: {
                            display: false 
                        }
                    }
                }
            });

        } catch (error) {
            console.error('Erro ao renderizar o gráfico:', error);
        }
    };

    
    renderizarGrafico();
});