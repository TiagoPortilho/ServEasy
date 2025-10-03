// Estado da aplicação
let mesaSelecionada = null;
let pedidos = [];
let filtroAtivo = 'todos';

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    verificarMesaSalva();
    carregarPedidos();
    setupEventListeners();
    atualizarResumoFinanceiro();
});

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
    
    if (mesaSelecionada) {
        mesaNumero.textContent = `Mesa ${mesaSelecionada}`;
        mesaAtiva.style.display = 'flex';
    } else {
        mesaAtiva.style.display = 'none';
    }
}

function carregarPedidos() {
    // Carregar pedidos do localStorage (em uma aplicação real, viria de uma API)
    const pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');
    
    // Filtrar pedidos pela mesa selecionada
    if (mesaSelecionada) {
        pedidos = pedidosSalvos.filter(pedido => pedido.mesa === mesaSelecionada);
    } else {
        pedidos = pedidosSalvos;
    }
    
    exibirPedidos();
}

function exibirPedidos() {
    const pedidosGrid = document.getElementById('pedidosGrid');
    const emptyMessage = document.getElementById('emptyMessage');
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    
    // Filtrar pedidos
    let pedidosFiltrados = pedidos.filter(pedido => {
        const correspondeStatus = filtroAtivo === 'todos' || pedido.status === filtroAtivo;
        const correspondeBusca = !searchTerm || 
            pedido.numero.toString().includes(searchTerm) ||
            `#${pedido.numero}`.includes(searchTerm);
        
        return correspondeStatus && correspondeBusca;
    });
    
    pedidosGrid.innerHTML = '';
    
    if (pedidosFiltrados.length === 0) {
        emptyMessage.classList.remove('hidden');
        return;
    } else {
        emptyMessage.classList.add('hidden');
    }
    
    pedidosFiltrados.forEach(pedido => {
        const pedidoCard = criarCardPedido(pedido);
        pedidosGrid.appendChild(pedidoCard);
    });
}

function criarCardPedido(pedido) {
    const card = document.createElement('div');
    card.className = 'pedido-card';
    
    const statusClass = `status-${pedido.status}`;
    const statusText = {
        'pendente': 'Pendente',
        'preparando': 'Preparando',
        'pronto': 'Pronto'
    }[pedido.status] || 'Desconhecido';
    
    const dataFormatada = new Date(pedido.timestamp).toLocaleString('pt-BR');
    
    let itensHtml = '';
    pedido.itens.forEach(item => {
        itensHtml += `
            <div style="display: flex; justify-content: space-between; align-items: center; padding: 5px 0; border-bottom: 1px solid #333;">
                <div>
                    <span style="font-weight: 500;">${item.nome}</span>
                    <small style="color: #999; display: block;">Qtd: ${item.quantidade}</small>
                </div>
                <span style="color: #28a745;">R$ ${(item.preco * item.quantidade).toFixed(2)}</span>
            </div>
        `;
    });
    
    card.innerHTML = `
        <div class="pedido-header">
            <div class="pedido-numero">#${pedido.numero}</div>
            <div class="pedido-status ${statusClass}">${statusText}</div>
        </div>
        
        <div style="margin: 15px 0;">
            <small style="color: #999;">
                <i class="fas fa-clock"></i>
                ${dataFormatada}
            </small>
        </div>
        
        <div style="margin: 15px 0;">
            <h4 style="color: #fca311; margin-bottom: 10px; font-size: 1rem;">Itens do Pedido:</h4>
            <div style="max-height: 150px; overflow-y: auto;">
                ${itensHtml}
            </div>
        </div>
        
        <div class="pedido-total">
            Total: R$ ${pedido.total.toFixed(2)}
        </div>
        
        <div style="display: flex; gap: 10px; margin-top: 15px;">
            <button onclick="verDetalhes(${pedido.numero})" style="flex: 1; background: rgba(252, 163, 17, 0.1); color: #fca311; border: 1px solid #fca311; padding: 8px; border-radius: 6px; cursor: pointer;">
                <i class="fas fa-eye"></i>
                Detalhes
            </button>
            ${pedido.status === 'pronto' ? `
                <button onclick="marcarComoEntregue(${pedido.numero})" style="flex: 1; background: linear-gradient(135deg, #28a745, #20c997); color: white; border: none; padding: 8px; border-radius: 6px; cursor: pointer;">
                    <i class="fas fa-check"></i>
                    Entregue
                </button>
            ` : ''}
        </div>
    `;
    
    return card;
}

function verDetalhes(numeroPedido) {
    const pedido = pedidos.find(p => p.numero === numeroPedido);
    if (!pedido) return;
    
    const conteudoDetalhes = document.getElementById('conteudoDetalhes');
    const dataFormatada = new Date(pedido.timestamp).toLocaleString('pt-BR');
    
    let itensDetalhados = '';
    pedido.itens.forEach(item => {
        itensDetalhados += `
            <div style="background: rgba(255, 255, 255, 0.02); border-radius: 8px; padding: 15px; margin-bottom: 10px;">
                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 8px;">
                    <h4 style="color: #fca311; margin: 0;">${item.nome}</h4>
                    <span style="color: #28a745; font-weight: 600;">R$ ${(item.preco * item.quantidade).toFixed(2)}</span>
                </div>
                <p style="color: #cccccc; margin: 5px 0; font-size: 0.9rem;">${item.descricao}</p>
                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
                    <small style="color: #999;">Quantidade: ${item.quantidade}</small>
                    <small style="color: #999;">Preço unitário: R$ ${item.preco.toFixed(2)}</small>
                </div>
            </div>
        `;
    });
    
    const statusClass = `status-${pedido.status}`;
    const statusText = {
        'pendente': 'Pendente',
        'preparando': 'Preparando',
        'pronto': 'Pronto para Entrega'
    }[pedido.status] || 'Desconhecido';
    
    conteudoDetalhes.innerHTML = `
        <div style="margin-bottom: 20px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
                <h3 style="color: #fca311; margin: 0;">Pedido #${pedido.numero}</h3>
                <span class="pedido-status ${statusClass}">${statusText}</span>
            </div>
            <div style="display: flex; gap: 20px; margin-bottom: 15px;">
                <div>
                    <strong style="color: #fca311;">Mesa:</strong>
                    <span style="color: #cccccc;">Mesa ${pedido.mesa}</span>
                </div>
                <div>
                    <strong style="color: #fca311;">Data/Hora:</strong>
                    <span style="color: #cccccc;">${dataFormatada}</span>
                </div>
            </div>
        </div>
        
        <div style="margin-bottom: 20px;">
            <h4 style="color: #fca311; margin-bottom: 15px;">Itens do Pedido:</h4>
            ${itensDetalhados}
        </div>
        
        <div style="border-top: 2px solid #fca311; padding-top: 15px;">
            <div style="display: flex; justify-content: space-between; align-items: center; font-size: 1.3rem; font-weight: 700;">
                <span style="color: #fca311;">Total do Pedido:</span>
                <span style="color: #28a745;">R$ ${pedido.total.toFixed(2)}</span>
            </div>
        </div>
    `;
    
    document.getElementById('modalDetalhesPedido').style.display = 'flex';
}

function fecharDetalhes() {
    document.getElementById('modalDetalhesPedido').style.display = 'none';
}

function marcarComoEntregue(numeroPedido) {
    let pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');
    const index = pedidosSalvos.findIndex(p => p.numero === numeroPedido);
    
    if (index !== -1) {
        pedidosSalvos[index].status = 'entregue';
        pedidosSalvos[index].dataEntrega = new Date().toISOString();
        localStorage.setItem('pedidos', JSON.stringify(pedidosSalvos));
        
        carregarPedidos();
        atualizarResumoFinanceiro();
        mostrarNotificacao(`Pedido #${numeroPedido} marcado como entregue!`, 'success');
    }
}

function atualizarResumoFinanceiro() {
    const totalGeral = document.getElementById('totalGeral');
    const pedidosAtivos = document.getElementById('pedidosAtivos');
    const pedidosProntos = document.getElementById('pedidosProntos');
    const btnFecharConta = document.getElementById('btnFecharConta');
    
    // Calcular totais
    const total = pedidos
        .filter(p => p.status !== 'entregue')
        .reduce((sum, pedido) => sum + pedido.total, 0);
    
    const countAtivos = pedidos.filter(p => p.status === 'pendente' || p.status === 'preparando').length;
    const countProntos = pedidos.filter(p => p.status === 'pronto').length;
    
    totalGeral.textContent = `R$ ${total.toFixed(2)}`;
    pedidosAtivos.textContent = countAtivos;
    pedidosProntos.textContent = countProntos;
    
    // Habilitar botão de fechar conta se houver pedidos
    btnFecharConta.disabled = pedidos.filter(p => p.status !== 'entregue').length === 0;
}

function fecharConta() {
    const pedidosParaFechar = pedidos.filter(p => p.status !== 'entregue');
    
    if (pedidosParaFechar.length === 0) {
        mostrarNotificacao('Não há pedidos para fechar!', 'warning');
        return;
    }
    
    // Verificar se há pedidos pendentes
    const pedidosPendentes = pedidosParaFechar.filter(p => p.status === 'pendente' || p.status === 'preparando');
    if (pedidosPendentes.length > 0) {
        if (!confirm(`Há ${pedidosPendentes.length} pedido(s) ainda em preparo. Deseja continuar mesmo assim?`)) {
            return;
        }
    }
    
    preencherResumoConta(pedidosParaFechar);
    document.getElementById('modalFecharConta').style.display = 'flex';
}

function preencherResumoConta(pedidosParaFechar) {
    const resumoConta = document.getElementById('resumoConta');
    const total = pedidosParaFechar.reduce((sum, pedido) => sum + pedido.total, 0);
    
    let itensResumo = '';
    pedidosParaFechar.forEach(pedido => {
        itensResumo += `
            <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #333;">
                <span>Pedido #${pedido.numero}</span>
                <span style="color: #28a745;">R$ ${pedido.total.toFixed(2)}</span>
            </div>
        `;
    });
    
    resumoConta.innerHTML = `
        <div style="background: rgba(255, 255, 255, 0.02); border-radius: 8px; padding: 15px; margin-bottom: 15px;">
            <h4 style="color: #fca311; margin-bottom: 10px;">Mesa ${mesaSelecionada}</h4>
            <div style="max-height: 200px; overflow-y: auto;">
                ${itensResumo}
            </div>
            <div style="border-top: 2px solid #fca311; margin-top: 15px; padding-top: 15px;">
                <div style="display: flex; justify-content: space-between; font-size: 1.2rem; font-weight: 700;">
                    <span>Total da Conta:</span>
                    <span style="color: #28a745;">R$ ${total.toFixed(2)}</span>
                </div>
            </div>
        </div>
    `;
}

function confirmarFechamento() {
    const metodoPagamento = document.getElementById('metodoPagamento').value;
    const observacoes = document.getElementById('observacoesPagamento').value;
    
    // Simular fechamento da conta
    const contaFechada = {
        mesa: mesaSelecionada,
        pedidos: pedidos.filter(p => p.status !== 'entregue'),
        metodoPagamento: metodoPagamento,
        observacoes: observacoes,
        dataFechamento: new Date().toISOString(),
        total: pedidos.filter(p => p.status !== 'entregue').reduce((sum, pedido) => sum + pedido.total, 0)
    };
    
    // Salvar histórico de contas (em uma aplicação real, enviaria para o servidor)
    let contasSalvas = JSON.parse(localStorage.getItem('contasFechadas') || '[]');
    contasSalvas.push(contaFechada);
    localStorage.setItem('contasFechadas', JSON.stringify(contasSalvas));
    
    // Marcar todos os pedidos como entregues
    let pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');
    pedidosSalvos.forEach(pedido => {
        if (pedido.mesa === mesaSelecionada && pedido.status !== 'entregue') {
            pedido.status = 'entregue';
            pedido.dataEntrega = new Date().toISOString();
        }
    });
    localStorage.setItem('pedidos', JSON.stringify(pedidosSalvos));
    
    // Limpar mesa selecionada
    localStorage.removeItem('mesaSelecionada');
    
    cancelarFechamento();
    mostrarNotificacao('Conta fechada com sucesso!', 'success');
    
    // Redirecionar para o cardápio após 2 segundos
    setTimeout(() => {
        window.location.href = 'cardapio.html';
    }, 2000);
}

function cancelarFechamento() {
    document.getElementById('modalFecharConta').style.display = 'none';
    document.getElementById('metodoPagamento').value = 'dinheiro';
    document.getElementById('observacoesPagamento').value = '';
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