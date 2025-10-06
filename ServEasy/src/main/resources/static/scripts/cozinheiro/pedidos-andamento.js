document.addEventListener('DOMContentLoaded', async function() {document.addEventListener('DOMContentLoaded', async function() {document.addEventListener('DOMContentLoaded', async function() {document.addEventListener('DOMContentLoaded', async function() {// ===== DADOS SIMULADOS =====

    const container = document.getElementById('andamentoList');

        const container = document.getElementById('andamentoList');

    if (container) {

        await loadPedidosAndamento();        const andamentoListContainer = document.getElementById('andamentoList');

        setInterval(loadPedidosAndamento, 30000);

    }    if (container) {

});

        await loadPedidosAndamento();    const andamentoListContainer = document.getElementById('andamentoList');let pedidosAndamento = [

async function loadPedidosAndamento() {

    try {        setInterval(loadPedidosAndamento, 30000);

        const response = await fetch('/api/orders?status=PREPARING');

            }    if (andamentoListContainer) {

        if (!response.ok) {

            throw new Error('HTTP error! status: ' + response.status);});

        }

                await loadPedidosAndamento();    {

        const orders = await response.json();

        displayPedidosAndamento(orders);async function loadPedidosAndamento() {

        

    } catch (error) {    try {        

        console.error('Erro ao carregar pedidos em andamento:', error);

        displayError('Erro ao carregar pedidos em andamento');        const response = await fetch('/api/orders?status=PREPARING');

    }

}                // Atualizar automaticamente a cada 30 segundos    if (andamentoListContainer) {        id: 1,



function displayPedidosAndamento(orders) {        if (!response.ok) {

    const container = document.getElementById('andamentoList');

                throw new Error('HTTP error! status: ' + response.status);        setInterval(loadPedidosAndamento, 30000);

    if (!container) {

        return;        }

    }

                }        await loadPedidosAndamento();        numeroMesa: 2,

    if (!orders || orders.length === 0) {

        container.innerHTML = '<div class="col-12"><div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> Nenhum pedido em andamento no momento</div></div>';        const orders = await response.json();

        return;

    }        displayPedidosAndamento(orders);});

    

    container.innerHTML = orders.map(function(order) {        

        const isUrgent = getPreparingMinutes(order.orderDate) > 20;

            } catch (error) {                numeroPedido: "PED-004",

        return `

            <div class="col-md-6 col-lg-4">        console.error('Erro ao carregar pedidos em andamento:', error);

                <div class="card mb-3 ${isUrgent ? 'border-warning' : ''}">

                    <div class="card-header d-flex justify-content-between align-items-center">        displayError('Erro ao carregar pedidos em andamento');async function loadPedidosAndamento() {

                        <h5 class="mb-0">Pedido #${order.id}</h5>

                        <div>    }

                            <span class="badge bg-warning">Mesa ${order.table ? order.table.tableNumber : 'N/A'}</span>

                            ${isUrgent ? '<span class="badge bg-danger ms-1">URGENTE</span>' : ''}}    try {        // Atualizar automaticamente a cada 30 segundos        itens: [

                        </div>

                    </div>

                    <div class="card-body">

                        <div class="mb-2">function displayPedidosAndamento(orders) {        const response = await fetch('/api/orders?status=PREPARING');

                            <small class="text-muted">

                                <i class="fas fa-clock"></i>    const container = document.getElementById('andamentoList');

                                Preparando há ${getPreparingTime(order.orderDate)}

                            </small>                    setInterval(loadPedidosAndamento, 30000);            { nome: "Pizza Quattro Stagioni", quantidade: 1 },

                        </div>

                            if (!container) {

                        <div class="order-items mb-3">

                            <h6>Itens do Pedido:</h6>        return;        if (!response.ok) {

                            ${order.items ? order.items.map(function(item) {

                                return `    }

                                    <div class="item-row d-flex justify-content-between align-items-center mb-2">

                                        <div>                throw new Error(`HTTP error! status: ${response.status}`);    }            { nome: "Água com Gás", quantidade: 1 }

                                            <span class="fw-bold">${item.quantity}x</span>

                                            <span>${item.menuItem ? item.menuItem.name : 'Item não encontrado'}</span>    if (!orders || orders.length === 0) {

                                        </div>

                                        <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>        container.innerHTML = '<div class="col-12"><div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> Nenhum pedido em andamento no momento</div></div>';        }

                                    </div>

                                `;        return;

                            }).join('') : '<p class="text-muted">Sem itens</p>'}

                        </div>    }        });        ],

                        

                        <div class="progress mb-3">    

                            <div class="progress-bar bg-warning" role="progressbar" 

                                 style="width: ${getProgressPercentage(order.orderDate)}%"     container.innerHTML = orders.map(order => {        const orders = await response.json();

                                 aria-valuenow="${getProgressPercentage(order.orderDate)}" 

                                 aria-valuemin="0" aria-valuemax="100">        const isUrgent = getPreparingMinutes(order.orderDate) > 20;

                            </div>

                        </div>                displayPedidosAndamento(orders);        tempoPreparando: "8 min",

                        

                        <div class="d-flex justify-content-between align-items-center">        return `

                            <div class="total">

                                <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>            <div class="col-md-6 col-lg-4">        

                            </div>

                            <button class="btn btn-success btn-sm"                 <div class="card mb-3 ${isUrgent ? 'border-warning' : ''}">

                                    onclick="finalizarPedido(${order.id})">

                                <i class="fas fa-check"></i>                    <div class="card-header d-flex justify-content-between align-items-center">    } catch (error) {async function loadPedidosAndamento() {        ingredientes: [

                                Finalizar

                            </button>                        <h5 class="mb-0">Pedido #${order.id}</h5>

                        </div>

                    </div>                        <div>        console.error('Erro ao carregar pedidos em andamento:', error);

                </div>

            </div>                            <span class="badge bg-warning">Mesa ${order.table?.tableNumber || 'N/A'}</span>

        `;

    }).join('');                            ${isUrgent ? '<span class="badge bg-danger ms-1">URGENTE</span>' : ''}        displayError('Erro ao carregar pedidos em andamento');    try {            { nome: "Massa de Pizza", quantidade: "1 unidade", usado: true },

}

                        </div>

async function finalizarPedido(orderId) {

    if (!orderId) {                    </div>    }

        alert('ID do pedido inválido');

        return;                    <div class="card-body">

    }

                            <div class="mb-2">}        const response = await fetch('/api/orders?status=PREPARING');            { nome: "Molho de Tomate", quantidade: "100ml", usado: true },

    try {

        const response = await fetch('/api/orders/' + orderId + '/status', {                            <small class="text-muted">

            method: 'PUT',

            headers: {                                <i class="fas fa-clock"></i>

                'Content-Type': 'application/json'

            },                                Preparando há ${getPreparingTime(order.orderDate)}

            body: JSON.stringify({ status: 'READY' })

        });                            </small>function displayPedidosAndamento(orders) {                    { nome: "Queijo Mussarela", quantidade: "150g", usado: true },

        

        if (!response.ok) {                        </div>

            throw new Error('HTTP error! status: ' + response.status);

        }                            const container = document.getElementById('andamentoList');

        

        await loadPedidosAndamento();                        <div class="order-items mb-3">

        showSuccess('Pedido finalizado com sucesso!');

                                    <h6>Itens do Pedido:</h6>            if (!response.ok) {            { nome: "Presunto", quantidade: "50g", usado: false },

    } catch (error) {

        console.error('Erro ao finalizar pedido:', error);                            ${order.items?.map(item => `

        alert('Erro ao finalizar pedido');

    }                                <div class="item-row d-flex justify-content-between align-items-center mb-2">    if (!container) {

}

                                    <div>

function getPreparingMinutes(orderDate) {

    if (!orderDate) return 0;                                        <span class="fw-bold">${item.quantity}x</span>        console.error('Container andamentoList não encontrado');            throw new Error(`HTTP error! status: ${response.status}`);            { nome: "Champignon", quantidade: "30g", usado: false }

    

    try {                                        <span>${item.menuItem?.name || 'Item não encontrado'}</span>

        const now = new Date();

        const order = new Date(orderDate);                                    </div>        return;

        const diffMs = now - order;

        return Math.floor(diffMs / (1000 * 60));                                    <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>

    } catch (error) {

        return 0;                                </div>    }        }        ],

    }

}                            `).join('') || '<p class="text-muted">Sem itens</p>'}



function getPreparingTime(orderDate) {                        </div>    

    const mins = getPreparingMinutes(orderDate);

                            

    if (mins < 1) return '< 1 min';

    if (mins < 60) return mins + ' min';                        <div class="progress mb-3">    if (!orders || orders.length === 0) {                urgente: false,

    

    const hours = Math.floor(mins / 60);                            <div class="progress-bar bg-warning" role="progressbar" 

    const remainingMins = mins % 60;

    return hours + 'h ' + remainingMins + 'min';                                 style="width: ${getProgressPercentage(order.orderDate)}%"         container.innerHTML = `

}

                                 aria-valuenow="${getProgressPercentage(order.orderDate)}" 

function getProgressPercentage(orderDate) {

    const mins = getPreparingMinutes(orderDate);                                 aria-valuemin="0" aria-valuemax="100">            <div class="col-12">        const orders = await response.json();        timestamp: new Date(Date.now() - 8 * 60000)

    const maxMins = 30; // 30 minutos é 100%

    const percentage = Math.min((mins / maxMins) * 100, 100);                            </div>

    return Math.round(percentage);

}                        </div>                <div class="alert alert-info text-center">



function displayError(message) {                        

    const container = document.getElementById('andamentoList');

    if (container) {                        <div class="d-flex justify-content-between align-items-center">                    <i class="fas fa-info-circle"></i>        displayPedidosAndamento(orders);    },

        container.innerHTML = '<div class="col-12"><div class="alert alert-danger text-center"><i class="fas fa-exclamation-triangle"></i> ' + message + '</div></div>';

    }                            <div class="total">

}

                                <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>                    Nenhum pedido em andamento no momento

function showSuccess(message) {

    const toast = document.createElement('div');                            </div>

    toast.className = 'toast align-items-center text-white bg-success border-0 show';

    toast.innerHTML = '<div class="d-flex"><div class="toast-body"><i class="fas fa-check-circle"></i> ' + message + '</div></div>';                            <button class="btn btn-success btn-sm"                 </div>            {

    toast.style.position = 'fixed';

    toast.style.top = '20px';                                    onclick="finalizarPedido(${order.id})">

    toast.style.right = '20px';

    toast.style.zIndex = '9999';                                <i class="fas fa-check"></i>            </div>

    document.body.appendChild(toast);

    setTimeout(function() { toast.remove(); }, 3000);                                Finalizar

}
                            </button>        `;    } catch (error) {        id: 2,

                        </div>

                    </div>        return;

                </div>

            </div>    }        console.error('Erro ao carregar pedidos em andamento:', error);        numeroMesa: 7,

        `;

    }).join('');    

}

    container.innerHTML = orders.map(order => `        displayError('Erro ao carregar pedidos em andamento');        numeroPedido: "PED-005",

async function finalizarPedido(orderId) {

    if (!orderId) {        <div class="col-md-6 col-lg-4">

        alert('ID do pedido inválido');

        return;            <div class="card mb-3 ${isUrgent(order) ? 'border-warning' : ''}">    }        itens: [

    }

                    <div class="card-header d-flex justify-content-between align-items-center">

    try {

        const response = await fetch('/api/orders/' + orderId + '/status', {                    <h5 class="mb-0">Pedido #${order.id}</h5>}            { nome: "Hambúrguer Bacon", quantidade: 2 },

            method: 'PUT',

            headers: {                    <div>

                'Content-Type': 'application/json'

            },                        <span class="badge bg-warning">Mesa ${order.table?.tableNumber || 'N/A'}</span>            { nome: "Onion Rings", quantidade: 1 }

            body: JSON.stringify({ status: 'READY' })

        });                        ${isUrgent(order) ? '<span class="badge bg-danger ms-1">URGENTE</span>' : ''}

        

        if (!response.ok) {                    </div>function displayPedidosAndamento(orders) {        ],

            throw new Error('HTTP error! status: ' + response.status);

        }                </div>

        

        await loadPedidosAndamento();                <div class="card-body">    const container = document.getElementById('andamentoList');        tempoPreparando: "15 min",

        showSuccess('Pedido finalizado com sucesso!');

                            <div class="mb-2">

    } catch (error) {

        console.error('Erro ao finalizar pedido:', error);                        <small class="text-muted">            ingredientes: [

        alert('Erro ao finalizar pedido');

    }                            <i class="fas fa-clock"></i>

}

                            Preparando há ${getPreparingTime(order.orderDate)}    if (!container) {            { nome: "Pão de Hambúrguer", quantidade: "2 unidades", usado: true },

function getPreparingMinutes(orderDate) {

    if (!orderDate) return 0;                        </small>

    

    try {                    </div>        console.error('Container andamentoList não encontrado');            { nome: "Carne Bovina", quantidade: "200g", usado: true },

        const now = new Date();

        const order = new Date(orderDate);                    

        const diffMs = now - order;

        return Math.floor(diffMs / (1000 * 60));                    <div class="order-items mb-3">        return;            { nome: "Bacon", quantidade: "100g", usado: true },

    } catch (error) {

        return 0;                        <h6>Itens do Pedido:</h6>

    }

}                        ${order.items?.map(item => `    }            { nome: "Queijo Cheddar", quantidade: "80g", usado: false },



function getPreparingTime(orderDate) {                            <div class="item-row d-flex justify-content-between align-items-center mb-2">

    const mins = getPreparingMinutes(orderDate);

                                    <div>                { nome: "Cebola", quantidade: "2 unidades", usado: false }

    if (mins < 1) return '< 1 min';

    if (mins < 60) return mins + ' min';                                    <span class="fw-bold">${item.quantity}x</span>

    

    const hours = Math.floor(mins / 60);                                    <span>${item.menuItem?.name || 'Item não encontrado'}</span>    if (!orders || orders.length === 0) {        ],

    const remainingMins = mins % 60;

    return hours + 'h ' + remainingMins + 'min';                                </div>

}

                                <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>        container.innerHTML = `        urgente: true,

function getProgressPercentage(orderDate) {

    const mins = getPreparingMinutes(orderDate);                            </div>

    const maxMins = 30; // 30 minutos é 100%

    const percentage = Math.min((mins / maxMins) * 100, 100);                        `).join('') || '<p class="text-muted">Sem itens</p>'}            <div class="col-12">        timestamp: new Date(Date.now() - 15 * 60000)

    return Math.round(percentage);

}                    </div>



function displayError(message) {                                    <div class="alert alert-info text-center">    },

    const container = document.getElementById('andamentoList');

    if (container) {                    <div class="progress mb-3">

        container.innerHTML = '<div class="col-12"><div class="alert alert-danger text-center"><i class="fas fa-exclamation-triangle"></i> ' + message + '</div></div>';

    }                        <div class="progress-bar bg-warning" role="progressbar"                     <i class="fas fa-info-circle"></i>    {

}

                             style="width: ${getProgressPercentage(order.orderDate)}%" 

function showSuccess(message) {

    const toast = document.createElement('div');                             aria-valuenow="${getProgressPercentage(order.orderDate)}"                     Nenhum pedido em andamento no momento        id: 3,

    toast.className = 'toast align-items-center text-white bg-success border-0 show';

    toast.innerHTML = '<div class="d-flex"><div class="toast-body"><i class="fas fa-check-circle"></i> ' + message + '</div></div>';                             aria-valuemin="0" aria-valuemax="100">

    toast.style.position = 'fixed';

    toast.style.top = '20px';                        </div>                </div>        numeroMesa: 4,

    toast.style.right = '20px';

    toast.style.zIndex = '9999';                    </div>

    document.body.appendChild(toast);

    setTimeout(function() { toast.remove(); }, 3000);                                </div>        numeroPedido: "PED-006",

}
                    <div class="d-flex justify-content-between align-items-center">

                        <div class="total">        `;        itens: [

                            <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>

                        </div>        return;            { nome: "Risotto de Camarão", quantidade: 1 }

                        <button class="btn btn-success btn-sm" 

                                onclick="finalizarPedido(${order.id})">    }        ],

                            <i class="fas fa-check"></i>

                            Finalizar            tempoPreparando: "12 min",

                        </button>

                    </div>    container.innerHTML = orders.map(order => `        ingredientes: [

                </div>

            </div>        <div class="col-md-6 col-lg-4">            { nome: "Arroz Arbório", quantidade: "200g", usado: true },

        </div>

    `).join('');            <div class="card mb-3 ${isUrgent(order) ? 'border-warning' : ''}">            { nome: "Camarão", quantidade: "150g", usado: true },

}

                <div class="card-header d-flex justify-content-between align-items-center">            { nome: "Caldo de Peixe", quantidade: "500ml", usado: false },

async function finalizarPedido(orderId) {

    if (!orderId) {                    <h5 class="mb-0">Pedido #${order.id}</h5>            { nome: "Vinho Branco", quantidade: "50ml", usado: false }

        alert('ID do pedido inválido');

        return;                    <div>        ],

    }

                            <span class="badge bg-warning">Mesa ${order.table?.tableNumber || 'N/A'}</span>        urgente: false,

    try {

        const response = await fetch(`/api/orders/${orderId}/status`, {                        ${isUrgent(order) ? '<span class="badge bg-danger ms-1">URGENTE</span>' : ''}        timestamp: new Date(Date.now() - 12 * 60000)

            method: 'PUT',

            headers: {                    </div>    }

                'Content-Type': 'application/json'

            },                </div>];

            body: JSON.stringify({ status: 'READY' })

        });                <div class="card-body">

        

        if (!response.ok) {                    <div class="mb-2">let pedidoSelecionado = null;

            throw new Error(`HTTP error! status: ${response.status}`);

        }                        <small class="text-muted">

        

        // Recarregar lista de pedidos                            <i class="fas fa-clock"></i>// ===== INICIALIZAÇÃO =====

        await loadPedidosAndamento();

                                    Preparando há ${getPreparingTime(order.orderDate)}document.addEventListener('DOMContentLoaded', function() {

        // Mostrar confirmação

        showSuccessMessage('Pedido finalizado com sucesso!');                        </small>    carregarPedidos();

        

    } catch (error) {                    </div>    configurarFiltros();

        console.error('Erro ao finalizar pedido:', error);

        alert('Erro ao finalizar pedido');                        configurarBusca();

    }

}                    <div class="order-items mb-3">    atualizarTempos();



function getPreparingTime(orderDate) {                        <h6>Itens do Pedido:</h6>    

    if (!orderDate) return '0 min';

                            ${order.items?.map(item => `    // Atualizar tempos a cada minuto

    try {

        const now = new Date();                            <div class="item-row d-flex justify-content-between align-items-center mb-2">    setInterval(atualizarTempos, 60000);

        const order = new Date(orderDate);

        const diffMs = now - order;                                <div>});

        const diffMins = Math.floor(diffMs / (1000 * 60));

                                            <span class="fw-bold">${item.quantity}x</span>

        if (diffMins < 1) return '< 1 min';

        if (diffMins < 60) return `${diffMins} min`;                                    <span>${item.menuItem?.name || 'Item não encontrado'}</span>// ===== CARREGAR E EXIBIR PEDIDOS =====

        

        const hours = Math.floor(diffMins / 60);                                </div>function carregarPedidos(filtro = 'todos', busca = '') {

        const mins = diffMins % 60;

        return `${hours}h ${mins}min`;                                <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>    const grid = document.getElementById('pedidosGrid');

    } catch (error) {

        return '0 min';                            </div>    const emptyMessage = document.getElementById('emptyMessage');

    }

}                        `).join('') || '<p class="text-muted">Sem itens</p>'}    



function getProgressPercentage(orderDate) {                    </div>    let pedidosFiltrados = [...pedidosAndamento];

    if (!orderDate) return 0;

                            

    try {

        const now = new Date();                    <div class="progress mb-3">    // Aplicar filtros

        const order = new Date(orderDate);

        const diffMs = now - order;                        <div class="progress-bar bg-warning" role="progressbar"     if (filtro === 'urgente') {

        const diffMins = Math.floor(diffMs / (1000 * 60));

                                     style="width: ${getProgressPercentage(order.orderDate)}%"         pedidosFiltrados = pedidosFiltrados.filter(p => p.urgente);

        // Assumindo que 30 minutos é 100% do tempo esperado

        const maxMins = 30;                             aria-valuenow="${getProgressPercentage(order.orderDate)}"     } else if (filtro === 'tempo') {

        const percentage = Math.min((diffMins / maxMins) * 100, 100);

                                     aria-valuemin="0" aria-valuemax="100">        pedidosFiltrados.sort((a, b) => b.timestamp - a.timestamp);

        return Math.round(percentage);

    } catch (error) {                        </div>    }

        return 0;

    }                    </div>    

}

                        // Aplicar busca

function isUrgent(order) {

    if (!order.orderDate) return false;                    <div class="d-flex justify-content-between align-items-center">    if (busca) {

    

    try {                        <div class="total">        pedidosFiltrados = pedidosFiltrados.filter(pedido => 

        const now = new Date();

        const orderTime = new Date(order.orderDate);                            <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>            pedido.numeroPedido.toLowerCase().includes(busca.toLowerCase()) ||

        const diffMs = now - orderTime;

        const diffMins = Math.floor(diffMs / (1000 * 60));                        </div>            pedido.numeroMesa.toString().includes(busca) ||

        

        // Considerar urgente se está há mais de 20 minutos preparando                        <button class="btn btn-success btn-sm"             pedido.itens.some(item => item.nome.toLowerCase().includes(busca.toLowerCase()))

        return diffMins > 20;

    } catch (error) {                                onclick="finalizarPedido(${order.id})">        );

        return false;

    }                            <i class="fas fa-check"></i>    }

}

                            Finalizar    

function displayError(message) {

    const container = document.getElementById('andamentoList');                        </button>    if (pedidosFiltrados.length === 0) {

    if (container) {

        container.innerHTML = `                    </div>        grid.innerHTML = '';

            <div class="col-12">

                <div class="alert alert-danger text-center">                </div>        emptyMessage.classList.remove('hidden');

                    <i class="fas fa-exclamation-triangle"></i>

                    ${message}            </div>        return;

                </div>

            </div>        </div>    }

        `;

    }    `).join('');    

}

}    emptyMessage.classList.add('hidden');

function showSuccessMessage(message) {

    // Criar toast de sucesso    grid.innerHTML = pedidosFiltrados.map(pedido => criarCardPedido(pedido)).join('');

    const toastHtml = `

        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">async function finalizarPedido(orderId) {}

            <div class="d-flex">

                <div class="toast-body">    if (!orderId) {

                    <i class="fas fa-check-circle"></i>

                    ${message}        alert('ID do pedido inválido');function criarCardPedido(pedido) {

                </div>

                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>        return;    const urgenteIcon = pedido.urgente ? '<i class="fas fa-exclamation-triangle"></i>' : '';

            </div>

        </div>    }    

    `;

            return `

    // Verificar se existe container de toasts

    let toastContainer = document.getElementById('toast-container');    try {        <div class="pedido-card fade-in" data-id="${pedido.id}">

    if (!toastContainer) {

        toastContainer = document.createElement('div');        const response = await fetch(`/api/orders/${orderId}/status`, {            <div class="pedido-header">

        toastContainer.id = 'toast-container';

        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';            method: 'PUT',                <div class="pedido-numero">${pedido.numeroPedido}</div>

        document.body.appendChild(toastContainer);

    }            headers: {                <div class="pedido-mesa">Mesa ${pedido.numeroMesa}</div>

    

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);                'Content-Type': 'application/json'                <div class="pedido-status status-andamento">

    

    // Inicializar e mostrar toast            },                    ${urgenteIcon} Em Andamento

    const toastElement = toastContainer.lastElementChild;

    const toast = new bootstrap.Toast(toastElement);            body: JSON.stringify({ status: 'READY' })                </div>

    toast.show();

            });            </div>

    // Remover toast após ser escondido

    toastElement.addEventListener('hidden.bs.toast', () => {                    

        toastElement.remove();

    });        if (!response.ok) {            <div class="pedido-itens">

}
            throw new Error(`HTTP error! status: ${response.status}`);                <h4>Itens do Pedido:</h4>

        }                ${pedido.itens.map(item => `

                            <div class="item-pedido">

        // Recarregar lista de pedidos                        <span class="item-nome">${item.nome}</span>

        await loadPedidosAndamento();                        <span class="item-quantidade">${item.quantidade}x</span>

                            </div>

        // Mostrar confirmação                `).join('')}

        showSuccessMessage('Pedido finalizado com sucesso!');            </div>

                    

    } catch (error) {            <div class="pedido-tempo">

        console.error('Erro ao finalizar pedido:', error);                <i class="fas fa-clock"></i> Preparando há ${pedido.tempoPreparando}

        alert('Erro ao finalizar pedido');            </div>

    }            

}            <div class="pedido-acoes">

                <button class="btn btn-success btn-sm" onclick="abrirModalFinalizar(${pedido.id})">

function getPreparingTime(orderDate) {                    <i class="fas fa-check"></i> Pronto

    if (!orderDate) return '0 min';                </button>

                    <button class="btn btn-danger btn-sm" onclick="abrirModalCancelar(${pedido.id})">

    try {                    <i class="fas fa-times"></i> Cancelar

        const now = new Date();                </button>

        const order = new Date(orderDate);            </div>

        const diffMs = now - order;        </div>

        const diffMins = Math.floor(diffMs / (1000 * 60));    `;

        }

        if (diffMins < 1) return '< 1 min';

        if (diffMins < 60) return `${diffMins} min`;// ===== FILTROS E BUSCA =====

        function configurarFiltros() {

        const hours = Math.floor(diffMins / 60);    const filterButtons = document.querySelectorAll('.filter-btn');

        const mins = diffMins % 60;    

        return `${hours}h ${mins}min`;    filterButtons.forEach(btn => {

    } catch (error) {        btn.addEventListener('click', function() {

        return '0 min';            // Remover active de todos

    }            filterButtons.forEach(b => b.classList.remove('active'));

}            // Adicionar active no clicado

            this.classList.add('active');

function getProgressPercentage(orderDate) {            

    if (!orderDate) return 0;            const filtro = this.dataset.filter;

                const busca = document.getElementById('searchInput').value;

    try {            carregarPedidos(filtro, busca);

        const now = new Date();        });

        const order = new Date(orderDate);    });

        const diffMs = now - order;}

        const diffMins = Math.floor(diffMs / (1000 * 60));

        function configurarBusca() {

        // Assumindo que 30 minutos é 100% do tempo esperado    const searchInput = document.getElementById('searchInput');

        const maxMins = 30;    

        const percentage = Math.min((diffMins / maxMins) * 100, 100);    searchInput.addEventListener('input', function() {

                const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;

        return Math.round(percentage);        carregarPedidos(filtroAtivo, this.value);

    } catch (error) {    });

        return 0;}

    }

}// ===== ATUALIZAR TEMPOS =====

function atualizarTempos() {

function isUrgent(order) {    pedidosAndamento.forEach(pedido => {

    if (!order.orderDate) return false;        const minutosPreparando = Math.floor((Date.now() - pedido.timestamp) / 60000);

            pedido.tempoPreparando = `${minutosPreparando} min`;

    try {        

        const now = new Date();        // Marcar como urgente se passou de 20 minutos

        const orderTime = new Date(order.orderDate);        if (minutosPreparando > 20) {

        const diffMs = now - orderTime;            pedido.urgente = true;

        const diffMins = Math.floor(diffMs / (1000 * 60));        }

            });

        // Considerar urgente se está há mais de 20 minutos preparando    

        return diffMins > 20;    // Recarregar apenas se não há busca ativa

    } catch (error) {    const buscaAtiva = document.getElementById('searchInput').value;

        return false;    if (!buscaAtiva) {

    }        const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;

}        carregarPedidos(filtroAtivo);

    }

function displayError(message) {}

    const container = document.getElementById('andamentoList');

    if (container) {// ===== MODAIS =====

        container.innerHTML = `function abrirModalFinalizar(pedidoId) {

            <div class="col-12">    pedidoSelecionado = pedidosAndamento.find(p => p.id === pedidoId);

                <div class="alert alert-danger text-center">    

                    <i class="fas fa-exclamation-triangle"></i>    const detalhes = document.getElementById('detalhesPedidoFinalizar');

                    ${message}    detalhes.innerHTML = `

                </div>        <div style="background: #333; padding: 15px; border-radius: 8px; margin: 15px 0;">

            </div>            <h4 style="color: #fca311; margin-bottom: 10px;">${pedidoSelecionado.numeroPedido} - Mesa ${pedidoSelecionado.numeroMesa}</h4>

        `;            <div style="margin-bottom: 10px;">

    }                ${pedidoSelecionado.itens.map(item => `

}                    <div style="display: flex; justify-content: space-between; margin: 5px 0;">

                        <span>${item.nome}</span>

function showSuccessMessage(message) {                        <span style="color: #fca311;">${item.quantidade}x</span>

    // Criar toast de sucesso                    </div>

    const toastHtml = `                `).join('')}

        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">            </div>

            <div class="d-flex">            <p style="color: #cccccc; font-size: 0.9rem; margin: 0;">

                <div class="toast-body">                <i class="fas fa-clock"></i> Em preparo há ${pedidoSelecionado.tempoPreparando}

                    <i class="fas fa-check-circle"></i>            </p>

                    ${message}        </div>

                </div>    `;

                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>    

            </div>    document.getElementById('modalFinalizarPedido').style.display = 'block';

        </div>}

    `;

    function abrirModalCancelar(pedidoId) {

    // Verificar se existe container de toasts    pedidoSelecionado = pedidosAndamento.find(p => p.id === pedidoId);

    let toastContainer = document.getElementById('toast-container');    

    if (!toastContainer) {    const detalhes = document.getElementById('detalhesPedidoCancelar');

        toastContainer = document.createElement('div');    detalhes.innerHTML = `

        toastContainer.id = 'toast-container';        <div style="background: #333; padding: 15px; border-radius: 8px; margin: 15px 0;">

        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';            <h4 style="color: #fca311; margin-bottom: 10px;">${pedidoSelecionado.numeroPedido} - Mesa ${pedidoSelecionado.numeroMesa}</h4>

        document.body.appendChild(toastContainer);            <div style="margin-bottom: 10px;">

    }                ${pedidoSelecionado.itens.map(item => `

                        <div style="display: flex; justify-content: space-between; margin: 5px 0;">

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);                        <span>${item.nome}</span>

                            <span style="color: #fca311;">${item.quantidade}x</span>

    // Inicializar e mostrar toast                    </div>

    const toastElement = toastContainer.lastElementChild;                `).join('')}

    const toast = new bootstrap.Toast(toastElement);            </div>

    toast.show();        </div>

        `;

    // Remover toast após ser escondido    

    toastElement.addEventListener('hidden.bs.toast', () => {    // Carregar lista de ingredientes

        toastElement.remove();    carregarIngredientes();

    });    

}    document.getElementById('modalCancelarPedido').style.display = 'block';
}

function carregarIngredientes() {
    const lista = document.getElementById('ingredientesList');
    
    lista.innerHTML = pedidoSelecionado.ingredientes.map((ingrediente, index) => `
        <div class="ingrediente-item">
            <div class="ingrediente-info">
                <div class="ingrediente-nome">${ingrediente.nome}</div>
                <div class="ingrediente-quantidade">${ingrediente.quantidade}</div>
            </div>
            <div class="ingrediente-checkbox">
                <label style="display: flex; align-items: center; gap: 8px; color: #cccccc; cursor: pointer;">
                    <input type="checkbox" value="${index}" ${ingrediente.usado ? 'checked' : ''}>
                    Descartar
                </label>
            </div>
        </div>
    `).join('');
}

function fecharModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
    pedidoSelecionado = null;
}

// ===== AÇÕES DOS PEDIDOS =====
function confirmarFinalizarPedido() {
    if (!pedidoSelecionado) return;
    
    // Remover da lista de pedidos em andamento
    pedidosAndamento = pedidosAndamento.filter(p => p.id !== pedidoSelecionado.id);
    
    // Simular envio para "Pedidos Prontos"
    console.log(`Pedido ${pedidoSelecionado.numeroPedido} finalizado e movido para Prontos`);
    
    // Atualizar interface
    carregarPedidos();
    fecharModal('modalFinalizarPedido');
    
    // Mostrar notificação
    mostrarNotificacao('Pedido marcado como pronto!', 'success');
}

function confirmarCancelarPedido() {
    if (!pedidoSelecionado) return;
    
    // Obter ingredientes selecionados para descarte
    const checkboxes = document.querySelectorAll('#ingredientesList input[type="checkbox"]:checked');
    const ingredientesDescartados = [];
    
    checkboxes.forEach(checkbox => {
        const index = parseInt(checkbox.value);
        ingredientesDescartados.push(pedidoSelecionado.ingredientes[index]);
    });
    
    // Remover da lista de pedidos em andamento
    pedidosAndamento = pedidosAndamento.filter(p => p.id !== pedidoSelecionado.id);
    
    // Simular descarte de ingredientes do estoque
    console.log(`Pedido ${pedidoSelecionado.numeroPedido} cancelado`);
    console.log('Ingredientes descartados:', ingredientesDescartados);
    
    // Atualizar interface
    carregarPedidos();
    fecharModal('modalCancelarPedido');
    
    // Mostrar notificação
    const numDescartados = ingredientesDescartados.length;
    mostrarNotificacao(`Pedido cancelado. ${numDescartados} ingrediente(s) descartado(s).`, 'info');
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