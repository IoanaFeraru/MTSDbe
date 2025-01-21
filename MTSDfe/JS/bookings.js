const API_BASE_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
    const userData = redirectIfNotLoggedIn();
    if (!userData) return;

    const username = userData.name;
    localStorage.setItem('username', username);

    checkProviderStatus(username);

    const bookingsList = document.getElementById("bookings-list");

    if (document.getElementById("bookings-list")) {
        loadBookings(username);
        return;
    }

    if(document.getElementById("Servicebookings-list")){
        loadProviderBookings(username);
        return;
    }
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
        console.log("Bookings:", bookings);

        bookingsContainer.innerHTML = "";
        if (bookings.length === 0) {
            bookingsContainer.innerHTML = "<p>No bookings found.</p>";
            return;
        }

        bookings.forEach(booking => {
            console.log("Creating list item for booking:", booking);

            const listItem = document.createElement("li");
            listItem.setAttribute("data-booking-id", booking.id);

            listItem.innerHTML = `
                <div class="booking-header">Service Name: ${booking.serviceName}</div>
                <div class="service-info">
                    <span><strong>Date:</strong> ${booking.dueDate}</span>
                    <span><strong>Time:</strong> ${booking.dueTime}</span>
                </div>
                <div class="service-info">
                    <span><strong>Delivery Address:</strong> ${booking.deliveryAddress}</span>
                    <span><strong>Price:</strong> $${booking.price}</span>
                </div>
                <div class="booking-state">
                    <strong>State:</strong> ${booking.bookingState}
                </div>
            `;

            listItem.addEventListener("click", () => {
                showBookingDetailsClient(booking.id);
            });

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

function loadProviderBookings(username) {
    const bookingsContainer = document.getElementById("Servicebookings-list");

    if (!bookingsContainer) {
        console.error("Elementul Servicebookings-list nu a fost găsit!");
        return;
    }

    fetch(`http://localhost:8080/bookings/provider/${username}`, {
        method: "GET",
        credentials: "include"
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch provider bookings");
        }
        return response.json();
    })
    .then(bookings => {
        console.log("Bookings fetched:", bookings);

        bookingsContainer.innerHTML = "";
        if (bookings.length === 0) {
            bookingsContainer.innerHTML = "<p>No booked services found.</p>";
            return;
        }

        bookings.forEach(booking => {
            const listItem = document.createElement("li");
            listItem.setAttribute("data-booking-id", booking.id);
            
            listItem.innerHTML = `
                <div class="booking-header">Customer: ${booking.username}</div>
                <div class="service-info">
                    <span>Service: ${booking.serviceName}</span>
                </div>
                <div class="booking-details">
                    <p><strong>Date:</strong> ${booking.dueDate}</p>
                    <p><strong>Time:</strong> ${booking.dueTime || 'N/A'}</p>
                    <p><strong>Delivery Address:</strong> ${booking.deliveryAddress}</p>
                    <p class="price"><strong>Price:</strong> $${booking.price}</p>
                    <p class="booking-state"><strong>State:</strong> ${booking.bookingState}</p>
                </div>
            `;
            listItem.addEventListener("click", () => {
                showBookingDetails(booking.id);
            });

            bookingsContainer.appendChild(listItem);
        });
    })
    .catch(error => {
        console.error("Error loading provider bookings:", error);
        bookingsContainer.innerHTML = "<p>Unable to load provider bookings.</p>";
    });
}

//butoane nav in functie de user type
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
            document.getElementById('bookings-link').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error checking provider status:', error);
    });
});

//modal
let currentBookingId = null;

function showBookingDetails(bookingId) {
    currentBookingId = bookingId;
    const modal = document.getElementById("booking-modal");
    const modalContent = document.getElementById("modal-content");
    const completeButton = document.getElementById("complete-booking-btn");
    const tasksList = document.getElementById("tasks-list"); 

    completeButton.disabled = false;

    fetch(`http://localhost:8080/bookings/bookings/${bookingId}`, {
        method: "GET",
        credentials: "include",
    })
    .then(response => response.json())
    .then(booking => {
        modalContent.innerHTML = `
            <p><strong>Customer:</strong> ${booking.username}</p>
            <p><strong>Service Name:</strong> ${booking.serviceName}</p>
            <p><strong>Due Date:</strong> ${booking.dueDate}</p>
            <p><strong>Due Time:</strong> ${booking.dueTime || 'N/A'}</p>
            <p><strong>Delivery Address:</strong> ${booking.deliveryAddress}</p>
            <p><strong>Price:</strong> $${booking.price}</p>
            <p><strong>Booking State:</strong> ${booking.bookingState}</p>
        `;
        modal.style.display = "block";

        // Taskuri
        fetch(`http://localhost:8080/tasks/findByBooking?bookingId=${bookingId}`, {
            method: "GET",
            credentials: "include",
        })
        .then(response => response.json())
        .then(tasks => {
            console.log('Received tasks:', tasks); 
            tasksList.innerHTML = '';
            if (tasks && tasks.length > 0) {
                tasks.forEach(task => {
                    const taskItem = document.createElement("li");
                    taskItem.textContent = `${task.taskNumber}: ${task.description}, Due: ${task.dueDate}, Status: ${task.status}`;

                    const statusDropdown = document.createElement("select");
                    const states = ["TODO", "DOING", "DONE", "CANCELED", "OVERDUE"];
                    states.forEach(state => {
                        const option = document.createElement("option");
                        option.value = state;
                        option.textContent = state;
                        if (state === task.status) {
                            option.selected = true;
                        }
                        statusDropdown.appendChild(option);
                    });

                    statusDropdown.addEventListener("change", (event) => {
                        const selectedState = event.target.value;

                        fetch(`http://localhost:8080/tasks/manageState?taskNumber=${task.taskNumber}&bookingId=${bookingId}&state=${selectedState}`, {
                            method: "PUT",
                            credentials: "include",
                        })
                        .then(response => {
                            if (response.ok) {
                                alert(`Task ${task.taskNumber} state updated to ${selectedState}`);
                                showBookingDetails(currentBookingId);
                            } else {
                                alert("Failed to update task state.");
                            }
                        })
                        .catch(error => {
                            console.error("Error updating task state:", error);
                        });
                    });

                    taskItem.appendChild(statusDropdown);
                    tasksList.appendChild(taskItem);
                });
            } else {
                tasksList.innerHTML = "<p>No tasks available for this booking.</p>";
            }
        })
        .catch(error => {
            console.error("Error fetching tasks:", error);
        });

        // Complete
        completeButton.addEventListener("click", () => {
            completeButton.disabled = true;

            fetch(`http://localhost:8080/bookings/${bookingId}/complete`, {
                method: "PUT",
                credentials: "include",
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message || "Booking completed successfully.");
                modal.style.display = "none";
                loadProviderBookings(username);
            })
            .catch(error => {
                console.error("Error completing booking:", error);
                alert("An error occurred while completing the booking.");
            });
        });
    })
    .catch(error => {
        console.error("Error fetching booking details:", error);
    });
}

function showBookingDetailsClient(bookingId) {
    currentBookingId = bookingId;
    const modal = document.getElementById("booking-modal");
    const modalContent = document.getElementById("modal-content");
    const tasksList = document.getElementById("tasks-list"); 

    fetch(`http://localhost:8080/bookings/bookings/${bookingId}`, {
        method: "GET",
        credentials: "include",
    })
    .then(response => response.json())
    .then(booking => {
        modalContent.innerHTML = `
            <p><strong>Customer:</strong> ${booking.username}</p>
            <p><strong>Service Name:</strong> ${booking.serviceName}</p>
            <p><strong>Due Date:</strong> ${booking.dueDate}</p>
            <p><strong>Due Time:</strong> ${booking.dueTime || 'N/A'}</p>
            <p><strong>Delivery Address:</strong> ${booking.deliveryAddress}</p>
            <p><strong>Price:</strong> $${booking.price}</p>
            <p><strong>Booking State:</strong> ${booking.bookingState}</p>
        `;
        modal.style.display = "block";

        // Taskuri
        fetch(`http://localhost:8080/tasks/findByBooking?bookingId=${bookingId}`, {
            method: "GET",
            credentials: "include",
        })
        .then(response => response.json())
        .then(tasks => {
            console.log('Received tasks:', tasks); 
            tasksList.innerHTML = '';
            if (tasks && tasks.length > 0) {
                tasks.forEach(task => {
                    const taskItem = document.createElement("li");
                    taskItem.textContent = `${task.taskNumber}: ${task.description}, Due: ${task.dueDate}, Status: ${task.status}`;
                    tasksList.appendChild(taskItem);
                });
            } else {
                tasksList.innerHTML = "<p>No tasks available for this booking.</p>";
            }
        })
        .catch(error => {
            console.error("Error fetching tasks:", error);
        });
    })
    .catch(error => {
        console.error("Error fetching booking details:", error);
    });
}


document.getElementById("close-modal").addEventListener("click", () => {
    document.getElementById("booking-modal").style.display = "none";
});

window.addEventListener("click", (event) => {
    const modal = document.getElementById("booking-modal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
});
