document.addEventListener('DOMContentLoaded', function() {

 
    const statusPill = document.getElementById('status-pill');
    const statusUpdated = document.getElementById('status-updated');
    const statusText = document.getElementById('status-text');

   
    async function fetchStatus() {
        try {
            const response = await fetch('/api/parque/status');
            if (!response.ok) {
                throw new Error('Não foi possível obter o status do parque.');
            }
            const status = await response.json();
            
            atualizarUI(status);

        } catch (error) {
            console.error('Erro:', error);
            statusPill.textContent = 'ERRO';
            statusText.textContent = 'Não foi possível carregar o status do parque. Tente novamente mais tarde.';
            statusPill.className = 'status-pill status-pill--fechado';
        }
    }

  
    function atualizarUI(status) {

        const dataFormatada = new Date(status.ultimaAtualizacao).toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });

        statusUpdated.textContent = `Última atualização: ${dataFormatada}`;
        statusText.textContent = status.motivo || '';

        
        statusPill.classList.remove('status-pill--aberto', 'status-pill--fechado', 'status-pill--manutencao');

      
        switch (status.estado) {
            case 'ABERTO':
                statusPill.textContent = 'PARQUE ABERTO';
                statusPill.classList.add('status-pill--aberto');
                break;
            case 'FECHADO':
                statusPill.textContent = 'PARQUE FECHADO';
                statusPill.classList.add('status-pill--fechado');
                break;
            case 'MANUTENCAO':
                statusPill.textContent = 'EM MANUTENÇÃO';
                statusPill.classList.add('status-pill--manutencao');
                break;
            default:
                statusPill.textContent = 'INDETERMINADO';
                statusPill.classList.add('status-pill--fechado');
        }
    }

   
    fetchStatus();
});