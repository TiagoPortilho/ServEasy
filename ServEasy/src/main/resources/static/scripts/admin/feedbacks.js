document.addEventListener("DOMContentLoaded", async () => {// feedbacks.js (fictício) - gerencia listagem e remoção com confirmação

    const feedbackBody = document.getElementById("feedbackBody");document.addEventListener("DOMContentLoaded", () => {

    const confirmModal = document.getElementById("confirmModal");  const feedbackBody = document.getElementById("feedbackBody");

    const confirmCancel = document.getElementById("confirmCancel");  const confirmModal = document.getElementById("confirmModal");

    const confirmOk = document.getElementById("confirmOk");  const confirmCancel = document.getElementById("confirmCancel");

    const confirmMessage = document.getElementById("confirmMessage");  const confirmOk = document.getElementById("confirmOk");

    const confirmTitle = document.getElementById("confirmTitle");  const confirmMessage = document.getElementById("confirmMessage");

  const confirmTitle = document.getElementById("confirmTitle");

    let toDeleteId = null;

  // dados fictícios

    // Carregar feedbacks da API  let feedbacks = [

    await loadFeedbacks();    { id: "f1", user: "Ana R.", text: "Ótimo atendimento! Recomendo.", date: "2025-08-10" },

    { id: "f2", user: "Lucas M.", text: "Comida boa mas demorou um pouco.", date: "2025-08-09" },

    // Atualizar automaticamente a cada 30 segundos    { id: "f3", user: "Mariana P.", text: "Preço justo e ambiente agradável.", date: "2025-08-08" }

    setInterval(loadFeedbacks, 30000);  ];



    async function loadFeedbacks() {  let toDeleteId = null;

        try {

            const response = await fetch('/api/feedbacks');  function render() {

                feedbackBody.innerHTML = "";

            if (!response.ok) {    if (!feedbacks.length) {

                throw new Error(`HTTP error! status: ${response.status}`);      const tr = document.createElement("tr");

            }      tr.innerHTML = `<td colspan="3" style="padding:2rem;text-align:center;color:#ccc">Nenhum feedback registrado.</td>`;

                  feedbackBody.appendChild(tr);

            const feedbacks = await response.json();      return;

            renderFeedbacks(feedbacks);    }

            

        } catch (error) {    feedbacks.forEach(f => {

            console.error('Erro ao carregar feedbacks:', error);      const tr = document.createElement("tr");

            showError('Erro ao carregar feedbacks');      tr.innerHTML = `

        }        <td>

    }          <div class="feedback-user">

            <div class="feedback-avatar">${escapeInitials(f.user)}</div>

    function renderFeedbacks(feedbacks) {            <div>

        feedbackBody.innerHTML = "";              <div class="feedback-name">${escapeHtml(f.user)}</div>

                      <div class="feedback-meta">${formatDate(f.date)}</div>

        if (!feedbacks || feedbacks.length === 0) {            </div>

            const tr = document.createElement("tr");          </div>

            tr.innerHTML = `<td colspan="4" style="padding:2rem;text-align:center;color:#ccc">Nenhum feedback registrado.</td>`;        </td>

            feedbackBody.appendChild(tr);        <td><div class="feedback-text">${escapeHtml(f.text)}</div></td>

            return;        <td class="actions-cell">

        }          <button class="action-btn delete-btn" data-id="${f.id}" title="Remover">×</button>

        </td>

        feedbacks.forEach(feedback => {      `;

            const tr = document.createElement("tr");      feedbackBody.appendChild(tr);

            tr.innerHTML = `    });

                <td>${feedback.customerName || 'Anônimo'}</td>

                <td>    // attach listeners for delete buttons

                    <div class="rating">    document.querySelectorAll(".delete-btn").forEach(btn => {

                        ${generateStarRating(feedback.rating || 0)}      btn.addEventListener("click", (e) => {

                    </div>        const id = e.currentTarget.dataset.id;

                </td>        showConfirm("Tem certeza que deseja remover este feedback?", id);

                <td>${feedback.comment || ''}</td>      });

                <td>${formatDate(feedback.feedbackDate)}</td>    });

                <td>  }

                    <button class="btn-delete" onclick="confirmDelete(${feedback.id})" title="Remover feedback">

                        <i class="fas fa-trash"></i>  function showConfirm(message, id) {

                    </button>    toDeleteId = id;

                </td>    confirmMessage.textContent = message;

            `;    confirmTitle.textContent = "Remover feedback";

            feedbackBody.appendChild(tr);    confirmModal.classList.add("open");

        });    document.body.style.overflow = "hidden";

    }  }



    function generateStarRating(rating) {  function closeConfirm() {

        let stars = '';    toDeleteId = null;

        for (let i = 1; i <= 5; i++) {    confirmModal.classList.remove("open");

            if (i <= rating) {    document.body.style.overflow = "";

                stars += '<i class="fas fa-star text-warning"></i>';  }

            } else {

                stars += '<i class="far fa-star text-muted"></i>';  confirmCancel.addEventListener("click", closeConfirm);

            }

        }  confirmOk.addEventListener("click", () => {

        return stars;    if (!toDeleteId) return closeConfirm();

    }    feedbacks = feedbacks.filter(f => f.id !== toDeleteId);

    render();

    function formatDate(dateString) {    closeConfirm();

        if (!dateString) return 'Data inválida';  });

        

        try {  // fechar clicando fora do modal

            const date = new Date(dateString);  confirmModal.addEventListener("click", (e) => {

            return date.toLocaleDateString('pt-BR');    if (e.target === confirmModal) closeConfirm();

        } catch (error) {  });

            return 'Data inválida';

        }  // fechar com ESC

    }  document.addEventListener("keydown", (e) => {

    if (e.key === "Escape" && confirmModal.classList.contains("open")) closeConfirm();

    // Função global para confirmar exclusão  });

    window.confirmDelete = function(id) {

        toDeleteId = id;  // helpers

        confirmTitle.textContent = "Confirmar Remoção";  function escapeHtml(s){

        confirmMessage.textContent = "Tem certeza que deseja remover este feedback? Esta ação não pode ser desfeita.";    return String(s).replace(/[&<>"']/g, m=>({"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"}[m]));

        confirmModal.classList.add("open");  }

    };  function escapeInitials(name){

    if(!name) return "";

    // Event listeners para modal de confirmação    const parts = name.trim().split(" ");

    confirmCancel?.addEventListener("click", () => {    if(parts.length === 1) return parts[0].slice(0,2).toUpperCase();

        confirmModal.classList.remove("open");    return (parts[0][0] + (parts[1] ? parts[1][0] : "")).toUpperCase();

        toDeleteId = null;  }

    });  function formatDate(iso){

    try{

    confirmOk?.addEventListener("click", async () => {      const d = new Date(iso);

        if (toDeleteId) {      if(isNaN(d)) return iso;

            await deleteFeedback(toDeleteId);      return d.toLocaleDateString();

        }    } catch(e){ return iso; }

        confirmModal.classList.remove("open");  }

        toDeleteId = null;

    });  // inicial

  render();

    // Fechar modal clicando fora});

    confirmModal?.addEventListener("click", (e) => {
        if (e.target === confirmModal) {
            confirmModal.classList.remove("open");
            toDeleteId = null;
        }
    });

    async function deleteFeedback(id) {
        try {
            const response = await fetch(`/api/feedbacks/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            await loadFeedbacks();
            showSuccess('Feedback removido com sucesso!');
            
        } catch (error) {
            console.error('Erro ao remover feedback:', error);
            alert('Erro ao remover feedback');
        }
    }

    function showError(message) {
        const feedbackBody = document.getElementById("feedbackBody");
        if (feedbackBody) {
            feedbackBody.innerHTML = `
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
    }

    function showSuccess(message) {
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
});