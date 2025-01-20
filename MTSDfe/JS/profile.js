document.addEventListener("DOMContentLoaded", () => {
    // Obține datele din cookie
    const username = localStorage.getItem('username');
    const usertype = localStorage.getItem('usertype');
    // Elementele formularului
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const cancelButton = document.createElement("button"); 
    const becomeProviderButton = document.getElementById("becomeProviderButton");
    const saveProviderButton = document.getElementById("saveProviderButton");
    const cancelProviderButton = document.getElementById("cancelProviderButton");
    const editProviderButton = document.createElement("button"); // 
    const saveNewProviderButton = document.createElement("button"); // 
    const cancelEditProviderButton = document.createElement("button"); //
    const inputs = document.querySelectorAll("#profileForm input");
    const usernameInput = document.getElementById("username");
    const emailInput = document.getElementById("email");
    const phoneInput = document.getElementById("phone");
    const addressInput = document.getElementById("address");
    const providerFields = document.getElementById("providerFields");
    const providerInputs = providerFields.querySelectorAll("input");
    let userId;
    const newPasswordInput = document.getElementById("newPassword");
    const changePasswordButton = document.getElementById("changePasswordButton");
    const passwordChangeContainer = document.getElementById("passwordChangeContainer");

    // URL-ul API pentru utilizatorul autentic
    const userApiUrl = `http://localhost:8080/users/${username}`;
    const providerApiUrl = `http://localhost:8080/users/providers/${username}`;

    cancelButton.textContent = "Cancel";
    cancelButton.style.marginLeft = "10px";
    cancelButton.style.marginTop = "10px";
    cancelButton.style.backgroundColor = "#ff4d4d"; 
    cancelButton.style.color = "#ffffff"; 
    cancelButton.style.border = "1px solid #ff9999"; 
    cancelButton.style.borderRadius = "5px"; 
    cancelButton.style.padding = "8px 15px";
    cancelButton.style.cursor = "pointer";

    
    editProviderButton.textContent = "Edit Provider Details";
    editProviderButton.style.marginTop = "10px";
    editProviderButton.style.padding = "8px 15px";
    editProviderButton.style.cursor = "pointer";
    editProviderButton.style.backgroundColor = '#004085';
    editProviderButton.style.color = '#fff';
    editProviderButton.style.alignSelf = 'center';
    providerFields.insertAdjacentElement("afterend", editProviderButton);

    // Creează un container pentru butoanele "Save New Provider Data" și "Cancel"
const providerButtonsContainer = document.createElement("div");
providerButtonsContainer.style.display = "flex";
providerButtonsContainer.style.justifyContent = "flex-start"; // Aliniază butoanele la stânga
providerButtonsContainer.style.gap = "10px"; // Adaugă un spațiu între butoane
providerButtonsContainer.style.marginTop = "10px"; // Adaugă o marjă de sus pentru mai mult spațiu

// Adaugă butoanele în container
providerButtonsContainer.appendChild(saveNewProviderButton);
providerButtonsContainer.appendChild(cancelEditProviderButton);

  // Stilizare pentru "Save New Provider Data"
saveNewProviderButton.textContent = "Save New Provider Data";
saveNewProviderButton.style.marginLeft = "10px";
saveNewProviderButton.style.padding = "10px 20px";
saveNewProviderButton.style.backgroundColor = "#1db93f";
saveNewProviderButton.style.color = "#ffffff";
saveNewProviderButton.style.border = "none";
saveNewProviderButton.style.borderRadius = "5px";
saveNewProviderButton.style.cursor = "pointer";
saveNewProviderButton.style.display = "none";
saveNewProviderButton.style.transition = "background-color 0.3s ease";
saveNewProviderButton.style.marginLeft = '0';
saveNewProviderButton.style.width = "250px";

// Stilizare pentru hover "Save New Provider Data"
saveNewProviderButton.addEventListener("mouseover", () => {
    saveNewProviderButton.style.backgroundColor = "#17a237";
});
saveNewProviderButton.addEventListener("mouseout", () => {
    saveNewProviderButton.style.backgroundColor = "#1db93f";
});
  
    // Stilizare pentru "Cancel"
cancelEditProviderButton.textContent = "Cancel";
cancelEditProviderButton.style.marginLeft = "10px";
cancelEditProviderButton.style.padding = "10px 20px";
cancelEditProviderButton.style.backgroundColor = "#ff4d4d";
cancelEditProviderButton.style.color = "#ffffff";
cancelEditProviderButton.style.border = "none";
cancelEditProviderButton.style.borderRadius = "5px";
cancelEditProviderButton.style.cursor = "pointer";
cancelEditProviderButton.style.display = "none";
cancelEditProviderButton.style.transition = "background-color 0.3s ease";
cancelEditProviderButton.style.marginLeft = '0';

// Stilizare pentru hover "Cancel"
cancelEditProviderButton.addEventListener("mouseover", () => {
    cancelEditProviderButton.style.backgroundColor = "#e64545";
});
cancelEditProviderButton.addEventListener("mouseout", () => {
    cancelEditProviderButton.style.backgroundColor = "#ff4d4d";

});


    providerFields.insertAdjacentElement("afterend", editProviderButton);
    editProviderButton.insertAdjacentElement("afterend", providerButtonsContainer);

    // Ascunderea inițială a câmpurilor provider
    providerInputs.forEach((input) => (input.disabled = true));

    // Funcție pentru popularea formularului cu datele utilizatorului
    const populateUserFields = (user) => {
        document.getElementById("user-name").textContent = username || "Guest";
        usernameInput.value = user.username;
        emailInput.value = user.email;
        phoneInput.value = user.phoneNumber;
        addressInput.value = user.address;
    };

    const populateProviderFields = (provider) => {
        document.getElementById("cif").value = provider.cif;
        document.getElementById("companyName").value = provider.companyName;
        document.getElementById("companyAddress").value = provider.companyAdress;
        document.getElementById("serviceDomain").value = provider.serviceDomain;
        document.getElementById("bankIban").value = provider.bankIban;
    };

    // Obține datele utilizatorului și ale providerului
    const fetchUserData = async () => {
        try {
            // Cerere GET pentru a obține datele utilizatorului
            const userResponse = await fetch(userApiUrl, { method: "GET", headers: { "Content-Type": "application/json" } });
            if (!userResponse.ok) throw new Error("Failed to fetch user details.");
            const userData = await userResponse.json();
            userId = userData.id;
            populateUserFields(userData);

            // Dacă utilizatorul este provider, obține și datele providerului
            if (usertype === "provider") {
                // Cerere GET pentru datele providerului
                const providerResponse = await fetch(providerApiUrl, { method: "GET", headers: { "Content-Type": "application/json" } });
                if (!providerResponse.ok) throw new Error("Failed to fetch provider details.");
                const providerData = await providerResponse.json();
                populateProviderFields(providerData);
    
                providerFields.style.display = "block"; // Afișează câmpurile provider
                becomeProviderButton.style.display = "none"; // Ascunde butonul "Become a Provider"
                saveProviderButton.style.display = "none"; // Ascunde butonul "Save Provider Details"
                cancelProviderButton.style.display = "none"; // Ascunde butonul "Cancel Provider Details"
            } else {
                // Dacă utilizatorul este client, afișăm opțiunea "Become a Provider"
                providerFields.style.display = "none";
                becomeProviderButton.style.display = "block";
                saveProviderButton.style.display = "none";
                cancelProviderButton.style.display = "none";
            }
        } catch (error) {
            console.error("Error fetching user/provider data:", error);
            alert("Failed to load user/provider data. Please try again later.");
        }
    };

    // Salvează datele modificate ale utilizatorului
    const saveUserData = async () => {
        try {
            const updatedUser = {
                username: usernameInput.value,
                email: emailInput.value,
                phoneNumber: phoneInput.value,
                address: addressInput.value,
            };

            const response = await fetch(userApiUrl, {
                method: "PUT", 
                headers: {
                    "Content-Type": "application/json", 
                },
                body: JSON.stringify(updatedUser),
            });

            if (!response.ok) throw new Error("Failed to save user data.");
            alert("User data updated successfully.");
        } catch (error) {
            console.error("Error saving user data:", error);
            alert("Failed to save user data. Please try again later.");
        }
    };

   // Eveniment: Activare mod editare
   editButton.addEventListener("click", () => {
    usernameInput.disabled = false;
    emailInput.disabled = false;
    phoneInput.disabled = false;
    addressInput.disabled = false;
    editButton.style.display = "none";
    saveButton.style.display = "block";
    cancelButton.style.display = "inline-block";
    becomeProviderButton.style.display = "none";
    passwordChangeContainer.style.display = "block";
    changePasswordButton.style.display = "block";
    saveButton.insertAdjacentElement("afterend", cancelButton);
});

// Eveniment: Anulare modificări utilizator
cancelButton.addEventListener("click", () => {
    fetchUserData(); // Resetare date
    inputs.forEach((input) => (input.disabled = true));
    editButton.style.display = "block";
    saveButton.style.display = "none";
    cancelButton.style.display = "none";
    passwordChangeContainer.style.display = "none";
    changePasswordButton.style.display = "none";
});

// Eveniment: Salvare modificări utilizator
saveButton.addEventListener("click", async (e) => {
    e.preventDefault();
    await saveUserData();
    inputs.forEach((input) => (input.disabled = true));
    editButton.style.display = "block";
    saveButton.style.display = "none";
    cancelButton.style.display = "none";
    passwordChangeContainer.style.display = "none";
    changePasswordButton.style.display = "none";
});

// Eveniment: Devino provider
becomeProviderButton.addEventListener("click", () => {
    providerFields.style.display = "block";
    providerInputs.forEach((input) => (input.disabled = false));
    becomeProviderButton.style.display = "none";
    saveProviderButton.style.display = "inline-block";
    cancelProviderButton.style.display = "inline-block";
});

// Eveniment: Anulare modificări provider
cancelProviderButton.addEventListener("click", () => {
    providerFields.style.display = "none";
    providerInputs.forEach((input) => (input.value = ""));
    becomeProviderButton.style.display = "block";
    saveProviderButton.style.display = "none";
    cancelProviderButton.style.display = "none";
});

    saveProviderButton.addEventListener("click", async () => {
        const providerData = {
            userId: userId, 
            cif: document.getElementById("cif").value,
            companyName: document.getElementById("companyName").value,
            companyAdress: document.getElementById("companyAddress").value,
            serviceDomain: document.getElementById("serviceDomain").value,
            bankIban: document.getElementById("bankIban").value,
        };
        try {
            // Realizare fetch
            const response = await fetch("http://localhost:8080/users/addProvider", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(providerData),
            });
    
            if (!response.ok) {
                throw new Error("Failed to save provider details.");
            }
    
            const responseData = await response.json();
            alert("Provider added successfully: " + responseData.message);
    
            // Resetare câmpuri și afișare mesaje de succes
            providerFields.style.display = "none";
            saveProviderButton.style.display = "none";
            becomeProviderButton.style.display = "none"; // Ascundem butonul deoarece utilizatorul este deja provider
        } catch (error) {
            console.error("Error saving provider details:", error);
            alert("Failed to add provider. Please try again.");
        }
    });

    changePasswordButton.addEventListener("click", async () => {
        const newPassword = newPasswordInput.value;

        if (!newPassword) {
            alert("Please enter a new password.");
            return;
        }

        try {
            const response = await fetch(`${userApiUrl}/password`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ password: newPassword }),
            });

            if (!response.ok) {
                throw new Error("Failed to change password.");
            }

            alert("Password updated successfully.");
            newPasswordInput.value = ""; // Clear the password input
            passwordChangeContainer.style.display = "none"; // Hide password change fields
            changePasswordButton.style.display = "none"; // Hide "Change Password" button
        } catch (error) {
            console.error("Error changing password:", error);
            alert("Failed to change password. Please try again.");
        }
    });

    editProviderButton.addEventListener("click", (e) => {
        e.preventDefault(); // Previne reîncărcarea paginii
    
        // Activează câmpurile pentru editare
        providerInputs.forEach((input) => (input.disabled = false));
    
        // Ascunde butonul de editare și afișează butoanele de salvare/anulare
        editProviderButton.style.display = "none";
        saveNewProviderButton.style.display = "inline-block";
        cancelEditProviderButton.style.display = "inline-block";
    });

// Eveniment: Salvare modificări provider existent
saveNewProviderButton.addEventListener("click", async () => {
    const providerData = {
        cif: document.getElementById("cif").value,
        companyName: document.getElementById("companyName").value,
        companyAdress: document.getElementById("companyAddress").value,
        serviceDomain: document.getElementById("serviceDomain").value,
        bankIban: document.getElementById("bankIban").value,
    };

    try {
        const response = await fetch(`http://localhost:8080/users/providers/${username}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(providerData),
        });

        if (!response.ok) throw new Error("Failed to update provider details.");
        
        
        console.log("Provider details updated successfully:", providerData);

        alert("Provider details updated successfully.");
    } catch (error) {
        console.error("Error updating provider details:", error);
        alert("Failed to update provider details.");
    } finally {
        providerInputs.forEach((input) => (input.disabled = true));
        editProviderButton.style.display = "block";
        saveNewProviderButton.style.display = "none";
        cancelEditProviderButton.style.display = "none";
    }
});

// Eveniment: Anulare modificări provider existent
cancelEditProviderButton.addEventListener("click", () => {
    fetchUserData(); // Reîncarcă datele utilizatorului din API
    providerInputs.forEach((input) => (input.disabled = true));

    // Resetare stare vizibilitate butoane
    editProviderButton.style.display = "block";
    saveNewProviderButton.style.display = "none";
    cancelEditProviderButton.style.display = "none";
});

    

    fetchUserData();


    document.getElementById("logout").addEventListener("click", () => {
        // Clear the user cookie and redirect
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });
});
