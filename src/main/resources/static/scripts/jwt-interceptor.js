// JWT Interceptor - Adiciona automaticamente o token JWT em todas as requisições
(function() {
    console.log('[JWT-Interceptor] Inicializando interceptor...');

    // Flag global para indicar que o interceptor está carregado
    window.jwtInterceptorLoaded = true;

    // Intercepta o fetch global
    const originalFetch = window.fetch;

    window.fetch = function(url, options = {}) {
        console.log('[JWT-Interceptor] Interceptando requisição para:', url, 'Method:', options.method || 'GET');

        // Determinar se é endpoint público baseado na URL e método HTTP
        const method = (options.method || 'GET').toUpperCase();
        const staticFileExtensions = ['.css', '.js', '.png', '.jpg', '.jpeg', '.gif', '.svg', '.ico', '.woff', '.woff2', '.ttf', '.eot', '.otf', '.mp4', '.mp3', '.pdf', '.txt'];
        const isStaticFile = staticFileExtensions.some(ext => url.includes(ext)) || url.includes('/assets/') || url.includes('/styles/') || url.includes('/scripts/') || url.includes('/static/');
        
        // Endpoints completamente públicos (qualquer método)
        const alwaysPublicEndpoints = ['/api/auth/login'];
        const isAlwaysPublic = alwaysPublicEndpoints.some(endpoint => url.includes(endpoint));
        
        // Endpoints públicos apenas para GET (leitura)
        const publicGetEndpoints = ['/api/feedbacks', '/api/menu'];
        const isPublicGet = method === 'GET' && publicGetEndpoints.some(endpoint => url.includes(endpoint));
        
        const isPublicEndpoint = isAlwaysPublic || isPublicGet;
        
        if (!isPublicEndpoint && !isStaticFile) {
            const token = localStorage.getItem('jwt_token');
            console.log('[JWT-Interceptor] Token encontrado:', token ? 'SIM' : 'NÃO');

            if (token) {
                options.headers = {
                    ...options.headers,
                    'Authorization': `Bearer ${token}`
                };
                console.log('[JWT-Interceptor] Token adicionado ao header Authorization');
            } else {
                console.warn('[JWT-Interceptor] Nenhum token encontrado para requisição protegida:', url);
            }
        } else {
            console.log('[JWT-Interceptor] Endpoint público ou arquivo estático, não adicionando token');
        }

        return originalFetch(url, options)
            .then(response => {
                console.log('[JWT-Interceptor] Resposta recebida:', response.status, url);
                // Se receber 401 em uma requisição para API, verificar se deve redirecionar
                if (response.status === 401 && url.includes('/api/')) {
                    console.warn('[JWT-Interceptor] Recebido 401 em API, verificando se deve redirecionar...');
                    // Verificar se estamos em uma página protegida
                    const currentPath = window.location.pathname;
                    const protectedPaths = ['/admin', '/cozinheiro', '/cliente-atendente'];
                    const isProtectedPage = protectedPaths.some(path => currentPath.startsWith(path));

                    if (isProtectedPage) {
                        const token = localStorage.getItem('jwt_token');
                        if (!token) {
                            console.warn('[JWT-Interceptor] Sem token em página protegida, redirecionando para login');
                            localStorage.removeItem('jwt_token');
                            localStorage.removeItem('user_role');
                            localStorage.removeItem('username');
                            window.location.href = '/login';
                        } else {
                            console.warn('[JWT-Interceptor] Token existe mas 401 recebido - possível token expirado');
                        }
                    }
                }
                return response;
            })
            .catch(error => {
                console.error('[JWT-Interceptor] Erro na requisição:', error);
                throw error;
            });
    };

    // Verificar se o usuário está logado ao carregar páginas protegidas
    function verificarAutenticacao() {
        const token = localStorage.getItem('jwt_token');
        const role = localStorage.getItem('user_role');
        const currentPath = window.location.pathname;

        console.log('[JWT-Interceptor] Verificando autenticação - Path:', currentPath, 'Token:', token ? 'SIM' : 'NÃO', 'Role:', role);

        // Lista de páginas que requerem autenticação
        const protectedPaths = ['/admin', '/cozinheiro', '/cliente-atendente'];
        const isProtectedPath = protectedPaths.some(path => currentPath.startsWith(path));

        if (isProtectedPath) {
            if (!token) {
                console.warn('[JWT-Interceptor] Página protegida sem token, redirecionando para login');
                window.location.href = '/login';
                return false;
            }

            // Verificar se o usuário tem permissão para acessar esta página
            // Ser mais permissivo - permitir acesso às páginas do perfil
            const rolePaths = {
                'COZINHEIRO': '/cozinheiro',
                'ADMIN': '/admin',
                'CLIENTE_ATENDENTE': '/cliente-atendente'
            };

            const expectedPath = rolePaths[role];
            if (expectedPath && !currentPath.startsWith(expectedPath)) {
                console.warn('[JWT-Interceptor] Usuário tentando acessar página de outro perfil. Role:', role, 'Expected path:', expectedPath, 'Current path:', currentPath);
                // Redirecionar para a página inicial do perfil correto
                window.location.href = expectedPath + (role === 'COZINHEIRO' ? '/novos-pedidos' :
                                                     role === 'ADMIN' ? '/dashboard' :
                                                     '/cardapio');
                return false;
            }
        }

        return true;
    }

    // Verificar autenticação quando a página carregar
    document.addEventListener('DOMContentLoaded', function() {
        // Delay maior para garantir que o localStorage foi salvo após redirecionamento
        setTimeout(() => {
            console.log('[JWT-Interceptor] Iniciando verificação de autenticação...');
            verificarAutenticacao();
        }, 500); // Aumentado para 500ms
    });

    // Função para fazer logout
    window.logout = function() {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_role');
        localStorage.removeItem('username');
        window.location.href = '/login';
    };

})();