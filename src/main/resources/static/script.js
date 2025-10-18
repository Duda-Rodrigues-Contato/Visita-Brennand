document.addEventListener('DOMContentLoaded', function() {

    // --- SEÇÃO DE ABAS (TABS) ---
const tabLinks = document.querySelectorAll('.tab-link');
const grupoFormSection = document.getElementById('grupo-form-section');
const individualFormSection = document.getElementById('individual-form-section');
const detalhesLegend = document.getElementById('detalhes-legend');
const numeroVisitantesInput = document.getElementById('numeroVisitantes');

tabLinks.forEach(tab => {
    tab.addEventListener('click', () => {
        const tabType = tab.dataset.tab;

        // Remove a classe 'active' de todos e adiciona no clicado
        tabLinks.forEach(link => link.classList.remove('active'));
        tab.classList.add('active');

        // Lógica explícita para mostrar/esconder
        if (tabType === 'individual') {
            grupoFormSection.classList.add('hidden');
            individualFormSection.classList.remove('hidden');
            
            detalhesLegend.textContent = '2. Detalhes da Visita';
            numeroVisitantesInput.value = 1; // Garante que o número de visitantes seja 1
        } else { // Se for 'grupo'
            individualFormSection.classList.add('hidden');
            grupoFormSection.classList.remove('hidden');

            detalhesLegend.textContent = '4. Detalhes da Visita';
            // O valor para grupo será atualizado pela função de adicionar membros
        }
    });
});

    // Adiciona o evento de clique para cada aba
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabType = tab.dataset.tab;
            switchTab(tabType);
        });
    });

    // ---->> ADIÇÃO IMPORTANTE <<----
    // Define o estado inicial ao carregar a página para garantir que 'grupo' seja o padrão
    switchTab('grupo');

    // --- SEÇÃO DE HORÁRIOS E DATAS ---
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
            case 0: // Domingo
            case 6: // Sábado
                gerarHorarios('09:00', '17:30', 30);
                break;
            default: // Terça a Sexta
                gerarHorarios('10:00', '16:30', 30);
                break;
        }
    }

    if (dataVisitaInput) {
        dataVisitaInput.addEventListener('change', atualizarHorariosDisponiveis);
        atualizarHorariosDisponiveis(); // Executa ao carregar a página
    }
    
    // --- SEÇÃO DE MÁSCARAS (IMask) ---
    IMask(document.getElementById('telefoneResponsavel'), { mask: '(00) 00000-0000' });
    IMask(document.getElementById('telefoneResponsavelInd'), { mask: '(00) 00000-0000' });
    IMask(document.getElementById('cnpj'), { mask: '00.000.000/0000-00' });
    IMask(document.getElementById('cnpjInd'), { mask: '000.000.000-00' });


    // --- SEÇÃO DE ADICIONAR MEMBROS ---
    const btnAddMembro = document.getElementById('btnAddMembro');
    const nomeMembroInput = document.getElementById('nomeMembro');
    const listaMembrosUL = document.getElementById('listaMembros');
    const contadorMembrosSpan = document.getElementById('contadorMembros');
    const membrosHiddenContainer = document.getElementById('membrosHiddenContainer');
    
    let membros = []; 

    function renderizarLista() {
        listaMembrosUL.innerHTML = ''; 
        membrosHiddenContainer.innerHTML = '';

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
        numeroVisitantesInput.value = membros.length + 1; // +1 para contar o responsável
    }

    function adicionarMembro() {
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
    }

    function removerMembro(nomeParaRemover) {
        membros = membros.filter(nome => nome !== nomeParaRemover);
        renderizarLista();
    }

    if (btnAddMembro) {
        btnAddMembro.addEventListener('click', adicionarMembro);
        nomeMembroInput.addEventListener('keypress', event => {
            if (event.key === 'Enter') {
                event.preventDefault();
                adicionarMembro();
            }
        });
    }

    // --- SEÇÃO DE ENVIO DO FORMULÁRIO (SUBMIT) ---
    const form = document.getElementById('formAgendamento');
    form.addEventListener('submit', function(event) {
        const activeTab = document.querySelector('.tab-link.active').dataset.tab;
        
        if (activeTab === 'individual') {
            document.getElementById('nomeInstituicao').value = 'Visita Individual';
            document.getElementById('tipoInstituicao').value = 'OUTRO';
            document.getElementById('numeroVisitantes').value = 1;
        }
        
        if (membros.length === 0 && activeTab === 'grupo') {
            numeroVisitantesInput.value = 1;
        }
    });
});