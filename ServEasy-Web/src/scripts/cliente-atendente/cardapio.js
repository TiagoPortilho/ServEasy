// Estado da aplicação
let mesaSelecionada = null;
let produtosSelecionados = [];
let produtosPorCategoria = {};

// Produtos de exemplo (em uma aplicação real, viria de uma API)
const produtos = [
    {
        id: 1,
        nome: "Pizza Margherita",
        preco: 35.00,
        categoria: "pratos",
        descricao: "Clássica pizza italiana com molho de tomate, mussarela e manjericão fresco",
        ingredientes: "Massa artesanal, molho de tomate, mussarela, manjericão, azeite extra virgem",
        imagem: "../../assets/menu-img/pizza-margherita.jpg",
        disponivel: true
    },
    {
        id: 2,
        nome: "Hambúrguer Artesanal",
        preco: 28.00,
        categoria: "pratos",
        descricao: "Hambúrguer de carne bovina artesanal com queijo, alface, tomate e batata frita",
        ingredientes: "Pão brioche, hambúrguer 180g, queijo cheddar, alface, tomate, cebola roxa, batata frita",
        imagem: "../../assets/menu-img/hamburguer-artesanal.jpg",
        disponivel: true
    },
    {
        id: 3,
        nome: "Coca-Cola Lata",
        preco: 5.50,
        categoria: "bebidas",
        descricao: "Refrigerante Coca-Cola gelado em lata 350ml",
        ingredientes: "Água, açúcar, extrato de noz de cola, cafeína",
        imagem: null,
        disponivel: true
    },
    {
        id: 4,
        nome: "Suco de Laranja Natural",
        preco: 8.00,
        categoria: "bebidas",
        descricao: "Suco de laranja natural feito na hora, sem açúcar adicionado",
        ingredientes: "Laranjas frescas",
        imagem: null,
        disponivel: true
    },
    {
        id: 5,
        nome: "Tiramisu",
        preco: 12.00,
        categoria: "sobremesas",
        descricao: "Sobremesa italiana tradicional com café e mascarpone",
        ingredientes: "Biscoito champagne, café, mascarpone, ovos, açúcar, cacau em pó",
        imagem: null,
        disponivel: true
    },
    {
        id: 6,
        nome: "Bruschetta Italiana",
        preco: 15.00,
        categoria: "entradas",
        descricao: "Torrada italiana com tomate, manjericão e azeite",
        ingredientes: "Pão italiano, tomate, manjericão, alho, azeite extra virgem",
        imagem: null,
        disponivel: true
    }
];

// Mesas disponíveis (em uma aplicação real, viria de uma API)
const mesas = [
    { numero: 1, ocupada: false },
    { numero: 2, ocupada: true },
    { numero: 3, ocupada: false },
    { numero: 4, ocupada: false },
    { numero: 5, ocupada: true },
    { numero: 6, ocupada: false },
    { numero: 7, ocupada: false },
    { numero: 8, ocupada: false },
    { numero: 9, ocupada: false },
    { numero: 10, ocupada: false }
];

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    verificarMesaSalva();
    carregarProdutos();
    setupEventListeners();
});

function verificarMesaSalva() {
    const mesaSalva = localStorage.getItem('mesaSelecionada');
    if (mesaSalva) {
        mesaSelecionada = parseInt(mesaSalva);
        mostrarMesaAtiva();
    }
}

function mostrarMesaAtiva() {
    const mesaAtiva = document.getElementById('mesaAtiva');
    const btnSelecionarMesa = document.getElementById('btnSelecionarMesa');
    const mesaNumero = document.getElementById('mesaNumero');
    
    if (mesaSelecionada) {
        mesaNumero.textContent = `Mesa ${mesaSelecionada}`;
        mesaAtiva.style.display = 'flex';
        btnSelecionarMesa.style.display = 'none';
    } else {
        mesaAtiva.style.display = 'none';
        btnSelecionarMesa.style.display = 'flex';
    }
}

function selecionarMesa() {
    carregarMesas();
    document.getElementById('modalMesa').style.display = 'flex';
}

function trocarMesa() {
    selecionarMesa();
}

function carregarMesas() {
    const mesasGrid = document.getElementById('mesasGrid');
    mesasGrid.innerHTML = '';
    
    mesas.forEach(mesa => {
        const mesaBtn = document.createElement('button');
        mesaBtn.className = 'mesa-btn';
        mesaBtn.textContent = mesa.numero;
        mesaBtn.dataset.mesa = mesa.numero;
        
        if (mesa.ocupada) {
            mesaBtn.classList.add('ocupada');
            mesaBtn.disabled = true;
        }
        
        mesaBtn.addEventListener('click', function() {
            if (!mesa.ocupada) {
                // Remove seleção anterior
                document.querySelectorAll('.mesa-btn').forEach(btn => {
                    btn.classList.remove('selecionada');
                });
                
                // Adiciona seleção atual
                mesaBtn.classList.add('selecionada');
                
                // Habilita botão de confirmar
                document.getElementById('btnConfirmarMesa').disabled = false;
            }
        });
        
        mesasGrid.appendChild(mesaBtn);
    });
}

function confirmarMesa() {
    const mesaSelecionadaElement = document.querySelector('.mesa-btn.selecionada');
    if (mesaSelecionadaElement) {
        mesaSelecionada = parseInt(mesaSelecionadaElement.dataset.mesa);
        localStorage.setItem('mesaSelecionada', mesaSelecionada);
        mostrarMesaAtiva();
        cancelarSelecaoMesa();
        
        // Notificação de sucesso
        mostrarNotificacao(`Mesa ${mesaSelecionada} selecionada com sucesso!`, 'success');
    }
}

function cancelarSelecaoMesa() {
    document.getElementById('modalMesa').style.display = 'none';
    document.getElementById('btnConfirmarMesa').disabled = true;
}

function carregarProdutos() {
    const produtosGrid = document.getElementById('produtosGrid');
    const emptyMessage = document.getElementById('emptyMessage');
    
    produtosGrid.innerHTML = '';
    
    const filtroAtivo = document.querySelector('.filter-btn.active').dataset.filter;
    const termoBusca = document.getElementById('searchInput').value.toLowerCase();
    
    let produtosFiltrados = produtos.filter(produto => {
        const correspondeCategoria = filtroAtivo === 'todos' || produto.categoria === filtroAtivo;
        const correspondeBusca = !termoBusca || 
            produto.nome.toLowerCase().includes(termoBusca) ||
            produto.descricao.toLowerCase().includes(termoBusca) ||
            produto.ingredientes.toLowerCase().includes(termoBusca);
        
        return correspondeCategoria && correspondeBusca && produto.disponivel;
    });
    
    if (produtosFiltrados.length === 0) {
        emptyMessage.classList.remove('hidden');
        return;
    } else {
        emptyMessage.classList.add('hidden');
    }
    
    produtosFiltrados.forEach(produto => {
        const produtoCard = criarCardProduto(produto);
        produtosGrid.appendChild(produtoCard);
    });
}

function criarCardProduto(produto) {
    const card = document.createElement('div');
    card.className = 'produto-card';
    
    card.innerHTML = `
        ${produto.imagem ? `<img src="${produto.imagem}" alt="${produto.nome}" class="produto-img">` : ''}
        <h3 class="produto-nome">${produto.nome}</h3>
        <div class="produto-preco">R$ ${produto.preco.toFixed(2)}</div>
        <p class="produto-descricao">${produto.descricao}</p>
        <div class="produto-ingredientes">Ingredientes: ${produto.ingredientes}</div>
        <div class="produto-acoes">
            <div class="number-wrapper">
                <input type="number" class="quantidade-input" value="1" min="1" max="10" id="qty-${produto.id}">
                <div class="spinner">
                    <button type="button" onclick="aumentarQuantidade('qty-${produto.id}')">▲</button>
                    <button type="button" onclick="diminuirQuantidade('qty-${produto.id}')">▼</button>
                </div>
            </div>
            <button class="btn-adicionar" onclick="adicionarAoPedido(${produto.id})">
                <i class="fas fa-plus"></i>
                Adicionar
            </button>
        </div>
    `;
    
    return card;
}

function adicionarAoPedido(produtoId) {
    if (!mesaSelecionada) {
        mostrarNotificacao('Selecione uma mesa antes de fazer pedidos!', 'warning');
        selecionarMesa();
        return;
    }
    
    const produto = produtos.find(p => p.id === produtoId);
    const quantidade = parseInt(document.getElementById(`qty-${produtoId}`).value);
    
    const itemExistente = produtosSelecionados.find(item => item.id === produtoId);
    
    if (itemExistente) {
        itemExistente.quantidade += quantidade;
    } else {
        produtosSelecionados.push({
            ...produto,
            quantidade: quantidade
        });
    }
    
    mostrarNotificacao(`${produto.nome} adicionado ao pedido!`, 'success');
}

// Funções para controlar os spinners customizados
function aumentarQuantidade(inputId) {
    const input = document.getElementById(inputId);
    const max = parseInt(input.max) || 10;
    if (parseInt(input.value) < max) {
        input.value = parseInt(input.value) + 1;
    }
}

function diminuirQuantidade(inputId) {
    const input = document.getElementById(inputId);
    const min = parseInt(input.min) || 1;
    if (parseInt(input.value) > min) {
        input.value = parseInt(input.value) - 1;
    }
}

function atualizarContadorPedido() {
    // Aqui podemos adicionar um indicador visual do número de itens no pedido
    const totalItens = produtosSelecionados.reduce((total, item) => total + item.quantidade, 0);
    if (totalItens > 0) {
        // Poderia adicionar um badge ou contador na interface
    }
}

function mostrarModalConfirmacao() {
    if (produtosSelecionados.length === 0) {
        mostrarNotificacao('Adicione pelo menos um item ao pedido!', 'warning');
        return;
    }
    
    const detalhesPedido = document.getElementById('detalhesPedido');
    let total = 0;
    
    let html = `
        <div style="margin-bottom: 20px;">
            <h4 style="color: #fca311; margin-bottom: 10px;">Mesa ${mesaSelecionada}</h4>
        </div>
        <div style="max-height: 300px; overflow-y: auto;">
    `;
    
    produtosSelecionados.forEach(item => {
        const subtotal = item.preco * item.quantidade;
        total += subtotal;
        
        html += `
            <div style="display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #333;">
                <div>
                    <strong>${item.nome}</strong><br>
                    <small style="color: #999;">Qtd: ${item.quantidade} x R$ ${item.preco.toFixed(2)}</small>
                </div>
                <div style="color: #28a745; font-weight: 600;">
                    R$ ${subtotal.toFixed(2)}
                </div>
            </div>
        `;
    });
    
    html += `
        </div>
        <div style="border-top: 2px solid #fca311; margin-top: 15px; padding-top: 15px;">
            <div style="display: flex; justify-content: space-between; align-items: center; font-size: 1.2rem; font-weight: 700;">
                <span>Total:</span>
                <span style="color: #28a745;">R$ ${total.toFixed(2)}</span>
            </div>
        </div>
    `;
    
    detalhesPedido.innerHTML = html;
    document.getElementById('modalPedido').style.display = 'flex';
}

function confirmarPedido() {
    // Simular envio do pedido (em uma aplicação real, faria uma requisição para o servidor)
    const pedido = {
        mesa: mesaSelecionada,
        itens: produtosSelecionados,
        total: produtosSelecionados.reduce((total, item) => total + (item.preco * item.quantidade), 0),
        timestamp: new Date().toISOString()
    };
    
    // Salvar pedidos no localStorage (em uma aplicação real, enviaria para o servidor)
    let pedidosSalvos = JSON.parse(localStorage.getItem('pedidos') || '[]');
    pedido.numero = pedidosSalvos.length + 1;
    pedido.status = 'pendente';
    pedidosSalvos.push(pedido);
    localStorage.setItem('pedidos', JSON.stringify(pedidosSalvos));
    
    // Limpar carrinho
    produtosSelecionados = [];
    cancelarPedido();
    
    mostrarNotificacao(`Pedido #${pedido.numero} confirmado com sucesso!`, 'success');
    
    // Redirecionar para a página de pedidos após 2 segundos
    setTimeout(() => {
        window.location.href = 'seus-pedidos.html';
    }, 2000);
}

function cancelarPedido() {
    document.getElementById('modalPedido').style.display = 'none';
}

function setupEventListeners() {
    // Filtros de categoria
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            carregarProdutos();
        });
    });
    
    // Busca
    document.getElementById('searchInput').addEventListener('input', carregarProdutos);
    
    // Adicionar botão flutuante para ver pedido se houver itens
    setInterval(() => {
        if (produtosSelecionados.length > 0) {
            if (!document.getElementById('btnVerPedido')) {
                const btnVerPedido = document.createElement('button');
                btnVerPedido.id = 'btnVerPedido';
                btnVerPedido.innerHTML = `
                    <i class="fas fa-shopping-cart"></i>
                    Ver Pedido (${produtosSelecionados.length})
                `;
                btnVerPedido.style.cssText = `
                    position: fixed;
                    bottom: 20px;
                    right: 20px;
                    background: linear-gradient(135deg, #28a745, #20c997);
                    color: white;
                    border: none;
                    padding: 15px 20px;
                    border-radius: 50px;
                    font-weight: 600;
                    cursor: pointer;
                    box-shadow: 0 4px 15px rgba(40, 167, 69, 0.4);
                    z-index: 999;
                    transition: all 0.3s ease;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                `;
                
                btnVerPedido.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-2px)';
                    this.style.boxShadow = '0 6px 20px rgba(40, 167, 69, 0.5)';
                });
                
                btnVerPedido.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0)';
                    this.style.boxShadow = '0 4px 15px rgba(40, 167, 69, 0.4)';
                });
                btnVerPedido.addEventListener('click', mostrarModalConfirmacao);
                document.body.appendChild(btnVerPedido);
            } else {
                document.getElementById('btnVerPedido').innerHTML = `
                    <i class="fas fa-shopping-cart"></i>
                    Ver Pedido (${produtosSelecionados.length})
                `;
            }
        } else {
            const btnExistente = document.getElementById('btnVerPedido');
            if (btnExistente) {
                btnExistente.remove();
            }
        }
    }, 1000);
}

function mostrarNotificacao(mensagem, tipo = 'info') {
    const notificacao = document.createElement('div');
    notificacao.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${tipo === 'success' ? '#28a745' : tipo === 'warning' ? '#ffc107' : '#17a2b8'};
        color: ${tipo === 'warning' ? '#000' : '#fff'};
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        z-index: 9999;
        max-width: 300px;
        word-wrap: break-word;
        font-weight: 500;
    `;
    notificacao.textContent = mensagem;
    
    document.body.appendChild(notificacao);
    
    setTimeout(() => {
        notificacao.remove();
    }, 3000);
}