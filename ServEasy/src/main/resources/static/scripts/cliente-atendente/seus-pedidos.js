// Estado da aplicação// Estado da aplicação// Estado da aplicação// Estado da aplicação

let mesaSelecionada = null;

let pedidos = [];let mesaSelecionada = null;

let filtroAtivo = 'todos';

let pedidos = [];let mesaSelecionada = null;let mesaSelecionada = null;

// Inicialização

document.addEventListener('DOMContentLoaded', function() {let filtroAtivo = 'todos';

    verificarMesaSalva();

    carregarPedidos();let pedidos = [];let pedidos = [];

    setupEventListeners();

    // Inicialização

    // Atualizar pedidos a cada 30 segundos

    setInterval(carregarPedidos, 30000);document.addEventListener('DOMContentLoaded', function() {let filtroAtivo = 'todos';let filtroAtivo = 'todos';

});

    verificarMesaSalva();

function verificarMesaSalva() {

    const mesaSalva = localStorage.getItem('mesaSelecionada');    carregarPedidos();

    if (mesaSalva) {

        mesaSelecionada = parseInt(mesaSalva);    setupEventListeners();

        mostrarMesaAtiva();

    }    // Inicialização// Inicialização

}

    // Atualizar pedidos a cada 30 segundos

function mostrarMesaAtiva() {

    const mesaAtiva = document.getElementById('mesaAtiva');    setInterval(carregarPedidos, 30000);document.addEventListener('DOMContentLoaded', function() {document.addEventListener('DOMContentLoaded', function() {

    const mesaNumero = document.getElementById('mesaNumero');

    });

    if (mesaSelecionada) {

        mesaNumero.textContent = 'Mesa ' + mesaSelecionada;    verificarMesaSalva();    verificarMesaSalva();

        mesaAtiva.style.display = 'flex';

    } else {function verificarMesaSalva() {

        mesaAtiva.style.display = 'none';

    }    const mesaSalva = localStorage.getItem('mesaSelecionada');    carregarPedidos();    carregarPedidos();

}

    if (mesaSalva) {

async function carregarPedidos() {

    try {        mesaSelecionada = parseInt(mesaSalva);    setupEventListeners();    setupEventListeners();

        let url = '/api/orders';

                mostrarMesaAtiva();

        // Se há mesa selecionada, filtrar por mesa

        if (mesaSelecionada) {    }        atualizarResumoFinanceiro();

            url += '?tableId=' + mesaSelecionada;

        }}

        

        const response = await fetch(url);    // Atualizar pedidos a cada 30 segundos});

        

        if (!response.ok) {function mostrarMesaAtiva() {

            throw new Error('HTTP error! status: ' + response.status);

        }    const mesaAtiva = document.getElementById('mesaAtiva');    setInterval(carregarPedidos, 30000);

        

        pedidos = await response.json();    const mesaNumero = document.getElementById('mesaNumero');

        exibirPedidos();

        atualizarResumoFinanceiro();    });function verificarMesaSalva() {

        

    } catch (error) {    if (mesaSelecionada) {

        console.error('Erro ao carregar pedidos:', error);

        mostrarErro('Erro ao carregar pedidos');        mesaNumero.textContent = `Mesa ${mesaSelecionada}`;    const mesaSalva = localStorage.getItem('mesaSelecionada');

    }

}        mesaAtiva.style.display = 'flex';



function exibirPedidos() {    } else {function verificarMesaSalva() {    if (mesaSalva) {

    const pedidosGrid = document.getElementById('pedidosGrid');

    const emptyMessage = document.getElementById('emptyMessage');        mesaAtiva.style.display = 'none';

    

    if (!pedidos || pedidos.length === 0) {    }    const mesaSalva = localStorage.getItem('mesaSelecionada');        mesaSelecionada = parseInt(mesaSalva);

        if (pedidosGrid) pedidosGrid.style.display = 'none';

        if (emptyMessage) emptyMessage.style.display = 'block';}

        return;

    }    if (mesaSalva) {        mostrarMesaAtiva();

    

    // Filtrar pedidos conforme o filtro ativoasync function carregarPedidos() {

    let pedidosFiltrados = pedidos;

        try {        mesaSelecionada = parseInt(mesaSalva);    }

    if (filtroAtivo !== 'todos') {

        pedidosFiltrados = pedidos.filter(function(pedido) {        let url = '/api/orders';

            const status = pedido.status ? pedido.status.toLowerCase() : '';

            switch (filtroAtivo) {                mostrarMesaAtiva();}

                case 'andamento':

                    return status === 'pending' || status === 'preparing';        // Se há mesa selecionada, filtrar por mesa

                case 'prontos':

                    return status === 'ready';        if (mesaSelecionada) {    }

                case 'entregues':

                    return status === 'delivered';            url += `?tableId=${mesaSelecionada}`;

                case 'cancelados':

                    return status === 'cancelled';        }}function mostrarMesaAtiva() {

                default:

                    return true;        

            }

        });        const response = await fetch(url);    const mesaAtiva = document.getElementById('mesaAtiva');

    }

            

    if (pedidosFiltrados.length === 0) {

        if (pedidosGrid) pedidosGrid.style.display = 'none';        if (!response.ok) {function mostrarMesaAtiva() {    const mesaNumero = document.getElementById('mesaNumero');

        if (emptyMessage) emptyMessage.style.display = 'block';

        return;            throw new Error(`HTTP error! status: ${response.status}`);

    }

            }    const mesaAtiva = document.getElementById('mesaAtiva');    

    if (pedidosGrid) pedidosGrid.style.display = 'grid';

    if (emptyMessage) emptyMessage.style.display = 'none';        

    

    if (pedidosGrid) {        pedidos = await response.json();    const mesaNumero = document.getElementById('mesaNumero');    if (mesaSelecionada) {

        pedidosGrid.innerHTML = pedidosFiltrados.map(function(pedido) {

            return `        exibirPedidos();

                <div class="pedido-card" data-status="${pedido.status ? pedido.status.toLowerCase() : ''}">

                    <div class="pedido-header">        atualizarResumoFinanceiro();            mesaNumero.textContent = `Mesa ${mesaSelecionada}`;

                        <div class="pedido-info">

                            <h3>Pedido #${pedido.id}</h3>        

                            <span class="mesa-badge">Mesa ${pedido.table ? pedido.table.tableNumber : 'N/A'}</span>

                        </div>    } catch (error) {    if (mesaSelecionada) {        mesaAtiva.style.display = 'flex';

                        <span class="status-badge status-${getStatusClass(pedido.status)}">

                            ${getStatusText(pedido.status)}        console.error('Erro ao carregar pedidos:', error);

                        </span>

                    </div>        mostrarErro('Erro ao carregar pedidos');        mesaNumero.textContent = `Mesa ${mesaSelecionada}`;    } else {

                    

                    <div class="pedido-detalhes">    }

                        <div class="timestamp">

                            <i class="fas fa-clock"></i>}        mesaAtiva.style.display = 'flex';        mesaAtiva.style.display = 'none';

                            ${formatDateTime(pedido.orderDate)}

                        </div>

                        

                        <div class="itens-lista">function exibirPedidos() {    } else {    }

                            ${pedido.items ? pedido.items.map(function(item) {

                                return `    const pedidosGrid = document.getElementById('pedidosGrid');

                                    <div class="item-linha">

                                        <span class="quantidade">${item.quantity}x</span>    const emptyMessage = document.getElementById('emptyMessage');        mesaAtiva.style.display = 'none';}

                                        <span class="nome">${item.menuItem ? item.menuItem.name : 'Item não encontrado'}</span>

                                        <span class="preco">R$ ${(item.price || 0).toFixed(2)}</span>    

                                    </div>

                                `;    if (!pedidos || pedidos.length === 0) {    }

                            }).join('') : '<p class="text-muted">Sem itens</p>'}

                        </div>        pedidosGrid.style.display = 'none';

                    </div>

                            emptyMessage.style.display = 'block';}function carregarPedidos() {

                    <div class="pedido-footer">

                        <div class="total">        return;

                            <strong>Total: R$ ${(pedido.totalAmount || 0).toFixed(2)}</strong>

                        </div>    }    // Carregar pedidos do localStorage (em uma aplicação real, viria de uma API)

                        <div class="acoes">

                            ${getActionButtons(pedido)}    

                        </div>

                    </div>    // Filtrar pedidos conforme o filtro ativoasync function carregarPedidos() {    const pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');

                </div>

            `;    let pedidosFiltrados = pedidos;

        }).join('');

    }        try {    

}

    if (filtroAtivo !== 'todos') {

function getStatusClass(status) {

    const statusMap = {        pedidosFiltrados = pedidos.filter(pedido => {        let url = '/api/orders';    // Filtrar pedidos pela mesa selecionada

        'PENDING': 'pendente',

        'PREPARING': 'preparando',            const status = pedido.status?.toLowerCase();

        'READY': 'pronto',

        'DELIVERED': 'entregue',            switch (filtroAtivo) {            if (mesaSelecionada) {

        'CANCELLED': 'cancelado'

    };                case 'andamento':

    return statusMap[status] || 'pendente';

}                    return status === 'pending' || status === 'preparing';        // Se há mesa selecionada, filtrar por mesa        pedidos = pedidosSalvos.filter(pedido => pedido.mesa === mesaSelecionada);



function getStatusText(status) {                case 'prontos':

    const statusMap = {

        'PENDING': 'Pendente',                    return status === 'ready';        if (mesaSelecionada) {    } else {

        'PREPARING': 'Preparando',

        'READY': 'Pronto',                case 'entregues':

        'DELIVERED': 'Entregue',

        'CANCELLED': 'Cancelado'                    return status === 'delivered';            url += `?tableId=${mesaSelecionada}`;        pedidos = pedidosSalvos;

    };

    return statusMap[status] || 'Pendente';                case 'cancelados':

}

                    return status === 'cancelled';        }    }

function getActionButtons(pedido) {

    const status = pedido.status;                default:

    

    if (status === 'PENDING') {                    return true;            

        return '<button class="btn-cancelar" onclick="cancelarPedido(' + pedido.id + ')"><i class="fas fa-times"></i> Cancelar</button>';

    } else if (status === 'READY') {            }

        return '<button class="btn-confirmar" onclick="confirmarRecebimento(' + pedido.id + ')"><i class="fas fa-check"></i> Recebi</button>';

    }        });        const response = await fetch(url);    exibirPedidos();

    

    return '';    }

}

            }

async function cancelarPedido(pedidoId) {

    if (!confirm('Tem certeza que deseja cancelar este pedido?')) {    if (pedidosFiltrados.length === 0) {

        return;

    }        pedidosGrid.style.display = 'none';        if (!response.ok) {

    

    try {        emptyMessage.style.display = 'block';

        const response = await fetch('/api/orders/' + pedidoId + '/status', {

            method: 'PUT',        return;            throw new Error(`HTTP error! status: ${response.status}`);function exibirPedidos() {

            headers: {

                'Content-Type': 'application/json'    }

            },

            body: JSON.stringify({ status: 'CANCELLED' })            }    const pedidosGrid = document.getElementById('pedidosGrid');

        });

            pedidosGrid.style.display = 'grid';

        if (!response.ok) {

            throw new Error('HTTP error! status: ' + response.status);    emptyMessage.style.display = 'none';            const emptyMessage = document.getElementById('emptyMessage');

        }

            

        await carregarPedidos();

        mostrarSucesso('Pedido cancelado com sucesso!');    pedidosGrid.innerHTML = pedidosFiltrados.map(pedido => `        pedidos = await response.json();    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

        

    } catch (error) {        <div class="pedido-card" data-status="${pedido.status?.toLowerCase()}">

        console.error('Erro ao cancelar pedido:', error);

        alert('Erro ao cancelar pedido');            <div class="pedido-header">        exibirPedidos();    

    }

}                <div class="pedido-info">



async function confirmarRecebimento(pedidoId) {                    <h3>Pedido #${pedido.id}</h3>        atualizarResumoFinanceiro();    // Filtrar pedidos

    try {

        const response = await fetch('/api/orders/' + pedidoId + '/status', {                    <span class="mesa-badge">Mesa ${pedido.table?.tableNumber || 'N/A'}</span>

            method: 'PUT',

            headers: {                </div>            let pedidosFiltrados = pedidos.filter(pedido => {

                'Content-Type': 'application/json'

            },                <span class="status-badge status-${getStatusClass(pedido.status)}">

            body: JSON.stringify({ status: 'DELIVERED' })

        });                    ${getStatusText(pedido.status)}    } catch (error) {        const correspondeStatus = filtroAtivo === 'todos' || pedido.status === filtroAtivo;

        

        if (!response.ok) {                </span>

            throw new Error('HTTP error! status: ' + response.status);

        }            </div>        console.error('Erro ao carregar pedidos:', error);        const correspondeBusca = !searchTerm || 

        

        await carregarPedidos();            

        mostrarSucesso('Pedido confirmado como recebido!');

                    <div class="pedido-detalhes">        mostrarErro('Erro ao carregar pedidos');            pedido.numero.toString().includes(searchTerm) ||

    } catch (error) {

        console.error('Erro ao confirmar recebimento:', error);                <div class="timestamp">

        alert('Erro ao confirmar recebimento');

    }                    <i class="fas fa-clock"></i>    }            `#${pedido.numero}`.includes(searchTerm);

}

                    ${formatDateTime(pedido.orderDate)}

function setupEventListeners() {

    // Filtros                </div>}        

    const filtros = document.querySelectorAll('.filtro-btn');

    filtros.forEach(function(btn) {                

        btn.addEventListener('click', function() {

            // Remover classe ativa de todos                <div class="itens-lista">        return correspondeStatus && correspondeBusca;

            filtros.forEach(function(f) { f.classList.remove('ativo'); });

                                ${pedido.items?.map(item => `

            // Adicionar classe ativa ao clicado

            this.classList.add('ativo');                        <div class="item-linha">function exibirPedidos() {    });

            

            // Atualizar filtro ativo                            <span class="quantidade">${item.quantity}x</span>

            filtroAtivo = this.dataset.filtro;

                                        <span class="nome">${item.menuItem?.name || 'Item não encontrado'}</span>    const pedidosGrid = document.getElementById('pedidosGrid');    

            // Reexibir pedidos

            exibirPedidos();                            <span class="preco">R$ ${(item.price || 0).toFixed(2)}</span>

            atualizarResumoFinanceiro();

        });                        </div>    const emptyMessage = document.getElementById('emptyMessage');    pedidosGrid.innerHTML = '';

    });

                        `).join('') || '<p class="text-muted">Sem itens</p>'}

    // Botão de atualizar

    const btnAtualizar = document.getElementById('btnAtualizar');                </div>        

    if (btnAtualizar) {

        btnAtualizar.addEventListener('click', carregarPedidos);            </div>

    }

}                if (!pedidos || pedidos.length === 0) {    if (pedidosFiltrados.length === 0) {



function atualizarResumoFinanceiro() {            <div class="pedido-footer">

    const totalPedidos = document.getElementById('totalPedidos');

    const valorTotal = document.getElementById('valorTotal');                <div class="total">        pedidosGrid.style.display = 'none';        emptyMessage.classList.remove('hidden');

    const pedidosEntregues = document.getElementById('pedidosEntregues');

                        <strong>Total: R$ ${(pedido.totalAmount || 0).toFixed(2)}</strong>

    if (!pedidos) {

        if (totalPedidos) totalPedidos.textContent = '0';                </div>        emptyMessage.style.display = 'block';        return;

        if (valorTotal) valorTotal.textContent = 'R$ 0,00';

        if (pedidosEntregues) pedidosEntregues.textContent = '0';                <div class="acoes">

        return;

    }                    ${getActionButtons(pedido)}        return;    } else {

    

    const total = pedidos.length;                </div>

    const valor = pedidos.reduce(function(sum, pedido) { return sum + (pedido.totalAmount || 0); }, 0);

    const entregues = pedidos.filter(function(p) { return p.status === 'DELIVERED'; }).length;            </div>    }        emptyMessage.classList.add('hidden');

    

    if (totalPedidos) totalPedidos.textContent = total.toString();        </div>

    if (valorTotal) valorTotal.textContent = 'R$ ' + valor.toFixed(2);

    if (pedidosEntregues) pedidosEntregues.textContent = entregues.toString();    `).join('');        }

}

}

function formatDateTime(dateString) {

    if (!dateString) return 'Data inválida';    // Filtrar pedidos conforme o filtro ativo    

    

    try {function getStatusClass(status) {

        const date = new Date(dateString);

        return date.toLocaleString('pt-BR', {    const statusMap = {    let pedidosFiltrados = pedidos;    pedidosFiltrados.forEach(pedido => {

            day: '2-digit',

            month: '2-digit',        'PENDING': 'pendente',

            year: 'numeric',

            hour: '2-digit',        'PREPARING': 'preparando',            const pedidoCard = criarCardPedido(pedido);

            minute: '2-digit'

        });        'READY': 'pronto',

    } catch (error) {

        return 'Data inválida';        'DELIVERED': 'entregue',    if (filtroAtivo !== 'todos') {        pedidosGrid.appendChild(pedidoCard);

    }

}        'CANCELLED': 'cancelado'



function mostrarErro(mensagem) {    };        pedidosFiltrados = pedidos.filter(pedido => {    });

    const emptyMessage = document.getElementById('emptyMessage');

    if (emptyMessage) {    return statusMap[status] || 'pendente';

        emptyMessage.innerHTML = '<div class="alert alert-danger"><i class="fas fa-exclamation-triangle"></i> ' + mensagem + '</div>';

        emptyMessage.style.display = 'block';}            const status = pedido.status?.toLowerCase();}

    }

}



function mostrarSucesso(mensagem) {function getStatusText(status) {            switch (filtroAtivo) {

    const toast = document.createElement('div');

    toast.className = 'toast align-items-center text-white bg-success border-0 show';    const statusMap = {

    toast.innerHTML = '<div class="d-flex"><div class="toast-body"><i class="fas fa-check-circle"></i> ' + mensagem + '</div></div>';

    toast.style.position = 'fixed';        'PENDING': 'Pendente',                case 'andamento':function criarCardPedido(pedido) {

    toast.style.top = '20px';

    toast.style.right = '20px';        'PREPARING': 'Preparando',

    toast.style.zIndex = '9999';

    document.body.appendChild(toast);        'READY': 'Pronto',                    return status === 'pending' || status === 'preparing';    const card = document.createElement('div');

    setTimeout(function() { toast.remove(); }, 3000);

}        'DELIVERED': 'Entregue',

        'CANCELLED': 'Cancelado'                case 'prontos':    card.className = 'pedido-card';

    };

    return statusMap[status] || 'Pendente';                    return status === 'ready';    

}

                case 'entregues':    const statusClass = `status-${pedido.status}`;

function getActionButtons(pedido) {

    const status = pedido.status;                    return status === 'delivered';    const statusText = {

    

    if (status === 'PENDING') {                case 'cancelados':        'pendente': 'Pendente',

        return `<button class="btn-cancelar" onclick="cancelarPedido(${pedido.id})">

                    <i class="fas fa-times"></i> Cancelar                    return status === 'cancelled';        'preparando': 'Preparando',

                </button>`;

    } else if (status === 'READY') {                default:        'pronto': 'Pronto'

        return `<button class="btn-confirmar" onclick="confirmarRecebimento(${pedido.id})">

                    <i class="fas fa-check"></i> Recebi                    return true;    }[pedido.status] || 'Desconhecido';

                </button>`;

    }            }    

    

    return '';        });    const dataFormatada = new Date(pedido.timestamp).toLocaleString('pt-BR');

}

    }    

async function cancelarPedido(pedidoId) {

    if (!confirm('Tem certeza que deseja cancelar este pedido?')) {        let itensHtml = '';

        return;

    }    if (pedidosFiltrados.length === 0) {    pedido.itens.forEach(item => {

    

    try {        pedidosGrid.style.display = 'none';        itensHtml += `

        const response = await fetch(`/api/orders/${pedidoId}/status`, {

            method: 'PUT',        emptyMessage.style.display = 'block';            <div style="display: flex; justify-content: space-between; align-items: center; padding: 5px 0; border-bottom: 1px solid #333;">

            headers: {

                'Content-Type': 'application/json'        return;                <div>

            },

            body: JSON.stringify({ status: 'CANCELLED' })    }                    <span style="font-weight: 500;">${item.nome}</span>

        });

                                <small style="color: #999; display: block;">Qtd: ${item.quantidade}</small>

        if (!response.ok) {

            throw new Error(`HTTP error! status: ${response.status}`);    pedidosGrid.style.display = 'grid';                </div>

        }

            emptyMessage.style.display = 'none';                <span style="color: #28a745;">R$ ${(item.preco * item.quantidade).toFixed(2)}</span>

        await carregarPedidos();

        mostrarSucesso('Pedido cancelado com sucesso!');                </div>

        

    } catch (error) {    pedidosGrid.innerHTML = pedidosFiltrados.map(pedido => `        `;

        console.error('Erro ao cancelar pedido:', error);

        alert('Erro ao cancelar pedido');        <div class="pedido-card" data-status="${pedido.status?.toLowerCase()}">    });

    }

}            <div class="pedido-header">    



async function confirmarRecebimento(pedidoId) {                <div class="pedido-info">    card.innerHTML = `

    try {

        const response = await fetch(`/api/orders/${pedidoId}/status`, {                    <h3>Pedido #${pedido.id}</h3>        <div class="pedido-header">

            method: 'PUT',

            headers: {                    <span class="mesa-badge">Mesa ${pedido.table?.tableNumber || 'N/A'}</span>            <div class="pedido-numero">#${pedido.numero}</div>

                'Content-Type': 'application/json'

            },                </div>            <div class="pedido-status ${statusClass}">${statusText}</div>

            body: JSON.stringify({ status: 'DELIVERED' })

        });                <span class="status-badge status-${getStatusClass(pedido.status)}">        </div>

        

        if (!response.ok) {                    ${getStatusText(pedido.status)}        

            throw new Error(`HTTP error! status: ${response.status}`);

        }                </span>        <div style="margin: 15px 0;">

        

        await carregarPedidos();            </div>            <small style="color: #999;">

        mostrarSucesso('Pedido confirmado como recebido!');

                                    <i class="fas fa-clock"></i>

    } catch (error) {

        console.error('Erro ao confirmar recebimento:', error);            <div class="pedido-detalhes">                ${dataFormatada}

        alert('Erro ao confirmar recebimento');

    }                <div class="timestamp">            </small>

}

                    <i class="fas fa-clock"></i>        </div>

function setupEventListeners() {

    // Filtros                    ${formatDateTime(pedido.orderDate)}        

    const filtros = document.querySelectorAll('.filtro-btn');

    filtros.forEach(btn => {                </div>        <div style="margin: 15px 0;">

        btn.addEventListener('click', function() {

            // Remover classe ativa de todos                            <h4 style="color: #fca311; margin-bottom: 10px; font-size: 1rem;">Itens do Pedido:</h4>

            filtros.forEach(f => f.classList.remove('ativo'));

                            <div class="itens-lista">            <div style="max-height: 150px; overflow-y: auto;">

            // Adicionar classe ativa ao clicado

            this.classList.add('ativo');                    ${pedido.items?.map(item => `                ${itensHtml}

            

            // Atualizar filtro ativo                        <div class="item-linha">            </div>

            filtroAtivo = this.dataset.filtro;

                                        <span class="quantidade">${item.quantity}x</span>        </div>

            // Reexibir pedidos

            exibirPedidos();                            <span class="nome">${item.menuItem?.name || 'Item não encontrado'}</span>        

            atualizarResumoFinanceiro();

        });                            <span class="preco">R$ ${(item.price || 0).toFixed(2)}</span>        <div class="pedido-total">

    });

                            </div>            Total: R$ ${pedido.total.toFixed(2)}

    // Botão de atualizar

    const btnAtualizar = document.getElementById('btnAtualizar');                    `).join('') || '<p class="text-muted">Sem itens</p>'}        </div>

    if (btnAtualizar) {

        btnAtualizar.addEventListener('click', carregarPedidos);                </div>        

    }

}            </div>        <div style="display: flex; gap: 10px; margin-top: 15px;">



function atualizarResumoFinanceiro() {                        <button onclick="verDetalhes(${pedido.numero})" style="flex: 1; background: rgba(252, 163, 17, 0.1); color: #fca311; border: 1px solid #fca311; padding: 8px; border-radius: 6px; cursor: pointer;">

    const totalPedidos = document.getElementById('totalPedidos');

    const valorTotal = document.getElementById('valorTotal');            <div class="pedido-footer">                <i class="fas fa-eye"></i>

    const pedidosEntregues = document.getElementById('pedidosEntregues');

                    <div class="total">                Detalhes

    if (!pedidos) {

        if (totalPedidos) totalPedidos.textContent = '0';                    <strong>Total: R$ ${(pedido.totalAmount || 0).toFixed(2)}</strong>            </button>

        if (valorTotal) valorTotal.textContent = 'R$ 0,00';

        if (pedidosEntregues) pedidosEntregues.textContent = '0';                </div>            ${pedido.status === 'pronto' ? `

        return;

    }                <div class="acoes">                <button onclick="marcarComoEntregue(${pedido.numero})" style="flex: 1; background: linear-gradient(135deg, #28a745, #20c997); color: white; border: none; padding: 8px; border-radius: 6px; cursor: pointer;">

    

    // Filtrar pedidos conforme filtro ativo se necessário                    ${getActionButtons(pedido)}                    <i class="fas fa-check"></i>

    let pedidosParaCalculo = pedidos;

                    </div>                    Entregue

    const total = pedidosParaCalculo.length;

    const valor = pedidosParaCalculo.reduce((sum, pedido) => sum + (pedido.totalAmount || 0), 0);            </div>                </button>

    const entregues = pedidosParaCalculo.filter(p => p.status === 'DELIVERED').length;

            </div>            ` : ''}

    if (totalPedidos) totalPedidos.textContent = total.toString();

    if (valorTotal) valorTotal.textContent = `R$ ${valor.toFixed(2)}`;    `).join('');        </div>

    if (pedidosEntregues) pedidosEntregues.textContent = entregues.toString();

}}    `;



function formatDateTime(dateString) {    

    if (!dateString) return 'Data inválida';

    function getStatusClass(status) {    return card;

    try {

        const date = new Date(dateString);    const statusMap = {}

        return date.toLocaleString('pt-BR', {

            day: '2-digit',        'PENDING': 'pendente',

            month: '2-digit',

            year: 'numeric',        'PREPARING': 'preparando',function verDetalhes(numeroPedido) {

            hour: '2-digit',

            minute: '2-digit'        'READY': 'pronto',    const pedido = pedidos.find(p => p.numero === numeroPedido);

        });

    } catch (error) {        'DELIVERED': 'entregue',    if (!pedido) return;

        return 'Data inválida';

    }        'CANCELLED': 'cancelado'    

}

    };    const conteudoDetalhes = document.getElementById('conteudoDetalhes');

function mostrarErro(mensagem) {

    const emptyMessage = document.getElementById('emptyMessage');    return statusMap[status] || 'pendente';    const dataFormatada = new Date(pedido.timestamp).toLocaleString('pt-BR');

    if (emptyMessage) {

        emptyMessage.innerHTML = `}    

            <div class="alert alert-danger">

                <i class="fas fa-exclamation-triangle"></i>    let itensDetalhados = '';

                ${mensagem}

            </div>function getStatusText(status) {    pedido.itens.forEach(item => {

        `;

        emptyMessage.style.display = 'block';    const statusMap = {        itensDetalhados += `

    }

}        'PENDING': 'Pendente',            <div style="background: rgba(255, 255, 255, 0.02); border-radius: 8px; padding: 15px; margin-bottom: 10px;">



function mostrarSucesso(mensagem) {        'PREPARING': 'Preparando',                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 8px;">

    // Criar toast de sucesso

    const toastHtml = `        'READY': 'Pronto',                    <h4 style="color: #fca311; margin: 0;">${item.nome}</h4>

        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">

            <div class="d-flex">        'DELIVERED': 'Entregue',                    <span style="color: #28a745; font-weight: 600;">R$ ${(item.preco * item.quantidade).toFixed(2)}</span>

                <div class="toast-body">

                    <i class="fas fa-check-circle"></i>        'CANCELLED': 'Cancelado'                </div>

                    ${mensagem}

                </div>    };                <p style="color: #cccccc; margin: 5px 0; font-size: 0.9rem;">${item.descricao}</p>

                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>

            </div>    return statusMap[status] || 'Pendente';                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">

        </div>

    `;}                    <small style="color: #999;">Quantidade: ${item.quantidade}</small>

    

    // Verificar se existe container de toasts                    <small style="color: #999;">Preço unitário: R$ ${item.preco.toFixed(2)}</small>

    let toastContainer = document.getElementById('toast-container');

    if (!toastContainer) {function getActionButtons(pedido) {                </div>

        toastContainer = document.createElement('div');

        toastContainer.id = 'toast-container';    const status = pedido.status;            </div>

        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';

        document.body.appendChild(toastContainer);            `;

    }

        if (status === 'PENDING') {    });

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);

            return `<button class="btn-cancelar" onclick="cancelarPedido(${pedido.id})">    

    // Inicializar e mostrar toast

    const toastElement = toastContainer.lastElementChild;                    <i class="fas fa-times"></i> Cancelar    const statusClass = `status-${pedido.status}`;

    const toast = new bootstrap.Toast(toastElement);

    toast.show();                </button>`;    const statusText = {

    

    // Remover toast após ser escondido    } else if (status === 'READY') {        'pendente': 'Pendente',

    toastElement.addEventListener('hidden.bs.toast', () => {

        toastElement.remove();        return `<button class="btn-confirmar" onclick="confirmarRecebimento(${pedido.id})">        'preparando': 'Preparando',

    });

}                    <i class="fas fa-check"></i> Recebi        'pronto': 'Pronto para Entrega'

                </button>`;    }[pedido.status] || 'Desconhecido';

    }    

        conteudoDetalhes.innerHTML = `

    return '';        <div style="margin-bottom: 20px;">

}            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">

                <h3 style="color: #fca311; margin: 0;">Pedido #${pedido.numero}</h3>

async function cancelarPedido(pedidoId) {                <span class="pedido-status ${statusClass}">${statusText}</span>

    if (!confirm('Tem certeza que deseja cancelar este pedido?')) {            </div>

        return;            <div style="display: flex; gap: 20px; margin-bottom: 15px;">

    }                <div>

                        <strong style="color: #fca311;">Mesa:</strong>

    try {                    <span style="color: #cccccc;">Mesa ${pedido.mesa}</span>

        const response = await fetch(`/api/orders/${pedidoId}/status`, {                </div>

            method: 'PUT',                <div>

            headers: {                    <strong style="color: #fca311;">Data/Hora:</strong>

                'Content-Type': 'application/json'                    <span style="color: #cccccc;">${dataFormatada}</span>

            },                </div>

            body: JSON.stringify({ status: 'CANCELLED' })            </div>

        });        </div>

                

        if (!response.ok) {        <div style="margin-bottom: 20px;">

            throw new Error(`HTTP error! status: ${response.status}`);            <h4 style="color: #fca311; margin-bottom: 15px;">Itens do Pedido:</h4>

        }            ${itensDetalhados}

                </div>

        await carregarPedidos();        

        mostrarSucesso('Pedido cancelado com sucesso!');        <div style="border-top: 2px solid #fca311; padding-top: 15px;">

                    <div style="display: flex; justify-content: space-between; align-items: center; font-size: 1.3rem; font-weight: 700;">

    } catch (error) {                <span style="color: #fca311;">Total do Pedido:</span>

        console.error('Erro ao cancelar pedido:', error);                <span style="color: #28a745;">R$ ${pedido.total.toFixed(2)}</span>

        alert('Erro ao cancelar pedido');            </div>

    }        </div>

}    `;

    

async function confirmarRecebimento(pedidoId) {    document.getElementById('modalDetalhesPedido').style.display = 'flex';

    try {}

        const response = await fetch(`/api/orders/${pedidoId}/status`, {

            method: 'PUT',function fecharDetalhes() {

            headers: {    document.getElementById('modalDetalhesPedido').style.display = 'none';

                'Content-Type': 'application/json'}

            },

            body: JSON.stringify({ status: 'DELIVERED' })function marcarComoEntregue(numeroPedido) {

        });    let pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');

            const index = pedidosSalvos.findIndex(p => p.numero === numeroPedido);

        if (!response.ok) {    

            throw new Error(`HTTP error! status: ${response.status}`);    if (index !== -1) {

        }        pedidosSalvos[index].status = 'entregue';

                pedidosSalvos[index].dataEntrega = new Date().toISOString();

        await carregarPedidos();        localStorage.setItem('pedidos', JSON.stringify(pedidosSalvos));

        mostrarSucesso('Pedido confirmado como recebido!');        

                carregarPedidos();

    } catch (error) {        atualizarResumoFinanceiro();

        console.error('Erro ao confirmar recebimento:', error);        mostrarNotificacao(`Pedido #${numeroPedido} marcado como entregue!`, 'success');

        alert('Erro ao confirmar recebimento');    }

    }}

}

function atualizarResumoFinanceiro() {

function setupEventListeners() {    const totalGeral = document.getElementById('totalGeral');

    // Filtros    const pedidosAtivos = document.getElementById('pedidosAtivos');

    const filtros = document.querySelectorAll('.filtro-btn');    const pedidosProntos = document.getElementById('pedidosProntos');

    filtros.forEach(btn => {    const btnFecharConta = document.getElementById('btnFecharConta');

        btn.addEventListener('click', function() {    

            // Remover classe ativa de todos    // Calcular totais

            filtros.forEach(f => f.classList.remove('ativo'));    const total = pedidos

                    .filter(p => p.status !== 'entregue')

            // Adicionar classe ativa ao clicado        .reduce((sum, pedido) => sum + pedido.total, 0);

            this.classList.add('ativo');    

                const countAtivos = pedidos.filter(p => p.status === 'pendente' || p.status === 'preparando').length;

            // Atualizar filtro ativo    const countProntos = pedidos.filter(p => p.status === 'pronto').length;

            filtroAtivo = this.dataset.filtro;    

                totalGeral.textContent = `R$ ${total.toFixed(2)}`;

            // Reexibir pedidos    pedidosAtivos.textContent = countAtivos;

            exibirPedidos();    pedidosProntos.textContent = countProntos;

            atualizarResumoFinanceiro();    

        });    // Habilitar botão de fechar conta se houver pedidos

    });    btnFecharConta.disabled = pedidos.filter(p => p.status !== 'entregue').length === 0;

    }

    // Botão de atualizar

    const btnAtualizar = document.getElementById('btnAtualizar');function fecharConta() {

    if (btnAtualizar) {    const pedidosParaFechar = pedidos.filter(p => p.status !== 'entregue');

        btnAtualizar.addEventListener('click', carregarPedidos);    

    }    if (pedidosParaFechar.length === 0) {

}        mostrarNotificacao('Não há pedidos para fechar!', 'warning');

        return;

function atualizarResumoFinanceiro() {    }

    const totalPedidos = document.getElementById('totalPedidos');    

    const valorTotal = document.getElementById('valorTotal');    // Verificar se há pedidos pendentes

    const pedidosEntregues = document.getElementById('pedidosEntregues');    const pedidosPendentes = pedidosParaFechar.filter(p => p.status === 'pendente' || p.status === 'preparando');

        if (pedidosPendentes.length > 0) {

    if (!pedidos) {        if (!confirm(`Há ${pedidosPendentes.length} pedido(s) ainda em preparo. Deseja continuar mesmo assim?`)) {

        if (totalPedidos) totalPedidos.textContent = '0';            return;

        if (valorTotal) valorTotal.textContent = 'R$ 0,00';        }

        if (pedidosEntregues) pedidosEntregues.textContent = '0';    }

        return;    

    }    preencherResumoConta(pedidosParaFechar);

        document.getElementById('modalFecharConta').style.display = 'flex';

    // Filtrar pedidos conforme filtro ativo se necessário}

    let pedidosParaCalculo = pedidos;

    function preencherResumoConta(pedidosParaFechar) {

    const total = pedidosParaCalculo.length;    const resumoConta = document.getElementById('resumoConta');

    const valor = pedidosParaCalculo.reduce((sum, pedido) => sum + (pedido.totalAmount || 0), 0);    const total = pedidosParaFechar.reduce((sum, pedido) => sum + pedido.total, 0);

    const entregues = pedidosParaCalculo.filter(p => p.status === 'DELIVERED').length;    

        let itensResumo = '';

    if (totalPedidos) totalPedidos.textContent = total.toString();    pedidosParaFechar.forEach(pedido => {

    if (valorTotal) valorTotal.textContent = `R$ ${valor.toFixed(2)}`;        itensResumo += `

    if (pedidosEntregues) pedidosEntregues.textContent = entregues.toString();            <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #333;">

}                <span>Pedido #${pedido.numero}</span>

                <span style="color: #28a745;">R$ ${pedido.total.toFixed(2)}</span>

function formatDateTime(dateString) {            </div>

    if (!dateString) return 'Data inválida';        `;

        });

    try {    

        const date = new Date(dateString);    resumoConta.innerHTML = `

        return date.toLocaleString('pt-BR', {        <div style="background: rgba(255, 255, 255, 0.02); border-radius: 8px; padding: 15px; margin-bottom: 15px;">

            day: '2-digit',            <h4 style="color: #fca311; margin-bottom: 10px;">Mesa ${mesaSelecionada}</h4>

            month: '2-digit',            <div style="max-height: 200px; overflow-y: auto;">

            year: 'numeric',                ${itensResumo}

            hour: '2-digit',            </div>

            minute: '2-digit'            <div style="border-top: 2px solid #fca311; margin-top: 15px; padding-top: 15px;">

        });                <div style="display: flex; justify-content: space-between; font-size: 1.2rem; font-weight: 700;">

    } catch (error) {                    <span>Total da Conta:</span>

        return 'Data inválida';                    <span style="color: #28a745;">R$ ${total.toFixed(2)}</span>

    }                </div>

}            </div>

        </div>

function mostrarErro(mensagem) {    `;

    const emptyMessage = document.getElementById('emptyMessage');}

    if (emptyMessage) {

        emptyMessage.innerHTML = `function confirmarFechamento() {

            <div class="alert alert-danger">    const metodoPagamento = document.getElementById('metodoPagamento').value;

                <i class="fas fa-exclamation-triangle"></i>    const observacoes = document.getElementById('observacoesPagamento').value;

                ${mensagem}    

            </div>    // Simular fechamento da conta

        `;    const contaFechada = {

        emptyMessage.style.display = 'block';        mesa: mesaSelecionada,

    }        pedidos: pedidos.filter(p => p.status !== 'entregue'),

}        metodoPagamento: metodoPagamento,

        observacoes: observacoes,

function mostrarSucesso(mensagem) {        dataFechamento: new Date().toISOString(),

    // Criar toast de sucesso        total: pedidos.filter(p => p.status !== 'entregue').reduce((sum, pedido) => sum + pedido.total, 0)

    const toastHtml = `    };

        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">    

            <div class="d-flex">    // Salvar histórico de contas (em uma aplicação real, enviaria para o servidor)

                <div class="toast-body">    let contasSalvas = JSON.parse(localStorage.getItem('contasFechadas') || '[]');

                    <i class="fas fa-check-circle"></i>    contasSalvas.push(contaFechada);

                    ${mensagem}    localStorage.setItem('contasFechadas', JSON.stringify(contasSalvas));

                </div>    

                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>    // Marcar todos os pedidos como entregues

            </div>    let pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');

        </div>    pedidosSalvos.forEach(pedido => {

    `;        if (pedido.mesa === mesaSelecionada && pedido.status !== 'entregue') {

                pedido.status = 'entregue';

    // Verificar se existe container de toasts            pedido.dataEntrega = new Date().toISOString();

    let toastContainer = document.getElementById('toast-container');        }

    if (!toastContainer) {    });

        toastContainer = document.createElement('div');    localStorage.setItem('pedidos', JSON.stringify(pedidosSalvos));

        toastContainer.id = 'toast-container';    

        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';    // Limpar mesa selecionada

        document.body.appendChild(toastContainer);    localStorage.removeItem('mesaSelecionada');

    }    

        cancelarFechamento();

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);    mostrarNotificacao('Conta fechada com sucesso!', 'success');

        

    // Inicializar e mostrar toast    // Redirecionar para o cardápio após 2 segundos

    const toastElement = toastContainer.lastElementChild;    setTimeout(() => {

    const toast = new bootstrap.Toast(toastElement);        window.location.href = 'cardapio.html';

    toast.show();    }, 2000);

    }

    // Remover toast após ser escondido

    toastElement.addEventListener('hidden.bs.toast', () => {function cancelarFechamento() {

        toastElement.remove();    document.getElementById('modalFecharConta').style.display = 'none';

    });    document.getElementById('metodoPagamento').value = 'dinheiro';

}    document.getElementById('observacoesPagamento').value = '';
}

function setupEventListeners() {
    // Filtros de status
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            filtroAtivo = this.dataset.filter;
            exibirPedidos();
        });
    });
    
    // Busca
    document.getElementById('searchInput').addEventListener('input', exibirPedidos);
    
    // Atualizar automaticamente os pedidos a cada 30 segundos
    setInterval(() => {
        carregarPedidos();
        atualizarResumoFinanceiro();
    }, 30000);
}

function mostrarNotificacao(mensagem, tipo = 'info') {
    const notificacao = document.createElement('div');
    notificacao.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${tipo === 'success' ? '#28a745' : tipo === 'warning' ? '#ffc107' : '#17a2b8'};
        color: ${tipo === 'warning' ? '#000' : '#fff'};
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        z-index: 9999;
        max-width: 300px;
        word-wrap: break-word;
        font-weight: 500;
    `;
    notificacao.textContent = mensagem;
    
    document.body.appendChild(notificacao);
    
    setTimeout(() => {
        notificacao.remove();
    }, 3000);
}