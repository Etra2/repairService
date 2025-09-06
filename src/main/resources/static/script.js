// ================== AUTH ==================

/**
 * Przełącza widok między logowaniem a rejestracją
 */
function toggleForms() {
    document.getElementById("login-form")?.classList.toggle("hidden");
    document.getElementById("register-form")?.classList.toggle("hidden");
}

/**
 * Logowanie użytkownika
 */
async function login() {
    const email = document.getElementById("login-email")?.value;
    const password = document.getElementById("login-password")?.value;

    if (!email || !password) {
        alert("Wypełnij wszystkie pola logowania!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json().catch(() => ({ message: response.statusText }));

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
    const fullName = document.getElementById("reg-fullName")?.value;
    const email = document.getElementById("reg-email")?.value;
    const password = document.getElementById("reg-password")?.value;

    if (!fullName || !email || !password) {
        alert("Wypełnij wszystkie pola rejestracji!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8082/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ fullName, email, password })
        });

        const data = await response.json().catch(() => ({ message: response.statusText }));

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
 * Tworzenie nowego zgłoszenia naprawy przez klienta
 */
async function createRepairRequest() {
    const token = localStorage.getItem("token");
    if (!token) return;

    const manufacturer = document.getElementById("manufacturer")?.value;
    const deviceModelName = document.getElementById("deviceModelName")?.value;
    const category = document.getElementById("category")?.value;
    const description = document.getElementById("description")?.value;
    const files = document.getElementById("images")?.files;

    if (!manufacturer || !deviceModelName || !category || !description) {
        alert("Wypełnij wszystkie pola!");
        return;
    }

    const body = { description, deviceModelName, manufacturer, category };

    try {
        const response = await fetch("http://localhost:8082/api/client/repairs", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(body)
        });

        const repair = await response.json().catch(() => null);

        if (!response.ok || !repair) {
            alert("Błąd przy tworzeniu zgłoszenia");
            return;
        }

        if (files && files.length > 0) {
            const formData = new FormData();
            for (let i = 0; i < Math.min(files.length, 3); i++) {
                formData.append("images", files[i]);
            }

            const uploadResponse = await fetch(`http://localhost:8082/api/client/repairs/${repair.id}/upload-images`, {
                method: "POST",
                headers: { "Authorization": `Bearer ${token}` },
                body: formData
            });

            if (!uploadResponse.ok) alert("Błąd podczas wysyłania zdjęć");
        }

        alert(`Zgłoszenie zostało wysłane! Numer śledzenia: ${repair.trackingId}`);
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
    if (!token) return;

    const trackingId = document.getElementById("trackingId")?.value;
    if (!trackingId) return;

    try {
        const response = await fetch(`http://localhost:8082/api/client/repairs/status/${trackingId}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        const data = await response.json().catch(() => null);

        if (response.ok && data) {
            let html = `<p><b>Opis:</b> ${data.description}</p>
                        <p><b>Status:</b> ${data.status}</p>`;

            if (data.reportId) html += `<button onclick="downloadReport(${data.reportId})">📥 Pobierz raport PDF</button>`;
            if (data.uploadedFiles?.length > 0) {
                html += `<p><b>Zdjęcia:</b></p>`;
                data.uploadedFiles.forEach(file => {
                    html += `<img src="${file.filePath}" alt="Zdjęcie" style="max-width:200px; margin:5px;">`;
                });
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

// ================== TECHNICIAN ==================

/**
 * Pobranie listy zgłoszeń dla technika
 */
async function loadRepairsForTechnician() {
    const token = localStorage.getItem("token");
    if (!token) return; // jeśli nie ma tokena, nic nie robimy

    console.log("Kliknięto przycisk załaduj listę zgłoszeń. Token:", token);

    try {
        const response = await fetch("http://localhost:8082/api/technician/repairs", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        console.log("Otrzymano odpowiedź z serwera, status:", response.status);

        if (!response.ok) {
            console.error("Błąd HTTP:", response.status, await response.text());
            return;
        }

        const repairs = await response.json();
        console.log("Lista zgłoszeń pobrana z backendu:", repairs);

        let html = "<h3>Lista zgłoszeń:</h3><ul>";
        repairs.forEach(r => {
            html += `<li><b>${r.deviceModelName}</b> – ${r.status} (ID: ${r.id})</li>`;
        });
        html += "</ul>";

        document.getElementById("repairs-list").innerHTML = html;
        console.log("Lista wyświetlona w DOM.");

    } catch (err) {
        console.error("Błąd fetch:", err);
    }
}

// ================== ROLE HANDLING ==================

/**
 * Pokazuje odpowiedni panel po stronie klienta/technika
 */
document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    // Jeśli nie ma tokena, nic nie robimy
    if (!token) return;

    if (role === "ROLE_CLIENT") {
        document.getElementById("client-view")?.classList.remove("hidden");
    } else if (role === "ROLE_TECHNICIAN") {
        document.getElementById("technician-view")?.classList.remove("hidden");
        // Wywołujemy listę tylko jeśli jesteśmy na dashboardzie
        if (window.location.pathname === "/dashboard") {
            loadRepairsForTechnician();
        }
    }
});


// debug do wywalenia potem
// script.js

/**
 * Debugowy fetch listy zgłoszeń dla technika
 */
async function debugLoadRepairs() {
    const token = localStorage.getItem("token");
    if (!token) {
        console.error("Brak tokena – zaloguj się ponownie!");
        return;
    }
    console.log("Kliknięto przycisk, token pobrany z localStorage:", token);

    try {
        const response = await fetch("http://localhost:8082/api/debug/technician/repairs/all", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        console.log("Response status:", response.status);
        if (!response.ok) {
            console.error("Nie udało się pobrać listy zgłoszeń. Status:", response.status);
            return;
        }

        const repairs = await response.json();
        console.log("Dane pobrane z backendu:", repairs);

        let html = "<h3>Lista zgłoszeń (DEBUG):</h3><ul>";
        repairs.forEach(r => {
            html += `<li>ID=${r.id} | Model=${r.deviceModel?.modelName || "brak"} | Status=${r.status}</li>`;
        });
        html += "</ul>";
        document.getElementById("repairs-list").innerHTML = html;
        console.log("Lista wyświetlona w DOM");
    } catch (err) {
        console.error("Błąd fetch:", err);
    }
}



// ładowanie listy dla techika
async function loadRepairs() {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
        const response = await fetch("http://localhost:8082/api/debug/technician/repairs/all", {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) return;

        const repairs = await response.json();

        // Tworzymy tabelkę
        let html = `
            <h3>Lista zgłoszeń:</h3>
            <table border="1" cellpadding="5" cellspacing="0">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Model</th>
                        <th>Status</th>
                        <th>Akcje</th>
                    </tr>
                </thead>
                <tbody>
        `;

        repairs.forEach(r => {
            html += `
                <tr>
                    <td>${r.id}</td>
                    <td>${r.deviceModel?.modelName || "brak"}</td>
                    <td>${r.status}</td>
                    <td>
                        <button onclick="editRepair(${r.id})">Edytuj</button>
                    </td>
                </tr>
            `;
        });

        html += `</tbody></table>`;
        document.getElementById("repairs-list").innerHTML = html;

    } catch (err) {
        console.error("Błąd fetch:", err);
    }
}

// Funkcja wywoływana po kliknięciu Edytuj
function editRepair(id) {
    // Tutaj możesz np. pokazać modal z danymi zgłoszenia,
    // pobrać pełne info z backendu (opis, zdjęcia, dane klienta)
    console.log("Edytujesz zgłoszenie ID:", id);
}
