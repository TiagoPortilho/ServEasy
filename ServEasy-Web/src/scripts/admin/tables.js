document.addEventListener("DOMContentLoaded", () => {
  const addBtn = document.getElementById("openAdd");
  const modal = document.getElementById("modalOverlay");
  const cancelBtn = document.getElementById("cancelBtn");
  const saveBtn = document.getElementById("saveBtn");
  const tablesGrid = document.querySelector(".tables-grid");

  const tableNumber = document.getElementById("tableNumber");
  const tableStatus = document.getElementById("tableStatus");
  
  // Confirmação
  const confirmModal = document.getElementById("confirmModal");
  const confirmCancel = document.getElementById("confirmCancel");
  const confirmOk = document.getElementById("confirmOk");
  let currentCallback = null;

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
    modal?.classList.add("open");
    document.body.style.overflow = "hidden";
    setTimeout(() => tableNumber?.focus(), 80);
  }

  function closeModal() {
    modal?.classList.remove("open");
    document.body.style.overflow = "";
    if (tableNumber) tableNumber.value = "";
    if (tableStatus) tableStatus.value = "vazio";
  }

  // Event Listeners
  addBtn?.addEventListener("click", openModal);
  cancelBtn?.addEventListener("click", closeModal);
  modal?.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
  });
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeModal();
  });

  // Salvar mesa
  saveBtn?.addEventListener("click", () => {
    const num = tableNumber?.value.trim() || "?";
    const status = tableStatus?.value || "vazio";

    const card = document.createElement("div");
    card.className = `table-card status-${status}`;
    card.innerHTML = `
      <button class="remove-btn" title="Remover">×</button>
      <div class="table-number">#${num}</div>
      <div class="table-status">${getStatusLabel(status)}</div>
      <div class="table-actions">
        <button class="btn-status" title="Mudar Status">⟳</button>
      </div>
    `;

    // Botão remover com confirmação
    const removeBtn = card.querySelector(".remove-btn");
    removeBtn?.addEventListener("click", () => {
      showConfirm("Tem certeza que deseja remover esta mesa?", () => {
        card.remove();
      });
    });

    // Botão status
    const statusBtn = card.querySelector(".btn-status");
    statusBtn?.addEventListener("click", () => {
      const currentStatus = card.className.split("status-")[1];
      const nextStatus = getNextStatus(currentStatus);
      card.className = `table-card status-${nextStatus}`;
      card.querySelector(".table-status").textContent = getStatusLabel(nextStatus);
    });

    tablesGrid?.prepend(card);
    closeModal();
  });

  // Funções auxiliares
  function getStatusLabel(status) {
    const labels = {
      "vazio": "Vazio",
      "ocupado": "Ocupado",
      "pedido-feito": "Pedido feito",
      "pedido-pago": "Pedido pago",
      "pedido-nao-pago": "Pedido não pago",
      "pedido-entregue": "Pedido entregue"
    };
    return labels[status] || status;
  }

  function getNextStatus(current) {
    const sequence = ["vazio", "ocupado", "pedido-feito", "pedido-entregue", "pedido-pago", "pedido-nao-pago"];
    const currentIndex = sequence.indexOf(current);
    return sequence[(currentIndex + 1) % sequence.length];
  }
});
