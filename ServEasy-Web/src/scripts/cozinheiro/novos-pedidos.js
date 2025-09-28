// ===== DADOS SIMULADOS =====
let pedidosNovos = [
    {
        id: 1,
        numeroMesa: 5,
        numeroPedido: "PED-001",
        itens: [
            { nome: "Pizza Margherita", quantidade: 1 },
            { nome: "Refrigerante Cola", quantidade: 2 }
        ],
        tempoEspera: "5 min",
        prioridade: false,
        timestamp: new Date(Date.now() - 5 * 60000)
    },
    {
        id: 2,
        numeroMesa: 3,
        numeroPedido: "PED-002",
        itens: [
            { nome: "Hambúrguer Artesanal", quantidade: 2 },
            { nome: "Batata Frita", quantidade: 1 }
        ],
        tempoEspera: "12 min",
        prioridade: true,
        timestamp: new Date(Date.now() - 12 * 60000)
    },
    {
        id: 3,
        numeroMesa: 8,
        numeroPedido: "PED-003",
        itens: [
            { nome: "Salada Caesar", quantidade: 1 },
            { nome: "Suco Natural", quantidade: 1 }
        ],
        tempoEspera: "3 min",
        prioridade: false,
        timestamp: new Date(Date.now() - 3 * 60000)
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
    
    let pedidosFiltrados = [...pedidosNovos];
    
    // Aplicar filtros
    if (filtro === 'prioridade') {
        pedidosFiltrados = pedidosFiltrados.filter(p => p.prioridade);
    } else if (filtro === 'recentes') {
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
    const prioridadeClass = pedido.prioridade ? 'status-urgente' : '';
    const prioridadeIcon = pedido.prioridade ? '<i class="fas fa-exclamation-triangle"></i>' : '';
    
    return `
        <div class="pedido-card fade-in" data-id="${pedido.id}">
            <div class="pedido-header">
                <div class="pedido-numero">${pedido.numeroPedido}</div>
                <div class="pedido-mesa">Mesa ${pedido.numeroMesa}</div>
                <div class="pedido-status status-novo">
                    ${prioridadeIcon} Novo
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
                <i class="fas fa-clock"></i> Aguardando há ${pedido.tempoEspera}
            </div>
            
            <div class="pedido-acoes">
                <button class="btn btn-success btn-sm" onclick="abrirModalAceitar(${pedido.id})">
                    <i class="fas fa-check"></i> Aceitar
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
    pedidosNovos.forEach(pedido => {
        const minutosEspera = Math.floor((Date.now() - pedido.timestamp) / 60000);
        pedido.tempoEspera = `${minutosEspera} min`;
        
        // Marcar como prioridade se passou de 15 minutos
        if (minutosEspera > 15) {
            pedido.prioridade = true;
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
function abrirModalAceitar(pedidoId) {
    pedidoSelecionado = pedidosNovos.find(p => p.id === pedidoId);
    
    const detalhes = document.getElementById('detalhesPedidoAceitar');
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
                <i class="fas fa-clock"></i> Aguardando há ${pedidoSelecionado.tempoEspera}
            </p>
        </div>
    `;
    
    document.getElementById('modalAceitarPedido').style.display = 'block';
}

function abrirModalCancelar(pedidoId) {
    pedidoSelecionado = pedidosNovos.find(p => p.id === pedidoId);
    
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
    
    document.getElementById('modalCancelarPedido').style.display = 'block';
}

function fecharModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
    pedidoSelecionado = null;
}

// ===== AÇÕES DOS PEDIDOS =====
function confirmarAceitarPedido() {
    if (!pedidoSelecionado) return;
    
    // Remover da lista de novos pedidos
    pedidosNovos = pedidosNovos.filter(p => p.id !== pedidoSelecionado.id);
    
    // Simular envio para "Em Andamento"
    console.log(`Pedido ${pedidoSelecionado.numeroPedido} aceito e movido para Em Andamento`);
    
    // Atualizar interface
    carregarPedidos();
    fecharModal('modalAceitarPedido');
    
    // Mostrar notificação
    mostrarNotificacao('Pedido aceito com sucesso!', 'success');
}

function confirmarCancelarPedido() {
    if (!pedidoSelecionado) return;
    
    // Remover da lista de novos pedidos
    pedidosNovos = pedidosNovos.filter(p => p.id !== pedidoSelecionado.id);
    
    // Como é pedido novo, não descarta ingredientes
    console.log(`Pedido ${pedidoSelecionado.numeroPedido} cancelado - sem descarte de ingredientes`);
    
    // Atualizar interface
    carregarPedidos();
    fecharModal('modalCancelarPedido');
    
    // Mostrar notificação
    mostrarNotificacao('Pedido cancelado. Cliente será notificado.', 'info');
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
    
    // Remover após 3 segundos
    setTimeout(() => {
        notificacao.style.opacity = '0';
        notificacao.style.transform = 'translateX(100px)';
        setTimeout(() => document.body.removeChild(notificacao), 300);
    }, 3000);
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