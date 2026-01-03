document.addEventListener('DOMContentLoaded', function() {
    console.log('Pedidos prontos carregado');

    // Aguardar o JWT interceptor carregar antes de fazer requisições
    waitForJwtInterceptor().then(() => {
        console.log('[pedidos-prontos.js] JWT Interceptor carregado, iniciando aplicação...');
        loadPedidosProntos();
        setInterval(loadPedidosProntos, 30000);
    });
});

// Função para aguardar o JWT interceptor carregar
function waitForJwtInterceptor() {
    return new Promise((resolve) => {
        if (window.jwtInterceptorLoaded) {
            console.log('[pedidos-prontos.js] JWT Interceptor já estava carregado');
            resolve();
            return;
        }

        console.log('[pedidos-prontos.js] Aguardando JWT Interceptor carregar...');
        const checkInterval = setInterval(() => {
            if (window.jwtInterceptorLoaded) {
                console.log('[pedidos-prontos.js] JWT Interceptor carregado com sucesso');
                clearInterval(checkInterval);
                resolve();
            }
        }, 100);

        // Timeout de segurança (5 segundos)
        setTimeout(() => {
            if (!window.jwtInterceptorLoaded) {
                console.warn('[pedidos-prontos.js] Timeout aguardando JWT Interceptor, continuando mesmo assim');
                clearInterval(checkInterval);
                resolve();
            }
        }, 5000);
    });
}

function loadPedidosProntos() {
    fetch('/api/orders?status=PRONTO')
        .then(response => response.json())
        .then(data => {
            console.log('Response data:', data);
            if (data.status === 'success' && data.data) {
                displayPedidosProntos(data.data);
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

function displayPedidosProntos(orders) {
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
                        <span class="pedido-status status-pronto">PRONTO</span>
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
                <span>Pronto há ${timeDisplay} minutos</span>
            </div>
            
            <div class="pedido-acoes">
                <button onclick="marcarEntregue(${order.id})" class="btn btn-success">
                    <i class="fas fa-truck"></i> Marcar como Entregue
                </button>
            </div>
        </div>`;
    }).join('');
}

function marcarEntregue(orderId) {
    fetch(`/api/orders/${orderId}/status`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({status: 'ENTREGUE'})
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            alert('Pedido marcado como entregue!');
            loadPedidosProntos();
        } else {
            alert('Erro ao marcar como entregue: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao marcar como entregue');
    });
}

function entregarPedido(orderId) {
    fetch(`/api/orders/${orderId}/status`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({status: 'ENTREGUE'})
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            alert('Pedido entregue!');
            loadPedidosProntos();
        } else {
            alert('Erro ao entregar pedido: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao entregar pedido');
    });
}
