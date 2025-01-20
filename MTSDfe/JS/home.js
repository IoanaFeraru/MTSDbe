document.addEventListener("DOMContentLoaded", async () => {
    // Get cookie data
    const userCookie = document.cookie
        .split("; ")
        .find((row) => row.startsWith("userData="));
    
    if (!userCookie) {
        // Redirect to login if no user data
        window.location.href = "../Html/login.html";
        return;
    }

    // Parse user data from cookie
    const userData = JSON.parse(decodeURIComponent(userCookie.split("=")[1]));

    // Update user names
    document.getElementById("user-name").textContent = userData.name || "Guest";
    localStorage.setItem('username', userData.name);
    console.log(localStorage.getItem('username'));


    // Default user type
    let usertype = "client";

    try {
        // Fetch user type from the server
        const userResponse = await fetch(`http://localhost:8080/users/providers/${userData.name}`, {
            method: "GET",
            headers: { "Content-Type": "application/json" },
        });

        if (userResponse.ok) {
            const isProvider = await userResponse.json();
            usertype = isProvider ? "provider" : "client";
            localStorage.setItem('usertype', usertype); // Save the user type locally
            console.log("User type:", usertype);
        } else {
            console.error("Error fetching user type:", await userResponse.text());
        }
    } catch (error) {
        console.error("Error fetching user data:", error);
        alert("Failed to load user data. Please try again later.");
        return;
    }

    // Set up the page based on the user type
    if (usertype === "client") {
        setupClientView();
    } else if (usertype === "provider") {
        setupProviderView();
    }

    // Log-out button functionality
    document.getElementById("logout").addEventListener("click", () => {
        // Clear the user cookie and redirect
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });

    // Add search functionality
    const searchInput = document.getElementById("search-input");
    searchInput.addEventListener("input", async () => {
        const query = searchInput.value.trim();
        if (query.length > 0) {
            try {
                const response = await fetch(`/services/search?query=${encodeURIComponent(query)}`);
                const data = await response.json();

                if (response.ok) {
                    updateCarouselWithResults(data);
                } else {
                    console.error("Error fetching services:", data);
                }
            } catch (error) {
                console.error("Error:", error);
            }
        } else {
            clearCarousel();
        }
    });
});

// Function to set up the view for a client
function setupClientView() {
    console.log("Setting up client view.");
    document.getElementById("client-section").style.display = "block";
    document.getElementById("provider-section").style.display = "none";
}

// Function to set up the view for a provider
function setupProviderView() {
    console.log("Setting up provider view.");
    document.getElementById("provider-section").style.display = "block";
    document.getElementById("client-section").style.display = "none";
}

// Carousel code
const carouselContainer = document.querySelector('.carousel-container');
const prevButton = document.querySelector('.carousel-btn.prev');
const nextButton = document.querySelector('.carousel-btn.next');

let currentSlide = 0;
let totalSlides = 0;

prevButton.addEventListener('click', () => {
    if (currentSlide > 0) {
        currentSlide--;
        updateCarousel();
    }
});

nextButton.addEventListener('click', () => {
    if (currentSlide < totalSlides - 1) {
        currentSlide++;
        updateCarousel();
    }
});

function updateCarousel() {
    const slideWidth = carouselContainer.offsetWidth + 30;
    carouselContainer.style.transform = `translateX(-${currentSlide * slideWidth}px)`;
}

function updateCarouselWithResults(services) {
    carouselContainer.innerHTML = '';

    services.forEach(service => {
        const serviceCard = document.createElement("div");
        serviceCard.classList.add("service-card");
        serviceCard.innerHTML = `
            <h3>${service.title}</h3>
            <p>${service.description}</p>
        `;
        carouselContainer.appendChild(serviceCard);
    });

    totalSlides = services.length;
    updateCarousel();
}

function clearCarousel() {
    carouselContainer.innerHTML = '';
    totalSlides = 0;
}
