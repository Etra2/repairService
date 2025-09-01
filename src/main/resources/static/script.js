// ================== AUTH ==================

/**
 * Przełącza widok między logowaniem a rejestracją
 */
function toggleForms() {
    document.getElementById("login-form").classList.toggle("hidden");
    document.getElementById("register-form").classList.toggle("hidden");
}

/**
 * Logowanie użytkownika
 */
async function login() {
    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    try {
        const response = await fetch("http://localhost:8082/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        let data;
        try {
            data = await response.json();
        } catch (e) {
            data = { message: response.statusText };
        }

        if (response.ok) {
            localStorage.setItem("token", data.token);
            localStorage.setItem("role", data.user.role);
            localStorage.setItem("userId", data.user.id);
            localStorage.setItem("email", data.user.email);
            window.location.href = "/dashboard";
        } else {
            alert("Błąd logowania: " + (data.message || "Nieznany błąd"));
        }
    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

/**
 * Rejestracja nowego użytkownika
 */
async function register() {
    const fullName = document.getElementById("reg-fullName").value;
    const email = document.getElementById("reg-email").value;
    const password = document.getElementById("reg-password").value;

    try {
        const response = await fetch("http://localhost:8082/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ fullName, email, password }) // role ustawiana w backendzie
        });

        let data;
        try {
            data = await response.json();
        } catch (e) {
            data = { message: response.statusText };
        }

        if (response.ok) {
            alert(data.message || "Rejestracja zakończona, zaloguj się!");
            toggleForms();
        } else {
            alert("Błąd rejestracji: " + (data.message || "Nieznany błąd"));
        }
    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

/**
 * Wylogowanie użytkownika
 */
function logout() {
    localStorage.clear();
    window.location.href = "/";
}

// ================== CLIENT ==================

/**
 * Tworzenie nowego zgłoszenia naprawy
 */
async function createRepairRequest() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Nie jesteś zalogowany!");
        return;
    }

    // Pobranie danych z formularza
    const manufacturer = document.getElementById("manufacturer").value;
    const deviceModelName = document.getElementById("deviceModelName").value;
    const category = document.getElementById("category").value;
    const description = document.getElementById("description").value;
    const files = document.getElementById("images").files;

    // Walidacja pól
    if (!manufacturer || !deviceModelName || !category || !description) {
        alert("Wypełnij wszystkie pola!");
        return;
    }

    const body = { description, deviceModelName, manufacturer, category };

    try {
        // Wysłanie zgłoszenia
        const response = await fetch("http://localhost:8082/api/repairs", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(body)
        });

        let repair;
        try {
            repair = await response.json();
        } catch (e) {
            repair = null;
        }

        if (!response.ok || !repair) {
            alert("Błąd przy tworzeniu zgłoszenia");
            return;
        }

        // Upload zdjęć (max 3)
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

    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

/**
 * Sprawdzenie statusu zgłoszenia przez klienta
 */
async function checkStatus() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Nie jesteś zalogowany!");
        return;
    }

    const id = document.getElementById("repairId").value;

    try {
        const response = await fetch(`http://localhost:8082/api/repairs/${id}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        let data;
        try {
            data = await response.json();
        } catch (e) {
            data = null;
        }

        if (response.ok && data) {
            let html = `<p><b>Opis:</b> ${data.description}</p>
                        <p><b>Status:</b> ${data.status}</p>`;
            if (data.reportId) {
                html += `<button onclick="downloadReport(${data.reportId})">📥 Pobierz raport PDF</button>`;
            }
            document.getElementById("statusResult").innerHTML = html;
        } else {
            alert("Nie znaleziono zgłoszenia.");
        }

    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

/**
 * Pobranie raportu PDF
 */
async function downloadReport(reportId) {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Nie jesteś zalogowany!");
        return;
    }

    try {
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
    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

// ================== TECHNICIAN ==================

/**
 * Pobranie wszystkich zgłoszeń dla technika
 */
async function loadRepairsForTechnician() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Nie jesteś zalogowany!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/repairs", {
            headers: { "Authorization": `Bearer ${token}` }
        });

        let repairs;
        try {
            repairs = await response.json();
        } catch (e) {
            repairs = [];
        }

        if (response.ok) {
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
    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

/**
 * Aktualizacja statusu zgłoszenia przez technika
 */
async function updateRepairStatus(id, status) {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Nie jesteś zalogowany!");
        return;
    }

    try {
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
    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}

// ================== ROLE HANDLING ==================

/**
 * Po załadowaniu DOM ustawiamy widok odpowiedni dla roli użytkownika
 */
document.addEventListener("DOMContentLoaded", () => {
    const role = localStorage.getItem("role");
    if (role === "ROLE_CLIENT") {
        document.getElementById("client-view")?.classList.remove("hidden");
    } else if (role === "ROLE_TECHNICIAN") {
        document.getElementById("technician-view")?.classList.remove("hidden");
        loadRepairsForTechnician();
    }
});
