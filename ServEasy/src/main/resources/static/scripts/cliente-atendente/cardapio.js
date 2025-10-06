// Cardápio Cliente-Atendente - Apenas dados reais da API
document.addEventListener("DOMContentLoaded", function() {
  console.log("[cardapio.js] carregado");

  let carrinho = [];
  let menuData = [];

  async function carregarCardapio() {
    try {
      const response = await fetch('/api/menu/available');
      const result = await response.json();
      
      if (result.status === 'success') {
        menuData = result.data;
        exibirCardapio(menuData);
      } else {
        console.error('Erro ao carregar cardápio:', result.message);
        alert('Erro ao carregar cardápio');
      }
    } catch (error) {
      console.error('Erro na requisição:', error);
      alert('Erro de conexão ao carregar cardápio');
    }
  }

  function exibirCardapio(items) {
    const container = document.getElementById("menuContainer");
    if (!container) return;
    
    container.innerHTML = '';
    
    items.forEach(function(item) {
      const card = document.createElement('div');
      card.className = 'menu-item-card';
      card.innerHTML = '<div class="item-content">' +
        '<h4>' + item.name + '</h4>' +
        '<p>' + (item.description || '') + '</p>' +
        '<p class="price">R$ ' + item.price.toFixed(2) + '</p>' +
        '<button onclick="adicionarAoCarrinho(' + item.id + ')">Adicionar</button>' +
        '</div>';
      container.appendChild(card);
    });
  }

  window.adicionarAoCarrinho = function(itemId) {
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
    alert(item.name + ' adicionado ao pedido');
  };

  function atualizarCarrinho() {
    const cartItems = document.getElementById("cartItems");
    const cartTotal = document.getElementById("cartTotal");
    
    if (!cartItems || !cartTotal) return;

    cartItems.innerHTML = '';
    let total = 0;

    carrinho.forEach(function(item) {
      const subtotal = item.price * item.quantity;
      total += subtotal;

      const itemElement = document.createElement('div');
      itemElement.className = 'cart-item';
      itemElement.innerHTML = '<span>' + item.name + ' - Qtd: ' + item.quantity + ' - R$ ' + subtotal.toFixed(2) + '</span>';
      cartItems.appendChild(itemElement);
    });

    cartTotal.textContent = 'Total: R$ ' + total.toFixed(2);
  }

  const finalizeBtn = document.getElementById("finalizeOrder");
  if (finalizeBtn) {
    finalizeBtn.addEventListener('click', async function() {
      if (carrinho.length === 0) {
        alert('Adicione itens ao pedido');
        return;
      }

      const tableNumberInput = document.getElementById("tableNumber");
      const tableNumber = parseInt(tableNumberInput ? tableNumberInput.value : 0);

      if (!tableNumber) {
        alert('Informe o número da mesa');
        return;
      }

      const orderData = {
        tableNumber: tableNumber,
        customerName: '',
        notes: '',
        items: carrinho.map(function(item) {
          return {
            menuItemId: item.id,
            quantity: item.quantity,
            notes: ''
          };
        })
      };

      try {
        const response = await fetch('/api/orders', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(orderData)
        });

        const result = await response.json();
        
        if (result.status === 'success') {
          alert('Pedido realizado com sucesso!');
          carrinho = [];
          atualizarCarrinho();
          if (tableNumberInput) tableNumberInput.value = '';
        } else {
          alert('Erro ao realizar pedido: ' + result.message);
        }
      } catch (error) {
        console.error('Erro ao finalizar pedido:', error);
        alert('Erro de conexão');
      }
    });
  }

  carregarCardapio();
});