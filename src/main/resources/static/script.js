document.addEventListener('DOMContentLoaded', function() {

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

    // --- SEÇÃO DE ABAS (TABS) ---
    const tabs = document.querySelectorAll('.tab-link');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabType = tab.dataset.tab;
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            document.querySelectorAll('.form-section').forEach(section => section.classList.add('hidden'));
            document.getElementById(`${tabType}-form-section`).classList.remove('hidden');
            document.getElementById('detalhes-legend').textContent = (tabType === 'individual') ? '2. Detalhes da Visita' : '4. Detalhes da Visita';
        });
    });

    // --- SEÇÃO DE MÁSCARAS (IMask) ---
    IMask(document.getElementById('telefoneResponsavel'), { mask: '(00) 00000-0000' });
    IMask(document.getElementById('telefoneResponsavelInd'), { mask: '(00) 00000-0000' });
    IMask(document.getElementById('cnpj'), { mask: '00.000.000/0000-00' });
    IMask(document.getElementById('cnpjInd'), { mask: '000.000.000-00' });


    // --- SEÇÃO DE ADICIONAR MEMBROS (VERSÃO CORRIGIDA E ROBUSTA) ---
    const btnAddMembro = document.getElementById('btnAddMembro');
    const nomeMembroInput = document.getElementById('nomeMembro');
    const listaMembrosUL = document.getElementById('listaMembros');
    const contadorMembrosSpan = document.getElementById('contadorMembros');
    const membrosHiddenContainer = document.getElementById('membrosHiddenContainer');
    const numeroVisitantesInput = document.getElementById('numeroVisitantes');

    let membros = []; // Array para guardar os nomes, evitando bugs

    function renderizarLista() {
        listaMembrosUL.innerHTML = ''; // Limpa a lista visual
        membrosHiddenContainer.innerHTML = ''; // Limpa os inputs hidden

        membros.forEach((nome, index) => {
            // Cria o item da lista (li)
            const li = document.createElement('li');
            li.textContent = nome;

            // Cria o botão de remover
            const removeButton = document.createElement('button');
            removeButton.textContent = 'x';
            removeButton.type = 'button';
            removeButton.className = 'remove-membro-btn';
            removeButton.addEventListener('click', () => removerMembro(nome));
            li.appendChild(removeButton);
            listaMembrosUL.appendChild(li);

            // Cria o input hidden para enviar o nome no formulário
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = `membros[${index}]`;
            hiddenInput.value = nome;
            membrosHiddenContainer.appendChild(hiddenInput);
        });

        // Atualiza o contador e o número total de visitantes
        contadorMembrosSpan.textContent = `(${membros.length})`;
        numeroVisitantesInput.value = membros.length + 1; // +1 para contar o responsável
    }

    function adicionarMembro() {
        const nome = nomeMembroInput.value.trim();
        if (nome === '') return; // Não adiciona se estiver vazio
        if (membros.includes(nome)) { // Impede nomes repetidos
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
            // Preenche os dados da visita individual nos campos principais
            document.getElementById('nomeInstituicao').value = 'Visita Individual';
            document.getElementById('tipoInstituicao').value = 'OUTRO';
            document.getElementById('numeroVisitantes').value = 1;
        }
        
        // Se a lista de membros estiver vazia, garante que o número de visitantes é 1 (o responsável)
        if (membros.length === 0 && activeTab === 'grupo') {
            numeroVisitantesInput.value = 1;
        }
    });
});