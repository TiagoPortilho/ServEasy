/**
 * Menu Manager - Gerencia a exibição, adição, edição e exclusão de itens do menu
 * Implementado com princípios SOLID
 */

// Aguardar o JWT interceptor carregar antes de inicializar
document.addEventListener('DOMContentLoaded', function() {
    // Aguardar o JWT interceptor carregar antes de fazer requisições
    waitForJwtInterceptor().then(() => {
        console.log('[menu.js] JWT Interceptor carregado, iniciando aplicação...');
        initializeMenuManager();
    });
});

// Função para aguardar o JWT interceptor carregar
function waitForJwtInterceptor() {
    return new Promise((resolve) => {
        if (window.jwtInterceptorLoaded) {
            console.log('[menu.js] JWT Interceptor já estava carregado');
            resolve();
            return;
        }

        console.log('[menu.js] Aguardando JWT Interceptor carregar...');
        const checkInterval = setInterval(() => {
            if (window.jwtInterceptorLoaded) {
                console.log('[menu.js] JWT Interceptor carregado com sucesso');
                clearInterval(checkInterval);
                resolve();
            }
        }, 100);

        // Timeout de segurança (5 segundos)
        setTimeout(() => {
            if (!window.jwtInterceptorLoaded) {
                console.warn('[menu.js] Timeout aguardando JWT Interceptor, continuando mesmo assim');
                clearInterval(checkInterval);
                resolve();
            }
        }, 5000);
    });
}

// Módulo de API - Responsável pela comunicação com o servidor
const MenuAPI = {
  async getMenuItems() {
    try {
      const response = await fetch('/api/menu');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();
      return result.status === 'success' ? result.data : [];
    } catch (error) {
      console.error('Erro ao carregar menu:', error);
      throw error;
    }
  },

  async getMenuItem(id) {
    try {
      const response = await fetch(`/api/menu/${id}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();
      return result.status === 'success' ? result.data : null;
    } catch (error) {
      console.error('Erro ao carregar item:', error);
      throw error;
    }
  },

  async saveMenuItem(menuItem, isEditing) {
    try {
      const url = isEditing ? `/api/menu/${menuItem.id}` : '/api/menu';
      const method = isEditing ? 'PUT' : 'POST';
      
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(menuItem)
      });

      const result = await response.json();
      if (result.status !== 'success') {
        throw new Error(result.message || 'Erro ao salvar item');
      }
      
      return result.data;
    } catch (error) {
      console.error('Erro ao salvar item:', error);
      throw error;
    }
  },

  async toggleAvailability(id) {
    try {
      const response = await fetch(`/api/menu/${id}/toggle-availability`, {
        method: 'PATCH'
      });
      
      const result = await response.json();
      if (result.status !== 'success') {
        throw new Error(result.message || 'Erro ao alterar disponibilidade');
      }
      
      return true;
    } catch (error) {
      console.error('Erro ao alterar disponibilidade:', error);
      throw error;
    }
  },

  async deleteMenuItem(id) {
    try {
      const response = await fetch(`/api/menu/${id}`, {
        method: 'DELETE'
      });
      
      const result = await response.json();
      if (result.status !== 'success') {
        throw new Error(result.message || 'Erro ao excluir item');
      }
      
      return true;
    } catch (error) {
      console.error('Erro ao excluir item:', error);
      throw error;
    }
  },

  async checkStock() {
    try {
      const response = await fetch('/api/menu/check-stock');
      const result = await response.json();
      return result.status === 'success' && result.data;
    } catch (error) {
      console.error('Erro ao verificar estoque:', error);
      return false;
    }
  },

  async getStockItems() {
    try {
      const response = await fetch('/api/stock');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();
      return result.status === 'success' ? result.data : [];
    } catch (error) {
      console.error('Erro ao carregar estoque:', error);
      throw error;
    }
  }
};

// Módulo de processamento de imagem - Responsável pelo processamento e compressão de imagens
const ImageProcessor = {
  async processFile(file) {
    return new Promise((resolve, reject) => {
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      const img = new Image();
      
      img.onload = () => {
        // Calcular dimensões otimizadas
        const { width, height } = this.calculateOptimalDimensions(img.width, img.height);
        
        canvas.width = width;
        canvas.height = height;
        
        // Desenhar imagem redimensionada
        ctx.drawImage(img, 0, 0, width, height);
        
        // Determinar qualidade baseada no tamanho
        const quality = file.size > 10 * 1024 * 1024 ? 0.7 : 0.85;
        
        // Converter para base64 com compressão
        const compressedDataUrl = canvas.toDataURL('image/jpeg', quality);
        resolve(compressedDataUrl);
      };
      
      img.onerror = () => reject(new Error('Falha ao carregar imagem'));
      
      const reader = new FileReader();
      reader.onload = e => img.src = e.target.result;
      reader.onerror = () => reject(new Error('Falha ao ler arquivo'));
      reader.readAsDataURL(file);
    });
  },
  
  calculateOptimalDimensions(originalWidth, originalHeight) {
    const MAX_WIDTH = 1920;
    const MAX_HEIGHT = 1080;
    
    let width = originalWidth;
    let height = originalHeight;
    
    if (width > MAX_WIDTH || height > MAX_HEIGHT) {
      const aspectRatio = width / height;
      
      if (width > height) {
        width = MAX_WIDTH;
        height = Math.round(width / aspectRatio);
      } else {
        height = MAX_HEIGHT;
        width = Math.round(height * aspectRatio);
      }
    }
    
    return { width, height };
  },
  
  getDefaultImage() {
    return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2Y1ZjVmNSIvPgogIDx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiM5OTk5OTkiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj5JbWFnZW0gZG8gUHJhdG88L3RleHQ+Cjwvc3ZnPg==';
  }
};

// Módulo de formatação - Responsável por formatar textos e valores
const FormatHelper = {
  formatarUnidade(quantidade, unidade) {
    const qtd = quantidade % 1 === 0 ? quantidade.toString() : quantidade.toFixed(1);
    
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
    
    if (['g', 'kg', 'ml', 'L'].includes(unidadeFormatada)) {
      return `${qtd}${unidadeFormatada}`;
    } else {
      return quantidade == 1 ? `${qtd} ${unidadeFormatada}` : `${qtd} ${unidadeFormatada}`;
    }
  },

  formatarUnidadeParaExibicao(unidade) {
    const unidadesFormatadas = {
      'g': 'gramas',
      'kg': 'quilogramas', 
      'ml': 'mililitros',
      'l': 'litros',
      'un': 'unidades',
      'unidades': 'unidades',
      'unidade': 'unidades',
      'fatia': 'fatias',
      'fatias': 'fatias',
      'colher': 'colheres',
      'colheres': 'colheres',
      'xícara': 'xícaras',
      'xícaras': 'xícaras',
      'pitada': 'pitadas',
      'pitadas': 'pitadas'
    };
    
    return unidadesFormatadas[unidade?.toLowerCase()] || unidade;
  },

  getCategoryDisplayName(category) {
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
};

// Módulo de UI - Responsável pela renderização e interação com o usuário
const MenuUI = {
  elements: {
    menuGrid: null,
    modal: null,
    addBtn: null,
    cancelBtn: null,
    saveBtn: null,
    nameInput: null,
    priceInput: null,
    categorySelect: null,
    imageInput: null,
    imgPreview: null,
    previewName: null,
    stockAlert: null,
    ingredientsSection: null,
    addIngredientBtn: null,
    ingredientsList: null
  },

  initialize() {
    // Capturar elementos DOM uma única vez
    this.elements.menuGrid = document.querySelector(".menu-grid");
    this.elements.modal = document.getElementById("modalOverlay");
    this.elements.addBtn = document.querySelector(".add-btn");
    this.elements.cancelBtn = document.getElementById("cancelBtn");
    this.elements.saveBtn = document.getElementById("saveBtn");
    this.elements.nameInput = document.getElementById("dishName");
    this.elements.priceInput = document.getElementById("dishPrice");
    this.elements.categorySelect = document.getElementById("dishCategory");
    this.elements.imageInput = document.getElementById("dishImage");
    this.elements.imgPreview = document.getElementById("imgPreview");
    this.elements.previewName = document.getElementById("previewName");
    this.elements.stockAlert = document.getElementById("stockAlert");
    this.elements.ingredientsSection = document.getElementById("ingredientsSection");
    this.elements.addIngredientBtn = document.getElementById("addIngredientBtn");
    this.elements.ingredientsList = document.getElementById("ingredientsList");

    // Configurar event listeners
    if (this.elements.imageInput) {
      this.elements.imageInput.addEventListener('change', this.handleImageChange.bind(this));
    }
  },

  async handleImageChange(e) {
    const file = e.target.files[0];
    if (!file) {
      this.resetImagePreview();
      return;
    }

    // Validar tipo de arquivo
    if (!file.type.startsWith('image/')) {
      NotificationManager.showMessage('Selecione apenas arquivos de imagem (JPG, PNG, GIF, WEBP, etc.)', 'error');
      e.target.value = '';
      this.resetImagePreview();
      return;
    }
    
    // Validar tamanho
    if (file.size > 50 * 1024 * 1024) {
      NotificationManager.showMessage('A imagem deve ter no máximo 50MB', 'error');
      e.target.value = '';
      this.resetImagePreview();
      return;
    }

    try {
      // Processar imagem
      const compressedImageUrl = await ImageProcessor.processFile(file);
      const { imgPreview, previewName } = this.elements;
      
      if (imgPreview) {
        imgPreview.src = compressedImageUrl;
        imgPreview.style.display = 'block';
      }
      
      if (previewName) {
        const sizeKB = Math.round(compressedImageUrl.length * 0.75 / 1024);
        previewName.textContent = `${file.name} (${sizeKB}KB comprimida)`;
      }
    } catch (error) {
      console.error('Erro ao processar imagem:', error);
      NotificationManager.showMessage('Erro ao processar a imagem', 'error');
      this.resetImagePreview();
    }
  },

  resetImagePreview() {
    const { imgPreview, previewName } = this.elements;
    
    if (imgPreview) {
      imgPreview.style.display = 'none';
      imgPreview.src = '';
    }
    
    if (previewName) {
      previewName.textContent = 'Nenhuma imagem selecionada';
    }
  },

  resetForm() {
    const { nameInput, priceInput, categorySelect, imageInput } = this.elements;
    
    if (nameInput) nameInput.value = '';
    if (priceInput) priceInput.value = '';
    if (categorySelect) categorySelect.value = '';
    if (imageInput) imageInput.value = '';
    
    this.resetImagePreview();
    this.clearIngredients();
  },

  clearIngredients() {
    const { ingredientsList } = this.elements;
    if (ingredientsList) {
      ingredientsList.innerHTML = '';
      this.updateIngredientsState();
    }
  },

  updateIngredientsState() {
    const { ingredientsList } = this.elements;
    if (!ingredientsList) return;

    const items = ingredientsList.querySelectorAll('.ingredient-item');
    if (items.length === 0) {
      ingredientsList.innerHTML = '<div class="empty-ingredients">Nenhum ingrediente adicionado</div>';
    } else {
      const emptyMsg = ingredientsList.querySelector('.empty-ingredients');
      if (emptyMsg) {
        emptyMsg.remove();
      }
    }
  },

  renderMenuItems(items) {
    const { menuGrid } = this.elements;
    if (!menuGrid) return;
    
    menuGrid.innerHTML = '';
    
    items.forEach(item => {
      const card = this.createMenuItemCard(item);
      menuGrid.appendChild(card);
    });
  },

  createMenuItemCard(item) {
    const card = document.createElement('div');
    card.className = 'dish-card';
    
    // Trata a URL da imagem
    const imageUrl = item.imageUrl && item.imageUrl.trim() !== '' ? item.imageUrl : ImageProcessor.getDefaultImage();
    
    // Criar descrição baseada nos ingredientes
    let descriptionHtml = this.createIngredientDescription(item);
    
    card.innerHTML = `
      <button class="remove-btn" title="Remover" onclick="MenuController.deleteItem(${item.id})">×</button>
      <img src="${imageUrl}" alt="${item.name}" class="dish-img" onerror="this.src='${ImageProcessor.getDefaultImage()}'" />
      <h2 class="dish-name">${item.name}</h2>
      <div class="dish-price">R$ ${item.price.toFixed(2)}</div>
      ${descriptionHtml}
      <div class="dish-category">Categoria: ${FormatHelper.getCategoryDisplayName(item.category)}</div>
      <div class="dish-actions">
        <button class="btn-edit" onclick="MenuController.editItem(${item.id})">Editar</button>
        <button class="btn-toggle ${!item.isAvailable ? 'disabled' : ''}" onclick="MenuController.toggleAvailability(${item.id})">
          ${item.isAvailable ? 'Desativar' : 'Ativar'}
        </button>
      </div>
    `;
    return card;
  },

  createIngredientDescription(item) {
    if (!item.ingredients || item.ingredients.length === 0) {
      return `<p class="dish-description"><em>Delicioso prato preparado especialmente para você.</em></p>`;
    }
    
    // Agrupar ingredientes para apresentação
    const ingredientsList = item.ingredients.map(ing => {
      const stockItem = MenuController.stockItems.find(s => s.id === ing.stockItemId);
      const itemName = stockItem ? stockItem.name : ing.stockItemName || 'Item não encontrado';
      
      if (ing.showUnit !== false) {
        const unidadeFormatada = FormatHelper.formatarUnidade(ing.quantity, ing.unit);
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
    
    return `<p class="dish-description">${description}</p>`;
  },

  addIngredient() {
    const { ingredientsList } = this.elements;
    if (!ingredientsList) return;

    const ingredientDiv = document.createElement('div');
    ingredientDiv.className = 'ingredient-item';
    ingredientDiv.innerHTML = `
      <div class="ingredient-select">
        <label>Ingrediente</label>
        <select class="ingredient-stock-select" required onchange="MenuUI.updateIngredientUnit(this)">
          <option value="">Selecione um item do estoque</option>
          ${MenuController.stockItems.map(item => {
            const unidadeExibicao = FormatHelper.formatarUnidadeParaExibicao(item.unit);
            return `<option value="${item.id}" data-unit="${item.unit}">${item.name} (${unidadeExibicao})</option>`;
          }).join('')}
        </select>
      </div>
      <div class="ingredient-quantity">
        <label>Quantidade</label>
        <input type="number" class="ingredient-quantity-input" placeholder="0" step="0.001" min="0" required>
      </div>
      <div class="ingredient-unit">
        <label>Unidade</label>
        <input type="text" class="ingredient-unit-input" placeholder="Selecione um ingrediente" readonly disabled>
      </div>
      <div class="ingredient-show-unit">
        <label class="checkbox-label">
          <input type="checkbox" class="ingredient-show-unit-checkbox" checked>
          <span class="checkmark"></span>
          Mostrar unidade
        </label>
      </div>
      <div class="ingredient-actions">
        <button type="button" class="btn-remove-ingredient" onclick="MenuUI.removeIngredient(this)">×</button>
      </div>
    `;
    
    ingredientsList.appendChild(ingredientDiv);
    this.updateIngredientsState();
  },

  updateIngredientUnit(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const unitInput = selectElement.closest('.ingredient-item').querySelector('.ingredient-unit-input');
    
    if (selectedOption.value && selectedOption.dataset.unit) {
      const unidadeFormatada = FormatHelper.formatarUnidadeParaExibicao(selectedOption.dataset.unit);
      unitInput.value = unidadeFormatada;
      unitInput.style.backgroundColor = '#2a2a2a';
      unitInput.style.color = '#fff';
    } else {
      unitInput.value = '';
      unitInput.placeholder = 'Selecione um ingrediente';
    }
  },

  removeIngredient(button) {
    const ingredientItem = button.closest('.ingredient-item');
    ingredientItem.remove();
    this.updateIngredientsState();
  },

  collectIngredients() {
    const { ingredientsList } = this.elements;
    if (!ingredientsList) return [];

    const ingredients = [];
    const items = ingredientsList.querySelectorAll('.ingredient-item');
    
    items.forEach(item => {
      const stockSelect = item.querySelector('.ingredient-stock-select');
      const quantityInput = item.querySelector('.ingredient-quantity-input');
      const showUnitCheckbox = item.querySelector('.ingredient-show-unit-checkbox');
      
      if (stockSelect.value && quantityInput.value) {
        const stockItem = MenuController.stockItems.find(s => s.id == stockSelect.value);
        ingredients.push({
          stockItemId: parseInt(stockSelect.value),
          stockItemName: stockItem ? stockItem.name : '',
          quantity: parseFloat(quantityInput.value),
          unit: stockItem ? stockItem.unit : '',
          showUnit: showUnitCheckbox ? showUnitCheckbox.checked : true
        });
      }
    });
    
    return ingredients;
  },

  loadIngredients(ingredients) {
    const { ingredientsList } = this.elements;
    if (!ingredientsList) return;
    
    ingredientsList.innerHTML = '';
    
    if (ingredients && ingredients.length > 0) {
      ingredients.forEach(ingredient => {
        const stockItem = MenuController.stockItems.find(s => s.id == ingredient.stockItemId);
        const ingredientDiv = document.createElement('div');
        ingredientDiv.className = 'ingredient-item';
        ingredientDiv.innerHTML = `
          <div class="ingredient-select">
            <label>Ingrediente</label>
            <select class="ingredient-stock-select" required onchange="MenuUI.updateIngredientUnit(this)">
              <option value="">Selecione um item do estoque</option>
              ${MenuController.stockItems.map(item => 
                `<option value="${item.id}" data-unit="${item.unit}" ${item.id == ingredient.stockItemId ? 'selected' : ''}>${item.name} (${FormatHelper.formatarUnidadeParaExibicao(item.unit)})</option>`
              ).join('')}
            </select>
          </div>
          <div class="ingredient-quantity">
            <label>Quantidade</label>
            <input type="number" class="ingredient-quantity-input" value="${ingredient.quantity}" step="0.001" min="0" required>
          </div>
          <div class="ingredient-unit">
            <label>Unidade</label>
            <input type="text" class="ingredient-unit-input" value="${stockItem ? FormatHelper.formatarUnidadeParaExibicao(stockItem.unit) : ingredient.unit}" readonly disabled>
          </div>
          <div class="ingredient-show-unit">
            <label class="checkbox-label">
              <input type="checkbox" class="ingredient-show-unit-checkbox" ${ingredient.showUnit !== false ? 'checked' : ''}>
              <span class="checkmark"></span>
              Mostrar unidade
            </label>
          </div>
          <div class="ingredient-actions">
            <button type="button" class="btn-remove-ingredient" onclick="MenuUI.removeIngredient(this)">×</button>
          </div>
        `;
        
        ingredientsList.appendChild(ingredientDiv);
      });
    }
    
    this.updateIngredientsState();
  }
};

// Módulo para gerenciar o modal
const ModalManager = {
  elements: null,
  editingItemId: null,

  initialize(elements) {
    this.elements = elements;
    this.editingItemId = null;
    
    // Configurar event listeners para o modal
    elements.addBtn?.addEventListener('click', this.openAddModal.bind(this));
    elements.cancelBtn?.addEventListener('click', this.closeModal.bind(this));
    elements.saveBtn?.addEventListener('click', MenuController.saveItem.bind(MenuController));
    
    // Event listener para adicionar ingrediente
    if (elements.addIngredientBtn && !elements.addIngredientBtn.dataset.listenerAdded) {
      elements.addIngredientBtn.addEventListener('click', () => MenuUI.addIngredient());
      elements.addIngredientBtn.dataset.listenerAdded = 'true';
    }
    
    // Fechar modal ao clicar fora dele
    elements.modal?.addEventListener('click', (e) => {
      if (e.target === elements.modal) {
        this.closeModal();
      }
    });
    
    // Fechar modal com tecla Escape
    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && elements.modal?.classList.contains('open')) {
        this.closeModal();
      }
    });
  },

  openAddModal() {
    this.editingItemId = null;
    MenuUI.resetForm();
    this.elements.modal.querySelector('#modalTitle').textContent = 'Adicionar Prato';
    MenuUI.updateIngredientsState();
    this.openModal();
  },

  openEditModal(id) {
    this.editingItemId = id;
    this.elements.modal.querySelector('#modalTitle').textContent = 'Editar Prato';
    this.openModal();
  },

  openModal() {
    const { modal } = this.elements;
    if (modal) {
      modal.classList.add('open');
      document.body.style.overflow = 'hidden';
    }
  },

  closeModal() {
    const { modal } = this.elements;
    if (modal) {
      modal.classList.remove('open');
      document.body.style.overflow = '';
      this.editingItemId = null;
    }
  }
};

// Módulo para gerenciar notificações
const NotificationManager = {
  showMessage(message, type = 'info') {
    // Criar toast simples
    const toast = document.createElement('div');
    toast.className = `toast-message ${type}`;
    toast.textContent = message;
    toast.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: ${type === 'error' ? '#d32f2f' : '#333'};
      color: white;
      padding: 12px 20px;
      border-radius: 6px;
      z-index: 10000;
      box-shadow: 0 4px 12px rgba(0,0,0,0.3);
      animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(toast);
    
    setTimeout(() => {
      toast.style.animation = 'slideOut 0.3s ease';
      setTimeout(() => document.body.removeChild(toast), 300);
    }, 3000);
  },

  addAnimationStyles() {
    // Adicionar estilos CSS para animações
    if (!document.getElementById('notification-styles')) {
      const style = document.createElement('style');
      style.id = 'notification-styles';
      style.textContent = `
        @keyframes slideIn {
          from { transform: translateX(100%); opacity: 0; }
          to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOut {
          from { transform: translateX(0); opacity: 1; }
          to { transform: translateX(100%); opacity: 0; }
        }
      `;
      document.head.appendChild(style);
    }
  }
};

// Controller principal - Gerencia o fluxo de dados e ações
const MenuController = {
  stockItems: [],
  
  async initialize() {
    console.log("[menuManager.js] Inicializando...");
    
    // Inicializar UI e notificações
    MenuUI.initialize();
    NotificationManager.addAnimationStyles();
    
    // Inicializar modal
    ModalManager.initialize(MenuUI.elements);
    
    // Configurar métodos globais
    window.MenuUI = MenuUI;
    window.MenuController = this;
    
    // Inicializar sistema
    await this.initializeSystem();
  },

  async initializeSystem() {
    await this.loadMenuItems();
    
    // Verificar estoque
    const hasStock = await MenuAPI.checkStock();
    if (hasStock) {
      await this.loadStockItems();
      this.updateStockAvailability(true);
    } else {
      this.updateStockAvailability(false);
    }
  },

  updateStockAvailability(available) {
    const { stockAlert, ingredientsSection } = MenuUI.elements;
    
    if (stockAlert) stockAlert.style.display = available ? 'none' : 'block';
    if (ingredientsSection) ingredientsSection.style.display = available ? 'block' : 'none';
  },

  async loadMenuItems() {
    try {
      const items = await MenuAPI.getMenuItems();
      MenuUI.renderMenuItems(items);
      return true;
    } catch (error) {
      NotificationManager.showMessage('Erro ao carregar itens do menu', 'error');
      return false;
    }
  },

  async loadStockItems() {
    try {
      this.stockItems = await MenuAPI.getStockItems();
      return true;
    } catch (error) {
      NotificationManager.showMessage('Erro ao carregar itens do estoque', 'error');
      return false;
    }
  },

  async editItem(id) {
    try {
      const item = await MenuAPI.getMenuItem(id);
      if (!item) {
        throw new Error('Item não encontrado');
      }
      
      const { nameInput, priceInput, categorySelect, imgPreview, previewName } = MenuUI.elements;
      
      // Configurar dados do formulário
      nameInput.value = item.name;
      priceInput.value = item.price;
      if (categorySelect) categorySelect.value = item.category;
      
      // Carregar ingredientes
      MenuUI.loadIngredients(item.ingredients || []);
      
      // Mostrar imagem atual
      if (item.imageUrl) {
        imgPreview.src = item.imageUrl;
        imgPreview.style.display = 'block';
        previewName.textContent = 'Imagem atual';
      } else {
        imgPreview.style.display = 'none';
        previewName.textContent = 'Nenhuma imagem selecionada';
      }
      
      // Abrir modal em modo edição
      ModalManager.openEditModal(id);
      
    } catch (error) {
      console.error('Erro ao carregar item:', error);
      NotificationManager.showMessage('Erro ao carregar item para edição', 'error');
    }
  },

  async toggleAvailability(id) {
    try {
      await MenuAPI.toggleAvailability(id);
      await this.loadMenuItems();
      NotificationManager.showMessage('Disponibilidade alterada com sucesso');
    } catch (error) {
      NotificationManager.showMessage('Erro ao alterar disponibilidade', 'error');
    }
  },

  async deleteItem(id) {
    if (!confirm('Tem certeza que deseja excluir este item?')) return;
    
    try {
      await MenuAPI.deleteMenuItem(id);
      await this.loadMenuItems();
      NotificationManager.showMessage('Item excluído com sucesso');
    } catch (error) {
      NotificationManager.showMessage('Erro ao excluir item', 'error');
    }
  },

  async saveItem() {
    const { nameInput, priceInput, categorySelect, imageInput, imgPreview } = MenuUI.elements;
    
    // Validar campos obrigatórios
    if (!nameInput?.value.trim()) {
      NotificationManager.showMessage('O nome do prato é obrigatório', 'error');
      nameInput?.focus();
      return;
    }

    if (!priceInput?.value || parseFloat(priceInput.value) <= 0) {
      NotificationManager.showMessage('O preço deve ser maior que zero', 'error');
      priceInput?.focus();
      return;
    }

    if (!categorySelect?.value) {
      NotificationManager.showMessage('Selecione uma categoria', 'error');
      categorySelect?.focus();
      return;
    }

    // Validar ingredientes
    const ingredients = MenuUI.collectIngredients();
    if (ingredients.length === 0) {
      NotificationManager.showMessage('Adicione pelo menos um ingrediente', 'error');
      return;
    }

    const menuItemDto = {
      id: ModalManager.editingItemId,
      name: nameInput.value.trim(),
      price: parseFloat(priceInput.value),
      category: categorySelect.value,
      isAvailable: true,
      ingredients: ingredients
    };

    // Adicionar imagem se foi selecionada
    if (imageInput?.files && imageInput.files[0]) {
      try {
        // Usar a imagem já processada e comprimida do preview
        if (imgPreview && imgPreview.src && !imgPreview.src.includes('data:image/svg')) {
          menuItemDto.imageUrl = imgPreview.src;
        } else {
          const imageBase64 = await ImageProcessor.processFile(imageInput.files[0]);
          menuItemDto.imageUrl = imageBase64;
        }
      } catch (error) {
        console.error('Erro ao processar imagem:', error);
        NotificationManager.showMessage('Erro ao processar a imagem', 'error');
        return;
      }
    } else if (ModalManager.editingItemId) {
      // Se estamos editando e não há nova imagem, manter a imagem atual
      const currentImg = imgPreview.src;
      if (currentImg && !currentImg.includes('data:image/svg')) {
        menuItemDto.imageUrl = currentImg;
      }
    }

    try {
      await MenuAPI.saveMenuItem(menuItemDto, ModalManager.editingItemId !== null);
      await this.loadMenuItems();
      ModalManager.closeModal();
      NotificationManager.showMessage(
        ModalManager.editingItemId ? 'Prato atualizado com sucesso' : 'Prato adicionado com sucesso'
      );
    } catch (error) {
      NotificationManager.showMessage(`Erro ao salvar: ${error.message}`, 'error');
    }
  }
};

// Inicialização
// REMOVIDO: document.addEventListener("DOMContentLoaded", () => {
//   MenuController.initialize();
// });

// Função para inicializar o menu manager após JWT interceptor
function initializeMenuManager() {
    MenuController.initialize();
    
    // Configurar botão de logout
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function(e) {
            e.preventDefault();
            if (window.logout) {
                window.logout();
            } else {
                // Fallback caso a função global não esteja disponível
                localStorage.removeItem('jwt_token');
                localStorage.removeItem('user_role'); 
                localStorage.removeItem('username');
                window.location.href = '/login';
            }
        });
    }
}