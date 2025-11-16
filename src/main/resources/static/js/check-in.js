document.addEventListener('DOMContentLoaded', () => {
    

    const searchInput = document.querySelector('.search-bar input');
    const visitsListContainer = document.querySelector('.visits-list');
    
    let debounceTimer;

    const renderVisitas = (visitas) => {
        visitsListContainer.innerHTML = '';
        
        if (visitas.length === 0) {
            visitsListContainer.innerHTML = `<p class="empty-message">Nenhuma visita agendada encontrada.</p>`;
            return;
        }

        visitas.forEach(visita => {
            const horario = visita.horarioChegada || 'N/A';
            const visitantes = visita.numeroVisitantes || 0;
            const instituicao = visita.nomeInstituicao || 'Visitante Individual';
            const responsavel = visita.nomeResponsavel || 'N/A';

            const itemHtml = `
                <div class="visit-item" id="item-visita-${visita.id}">
                    <div class="visit-info">
                        <h3>${instituicao}</h3>
                        <p>Responsável: ${responsavel}</p>
                        <div class="visit-details">
                            <span><i class="fas fa-clock"></i> ${horario}</span>
                            <span><i class="fas fa-users"></i> ${visitantes}</span>
                        </div>
                    </div>
                    <div class="visit-actions">
                        <button class="check-in-btn" data-id="${visita.id}">
                            Fazer Check-in
                        </button>
                    </div>
                </div>
            `;
            visitsListContainer.insertAdjacentHTML('beforeend', itemHtml);
        });
    };

    const carregarVisitas = async (termo = '') => {
        try {
            const response = await fetch(`/api/check-in/buscar?nome=${encodeURIComponent(termo)}`);
            if (!response.ok) throw new Error('Falha ao buscar visitas');
            const visitas = await response.json();
            renderVisitas(visitas);
        } catch (error) {
            console.error(error);
            visitsListContainer.innerHTML = `<p class="empty-message">Erro ao carregar visitas.</p>`;
        }
    };

    const realizarCheckIn = async (visitaId, botao) => {
        botao.disabled = true;
        botao.textContent = 'Aguarde...';

        try {
            const response = await fetch(`/api/check-in/${visitaId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            const visitItem = document.getElementById(`item-visita-${visitaId}`);
            const actionsContainer = botao.parentElement;

            if (response.ok) {
                const visitaAtualizada = await response.json();
                actionsContainer.innerHTML = `<span class="status-badge status-done">Check-in Feito</span>`;
            } else {
                const errorMsg = await response.text();
                alert(`Erro no Check-in: ${errorMsg}`);
                botao.disabled = false;
                botao.textContent = 'Fazer Check-in';
            }
        } catch (error) {
            console.error(error);
            alert('Erro de conexão. Tente novamente.');
            botao.disabled = false;
            botao.textContent = 'Fazer Check-in';
        }
    };

    searchInput.addEventListener('input', (e) => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            carregarVisitas(e.target.value);
        }, 300);
    });

    visitsListContainer.addEventListener('click', (e) => {
        if (e.target.classList.contains('check-in-btn')) {
            const visitaId = e.target.dataset.id;
            realizarCheckIn(visitaId, e.target);
        }
    });

    carregarVisitas();
});