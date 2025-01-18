document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');
    const message = document.getElementById('message');

    if (form) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            const formData = {
                username: document.getElementById('username').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
                phoneNumber: document.getElementById('phoneNumber').value,
            };

            try {
                const response = await fetch('http://localhost:8080/auth/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData),
                });

                if (response.ok) {
                    const successMessage = await response.text();
                    if (message) {
                        message.textContent = successMessage;
                        message.className = "success";
                    }

                    localStorage.setItem('username', formData.username);

                    setTimeout(() => {
                        window.location.href = '/Html/home.html';
                    }, 1500);
                } else {
                    const errorMessage = await response.text();
                    if (message) {
                        message.textContent = errorMessage;
                        message.className = "error";
                    }
                }
            } catch (error) {
                if (message) {
                    message.textContent = "Eroare de rețea. Încercați din nou.";
                    message.className = "error";
                }
            }
        });
    } else {
        console.error('Formularul cu ID-ul "registerForm" nu a fost găsit.');
    }
});
console.log("Cookie userData:", document.cookie)