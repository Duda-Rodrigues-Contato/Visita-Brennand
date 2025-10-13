document.addEventListener('DOMContentLoaded', function() {
    
  
    const dataVisitaInput = document.getElementById('dataVisita');
    const horarioChegadaSelect = document.getElementById('horarioChegada');
    const dateError = document.getElementById('date-error');
    const submitButton = document.querySelector('.submit-btn');

    function gerarHorarios(inicio, fim, intervalo) {
        horarioChegadaSelect.innerHTML = '<option value="" disabled selected>Selecione um horário</option>';
        let horaAtual = new Date(`1970-01-01T${inicio}:00`);
        const horaFim = new Date(`1970-01-01T${fim}:00`);

        while (horaAtual <= horaFim) {
            const horaFormatada = horaAtual.toTimeString().substring(0, 5);
            const option = new Option(horaFormatada, horaFormatada);
            horarioChegadaSelect.add(option);
            horaAtual.setMinutes(horaAtual.getMinutes() + intervalo);
        }
    }

    function atualizarHorariosDisponiveis() {
        const dataSelecionada = dataVisitaInput.value;
        if (!dataSelecionada) return;

        const diaDaSemana = new Date(dataSelecionada + 'T00:00:00').getUTCDay();
        
        horarioChegadaSelect.disabled = false;
        submitButton.disabled = false;
        dateError.textContent = '';

        switch (diaDaSemana) {
            case 1: 
                horarioChegadaSelect.innerHTML = '<option value="" disabled selected>Fechado às Segundas</option>';
                horarioChegadaSelect.disabled = true;
                submitButton.disabled = true;
                dateError.textContent = 'O parque não abre às segundas-feiras. Por favor, escolha outra data.';
                break;
            case 0: 
            case 6: 
                gerarHorarios('09:00', '17:30', 30);
                break;
            default: 
                gerarHorarios('10:00', '16:30', 30);
                break;
        }
    }

    dataVisitaInput.addEventListener('change', atualizarHorariosDisponiveis);
    atualizarHorariosDisponiveis();

   
    const tabs = document.querySelectorAll('.tab-link');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabType = tab.dataset.tab;
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            
            document.querySelectorAll('.form-section').forEach(section => {
                section.classList.add('hidden');
            });
            document.getElementById(`${tabType}-form-section`).classList.remove('hidden');

            const legend = document.getElementById('detalhes-legend');
            if(tabType === 'individual') {
                legend.textContent = '2. Detalhes da Visita';
            } else {
                legend.textContent = '4. Detalhes da Visita';
            }
        });
    });


    const form = document.getElementById('formAgendamento');
    form.addEventListener('submit', function(event) {
        const activeTab = document.querySelector('.tab-link.active').dataset.tab;
        
        if (activeTab === 'individual') {
            document.querySelector('[name="nomeInstituicao"]').value = 'Visita Individual';
            document.querySelector('[name="tipoInstituicao"]').value = 'OUTRO';
            document.querySelector('[name="numeroVisitantes"]').value = 1;
        }

     
        const membrosHiddenContainer = document.getElementById('membrosHiddenContainer');
        const listaMembrosUI = document.getElementById('listaMembros');
        if (membrosHiddenContainer && listaMembrosUI) {
            const membros = Array.from(listaMembrosUI.querySelectorAll('li')).map(li => li.dataset.nome);
            membrosHiddenContainer.innerHTML = '';
            membros.forEach((nome, index) => {
                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = `membros[${index}]`;
                hiddenInput.value = nome;
                membrosHiddenContainer.appendChild(hiddenInput);
            });
        }
    });

  
    IMask(document.getElementById('telefoneResponsavel'), { mask: '(00) 00000-0000' });
    IMask(document.getElementById('telefoneResponsavelInd'), { mask: '(00) 00000-0000' });
    IMask(document.getElementById('cnpj'), { mask: '00.000.000/0000-00' });
    IMask(document.getElementById('cnpjInd'), { mask: '000.000.000-00' });

    
    const btnAddMembro = document.getElementById('btnAddMembro');
    const nomeMembroInput = document.getElementById('nomeMembro');
    const listaMembrosUI = document.getElementById('listaMembros');

    if(btnAddMembro) {
        btnAddMembro.addEventListener('click', function() {
            const nome = nomeMembroInput.value.trim();
            if (nome) {
                const li = document.createElement('li');
                li.textContent = nome;
                li.dataset.nome = nome; 
                
                const removeBtn = document.createElement('button');
                removeBtn.textContent = 'x';
                removeBtn.type = 'button';
                removeBtn.className = 'remove-btn';
                removeBtn.onclick = function() {
                    li.remove();
                };
                
                li.appendChild(removeBtn);
                listaMembrosUI.appendChild(li);
                
                nomeMembroInput.value = '';
                nomeMembroInput.focus();
            }
        });
    }
});