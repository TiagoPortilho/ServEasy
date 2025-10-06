// Dashboard Admin - Apenas dados reais da API
(function () {
  const ganhosValor = document.getElementById("ganhosValor");
  const metaPedidos = document.getElementById("metaPedidos");
  const metaTicket = document.getElementById("metaTicket");
  const ganhosHora = document.getElementById("ganhosHora");
  const statVendas = document.getElementById("statVendas");
  const statClientes = document.getElementById("statClientes");
  const statCancel = document.getElementById("statCancel");
  const logArea = document.getElementById("logArea");
  const toggleOps = document.getElementById("toggleOps");
  const opsState = document.getElementById("opsState");
  const lastChange = document.getElementById("lastChange");

  function moneyBR(num) {
    return num.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  }

  async function carregarDadosReais() {
    try {
      const response = await fetch('/api/dashboard/stats');
      const result = await response.json();
      
      if (result.status === 'success') {
        const stats = result.data;
        
        if (ganhosValor) ganhosValor.textContent = moneyBR(stats.totalRevenue);
        if (metaPedidos) metaPedidos.textContent = stats.totalOrders;
        if (metaTicket) metaTicket.textContent = moneyBR(stats.averageTicket);
        if (ganhosHora) ganhosHora.textContent = new Date().toLocaleTimeString();
        if (statVendas) statVendas.textContent = stats.totalOrders;
        if (statClientes) statClientes.textContent = stats.uniqueCustomers;
        if (statCancel) statCancel.textContent = stats.cancelledOrders;
        
        atualizarLog("Dados atualizados com sucesso");
      } else {
        mostrarDadosVazios();
      }
    } catch (error) {
      console.error('Erro na requisição:', error);
      mostrarDadosVazios();
    }
  }

  function mostrarDadosVazios() {
    if (ganhosValor) ganhosValor.textContent = moneyBR(0);
    if (metaPedidos) metaPedidos.textContent = "0";
    if (metaTicket) metaTicket.textContent = moneyBR(0);
    if (ganhosHora) ganhosHora.textContent = new Date().toLocaleTimeString();
    if (statVendas) statVendas.textContent = "0";
    if (statClientes) statClientes.textContent = "0";
    if (statCancel) statCancel.textContent = "0";
    atualizarLog("Aguardando dados do sistema");
  }

  function atualizarLog(mensagem) {
    if (!logArea) return;
    logArea.innerHTML = '<div style="font-weight:700; margin-bottom:0.25rem;">Log de Ações</div><div class="entry">' + new Date().toLocaleString() + ' — ' + mensagem + '</div>';
  }

  carregarDadosReais();

  let aberto = false;
  if (toggleOps) {
    toggleOps.addEventListener("click", function() {
      aberto = !aberto;
      if (aberto) {
        toggleOps.textContent = "Fechar Operações";
        toggleOps.classList.remove("off");
        if (opsState) {
          opsState.textContent = "Aberto";
          opsState.style.color = "var(--body-text)";
        }
        if (lastChange) lastChange.textContent = new Date().toLocaleString();
        atualizarLog("Operações iniciadas");
      } else {
        toggleOps.textContent = "Iniciar Operações";
        toggleOps.classList.add("off");
        if (opsState) {
          opsState.textContent = "Fechado";
          opsState.style.color = "inherit";
        }
        if (lastChange) lastChange.textContent = new Date().toLocaleString();
        atualizarLog("Operações finalizadas");
      }
    });
  }

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", function() {
      atualizarLog("Logout realizado");
      window.location.href = "/login";
    });
  }

  setInterval(carregarDadosReais, 60000);
})();