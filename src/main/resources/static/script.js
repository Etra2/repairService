// ================== AUTH ==================
function toggleForms() {
    document.getElementById("login-form").classList.toggle("hidden");
    document.getElementById("register-form").classList.toggle("hidden");
}

async function login() {
    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    // Pełny URL backendu
    const response = await fetch("http://localhost:8082/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    if (response.ok) {
        const data = await response.json();

        // zapisujemy token i dane użytkownika w localStorage
        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.user.role);
        localStorage.setItem("userId", data.user.id);
        localStorage.setItem("email", data.user.email);

        // przekierowanie do dashboard przez endpoint kontrolera
        window.location.href = "/dashboard";
    } else {
        const error = await response.json();
        alert("Błąd logowania: " + (error.message || "Nieznany błąd"));
    }
}

async function register() {
    const fullName = document.getElementById("reg-fullName").value;
    const email = document.getElementById("reg-email").value;
    const password = document.getElementById("reg-password").value;

    // Pełny URL backendu
    const response = await fetch("http://localhost:8082/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ fullName, email, password, role: "ROLE_CLIENT" })
    });

    if (response.ok) {
        const data = await response.json();
        alert(data.message || "Rejestracja zakończona, zaloguj się!");
        toggleForms();
    } else {
        const error = await response.json();
        alert("Błąd rejestracji: " + (error.message || "Nieznany błąd"));
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "/"; // przekierowanie do strony startowej
}

// ================== CLIENT ==================
async function createRepairRequest() {
    const token = localStorage.getItem("token");
    const device = document.getElementById("device").value;
    const description = document.getElementById("description").value;
    const files = document.getElementById("images").files;

    const response = await fetch("http://localhost:8082/api/repairs", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ deviceModel: device, description })
    });

    if (!response.ok) {
        alert("Błąd przy tworzeniu zgłoszenia");
        return;
    }

    const repair = await response.json();

    if (files.length > 0) {
        const formData = new FormData();
        for (let i = 0; i < files.length && i < 3; i++) {
            formData.append("images", files[i]);
        }

        await fetch(`http://localhost:8082/api/repairs/${repair.id}/upload-images`, {
            method: "POST",
            headers: { "Authorization": `Bearer ${token}` },
            body: formData
        });
    }

    alert("Zgłoszenie zostało wysłane!");
    window.location.href = "/dashboard";
}

async function checkStatus() {
    const id = document.getElementById("repairId").value;
    const token = localStorage.getItem("token");

    const response = await fetch(`http://localhost:8082/api/repairs/${id}`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    if (response.ok) {
        const data = await response.json();
        let html = `<p><b>Opis:</b> ${data.description}</p>
                    <p><b>Status:</b> ${data.status}</p>`;

        if (data.reportId) {
            html += `<button onclick="downloadReport(${data.reportId})">📥 Pobierz raport PDF</button>`;
        }

        document.getElementById("statusResult").innerHTML = html;
    } else {
        alert("Nie znaleziono zgłoszenia.");
    }
}

async function downloadReport(reportId) {
    const token = localStorage.getItem("token");

    const response = await fetch(`http://localhost:8082/api/reports/${reportId}/pdf`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `raport-${reportId}.pdf`;
        document.body.appendChild(a);
        a.click();
        a.remove();
    } else {
        alert("Nie udało się pobrać raportu.");
    }
}

// ================== TECHNICIAN ==================
async function loadRepairsForTechnician() {
    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8082/api/repairs", {
        headers: { "Authorization": `Bearer ${token}` }
    });

    if (response.ok) {
        const repairs = await response.json();
        const list = document.getElementById("repairs-list");
        list.innerHTML = "";

        repairs.forEach(r => {
            const item = document.createElement("div");
            item.className = "repair-item";
            item.innerHTML = `
                <p><b>ID:</b> ${r.id}</p>
                <p><b>Opis:</b> ${r.description}</p>
                <p><b>Status:</b> ${r.status}</p>
                <button onclick="updateRepairStatus(${r.id}, 'IN_PROGRESS')">🔧 W trakcie</button>
                <button onclick="updateRepairStatus(${r.id}, 'COMPLETED')">✅ Zakończ</button>
                <hr/>
            `;
            list.appendChild(item);
        });
    } else {
        alert("Błąd przy pobieraniu zgłoszeń.");
    }
}

async function updateRepairStatus(id, status) {
    const token = localStorage.getItem("token");

    const response = await fetch(`http://localhost:8082/api/repairs/${id}/status`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ status })
    });

    if (response.ok) {
        alert("Status został zaktualizowany");
        loadRepairsForTechnician();
    } else {
        alert("Błąd przy aktualizacji statusu");
    }
}

// ================== ROLE HANDLING ==================
document.addEventListener("DOMContentLoaded", () => {
    const role = localStorage.getItem("role");
    if (role === "ROLE_CLIENT") {
        document.getElementById("client-view")?.classList.remove("hidden");
    } else if (role === "ROLE_TECHNICIAN") {
        document.getElementById("technician-view")?.classList.remove("hidden");
        loadRepairsForTechnician();
    }
});
