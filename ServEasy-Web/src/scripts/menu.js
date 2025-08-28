// menu.js (fictício, front-end)
document.addEventListener("DOMContentLoaded", () => {
  console.log("[menu.js] carregado");

  const addBtn = document.querySelector(".add-btn");
  const modal = document.getElementById("modalOverlay");
  const cancelBtn = document.getElementById("cancelBtn");
  const saveBtn = document.getElementById("saveBtn");
  const menuGrid = document.querySelector(".menu-grid");

  const nameInput = document.getElementById("dishName");
  const priceInput = document.getElementById("dishPrice");
  const descInput = document.getElementById("dishDesc");
  const ingrInput = document.getElementById("dishIngr");

  const dishImageInput = document.getElementById("dishImage");
  const imgPreview = document.getElementById("imgPreview");
  const previewName = document.getElementById("previewName");

  if (!modal) {
    console.error(
      "[menu.js] modalOverlay não encontrado - verifique o HTML/CSS"
    );
    return;
  }

  // abrir ao clicar
  addBtn?.addEventListener("click", () => {
    modal?.classList.add("open");
    document.body.style.overflow = "hidden";
    // pequeno delay pra garantir foco após animação
    setTimeout(() => {
      nameInput?.focus();
    }, 80);
  });

  // fechar modal
  const closeModal = () => {
    modal?.classList.remove("open");
    document.body.style.overflow = "";
    resetForm();
    console.log("[menu.js] modal fechado");
  };

  function resetForm() {
    if (nameInput) nameInput.value = "";
    if (priceInput) priceInput.value = "";
    if (descInput) descInput.value = "";
    if (ingrInput) ingrInput.value = "";
    if (previewName) previewName.textContent = "Nenhum arquivo selecionado";
    if (imgPreview) {
      if (imgPreview.dataset.objectUrl) {
        try {
          URL.revokeObjectURL(imgPreview.dataset.objectUrl);
        } catch (e) {}
        delete imgPreview.dataset.objectUrl;
      }
      imgPreview.src = "";
      imgPreview.style.display = "none";
    }
    if (dishImageInput) dishImageInput.value = "";
  }

  // fechar com cancelar
  cancelBtn?.addEventListener("click", closeModal);

  // fechar clicando no overlay (fora do modal)
  modal?.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
  });

  // fechar com ESC
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeModal();
  });

  // preview da imagem selecionada
  dishImageInput?.addEventListener("change", () => {
    const file = dishImageInput.files?.[0];
    if (!file) {
      previewName && (previewName.textContent = "Nenhum arquivo selecionado");
      imgPreview && (imgPreview.style.display = "none");
      return;
    }
    const url = URL.createObjectURL(file);
    if (imgPreview) {
      imgPreview.src = url;
      imgPreview.style.display = "block";
      imgPreview.dataset.objectUrl = url;
    }
    previewName && (previewName.textContent = file.name);
  });

  // salvar prato (fictício)
  saveBtn?.addEventListener("click", () => {
    const name = (nameInput?.value || "").trim() || "Prato sem nome";
    const rawPrice = (priceInput?.value || "").trim();
    const priceText = rawPrice ? `R$ ${rawPrice}` : "R$ 0,00";
    const desc = (descInput?.value || "").trim() || "Sem descrição.";
    const ingr = (ingrInput?.value || "").trim() || "-";

    const card = document.createElement("div");
    card.className = "dish-card";

    // usa imagem blob preview se existir
    const imgHtml = imgPreview?.dataset?.objectUrl
      ? `<img class="dish-img" src="${
          imgPreview.dataset.objectUrl
        }" alt="${escapeHtml(name)}">`
      : "";

    card.innerHTML = `
      <button class="remove-btn" title="Remover">×</button>
      ${imgHtml}
      <h2 class="dish-name">${escapeHtml(name)}</h2>
      <div class="dish-price">${escapeHtml(priceText)}</div>
      <p class="dish-desc">${escapeHtml(desc)}</p>
      <div class="dish-ingredients">Ingredientes: ${escapeHtml(ingr)}</div>
    `;

    // remove handler
    const removeBtn = card.querySelector(".remove-btn");
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

    // Modificar handler de remoção
    removeBtn?.addEventListener("click", () => {
      showConfirm("Tem certeza que deseja remover este prato do cardápio?", () => {
        const img = card.querySelector(".dish-img");
        if (img?.src?.startsWith("blob:")) {
          try { URL.revokeObjectURL(img.src); } catch (e) {}
        }
        card.remove();
      });
    });

    menuGrid?.prepend(card);
    closeModal();
  });

  // escape básico para inserir texto no DOM com segurança
  function escapeHtml(s) {
    return String(s).replace(
      /[&<>"']/g,
      (m) =>
        ({
          "&": "&amp;",
          "<": "&lt;",
          ">": "&gt;",
          '"': "&quot;",
          "'": "&#39;",
        }[m])
    );
  }
});
const removeBtn = card.querySelector(".remove-btn");
removeBtn?.addEventListener("click", () => {
  const img = card.querySelector(".dish-img");
  if (img?.src?.startsWith("blob:")) {
    try {
      URL.revokeObjectURL(img.src);
    } catch (e) {}
  }
  card.remove();
});

menuGrid?.prepend(card);
closeModal();

// escape básico para inserir texto no DOM com segurança
function escapeHtml(s) {
  return String(s).replace(
    /[&<>"']/g,
    (m) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[
        m
      ])
  );
}
