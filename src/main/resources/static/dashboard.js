document.addEventListener('DOMContentLoaded', function() {
    const dateFilter = document.getElementById('date-filter');
    const btnHoje = document.getElementById('btn-hoje');

    
    dateFilter.addEventListener('change', function() {
        const selectedDate = this.value;
        if (selectedDate) {
            
            window.location.href = '/dashboard?data=' + selectedDate;
        }
    });

    
    btnHoje.addEventListener('click', function() {
        
        const today = new Date().toISOString().slice(0, 10);
        window.location.href = '/dashboard?data=' + today;
    });
    
    
    
});