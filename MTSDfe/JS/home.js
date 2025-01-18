document.addEventListener("DOMContentLoaded", () => {
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

    // Update the profile picture and user names
    document.getElementById("user-name").textContent = userData.name || "Guest";
    localStorage.setItem('username', userData.name);
    console.log(localStorage.getItem('username'));

    // Log-out button functionality
    document.getElementById("logout").addEventListener("click", () => {
        // Clear the user cookie
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });

    // Add search functionality
    const searchInput = document.getElementById("search-input");
    searchInput.addEventListener("input", async () => {
        const query = searchInput.value.trim();
        if (query.length > 0) {
            try {
                // Update fetch URL to match your @RequestMapping "/services"
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
            // Clear results if search bar is empty
            clearCarousel();
        }
    });
});

// Carousel code
const carouselContainer = document.querySelector('.carousel-container');
const prevButton = document.querySelector('.carousel-btn.prev');
const nextButton = document.querySelector('.carousel-btn.next');

let currentSlide = 0;
let totalSlides = 0; // This will be dynamically set based on the search results

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
    // Clear current carousel items
    carouselContainer.innerHTML = '';

    // Populate the carousel with new service cards based on the search results
    services.forEach(service => {
        const serviceCard = document.createElement("div");
        serviceCard.classList.add("service-card");
        serviceCard.innerHTML = `
            <h3>${service.title}</h3>
            <p>${service.description}</p>
        `;
        carouselContainer.appendChild(serviceCard);
    });

    // Update the total number of slides for navigation
    totalSlides = services.length;

    // Recalculate the carousel layout
    updateCarousel();
}

// Function to clear the carousel
function clearCarousel() {
    carouselContainer.innerHTML = '';
    totalSlides = 0; // Reset total slides when clearing the carousel
}
