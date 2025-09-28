// feedbacks.js (fictício) - gerencia listagem e remoção com confirmação
document.addEventListener("DOMContentLoaded", () => {
  const feedbackBody = document.getElementById("feedbackBody");
  const confirmModal = document.getElementById("confirmModal");
  const confirmCancel = document.getElementById("confirmCancel");
  const confirmOk = document.getElementById("confirmOk");
  const confirmMessage = document.getElementById("confirmMessage");
  const confirmTitle = document.getElementById("confirmTitle");

  // dados fictícios
  let feedbacks = [
    { id: "f1", user: "Ana R.", text: "Ótimo atendimento! Recomendo.", date: "2025-08-10" },
    { id: "f2", user: "Lucas M.", text: "Comida boa mas demorou um pouco.", date: "2025-08-09" },
    { id: "f3", user: "Mariana P.", text: "Preço justo e ambiente agradável.", date: "2025-08-08" }
  ];

  let toDeleteId = null;

  function render() {
    feedbackBody.innerHTML = "";
    if (!feedbacks.length) {
      const tr = document.createElement("tr");
      tr.innerHTML = `<td colspan="3" style="padding:2rem;text-align:center;color:#ccc">Nenhum feedback registrado.</td>`;
      feedbackBody.appendChild(tr);
      return;
    }

    feedbacks.forEach(f => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>
          <div class="feedback-user">
            <div class="feedback-avatar">${escapeInitials(f.user)}</div>
            <div>
              <div class="feedback-name">${escapeHtml(f.user)}</div>
              <div class="feedback-meta">${formatDate(f.date)}</div>
            </div>
          </div>
        </td>
        <td><div class="feedback-text">${escapeHtml(f.text)}</div></td>
        <td class="actions-cell">
          <button class="action-btn delete-btn" data-id="${f.id}" title="Remover">×</button>
        </td>
      `;
      feedbackBody.appendChild(tr);
    });

    // attach listeners for delete buttons
    document.querySelectorAll(".delete-btn").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const id = e.currentTarget.dataset.id;
        showConfirm("Tem certeza que deseja remover este feedback?", id);
      });
    });
  }

  function showConfirm(message, id) {
    toDeleteId = id;
    confirmMessage.textContent = message;
    confirmTitle.textContent = "Remover feedback";
    confirmModal.classList.add("open");
    document.body.style.overflow = "hidden";
  }

  function closeConfirm() {
    toDeleteId = null;
    confirmModal.classList.remove("open");
    document.body.style.overflow = "";
  }

  confirmCancel.addEventListener("click", closeConfirm);

  confirmOk.addEventListener("click", () => {
    if (!toDeleteId) return closeConfirm();
    feedbacks = feedbacks.filter(f => f.id !== toDeleteId);
    render();
    closeConfirm();
  });

  // fechar clicando fora do modal
  confirmModal.addEventListener("click", (e) => {
    if (e.target === confirmModal) closeConfirm();
  });

  // fechar com ESC
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && confirmModal.classList.contains("open")) closeConfirm();
  });

  // helpers
  function escapeHtml(s){
    return String(s).replace(/[&<>"']/g, m=>({"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"}[m]));
  }
  function escapeInitials(name){
    if(!name) return "";
    const parts = name.trim().split(" ");
    if(parts.length === 1) return parts[0].slice(0,2).toUpperCase();
    return (parts[0][0] + (parts[1] ? parts[1][0] : "")).toUpperCase();
  }
  function formatDate(iso){
    try{
      const d = new Date(iso);
      if(isNaN(d)) return iso;
      return d.toLocaleDateString();
    } catch(e){ return iso; }
  }

  // inicial
  render();
});
