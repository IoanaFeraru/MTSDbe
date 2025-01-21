function getUserDataFromCookie() {
    const userCookie = document.cookie
        .split("; ")
        .find((row) => row.startsWith("userData="));

    if (!userCookie) {
        return null; 
    }

    return JSON.parse(decodeURIComponent(userCookie.split("=")[1]));
}

let currentService = null;

async function fetchActiveServices() {
    try {
        const response = await fetch("http://localhost:8080/services/active", {
            method: "GET", 
            credentials: "include"
        });
        const services = await response.json();

        if (response.ok) {
            updateGridWithResults(services);
        } else {
            console.error("Error fetching active services:", services);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

function openModal(serviceId) {
    fetch(`http://localhost:8080/services/id/${serviceId}`)
        .then(response => response.json())
        .then(service => {
            document.getElementById("modal-service-name").textContent = service.name;
            document.getElementById("modal-service-description").textContent = service.description;
            document.getElementById("modal-service-domain").textContent = service.domain;
            document.getElementById("modal-service-subdomain").textContent = service.subdomain;
            document.getElementById("modal-service-price").textContent = service.price;
            document.getElementById("modal-service-region").textContent = service.region;
            document.getElementById("modal-service-type").textContent = service.serviceType;
            document.getElementById("modal-service-min-booking-time").textContent = service.minimumBookingTime;
            document.getElementById("modal-service-username").textContent = service.username;

            document.getElementById("service-modal").style.display = "block";
            currentService = service;
        })
        .catch(error => console.error("Error fetching service details:", error));
}

document.getElementById("modal-booking-btn").addEventListener("click", () => {
    if (currentService) {
        document.getElementById("booking-price").value = currentService.price;
        document.getElementById("booking-modal").style.display = "block";
    }
});

document.getElementById("booking-close-btn").addEventListener("click", () => {
    document.getElementById("booking-modal").style.display = "none"; 
});

document.getElementById("modal-close-btn").addEventListener("click", () => {
    document.getElementById("service-modal").style.display = "none";
});

function closeModal() {
    document.getElementById('service-modal').style.display = 'none';
}

function updateGridWithResults(services) {
    const servicesGrid = document.querySelector('.services-grid');
    servicesGrid.innerHTML = ''; 

    services.forEach(service => {
        const truncatedDescription = service.description.length > 100 ? service.description.substring(0, 100) + '...' : service.description;

        const serviceCard = document.createElement("div");
        serviceCard.classList.add("service-card");
        serviceCard.innerHTML = `
            <div class="service-image-placeholder">
            </div>
            <h3>${service.name}</h3>
            <p><strong>Description:</strong> ${truncatedDescription}</p>
            <p><strong>Domain:</strong> ${service.domain}</p>
            <p><strong>Subdomain:</strong> ${service.subdomain}</p>
            <p><strong>Price:</strong> ${service.price} RON</p>
            <p><strong>Region:</strong> ${service.region}</p>
        `;
        
        serviceCard.addEventListener('click', () => openModal(service.id));
        
        servicesGrid.appendChild(serviceCard);
    });
}

document.getElementById("booking-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const dueDate = document.getElementById("booking-due-date").value;
    const dueTime = document.getElementById("booking-due-time").value;
    const deliveryAddress = document.getElementById("booking-delivery-address").value;
    const price = document.getElementById("booking-price").value;
    
    const userData = getUserDataFromCookie();
    if (!userData) {
        alert("You need to be logged in to book a service.");
        return;
    }

    const bookingData = {
        username: userData.name,
        serviceId: currentService.id,
        dueDate: dueDate,
        dueTime: dueTime,
        deliveryAddress: deliveryAddress,
        price: price,
        bookingState: "PENDING_PAYMENT"
    };

    console.log("user data: ", userData);

    console.log("Booking data to be sent:", bookingData);

    try {
        const response = await fetch("http://localhost:8080/bookings", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bookingData)
        });

        if (response.ok) {
            alert("Booking successfully created!");
            document.getElementById("booking-modal").style.display = "none";
        } else {
            const errorMessage = await response.text();
            alert("Error: " + errorMessage);
        }
    } catch (error) {
        console.error("Error creating booking:", error);
        alert("An error occurred while creating the booking.");
    }
});

document.getElementById('modal-close-btn').addEventListener('click', closeModal);

window.addEventListener('DOMContentLoaded', function () {
    const userData = getUserDataFromCookie();

    if (!userData) {
        console.error('User data not found.');
        return;
    }

    const username = userData.name;

    fetch(`http://localhost:8080/users/check-provider?username=${username}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'same-origin'
    })
    .then(response => response.json())
    .then(isProvider => {
        if (isProvider) {
            document.getElementById('services-link').style.display = 'block';
            this.document.getElementById('bookings-link').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error checking provider status:', error);
    });
});

document.addEventListener("DOMContentLoaded", async () => {
    fetchActiveServices();
    
    document.getElementById("logout").addEventListener("click", () => {
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });

    const userCookie = document.cookie
        .split("; ")
        .find((row) => row.startsWith("userData="));
    
    if (!userCookie) {
        window.location.href = "../Html/login.html";
        return;
    }

    const userData = JSON.parse(decodeURIComponent(userCookie.split("=")[1]));

    document.getElementById("user-name").textContent = userData.name || "Guest";
    localStorage.setItem('username', userData.name);
    console.log(localStorage.getItem('username'));

    let usertype = "client";

    try {
        const userResponse = await fetch(`http://localhost:8080/users/providers/${userData.name}`, {
            method: "GET",
            headers: { "Content-Type": "application/json" },
        });

        if (userResponse.ok) {
            const isProvider = await userResponse.json();
            usertype = isProvider ? "provider" : "client";
            localStorage.setItem('usertype', usertype);
            console.log("User type:", usertype);
        } else {
            console.error("Error fetching user type:", await userResponse.text());
        }
    } catch (error) {
        console.error("Error fetching user data:", error);
        alert("Failed to load user data. Please try again later.");
        return;
    }

    if (usertype === "client") {
        setupClientView();
    } else if (usertype === "provider") {
        setupProviderView();
    }

    document.getElementById("logout").addEventListener("click", () => {
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });

    const searchInput = document.getElementById("search-input");
    searchInput.addEventListener("input", async () => {
        const query = searchInput.value.trim();
        if (query.length > 0) {
            try {
                const response = await fetch(`/services/search?query=${encodeURIComponent(query)}`);
                const data = await response.json();

                if (response.ok) {
                    updateGridWithResults(data);
                } else {
                    console.error("Error fetching services:", data);
                }
            } catch (error) {
                console.error("Error:", error);
            }
        }
    });
});

function setupClientView() {
    console.log("Setting up client view.");
    document.getElementById("client-section").style.display = "block";
    document.getElementById("provider-section").style.display = "none";
}

function setupProviderView() {
    console.log("Setting up provider view.");
    document.getElementById("provider-section").style.display = "block";
    document.getElementById("client-section").style.display = "none";
}



/*

document.addEventListener("DOMContentLoaded", () => {
    const userData = getUserDataFromCookie();
    
    if (!userData) {
        window.location.href = "../Html/login.html";
        return;
    }

    document.getElementById("user-name").textContent = userData.name || "Guest";
    localStorage.setItem('username', userData.name);
});
*/