/**
 * Feedback Manager - Gerencia listagem, exibição e exclusão de feedbacks
 * Implementado com princípios SOLID
 */

// Aguardar o JWT interceptor carregar antes de inicializar
document.addEventListener('DOMContentLoaded', function() {
    // Aguardar o JWT interceptor carregar antes de fazer requisições
    waitForJwtInterceptor().then(() => {
        console.log('[feedbacks.js] JWT Interceptor carregado, iniciando aplicação...');
        initializeFeedbackManager();
    });
});

// Função para aguardar o JWT interceptor carregar
function waitForJwtInterceptor() {
    return new Promise((resolve) => {
        if (window.jwtInterceptorLoaded) {
            console.log('[feedbacks.js] JWT Interceptor já estava carregado');
            resolve();
            return;
        }

        console.log('[feedbacks.js] Aguardando JWT Interceptor carregar...');
        const checkInterval = setInterval(() => {
            if (window.jwtInterceptorLoaded) {
                console.log('[feedbacks.js] JWT Interceptor carregado com sucesso');
                clearInterval(checkInterval);
                resolve();
            }
        }, 100);

        // Timeout de segurança (5 segundos)
        setTimeout(() => {
            if (!window.jwtInterceptorLoaded) {
                console.warn('[feedbacks.js] Timeout aguardando JWT Interceptor, continuando mesmo assim');
                clearInterval(checkInterval);
                resolve();
            }
        }, 5000);
    });
}

// Módulo de API - Responsável pela comunicação com o servidor
const FeedbackAPI = {
  async getFeedbacks() {
    try {
      console.log('Fazendo requisição para /api/feedbacks...');
      const response = await fetch('/api/feedbacks');
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const apiResponse = await response.json();
      console.log('API Response completa:', apiResponse);
      
      // A resposta da API tem a estrutura: { status: 'success', message: '', data: [...] }
      if (apiResponse.status === 'success') {
        console.log('Feedbacks extraídos:', apiResponse.data);
        return apiResponse.data; // Retornar apenas os dados dos feedbacks
      } else {
        throw new Error(apiResponse.message || 'Erro na resposta da API');
      }
    } catch (error) {
      console.error('Erro ao carregar feedbacks:', error);
      throw error;
    }
  },

  async deleteFeedback(id) {
    try {
      const response = await fetch(`/api/feedbacks/${id}`, {
        method: 'DELETE'
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return true;
    } catch (error) {
      console.error('Erro ao remover feedback:', error);
      throw error;
    }
  }
};

// Módulo de UI - Responsável pela renderização dos feedbacks
const FeedbackUI = {
  elements: {
    feedbackBody: null,
    confirmModal: null,
    confirmCancel: null,
    confirmOk: null,
    confirmTitle: null,
    confirmMessage: null
  },

  initialize() {
    // Capturar elementos DOM uma única vez
    this.elements.feedbackBody = document.getElementById("feedbackBody");
    this.elements.confirmModal = document.getElementById("confirmModal");
    this.elements.confirmCancel = document.getElementById("confirmCancel");
    this.elements.confirmOk = document.getElementById("confirmOk");
    this.elements.confirmTitle = document.getElementById("confirmTitle");
    this.elements.confirmMessage = document.getElementById("confirmMessage");
  },

  render(feedbacks) {
    console.log('FeedbackUI.render: Iniciando renderização com dados:', feedbacks);
    const { feedbackBody } = this.elements;
    
    if (!feedbackBody) {
      console.error('FeedbackUI.render: Elemento feedbackBody não encontrado!');
      return;
    }
    
    feedbackBody.innerHTML = "";

    if (!feedbacks || feedbacks.length === 0) {
      console.log('FeedbackUI.render: Nenhum feedback encontrado');
      feedbackBody.innerHTML = `
        <tr>
          <td colspan="4" style="padding:2rem;text-align:center;color:#ccc">
            Nenhum feedback registrado.
          </td>
        </tr>`;
      return;
    }

    console.log('FeedbackUI.render: Renderizando', feedbacks.length, 'feedbacks');
    feedbacks.forEach((feedback, index) => {
      console.log(`FeedbackUI.render: Processando feedback ${index + 1}:`, feedback);
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>
          <div class="feedback-user">
            <div class="feedback-avatar">${this.escapeInitials(feedback.customerName || 'Anônimo')}</div>
            <div>
              <div class="feedback-name">${this.escapeHtml(feedback.customerName || 'Anônimo')}</div>
              <div class="feedback-meta">${this.formatDate(feedback.feedbackDate)}</div>
            </div>
          </div>
        </td>
        <td>
          <div class="rating">
            ${this.generateStarRating(feedback.rating || 0)}
          </div>
        </td>
        <td><div class="feedback-text">${this.escapeHtml(feedback.comment || '')}</div></td>
        <td class="actions-cell">
          <button class="action-btn delete-btn" data-id="${feedback.id}" title="Remover">×</button>
        </td>
      `;
      feedbackBody.appendChild(tr);
    });

    // Adicionar event listeners aos botões de excluir
    document.querySelectorAll(".delete-btn").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const id = e.currentTarget.dataset.id;
        ConfirmationModal.show("Remover feedback", "Tem certeza que deseja remover este feedback?", id);
      });
    });
  },

  showError(message) {
    if (this.elements.feedbackBody) {
      this.elements.feedbackBody.innerHTML = `
        <tr>
          <td colspan="4" style="padding:2rem;text-align:center;">
            <div class="alert alert-danger">
              <i class="fas fa-exclamation-triangle"></i>
              ${message}
            </div>
          </td>
        </tr>
      `;
    }
  },

  // Funções utilitárias
  escapeHtml(s) {
    return String(s).replace(/[&<>"']/g, m => ({
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      "\"": "&quot;",
      "'": "&#39;"
    }[m]));
  },

  escapeInitials(name) {
    if(!name) return "";
    const parts = name.trim().split(" ");
    if(parts.length === 1) return parts[0].slice(0,2).toUpperCase();
    return (parts[0][0] + (parts[1] ? parts[1][0] : "")).toUpperCase();
  },

  formatDate(dateString) {
    if (!dateString) return 'Data inválida';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('pt-BR');
    } catch (error) {
      return 'Data inválida';
    }
  },

  generateStarRating(rating) {
    let stars = '';
    for (let i = 1; i <= 5; i++) {
      stars += i <= rating 
        ? '<i class="fas fa-star text-warning"></i>' 
        : '<i class="far fa-star text-muted"></i>';
    }
    return stars;
  }
};

// Módulo de notificações - Responsável pelos toasts e alertas
const NotificationManager = {
  showSuccess(message) {
    // Criar toast de sucesso
    const toastHtml = `
      <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body">
            <i class="fas fa-check-circle"></i>
            ${message}
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
      </div>
    `;
    
    // Verificar se existe container de toasts
    let toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
      toastContainer = document.createElement('div');
      toastContainer.id = 'toast-container';
      toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
      document.body.appendChild(toastContainer);
    }
    
    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    
    // Inicializar e mostrar toast
    const toastElement = toastContainer.lastElementChild;
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
    
    // Remover toast após ser escondido
    toastElement.addEventListener('hidden.bs.toast', () => {
      toastElement.remove();
    });
  }
};

// Módulo de confirmação - Gerencia o modal de confirmação
const ConfirmationModal = {
  currentItemId: null,
  onConfirm: null,
  elements: null,

  initialize(elements) {
    this.elements = elements;
    
    // Event listeners para o modal
    elements.confirmCancel.addEventListener("click", this.close.bind(this));
    
    elements.confirmOk.addEventListener("click", () => {
      if (this.onConfirm && this.currentItemId) {
        this.onConfirm(this.currentItemId);
      }
      this.close();
    });

    // Fechar clicando fora do modal
    elements.confirmModal.addEventListener("click", (e) => {
      if (e.target === elements.confirmModal) {
        this.close();
      }
    });

    // Fechar com ESC
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && elements.confirmModal.classList.contains("open")) {
        this.close();
      }
    });
  },

  show(title, message, itemId, callback) {
    this.currentItemId = itemId;
    this.onConfirm = callback;
    this.elements.confirmTitle.textContent = title;
    this.elements.confirmMessage.textContent = message;
    this.elements.confirmModal.classList.add("open");
    document.body.style.overflow = "hidden";
  },

  close() {
    this.currentItemId = null;
    this.elements.confirmModal.classList.remove("open");
    document.body.style.overflow = "";
  }
};

// Controller principal - Gerencia o fluxo de dados e ações
const FeedbackController = {
  initialize() {
    FeedbackUI.initialize();
    ConfirmationModal.initialize(FeedbackUI.elements);
    this.loadFeedbacks();

    // Atualizar automaticamente a cada 30 segundos
    setInterval(this.loadFeedbacks.bind(this), 30000);
  },

  async loadFeedbacks() {
    try {
      console.log('loadFeedbacks: Iniciando carregamento...');
      const feedbacks = await FeedbackAPI.getFeedbacks();
      console.log('loadFeedbacks: Feedbacks recebidos:', feedbacks);
      FeedbackUI.render(feedbacks);
      console.log('loadFeedbacks: Renderização concluída');
    } catch (error) {
      console.error('loadFeedbacks: Erro:', error);
      FeedbackUI.showError('Erro ao carregar feedbacks');
    }
  },

  async deleteFeedback(id) {
    try {
      await FeedbackAPI.deleteFeedback(id);
      await this.loadFeedbacks();
      NotificationManager.showSuccess('Feedback removido com sucesso!');
    } catch (error) {
      FeedbackUI.showError('Erro ao remover feedback');
    }
  }
};

// Inicialização
// REMOVIDO: document.addEventListener("DOMContentLoaded", () => {
//   FeedbackController.initialize();
// });

// Função para inicializar o feedback manager após JWT interceptor
function initializeFeedbackManager() {
    FeedbackController.initialize();

    // Configurar o callback para exclusão de feedbacks
    ConfirmationModal.onConfirm = FeedbackController.deleteFeedback.bind(FeedbackController);
}