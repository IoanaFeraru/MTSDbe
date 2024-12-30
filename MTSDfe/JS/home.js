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

    // Update the profile picture and user name
    document.getElementById("profile-pic").src = userData.profilePic || "default-profile.png";
    document.getElementById("user-name").textContent = userData.name || "Guest";

    // Log-out button functionality
    document.getElementById("logout").addEventListener("click", () => {
        // Clear the user cookie
        document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
        // Redirect to login page
        window.location.href = "../Html/landingpage.html";
    });
});

const carouselContainer = document.querySelector('.carousel-container');
const prevButton = document.querySelector('.carousel-btn.prev');
const nextButton = document.querySelector('.carousel-btn.next');

let currentSlide = 0;
const totalSlides = 2; 

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
