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
