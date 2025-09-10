// ================== AUTH ==================

function toggleForms() {
    document.getElementById("login-form")?.classList.toggle("hidden");
    document.getElementById("register-form")?.classList.toggle("hidden");
}

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

function logout() {
    localStorage.clear();
    window.location.href = "/";
}

// ================== CLIENT ==================

async function createRepairRequest() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Brak tokena – zaloguj się najpierw!");
        return;
    }

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
        // Tworzy zgłoszenie naprawy
        const response = await fetch("http://localhost:8082/api/client/repairs", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            const text = await response.text();
            alert("Błąd przy tworzeniu zgłoszenia: " + response.status + " " + text);
            return;
        }

        const repair = await response.json();
        if (!repair || !repair.id) {
            alert("Nie udało się utworzyć zgłoszenia – brak ID w odpowiedzi");
            return;
        }

        // Upload zdjęć (max 3)
        if (files && files.length > 0) {
            const formData = new FormData();
            for (let i = 0; i < Math.min(files.length, 3); i++) {
                formData.append("images", files[i]);
            }

            const uploadResponse = await fetch(
                `http://localhost:8082/api/client/repairs/${repair.id}/upload-images`,
                {
                    method: "POST",
                    headers: {
                        "Authorization": `Bearer ${token}` // ważne: bez Content-Type
                    },
                    body: formData
                }
            );

            if (!uploadResponse.ok) {
                const text = await uploadResponse.text();
                alert("Błąd podczas wysyłania zdjęć: " + uploadResponse.status + " " + text);
                return;
            }
        }

        alert(` Zgłoszenie zostało wysłane!\nNumer śledzenia: ${repair.trackingId}`);
        window.location.href = "/dashboard";

    } catch (err) {
        console.error("Błąd sieci:", err);
        alert("Błąd sieci. Spróbuj ponownie.");
    }
}


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

            if (data.reportId) html += `<button onclick="downloadReport(${data.reportId})"> Pobierz raport PDF</button>`;
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

// Ładowanie listy zgłoszeń technika
async function loadRepairs() {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
        // Pobranie wszystkich zgłoszeń technika
        const response = await fetch("http://localhost:8082/api/technician/repairs/all", {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) return;

        const repairs = await response.json();

        // Tworzy tabelę
        let html = `<h3>Lista zgłoszeń:</h3>
            <table border="1" cellpadding="5" cellspacing="0">
                <thead><tr><th>ID</th><th>Model</th><th>Status</th><th>Akcje</th></tr></thead>
                <tbody>`;

        repairs.forEach(r => {
            // domyślnie przycisk Edytuj
            let actions = `<button type="button" onclick="editRepair(${r.id})">Edytuj</button>`;

            // jeśli status = DONE - dodatkowe przyciski
            if (r.status === "DONE") {
                actions += `
                    <button type="button" onclick="downloadPdf(${r.id})">Pobierz PDF</button>
                    <button type="button" onclick="sendEmail(${r.id})">Wyślij email</button>
                `;
            }

            html += `<tr>
                        <td>${r.id}</td>
                        <td>${r.deviceModel?.modelName || "brak"}</td>
                        <td>${r.status}</td>
                        <td>${actions}</td>
                    </tr>`;
        });

        html += `</tbody></table>`;
        document.getElementById("repairs-list").innerHTML = html;

    } catch (err) {
        console.error(err);
    }
}

// Otwieranie modala z edycją zgłoszenia
async function editRepair(id) {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
        const response = await fetch(`http://localhost:8082/api/technician/repairs/${id}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) return;

        const repair = await response.json();

        // Tworzymy modal z danymi
        const modal = document.createElement("div");
        modal.className = "modal";
        modal.id = "dynamic-modal";
        modal.innerHTML = `
            <div class="modal-content">
                <span class="close">&times;</span>
                <h3>Szczegóły zgłoszenia</h3>
                <p><b>Model:</b> ${repair.deviceModel?.modelName || "brak"}</p>
                <p><b>Opis klienta:</b> ${repair.description || "brak"}</p>
                <p><b>Klient:</b> ${repair.customer?.fullName || "brak"} (${repair.customer?.email || ""})</p>
                <div>${repair.uploadedFiles?.map(f => `<img src="${f.filePath}" width="100" style="margin:5px;">`).join("") || ""}</div>
                <label for="repair-description">Opis naprawy:</label>
                <textarea id="repair-description">${repair.technicianDescription || ""}</textarea>
                <label for="repair-status">Status:</label>
                <select id="repair-status">
                    <option value="NEW" ${repair.status === "NEW" ? "selected" : ""}>NEW</option>
                    <option value="IN_PROGRESS" ${repair.status === "IN_PROGRESS" ? "selected" : ""}>IN_PROGRESS</option>
                    <option value="DONE" ${repair.status === "DONE" ? "selected" : ""}>DONE</option>
                </select>
                <button id="save-repair-btn"> Zapisz zmiany</button>
            </div>
        `;

        document.body.appendChild(modal);

        // zamykanie modala
        modal.querySelector(".close").addEventListener("click", () => modal.remove());

        // zapis zmian
        modal.querySelector("#save-repair-btn").addEventListener("click", async () => {
            await saveRepairDynamic(repair.id);
            modal.remove();
            loadRepairs();
        });

    } catch (err) {
        console.error(err);
    }
}

// Zapis zmian opisu/statusu technika
async function saveRepairDynamic(id) {
    const token = localStorage.getItem("token");
    const description = document.getElementById("repair-description").value;
    const status = document.getElementById("repair-status").value;

    try {
        const response = await fetch(`http://localhost:8082/api/technician/repairs/${id}`, {
            method: "PUT",
            headers: { "Authorization": `Bearer ${token}`, "Content-Type": "application/json" },
            body: JSON.stringify({ technicianDescription: description, status })
        });
        if (response.ok) alert("Zmiany zapisane!");
        else alert("Błąd zapisu: " + response.status);
    } catch (err) {
        console.error(err);
    }
}

// Pobieranie PDF z backendu
async function downloadPdf(repairId) {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
        const response = await fetch(`http://localhost:8082/api/technician/repairs/${repairId}/report-pdf`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            alert("Błąd przy pobieraniu PDF: " + response.status);
            return;
        }

        // Zamiana odpowiedzi na plik i pobranie
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = url;
        a.download = `repair-report-${repairId}.pdf`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);

    } catch (err) {
        console.error("Błąd sieci przy pobieraniu PDF:", err);
    }
}

// Wysyłka maila z backendu
async function sendEmail(repairId) {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Brak tokenu – zaloguj się ponownie!");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8082/api/technician/repairs/${repairId}/send-email`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            alert("Email wysłany!");
        } else {
            const errorText = await response.text(); // dodatkowo możemy pokazać treść błędu z backendu
            alert("Błąd wysyłki: " + response.status + " " + errorText);
        }

    } catch (err) {
        console.error("Błąd sieci przy wysyłce email:", err);
        alert("Błąd sieci – sprawdź połączenie z serwerem.");
    }
}





// ================== ROLE HANDLING ==================

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) return;

    if (role === "ROLE_CLIENT") {
        document.getElementById("client-view")?.classList.remove("hidden");
    } else if (role === "ROLE_TECHNICIAN") {
        document.getElementById("technician-view")?.classList.remove("hidden");
    }
});

