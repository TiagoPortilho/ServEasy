let user = document.getElementById("username");
let password = document.getElementById("password");

async function login(event) {
  event.preventDefault();
  
  if (!user.value.trim() || !password.value.trim()) {
    alert('Por favor, preencha usuário e senha.');
    return;
  }

  const loginData = {
    username: user.value.trim(),
    password: password.value.trim()
  };

  console.log('Fazendo login...', loginData.username); // Debug

  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData)
    });

    console.log('Response status:', response.status); // Debug

    if (response.ok) {
      const result = await response.json();
      console.log('Login response:', result); // Debug
      
      // Verificar se a resposta tem o formato correto da ApiResponse
      if (result.status === 'success' && result.data) {
        const loginData = result.data;
        
        if (loginData.success && loginData.token && loginData.role) {
          console.log("Login bem-sucedido com JWT!");
          
          // Armazenar o token JWT no localStorage
          localStorage.setItem('jwt_token', loginData.token);
          localStorage.setItem('user_role', loginData.role);
          localStorage.setItem('username', loginData.username);
          
          // Redirecionar usando a URL fornecida pelo backend
          if (loginData.redirectUrl) {
            window.location.href = loginData.redirectUrl;
          } else {
            // Fallback para redirecionamento baseado no papel do usuário
            switch (loginData.role) {
              case 'ADMIN':
                window.location.href = '/admin';
                break;
              case 'COZINHEIRO':
                window.location.href = '/cozinheiro';
                break;
              case 'CLIENTE_ATENDENTE':
                window.location.href = '/cliente-atendente';
                break;
              default:
                console.error('Papel de usuário desconhecido:', loginData.role);
                alert('Erro: Papel de usuário não reconhecido.');
            }
          }
        } else {
          alert("Dados de login incompletos na resposta.");
        }
      } else {
        alert(result.message || "Credenciais inválidas.");
      }
    } else {
      const errorData = await response.json().catch(() => ({ error: 'Erro desconhecido' }));
      console.error('Login failed:', errorData);
      alert('Erro no login: ' + (errorData.error || errorData.message || 'Credenciais inválidas'));
    }
  } catch (error) {
    console.error('Erro no login:', error);
    alert("Erro ao realizar login. Tente novamente.");
  }
}

document.getElementById("AccessButton").addEventListener("click", login);