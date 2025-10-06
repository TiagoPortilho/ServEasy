// Menu Admin - Apenas dados reais da API
document.addEventListener("DOMContentLoaded", async () => {
  console.log("[menu.js] carregado");

  const addBtn = document.querySelector(".add-btn");
  const modal = document.getElementById("modalOverlay");
  const cancelBtn = document.getElementById("cancelBtn");
  const saveBtn = document.getElementById("saveBtn");
  const menuGrid = document.querySelector(".menu-grid");

  const nameInput = document.getElementById("dishName");
  const priceInput = document.getElementById("dishPrice");
  const categorySelect = document.getElementById("dishCategory");
  const imageInput = document.getElementById("dishImage");
  const imgPreview = document.getElementById("imgPreview");
  const previewName = document.getElementById("previewName");
  
  // Elementos para ingredientes
  const stockAlert = document.getElementById("stockAlert");
  const ingredientsSection = document.getElementById("ingredientsSection");
  const addIngredientBtn = document.getElementById("addIngredientBtn");
  const ingredientsList = document.getElementById("ingredientsList");

  let editingItemId = null;
  let stockItems = [];
  let currentIngredients = [];

  // Função para formatar unidades de medida de forma mais elegante
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

  // Função para formatar unidades apenas para exibição (sem quantidade)
  function formatarUnidadeParaExibicao(unidade) {
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
    
    return unidadesFormatadas[unidade.toLowerCase()] || unidade;
  }

  // Inicializar sistema
  await inicializarSistema();

  // Event listeners
  addBtn?.addEventListener('click', abrirModalAdicionar);
  addIngredientBtn?.addEventListener('click', adicionarIngrediente);
  cancelBtn?.addEventListener('click', fecharModal);

  // Inicializar sistema
  async function inicializarSistema() {
    await carregarMenuItems();
    
    // Verificar se há itens no estoque
    const temEstoque = await verificarEstoque();
    if (temEstoque) {
      await carregarEstoque();
      if (stockAlert) stockAlert.style.display = 'none';
      if (ingredientsSection) ingredientsSection.style.display = 'block';
    } else {
      if (stockAlert) stockAlert.style.display = 'block';
      if (ingredientsSection) ingredientsSection.style.display = 'none';
    }
  }

  // Abrir modal para adicionar
  function abrirModalAdicionar() {
    editingItemId = null;
    limparFormulario();
    document.getElementById('modalTitle').textContent = 'Adicionar Prato';
    atualizarEstadoIngredientes();
    abrirModal();
  }

  // Limpar formulário
  function limparFormulario() {
    if (nameInput) nameInput.value = '';
    if (priceInput) priceInput.value = '';
    if (categorySelect) categorySelect.value = '';
    if (imageInput) imageInput.value = '';
    if (imgPreview) {
      imgPreview.style.display = 'none';
      imgPreview.src = '';
    }
    if (previewName) previewName.textContent = 'Nenhuma imagem selecionada';
    
    // Limpar ingredientes
    if (ingredientsList) {
      ingredientsList.innerHTML = '';
      atualizarEstadoIngredientes();
    }
  }

  // Abrir modal
  function abrirModal() {
    if (modal) {
      modal.classList.add('open');
      document.body.style.overflow = 'hidden';
    }
  }

  // Fechar modal
  function fecharModal() {
    if (modal) {
      modal.classList.remove('open');
      document.body.style.overflow = '';
      editingItemId = null;
    }
  }

  // Carregar itens do menu da API
  async function carregarMenuItems() {
    try {
      const response = await fetch('/api/menu');
      const result = await response.json();
      
      if (result.status === 'success') {
        exibirMenuItems(result.data);
      } else {
        console.error('Erro ao carregar menu:', result.message);
        mostrarMensagem('Erro ao carregar itens do menu');
      }
    } catch (error) {
      console.error('Erro na requisição:', error);
      mostrarMensagem('Erro de conexão ao carregar menu');
    }
  }

  // Verificar se há itens no estoque
  async function verificarEstoque() {
    try {
      const response = await fetch('/api/menu/check-stock');
      const result = await response.json();
      
      if (result.status === 'success') {
        return result.data;
      }
      return false;
    } catch (error) {
      console.error('Erro ao verificar estoque:', error);
      return false;
    }
  }

  // Carregar itens do estoque
  async function carregarEstoque() {
    try {
      const response = await fetch('/api/stock');
      const result = await response.json();
      
      if (result.status === 'success') {
        stockItems = result.data;
        return true;
      }
      return false;
    } catch (error) {
      console.error('Erro ao carregar estoque:', error);
      return false;
    }
  }

  // Função para obter imagem padrão
  function getDefaultImage() {
    return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2Y1ZjVmNSIvPgogIDx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiM5OTk5OTkiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj5JbWFnZW0gZG8gUHJhdG88L3RleHQ+Cjwvc3ZnPg==';
  }

  // Função para processar e comprimir imagens profissionais
  async function processImageFile(file) {
    return new Promise((resolve, reject) => {
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      const img = new Image();
      
      img.onload = function() {
        // Determinar dimensões otimizadas
        let { width, height } = calculateOptimalDimensions(img.width, img.height);
        
        canvas.width = width;
        canvas.height = height;
        
        // Desenhar imagem redimensionada
        ctx.drawImage(img, 0, 0, width, height);
        
        // Determinar qualidade baseada no tamanho original
        const quality = file.size > 10 * 1024 * 1024 ? 0.7 : 0.85; // 70% para imagens > 10MB, 85% para menores
        
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
  }
  
  // Função para calcular dimensões otimizadas
  function calculateOptimalDimensions(originalWidth, originalHeight) {
    const MAX_WIDTH = 1920;  // Full HD width para qualidade profissional
    const MAX_HEIGHT = 1080; // Full HD height
    
    let width = originalWidth;
    let height = originalHeight;
    
    // Redimensionar apenas se necessário, mantendo proporção
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
  }

  // Função para processar e comprimir imagens profissionais
  async function processImageFile(file) {
    return new Promise((resolve, reject) => {
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      const img = new Image();
      
      img.onload = function() {
        // Determinar dimensões otimizadas
        let { width, height } = calculateOptimalDimensions(img.width, img.height);
        
        canvas.width = width;
        canvas.height = height;
        
        // Desenhar imagem redimensionada
        ctx.drawImage(img, 0, 0, width, height);
        
        // Determinar qualidade baseada no tamanho original
        const quality = file.size > 10 * 1024 * 1024 ? 0.7 : 0.85; // 70% para imagens > 10MB, 85% para menores
        
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
  }
  
  // Função para calcular dimensões otimizadas
  function calculateOptimalDimensions(originalWidth, originalHeight) {
    const MAX_WIDTH = 1920;  // Full HD width para qualidade profissional
    const MAX_HEIGHT = 1080; // Full HD height
    
    let width = originalWidth;
    let height = originalHeight;
    
    // Redimensionar apenas se necessário, mantendo proporção
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
  }

  // Função para converter enum para texto legível
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

  function exibirMenuItems(items) {
    if (!menuGrid) return;
    
    menuGrid.innerHTML = '';
    
    items.forEach(item => {
      const card = criarCardMenuItem(item);
      menuGrid.appendChild(card);
    });
  }

  function criarCardMenuItem(item) {
    const card = document.createElement('div');
    card.className = 'dish-card';
    
    // Trata a URL da imagem
    const imageUrl = item.imageUrl && item.imageUrl.trim() !== '' ? item.imageUrl : '';
    
    // Criar descrição baseada nos ingredientes de forma mais elegante
    let descriptionHtml = '';
    if (item.ingredients && item.ingredients.length > 0) {
      // Agrupar ingredientes por tipo para uma apresentação mais natural
      const ingredientsList = item.ingredients.map(ing => {
        const stockItem = stockItems.find(s => s.id === ing.stockItemId);
        const itemName = stockItem ? stockItem.name : ing.stockItemName || 'Item não encontrado';
        
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
      
      descriptionHtml = `<p class="dish-description">${description}</p>`;
    } else {
      descriptionHtml = `<p class="dish-description"><em>Delicioso prato preparado especialmente para você.</em></p>`;
    }
    
    card.innerHTML = `
      <button class="remove-btn" title="Remover" onclick="deletarItem(${item.id})">×</button>
      <img src="${imageUrl}" alt="${item.name}" class="dish-img" onerror="this.src=''" />
      <h2 class="dish-name">${item.name}</h2>
      <div class="dish-price">R$ ${item.price.toFixed(2)}</div>
      ${descriptionHtml}
      <div class="dish-category">Categoria: ${getCategoryDisplayName(item.category)}</div>
      <div class="dish-actions">
        <button class="btn-edit" onclick="editarItem(${item.id})">Editar</button>
        <button class="btn-toggle ${!item.isAvailable ? 'disabled' : ''}" onclick="toggleDisponibilidade(${item.id})">
          ${item.isAvailable ? 'Desativar' : 'Ativar'}
        </button>
      </div>
    `;
    return card;
  }

  // Upload de imagem com Base64
  async function uploadImage(file) {
    return new Promise((resolve) => {
      const reader = new FileReader();
      reader.onload = function(e) {
        resolve(e.target.result);
      };
      reader.readAsDataURL(file);
    });
  }

  // Preview da imagem melhorado com suporte a imagens profissionais
  imageInput?.addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
      // Validar tipo de arquivo
      if (!file.type.startsWith('image/')) {
        mostrarMensagem('Selecione apenas arquivos de imagem (JPG, PNG, GIF, WEBP, etc.)');
        this.value = '';
        resetImagePreview();
        return;
      }
      
      // Validar tamanho (máximo 50MB para imagens profissionais)
      if (file.size > 50 * 1024 * 1024) {
        mostrarMensagem('A imagem deve ter no máximo 50MB');
        this.value = '';
        resetImagePreview();
        return;
      }

      // Processar imagem com compressão inteligente
      processImageFile(file).then(compressedImageUrl => {
        if (imgPreview) {
          imgPreview.src = compressedImageUrl;
          imgPreview.style.display = 'block';
        }
        if (previewName) {
          const sizeKB = Math.round(compressedImageUrl.length * 0.75 / 1024);
          previewName.textContent = `${file.name} (${sizeKB}KB comprimida)`;
        }
      }).catch(error => {
        console.error('Erro ao processar imagem:', error);
        mostrarMensagem('Erro ao processar a imagem');
        resetImagePreview();
      });
    } else {
      resetImagePreview();
    }
  });

  function resetImagePreview() {
    if (imgPreview) {
      imgPreview.style.display = 'none';
      imgPreview.src = '';
    }
    if (previewName) {
      previewName.textContent = 'Nenhuma imagem selecionada';
    }
  }

  // Funções globais para os botões
  window.editarItem = async function(id) {
    try {
      const response = await fetch(`/api/menu/${id}`);
      const result = await response.json();
      
      if (result.status === 'success') {
        const item = result.data;
        editingItemId = id;
        
        nameInput.value = item.name;
        priceInput.value = item.price;
        if (categorySelect) categorySelect.value = item.category;
        
        // Carregar ingredientes se existirem
        if (item.ingredients) {
          carregarIngredientes(item.ingredients);
        } else {
          carregarIngredientes([]);
        }
        
        // Mostrar imagem atual se existir
        if (item.imageUrl) {
          imgPreview.src = item.imageUrl;
          imgPreview.style.display = 'block';
          previewName.textContent = 'Imagem atual';
        } else {
          imgPreview.style.display = 'none';
          previewName.textContent = 'Nenhuma imagem selecionada';
        }
        
        modal.classList.add("open");
        document.body.style.overflow = "hidden";
      }
    } catch (error) {
      console.error('Erro ao carregar item:', error);
      mostrarMensagem('Erro ao carregar item para edição');
    }
  };

  window.toggleDisponibilidade = async function(id) {
    try {
      const response = await fetch(`/api/menu/${id}/toggle-availability`, {
        method: 'PATCH'
      });
      const result = await response.json();
      
      if (result.status === 'success') {
        carregarMenuItems();
        mostrarMensagem('Disponibilidade alterada com sucesso');
      } else {
        mostrarMensagem('Erro ao alterar disponibilidade');
      }
    } catch (error) {
      console.error('Erro ao alterar disponibilidade:', error);
      mostrarMensagem('Erro de conexão');
    }
  };

  window.deletarItem = async function(id) {
    if (!confirm('Tem certeza que deseja excluir este item?')) return;
    
    try {
      const response = await fetch(`/api/menu/${id}`, {
        method: 'DELETE'
      });
      const result = await response.json();
      
      if (result.status === 'success') {
        carregarMenuItems();
        mostrarMensagem('Item excluído com sucesso');
      } else {
        mostrarMensagem('Erro ao excluir item');
      }
    } catch (error) {
      console.error('Erro ao excluir item:', error);
      mostrarMensagem('Erro de conexão');
    }
  };

  // Modal controls
  addBtn?.addEventListener("click", () => {
    editingItemId = null;
    resetForm();
    modal?.classList.add("open");
    document.body.style.overflow = "hidden";
    setTimeout(() => nameInput?.focus(), 80);
  });

  const closeModal = () => {
    modal?.classList.remove("open");
    document.body.style.overflow = "";
    resetForm();
  };

  function resetForm() {
    if (nameInput) nameInput.value = "";
    if (priceInput) priceInput.value = "";
    if (categorySelect) categorySelect.value = "";
    if (imageInput) imageInput.value = "";
    resetImagePreview();
  }

  cancelBtn?.addEventListener("click", closeModal);

  // Salvar item com imagem e validação melhorada
  saveBtn?.addEventListener("click", async () => {
    // Validar campos obrigatórios
    if (!nameInput?.value.trim()) {
      mostrarMensagem('O nome do prato é obrigatório');
      nameInput?.focus();
      return;
    }

    if (!priceInput?.value || parseFloat(priceInput.value) <= 0) {
      mostrarMensagem('O preço deve ser maior que zero');
      priceInput?.focus();
      return;
    }

    if (!categorySelect?.value) {
      mostrarMensagem('Selecione uma categoria');
      categorySelect?.focus();
      return;
    }

    // Validar ingredientes
    const ingredients = coletarIngredientes();
    if (ingredients.length === 0) {
      mostrarMensagem('Adicione pelo menos um ingrediente');
      return;
    }

    const menuItemDto = {
      id: editingItemId,
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
          const imageBase64 = await processImageFile(imageInput.files[0]);
          menuItemDto.imageUrl = imageBase64;
        }
      } catch (error) {
        console.error('Erro ao processar imagem:', error);
        mostrarMensagem('Erro ao processar a imagem');
        return;
      }
    } else if (editingItemId) {
      // Se estamos editando e não há nova imagem, manter a imagem atual
      const currentImg = imgPreview.src;
      if (currentImg && !currentImg.includes('data:image/svg')) {
        menuItemDto.imageUrl = currentImg;
      }
    }

    try {
      const url = editingItemId ? `/api/menu/${editingItemId}` : '/api/menu';
      const method = editingItemId ? 'PUT' : 'POST';
      
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(menuItemDto)
      });

      const result = await response.json();
      
      if (result.status === 'success') {
        carregarMenuItems();
        fecharModal();
        mostrarMensagem(editingItemId ? 'Prato atualizado com sucesso' : 'Prato adicionado com sucesso');
      } else {
        mostrarMensagem('Erro ao salvar: ' + result.message);
      }
    } catch (error) {
      console.error('Erro ao salvar item:', error);
      mostrarMensagem('Erro de conexão ao salvar');
    }
  });

  // ===============================
  // FUNÇÕES DE INGREDIENTES
  // ===============================

  // Adicionar um novo ingrediente
  function adicionarIngrediente() {
    const ingredientDiv = document.createElement('div');
    ingredientDiv.className = 'ingredient-item';
    ingredientDiv.innerHTML = `
      <div class="ingredient-select">
        <label>Ingrediente</label>
        <select class="ingredient-stock-select" required onchange="atualizarUnidadeIngrediente(this)">
          <option value="">Selecione um item do estoque</option>
          ${stockItems.map(item => {
            const unidadeExibicao = formatarUnidadeParaExibicao(item.unit);
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
        <button type="button" class="btn-remove-ingredient" onclick="removerIngrediente(this)">×</button>
      </div>
    `;
    
    ingredientsList.appendChild(ingredientDiv);
    atualizarEstadoIngredientes();
  }

  // Atualizar unidade quando ingrediente é selecionado
  window.atualizarUnidadeIngrediente = function(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const unitInput = selectElement.closest('.ingredient-item').querySelector('.ingredient-unit-input');
    
    if (selectedOption.value && selectedOption.dataset.unit) {
      const unidadeFormatada = formatarUnidadeParaExibicao(selectedOption.dataset.unit);
      unitInput.value = unidadeFormatada;
      unitInput.style.backgroundColor = '#2a2a2a';
      unitInput.style.color = '#fff';
    } else {
      unitInput.value = '';
      unitInput.placeholder = 'Selecione um ingrediente';
    }
  };

  // Remover ingrediente
  window.removerIngrediente = function(button) {
    const ingredientItem = button.closest('.ingredient-item');
    ingredientItem.remove();
    atualizarEstadoIngredientes();
  };

  // Atualizar estado da lista de ingredientes
  function atualizarEstadoIngredientes() {
    const items = ingredientsList.querySelectorAll('.ingredient-item');
    if (items.length === 0) {
      ingredientsList.innerHTML = '<div class="empty-ingredients">Nenhum ingrediente adicionado</div>';
    } else {
      const emptyMsg = ingredientsList.querySelector('.empty-ingredients');
      if (emptyMsg) {
        emptyMsg.remove();
      }
    }
  }

  // Coletar dados dos ingredientes
  function coletarIngredientes() {
    const ingredients = [];
    const items = ingredientsList.querySelectorAll('.ingredient-item');
    
    items.forEach(item => {
      const stockSelect = item.querySelector('.ingredient-stock-select');
      const quantityInput = item.querySelector('.ingredient-quantity-input');
      const showUnitCheckbox = item.querySelector('.ingredient-show-unit-checkbox');
      
      if (stockSelect.value && quantityInput.value) {
        const stockItem = stockItems.find(s => s.id == stockSelect.value);
        ingredients.push({
          stockItemId: parseInt(stockSelect.value),
          stockItemName: stockItem ? stockItem.name : '',
          quantity: parseFloat(quantityInput.value),
          unit: stockItem ? stockItem.unit : '', // Sempre usar a unidade do item do estoque
          showUnit: showUnitCheckbox ? showUnitCheckbox.checked : true
        });
      }
    });
    
    return ingredients;
  }

  // Carregar ingredientes para edição
  function carregarIngredientes(ingredients) {
    ingredientsList.innerHTML = '';
    
    if (ingredients && ingredients.length > 0) {
      ingredients.forEach(ingredient => {
        const stockItem = stockItems.find(s => s.id == ingredient.stockItemId);
        const ingredientDiv = document.createElement('div');
        ingredientDiv.className = 'ingredient-item';
        ingredientDiv.innerHTML = `
          <div class="ingredient-select">
            <label>Ingrediente</label>
            <select class="ingredient-stock-select" required onchange="atualizarUnidadeIngrediente(this)">
              <option value="">Selecione um item do estoque</option>
              ${stockItems.map(item => 
                `<option value="${item.id}" data-unit="${item.unit}" ${item.id == ingredient.stockItemId ? 'selected' : ''}>${item.name} (${formatarUnidadeParaExibicao(item.unit)})</option>`
              ).join('')}
            </select>
          </div>
          <div class="ingredient-quantity">
            <label>Quantidade</label>
            <input type="number" class="ingredient-quantity-input" value="${ingredient.quantity}" step="0.001" min="0" required>
          </div>
          <div class="ingredient-unit">
            <label>Unidade</label>
            <input type="text" class="ingredient-unit-input" value="${stockItem ? formatarUnidadeParaExibicao(stockItem.unit) : ingredient.unit}" readonly disabled>
          </div>
          <div class="ingredient-show-unit">
            <label class="checkbox-label">
              <input type="checkbox" class="ingredient-show-unit-checkbox" ${ingredient.showUnit !== false ? 'checked' : ''}>
              <span class="checkmark"></span>
              Mostrar unidade
            </label>
          </div>
          <div class="ingredient-actions">
            <button type="button" class="btn-remove-ingredient" onclick="removerIngrediente(this)">×</button>
          </div>
        `;
        
        ingredientsList.appendChild(ingredientDiv);
      });
    }
    
    atualizarEstadoIngredientes();
  }

  function mostrarMensagem(mensagem) {
    // Criando um toast simples
    const toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.textContent = mensagem;
    toast.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: #333;
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
  }

  // CSS para animações de toast
  const style = document.createElement('style');
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

  // Carregar dados iniciais
  carregarMenuItems();
});