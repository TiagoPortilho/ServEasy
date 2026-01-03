document.addEventListener('DOMContentLoaded', function() {
    console.log('Novos pedidos carregado');

    // Aguardar o JWT interceptor carregar antes de fazer requisições
    waitForJwtInterceptor().then(() => {
        console.log('[novos-pedidos.js] JWT Interceptor carregado, iniciando aplicação...');
        loadNovosPedidos();
        setInterval(loadNovosPedidos, 30000);
    });
});

// Função para aguardar o JWT interceptor carregar
function waitForJwtInterceptor() {
    return new Promise((resolve) => {
        if (window.jwtInterceptorLoaded) {
            console.log('[novos-pedidos.js] JWT Interceptor já estava carregado');
            resolve();
            return;
        }

        console.log('[novos-pedidos.js] Aguardando JWT Interceptor carregar...');
        const checkInterval = setInterval(() => {
            if (window.jwtInterceptorLoaded) {
                console.log('[novos-pedidos.js] JWT Interceptor carregado com sucesso');
                clearInterval(checkInterval);
                resolve();
            }
        }, 100);

        // Timeout de segurança (5 segundos)
        setTimeout(() => {
            if (!window.jwtInterceptorLoaded) {
                console.warn('[novos-pedidos.js] Timeout aguardando JWT Interceptor, continuando mesmo assim');
                clearInterval(checkInterval);
                resolve();
            }
        }, 5000);
    });
}

function loadNovosPedidos() {
    fetch('/api/orders?status=NOVO')
        .then(response => response.json())
        .then(data => {
            console.log('Response data:', data);
            if (data.status === 'success' && data.data) {
                displayPedidos(data.data);
            } else {
                console.error('Error in response:', data.message);
                document.getElementById('pedidosGrid').innerHTML = '<div class="alert alert-danger">Erro ao carregar pedidos</div>';
            }
        })
        .catch(error => {
            console.error('Erro:', error);
            document.getElementById('pedidosGrid').innerHTML = '<div class="alert alert-danger">Erro ao carregar pedidos</div>';
        });
}

function displayPedidos(orders) {
    const container = document.getElementById('pedidosGrid');
    const emptyMessage = document.getElementById('emptyMessage');
    
    if (!orders || orders.length === 0) {
        container.innerHTML = '';
        if (emptyMessage) emptyMessage.classList.remove('hidden');
        return;
    }
    
    if (emptyMessage) emptyMessage.classList.add('hidden');
    
    container.innerHTML = orders.map(order => {
        const createdAt = new Date(order.createdAt);
        const timeElapsed = Math.floor((new Date() - createdAt) / (1000 * 60));
        const timeDisplay = timeElapsed >= 0 ? timeElapsed : 0;
        
        return `<div class="pedido-card">
            <div class="pedido-header">
                <div class="pedido-info-left">
                    <span class="pedido-numero">Pedido #${order.id}</span>
                    ${order.customerName ? `<span class="pedido-customer">Cliente: ${order.customerName}</span>` : ''}
                    <div class="pedido-mesa-status">
                        <span class="pedido-mesa">Mesa ${order.tableNumber || 'N/A'}</span>
                        <span class="pedido-status status-novo">NOVO</span>
                    </div>
                </div>
            </div>
            
            ${order.notes ? `<div class="pedido-descricao">
                <strong>Observações:</strong> ${order.notes}
            </div>` : ''}
            
            <div class="pedido-itens">
                <h4>Itens do Pedido:</h4>
                ${order.items ? order.items.map(item => 
                    `<div class="item-pedido">
                        <span class="item-nome">${item.menuItem.name}</span>
                        <span class="item-quantidade">${item.quantity}x</span>
                    </div>`
                ).join('') : ''}
            </div>
            
            <div class="pedido-tempo">
                <i class="fas fa-clock"></i>
                <span>Pedido há ${timeDisplay} minutos</span>
            </div>
            
            <div class="pedido-acoes">
                <button onclick="iniciarPreparo(${order.id})" class="btn btn-primary">
                    <i class="fas fa-play"></i> Iniciar Preparo
                </button>
            </div>
        </div>`;
    }).join('');
}

function iniciarPreparo(orderId) {
    fetch(`/api/orders/${orderId}/status`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({status: 'EM_ANDAMENTO'})
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            alert('Preparo iniciado!');
            loadNovosPedidos();
        } else {
            alert('Erro ao iniciar preparo: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao iniciar preparo');
    });
}
