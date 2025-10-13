
document.addEventListener('DOMContentLoaded', function() {
    
   
    const btnAddMembro = document.getElementById('btnAddMembro');
    const nomeMembroInput = document.getElementById('nomeMembro');
    const listaMembrosUI = document.getElementById('listaMembros');
    const form = document.getElementById('formAgendamento');
    const membrosHiddenContainer = document.getElementById('membrosHiddenContainer');
    
    let membros = [];

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
                removerMembro(li);
            };
            
            li.appendChild(removeBtn);
            listaMembrosUI.appendChild(li);
            
            membros.push(nome);
            
            nomeMembroInput.value = '';
            nomeMembroInput.focus();
        }
    });
    
    function removerMembro(listItem) {
        const nomeParaRemover = listItem.dataset.nome;
        membros = membros.filter(m => m !== nomeParaRemover);
        listItem.remove();
    }

    form.addEventListener('submit', function(event) {
        membrosHiddenContainer.innerHTML = '';
        membros.forEach((nome, index) => {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = `membros[${index}]`;
            hiddenInput.value = nome;
            membrosHiddenContainer.appendChild(hiddenInput);
        });
    });

   


    const telefoneInput = document.getElementById('telefoneResponsavel');
   
    const mascaraTelefoneOpcoes = {
        mask: '(00) 00000-0000'
    };
    
    const mascaraTelefone = IMask(telefoneInput, mascaraTelefoneOpcoes)
    const cnpjInput = document.getElementById('cnpj');
   
    const mascaraCnpjOpcoes = {
        mask: '00.000.000/0000-00'
    };

    const mascaraCnpj = IMask(cnpjInput, mascaraCnpjOpcoes);
});