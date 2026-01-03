// Stock Admin - Integração completa com banco de dados
document.addEventListener("DOMContentLoaded", () => {
  console.log("[stock.js] carregado - Versão integrada com API");

  const addBtn = document.querySelector(".add-btn");
  const modal = document.getElementById("modalOverlay");
  const modalTitle = document.getElementById("modalTitle");
  const confirmModal = document.getElementById("confirmModal");
  const tableBody = document.querySelector(".stock-table tbody");
  const cancelBtn = document.getElementById("cancelBtn");
  const saveBtn = document.getElementById("saveBtn");

  // Form elements
  const itemCategory = document.getElementById("itemCategory");
  const itemName = document.getElementById("itemName");
  const itemPrice = document.getElementById("itemPrice");
  const itemQuantity = document.getElementById("itemQuantity");
  const itemUnit = document.getElementById("itemUnit");
  const itemMinQuantity = document.getElementById("itemMinQuantity");
  const itemEntry = document.getElementById("itemEntry");
  const itemExpiry = document.getElementById("itemExpiry");

  let editingItemId = null;

  // Aguardar o JWT interceptor carregar antes de fazer requisições
  waitForJwtInterceptor().then(() => {
    console.log('[stock.js] JWT Interceptor carregado, iniciando aplicação...');
    // Carregar itens do estoque ao inicializar
    loadStockItems();
  });

  // Função para aguardar o JWT interceptor carregar
  function waitForJwtInterceptor() {
    return new Promise((resolve) => {
      if (window.jwtInterceptorLoaded) {
        console.log('[stock.js] JWT Interceptor já estava carregado');
        resolve();
        return;
      }

      console.log('[stock.js] Aguardando JWT Interceptor carregar...');
      const checkInterval = setInterval(() => {
        if (window.jwtInterceptorLoaded) {
          console.log('[stock.js] JWT Interceptor carregado com sucesso');
          clearInterval(checkInterval);
          resolve();
        }
      }, 100);

      // Timeout de segurança (5 segundos)
      setTimeout(() => {
        if (!window.jwtInterceptorLoaded) {
          console.warn('[stock.js] Timeout aguardando JWT Interceptor, continuando mesmo assim');
          clearInterval(checkInterval);
          resolve();
        }
      }, 5000);
    });
  }

  // Event listeners
  addBtn?.addEventListener("click", () => {
    modalTitle.textContent = "Adicionar Item ao Estoque";
    editingItemId = null;
    openModal();
  });

  cancelBtn?.addEventListener("click", closeModal);
  saveBtn?.addEventListener("click", saveStockItem);

  // Fechar modal clicando fora
  modal?.addEventListener("click", (e) => {
    if (e.target === modal) {
      closeModal();
    }
  });

  // Carregar itens do estoque da API
  async function loadStockItems() {
    try {
      console.log('Carregando itens do estoque...');
      
      const response = await fetch('/api/stock');
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      const result = await response.json();
      console.log('Resposta da API:', result);
      
      let stockItems = [];
      
      // Verificar diferentes formatos de resposta
      if (Array.isArray(result)) {
        stockItems = result;
      } else if (result.data && Array.isArray(result.data)) {
        stockItems = result.data;
      } else if (result.status === 'success' && result.data && Array.isArray(result.data)) {
        stockItems = result.data;
      } else {
        console.warn('Formato de resposta não reconhecido:', result);
        stockItems = [];
      }
      
      console.log('Itens do estoque carregados:', stockItems);
      renderStockItems(stockItems);
      
    } catch (error) {
      console.error('Erro ao carregar itens do estoque:', error);
      mostrarMensagem('Erro ao carregar itens do estoque. Verifique se o servidor está funcionando.', 'error');
      renderStockItems([]); // Renderizar tabela vazia em caso de erro
    }
  }

  // Renderizar itens do estoque na tabela
  function renderStockItems(items) {
    console.log('Renderizando itens:', items);
    
    if (!tableBody) {
      console.error('Elemento tableBody não encontrado');
      mostrarMensagem('Erro: Elemento da tabela não encontrado', 'error');
      return;
    }

    if (!items || items.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="9" style="text-align: center; padding: 20px; color: #888; font-style: italic;">
            Nenhum item no estoque encontrado
          </td>
        </tr>
      `;
      return;
    }

    try {
      tableBody.innerHTML = items.map(item => {
        const unitPrice = parseFloat(item.unitPrice) || 0;
        const quantity = parseInt(item.quantity) || 0;
        const totalValue = unitPrice * quantity;
        
        return `
          <tr data-item-id="${item.id || 'N/A'}">
            <td>${escapeHtml(item.description || 'N/A')}</td>
            <td>${escapeHtml(item.name || 'N/A')}</td>
            <td>R$ ${unitPrice.toFixed(2).replace('.', ',')}</td>
            <td>${quantity}</td>
            <td>${escapeHtml(item.unit || 'N/A')}</td>
            <td>R$ ${totalValue.toFixed(2).replace('.', ',')}</td>
            <td>${formatDate(item.entryDate)}</td>
            <td>${formatDate(item.expiryDate)}</td>
            <td>
              <button class="action-btn edit-btn" title="Editar" onclick="editStockItem(${item.id || 0})">✎</button>
              <button class="action-btn delete-btn" title="Remover" onclick="deleteStockItem(${item.id || 0})">×</button>
            </td>
          </tr>
        `;
      }).join('');
    } catch (error) {
      console.error('Erro ao renderizar tabela:', error);
      mostrarMensagem('Erro ao exibir itens do estoque', 'error');
      tableBody.innerHTML = `
        <tr>
          <td colspan="9" style="text-align: center; padding: 20px; color: #f44336;">
            Erro ao carregar dados
          </td>
        </tr>
      `;
    }
  }

  // Formatar data
  function formatDate(dateString) {
    if (!dateString) return 'N/A';
    try {
      // Se for uma data completa (com horário), pegar apenas a parte da data
      if (dateString.includes('T')) {
        dateString = dateString.split('T')[0];
      }
      const date = new Date(dateString + 'T00:00:00');
      return date.toLocaleDateString('pt-BR');
    } catch (error) {
      return 'N/A';
    }
  }

  // Abrir modal
  function openModal() {
    if (modal) {
      modal.classList.add("open");
      document.body.style.overflow = "hidden";
      if (!editingItemId) {
        resetForm();
      }
    }
  }

  // Fechar modal
  function closeModal() {
    if (modal) {
      modal.classList.remove("open");
      document.body.style.overflow = "";
      editingItemId = null;
      resetForm();
    }
  }

  // Resetar formulário
  function resetForm() {
    if (itemCategory) itemCategory.value = "";
    if (itemName) itemName.value = "";
    if (itemPrice) itemPrice.value = "";
    if (itemQuantity) itemQuantity.value = "";
    if (itemUnit) itemUnit.value = "";
    if (itemMinQuantity) itemMinQuantity.value = "";
    if (itemEntry) {
      // Preencher automaticamente com a data atual
      const today = new Date().toISOString().split('T')[0];
      itemEntry.value = today;
    }
    if (itemExpiry) itemExpiry.value = "";
  }

  // Salvar item do estoque
  async function saveStockItem() {
    try {
      // Coletando dados do formulário
      const priceValue = itemPrice?.value?.replace(/[^\d.,]/g, "").replace(",", ".") || '0';
      
      const formData = {
        name: itemName?.value?.trim() || '',
        description: itemCategory?.value?.trim() || '',
        quantity: parseInt(itemQuantity?.value) || 0,
        unitPrice: parseFloat(priceValue) || 0,
        unit: itemUnit?.value?.trim() || '',
        minQuantity: parseInt(itemMinQuantity?.value) || 5,
        supplier: 'N/A', // Valor padrão
        entryDate: itemEntry?.value || new Date().toISOString().split('T')[0], // Data atual se não informada
        expiryDate: itemExpiry?.value || null
      };

      // Validação básica
      if (!formData.name) {
        mostrarMensagem('Nome do item é obrigatório', 'error');
        return;
      }

      if (!formData.unit) {
        mostrarMensagem('Unidade de medida é obrigatória', 'error');
        return;
      }

      if (formData.quantity < 0) {
        mostrarMensagem('Quantidade não pode ser negativa', 'error');
        return;
      }

      if (formData.unitPrice < 0) {
        mostrarMensagem('Preço não pode ser negativo', 'error');
        return;
      }

      if (formData.minQuantity < 0) {
        mostrarMensagem('Quantidade mínima não pode ser negativa', 'error');
        return;
      }

      const url = editingItemId ? `/api/stock/${editingItemId}` : '/api/stock';
      const method = editingItemId ? 'PUT' : 'POST';

      console.log(`${method} ${url}`, formData);

      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      const result = await response.json();

      if (!response.ok) {
        throw new Error(result.message || `HTTP ${response.status}`);
      }

      // Verificar se a resposta indica sucesso
      if (result.status === 'success' || response.ok) {
        mostrarMensagem(`Item ${editingItemId ? 'atualizado' : 'criado'} com sucesso!`, 'success');
        closeModal();
        await loadStockItems(); // Recarregar lista
      } else {
        throw new Error(result.message || 'Erro ao salvar item');
      }

    } catch (error) {
      console.error('Erro ao salvar item:', error);
      mostrarMensagem('Erro ao salvar item: ' + error.message, 'error');
    }
  }

  // Editar item do estoque
  window.editStockItem = async function(id) {
    try {
      const response = await fetch(`/api/stock/${id}`);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      const result = await response.json();
      let item = result;
      
      // Verificar formato da resposta
      if (result.data) {
        item = result.data;
      } else if (result.status === 'success' && result.data) {
        item = result.data;
      }
      
      // Configurar modal para edição
      modalTitle.textContent = "Editar Item do Estoque";
      editingItemId = id;
      
      // Preencher todos os campos do formulário
      if (itemCategory) itemCategory.value = item.description || '';
      if (itemName) itemName.value = item.name || '';
      if (itemPrice) itemPrice.value = item.unitPrice ? parseFloat(item.unitPrice).toFixed(2).replace('.', ',') : '';
      if (itemQuantity) itemQuantity.value = item.quantity || '';
      if (itemUnit) itemUnit.value = item.unit || '';
      if (itemMinQuantity) itemMinQuantity.value = item.minQuantity || '';
      if (itemEntry) {
        // Data de entrada do produto
        if (item.entryDate) {
          itemEntry.value = item.entryDate;
        } else {
          itemEntry.value = '';
        }
      }
      if (itemExpiry) {
        // Data de validade do produto  
        if (item.expiryDate) {
          itemExpiry.value = item.expiryDate;
        } else {
          itemExpiry.value = '';
        }
      }
      
      openModal();
      
    } catch (error) {
      console.error('Erro ao carregar item:', error);
      mostrarMensagem('Erro ao carregar item: ' + error.message, 'error');
    }
  };

  // Excluir item do estoque
  window.deleteStockItem = async function(id) {
    if (!confirm('Tem certeza que deseja excluir este item do estoque?')) {
      return;
    }

    try {
      const response = await fetch(`/api/stock/${id}`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        const result = await response.json();
        throw new Error(result.message || `HTTP ${response.status}`);
      }

      mostrarMensagem('Item excluído com sucesso!', 'success');
      await loadStockItems(); // Recarregar lista

    } catch (error) {
      console.error('Erro ao excluir item:', error);
      mostrarMensagem('Erro ao excluir item: ' + error.message, 'error');
    }
  };

  // Função para escapar HTML
  function escapeHtml(text) {
    if (text === null || text === undefined) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

  // Mostrar mensagens
  function mostrarMensagem(mensagem, tipo = 'info') {
    // Criar elemento de notificação se não existir
    let notificationDiv = document.getElementById('notification');
    if (!notificationDiv) {
      notificationDiv = document.createElement('div');
      notificationDiv.id = 'notification';
      notificationDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 4px;
        z-index: 10000;
        max-width: 400px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        color: white;
        font-weight: 500;
      `;
      document.body.appendChild(notificationDiv);
    }
    
    // Definir cor baseada no tipo
    const cores = {
      success: '#4CAF50',
      error: '#f44336',
      info: '#2196F3',
      warning: '#FF9800'
    };
    
    notificationDiv.style.background = cores[tipo] || cores.info;
    notificationDiv.textContent = mensagem;
    notificationDiv.style.display = 'block';
    
    // Auto-hide após 5 segundos
    setTimeout(() => {
      notificationDiv.style.display = 'none';
    }, 5000);
  }

  // Atualização automática a cada 30 segundos
  setInterval(loadStockItems, 30000);

  // Atualizar quando a página fica visível
  document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
      loadStockItems();
    }
  });

  // Atualizar quando a janela recebe foco
  window.addEventListener('focus', loadStockItems);
});
