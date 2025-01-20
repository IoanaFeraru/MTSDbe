const API_BASE_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
    const userData = redirectIfNotLoggedIn();
    if (!userData) return;

    const username = userData.name;
    localStorage.setItem('username', username);

    checkProviderStatus(username);
    loadBookings(username);
    setupLogout();
});

function getUserDataFromCookie() {
    const userCookie = document.cookie
        .split("; ")
        .find((row) => row.startsWith("userData="));

    if (!userCookie) {
        return null;
    }

    return JSON.parse(decodeURIComponent(userCookie.split("=")[1]));
}

function redirectIfNotLoggedIn() {
    const userData = getUserDataFromCookie();
    if (!userData) {
        window.location.href = "../Html/login.html";
    }
    return userData;
}

function loadBookings(username) {
    const bookingsContainer = document.getElementById("bookings-list");

    if (!bookingsContainer) {
        console.error("Elementul bookings-list nu a fost găsit!");
        return;
    }

    console.log("Bookings container found:", bookingsContainer);  // Verifică dacă elementul a fost găsit

    fetch(`http://localhost:8080/bookings/user/${username}`, {
        method: "GET",
        credentials: "include"
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch bookings");
        }
        return response.json();
    })
    .then(bookings => {
        console.log("Bookings:", bookings); // Verifică ce se întoarce API-ul

        bookingsContainer.innerHTML = "";
        if (bookings.length === 0) {
            bookingsContainer.innerHTML = "<p>No bookings found.</p>";
            return;
        }

        bookings.forEach(booking => {
            console.log("Creating list item for booking:", booking);  // Verifică fiecare booking procesat
            const listItem = document.createElement("li");
            listItem.textContent = `Booking ID: ${booking.id}, Service ID: ${booking.serviceId}, Date: ${booking.dueDate}, Time: ${booking.dueTime}`;
            bookingsContainer.appendChild(listItem);
        });
    })
    .catch(error => {
        console.error("Error loading bookings:", error);
        bookingsContainer.innerHTML = "<p>Unable to load bookings.</p>";
    });
}


function checkProviderStatus(username) {
    fetch(`${API_BASE_URL}/users/check-provider?username=${username}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'same-origin'
    })
        .then(response => response.json())
        .then(isProvider => {
            const servicesLink = document.getElementById('services-link');
            if (isProvider) {
                servicesLink.classList.remove("hidden");
            }
        })
        .catch(error => {
            console.error('Error checking provider status:', error);
        });
}

function setupLogout() {
    document.getElementById("logout").addEventListener("click", () => {
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });
}
