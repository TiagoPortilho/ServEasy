// ===== DADOS SIMULADOS =====
let pedidosProntos = [
    {
        id: 1,
        numeroMesa: 1,
        numeroPedido: "PED-007",
        itens: [
            { nome: "Lasanha Bolonhesa", quantidade: 1 },
            { nome: "Salada Verde", quantidade: 1 }
        ],
        tempoEsperando: "3 min",
        timestamp: new Date(Date.now() - 3 * 60000),
        aguardandoRetirada: true
    },
    {
        id: 2,
        numeroMesa: 6,
        numeroPedido: "PED-008",
        itens: [
            { nome: "Peixe Grelhado", quantidade: 1 },
            { nome: "Legumes no Vapor", quantidade: 1 },
            { nome: "Água", quantidade: 2 }
        ],
        tempoEsperando: "7 min",
        timestamp: new Date(Date.now() - 7 * 60000),
        aguardandoRetirada: true
    },
    {
        id: 3,
        numeroMesa: 9,
        numeroPedido: "PED-009",
        itens: [
            { nome: "Wrap de Frango", quantidade: 2 },
            { nome: "Batata Doce", quantidade: 1 }
        ],
        tempoEsperando: "12 min",
        timestamp: new Date(Date.now() - 12 * 60000),
        aguardandoRetirada: true
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
    
    let pedidosFiltrados = [...pedidosProntos];
    
    // Aplicar filtros
    if (filtro === 'aguardando') {
        pedidosFiltrados = pedidosFiltrados.filter(p => p.aguardandoRetirada);
    } else if (filtro === 'antigos') {
        pedidosFiltrados.sort((a, b) => a.timestamp - b.timestamp);
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
    const tempoMinutos = Math.floor((Date.now() - pedido.timestamp) / 60000);
    const urgentClass = tempoMinutos > 10 ? 'style="color: #dc3545;"' : '';
    
    return `
        <div class="pedido-card fade-in" data-id="${pedido.id}">
            <div class="pedido-header">
                <div class="pedido-numero">${pedido.numeroPedido}</div>
                <div class="pedido-mesa">Mesa ${pedido.numeroMesa}</div>
                <div class="pedido-status status-pronto">
                    <i class="fas fa-check-circle"></i> Pronto
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
            
            <div class="pedido-tempo" ${urgentClass}>
                <i class="fas fa-clock"></i> Aguardando retirada há ${pedido.tempoEsperando}
                ${tempoMinutos > 10 ? '<i class="fas fa-exclamation-triangle" style="margin-left: 10px; color: #dc3545;"></i>' : ''}
            </div>
            
            <div class="pedido-acoes">
                <button class="btn btn-success btn-sm" onclick="abrirModalEntregar(${pedido.id})">
                    <i class="fas fa-utensils"></i> Entregar
                </button>
                <button class="btn btn-danger btn-sm" onclick="abrirModalDescartar(${pedido.id})">
                    <i class="fas fa-trash"></i> Descartar
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
    pedidosProntos.forEach(pedido => {
        const minutosEsperando = Math.floor((Date.now() - pedido.timestamp) / 60000);
        pedido.tempoEsperando = `${minutosEsperando} min`;
    });
    
    // Recarregar apenas se não há busca ativa
    const buscaAtiva = document.getElementById('searchInput').value;
    if (!buscaAtiva) {
        const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;
        carregarPedidos(filtroAtivo);
    }
}

// ===== MODAIS =====
function abrirModalEntregar(pedidoId) {
    pedidoSelecionado = pedidosProntos.find(p => p.id === pedidoId);
    
    const detalhes = document.getElementById('detalhesPedidoEntregar');
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
                <i class="fas fa-clock"></i> Pronto há ${pedidoSelecionado.tempoEsperando}
            </p>
        </div>
    `;
    
    document.getElementById('modalConfirmarEntrega').style.display = 'block';
}

function abrirModalDescartar(pedidoId) {
    pedidoSelecionado = pedidosProntos.find(p => p.id === pedidoId);
    
    const detalhes = document.getElementById('detalhesPedidoDescartar');
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
                <i class="fas fa-clock"></i> Pronto há ${pedidoSelecionado.tempoEsperando}
            </p>
        </div>
    `;
    
    // Resetar o select de motivo
    document.getElementById('motivoDescarte').value = '';
    
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