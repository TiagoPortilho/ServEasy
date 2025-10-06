document.addEventListener('DOMContentLoaded', async function() {document.addEventListener('DOMContentLoaded', async function() {document.addEventListener('DOMContentLoaded', async function() {document.addEventListener('DOMContentLoaded', async function() {// ===== DADOS SIMULADOS =====

    const container = document.getElementById('prontosList');

        const container = document.getElementById('prontosList');

    if (container) {

        await loadPedidosProntos();        const prontosListContainer = document.getElementById('prontosList');

        setInterval(loadPedidosProntos, 30000);

    }    if (container) {

});

        await loadPedidosProntos();    const prontosListContainer = document.getElementById('prontosList');let pedidosProntos = [

async function loadPedidosProntos() {

    try {        setInterval(loadPedidosProntos, 30000);

        const response = await fetch('/api/orders?status=READY');

            }    if (prontosListContainer) {

        if (!response.ok) {

            throw new Error('HTTP error! status: ' + response.status);});

        }

                await loadPedidosProntos();    {

        const orders = await response.json();

        displayPedidosProntos(orders);async function loadPedidosProntos() {

        

    } catch (error) {    try {        

        console.error('Erro ao carregar pedidos prontos:', error);

        displayError('Erro ao carregar pedidos prontos');        const response = await fetch('/api/orders?status=READY');

    }

}                // Atualizar automaticamente a cada 30 segundos    if (prontosListContainer) {        id: 1,



function displayPedidosProntos(orders) {        if (!response.ok) {

    const container = document.getElementById('prontosList');

                throw new Error('HTTP error! status: ' + response.status);        setInterval(loadPedidosProntos, 30000);

    if (!container) {

        return;        }

    }

                }        await loadPedidosProntos();        numeroMesa: 1,

    if (!orders || orders.length === 0) {

        container.innerHTML = '<div class="col-12"><div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> Nenhum pedido pronto no momento</div></div>';        const orders = await response.json();

        return;

    }        displayPedidosProntos(orders);});

    

    container.innerHTML = orders.map(function(order) {        

        const waitingTooLong = getWaitingMinutes(order.orderDate) > 10;

            } catch (error) {                numeroPedido: "PED-007",

        return `

            <div class="col-md-6 col-lg-4">        console.error('Erro ao carregar pedidos prontos:', error);

                <div class="card mb-3 ${waitingTooLong ? 'border-danger' : 'border-success'}">

                    <div class="card-header d-flex justify-content-between align-items-center">        displayError('Erro ao carregar pedidos prontos');async function loadPedidosProntos() {

                        <h5 class="mb-0">Pedido #${order.id}</h5>

                        <div>    }

                            <span class="badge bg-success">Mesa ${order.table ? order.table.tableNumber : 'N/A'}</span>

                            ${waitingTooLong ? '<span class="badge bg-danger ms-1">RETIRAR</span>' : ''}}    try {        // Atualizar automaticamente a cada 30 segundos        itens: [

                        </div>

                    </div>

                    <div class="card-body">

                        <div class="mb-2">function displayPedidosProntos(orders) {        const response = await fetch('/api/orders?status=READY');

                            <small class="text-muted">

                                <i class="fas fa-clock"></i>    const container = document.getElementById('prontosList');

                                Pronto há ${getWaitingTime(order.orderDate)}

                            </small>                    setInterval(loadPedidosProntos, 30000);            { nome: "Lasanha Bolonhesa", quantidade: 1 },

                        </div>

                            if (!container) {

                        <div class="order-items mb-3">

                            <h6>Itens do Pedido:</h6>        return;        if (!response.ok) {

                            ${order.items ? order.items.map(function(item) {

                                return `    }

                                    <div class="item-row d-flex justify-content-between align-items-center mb-2">

                                        <div>                throw new Error(`HTTP error! status: ${response.status}`);    }            { nome: "Salada Verde", quantidade: 1 }

                                            <span class="fw-bold">${item.quantity}x</span>

                                            <span>${item.menuItem ? item.menuItem.name : 'Item não encontrado'}</span>    if (!orders || orders.length === 0) {

                                        </div>

                                        <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>        container.innerHTML = '<div class="col-12"><div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> Nenhum pedido pronto no momento</div></div>';        }

                                    </div>

                                `;        return;

                            }).join('') : '<p class="text-muted">Sem itens</p>'}

                        </div>    }        });        ],

                        

                        <div class="mb-3">    

                            <div class="d-flex align-items-center">

                                <i class="fas fa-check-circle text-success me-2"></i>    container.innerHTML = orders.map(order => {        const orders = await response.json();

                                <span class="text-success fw-bold">Pedido Pronto</span>

                            </div>        const waitingTooLong = getWaitingMinutes(order.orderDate) > 10;

                        </div>

                                        displayPedidosProntos(orders);        tempoEsperando: "3 min",

                        <div class="d-flex justify-content-between align-items-center">

                            <div class="total">        return `

                                <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>

                            </div>            <div class="col-md-6 col-lg-4">        

                            <button class="btn btn-primary btn-sm" 

                                    onclick="entregarPedido(${order.id})">                <div class="card mb-3 ${waitingTooLong ? 'border-danger' : 'border-success'}">

                                <i class="fas fa-hand-holding"></i>

                                Entregar                    <div class="card-header d-flex justify-content-between align-items-center">    } catch (error) {async function loadPedidosProntos() {        timestamp: new Date(Date.now() - 3 * 60000),

                            </button>

                        </div>                        <h5 class="mb-0">Pedido #${order.id}</h5>

                    </div>

                </div>                        <div>        console.error('Erro ao carregar pedidos prontos:', error);

            </div>

        `;                            <span class="badge bg-success">Mesa ${order.table?.tableNumber || 'N/A'}</span>

    }).join('');

}                            ${waitingTooLong ? '<span class="badge bg-danger ms-1">RETIRAR</span>' : ''}        displayError('Erro ao carregar pedidos prontos');    try {        aguardandoRetirada: true



async function entregarPedido(orderId) {                        </div>

    if (!orderId) {

        alert('ID do pedido inválido');                    </div>    }

        return;

    }                    <div class="card-body">

    

    try {                        <div class="mb-2">}        const response = await fetch('/api/orders?status=READY');    },

        const response = await fetch('/api/orders/' + orderId + '/status', {

            method: 'PUT',                            <small class="text-muted">

            headers: {

                'Content-Type': 'application/json'                                <i class="fas fa-clock"></i>

            },

            body: JSON.stringify({ status: 'DELIVERED' })                                Pronto há ${getWaitingTime(order.orderDate)}

        });

                                    </small>function displayPedidosProntos(orders) {            {

        if (!response.ok) {

            throw new Error('HTTP error! status: ' + response.status);                        </div>

        }

                                    const container = document.getElementById('prontosList');

        await loadPedidosProntos();

        showSuccess('Pedido entregue com sucesso!');                        <div class="order-items mb-3">

        

    } catch (error) {                            <h6>Itens do Pedido:</h6>            if (!response.ok) {        id: 2,

        console.error('Erro ao entregar pedido:', error);

        alert('Erro ao entregar pedido');                            ${order.items?.map(item => `

    }

}                                <div class="item-row d-flex justify-content-between align-items-center mb-2">    if (!container) {



function getWaitingMinutes(orderDate) {                                    <div>

    if (!orderDate) return 0;

                                            <span class="fw-bold">${item.quantity}x</span>        console.error('Container prontosList não encontrado');            throw new Error(`HTTP error! status: ${response.status}`);        numeroMesa: 6,

    try {

        const now = new Date();                                        <span>${item.menuItem?.name || 'Item não encontrado'}</span>

        const order = new Date(orderDate);

        const diffMs = now - order;                                    </div>        return;

        return Math.floor(diffMs / (1000 * 60));

    } catch (error) {                                    <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>

        return 0;

    }                                </div>    }        }        numeroPedido: "PED-008",

}

                            `).join('') || '<p class="text-muted">Sem itens</p>'}

function getWaitingTime(orderDate) {

    const mins = getWaitingMinutes(orderDate);                        </div>    

    

    if (mins < 1) return '< 1 min';                        

    if (mins < 60) return mins + ' min';

                            <div class="mb-3">    if (!orders || orders.length === 0) {                itens: [

    const hours = Math.floor(mins / 60);

    const remainingMins = mins % 60;                            <div class="d-flex align-items-center">

    return hours + 'h ' + remainingMins + 'min';

}                                <i class="fas fa-check-circle text-success me-2"></i>        container.innerHTML = `



function displayError(message) {                                <span class="text-success fw-bold">Pedido Pronto</span>

    const container = document.getElementById('prontosList');

    if (container) {                            </div>            <div class="col-12">        const orders = await response.json();            { nome: "Peixe Grelhado", quantidade: 1 },

        container.innerHTML = '<div class="col-12"><div class="alert alert-danger text-center"><i class="fas fa-exclamation-triangle"></i> ' + message + '</div></div>';

    }                        </div>

}

                                        <div class="alert alert-info text-center">

function showSuccess(message) {

    const toast = document.createElement('div');                        <div class="d-flex justify-content-between align-items-center">

    toast.className = 'toast align-items-center text-white bg-success border-0 show';

    toast.innerHTML = '<div class="d-flex"><div class="toast-body"><i class="fas fa-check-circle"></i> ' + message + '</div></div>';                            <div class="total">                    <i class="fas fa-info-circle"></i>        displayPedidosProntos(orders);            { nome: "Legumes no Vapor", quantidade: 1 },

    toast.style.position = 'fixed';

    toast.style.top = '20px';                                <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>

    toast.style.right = '20px';

    toast.style.zIndex = '9999';                            </div>                    Nenhum pedido pronto no momento

    document.body.appendChild(toast);

    setTimeout(function() { toast.remove(); }, 3000);                            <button class="btn btn-primary btn-sm" 

}
                                    onclick="entregarPedido(${order.id})">                </div>                    { nome: "Água", quantidade: 2 }

                                <i class="fas fa-hand-holding"></i>

                                Entregar            </div>

                            </button>

                        </div>        `;    } catch (error) {        ],

                    </div>

                </div>        return;

            </div>

        `;    }        console.error('Erro ao carregar pedidos prontos:', error);        tempoEsperando: "7 min",

    }).join('');

}    



async function entregarPedido(orderId) {    container.innerHTML = orders.map(order => `        displayError('Erro ao carregar pedidos prontos');        timestamp: new Date(Date.now() - 7 * 60000),

    if (!orderId) {

        alert('ID do pedido inválido');        <div class="col-md-6 col-lg-4">

        return;

    }            <div class="card mb-3 ${isWaitingTooLong(order) ? 'border-danger' : 'border-success'}">    }        aguardandoRetirada: true

    

    try {                <div class="card-header d-flex justify-content-between align-items-center">

        const response = await fetch('/api/orders/' + orderId + '/status', {

            method: 'PUT',                    <h5 class="mb-0">Pedido #${order.id}</h5>}    },

            headers: {

                'Content-Type': 'application/json'                    <div>

            },

            body: JSON.stringify({ status: 'DELIVERED' })                        <span class="badge bg-success">Mesa ${order.table?.tableNumber || 'N/A'}</span>    {

        });

                                ${isWaitingTooLong(order) ? '<span class="badge bg-danger ms-1">RETIRAR</span>' : ''}

        if (!response.ok) {

            throw new Error('HTTP error! status: ' + response.status);                    </div>function displayPedidosProntos(orders) {        id: 3,

        }

                        </div>

        await loadPedidosProntos();

        showSuccess('Pedido entregue com sucesso!');                <div class="card-body">    const container = document.getElementById('prontosList');        numeroMesa: 9,

        

    } catch (error) {                    <div class="mb-2">

        console.error('Erro ao entregar pedido:', error);

        alert('Erro ao entregar pedido');                        <small class="text-muted">            numeroPedido: "PED-009",

    }

}                            <i class="fas fa-clock"></i>



function getWaitingMinutes(orderDate) {                            Pronto há ${getWaitingTime(order.orderDate)}    if (!container) {        itens: [

    if (!orderDate) return 0;

                            </small>

    try {

        const now = new Date();                    </div>        console.error('Container prontosList não encontrado');            { nome: "Wrap de Frango", quantidade: 2 },

        const order = new Date(orderDate);

        const diffMs = now - order;                    

        return Math.floor(diffMs / (1000 * 60));

    } catch (error) {                    <div class="order-items mb-3">        return;            { nome: "Batata Doce", quantidade: 1 }

        return 0;

    }                        <h6>Itens do Pedido:</h6>

}

                        ${order.items?.map(item => `    }        ],

function getWaitingTime(orderDate) {

    const mins = getWaitingMinutes(orderDate);                            <div class="item-row d-flex justify-content-between align-items-center mb-2">

    

    if (mins < 1) return '< 1 min';                                <div>            tempoEsperando: "12 min",

    if (mins < 60) return mins + ' min';

                                        <span class="fw-bold">${item.quantity}x</span>

    const hours = Math.floor(mins / 60);

    const remainingMins = mins % 60;                                    <span>${item.menuItem?.name || 'Item não encontrado'}</span>    if (!orders || orders.length === 0) {        timestamp: new Date(Date.now() - 12 * 60000),

    return hours + 'h ' + remainingMins + 'min';

}                                </div>



function displayError(message) {                                <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>        container.innerHTML = `        aguardandoRetirada: true

    const container = document.getElementById('prontosList');

    if (container) {                            </div>

        container.innerHTML = '<div class="col-12"><div class="alert alert-danger text-center"><i class="fas fa-exclamation-triangle"></i> ' + message + '</div></div>';

    }                        `).join('') || '<p class="text-muted">Sem itens</p>'}            <div class="col-12">    }

}

                    </div>

function showSuccess(message) {

    const toast = document.createElement('div');                                    <div class="alert alert-info text-center">];

    toast.className = 'toast align-items-center text-white bg-success border-0 show';

    toast.innerHTML = '<div class="d-flex"><div class="toast-body"><i class="fas fa-check-circle"></i> ' + message + '</div></div>';                    <div class="mb-3">

    toast.style.position = 'fixed';

    toast.style.top = '20px';                        <div class="d-flex align-items-center">                    <i class="fas fa-info-circle"></i>

    toast.style.right = '20px';

    toast.style.zIndex = '9999';                            <i class="fas fa-check-circle text-success me-2"></i>

    document.body.appendChild(toast);

    setTimeout(function() { toast.remove(); }, 3000);                            <span class="text-success fw-bold">Pedido Pronto</span>                    Nenhum pedido pronto no momentolet pedidoSelecionado = null;

}
                        </div>

                    </div>                </div>

                    

                    <div class="d-flex justify-content-between align-items-center">            </div>// ===== INICIALIZAÇÃO =====

                        <div class="total">

                            <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>        `;document.addEventListener('DOMContentLoaded', function() {

                        </div>

                        <button class="btn btn-primary btn-sm"         return;    carregarPedidos();

                                onclick="entregarPedido(${order.id})">

                            <i class="fas fa-hand-holding"></i>    }    configurarFiltros();

                            Entregar

                        </button>        configurarBusca();

                    </div>

                </div>    container.innerHTML = orders.map(order => `    atualizarTempos();

            </div>

        </div>        <div class="col-md-6 col-lg-4">    

    `).join('');

}            <div class="card mb-3 ${isWaitingTooLong(order) ? 'border-danger' : 'border-success'}">    // Atualizar tempos a cada minuto



async function entregarPedido(orderId) {                <div class="card-header d-flex justify-content-between align-items-center">    setInterval(atualizarTempos, 60000);

    if (!orderId) {

        alert('ID do pedido inválido');                    <h5 class="mb-0">Pedido #${order.id}</h5>});

        return;

    }                    <div>

    

    try {                        <span class="badge bg-success">Mesa ${order.table?.tableNumber || 'N/A'}</span>// ===== CARREGAR E EXIBIR PEDIDOS =====

        const response = await fetch(`/api/orders/${orderId}/status`, {

            method: 'PUT',                        ${isWaitingTooLong(order) ? '<span class="badge bg-danger ms-1">RETIRAR</span>' : ''}function carregarPedidos(filtro = 'todos', busca = '') {

            headers: {

                'Content-Type': 'application/json'                    </div>    const grid = document.getElementById('pedidosGrid');

            },

            body: JSON.stringify({ status: 'DELIVERED' })                </div>    const emptyMessage = document.getElementById('emptyMessage');

        });

                        <div class="card-body">    

        if (!response.ok) {

            throw new Error(`HTTP error! status: ${response.status}`);                    <div class="mb-2">    let pedidosFiltrados = [...pedidosProntos];

        }

                                <small class="text-muted">    

        // Recarregar lista de pedidos

        await loadPedidosProntos();                            <i class="fas fa-clock"></i>    // Aplicar filtros

        

        // Mostrar confirmação                            Pronto há ${getWaitingTime(order.orderDate)}    if (filtro === 'aguardando') {

        showSuccessMessage('Pedido entregue com sucesso!');

                                </small>        pedidosFiltrados = pedidosFiltrados.filter(p => p.aguardandoRetirada);

    } catch (error) {

        console.error('Erro ao entregar pedido:', error);                    </div>    } else if (filtro === 'antigos') {

        alert('Erro ao entregar pedido');

    }                            pedidosFiltrados.sort((a, b) => a.timestamp - b.timestamp);

}

                    <div class="order-items mb-3">    }

function getWaitingTime(orderDate) {

    if (!orderDate) return '0 min';                        <h6>Itens do Pedido:</h6>    

    

    try {                        ${order.items?.map(item => `    // Aplicar busca

        const now = new Date();

        const order = new Date(orderDate);                            <div class="item-row d-flex justify-content-between align-items-center mb-2">    if (busca) {

        const diffMs = now - order;

        const diffMins = Math.floor(diffMs / (1000 * 60));                                <div>        pedidosFiltrados = pedidosFiltrados.filter(pedido => 

        

        if (diffMins < 1) return '< 1 min';                                    <span class="fw-bold">${item.quantity}x</span>            pedido.numeroPedido.toLowerCase().includes(busca.toLowerCase()) ||

        if (diffMins < 60) return `${diffMins} min`;

                                            <span>${item.menuItem?.name || 'Item não encontrado'}</span>            pedido.numeroMesa.toString().includes(busca) ||

        const hours = Math.floor(diffMins / 60);

        const mins = diffMins % 60;                                </div>            pedido.itens.some(item => item.nome.toLowerCase().includes(busca.toLowerCase()))

        return `${hours}h ${mins}min`;

    } catch (error) {                                <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>        );

        return '0 min';

    }                            </div>    }

}

                        `).join('') || '<p class="text-muted">Sem itens</p>'}    

function isWaitingTooLong(order) {

    if (!order.orderDate) return false;                    </div>    if (pedidosFiltrados.length === 0) {

    

    try {                            grid.innerHTML = '';

        const now = new Date();

        const orderTime = new Date(order.orderDate);                    <div class="mb-3">        emptyMessage.classList.remove('hidden');

        const diffMs = now - orderTime;

        const diffMins = Math.floor(diffMs / (1000 * 60));                        <div class="d-flex align-items-center">        return;

        

        // Considerar que está esperando muito se passou mais de 10 minutos                            <i class="fas fa-check-circle text-success me-2"></i>    }

        return diffMins > 10;

    } catch (error) {                            <span class="text-success fw-bold">Pedido Pronto</span>    

        return false;

    }                        </div>    emptyMessage.classList.add('hidden');

}

                    </div>    grid.innerHTML = pedidosFiltrados.map(pedido => criarCardPedido(pedido)).join('');

function displayError(message) {

    const container = document.getElementById('prontosList');                    }

    if (container) {

        container.innerHTML = `                    <div class="d-flex justify-content-between align-items-center">

            <div class="col-12">

                <div class="alert alert-danger text-center">                        <div class="total">function criarCardPedido(pedido) {

                    <i class="fas fa-exclamation-triangle"></i>

                    ${message}                            <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>    const tempoMinutos = Math.floor((Date.now() - pedido.timestamp) / 60000);

                </div>

            </div>                        </div>    const urgentClass = tempoMinutos > 10 ? 'style="color: #dc3545;"' : '';

        `;

    }                        <button class="btn btn-primary btn-sm"     

}

                                onclick="entregarPedido(${order.id})">    return `

function showSuccessMessage(message) {

    // Criar toast de sucesso                            <i class="fas fa-hand-holding"></i>        <div class="pedido-card fade-in" data-id="${pedido.id}">

    const toastHtml = `

        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">                            Entregar            <div class="pedido-header">

            <div class="d-flex">

                <div class="toast-body">                        </button>                <div class="pedido-numero">${pedido.numeroPedido}</div>

                    <i class="fas fa-check-circle"></i>

                    ${message}                    </div>                <div class="pedido-mesa">Mesa ${pedido.numeroMesa}</div>

                </div>

                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>                </div>                <div class="pedido-status status-pronto">

            </div>

        </div>            </div>                    <i class="fas fa-check-circle"></i> Pronto

    `;

            </div>                </div>

    // Verificar se existe container de toasts

    let toastContainer = document.getElementById('toast-container');    `).join('');            </div>

    if (!toastContainer) {

        toastContainer = document.createElement('div');}            

        toastContainer.id = 'toast-container';

        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';            <div class="pedido-itens">

        document.body.appendChild(toastContainer);

    }async function entregarPedido(orderId) {                <h4>Itens do Pedido:</h4>

    

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);    if (!orderId) {                ${pedido.itens.map(item => `

    

    // Inicializar e mostrar toast        alert('ID do pedido inválido');                    <div class="item-pedido">

    const toastElement = toastContainer.lastElementChild;

    const toast = new bootstrap.Toast(toastElement);        return;                        <span class="item-nome">${item.nome}</span>

    toast.show();

        }                        <span class="item-quantidade">${item.quantidade}x</span>

    // Remover toast após ser escondido

    toastElement.addEventListener('hidden.bs.toast', () => {                        </div>

        toastElement.remove();

    });    try {                `).join('')}

}
        const response = await fetch(`/api/orders/${orderId}/status`, {            </div>

            method: 'PUT',            

            headers: {            <div class="pedido-tempo" ${urgentClass}>

                'Content-Type': 'application/json'                <i class="fas fa-clock"></i> Aguardando retirada há ${pedido.tempoEsperando}

            },                ${tempoMinutos > 10 ? '<i class="fas fa-exclamation-triangle" style="margin-left: 10px; color: #dc3545;"></i>' : ''}

            body: JSON.stringify({ status: 'DELIVERED' })            </div>

        });            

                    <div class="pedido-acoes">

        if (!response.ok) {                <button class="btn btn-success btn-sm" onclick="abrirModalEntregar(${pedido.id})">

            throw new Error(`HTTP error! status: ${response.status}`);                    <i class="fas fa-utensils"></i> Entregar

        }                </button>

                        <button class="btn btn-danger btn-sm" onclick="abrirModalDescartar(${pedido.id})">

        // Recarregar lista de pedidos                    <i class="fas fa-trash"></i> Descartar

        await loadPedidosProntos();                </button>

                    </div>

        // Mostrar confirmação        </div>

        showSuccessMessage('Pedido entregue com sucesso!');    `;

        }

    } catch (error) {

        console.error('Erro ao entregar pedido:', error);// ===== FILTROS E BUSCA =====

        alert('Erro ao entregar pedido');function configurarFiltros() {

    }    const filterButtons = document.querySelectorAll('.filter-btn');

}    

    filterButtons.forEach(btn => {

function getWaitingTime(orderDate) {        btn.addEventListener('click', function() {

    if (!orderDate) return '0 min';            // Remover active de todos

                filterButtons.forEach(b => b.classList.remove('active'));

    try {            // Adicionar active no clicado

        const now = new Date();            this.classList.add('active');

        const order = new Date(orderDate);            

        const diffMs = now - order;            const filtro = this.dataset.filter;

        const diffMins = Math.floor(diffMs / (1000 * 60));            const busca = document.getElementById('searchInput').value;

                    carregarPedidos(filtro, busca);

        if (diffMins < 1) return '< 1 min';        });

        if (diffMins < 60) return `${diffMins} min`;    });

        }

        const hours = Math.floor(diffMins / 60);

        const mins = diffMins % 60;function configurarBusca() {

        return `${hours}h ${mins}min`;    const searchInput = document.getElementById('searchInput');

    } catch (error) {    

        return '0 min';    searchInput.addEventListener('input', function() {

    }        const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;

}        carregarPedidos(filtroAtivo, this.value);

    });

function isWaitingTooLong(order) {}

    if (!order.orderDate) return false;

    // ===== ATUALIZAR TEMPOS =====

    try {function atualizarTempos() {

        const now = new Date();    pedidosProntos.forEach(pedido => {

        const orderTime = new Date(order.orderDate);        const minutosEsperando = Math.floor((Date.now() - pedido.timestamp) / 60000);

        const diffMs = now - orderTime;        pedido.tempoEsperando = `${minutosEsperando} min`;

        const diffMins = Math.floor(diffMs / (1000 * 60));    });

            

        // Considerar que está esperando muito se passou mais de 10 minutos    // Recarregar apenas se não há busca ativa

        return diffMins > 10;    const buscaAtiva = document.getElementById('searchInput').value;

    } catch (error) {    if (!buscaAtiva) {

        return false;        const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;

    }        carregarPedidos(filtroAtivo);

}    }

}

function displayError(message) {

    const container = document.getElementById('prontosList');// ===== MODAIS =====

    if (container) {function abrirModalEntregar(pedidoId) {

        container.innerHTML = `    pedidoSelecionado = pedidosProntos.find(p => p.id === pedidoId);

            <div class="col-12">    

                <div class="alert alert-danger text-center">    const detalhes = document.getElementById('detalhesPedidoEntregar');

                    <i class="fas fa-exclamation-triangle"></i>    detalhes.innerHTML = `

                    ${message}        <div style="background: #333; padding: 15px; border-radius: 8px; margin: 15px 0;">

                </div>            <h4 style="color: #fca311; margin-bottom: 10px;">${pedidoSelecionado.numeroPedido} - Mesa ${pedidoSelecionado.numeroMesa}</h4>

            </div>            <div style="margin-bottom: 10px;">

        `;                ${pedidoSelecionado.itens.map(item => `

    }                    <div style="display: flex; justify-content: space-between; margin: 5px 0;">

}                        <span>${item.nome}</span>

                        <span style="color: #fca311;">${item.quantidade}x</span>

function showSuccessMessage(message) {                    </div>

    // Criar toast de sucesso                `).join('')}

    const toastHtml = `            </div>

        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">            <p style="color: #cccccc; font-size: 0.9rem; margin: 0;">

            <div class="d-flex">                <i class="fas fa-clock"></i> Pronto há ${pedidoSelecionado.tempoEsperando}

                <div class="toast-body">            </p>

                    <i class="fas fa-check-circle"></i>        </div>

                    ${message}    `;

                </div>    

                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>    document.getElementById('modalConfirmarEntrega').style.display = 'block';

            </div>}

        </div>

    `;function abrirModalDescartar(pedidoId) {

        pedidoSelecionado = pedidosProntos.find(p => p.id === pedidoId);

    // Verificar se existe container de toasts    

    let toastContainer = document.getElementById('toast-container');    const detalhes = document.getElementById('detalhesPedidoDescartar');

    if (!toastContainer) {    detalhes.innerHTML = `

        toastContainer = document.createElement('div');        <div style="background: #333; padding: 15px; border-radius: 8px; margin: 15px 0;">

        toastContainer.id = 'toast-container';            <h4 style="color: #fca311; margin-bottom: 10px;">${pedidoSelecionado.numeroPedido} - Mesa ${pedidoSelecionado.numeroMesa}</h4>

        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';            <div style="margin-bottom: 10px;">

        document.body.appendChild(toastContainer);                ${pedidoSelecionado.itens.map(item => `

    }                    <div style="display: flex; justify-content: space-between; margin: 5px 0;">

                            <span>${item.nome}</span>

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);                        <span style="color: #fca311;">${item.quantidade}x</span>

                        </div>

    // Inicializar e mostrar toast                `).join('')}

    const toastElement = toastContainer.lastElementChild;            </div>

    const toast = new bootstrap.Toast(toastElement);            <p style="color: #cccccc; font-size: 0.9rem; margin: 0;">

    toast.show();                <i class="fas fa-clock"></i> Pronto há ${pedidoSelecionado.tempoEsperando}

                </p>

    // Remover toast após ser escondido        </div>

    toastElement.addEventListener('hidden.bs.toast', () => {    `;

        toastElement.remove();    

    });    // Resetar o select de motivo

}    document.getElementById('motivoDescarte').value = '';
    
    document.getElementById('modalDescartarPedido').style.display = 'block';
}

function fecharModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
    pedidoSelecionado = null;
}

// ===== AÇÕES DOS PEDIDOS =====
function confirmarEntregarPedido() {
    if (!pedidoSelecionado) return;
    
    // Remover da lista de pedidos prontos
    pedidosProntos = pedidosProntos.filter(p => p.id !== pedidoSelecionado.id);
    
    // Simular confirmação de entrega no sistema
    console.log(`Pedido ${pedidoSelecionado.numeroPedido} entregue e removido do sistema`);
    
    // Atualizar interface
    carregarPedidos();
    fecharModal('modalConfirmarEntrega');
    
    // Mostrar notificação
    mostrarNotificacao('Pedido entregue com sucesso!', 'success');
}

function confirmarDescartarPedido() {
    if (!pedidoSelecionado) return;
    
    const motivo = document.getElementById('motivoDescarte').value;
    
    if (!motivo) {
        mostrarNotificacao('Por favor, selecione um motivo para o descarte.', 'error');
        return;
    }
    
    // Remover da lista de pedidos prontos
    pedidosProntos = pedidosProntos.filter(p => p.id !== pedidoSelecionado.id);
    
    // Simular remoção completa do banco de dados
    console.log(`Pedido ${pedidoSelecionado.numeroPedido} descartado completamente`);
    console.log(`Motivo: ${motivo}`);
    
    // Atualizar interface
    carregarPedidos();
    fecharModal('modalDescartarPedido');
    
    // Mostrar notificação
    const motivoTexto = document.querySelector(`#motivoDescarte option[value="${motivo}"]`).textContent;
    mostrarNotificacao(`Pedido descartado: ${motivoTexto}`, 'info');
}

// ===== UTILITÁRIOS =====
function mostrarNotificacao(mensagem, tipo = 'info') {
    // Criar elemento de notificação
    const notificacao = document.createElement('div');
    notificacao.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 8px;
        color: white;
        font-weight: 600;
        z-index: 3000;
        opacity: 0;
        transform: translateX(100px);
        transition: all 0.3s ease;
        max-width: 300px;
    `;
    
    // Definir cor baseada no tipo
    switch(tipo) {
        case 'success':
            notificacao.style.background = 'linear-gradient(135deg, #28a745, #20c997)';
            break;
        case 'error':
            notificacao.style.background = 'linear-gradient(135deg, #dc3545, #c82333)';
            break;
        case 'info':
        default:
            notificacao.style.background = 'linear-gradient(135deg, #fca311, #ff9d01)';
            notificacao.style.color = '#1a1a1a';
    }
    
    notificacao.textContent = mensagem;
    document.body.appendChild(notificacao);
    
    // Animar entrada
    setTimeout(() => {
        notificacao.style.opacity = '1';
        notificacao.style.transform = 'translateX(0)';
    }, 100);
    
    // Remover após 4 segundos
    setTimeout(() => {
        notificacao.style.opacity = '0';
        notificacao.style.transform = 'translateX(100px)';
        setTimeout(() => document.body.removeChild(notificacao), 300);
    }, 4000);
}

// ===== EVENT LISTENERS GLOBAIS =====
// Fechar modal ao clicar fora
window.addEventListener('click', function(event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
            pedidoSelecionado = null;
        }
    });
});

// Fechar modais com ESC
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const modalsAbertos = document.querySelectorAll('.modal[style*="display: block"]');
        modalsAbertos.forEach(modal => {
            modal.style.display = 'none';
        });
        pedidoSelecionado = null;
    }
});

// Validação do select de motivo
document.addEventListener('DOMContentLoaded', function() {
    const selectMotivo = document.getElementById('motivoDescarte');
    if (selectMotivo) {
        selectMotivo.addEventListener('change', function() {
            const btnDescartar = document.querySelector('#modalDescartarPedido .btn-danger');
            if (this.value) {
                btnDescartar.style.opacity = '1';
                btnDescartar.style.pointerEvents = 'auto';
            } else {
                btnDescartar.style.opacity = '0.6';
                btnDescartar.style.pointerEvents = 'none';
            }
        });
    }
});