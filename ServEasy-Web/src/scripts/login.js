let user = document.getElementById("username");
let password = document.getElementById("password");

function login() {
    event.preventDefault();
  if (user.value === "admin" && password.value === "admin") {
    window.location.href = "dashboard.html";
    console.log("Login bem-sucedido!");
  } else {
    alert("Credenciais inválidas. Tente novamente.");
  }
}

document.getElementById("AccessButton").addEventListener("click", login);