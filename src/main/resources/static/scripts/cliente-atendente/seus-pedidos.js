// Estado da aplicação - Seus Pedidos
let mesaSelecionada = null;
let pedidos = [];
let filtroAtivo = 'todos';
let mesas = [];

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    // Aguardar o JWT interceptor carregar antes de fazer requisições
    waitForJwtInterceptor().then(() => {
        console.log('[seus-pedidos.js] JWT Interceptor carregado, iniciando aplicação...');
        verificarMesaSalva();
        carregarMesas();
        carregarPedidos();
        setupEventListeners();

        // Atualizar pedidos a cada 30 segundos
        setInterval(carregarPedidos, 30000);
    });
});

// Função para aguardar o JWT interceptor carregar
function waitForJwtInterceptor() {
    return new Promise((resolve) => {
        if (window.jwtInterceptorLoaded) {
            console.log('[seus-pedidos.js] JWT Interceptor já estava carregado');
            resolve();
            return;
        }

        console.log('[seus-pedidos.js] Aguardando JWT Interceptor carregar...');
        const checkInterval = setInterval(() => {
            if (window.jwtInterceptorLoaded) {
                console.log('[seus-pedidos.js] JWT Interceptor carregado com sucesso');
                clearInterval(checkInterval);
                resolve();
            }
        }, 100);

        // Timeout de segurança (5 segundos)
        setTimeout(() => {
            if (!window.jwtInterceptorLoaded) {
                console.warn('[seus-pedidos.js] Timeout aguardando JWT Interceptor, continuando mesmo assim');
                clearInterval(checkInterval);
                resolve();
            }
        }, 5000);
    });
}

function verificarMesaSalva() {
    const mesaSalva = localStorage.getItem('mesaSelecionada');
    if (mesaSalva) {
        mesaSelecionada = parseInt(mesaSalva);
        mostrarMesaAtiva();
    }
}

function mostrarMesaAtiva() {
    const mesaAtiva = document.getElementById('mesaAtiva');
    const mesaNumero = document.getElementById('mesaNumero');
    const btnSelecionarMesa = document.getElementById('btnSelecionarMesa');
    
    if (mesaSelecionada && mesaAtiva && mesaNumero) {
        mesaNumero.textContent = `Mesa ${mesaSelecionada}`;
        mesaAtiva.style.display = 'flex';
        
        if (btnSelecionarMesa) {
            btnSelecionarMesa.style.display = 'none';
        }
    } else if (mesaAtiva) {
        mesaAtiva.style.display = 'none';
        
        if (btnSelecionarMesa) {
            btnSelecionarMesa.style.display = 'flex';
        }
    }
}

async function carregarMesas() {
    try {
        const response = await fetch('/api/tables');
        const result = await response.json();
        
        if (result.status === 'success' || result.success) {
            mesas = result.data || [];
        } else {
            mesas = [];
        }
    } catch (error) {
        console.warn('Erro ao carregar mesas:', error);
        mesas = [];
    }
}

async function carregarPedidos() {
    try {
        let url = '/api/orders';
        
        if (mesaSelecionada) {
            url += `/table/${mesaSelecionada}`;
        }
        
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        
        if (result.status === 'success' || result.success) {
            pedidos = result.data || [];
        } else if (Array.isArray(result)) {
            pedidos = result;
        } else {
            pedidos = [];
        }
        
        console.log('Pedidos carregados:', pedidos);
        exibirPedidos();
        atualizarEstatisticas();
        
    } catch (error) {
        console.error('Erro ao carregar pedidos:', error);
        mostrarErro('Erro ao carregar pedidos');
    }
}

function exibirPedidos() {
    const pedidosGrid = document.getElementById('pedidosGrid');
    const emptyMessage = document.getElementById('emptyMessage');
    
    if (!pedidos || pedidos.length === 0) {
        if (pedidosGrid) pedidosGrid.style.display = 'none';
        if (emptyMessage) {
            emptyMessage.style.display = 'block';
            const h3 = emptyMessage.querySelector('h3');
            if (h3) {
                h3.textContent = mesaSelecionada ? 
                    `Nenhum pedido encontrado para a Mesa ${mesaSelecionada}` : 
                    'Selecione uma mesa para ver os pedidos';
            }
        }
        return;
    }
    
    let pedidosFiltrados = pedidos;
    if (filtroAtivo !== 'todos') {
        pedidosFiltrados = pedidos.filter(function(pedido) {
            const status = pedido.status ? pedido.status.toLowerCase() : '';
            switch (filtroAtivo) {
                case 'andamento':
                    return status === 'pendente' || status === 'preparando';
                case 'prontos':
                    return status === 'pronto';
                case 'entregues':
                    return status === 'entregue';
                case 'cancelados':
                    return status === 'cancelado';
                default:
                    return true;
            }
        });
    }
    
    if (pedidosFiltrados.length === 0) {
        if (pedidosGrid) pedidosGrid.style.display = 'none';
        if (emptyMessage) {
            emptyMessage.style.display = 'block';
            const h3 = emptyMessage.querySelector('h3');
            if (h3) {
                h3.textContent = `Nenhum pedido ${filtroAtivo} encontrado`;
            }
        }
        return;
    }
    
    if (pedidosGrid) pedidosGrid.style.display = 'grid';
    if (emptyMessage) emptyMessage.style.display = 'none';
    
    pedidosGrid.innerHTML = '';
    
    pedidosFiltrados.forEach(function(pedido) {
        const card = criarCardPedido(pedido);
        pedidosGrid.appendChild(card);
    });
}

function atualizarEstatisticas() {
    // Filtrar pedidos válidos (não cancelados)
    const pedidosValidos = pedidos.filter(pedido => 
        pedido.status && pedido.status.toUpperCase() !== 'CANCELADO'
    );
    
    // Calcular total geral
    const totalGeral = pedidosValidos.reduce((sum, pedido) => {
        const total = pedido.items ? pedido.items.reduce((itemSum, item) => {
            return itemSum + (item.quantity * item.unitPrice);
        }, 0) : pedido.total || 0;
        return sum + total;
    }, 0);
    
    // Contar pedidos por status
    const pedidosAtivos = pedidosValidos.filter(pedido => {
        const status = pedido.status.toUpperCase();
        return status === 'PENDENTE' || status === 'NOVO' || status === 'EM_ANDAMENTO';
    }).length;
    
    // Contar pedidos prontos (aguardando entrega)
    const pedidosProntos = pedidosValidos.filter(pedido => 
        pedido.status.toUpperCase() === 'PRONTO'
    ).length;
    
    // Contar pedidos entregues
    const pedidosEntregues = pedidosValidos.filter(pedido => 
        pedido.status.toUpperCase() === 'ENTREGUE'
    ).length;
    
    // Atualizar elementos na tela
    const totalGeralElement = document.getElementById('totalGeral');
    const pedidosAtivosElement = document.getElementById('pedidosAtivos');
    const pedidosProntosElement = document.getElementById('pedidosProntos');
    const btnFecharConta = document.getElementById('btnFecharConta');
    
    if (totalGeralElement) {
        totalGeralElement.textContent = `R$ ${totalGeral.toFixed(2)}`;
    }
    
    if (pedidosAtivosElement) {
        pedidosAtivosElement.textContent = pedidosAtivos;
    }
    
    if (pedidosProntosElement) {
        pedidosProntosElement.textContent = pedidosProntos;
    }
    
    // Habilitar/desabilitar botão de fechar conta
    if (btnFecharConta) {
        // NÃO pode fechar se há pedidos prontos (aguardando entrega)
        const temPedidosProntos = pedidosProntos > 0;
        btnFecharConta.disabled = temPedidosProntos;
        
        if (temPedidosProntos) {
            btnFecharConta.title = 'Não é possível fechar a conta com pedidos prontos aguardando entrega';
        } else if (pedidosAtivos > 0) {
            btnFecharConta.title = 'Fechar conta (pedidos em preparo serão cancelados)';
        } else if (pedidosEntregues > 0) {
            btnFecharConta.title = 'Fechar conta da mesa';
        } else {
            btnFecharConta.title = 'Liberar mesa (sem pedidos)';
        }
    }
}

function criarCardPedido(pedido) {
    const card = document.createElement('div');
    card.className = 'pedido-card';
    
    const total = pedido.items ? pedido.items.reduce((sum, item) => {
        return sum + (item.quantity * item.unitPrice);
    }, 0) : pedido.total || 0;
    
    const dataFormatada = pedido.createdAt ? 
        new Date(pedido.createdAt).toLocaleString('pt-BR') : 
        'Data não disponível';
    
    // Verificar se o pedido pode ser cancelado (apenas pendentes)
    const podeSerCancelado = pedido.status && (
        pedido.status.toUpperCase() === 'PENDENTE' || 
        pedido.status.toUpperCase() === 'NOVO'
    );
    
    card.innerHTML = `
        <div class="pedido-header">
            <div class="pedido-info">
                <h3>Pedido #${pedido.id}</h3>
                <p class="pedido-mesa">Mesa ${pedido.tableNumber || 'N/A'}</p>
                <p class="pedido-data">${dataFormatada}</p>
            </div>
            <div class="pedido-status ${getStatusClass(pedido.status)}">
                ${getStatusText(pedido.status)}
            </div>
        </div>
        
        <div class="pedido-itens">
            ${pedido.items ? pedido.items.map(item => `
                <div class="item-linha">
                    <span class="item-nome">${item.menuItem?.name || 'Item desconhecido'}</span>
                    <span class="item-quantidade">x${item.quantity}</span>
                    <span class="item-preco">R$ ${(item.quantity * item.unitPrice).toFixed(2)}</span>
                </div>
            `).join('') : '<p>Itens não disponíveis</p>'}
        </div>
        
        ${pedido.observations ? `
            <div class="pedido-observacoes">
                <strong>Observações:</strong> ${pedido.observations}
            </div>
        ` : ''}
        
        <div class="pedido-footer">
            <div class="pedido-total">
                <strong>Total: R$ ${total.toFixed(2)}</strong>
            </div>
        </div>
        
        <div class="pedido-acoes">
            <button class="btn-detalhes" onclick="mostrarDetalhes(${pedido.id})">
                <i class="fas fa-info-circle"></i>
                Detalhes
            </button>
            ${podeSerCancelado ? `
                <button class="btn-cancelar" onclick="confirmarCancelamento(${pedido.id})">
                    <i class="fas fa-times"></i>
                    Cancelar
                </button>
            ` : ''}
        </div>
    `;
    
    return card;
}

function getStatusClass(status) {
    const statusMap = {
        'PENDENTE': 'status-pendente',
        'PREPARANDO': 'status-preparando',  
        'PRONTO': 'status-pronto',
        'ENTREGUE': 'status-entregue',
        'CANCELADO': 'status-cancelado'
    };
    return statusMap[status?.toUpperCase()] || 'status-pendente';
}

function getStatusText(status) {
    const statusMap = {
        'PENDENTE': 'Pendente',
        'PREPARANDO': 'Preparando',
        'PRONTO': 'Pronto', 
        'ENTREGUE': 'Entregue',
        'CANCELADO': 'Cancelado'
    };
    return statusMap[status?.toUpperCase()] || 'Pendente';
}

function setupEventListeners() {
    const filterButtons = document.querySelectorAll('.filter-btn');
    filterButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            filterButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            filtroAtivo = btn.dataset.filter;
            exibirPedidos();
        });
    });
}

function mostrarErro(mensagem) {
    console.error(mensagem);
}

// Funções de cancelamento de pedido
window.confirmarCancelamento = function(pedidoId) {
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2><i class="fas fa-exclamation-triangle"></i> Cancelar Pedido</h2>
                <button class="btn-close" onclick="fecharModal()">&times;</button>
            </div>
            <div class="modal-body">
                <div class="confirmacao-texto">
                    <p><strong>Atenção!</strong> Você está prestes a cancelar o pedido #${pedidoId}.</p>
                    <p>Esta ação não pode ser desfeita. O pedido será marcado como cancelado e não poderá ser mais preparado.</p>
                    <p>Tem certeza que deseja continuar?</p>
                </div>
                <div class="modal-actions">
                    <button class="btn btn-secondary" onclick="fecharModal()">
                        <i class="fas fa-arrow-left"></i>
                        Manter Pedido
                    </button>
                    <button class="btn btn-danger" onclick="cancelarPedido(${pedidoId})">
                        <i class="fas fa-times"></i>
                        Confirmar Cancelamento
                    </button>
                </div>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    modal.style.display = 'flex';
};

async function cancelarPedido(pedidoId) {
    try {
        // Mostrar loading
        const btnConfirmar = document.querySelector('.btn-danger');
        if (btnConfirmar) {
            btnConfirmar.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Cancelando...';
            btnConfirmar.disabled = true;
        }
        
        const response = await fetch(`/api/orders/${pedidoId}/cancel`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        
        if (result.status === 'success') {
            fecharModal();
            mostrarNotificacao('Pedido cancelado com sucesso!', 'success');
            carregarPedidos(); // Recarregar a lista de pedidos
        } else {
            throw new Error(result.message || 'Erro ao cancelar pedido');
        }
        
    } catch (error) {
        console.error('Erro ao cancelar pedido:', error);
        mostrarNotificacao('Erro ao cancelar pedido. Tente novamente.', 'error');
        
        // Restaurar botão
        const btnConfirmar = document.querySelector('.btn-danger');
        if (btnConfirmar) {
            btnConfirmar.innerHTML = '<i class="fas fa-times"></i> Confirmar Cancelamento';
            btnConfirmar.disabled = false;
        }
    }
}

// Função para mostrar detalhes do pedido
window.mostrarDetalhes = function(pedidoId) {
    const pedido = pedidos.find(p => p.id === pedidoId);
    if (!pedido) {
        mostrarNotificacao('Pedido não encontrado', 'error');
        return;
    }
    
    const modal = document.getElementById('modalDetalhesPedido');
    const conteudo = document.getElementById('conteudoDetalhes');
    
    if (modal && conteudo) {
        const total = pedido.items ? pedido.items.reduce((sum, item) => {
            return sum + (item.quantity * item.unitPrice);
        }, 0) : pedido.total || 0;
        
        conteudo.innerHTML = `
            <div class="detalhes-pedido">
                <div class="detalhe-linha">
                    <strong>Número do Pedido:</strong> #${pedido.id}
                </div>
                <div class="detalhe-linha">
                    <strong>Mesa:</strong> ${pedido.tableNumber || 'N/A'}
                </div>
                <div class="detalhe-linha">
                    <strong>Status:</strong> 
                    <span class="pedido-status ${getStatusClass(pedido.status)}">
                        ${getStatusText(pedido.status)}
                    </span>
                </div>
                <div class="detalhe-linha">
                    <strong>Data/Hora:</strong> ${pedido.createdAt ? new Date(pedido.createdAt).toLocaleString('pt-BR') : 'N/A'}
                </div>
                
                <h4 style="color: var(--btn-bg); margin: 20px 0 10px 0;">Itens do Pedido:</h4>
                <div class="detalhes-itens">
                    ${pedido.items ? pedido.items.map(item => `
                        <div class="detalhe-item">
                            <div class="item-info">
                                <span class="item-nome">${item.menuItem?.name || 'Item desconhecido'}</span>
                                <span class="item-descricao">${item.menuItem?.description || ''}</span>
                            </div>
                            <div class="item-valores">
                                <span class="item-quantidade">Qtd: ${item.quantity}</span>
                                <span class="item-preco-unit">Unit: R$ ${item.unitPrice.toFixed(2)}</span>
                                <span class="item-preco-total">Total: R$ ${(item.quantity * item.unitPrice).toFixed(2)}</span>
                            </div>
                        </div>
                    `).join('') : '<p>Itens não disponíveis</p>'}
                </div>
                
                ${pedido.observations ? `
                    <h4 style="color: var(--btn-bg); margin: 20px 0 10px 0;">Observações:</h4>
                    <div class="detalhe-observacoes">
                        ${pedido.observations}
                    </div>
                ` : ''}
                
                <div class="detalhe-total">
                    <strong>Total Geral: R$ ${total.toFixed(2)}</strong>
                </div>
            </div>
        `;
        
        modal.style.display = 'flex';
    }
};

// Função para fechar detalhes
window.fecharDetalhes = function() {
    const modal = document.getElementById('modalDetalhesPedido');
    if (modal) {
        modal.style.display = 'none';
    }
};

// Função auxiliar para notificações
function mostrarNotificacao(mensagem, tipo = 'info') {
    // Criar elemento de notificação
    const notificacao = document.createElement('div');
    notificacao.className = `notificacao notificacao-${tipo}`;
    notificacao.innerHTML = `
        <div class="notificacao-conteudo">
            <i class="fas ${tipo === 'success' ? 'fa-check-circle' : tipo === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i>
            <span>${mensagem}</span>
        </div>
    `;
    
    // Adicionar estilos inline para a notificação
    notificacao.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 10000;
        padding: 16px 20px;
        border-radius: 8px;
        color: white;
        font-weight: 600;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
        transform: translateX(400px);
        transition: transform 0.3s ease;
        background: ${tipo === 'success' ? 'linear-gradient(135deg, #28a745, #20c997)' : 
                    tipo === 'error' ? 'linear-gradient(135deg, #dc3545, #e74c3c)' : 
                    'linear-gradient(135deg, var(--btn-bg), var(--btn-hover-end))'};
    `;
    
    document.body.appendChild(notificacao);
    
    // Animar entrada
    setTimeout(() => {
        notificacao.style.transform = 'translateX(0)';
    }, 100);
    
    // Remover após 4 segundos
    setTimeout(() => {
        notificacao.style.transform = 'translateX(400px)';
        setTimeout(() => {
            document.body.removeChild(notificacao);
        }, 300);
    }, 4000);
}

// Função para fechar modal genérico
window.fecharModal = function() {
    const modals = document.querySelectorAll('.modal-overlay');
    modals.forEach(modal => {
        if (modal.style.display !== 'none') {
            modal.style.display = 'none';
            document.body.removeChild(modal);
        }
    });
};

// Funções de mesa
window.selecionarMesa = function() {
    mostrarModalSelecaoMesa();
};

window.trocarMesa = function() {
    mostrarModalSelecaoMesa();
};

window.liberarMesa = function() {
    if (confirm('Deseja realmente liberar a mesa atual?')) {
        mesaSelecionada = null;
        localStorage.removeItem('mesaSelecionada');
        mostrarMesaAtiva();
        carregarPedidos();
        mostrarNotificacao('Mesa liberada com sucesso!', 'info');
    }
};

function mostrarModalSelecaoMesa() {
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
        <div class="modal-content mesa-modal">
            <div class="modal-header">
                <h2>Selecionar Mesa</h2>
                <button class="btn-close" onclick="fecharModal()">&times;</button>
            </div>
            <div class="modal-body">
                <p>Selecione uma mesa para ver seus pedidos:</p>
                <div class="mesas-grid">
                    ${mesas.map(mesa => `
                        <button class="mesa-btn ${getStatusClassMesa(mesa.status)}" 
                                onclick="selecionarMesaEspecifica(${mesa.tableNumber})">
                            <div class="mesa-numero">Mesa ${mesa.tableNumber}</div>
                            <div class="mesa-status">${getStatusTextMesa(mesa.status)}</div>
                            <div class="mesa-capacidade">${mesa.capacity} pessoas</div>
                        </button>
                    `).join('')}
                </div>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    modal.style.display = 'flex';
}

function getStatusClassMesa(status) {
    switch(status) {
        case 'DISPONIVEL': return 'disponivel';
        case 'OCUPADA': return 'ocupada';
        case 'RESERVADA': return 'reservada';
        case 'MANUTENCAO': return 'manutencao';
        default: return '';
    }
}

function getStatusTextMesa(status) {
    switch(status) {
        case 'DISPONIVEL': return 'Disponível';
        case 'OCUPADA': return 'Ocupada';
        case 'RESERVADA': return 'Reservada';
        case 'MANUTENCAO': return 'Manutenção';
        default: return status;
    }
}

window.selecionarMesaEspecifica = function(numeroMesa) {
    mesaSelecionada = numeroMesa;
    localStorage.setItem('mesaSelecionada', numeroMesa);
    mostrarMesaAtiva();
    fecharModal();
    carregarPedidos();
    mostrarNotificacao(`Mesa ${numeroMesa} selecionada!`, 'success');
};

window.fecharModal = function() {
    const modals = document.querySelectorAll('.modal-overlay');
    modals.forEach(modal => modal.remove());
};

// Função para fechar conta da mesa
window.fecharConta = function() {
    if (!mesaSelecionada) {
        mostrarNotificacao('Selecione uma mesa primeiro', 'error');
        return;
    }

    // Buscar pedidos da mesa para análise
    const pedidosValidos = pedidos.filter(pedido => pedido.tableNumber == mesaSelecionada);
    
    if (pedidosValidos.length === 0) {
        // Não há pedidos, apenas confirmar liberação da mesa
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h2>Liberar Mesa ${mesaSelecionada}</h2>
                    <button class="btn-close" onclick="fecharModal()">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="release-message">
                        <p>Esta mesa não possui pedidos.</p>
                        <p>Deseja liberar a mesa ${mesaSelecionada}?</p>
                    </div>
                    
                    <div class="modal-buttons">
                        <button class="btn btn-secondary" onclick="fecharModal()">
                            <i class="fas fa-times"></i> Cancelar
                        </button>
                        <button class="btn btn-success" onclick="confirmarFecharConta()">
                            <i class="fas fa-check"></i> Liberar Mesa
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        document.body.appendChild(modal);
        modal.style.display = 'flex';
        return;
    }

    // Analisar status dos pedidos
    const pedidosProntos = pedidosValidos.filter(p => p.status.toUpperCase() === 'PRONTO');
    const pedidosEntregues = pedidosValidos.filter(p => p.status.toUpperCase() === 'ENTREGUE');
    const pedidosAtivos = pedidosValidos.filter(p => {
        const status = p.status.toUpperCase();
        return status === 'NOVO' || status === 'EM_ANDAMENTO' || status === 'PENDENTE';
    });

    // REGRA: Não permitir fechar se há pedidos PRONTOS
    if (pedidosProntos.length > 0) {
        mostrarNotificacao(`Não é possível fechar a conta. Há ${pedidosProntos.length} pedido(s) pronto(s) aguardando entrega.`, 'error');
        return;
    }

    // Calcular total apenas dos pedidos ENTREGUES
    const totalCobrar = pedidosEntregues.reduce((sum, pedido) => {
        const total = pedido.items ? pedido.items.reduce((itemSum, item) => {
            return itemSum + (item.quantity * item.unitPrice);
        }, 0) : pedido.total || 0;
        return sum + total;
    }, 0);

    // Preparar mensagens
    let mensagemAviso = '';
    let classeAviso = '';
    
    if (pedidosAtivos.length > 0) {
        mensagemAviso = `
            <div class="cancel-warning">
                <h4>Atenção</h4>
                <p>
                    <strong>${pedidosAtivos.length} pedido(s) em preparo será(ão) cancelado(s)</strong> ao fechar a conta.
                </p>
            </div>
        `;
    }

    // Mostrar modal de confirmação
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
        <div class="modal-content close-account-modal">
            <div class="modal-header">
                <h2>Fechar Conta - Mesa ${mesaSelecionada}</h2>
                <button class="btn-close" onclick="fecharModal()">&times;</button>
            </div>
            <div class="modal-body">
                ${mensagemAviso}
                
                <div class="total-section">
                    <h3>Total a Cobrar</h3>
                    <div class="total-amount">R$ ${totalCobrar.toFixed(2)}</div>
                    <p class="total-description">Baseado em ${pedidosEntregues.length} pedido(s) entregue(s)</p>
                </div>

                <div class="orders-summary">
                    <h4>Resumo dos Pedidos:</h4>
                    <div class="summary-item">
                        <span>Entregues (cobrará):</span>
                        <strong>${pedidosEntregues.length}</strong>
                    </div>
                    <div class="summary-item">
                        <span>Em preparo (cancelará):</span>
                        <strong class="cancel-count">${pedidosAtivos.length}</strong>
                    </div>
                    <div class="summary-item">
                        <span>Prontos (bloqueando):</span>
                        <strong class="ready-count">${pedidosProntos.length}</strong>
                    </div>
                </div>
                
                <div class="modal-buttons">
                    <button class="btn btn-secondary" onclick="fecharModal()">
                        <i class="fas fa-times"></i> Cancelar
                    </button>
                    <button class="btn btn-success" onclick="confirmarFecharConta()">
                        <i class="fas fa-receipt"></i> Confirmar Fechamento
                    </button>
                </div>
            </div>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'flex';
};

// Função para confirmar fechamento da conta
window.confirmarFecharConta = async function() {
    try {
        // Fazer requisição para fechar conta (novo endpoint com regras de negócio)
        const response = await fetch(`/api/tables/close-account/${mesaSelecionada}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        const result = await response.json();

        if (response.ok) {
            fecharModal();
            
            // Mostrar resultado detalhado
            if (result.data && result.data.totalCobrado !== undefined) {
                const totalCobrado = result.data.totalCobrado;
                const pedidosEntregues = result.data.pedidosEntregues || 0;
                const pedidosCancelados = result.data.pedidosCancelados || 0;
                
                let mensagem = `Conta da mesa ${mesaSelecionada} fechada!\n`;
                mensagem += ` Total cobrado: R$ ${totalCobrado}\n`;
                if (pedidosEntregues > 0) {
                    mensagem += ` ${pedidosEntregues} pedido(s) entregue(s)\n`;
                }
                if (pedidosCancelados > 0) {
                    mensagem += ` ${pedidosCancelados} pedido(s) cancelado(s)`;
                }
                
                mostrarNotificacao(mensagem, 'success');
            } else {
                mostrarNotificacao(result.message || 'Mesa liberada com sucesso!', 'success');
            }
            
            // Limpar seleção de mesa e recarregar
            mesaSelecionada = null;
            localStorage.removeItem('mesaSelecionada');
            mostrarMesaAtiva();
            carregarPedidos();
            carregarMesas();
            
        } else {
            throw new Error(result.message || `Erro ${response.status}: ${response.statusText}`);
        }
    } catch (error) {
        console.error('Erro ao fechar conta:', error);
        mostrarNotificacao('Erro ao fechar conta: ' + error.message, 'error');
    }
};

function mostrarNotificacao(mensagem, tipo = 'info') {
    const notificacao = document.createElement('div');
    
    let backgroundColor, textColor;
    switch(tipo) {
        case 'success':
            backgroundColor = '#28a745';
            textColor = 'white';
            break;
        case 'warning':
            backgroundColor = '#ffc107';
            textColor = '#000';
            break;
        case 'info':
            backgroundColor = '#17a2b8';
            textColor = 'white';
            break;
        default:
            backgroundColor = '#6c757d';
            textColor = 'white';
    }
    
    notificacao.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${backgroundColor};
        color: ${textColor};
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        z-index: 9999;
        max-width: 300px;
        word-wrap: break-word;
        font-weight: 500;
        transform: translateX(100%);
        transition: transform 0.3s ease;
    `;
    notificacao.textContent = mensagem;
    
    document.body.appendChild(notificacao);
    
    setTimeout(() => {
        notificacao.style.transform = 'translateX(0)';
    }, 100);
    
    setTimeout(() => {
        notificacao.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notificacao.parentNode) {
                notificacao.remove();
            }
        }, 300);
    }, 3000);
}