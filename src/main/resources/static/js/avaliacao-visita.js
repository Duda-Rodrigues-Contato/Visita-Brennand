document.addEventListener('DOMContentLoaded', function () {

    document.querySelectorAll('.star-rating').forEach(ratingContainer => {
        const stars = Array.from(ratingContainer.querySelectorAll('label')).reverse();
        const inputs = Array.from(ratingContainer.querySelectorAll('input')).reverse();

        ratingContainer.addEventListener('mouseleave', () => {
            let checkedIndex = -1;
            inputs.forEach((input, i) => { if (input.checked) checkedIndex = i; });
            stars.forEach((star, i) => {
                star.style.color = i <= checkedIndex ? 'var(--laranja-principal)' : '#ccc';
                star.style.fontWeight = i <= checkedIndex ? '900' : 'normal';
            });
        });

        stars.forEach((star, index) => {
            star.addEventListener('mouseover', () => {
                for (let i = 0; i <= index; i++) {
                    stars[i].style.color = 'var(--laranja-hover)';
                    stars[i].style.fontWeight = '900';
                }
            });
        });
    });

    const form = document.getElementById('formAvaliacao');
    if(form) {
        const submitBtn = form.querySelector('.submit-btn');
        const geralError = document.getElementById('geral-error');

        form.addEventListener('submit', function(event) {
            const avaliacaoGeralChecked = form.querySelector('input[name="avaliacaoGeral"]:checked');

            if (!avaliacaoGeralChecked) {
                event.preventDefault();
                geralError.textContent = 'Por favor, selecione uma avaliação geral.';
                const starRatingDiv = form.querySelector('.star-rating'); 
                starRatingDiv.style.animation = 'shake 0.5s';
                setTimeout(() => starRatingDiv.style.animation = '', 500);
            } else {
                geralError.textContent = '';
                submitBtn.disabled = true;
                submitBtn.textContent = 'ENVIANDO...';
            }
        });
    }

    const style = document.createElement('style');
    style.innerHTML = `
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
            20%, 40%, 60%, 80% { transform: translateX(5px); }
        }
    `;
    document.head.appendChild(style);
});