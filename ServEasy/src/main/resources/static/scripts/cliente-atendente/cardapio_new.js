// Cardápio Cliente-Atendente - Versão com suporte a imagens
document.addEventListener("DOMContentLoaded", function() {
  console.log("[cardapio_new.js] carregado");

  let carrinho = [];
  let menuData = [];
  let mesaSelecionada = null;

  async function carregarCardapio() {
    try {
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
      
      // Cria a estrutura do card com imagem
      card.innerHTML = `
        <div class="produto-content">
          <img src="${item.imageUrl || ''}" 
               alt="${item.name}" 
               class="produto-img"
               onerror="handleImageError(this)"
               onload="handleImageLoad(this)">
          <h3 class="produto-nome">${item.name}</h3>
          <p class="produto-preco">R$ ${item.price.toFixed(2)}</p>
          ${item.description ? `<p class="produto-descricao">${item.description}</p>` : ''}
          <div class="produto-acoes">
            <button onclick="adicionarAoCarrinho(${item.id})" class="btn-adicionar">
              <i class="fas fa-plus"></i> Adicionar
            </button>
          </div>
        </div>
      `;
      
      container.appendChild(card);
    });

    ocultarMensagemVazia();
  }

  // Função global para tratar erro de imagem
  window.handleImageError = function(img) {
    img.classList.add('error');
    img.style.display = 'flex';
    img.removeAttribute('src');
  };

  // Função global para tratar sucesso no carregamento da imagem
  window.handleImageLoad = function(img) {
    img.classList.remove('error');
    // Se a imagem não tem src ou é vazia, trata como erro
    if (!img.src || img.src === window.location.href || img.src.endsWith('#')) {
      handleImageError(img);
    }
  };

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
    if (!mesaSelecionada) {
      alert('Por favor, selecione uma mesa antes de fazer o pedido.');
      selecionarMesa();
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

  function mostrarNotificacao(mensagem) {
    // Cria uma notificação toast simples
    const toast = document.createElement('div');
    toast.className = 'toast-notification';
    toast.textContent = mensagem;
    toast.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: var(--btn-bg);
      color: black;
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
    // Implementar conforme necessário
    console.log('Carrinho atualizado:', carrinho);
  }

  // Funções de mesa (implementar conforme necessário)
  window.selecionarMesa = function() {
    console.log('Selecionando mesa...');
    // Implementar lógica de seleção de mesa
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