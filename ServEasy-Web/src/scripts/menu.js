const addBtn = document.querySelector(".add-btn");
  const modalOverlay = document.getElementById("modalOverlay");
  const cancelBtn = document.getElementById("cancelBtn");
  const saveBtn = document.getElementById("saveBtn");

  // Abrir modal
  addBtn.addEventListener("click", () => {
    modalOverlay.style.display = "flex";
  });

  // Fechar modal (cancelar)
  cancelBtn.addEventListener("click", () => {
    modalOverlay.style.display = "none";
  });

  // Fechar clicando fora
  modalOverlay.addEventListener("click", (e) => {
    if (e.target === modalOverlay) {
      modalOverlay.style.display = "none";
    }
  });

  // Salvar prato ficticiamente
  saveBtn.addEventListener("click", () => {
    const name = document.getElementById("dishName").value;
    const price = document.getElementById("dishPrice").value;
    const desc = document.getElementById("dishDesc").value;
    const ingr = document.getElementById("dishIngr").value;

    alert(`Prato salvo ficticiamente:\n\n${name} - R$${price}\n${desc}\nIngredientes: ${ingr}`);
    
    modalOverlay.style.display = "none";
  });