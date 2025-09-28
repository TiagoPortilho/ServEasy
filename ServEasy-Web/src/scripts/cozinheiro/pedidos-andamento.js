// ===== DADOS SIMULADOS =====
let pedidosAndamento = [
    {
        id: 1,
        numeroMesa: 2,
        numeroPedido: "PED-004",
        itens: [
            { nome: "Pizza Quattro Stagioni", quantidade: 1 },
            { nome: "Água com Gás", quantidade: 1 }
        ],
        tempoPreparando: "8 min",
        ingredientes: [
            { nome: "Massa de Pizza", quantidade: "1 unidade", usado: true },
            { nome: "Molho de Tomate", quantidade: "100ml", usado: true },
            { nome: "Queijo Mussarela", quantidade: "150g", usado: true },
            { nome: "Presunto", quantidade: "50g", usado: false },
            { nome: "Champignon", quantidade: "30g", usado: false }
        ],
        urgente: false,
        timestamp: new Date(Date.now() - 8 * 60000)
    },
    {
        id: 2,
        numeroMesa: 7,
        numeroPedido: "PED-005",
        itens: [
            { nome: "Hambúrguer Bacon", quantidade: 2 },
            { nome: "Onion Rings", quantidade: 1 }
        ],
        tempoPreparando: "15 min",
        ingredientes: [
            { nome: "Pão de Hambúrguer", quantidade: "2 unidades", usado: true },
            { nome: "Carne Bovina", quantidade: "200g", usado: true },
            { nome: "Bacon", quantidade: "100g", usado: true },
            { nome: "Queijo Cheddar", quantidade: "80g", usado: false },
            { nome: "Cebola", quantidade: "2 unidades", usado: false }
        ],
        urgente: true,
        timestamp: new Date(Date.now() - 15 * 60000)
    },
    {
        id: 3,
        numeroMesa: 4,
        numeroPedido: "PED-006",
        itens: [
            { nome: "Risotto de Camarão", quantidade: 1 }
        ],
        tempoPreparando: "12 min",
        ingredientes: [
            { nome: "Arroz Arbório", quantidade: "200g", usado: true },
            { nome: "Camarão", quantidade: "150g", usado: true },
            { nome: "Caldo de Peixe", quantidade: "500ml", usado: false },
            { nome: "Vinho Branco", quantidade: "50ml", usado: false }
        ],
        urgente: false,
        timestamp: new Date(Date.now() - 12 * 60000)
    }
];

let pedidoSelecionado = null;

// ===== INICIALIZAÇÃO =====
document.addEventListener('DOMContentLoaded', function() {
    carregarPedidos();
    configurarFiltros();
    configurarBusca();
    atualizarTempos();
    
    // Atualizar tempos a cada minuto
    setInterval(atualizarTempos, 60000);
});

// ===== CARREGAR E EXIBIR PEDIDOS =====
function carregarPedidos(filtro = 'todos', busca = '') {
    const grid = document.getElementById('pedidosGrid');
    const emptyMessage = document.getElementById('emptyMessage');
    
    let pedidosFiltrados = [...pedidosAndamento];
    
    // Aplicar filtros
    if (filtro === 'urgente') {
        pedidosFiltrados = pedidosFiltrados.filter(p => p.urgente);
    } else if (filtro === 'tempo') {
        pedidosFiltrados.sort((a, b) => b.timestamp - a.timestamp);
    }
    
    // Aplicar busca
    if (busca) {
        pedidosFiltrados = pedidosFiltrados.filter(pedido => 
            pedido.numeroPedido.toLowerCase().includes(busca.toLowerCase()) ||
            pedido.numeroMesa.toString().includes(busca) ||
            pedido.itens.some(item => item.nome.toLowerCase().includes(busca.toLowerCase()))
        );
    }
    
    if (pedidosFiltrados.length === 0) {
        grid.innerHTML = '';
        emptyMessage.classList.remove('hidden');
        return;
    }
    
    emptyMessage.classList.add('hidden');
    grid.innerHTML = pedidosFiltrados.map(pedido => criarCardPedido(pedido)).join('');
}

function criarCardPedido(pedido) {
    const urgenteIcon = pedido.urgente ? '<i class="fas fa-exclamation-triangle"></i>' : '';
    
    return `
        <div class="pedido-card fade-in" data-id="${pedido.id}">
            <div class="pedido-header">
                <div class="pedido-numero">${pedido.numeroPedido}</div>
                <div class="pedido-mesa">Mesa ${pedido.numeroMesa}</div>
                <div class="pedido-status status-andamento">
                    ${urgenteIcon} Em Andamento
                </div>
            </div>
            
            <div class="pedido-itens">
                <h4>Itens do Pedido:</h4>
                ${pedido.itens.map(item => `
                    <div class="item-pedido">
                        <span class="item-nome">${item.nome}</span>
                        <span class="item-quantidade">${item.quantidade}x</span>
                    </div>
                `).join('')}
            </div>
            
            <div class="pedido-tempo">
                <i class="fas fa-clock"></i> Preparando há ${pedido.tempoPreparando}
            </div>
            
            <div class="pedido-acoes">
                <button class="btn btn-success btn-sm" onclick="abrirModalFinalizar(${pedido.id})">
                    <i class="fas fa-check"></i> Pronto
                </button>
                <button class="btn btn-danger btn-sm" onclick="abrirModalCancelar(${pedido.id})">
                    <i class="fas fa-times"></i> Cancelar
                </button>
            </div>
        </div>
    `;
}

// ===== FILTROS E BUSCA =====
function configurarFiltros() {
    const filterButtons = document.querySelectorAll('.filter-btn');
    
    filterButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            // Remover active de todos
            filterButtons.forEach(b => b.classList.remove('active'));
            // Adicionar active no clicado
            this.classList.add('active');
            
            const filtro = this.dataset.filter;
            const busca = document.getElementById('searchInput').value;
            carregarPedidos(filtro, busca);
        });
    });
}

function configurarBusca() {
    const searchInput = document.getElementById('searchInput');
    
    searchInput.addEventListener('input', function() {
        const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;
        carregarPedidos(filtroAtivo, this.value);
    });
}

// ===== ATUALIZAR TEMPOS =====
function atualizarTempos() {
    pedidosAndamento.forEach(pedido => {
        const minutosPreparando = Math.floor((Date.now() - pedido.timestamp) / 60000);
        pedido.tempoPreparando = `${minutosPreparando} min`;
        
        // Marcar como urgente se passou de 20 minutos
        if (minutosPreparando > 20) {
            pedido.urgente = true;
        }
    });
    
    // Recarregar apenas se não há busca ativa
    const buscaAtiva = document.getElementById('searchInput').value;
    if (!buscaAtiva) {
        const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;
        carregarPedidos(filtroAtivo);
    }
}

// ===== MODAIS =====
function abrirModalFinalizar(pedidoId) {
    pedidoSelecionado = pedidosAndamento.find(p => p.id === pedidoId);
    
    const detalhes = document.getElementById('detalhesPedidoFinalizar');
    detalhes.innerHTML = `
        <div style="background: #333; padding: 15px; border-radius: 8px; margin: 15px 0;">
            <h4 style="color: #fca311; margin-bottom: 10px;">${pedidoSelecionado.numeroPedido} - Mesa ${pedidoSelecionado.numeroMesa}</h4>
            <div style="margin-bottom: 10px;">
                ${pedidoSelecionado.itens.map(item => `
                    <div style="display: flex; justify-content: space-between; margin: 5px 0;">
                        <span>${item.nome}</span>
                        <span style="color: #fca311;">${item.quantidade}x</span>
                    </div>
                `).join('')}
            </div>
            <p style="color: #cccccc; font-size: 0.9rem; margin: 0;">
                <i class="fas fa-clock"></i> Em preparo há ${pedidoSelecionado.tempoPreparando}
            </p>
        </div>
    `;
    
    document.getElementById('modalFinalizarPedido').style.display = 'block';
}

function abrirModalCancelar(pedidoId) {
    pedidoSelecionado = pedidosAndamento.find(p => p.id === pedidoId);
    
    const detalhes = document.getElementById('detalhesPedidoCancelar');
    detalhes.innerHTML = `
        <div style="background: #333; padding: 15px; border-radius: 8px; margin: 15px 0;">
            <h4 style="color: #fca311; margin-bottom: 10px;">${pedidoSelecionado.numeroPedido} - Mesa ${pedidoSelecionado.numeroMesa}</h4>
            <div style="margin-bottom: 10px;">
                ${pedidoSelecionado.itens.map(item => `
                    <div style="display: flex; justify-content: space-between; margin: 5px 0;">
                        <span>${item.nome}</span>
                        <span style="color: #fca311;">${item.quantidade}x</span>
                    </div>
                `).join('')}
            </div>
        </div>
    `;
    
    // Carregar lista de ingredientes
    carregarIngredientes();
    
    document.getElementById('modalCancelarPedido').style.display = 'block';
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