// Cardápio Cliente-Atendente - Versão com suporte a imagens
document.addEventListener("DOMContentLoaded", function() {
  console.log("[cardapio.js] carregado");

  let carrinho = [];
  let menuData = [];
  let mesaSelecionada = null;
  let stockItems = []; // Adicionar stockItems para compatibilidade com admin
  let mesas = []; // Lista de mesas

  async function carregarCardapio() {
    try {
      // Carregar estoque primeiro para referências
      await carregarEstoque();
      
      // Carregar mesas
      await carregarMesas();
      
      // Verificar se há mesa selecionada no localStorage
      verificarMesaSalva();
      
      const response = await fetch('/api/menu/available');
      const result = await response.json();
      
      if (result.status === 'success' || result.success) {
        menuData = result.data;
        exibirCardapio(menuData);
      } else {
        console.error('Erro ao carregar cardápio:', result.message);
        mostrarMensagemVazia('Erro ao carregar o cardápio');
      }
    } catch (error) {
      console.error('Erro na requisição:', error);
      mostrarMensagemVazia('Erro de conexão ao carregar cardápio');
    }
  }

  // Função para carregar estoque (igual ao admin)
  async function carregarEstoque() {
    try {
      const response = await fetch('/api/stock');
      const result = await response.json();
      
      if (result.status === 'success' || result.success) {
        stockItems = result.data || [];
      } else {
        console.warn('Não foi possível carregar o estoque');
        stockItems = [];
      }
    } catch (error) {
      console.warn('Erro ao carregar estoque:', error);
      stockItems = [];
    }
  }

  // Função para carregar mesas
  async function carregarMesas() {
    try {
      const response = await fetch('/api/tables');
      const result = await response.json();
      
      if (result.status === 'success' || result.success) {
        mesas = result.data || [];
      } else {
        console.warn('Não foi possível carregar as mesas');
        mesas = [];
      }
    } catch (error) {
      console.warn('Erro ao carregar mesas:', error);
      mesas = [];
    }
  }

  // Verificar mesa salva no localStorage
  function verificarMesaSalva() {
    const mesaSalva = localStorage.getItem('mesaSelecionada');
    if (mesaSalva) {
      mesaSelecionada = parseInt(mesaSalva);
      mostrarMesaAtiva();
    }
  }

  // Mostrar mesa ativa no header
  function mostrarMesaAtiva() {
    const mesaAtiva = document.getElementById('mesaAtiva');
    const mesaNumero = document.getElementById('mesaNumero');
    const btnSelecionarMesa = document.getElementById('btnSelecionarMesa');
    
    if (mesaSelecionada && mesaAtiva && mesaNumero) {
      mesaNumero.textContent = `Mesa ${mesaSelecionada}`;
      mesaAtiva.style.display = 'flex';
      
      // Ocultar botão de selecionar mesa quando uma mesa estiver ativa
      if (btnSelecionarMesa) {
        btnSelecionarMesa.style.display = 'none';
      }
    } else if (mesaAtiva) {
      mesaAtiva.style.display = 'none';
      
      // Mostrar botão de selecionar mesa quando nenhuma mesa estiver ativa
      if (btnSelecionarMesa) {
        btnSelecionarMesa.style.display = 'inline-flex';
      }
    }
  }

  function exibirCardapio(items) {
    const container = document.getElementById("produtosGrid");
    if (!container) return;
    
    container.innerHTML = '';
    
    if (!items || items.length === 0) {
      mostrarMensagemVazia('Nenhum produto disponível no momento');
      return;
    }

    items.forEach(function(item) {
      const card = document.createElement('div');
      card.className = 'produto-card';
      card.dataset.category = item.category ? item.category.toLowerCase() : 'outros';
      
      // Criar descrição baseada nos ingredientes de forma elegante (igual ao admin)
      let descriptionHtml = '';
      if (item.ingredients && item.ingredients.length > 0) {
        const ingredientsList = item.ingredients.map(ing => {
          const stockItem = stockItems.find(s => s.id === ing.stockItemId);
          const itemName = stockItem ? stockItem.name : (ing.stockItem ? ing.stockItem.name : 'Item não encontrado');
          
          // Mostrar ou não a unidade baseado no showUnit individual do ingrediente
          if (ing.showUnit !== false) {
            const unidadeFormatada = formatarUnidade(ing.quantity, ing.unit);
            return `${itemName.toLowerCase()} (${unidadeFormatada})`;
          } else {
            return itemName.toLowerCase();
          }
        });
        
        // Criar uma descrição natural
        let description = '';
        if (ingredientsList.length === 1) {
          description = `Preparado com ${ingredientsList[0]}.`;
        } else if (ingredientsList.length === 2) {
          description = `Preparado com ${ingredientsList[0]} e ${ingredientsList[1]}.`;
        } else {
          const lastIngredient = ingredientsList.pop();
          description = `Preparado com ${ingredientsList.join(', ')} e ${lastIngredient}.`;
        }
        
        descriptionHtml = `<p class="produto-descricao">${description}</p>`;
      } else {
        descriptionHtml = `<p class="produto-descricao"><em>Delicioso prato preparado especialmente para você.</em></p>`;
      }
      
      // Trata a URL da imagem
      const imageUrl = item.imageUrl && item.imageUrl.trim() !== '' ? item.imageUrl : '';
      
      // Cria a estrutura do card igual ao do admin
      card.innerHTML = `
        <img src="${imageUrl}" alt="${item.name}" class="produto-img" onerror="this.src=''" />
        <h3 class="produto-nome">${item.name}</h3>
        <div class="produto-preco">R$ ${item.price.toFixed(2)}</div>
        ${descriptionHtml}
        <div class="produto-categoria">${getCategoryDisplayName(item.category)}</div>
        <div class="produto-acoes">
          <button onclick="adicionarAoCarrinho(${item.id})" class="btn-adicionar">
            <i class="fas fa-plus"></i> Adicionar
          </button>
        </div>
      `;
      
      container.appendChild(card);
    });

    ocultarMensagemVazia();
  }

  // Função para formatar unidades de medida (igual ao admin)
  function formatarUnidade(quantidade, unidade) {
    const qtd = quantidade % 1 === 0 ? quantidade.toString() : quantidade.toFixed(1);
    
    // Mapeamento de unidades para formatação mais elegante
    const unidadesFormatadas = {
      'g': 'g',
      'kg': 'kg', 
      'ml': 'ml',
      'l': 'L',
      'un': quantidade == 1 ? 'unidade' : 'unidades',
      'unidades': quantidade == 1 ? 'unidade' : 'unidades',
      'unidade': quantidade == 1 ? 'unidade' : 'unidades',
      'fatia': quantidade == 1 ? 'fatia' : 'fatias',
      'fatias': quantidade == 1 ? 'fatia' : 'fatias',
      'colher': quantidade == 1 ? 'colher' : 'colheres',
      'colheres': quantidade == 1 ? 'colher' : 'colheres',
      'xícara': quantidade == 1 ? 'xícara' : 'xícaras',
      'xícaras': quantidade == 1 ? 'xícara' : 'xícaras',
      'pitada': quantidade == 1 ? 'pitada' : 'pitadas',
      'pitadas': quantidade == 1 ? 'pitada' : 'pitadas'
    };
    
    const unidadeFormatada = unidadesFormatadas[unidade.toLowerCase()] || unidade;
    
    // Para unidades de peso/volume, colocar a unidade junto com o número
    if (['g', 'kg', 'ml', 'L'].includes(unidadeFormatada)) {
      return `${qtd}${unidadeFormatada}`;
    } else {
      // Para outras unidades, colocar um espaço
      return quantidade == 1 ? `${qtd} ${unidadeFormatada}` : `${qtd} ${unidadeFormatada}`;
    }
  }
  
  // Função para obter nome de exibição da categoria (igual ao admin)
  function getCategoryDisplayName(category) {
    const categoryMap = {
      'ENTRADAS': 'Entradas',
      'PRATOS_PRINCIPAIS': 'Pratos Principais',
      'SOBREMESAS': 'Sobremesas',
      'BEBIDAS': 'Bebidas',
      'LANCHES': 'Lanches',
      'PIZZAS': 'Pizzas'
    };
    return categoryMap[category] || category;
  }

  function mostrarMensagemVazia(mensagem) {
    const emptyMessage = document.getElementById('emptyMessage');
    if (emptyMessage) {
      emptyMessage.querySelector('h3').textContent = mensagem;
      emptyMessage.classList.remove('hidden');
    }
  }

  function ocultarMensagemVazia() {
    const emptyMessage = document.getElementById('emptyMessage');
    if (emptyMessage) {
      emptyMessage.classList.add('hidden');
    }
  }

  window.adicionarAoCarrinho = function(itemId) {
    // Verificar se uma mesa foi selecionada
    if (!mesaSelecionada) {
      mostrarModalSelecaoMesa();
      return;
    }

    const item = menuData.find(function(i) { return i.id === itemId; });
    if (!item) return;

    const itemCarrinho = carrinho.find(function(i) { return i.id === itemId; });
    if (itemCarrinho) {
      itemCarrinho.quantity += 1;
    } else {
      carrinho.push({
        id: item.id,
        name: item.name,
        price: item.price,
        quantity: 1
      });
    }
    
    atualizarCarrinho();
    // Feedback visual mais discreto
    mostrarNotificacao(`${item.name} adicionado ao pedido`);
  };

  function mostrarNotificacao(mensagem, tipo = 'success') {
    // Cria uma notificação toast simples
    const toast = document.createElement('div');
    toast.className = 'toast-notification';
    toast.textContent = mensagem;
    
    let backgroundColor, textColor;
    switch(tipo) {
      case 'success':
        backgroundColor = 'var(--btn-bg)';
        textColor = 'black';
        break;
      case 'warning':
        backgroundColor = '#ff6b6b';
        textColor = 'white';
        break;
      case 'info':
        backgroundColor = '#17a2b8';
        textColor = 'white';
        break;
      default:
        backgroundColor = 'var(--btn-bg)';
        textColor = 'black';
    }
    
    toast.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: ${backgroundColor};
      color: ${textColor};
      padding: 12px 20px;
      border-radius: 8px;
      z-index: 1000;
      font-weight: 500;
      box-shadow: 0 4px 12px rgba(0,0,0,0.3);
      transform: translateX(100%);
      transition: transform 0.3s ease;
    `;
    
    document.body.appendChild(toast);
    
    // Anima a entrada
    setTimeout(() => {
      toast.style.transform = 'translateX(0)';
    }, 100);
    
    // Remove após 3 segundos
    setTimeout(() => {
      toast.style.transform = 'translateX(100%)';
      setTimeout(() => {
        if (toast.parentNode) {
          toast.parentNode.removeChild(toast);
        }
      }, 300);
    }, 3000);
  }

  function atualizarCarrinho() {
    console.log('Carrinho atualizado:', carrinho);
    
    // Atualizar contador de itens no carrinho se houver
    const contadorCarrinho = document.getElementById('contadorCarrinho');
    if (contadorCarrinho) {
      const totalItens = carrinho.reduce((total, item) => total + item.quantity, 0);
      contadorCarrinho.textContent = totalItens;
      contadorCarrinho.style.display = totalItens > 0 ? 'inline' : 'none';
    }

    // Mostrar/ocultar botão de finalizar pedido
    const btnFinalizarPedido = document.getElementById('btnFinalizarPedido');
    if (btnFinalizarPedido) {
      btnFinalizarPedido.style.display = carrinho.length > 0 ? 'flex' : 'none';
    }
  }

  // Função para remover item do carrinho
  window.removerDoCarrinho = function(itemId) {
    const index = carrinho.findIndex(item => item.id === itemId);
    if (index > -1) {
      if (carrinho[index].quantity > 1) {
        carrinho[index].quantity -= 1;
      } else {
        carrinho.splice(index, 1);
      }
      atualizarCarrinho();
      mostrarNotificacao('Item removido do pedido', 'info');
      
      // Atualizar o modal se estiver aberto
      const modalPedido = document.querySelector('.pedido-modal');
      if (modalPedido) {
        fecharModal();
        setTimeout(() => mostrarModalFinalizarPedido(), 100);
      }
    }
  };

  // Função para finalizar pedido
  window.finalizarPedido = function() {
    if (!mesaSelecionada) {
      mostrarNotificacao('Selecione uma mesa primeiro!', 'warning');
      return;
    }
    
    if (carrinho.length === 0) {
      mostrarNotificacao('Adicione itens ao pedido primeiro!', 'warning');
      return;
    }
    
    mostrarModalFinalizarPedido();
  };

  function mostrarModalFinalizarPedido() {
    if (carrinho.length === 0) {
      mostrarNotificacao('Carrinho vazio!', 'warning');
      return;
    }
    
    const total = carrinho.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
      <div class="modal-content pedido-modal">
        <div class="modal-header">
          <h2><i class="fas fa-shopping-cart"></i> Finalizar Pedido - Mesa ${mesaSelecionada}</h2>
          <button class="btn-close" onclick="fecharModal()">&times;</button>
        </div>
        <div class="modal-body">
          <div class="resumo-pedido">
            <h3>Resumo do Pedido</h3>
            <div class="itens-pedido" id="itensModalPedido">
              ${renderizarItensModal()}
            </div>
            <div class="total-pedido">
              <strong>Total: R$ ${total.toFixed(2)}</strong>
            </div>
          </div>
          <div class="observacoes-pedido">
            <label for="observacoesPedido">Observações (opcional):</label>
            <textarea id="observacoesPedido" placeholder="Observações especiais para o pedido..."></textarea>
          </div>
          <div class="modal-actions">
            <button class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
            <button class="btn btn-success" onclick="confirmarPedido()" id="btnConfirmarPedido">
              <i class="fas fa-paper-plane"></i> Enviar para Cozinha
            </button>
          </div>
        </div>
      </div>
    `;
    
    document.body.appendChild(modal);
    modal.style.display = 'flex';
  }

  function renderizarItensModal() {
    return carrinho.map(item => `
      <div class="item-pedido" id="item-${item.id}">
        <span class="item-nome">${item.name}</span>
        <span class="item-quantidade">x${item.quantity}</span>
        <span class="item-preco">R$ ${(item.price * item.quantity).toFixed(2)}</span>
        <button class="btn-remover" onclick="removerDoCarrinho(${item.id})" title="Remover item">
          <i class="fas fa-minus"></i>
        </button>
      </div>
    `).join('');
  }

  window.confirmarPedido = async function() {
    const btnConfirmar = document.getElementById('btnConfirmarPedido');
    const observacoes = document.getElementById('observacoesPedido').value.trim();
    
    if (carrinho.length === 0) {
      mostrarNotificacao('Carrinho vazio!', 'warning');
      return;
    }
    
    // Desabilitar botão durante envio
    btnConfirmar.disabled = true;
    btnConfirmar.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
    
    const pedidoData = {
      tableNumber: mesaSelecionada,
      items: carrinho.map(item => ({
        menuItemId: item.id,
        quantity: item.quantity,
        unitPrice: item.price
      })),
      observations: observacoes || null,
      status: 'PENDENTE'
    };
    
    console.log('Enviando pedido:', pedidoData);
    
    try {
      const response = await fetch('/api/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(pedidoData)
      });
      
      console.log('Response status:', response.status);
      const result = await response.json();
      console.log('Response data:', result);
      
      if (response.ok && (result.status === 'success' || result.success)) {
        // Limpar carrinho
        carrinho = [];
        atualizarCarrinho();
        fecharModal();
        mostrarNotificacao(`Pedido enviado para a cozinha! Mesa ${mesaSelecionada}`, 'success');
      } else {
        throw new Error(result.message || 'Erro ao enviar pedido');
      }
    } catch (error) {
      console.error('Erro ao enviar pedido:', error);
      mostrarNotificacao('Erro ao enviar pedido: ' + error.message, 'warning');
      
      // Reabilitar botão
      btnConfirmar.disabled = false;
      btnConfirmar.innerHTML = '<i class="fas fa-paper-plane"></i> Enviar para Cozinha';
    }
  };

  // Funções de mesa (implementar conforme necessário)
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
      carrinho = []; // Limpar carrinho também
      atualizarCarrinho();
      mostrarNotificacao('Mesa liberada com sucesso!', 'info');
    }
  };

  // Modal de seleção de mesa
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
          <p>Selecione uma mesa para continuar:</p>
          <div class="mesas-grid" id="mesasGrid">
            ${mesas.map(mesa => `
              <button class="mesa-btn ${getStatusClass(mesa.status)}" 
                      data-mesa="${mesa.tableNumber}" 
                      data-status="${mesa.status}"
                      onclick="selecionarMesaEspecifica(${mesa.tableNumber}, '${mesa.status}')">
                <div class="mesa-numero">Mesa ${mesa.tableNumber}</div>
                <div class="mesa-status">${getStatusText(mesa.status)}</div>
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

  function getStatusClass(status) {
    switch(status) {
      case 'DISPONIVEL': return 'disponivel';
      case 'OCUPADA': return 'ocupada';
      case 'RESERVADA': return 'reservada';
      case 'MANUTENCAO': return 'manutencao';
      default: return '';
    }
  }

  function getStatusText(status) {
    switch(status) {
      case 'DISPONIVEL': return 'Disponível';
      case 'OCUPADA': return 'Ocupada';
      case 'RESERVADA': return 'Reservada';
      case 'MANUTENCAO': return 'Manutenção';
      default: return status;
    }
  }

  window.selecionarMesaEspecifica = async function(numeroMesa, status) {
    if (status === 'MANUTENCAO') {
      mostrarNotificacao('Esta mesa está em manutenção', 'warning');
      return;
    }

    if (status === 'DISPONIVEL') {
      // Mesa disponível - mostrar modal para ocupar
      mostrarModalOcuparMesa(numeroMesa);
    } else if (status === 'RESERVADA') {
      // Mesa reservada - mostrar modal para confirmar reserva
      mostrarModalConfirmarReserva(numeroMesa);
    } else if (status === 'OCUPADA') {
      // Mesa ocupada - o atendente pode selecionar para trabalhar nela
      selecionarMesaOcupada(numeroMesa);
    }
  };

  function mostrarModalOcuparMesa(numeroMesa) {
    fecharModal();
    
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
      <div class="modal-content">
        <div class="modal-header">
          <h2>Ocupar Mesa ${numeroMesa}</h2>
          <button class="btn-close" onclick="fecharModal()">&times;</button>
        </div>
        <div class="modal-body">
          <p>A Mesa ${numeroMesa} está disponível. Deseja ocupá-la?</p>
          <div class="modal-actions">
            <button class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
            <button class="btn btn-primary" onclick="ocuparMesa(${numeroMesa})">Ocupar Mesa</button>
          </div>
        </div>
      </div>
    `;
    
    document.body.appendChild(modal);
    modal.style.display = 'flex';
  }

  function mostrarModalConfirmarReserva(numeroMesa) {
    fecharModal();
    
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
      <div class="modal-content">
        <div class="modal-header">
          <h2>Mesa Reservada</h2>
          <button class="btn-close" onclick="fecharModal()">&times;</button>
        </div>
        <div class="modal-body">
          <p>A Mesa ${numeroMesa} está reservada.</p>
          <p>Se você é o cliente com reserva, informe seu nome:</p>
          <input type="text" id="nomeCliente" placeholder="Nome do cliente" class="form-input">
          <div class="modal-actions">
            <button class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
            <button class="btn btn-primary" onclick="confirmarReserva(${numeroMesa})">Confirmar</button>
          </div>
        </div>
      </div>
    `;
    
    document.body.appendChild(modal);
    modal.style.display = 'flex';
  }

  window.ocuparMesa = async function(numeroMesa) {
    try {
      const response = await fetch(`/api/tables/occupy/${numeroMesa}`, {
        method: 'POST'
      });
      
      const result = await response.json();
      
      if (result.status === 'success' || result.success) {
        mesaSelecionada = numeroMesa;
        localStorage.setItem('mesaSelecionada', numeroMesa);
        mostrarMesaAtiva();
        fecharModal();
        mostrarNotificacao(`Mesa ${numeroMesa} ocupada com sucesso!`, 'success');
        // Recarregar lista de mesas
        await carregarMesas();
      } else {
        mostrarNotificacao(result.message || 'Erro ao ocupar mesa', 'warning');
      }
    } catch (error) {
      console.error('Erro ao ocupar mesa:', error);
      mostrarNotificacao('Erro de conexão ao ocupar mesa', 'warning');
    }
  };

  window.confirmarReserva = async function(numeroMesa) {
    const nomeCliente = document.getElementById('nomeCliente').value.trim();
    
    if (!nomeCliente) {
      mostrarNotificacao('Por favor, informe o nome do cliente', 'warning');
      return;
    }

    try {
      const response = await fetch(`/api/tables/occupy/${numeroMesa}?customerName=${encodeURIComponent(nomeCliente)}`, {
        method: 'POST'
      });
      
      const result = await response.json();
      
      if (result.status === 'success' || result.success) {
        mesaSelecionada = numeroMesa;
        localStorage.setItem('mesaSelecionada', numeroMesa);
        mostrarMesaAtiva();
        fecharModal();
        mostrarNotificacao(`Mesa ${numeroMesa} confirmada para ${nomeCliente}!`, 'success');
        // Recarregar lista de mesas
        await carregarMesas();
      } else {
        mostrarNotificacao(result.message || 'Erro ao confirmar reserva', 'warning');
      }
    } catch (error) {
      console.error('Erro ao confirmar reserva:', error);
      mostrarNotificacao('Erro de conexão ao confirmar reserva', 'warning');
    }
  };

  function selecionarMesaOcupada(numeroMesa) {
    fecharModal();
    
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
      <div class="modal-content">
        <div class="modal-header">
          <h2>Mesa ${numeroMesa} - Ocupada</h2>
          <button class="btn-close" onclick="fecharModal()">&times;</button>
        </div>
        <div class="modal-body">
          <p>Esta mesa está ocupada. Deseja trabalhar com ela?</p>
          <p><small>Você poderá ver os pedidos e adicionar novos itens.</small></p>
          <div class="modal-actions">
            <button class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
            <button class="btn btn-primary" onclick="trabalharComMesa(${numeroMesa})">Trabalhar com Mesa</button>
          </div>
        </div>
      </div>
    `;
    
    document.body.appendChild(modal);
    modal.style.display = 'flex';
  }

  window.trabalharComMesa = function(numeroMesa) {
    mesaSelecionada = numeroMesa;
    localStorage.setItem('mesaSelecionada', numeroMesa);
    mostrarMesaAtiva();
    fecharModal();
    mostrarNotificacao(`Trabalhando com Mesa ${numeroMesa}!`, 'success');
  };

  window.fecharModal = function() {
    const modals = document.querySelectorAll('.modal-overlay');
    modals.forEach(modal => modal.remove());
  };

  // Filtros
  function configurarFiltros() {
    const filterButtons = document.querySelectorAll('.filter-btn');
    const searchInput = document.getElementById('searchInput');

    filterButtons.forEach(btn => {
      btn.addEventListener('click', () => {
        // Remove active de todos
        filterButtons.forEach(b => b.classList.remove('active'));
        // Adiciona active no clicado
        btn.classList.add('active');
        
        filtrarProdutos();
      });
    });

    if (searchInput) {
      searchInput.addEventListener('input', filtrarProdutos);
    }
  }

  function filtrarProdutos() {
    const filtroAtivo = document.querySelector('.filter-btn.active');
    const termoBusca = document.getElementById('searchInput')?.value.toLowerCase() || '';
    const categoriaFiltro = filtroAtivo?.dataset.filter || 'todos';

    const produtosFiltrados = menuData.filter(item => {
      const matchCategoria = categoriaFiltro === 'todos' || 
                            (item.category && item.category.toLowerCase().includes(categoriaFiltro));
      
      const matchBusca = !termoBusca || 
                        item.name.toLowerCase().includes(termoBusca) ||
                        (item.description && item.description.toLowerCase().includes(termoBusca));

      return matchCategoria && matchBusca;
    });

    exibirCardapio(produtosFiltrados);
  }

  // Inicialização
  carregarCardapio();
  configurarFiltros();
});