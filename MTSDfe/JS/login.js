    const form = document.getElementById('loginForm');
    const message = document.getElementById('message');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new URLSearchParams();
        formData.append('username', document.getElementById('username').value);
        formData.append('password', document.getElementById('password').value);

        try {
            console.log("Sending data: ", formData.toString());

            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded', 
                },
                body: formData.toString(),
            });

            if (response.ok) {
                const successMessage = await response.text();
                console.log("Response: ", successMessage);
                message.textContent = successMessage;
                message.className = "success";

                localStorage.setItem('username', document.getElementById('username').value);

                setTimeout(() => {
                    window.location.href = '/Html/home.html';
                }, 1500);
            } else {
                const errorMessage = await response.text();
                console.error("Error Response: ", errorMessage);
                message.textContent = errorMessage;
                message.className = "error";
            }
        } catch (error) {
            console.error("Network Error: ", error);
            message.textContent = "Eroare de rețea. Încercați din nou.";
            message.className = "error";
        }
    });

