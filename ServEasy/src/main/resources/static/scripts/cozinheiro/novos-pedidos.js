document.addEventListener('DOMContentLoaded', async function() {document.addEventListener("DOMContentLoaded", async function() {

    const container = document.getElementById('novosList');    const container = document.getElementById("novosList");

        if (container) {

    if (container) {        await loadNovosPedidos();

        await loadNovosPedidos();        setInterval(loadNovosPedidos, 30000);

        setInterval(loadNovosPedidos, 30000);    }

    }});

});

async function loadNovosPedidos() {

async function loadNovosPedidos() {    try {

    try {        const response = await fetch("/api/orders?status=PENDING");

        const response = await fetch('/api/orders?status=PENDING');        if (!response.ok) throw new Error("HTTP error! status: " + response.status);

                const orders = await response.json();

        if (!response.ok) {        displayNovosPedidos(orders);

            throw new Error('HTTP error! status: ' + response.status);    } catch (error) {

        }        console.error("Erro ao carregar novos pedidos:", error);

                displayError("Erro ao carregar pedidos");

        const orders = await response.json();    }

        displayNovosPedidos(orders);}

        

    } catch (error) {function displayNovosPedidos(orders) {

        console.error('Erro ao carregar novos pedidos:', error);    const container = document.getElementById("novosList");

        displayError('Erro ao carregar pedidos');    if (!container) return;

    }    

}    if (!orders || orders.length === 0) {

        container.innerHTML = `<div class="col-12"><div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> Nenhum novo pedido no momento</div></div>`;

function displayNovosPedidos(orders) {        return;

    const container = document.getElementById('novosList');    }

        

    if (!container) {    container.innerHTML = orders.map(order => `

        return;        <div class="col-md-6 col-lg-4">

    }            <div class="card mb-3">

                    <div class="card-header d-flex justify-content-between">

    if (!orders || orders.length === 0) {                    <h5>Pedido #${order.id}</h5>

        container.innerHTML = '<div class="col-12"><div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> Nenhum novo pedido no momento</div></div>';                    <span class="badge bg-primary">Mesa ${order.table?.tableNumber || "N/A"}</span>

        return;                </div>

    }                <div class="card-body">

                        <div class="mb-2"><small class="text-muted"><i class="fas fa-clock"></i> ${formatDateTime(order.orderDate)}</small></div>

    container.innerHTML = orders.map(function(order) {                    <div class="order-items mb-3">

        return `                        ${order.items?.map(item => `<div class="d-flex justify-content-between mb-2"><div><span class="fw-bold">${item.quantity}x</span> ${item.menuItem?.name || "Item não encontrado"}</div><span>R$ ${(item.price || 0).toFixed(2)}</span></div>`).join("") || "<p class=\"text-muted\">Sem itens</p>"}

            <div class="col-md-6 col-lg-4">                    </div>

                <div class="card mb-3">                    <div class="d-flex justify-content-between">

                    <div class="card-header d-flex justify-content-between align-items-center">                        <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>

                        <h5 class="mb-0">Pedido #${order.id}</h5>                        <button class="btn btn-success btn-sm" onclick="iniciarPreparo(${order.id})"><i class="fas fa-play"></i> Iniciar Preparo</button>

                        <span class="badge bg-primary">Mesa ${order.table ? order.table.tableNumber : 'N/A'}</span>                    </div>

                    </div>                </div>

                    <div class="card-body">            </div>

                        <div class="mb-2">        </div>

                            <small class="text-muted">    `).join("");

                                <i class="fas fa-clock"></i>}

                                ${formatDateTime(order.orderDate)}

                            </small>async function iniciarPreparo(orderId) {

                        </div>    try {

                                const response = await fetch(`/api/orders/${orderId}/status`, {

                        <div class="order-items mb-3">            method: "PUT",

                            ${order.items ? order.items.map(function(item) {            headers: {"Content-Type": "application/json"},

                                return `            body: JSON.stringify({status: "PREPARING"})

                                    <div class="item-row d-flex justify-content-between align-items-center mb-2">        });

                                        <div>        if (!response.ok) throw new Error("HTTP error! status: " + response.status);

                                            <span class="fw-bold">${item.quantity}x</span>        await loadNovosPedidos();

                                            <span>${item.menuItem ? item.menuItem.name : 'Item não encontrado'}</span>        showSuccess("Pedido movido para preparo!");

                                        </div>    } catch (error) {

                                        <span class="text-primary">R$ ${(item.price || 0).toFixed(2)}</span>        console.error("Erro:", error);

                                    </div>        alert("Erro ao iniciar preparo");

                                `;    }

                            }).join('') : '<p class="text-muted">Sem itens</p>'}}

                        </div>

                        function formatDateTime(dateString) {

                        <div class="d-flex justify-content-between align-items-center">    if (!dateString) return "Data inválida";

                            <div class="total">    try {

                                <strong>Total: R$ ${(order.totalAmount || 0).toFixed(2)}</strong>        return new Date(dateString).toLocaleString("pt-BR", {day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit"});

                            </div>    } catch (error) {

                            <button class="btn btn-success btn-sm"         return "Data inválida";

                                    onclick="iniciarPreparo(${order.id})">    }

                                <i class="fas fa-play"></i>}

                                Iniciar Preparo

                            </button>function displayError(message) {

                        </div>    const container = document.getElementById("novosList");

                    </div>    if (container) container.innerHTML = `<div class="col-12"><div class="alert alert-danger"><i class="fas fa-exclamation-triangle"></i> ${message}</div></div>`;

                </div>}

            </div>

        `;function showSuccess(message) {

    }).join('');    const toast = document.createElement("div");

}    toast.className = "toast align-items-center text-white bg-success border-0 show";

    toast.innerHTML = `<div class="d-flex"><div class="toast-body">${message}</div></div>`;

async function iniciarPreparo(orderId) {    document.body.appendChild(toast);

    if (!orderId) {    setTimeout(() => toast.remove(), 3000);

        alert('ID do pedido inválido');}

        return;
    }
    
    try {
        const response = await fetch('/api/orders/' + orderId + '/status', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: 'PREPARING' })
        });
        
        if (!response.ok) {
            throw new Error('HTTP error! status: ' + response.status);
        }
        
        await loadNovosPedidos();
        showSuccess('Pedido movido para preparo com sucesso!');
        
    } catch (error) {
        console.error('Erro ao iniciar preparo:', error);
        alert('Erro ao iniciar preparo do pedido');
    }
}

function formatDateTime(dateString) {
    if (!dateString) return 'Data inválida';
    
    try {
        const date = new Date(dateString);
        return date.toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (error) {
        return 'Data inválida';
    }
}

function displayError(message) {
    const container = document.getElementById('novosList');
    if (container) {
        container.innerHTML = '<div class="col-12"><div class="alert alert-danger text-center"><i class="fas fa-exclamation-triangle"></i> ' + message + '</div></div>';
    }
}

function showSuccess(message) {
    const toast = document.createElement('div');
    toast.className = 'toast align-items-center text-white bg-success border-0 show';
    toast.innerHTML = '<div class="d-flex"><div class="toast-body"><i class="fas fa-check-circle"></i> ' + message + '</div></div>';
    toast.style.position = 'fixed';
    toast.style.top = '20px';
    toast.style.right = '20px';
    toast.style.zIndex = '9999';
    document.body.appendChild(toast);
    setTimeout(function() { toast.remove(); }, 3000);
}