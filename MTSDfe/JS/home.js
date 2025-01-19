function getUserDataFromCookie() {
    const userCookie = document.cookie
        .split("; ")
        .find((row) => row.startsWith("userData="));

    if (!userCookie) {
        return null; 
    }

    const userData = JSON.parse(decodeURIComponent(userCookie.split("=")[1]));
    return userData;
}

async function fetchActiveServices() {
    try {
        const response = await fetch("http://localhost:8080/services/active", {
            method: "GET", 
            credentials: "include"
        });
        const services = await response.json();

        if (response.ok) {
            updateCarouselWithResults(services);
        } else {
            console.error("Error fetching active services:", services);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

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
        const truncatedDescription = service.description.length > 100 ? service.description.substring(0, 100) + '...' : service.description;

        const serviceCard = document.createElement("div");
        serviceCard.classList.add("service-card");
        serviceCard.innerHTML = `
            <div class="service-image-placeholder">
                <img src="https://via.placeholder.com/150" alt="Service Image" style="width:100%; height:100%; object-fit:cover;" /> <!-- Placeholder image -->
            </div>
            <h3>${service.name}</h3>
            <p><strong>Description:</strong> ${truncatedDescription}</p>
            <p><strong>Domain:</strong> ${service.domain}</p>
            <p><strong>Subdomain:</strong> ${service.subdomain}</p>
            <p><strong>Price:</strong> ${service.price} RON</p>
            <p><strong>Region:</strong> ${service.region}</p>
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

document.addEventListener("DOMContentLoaded", () => {
    const userData = getUserDataFromCookie();
    
    if (!userData) {
        window.location.href = "../Html/login.html";
        return;
    }

    document.getElementById("user-name").textContent = userData.name || "Guest";
    localStorage.setItem('username', userData.name);
    console.log(localStorage.getItem('username'));

    const searchInput = document.getElementById("search-input");

    searchInput.addEventListener("input", () => {
        const query = searchInput.value.trim();
        fetchServicesWithSearch(query);
    });

    document.getElementById("logout").addEventListener("click", () => {
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        window.location.href = "../Html/landingpage.html";
    });

    fetchActiveServices();
});

window.addEventListener('DOMContentLoaded', function () {
    const userData = getUserDataFromCookie();

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
        }
    })
    .catch(error => {
        console.error('Error checking provider status:', error);
    });
});