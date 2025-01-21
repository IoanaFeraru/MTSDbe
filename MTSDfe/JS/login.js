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
            credentials: 'include',
        });

        if (response.ok) {
            const contentType = response.headers.get("Content-Type");
            let userData;

            if (contentType && contentType.includes("application/json")) {
                userData = await response.json();
            } else {
                userData = { name: document.getElementById('username').value };
                console.log("Server returned plain text. Using username as fallback.");
            }

            console.log("User Data: ", userData);

            message.textContent = "Login successful! Redirecting...";
            message.className = "success";

            document.cookie = `userData=${encodeURIComponent(JSON.stringify(userData))}; path=/`;

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
