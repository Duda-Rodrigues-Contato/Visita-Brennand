document.addEventListener('DOMContentLoaded', function() {

    // --- SELETORES DO FORMULÁRIO ---
    const form = document.getElementById('formAgendamento');
    const submitBtn = form.querySelector('.submit-btn');
    
    // Abas
    const tabLinks = document.querySelectorAll('.tab-link');
    const grupoFormSection = document.getElementById('grupo-form-section');
    const individualFormSection = document.getElementById('individual-form-section');
    const membrosSection = document.getElementById('membros-section');
    const responsavelLegend = document.getElementById('responsavel-legend');
    const detalhesLegend = document.getElementById('detalhes-legend');

    // Campos
    const nomeInstituicao = document.getElementById('nomeInstituicao');
    const tipoInstituicao = document.getElementById('tipoInstituicao');
    const cnpj = document.getElementById('cnpj');
    const cnpjInd = document.getElementById('cnpjInd');
    const nomeResponsavel = document.getElementById('nomeResponsavel');
    const emailResponsavel = document.getElementById('emailResponsavel');
    const telefoneResponsavel = document.getElementById('telefoneResponsavel');
    const origemVisitante = document.getElementById('origemVisitante');
    const dataVisitaInput = document.getElementById('dataVisita');
    const horarioChegadaSelect = document.getElementById('horarioChegada');
    
    let activeTab = 'grupo'; // Inicia como grupo por padrão

    // --- LÓGICA DAS ABAS (Existente) ---
    if (tabLinks.length > 0) {
        tabLinks.forEach(tab => {
            tab.addEventListener('click', () => {
                activeTab = tab.dataset.tab; // Atualiza a aba ativa
                tabLinks.forEach(link => link.classList.remove('active'));
                tab.classList.add('active');

                if (activeTab === 'individual') {
                    grupoFormSection.classList.add('hidden');
                    membrosSection.classList.add('hidden');
                    individualFormSection.classList.remove('hidden');
                    responsavelLegend.textContent = '2. Suas Informações de Contato';
                    detalhesLegend.textContent = '3. Detalhes da Visita';
                } else { // 'grupo'
                    individualFormSection.classList.add('hidden');
                    grupoFormSection.classList.remove('hidden');
                    membrosSection.classList.remove('hidden');
                    responsavelLegend.textContent = '2. Informações do Responsável';
                    detalhesLegend.textContent = '4. Detalhes da Visita';
                }
                clearAllErrors(); // Limpa erros ao trocar de aba
            });
        });
        document.querySelector('.tab-link[data-tab="grupo"]').click();
    }

    // --- LÓGICA DOS HORÁRIOS (Existente) ---
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
    
    // --- LÓGICA DE MEMBROS (Existente) ---
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

    // --- MÁSCARAS (Existente) ---
    if (typeof IMask !== 'undefined') {
        const telMask = IMask(telefoneResponsavel, { mask: '(00) 00000-0000' });
        const cnpjMask = IMask(cnpj, { mask: '00.000.000/0000-00' });
        const cpfMask = IMask(cnpjInd, { mask: '000.000.000-00' });
    }

    // --- NOVO SISTEMA DE VALIDAÇÃO (EM TEMPO REAL) ---

    /** Mostra uma mensagem de erro */
    function showError(inputId, message) {
        const errorElement = document.getElementById(inputId + '-error');
        if (errorElement) errorElement.textContent = message;
    }
    
    /** Limpa uma mensagem de erro */
    function clearError(inputId) {
        const errorElement = document.getElementById(inputId + '-error');
        if (errorElement) errorElement.textContent = '';
    }

    /** Limpa todos os erros de JS da tela */
    function clearAllErrors() {
        const errorMessages = form.querySelectorAll('.error-message');
        errorMessages.forEach(msg => {
            if (!msg.hasAttribute('th:if')) {
                msg.textContent = '';
            }
        });
    }

    /** Valida formato de email */
    function isValidEmail(email) {
        const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    // --- Funções de Validação Individuais ---

    function validateNomeInstituicao() {
        if (activeTab === 'grupo' && nomeInstituicao.value.trim() === '') {
            showError('nomeInstituicao', 'O nome da instituição é obrigatório.');
            return false;
        }
        clearError('nomeInstituicao');
        return true;
    }

    function validateTipoInstituicao() {
        if (activeTab === 'grupo' && tipoInstituicao.value === '') {
            showError('tipoInstituicao', 'O tipo de instituição é obrigatório.');
            return false;
        }
        clearError('tipoInstituicao');
        return true;
    }

    function validateNomeResponsavel() {
        if (nomeResponsavel.value.trim() === '') {
            showError('nomeResponsavel', 'O nome do responsável é obrigatório.');
            return false;
        }
        clearError('nomeResponsavel');
        return true;
    }

    function validateEmail() {
        if (emailResponsavel.value.trim() === '') {
            showError('emailResponsavel', 'O e-mail é obrigatório.');
            return false;
        } else if (!isValidEmail(emailResponsavel.value)) {
            showError('emailResponsavel', 'Por favor, insira um e-mail válido.');
            return false;
        }
        clearError('emailResponsavel');
        return true;
    }

    function validateTelefone() {
        // A máscara (00) 00000-0000 tem 15 caracteres
        if (telefoneResponsavel.value.trim().length < 15) {
            showError('telefoneResponsavel', 'O telefone é obrigatório.');
            return false;
        }
        clearError('telefoneResponsavel');
        return true;
    }

    function validateOrigemVisitante() {
        if (origemVisitante.value.trim() === '') {
            showError('origemVisitante', 'A origem (Cidade/Estado) é obrigatória.');
            return false;
        }
        clearError('origemVisitante');
        return true;
    }

    function validateDataVisita() {
        const dataSelecionadaStr = dataVisitaInput.value;
        const errorId = 'date-error'; // ID do <small> de erro da data

        if (!dataSelecionadaStr) {
            showError(errorId, 'A data da visita é obrigatória.');
            return false;
        }

        const dataSelecionada = new Date(dataSelecionadaStr + 'T00:00:00'); // Fuso local
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        if (dataSelecionada < hoje) {
            showError(errorId, 'A data da visita não pode ser no passado.');
            return false;
        }

        const diaDaSemana = dataSelecionada.getDay(); // Domingo = 0, Segunda = 1
        if (diaDaSemana === 1) { // 1 = Segunda-feira
            showError(errorId, 'Agendamentos não são permitidos às segundas-feiras.');
            return false;
        }
        
        clearError(errorId);
        return true;
    }

    function validateHorarioChegada() {
         if (horarioChegadaSelect.value === '') {
            showError('horarioChegada', 'O horário de chegada é obrigatório.');
            return false;
         }
         clearError('horarioChegada');
         return true;
    }

    /** Roda TODAS as validações (usado no Submit) */
    function validateForm() {
        // Roda todas as validações de uma vez
        const isNomeInstValid = validateNomeInstituicao();
        const isTipoInstValid = validateTipoInstituicao();
        const isNomeRespValid = validateNomeResponsavel();
        const isEmailValid = validateEmail();
        const isTelefoneValid = validateTelefone();
        const isOrigemValid = validateOrigemVisitante();
        const isDataValid = validateDataVisita();
        const isHorarioValid = validateHorarioChegada();
        
        if (activeTab === 'grupo') {
            return isNomeInstValid && isTipoInstValid && isNomeRespValid && isEmailValid && isTelefoneValid && isOrigemValid && isDataValid && isHorarioValid;
        } else { // individual
            return isNomeRespValid && isEmailValid && isTelefoneValid && isOrigemValid && isDataValid && isHorarioValid;
        }
    }

    // --- ATUALIZAÇÃO DOS HORÁRIOS (Separado da validação) ---
    function atualizarHorariosDisponiveis() {
        if (!dataVisitaInput) return;
        horarioChegadaSelect.disabled = false;
        
        // Se a data for inválida (passado ou segunda), não mostre horários
        if (!validateDataVisita()) {
             horarioChegadaSelect.innerHTML = '<option value="" disabled selected>Data inválida</option>';
             horarioChegadaSelect.disabled = true;
             return;
        }

        const dataSelecionada = new Date(dataVisitaInput.value + 'T00:00:00');
        const diaDaSemana = dataSelecionada.getDay();
        
        switch (diaDaSemana) {
            case 0: // Domingo
            case 6: // Sábado
                gerarHorarios('09:00', '17:30', 30);
                break;
            default: // Terça a Sexta
                gerarHorarios('10:00', '16:30', 30);
                break;
        }
    }

    // --- CONECTANDO OS EVENTOS DE VALIDAÇÃO ---
    
    // Adiciona validação "ao sair" (blur) ou "ao digitar" (input)
    nomeInstituicao.addEventListener('blur', validateNomeInstituicao);
    nomeInstituicao.addEventListener('input', validateNomeInstituicao);
    
    tipoInstituicao.addEventListener('blur', validateTipoInstituicao);
    tipoInstituicao.addEventListener('change', validateTipoInstituicao); // 'change' é melhor para <select>

    nomeResponsavel.addEventListener('blur', validateNomeResponsavel);
    nomeResponsavel.addEventListener('input', validateNomeResponsavel);

    emailResponsavel.addEventListener('blur', validateEmail);
    emailResponsavel.addEventListener('input', validateEmail);

    telefoneResponsavel.addEventListener('blur', validateTelefone);
    telefoneResponsavel.addEventListener('input', validateTelefone);
    
    origemVisitante.addEventListener('blur', validateOrigemVisitante);
    origemVisitante.addEventListener('input', validateOrigemVisitante);

    // Para data, o evento 'change' é melhor
    dataVisitaInput.addEventListener('change', () => {
        validateDataVisita();
        atualizarHorariosDisponiveis(); // Atualiza os horários também
    });

    horarioChegadaSelect.addEventListener('blur', validateHorarioChegada);
    horarioChegadaSelect.addEventListener('change', validateHorarioChegada);

    // Limpa os horários se a data for apagada
    dataVisitaInput.addEventListener('input', () => {
        if(dataVisitaInput.value === '') {
             horarioChegadaSelect.innerHTML = '<option value="" disabled selected>Selecione primeiro a data</option>';
             horarioChegadaSelect.disabled = true;
             clearError('date-error');
        }
    });

    // --- CONTROLE DE SUBMIT (Atualizado) ---
    if (form) {
        form.addEventListener('submit', function(event) {
            
            // 1. Impede o envio padrão
            event.preventDefault();

            // 2. Preenche os campos hidden (lógica antiga)
            if (activeTab === 'individual') {
                nomeInstituicao.value = 'Visita Individual';
                tipoInstituicao.value = 'OUTRO';
                document.getElementById('numeroVisitantes').value = 1;
            } else { // 'grupo'
                const membrosAdicionados = document.querySelectorAll('#membrosHiddenContainer input').length;
                document.getElementById('numeroVisitantes').value = membrosAdicionados + 1; // +1 (o responsável)
            }

            // 3. Roda nossa validação FINAL
            if (validateForm()) {
                // Se tudo estiver OK, desabilita o botão e envia o formulário
                submitBtn.disabled = true;
                submitBtn.textContent = 'AGUARDE...';
                form.submit(); // Envia o formulário de verdade
            } else {
                // Se der erro, avisa e rola para o primeiro erro
                console.log('Formulário inválido. Verifique os erros.');
                const firstError = form.querySelector('.error-message:not(:empty)');
                if(firstError) {
                    firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }
        });
    }
});