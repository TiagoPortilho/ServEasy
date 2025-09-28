(function () {
        const rand = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

        // elementos
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

        // populador fictício inicial
        function preencherDemo() {
          const pedidos = rand(8, 46);
          const ticket = rand(1900, 5400) / 100;
          const total = pedidos * ticket;
          const vendas = pedidos;
          const clientes = Math.max(1, Math.floor(pedidos * (0.6 + Math.random() * 0.5)));
          const cancelamentos = rand(0, 3);

          ganhosValor.textContent = moneyBR(total);
          metaPedidos.textContent = pedidos;
          metaTicket.textContent = moneyBR(ticket);
          ganhosHora.textContent = new Date().toLocaleTimeString();

          statVendas.textContent = vendas;
          statClientes.textContent = clientes;
          statCancel.textContent = cancelamentos;

          // preencher log com entradas fictícias
          const actions = [
            "Pedido #1023 confirmado",
            "Pagamento recebido — Pedido #1021",
            "Atualizado cardápio: adicionou 'Combo Especial'",
            "Usuário Tiago atualizou perfil",
            "Promoção 10% aplicada em categoria Pizzas",
            "Pedido #1019 cancelado (cliente)",
            "Backup diário concluído"
          ];
          logArea.innerHTML = '<div style="font-weight:700; margin-bottom:0.25rem;">Log de Ações</div>';
          const now = new Date();
          for (let i = 0; i < 6; i++) {
            const time = new Date(now.getTime() - i * 1000 * 60 * rand(5, 40));
            const entry = document.createElement("div");
            entry.className = "entry";
            entry.textContent = `${time.toLocaleString()} — ${actions[rand(0, actions.length - 1)]}`;
            logArea.appendChild(entry);
          }
        }

        preencherDemo();

        // toggle abrir/fechar (visual apenas)
        let aberto = false;
        toggleOps.addEventListener("click", () => {
          aberto = !aberto;
          if (aberto) {
            toggleOps.textContent = "Fechar Operações";
            toggleOps.classList.remove("off");
            opsState.textContent = "Aberto";
            opsState.style.color = "(--body-text)";
            lastChange.textContent = new Date().toLocaleString();
            // adiciona ao log
            const e = document.createElement("div");
            e.className = "entry";
            e.textContent = `${new Date().toLocaleString()} — Operações iniciadas (abertura)`;
            logArea.insertBefore(e, logArea.children[1]);
          } else {
            toggleOps.textContent = "Iniciar Operações";
            toggleOps.classList.add("off");
            opsState.textContent = "Fechado";
            opsState.style.color = "inherit";
            lastChange.textContent = new Date().toLocaleString();
            const e = document.createElement("div");
            e.className = "entry";
            e.textContent = `${new Date().toLocaleString()} — Operações finalizadas (fechamento)`;
            logArea.insertBefore(e, logArea.children[1]);
          }
        });

        // logout (fictício)
        document.getElementById("logoutBtn").addEventListener("click", () => {
          const e = document.createElement("div");
          e.className = "entry";
          e.textContent = `${new Date().toLocaleString()} — Usuário efetuou logout (demo)`;
          logArea.insertBefore(e, logArea.children[1]);
          alert("Logout (fictício) executado. Implementar backend depois.");
        });

        // atualiza demo a cada 90s (fictício)
        setInterval(() => {
          preencherDemo();
          const e = document.createElement("div");
          e.className = "entry";
          e.textContent = `${new Date().toLocaleString()} — Atualização automática de dados (demo)`;
          logArea.insertBefore(e, logArea.children[1]);
        }, 90_000);
      })();