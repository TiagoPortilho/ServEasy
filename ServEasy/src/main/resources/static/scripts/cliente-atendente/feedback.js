// Estado da aplicação
let mesaSelecionada = null;
let abaAtiva = 'feedback';
let avaliacaoSelecionada = 0;

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    verificarMesaSalva();
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
    const mesaNumero = document.getElementById('mesaNumero');
    
    if (mesaSelecionada) {
        mesaNumero.textContent = `Mesa ${mesaSelecionada}`;
        mesaAtiva.style.display = 'flex';
    } else {
        mesaAtiva.style.display = 'none';
    }
}

function trocarAba(aba) {
    // Remover active de todas as abas
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.style.display = 'none');
    
    // Ativar a aba selecionada
    document.querySelector(`[data-tab="${aba}"]`).classList.add('active');
    document.getElementById(`tab-${aba}`).style.display = 'block';
    
    abaAtiva = aba;
}

function setupEventListeners() {
    // Avaliação com estrelas
    document.querySelectorAll('#ratingStars i').forEach((star, index) => {
        star.addEventListener('click', function() {
            avaliacaoSelecionada = index + 1;
            atualizarEstrelas();
        });
        
        star.addEventListener('mouseenter', function() {
            destacarEstrelas(index + 1);
        });
    });
    
    document.getElementById('ratingStars').addEventListener('mouseleave', function() {
        atualizarEstrelas();
    });
    
    // Forms
    document.getElementById('feedbackForm').addEventListener('submit', enviarFeedback);
    document.getElementById('ajudaForm').addEventListener('submit', enviarSolicitacaoAjuda);
}

function destacarEstrelas(quantidade) {
    document.querySelectorAll('#ratingStars i').forEach((star, index) => {
        if (index < quantidade) {
            star.className = 'fas fa-star active';
        } else {
            star.className = 'far fa-star';
        }
    });
    
    const textos = ['', 'Muito Ruim', 'Ruim', 'Regular', 'Bom', 'Excelente'];
    document.getElementById('ratingText').textContent = textos[quantidade] || 'Selecione uma avaliação';
}

function atualizarEstrelas() {
    destacarEstrelas(avaliacaoSelecionada);
}

function enviarFeedback(e) {
    e.preventDefault();
    
    const tipoFeedback = document.getElementById('tipoFeedback').value;
    const nomeCliente = document.getElementById('nomeCliente').value || 'Anônimo';
    const comentario = document.getElementById('comentarioFeedback').value;
    
    if (!tipoFeedback || !comentario || avaliacaoSelecionada === 0) {
        mostrarNotificacao('Por favor, preencha todos os campos obrigatórios e selecione uma avaliação!', 'warning');
        return;
    }
    
    const feedback = {
        id: Date.now(),
        tipo: 'feedback',
        categoria: tipoFeedback,
        nome: nomeCliente,
        comentario: comentario,
        avaliacao: avaliacaoSelecionada,
        mesa: mesaSelecionada,
        dataEnvio: new Date().toISOString(),
        status: 'enviado'
    };
    
    // Limpar formulário
    document.getElementById('feedbackForm').reset();
    avaliacaoSelecionada = 0;
    atualizarEstrelas();
    
    // Mostrar modal de sucesso
    document.getElementById('mensagemSucesso').textContent = 'Seu feedback foi enviado com sucesso. Obrigado por sua opinião!';
    document.getElementById('modalSucesso').style.display = 'flex';
}

function enviarSolicitacaoAjuda(e) {
    e.preventDefault();
    
    const tipoProblema = document.getElementById('tipoProblema').value;
    const prioridade = document.getElementById('prioridadeAjuda').value;
    const descricao = document.getElementById('descricaoProblema').value;
    const numeroPedido = document.getElementById('numeroPedidoAjuda').value;
    
    if (!tipoProblema || !prioridade || !descricao) {
        mostrarNotificacao('Por favor, preencha todos os campos obrigatórios!', 'warning');
        return;
    }
    
    const solicitacao = {
        id: Date.now(),
        tipo: 'ajuda',
        categoria: tipoProblema,
        prioridade: prioridade,
        descricao: descricao,
        numeroPedido: numeroPedido || null,
        mesa: mesaSelecionada,
        dataEnvio: new Date().toISOString(),
        status: 'pendente'
    };
    
    // Limpar formulário
    document.getElementById('ajudaForm').reset();
    
    // Mostrar modal de sucesso
    const mensagemPrioridade = {
        'baixa': 'Sua solicitação foi enviada e será atendida em breve.',
        'media': 'Sua solicitação foi enviada com prioridade média.',
        'alta': 'Sua solicitação foi enviada com alta prioridade. Aguarde o atendimento.',
        'urgente': 'Sua solicitação urgente foi enviada! Nossa equipe irá atendê-lo imediatamente.'
    };
    
    document.getElementById('mensagemSucesso').textContent = mensagemPrioridade[prioridade];
    document.getElementById('modalSucesso').style.display = 'flex';
}

function fecharModalSucesso() {
    document.getElementById('modalSucesso').style.display = 'none';
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