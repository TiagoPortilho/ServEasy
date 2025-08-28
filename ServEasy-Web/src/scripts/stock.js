document.addEventListener("DOMContentLoaded", () => {
  console.log("[stock.js] carregado");

  const addBtn = document.querySelector(".add-btn");
  const modal = document.getElementById("modalOverlay");
  const modalTitle = document.getElementById("modalTitle");
  const confirmModal = document.getElementById("confirmModal");
  const tableBody = document.querySelector(".stock-table tbody");

  // Form elements
  const itemCategory = document.getElementById("itemCategory");
  const itemName = document.getElementById("itemName");
  const itemPrice = document.getElementById("itemPrice");
  const itemQuantity = document.getElementById("itemQuantity");
  const itemEntry = document.getElementById("itemEntry");
  const itemExpiry = document.getElementById("itemExpiry");

  let editingRow = null;

  // Open modal for adding
  addBtn?.addEventListener("click", () => {
    modalTitle.textContent = "Adicionar Item";
    editingRow = null;
    openModal();
  });

  // Modal controls
  function openModal() {
    modal?.classList.add("open");
    document.body.style.overflow = "hidden";
    resetForm();
  }

  function closeModal() {
    modal?.classList.remove("open");
    document.body.style.overflow = "";
    editingRow = null;
    resetForm();
  }

  function resetForm() {
    itemCategory.value = "";
    itemName.value = "";
    itemPrice.value = "";
    itemQuantity.value = "";
    itemEntry.value = "";
    itemExpiry.value = "";
  }

  // Close modal handlers
  document.getElementById("cancelBtn")?.addEventListener("click", closeModal);
  modal?.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
  });

  // Save item
  document.getElementById("saveBtn")?.addEventListener("click", () => {
    const price = parseFloat(itemPrice.value.replace(/[^\d.,]/g, "").replace(",", "."));
    const quantity = parseFloat(itemQuantity.value);
    const total = (price * quantity).toFixed(2);

    const rowData = {
      category: itemCategory.value,
      name: itemName.value,
      price: `R$ ${price.toFixed(2)}`,
      quantity: `${quantity} ${getUnit(itemCategory.value)}`,
      total: `R$ ${total}`,
      entry: formatDate(itemEntry.value),
      expiry: formatDate(itemExpiry.value)
    };

    if (editingRow) {
      updateRow(editingRow, rowData);
    } else {
      createRow(rowData);
    }

    closeModal();
  });

  // Edit handler
  function handleEdit(row) {
    editingRow = row;
    modalTitle.textContent = "Editar Item";

    const cells = row.cells;
    itemCategory.value = cells[0].textContent;
    itemName.value = cells[1].textContent;
    itemPrice.value = cells[2].textContent.replace("R$ ", "");
    itemQuantity.value = cells[3].textContent.split(" ")[0];
    itemEntry.value = formatDateForInput(cells[5].textContent);
    itemExpiry.value = formatDateForInput(cells[6].textContent);

    openModal();
  }

  // Delete handler
  function handleDelete(row) {
    const confirmModal = document.getElementById("confirmModal");
    confirmModal?.classList.add("open");

    const handleConfirm = () => {
      row.remove();
      confirmModal?.classList.remove("open");
    };

    const handleCancel = () => {
      confirmModal?.classList.remove("open");
    };

    document.getElementById("confirmOk")?.addEventListener("click", handleConfirm, { once: true });
    document.getElementById("confirmCancel")?.addEventListener("click", handleCancel, { once: true });
  }

  // Helper functions
  function createRow(data) {
    const row = tableBody.insertRow(0);
    row.innerHTML = `
      <td>${escapeHtml(data.category)}</td>
      <td>${escapeHtml(data.name)}</td>
      <td>${escapeHtml(data.price)}</td>
      <td>${escapeHtml(data.quantity)}</td>
      <td>${escapeHtml(data.total)}</td>
      <td>${escapeHtml(data.entry)}</td>
      <td>${escapeHtml(data.expiry)}</td>
      <td>
        <button class="action-btn edit-btn" title="Editar">✎</button>
        <button class="action-btn delete-btn" title="Remover">×</button>
      </td>
    `;

    attachRowHandlers(row);
  }

  function updateRow(row, data) {
    const cells = row.cells;
    cells[0].textContent = data.category;
    cells[1].textContent = data.name;
    cells[2].textContent = data.price;
    cells[3].textContent = data.quantity;
    cells[4].textContent = data.total;
    cells[5].textContent = data.entry;
    cells[6].textContent = data.expiry;
  }

  function attachRowHandlers(row) {
    const editBtn = row.querySelector(".edit-btn");
    const deleteBtn = row.querySelector(".delete-btn");

    editBtn?.addEventListener("click", () => handleEdit(row));
    deleteBtn?.addEventListener("click", () => handleDelete(row));
  }

  function getUnit(category) {
    const units = {
      "Hortifruti": "kg",
      "Laticínios": "L",
      "Bebidas": "un",
      "Embalagens": "un"
    };
    return units[category] || "un";
  }

  function formatDate(dateStr) {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    return date.toLocaleDateString("pt-BR");
  }

  function formatDateForInput(dateStr) {
    if (!dateStr) return "";
    const [day, month, year] = dateStr.split("/");
    return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
  }

  function escapeHtml(str) {
    return String(str).replace(/[&<>"']/g, m => ({
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#39;"
    }[m]));
  }

  // Attach handlers to existing rows
  document.querySelectorAll(".stock-table tbody tr").forEach(attachRowHandlers);
});
