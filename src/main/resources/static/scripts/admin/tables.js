document.addEventListener("DOMContentLoaded", () => {
  const addBtn = document.getElementById("openAdd");
  const cancelBtn = document.getElementById("cancelBtn");
  const saveBtn = document.getElementById("saveBtn");
  const tablesGrid = document.querySelector(".tables-grid");
  
  let editingTableId = null;
  
  // Confirmação
  const confirmModal = document.getElementById("confirmModal");
  const confirmCancel = document.getElementById("confirmCancel");
  const confirmOk = document.getElementById("confirmOk");
  let currentCallback = null;

  // Carrega as mesas ao iniciar
  loadTables();
  
  // Recarrega as mesas quando a página fica visível novamente
  document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
      console.log('Página ficou visível, recarregando mesas...');
      loadTables();
    }
  });
  
  // Recarrega as mesas quando a janela recebe foco
  window.addEventListener('focus', () => {
    console.log('Janela recebeu foco, recarregando mesas...');
    loadTables();
  });
  
  // Atualização automática a cada 30 segundos
  setInterval(() => {
    console.log('Atualização automática das mesas...');
    loadTables();
  }, 30000);

  function showConfirm(message, callback) {
    document.getElementById("confirmMessage").textContent = message;
    confirmModal?.classList.add("open");
    currentCallback = callback;
  }

  confirmCancel?.addEventListener("click", () => {
    confirmModal?.classList.remove("open");
    currentCallback = null;
  });

  confirmOk?.addEventListener("click", () => {
    if (currentCallback) currentCallback();
    confirmModal?.classList.remove("open");
    currentCallback = null;
  });

  function openModal() {
    // Limpar TUDO primeiro
    editingTableId = null;
    
    // Pegar elementos
    const modal = document.getElementById('modalOverlay');
    const numeroInput = document.getElementById('tableNumber');
    const capacidadeInput = document.getElementById('tableCapacity');
    const statusSelect = document.getElementById('tableStatus');
    const titulo = modal.querySelector('h2');
    
    // Alterar título
    titulo.textContent = 'Adicionar Mesa';
    
    // LIMPAR CAMPOS
    numeroInput.value = '';
    capacidadeInput.value = '4';
    statusSelect.value = 'DISPONIVEL';
    
    // Abrir modal
    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
    numeroInput.focus();
  }

  function closeModal() {
    // Pegar elementos
    const modal = document.getElementById('modalOverlay');
    const numeroInput = document.getElementById('tableNumber');
    const capacidadeInput = document.getElementById('tableCapacity');
    const statusSelect = document.getElementById('tableStatus');
    
    // Fechar modal
    modal.classList.remove('open');
    document.body.style.overflow = '';
    
    // LIMPAR CAMPOS
    numeroInput.value = '';
    capacidadeInput.value = '4';
    statusSelect.value = 'DISPONIVEL';
    
    // Resetar ID de edição
    editingTableId = null;
  }

  // Event Listeners
  addBtn?.addEventListener("click", openModal);
  cancelBtn?.addEventListener("click", closeModal);
  
  // Usar getElementById para garantir que encontre o modal
  const modalOverlay = document.getElementById('modalOverlay');
  modalOverlay?.addEventListener("click", (e) => {
    if (e.target === modalOverlay) closeModal();
  });
  
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeModal();
  });

  // Carregar mesas do servidor
  async function loadTables() {
    try {
      console.log('Carregando mesas...');
      
      // Mostra indicador de carregamento
      if (tablesGrid) {
        tablesGrid.innerHTML = '<div style="color: #fca311; text-align: center; padding: 2rem;"><i class="fas fa-spinner fa-spin"></i> Carregando mesas...</div>';
      }
      
      const response = await fetch('/api/tables');
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Erro na resposta:', errorText);
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }
      
      const data = await response.json();
      console.log('Dados recebidos (tipo):', typeof data);
      console.log('Dados recebidos (conteúdo):', data);
      
      // Vamos tentar diferentes formatos de resposta
      let tables = [];
      
      if (data && data.success === true && Array.isArray(data.data)) {
        // Formato: { success: true, data: [...] }
        console.log('Formato ApiResponse detectado');
        tables = data.data;
      } else if (data && data.status === 'success' && Array.isArray(data.data)) {
        // Formato: { status: 'success', data: [...] }
        console.log('Formato status success detectado');
        tables = data.data;
      } else if (Array.isArray(data)) {
        // Formato: [...]
        console.log('Formato array direto detectado');
        tables = data;
      } else if (data && Array.isArray(data.content)) {
        // Formato Page: { content: [...] }
        console.log('Formato Page detectado');
        tables = data.content;
      } else {
        console.log('Tentando acessar propriedades do objeto...');
        // Vamos listar todas as propriedades para debug
        for (let prop in data) {
          console.log(`Propriedade '${prop}':`, data[prop]);
        }
        throw new Error('Formato de resposta não reconhecido');
      }
      
      console.log('Tables processadas:', tables);
      
      // Ordena as mesas por número da mesa
      tables.sort((a, b) => {
        const numA = parseInt(a.tableNumber) || 0;
        const numB = parseInt(b.tableNumber) || 0;
        return numA - numB;
      });
      
      console.log('Tables ordenadas por número:', tables);
      renderTables(tables);
      
    } catch (error) {
      console.error('Erro ao carregar mesas:', error);
      if (tablesGrid) {
        tablesGrid.innerHTML = '<div style="color: #ff6b6b; text-align: center; padding: 2rem;">Erro ao carregar mesas: ' + error.message + '</div>';
      }
    }
  }

  // Renderiza as mesas na interface
  function renderTables(tables) {
    console.log('=== RENDERIZANDO MESAS ===');
    console.log('renderTables chamada com:', tables);
    console.log('Tipo dos dados:', typeof tables);
    console.log('É array?', Array.isArray(tables));
    
    if (!tablesGrid) {
      console.error('Elemento tablesGrid não encontrado!');
      return;
    }
    
    // Converte para array se não for
    if (!Array.isArray(tables)) {
      console.log('Convertendo para array:', tables);
      tables = tables ? [tables] : [];
    }
    
    console.log('Renderizando', tables.length, 'mesas');
    tablesGrid.innerHTML = '';
    
    if (tables.length === 0) {
      tablesGrid.innerHTML = '<div style="color: #888; text-align: center; padding: 2rem;">Nenhuma mesa cadastrada. Clique em "Adicionar mesa" para criar a primeira.</div>';
      return;
    }
    
    tables.forEach((table, index) => {
      try {
        console.log(`=== Mesa ${index + 1} ===`);
        console.log('Dados completos da mesa:', table);
        console.log('Tipo:', typeof table);
        console.log('Propriedades:', Object.keys(table || {}));
        
        // Validações dos dados da mesa
        if (!table || typeof table !== 'object') {
          console.error('Mesa inválida:', table);
          return;
        }
        
        const tableId = table.id || 'N/A';
        const tableNumber = table.tableNumber || 'N/A';
        const capacity = table.capacity || 0;
        const status = table.status || 'DISPONIVEL';
        
        console.log('Valores extraídos:');
        console.log('- ID:', tableId, '(tipo:', typeof tableId, ')');
        console.log('- Número:', tableNumber, '(tipo:', typeof tableNumber, ')');
        console.log('- Capacidade:', capacity, '(tipo:', typeof capacity, ')');
        console.log('- Status:', status, '(tipo:', typeof status, ')');
        
        const statusClass = getStatusClass(status);
        
        const card = document.createElement("div");
        card.className = `table-card ${statusClass}`;
        card.dataset.tableId = tableId;
        card.dataset.tableNumber = tableNumber;
        
        card.innerHTML = `
          <button class="remove-btn" title="Remover">×</button>
          <div class="table-number">#${tableNumber}</div>
          <div class="table-capacity"><i class="fas fa-users"></i> ${capacity} lugares</div>
          <div class="table-status">${getStatusLabel(status)}</div>
          <div class="table-actions">
            <button class="action-btn btn-edit" title="Editar" onclick="editTable('${tableId}')">
              <i class="fas fa-edit"></i>
            </button>
            <button class="action-btn btn-status" title="Alterar Status" onclick="toggleTableStatus(event, '${tableId}', '${status}')">
              <i class="fas fa-exchange-alt"></i>
            </button>
          </div>
        `;
        
        console.log('Card criado para mesa:', tableId, 'com botões de ação');

        // Botão remover com confirmação
        const removeBtn = card.querySelector(".remove-btn");
        removeBtn?.addEventListener("click", () => {
          showConfirm("Tem certeza que deseja remover esta mesa?", () => {
            deleteTable(tableId);
          });
        });



        tablesGrid?.appendChild(card);
      } catch (error) {
        console.error(`Erro ao renderizar mesa ${index + 1}:`, error, table);
      }
    });
  }

  // Salvar mesa (nova ou editada)
  saveBtn?.addEventListener("click", async () => {
    console.log('Tentando salvar mesa...');
    
    const tableNumberInput = document.getElementById('tableNumber');
    const tableCapacityInput = document.getElementById('tableCapacity');
    const tableStatusSelect = document.getElementById('tableStatus');
    
    if (!tableNumberInput || !tableCapacityInput || !tableStatusSelect) {
      alert('Elementos do formulário não encontrados!');
      return;
    }
    
    const num = parseInt(tableNumberInput.value.trim());
    if (!num || isNaN(num) || num <= 0) {
      alert('Por favor, informe um número de mesa válido (maior que 0).');
      return;
    }
    
    const capacity = parseInt(tableCapacityInput.value.trim());
    if (!capacity || isNaN(capacity) || capacity <= 0) {
      alert('Por favor, informe uma quantidade válida de lugares (maior que 0).');
      return;
    }
    
    const status = tableStatusSelect.value;
    
    console.log('Dados a enviar:', { tableNumber: num, capacity: capacity, status: status });

    try {
      const requestBody = {
        tableNumber: num,
        capacity: capacity,
        status: status
      };
      
      console.log('Enviando requisição:', requestBody);
      
      const url = editingTableId ? `/api/tables/${editingTableId}` : '/api/tables';
      const method = editingTableId ? 'PUT' : 'POST';
      
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      });
      
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        let errorMessage = 'Erro ao criar mesa';
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch {
          const errorText = await response.text();
          errorMessage = errorText || errorMessage;
        }
        throw new Error(errorMessage);
      }
      
      const data = await response.json();
      console.log(editingTableId ? 'Mesa atualizada:' : 'Mesa criada:', data);
      
      // Verifica diferentes formatos de sucesso
      const isSuccess = data.success === true || data.status === 'success' || response.status === 200 || response.status === 201;
      
      if (isSuccess) {
        console.log(editingTableId ? 'Mesa atualizada com sucesso!' : 'Mesa criada com sucesso!');
        await loadTables(); // Recarrega todas as mesas
        closeModal();
        // Não mostra alert de sucesso, apenas feedback visual
      } else {
        throw new Error(data.message || 'Erro desconhecido');
      }
    } catch (error) {
      console.error(editingTableId ? 'Erro ao atualizar mesa:' : 'Erro ao criar mesa:', error);
      alert('Não foi possível ' + (editingTableId ? 'atualizar' : 'criar') + ' a mesa: ' + error.message);
    }
  });

  // Excluir mesa
  async function deleteTable(id) {
    try {
      const response = await fetch(`/api/tables/${id}`, {
        method: 'DELETE'
      });
      
      if (!response.ok) {
        throw new Error('Erro ao excluir mesa');
      }
      
      const data = await response.json();
      console.log('Resposta da exclusão:', data);
      
      // Verifica diferentes formatos de sucesso
      const isSuccess = data.success === true || data.status === 'success' || response.status === 200;
      
      if (isSuccess) {
        console.log('Mesa excluída com sucesso!');
        await loadTables(); // Recarrega todas as mesas
      } else {
        throw new Error(data.message || 'Erro desconhecido');
      }
    } catch (error) {
      console.error('Erro ao excluir mesa:', error);
      alert('Não foi possível excluir a mesa.');
    }
  }

  // Funções auxiliares
  function getStatusClass(status) {
    const statusMap = {
      'DISPONIVEL': 'status-vazio',
      'OCUPADA': 'status-ocupado',
      'RESERVADA': 'status-pedido-feito',
      'MANUTENCAO': 'status-pedido-nao-pago'
    };
    return statusMap[status] || 'status-vazio';
  }

  function getStatusLabel(status) {
    const labels = {
      'DISPONIVEL': 'Disponível',
      'OCUPADA': 'Ocupada',
      'RESERVADA': 'Reservada',
      'MANUTENCAO': 'Manutenção'
    };
    return labels[status] || status;
  }

  // Função para editar mesa - GARANTIDA PARA FUNCIONAR
  window.editTable = async function(tableId) {
    console.log('EDITANDO MESA ID:', tableId);
    
    try {
      // 1. BUSCAR DADOS DA MESA
      const response = await fetch(`/api/tables/${tableId}`);
      const data = await response.json();
      console.log('DADOS RECEBIDOS:', data);
      
      // 2. EXTRAIR MESA - suporta ambos formatos
      const table = (data.status === 'success' || data.success) ? data.data : data;
      console.log('MESA:', table);
      
      if (!table || !table.tableNumber) {
        throw new Error('Dados da mesa não encontrados');
      }
      
      // 3. CONFIGURAR EDIÇÃO
      editingTableId = tableId;
      
      // 4. PREENCHER E ABRIR MODAL
      setTimeout(() => {
        // Pegar elementos
        const numeroInput = document.getElementById('tableNumber');
        const capacidadeInput = document.getElementById('tableCapacity');
        const statusSelect = document.getElementById('tableStatus');
        const modal = document.getElementById('modalOverlay');
        const titulo = modal.querySelector('h2');
        
        console.log('ELEMENTOS ENCONTRADOS:', {
          numero: !!numeroInput,
          capacidade: !!capacidadeInput,
          status: !!statusSelect,
          modal: !!modal
        });
        
        // PREENCHER CAMPOS
        numeroInput.value = table.tableNumber || '';
        capacidadeInput.value = table.capacity || '';
        statusSelect.value = table.status || 'DISPONIVEL';
        
        console.log('CAMPOS PREENCHIDOS:', {
          numero: numeroInput.value,
          capacidade: capacidadeInput.value,
          status: statusSelect.value
        });
        
        // ALTERAR TÍTULO E ABRIR
        titulo.textContent = 'Editar Mesa';
        modal.classList.add('open');
        document.body.style.overflow = 'hidden';
        
        console.log('MODAL ABERTO PARA EDIÇÃO');
      }, 100);
      
    } catch (error) {
      console.error('ERRO:', error);
      alert('Erro ao carregar mesa: ' + error.message);
    }
  };

  // Função para alterar status da mesa
  window.toggleTableStatus = function(event, tableId, currentStatus) {
    event.stopPropagation();
    console.log('Toggle status para mesa:', tableId, 'Status atual:', currentStatus);
    
    // Remover menu anterior se existir
    const existingMenu = document.querySelector('.status-menu.show');
    if (existingMenu) {
      existingMenu.classList.remove('show');
      setTimeout(() => existingMenu.remove(), 200);
      return;
    }
    
    const statusOptions = [
      { value: 'DISPONIVEL', label: 'Disponível', class: 'disponivel' },
      { value: 'OCUPADA', label: 'Ocupado', class: 'ocupado' },
      { value: 'RESERVADA', label: 'Reservada', class: 'reservado' },
      { value: 'MANUTENCAO', label: 'Manutenção', class: 'manutencao' }
    ];
    
    // Criar menu de status
    const menu = document.createElement('div');
    menu.className = 'status-menu';
    menu.innerHTML = statusOptions
      .filter(option => option.value !== currentStatus)
      .map(option => `
        <button class="status-menu-item ${option.class}" onclick="updateTableStatus(${tableId}, '${option.value}')">
          ${option.label}
        </button>
      `).join('');
    
    // Posicionar menu próximo ao botão
    const button = event.target.closest('.action-btn');
    const rect = button.getBoundingClientRect();
    menu.style.position = 'fixed';
    menu.style.top = `${rect.bottom + 5}px`;
    menu.style.left = `${rect.left - 50}px`;
    menu.style.zIndex = '1000';
    
    document.body.appendChild(menu);
    
    // Mostrar menu com animação
    setTimeout(() => menu.classList.add('show'), 10);
    
    // Fechar menu ao clicar fora
    setTimeout(() => {
      document.addEventListener('click', function closeMenu() {
        menu.classList.remove('show');
        setTimeout(() => menu.remove(), 200);
        document.removeEventListener('click', closeMenu);
      });
    }, 100);
  };

  // Função para atualizar status da mesa
  window.updateTableStatus = async function(tableId, newStatus) {
    try {
      const response = await fetch(`/api/tables/${tableId}/status?status=${newStatus}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Erro ao atualizar status');
      }

      console.log('Status da mesa atualizado com sucesso!');
      await loadTables(); // Recarrega as mesas
      
      // Remover menu de status se existir
      const menu = document.querySelector('.status-menu');
      if (menu) {
        menu.classList.remove('show');
        setTimeout(() => menu.remove(), 200);
      }
      
    } catch (error) {
      console.error('Erro ao atualizar status da mesa:', error);
      alert('Erro ao atualizar status da mesa: ' + error.message);
    }
  };

});
