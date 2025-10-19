document.addEventListener('DOMContentLoaded', function() {

    const tabLinks = document.querySelectorAll('.tab-link');
    const grupoFormSection = document.getElementById('grupo-form-section');
    const individualFormSection = document.getElementById('individual-form-section');
    const detalhesLegend = document.getElementById('detalhes-legend');
    const numeroVisitantesInput = document.getElementById('numeroVisitantes');

    if (tabLinks.length > 0 && grupoFormSection && individualFormSection) {
        tabLinks.forEach(tab => {
            tab.addEventListener('click', () => {
                const tabType = tab.dataset.tab;

                tabLinks.forEach(link => link.classList.remove('active'));
                tab.classList.add('active');

                if (tabType === 'individual') {
                    grupoFormSection.classList.add('hidden');
                    individualFormSection.classList.remove('hidden');
                    detalhesLegend.textContent = '2. Detalhes da Visita';
                    numeroVisitantesInput.value = 1;
                } else { // Se for 'grupo'
                    individualFormSection.classList.add('hidden');
                    grupoFormSection.classList.remove('hidden');
                    detalhesLegend.textContent = '4. Detalhes da Visita';
                }
            });
        });

        document.querySelector('.tab-link[data-tab="grupo"]').click();
    }

    const dataVisitaInput = document.getElementById('dataVisita');
    const horarioChegadaSelect = document.getElementById('horarioChegada');
    const dateError = document.getElementById('date-error');
    const submitBtn = document.querySelector('.submit-btn');

    function gerarHorarios(inicio, fim, intervalo) {
        horarioChegadaSelect.innerHTML = '<option value="" disabled selected>Selecione um horário</option>';
        let horaAtual = new Date(`1970-01-01T${inicio}:00`);
        const horaFim = new Date(`1970-01-01T${fim}:00`);
        while (horaAtual <= horaFim) {
            const horaFormatada = horaAtual.toTimeString().substring(0, 5);
            horarioChegadaSelect.add(new Option(horaFormatada, horaFormatada));
            horaAtual.setMinutes(horaAtual.getMinutes() + intervalo);
        }
    }

    function atualizarHorariosDisponiveis() {
        if (!dataVisitaInput) return;
        const dataSelecionada = dataVisitaInput.value;
        if (!dataSelecionada) return;
        const diaDaSemana = new Date(dataSelecionada + 'T00:00:00').getUTCDay();
        horarioChegadaSelect.disabled = false;
        submitBtn.disabled = false;
        dateError.textContent = '';
        switch (diaDaSemana) {
            case 1: // Segunda-feira
                horarioChegadaSelect.innerHTML = '<option value="" disabled selected>Fechado às Segundas</option>';
                horarioChegadaSelect.disabled = true;
                submitBtn.disabled = true;
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

    if (dataVisitaInput) {
        dataVisitaInput.addEventListener('change', atualizarHorariosDisponiveis);
        atualizarHorariosDisponiveis(); 
    }

    if (typeof IMask !== 'undefined') {
        IMask(document.getElementById('telefoneResponsavel'), { mask: '(00) 00000-0000' });
        IMask(document.getElementById('telefoneResponsavelInd'), { mask: '(00) 00000-0000' });
        IMask(document.getElementById('cnpj'), { mask: '00.000.000/0000-00' });
        IMask(document.getElementById('cnpjInd'), { mask: '000.000.000-00' });
    }

    const btnAddMembro = document.getElementById('btnAddMembro');
    const nomeMembroInput = document.getElementById('nomeMembro');
    const listaMembrosUL = document.getElementById('listaMembros');
    const contadorMembrosSpan = document.getElementById('contadorMembros');
    const membrosHiddenContainer = document.getElementById('membrosHiddenContainer');

    if (btnAddMembro && nomeMembroInput && listaMembrosUL) {
        let membros = [];

        const renderizarLista = () => {
            listaMembrosUL.innerHTML = '';
            membrosHiddenContainer.innerHTML = '';
            const numeroVisitantesInput = document.getElementById('numeroVisitantes');

            membros.forEach((nome, index) => {
                const li = document.createElement('li');
                li.textContent = nome;

                const removeButton = document.createElement('button');
                removeButton.textContent = 'x';
                removeButton.type = 'button';
                removeButton.className = 'remove-membro-btn';
                removeButton.addEventListener('click', () => removerMembro(nome));
                li.appendChild(removeButton);
                listaMembrosUL.appendChild(li);

                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = `membros[${index}]`;
                hiddenInput.value = nome;
                membrosHiddenContainer.appendChild(hiddenInput);
            });

            contadorMembrosSpan.textContent = `(${membros.length})`;
            if (numeroVisitantesInput) {
                numeroVisitantesInput.value = membros.length + 1;
            }
        };

        const removerMembro = (nomeParaRemover) => {
            membros = membros.filter(nome => nome !== nomeParaRemover);
            renderizarLista();
        };

        const adicionarMembro = () => {
            const nome = nomeMembroInput.value.trim();
            if (nome === '') return;
            if (membros.includes(nome)) {
                alert('Este membro já foi adicionado.');
                return;
            }
            membros.push(nome);
            renderizarLista();
            nomeMembroInput.value = '';
            nomeMembroInput.focus();
        };

        btnAddMembro.addEventListener('click', adicionarMembro);
        nomeMembroInput.addEventListener('keypress', (event) => {
            if (event.key === 'Enter') {
                event.preventDefault();
                adicionarMembro();
            }
        });
    }

    const form = document.getElementById('formAgendamento');
    if (form) {
        form.addEventListener('submit', function(event) {
            const activeTab = document.querySelector('.tab-link.active').dataset.tab;
            if (activeTab === 'individual') {
                document.getElementById('nomeInstituicao').value = 'Visita Individual';
                document.getElementById('tipoInstituicao').value = 'OUTRO';
                document.getElementById('numeroVisitantes').value = 1;
            } else {
                const membrosAdicionados = document.querySelectorAll('#membrosHiddenContainer input').length;
                document.getElementById('numeroVisitantes').value = membrosAdicionados + 1;
            }
        });
    }
});