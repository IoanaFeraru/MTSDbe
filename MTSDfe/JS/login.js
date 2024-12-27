const form = document.getElementById('loginForm');
const message = document.getElementById('message');

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
    };

    try {
        // Fetch to the login endpoint
        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        });

        if (response.ok) {
            const successMessage = await response.text();
            message.textContent = successMessage;
            message.className = "success";

            // Save the username locally
            localStorage.setItem('username', formData.username);

            // Redirect to personalized home page
            setTimeout(() => {
                window.location.href = '/home.html';
            }, 1500);
        } else {
            const errorMessage = await response.text();
            message.textContent = errorMessage;
            message.className = "error";
        }
    } catch (error) {
        message.textContent = "Eroare de rețea. Încercați din nou.";
        message.className = "error";
    }
});
