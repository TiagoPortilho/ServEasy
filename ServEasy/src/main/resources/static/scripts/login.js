let user = document.getElementById("username");
let password = document.getElementById("password");

async function login() {
  event.preventDefault();
  
  const loginData = {
    username: user.value,
    password: password.value
  };

  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData)
    });

    const result = await response.json();
    
    if (result.status === 'success' && result.data.success) {
      console.log("Login bem-sucedido!");
      window.location.href = result.data.redirectUrl;
    } else {
      alert(result.data.message || "Credenciais inválidas. Tente novamente.");
    }
  } catch (error) {
    console.error('Erro no login:', error);
    alert("Erro ao realizar login. Tente novamente.");
  }
}

document.getElementById("AccessButton").addEventListener("click", login);