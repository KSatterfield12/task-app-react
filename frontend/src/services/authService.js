const backendUrl = "http://localhost:8080";

export async function login(username, password) {
  const res = await fetch(`${backendUrl}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  });

  if (!res.ok) throw new Error("Login failed");
  return res.json();
}

export function logout() {
  localStorage.removeItem("token");
  sessionStorage.clear();
  document.body.classList.remove("dark-mode");
}