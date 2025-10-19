document.addEventListener('DOMContentLoaded', function() {
    const statusPill = document.getElementById('status-pill');
    const statusText = document.getElementById('status-text');
    const statusUpdated = document.getElementById('status-updated');
    const agendeBtn = document.querySelector('.status-card__panel .btn-primary');

    function fetchParkStatus() {
        const estados = [
            { status: 'ABERTO', classe: 'aberto', texto: 'O Parque de Esculturas está aberto para visitação. Venha apreciar a arte e a paisagem!', botaoAtivo: true },
            { status: 'FECHADO', classe: 'fechado', texto: 'O parque encontra-se fechado no momento. A reabertura está prevista para o próximo dia útil.', botaoAtivo: false },
            { status: 'ALERTA', classe: 'alerta', texto: 'O parque está operando com capacidade reduzida devido a evento no local. Planeje sua visita.', botaoAtivo: true }
        ];

        
        const estadoAtual = estados[Math.floor(Math.random() * estados.length)];

        
        statusPill.textContent = estadoAtual.status;
        statusText.textContent = estadoAtual.texto;

        statusPill.classList.remove('status-pill--aberto', 'status-pill--fechado', 'status-pill--alerta');
        statusPill.classList.add(`status-pill--${estadoAtual.classe}`);
        
        const agora = new Date();
        statusUpdated.textContent = `Atualizado em: ${agora.toLocaleDateString()} às ${agora.toLocaleTimeString()}`;

        
        if (estadoAtual.botaoAtivo) {
            agendeBtn.style.display = 'inline-block';
        } else {
            agendeBtn.style.display = 'none';
        }
    }


    
    fetchParkStatus();
});