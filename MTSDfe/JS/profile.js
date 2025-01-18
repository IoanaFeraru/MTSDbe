document.addEventListener("DOMContentLoaded", () => {
    // Obține datele din cookie
    const username = localStorage.getItem('username');
    // Elementele formularului
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const becomeProviderButton = document.getElementById("becomeProviderButton");
    const saveProviderButton = document.getElementById("saveProviderButton");
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

    // Funcție pentru popularea formularului cu datele utilizatorului
    const populateForm = (user) => {
        document.getElementById("user-name").textContent = username || "Guest";
        usernameInput.value = user.username;
        emailInput.value = user.email;
        phoneInput.value = user.phoneNumber;
        addressInput.value = user.address;
    };

    // Obține datele utilizatorului la încărcarea paginii
    const fetchUserData = async () => {
        try {
            // Cerere GET pentru a obține datele utilizatorului pe baza username-ului
            const userResponse = await fetch(userApiUrl, {
                method: "GET", 
                headers: {
                    "Content-Type": "application/json", 
                },
            });

            if (!userResponse.ok) throw new Error("Failed to fetch user details.");
            const userData = await userResponse.json();
            userId = userData.id; 
            // Populare formular cu datele utilizatorului
            populateForm(userData);
        } catch (error) {
            console.error("Error fetching user data:", error);
            alert("Failed to load user data. Please try again later.");
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
        inputs.forEach((input) => (input.disabled = false));
        editButton.style.display = "none";
        saveButton.style.display = "block";
        becomeProviderButton.style.display = "none";
        passwordChangeContainer.style.display = "block"; // Show password change container again
        changePasswordButton.style.display = "block"; // Show "Change Password" button again
    });

    // Eveniment: Salvare modificări
    saveButton.addEventListener("click", async (e) => {
        e.preventDefault(); // Previne trimiterea formularului
        await saveUserData();
        inputs.forEach((input) => (input.disabled = true));
        editButton.style.display = "block";
        saveButton.style.display = "none";
        becomeProviderButton.style.display = "block";
        passwordChangeContainer.style.display = "none"; // Hide the password change section
        changePasswordButton.style.display = "none"; // Hide the "Change Password" button
    });

    becomeProviderButton.addEventListener("click", () => {
        providerFields.style.display = "block";
        becomeProviderButton.style.display = "none";
        saveProviderButton.style.display = "block";
    
        // Ascundem butonul Edit
        editButton.style.display = "none";
    
        // Activăm câmpurile pentru provider
        providerInputs.forEach((input) => {
            input.disabled = false; 
        });
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
                method: "PATCH",
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

    fetchUserData();
});
