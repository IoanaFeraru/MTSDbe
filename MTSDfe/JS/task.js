
// TASK 
document.getElementById("add-task-btn").addEventListener("click", function() {
    document.getElementById("add-task-modal").style.display = "block";
});

document.getElementById("close-add-task-modal").addEventListener("click", function() {
    document.getElementById("add-task-modal").style.display = "none";
});

document.getElementById("add-task-form").addEventListener("submit", function(event) {
    event.preventDefault();
    
    const taskData = {
        bookingId: currentBookingId,
        description: document.getElementById("description").value,
        dueDate: document.getElementById("due-date").value
    };
    console.log(taskData);

    fetch("http://localhost:8080/tasks/add", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        credentials: "include",
        body: JSON.stringify(taskData)
    })
    .then(response => {
        if (response.ok) {
            showBookingDetails(currentBookingId);
        } else {
            alert("Failed to add task.");
        }
        document.getElementById("add-task-modal").style.display = "none";
    })
    .catch(error => {
        console.error("Error adding task:", error);
        alert("An error occurred while adding the task.");
    });
});

window.addEventListener('click', function(event) {
    const modal = document.getElementById("add-task-modal");
    const modalContent = document.querySelector(".modal-content");
    if (event.target === modal) {
        modal.style.display = "none";
    }
});
