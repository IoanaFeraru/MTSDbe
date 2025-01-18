function openServiceForm() {
    document.getElementById('serviceForm').style.display = 'block';
}

function closeServiceForm() {
    document.getElementById('serviceForm').style.display = 'none';
}

function saveService() {
    const serviceData = {
      name: document.getElementById('name').value,
      description: document.getElementById('description').value,
      domain: document.getElementById('domain').value,
      subdomain: document.getElementById('subdomain').value,
      price: parseFloat(document.getElementById('price').value),
      region: document.getElementById('region').value,
      materials: document.getElementById('materials').value.split(','),
      active: document.getElementById('active').checked,
      acceptedPaymentMethods: document.getElementById('acceptedPaymentMethods').value.split(','),
      serviceType: document.getElementById('serviceType').value,
      minimumBookingTime: parseInt(document.getElementById('minimumBookingTime').value)
    };
  
    fetch('http://localhost:8080/services/saveService', { 
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(serviceData)
    })
    .then(response => {
      if (!response.ok) {
        
        throw new Error('Failed to save service: ' + response.statusText); 
      }
      return response.json();
    })
    .then(data => {
      console.log('Success:', data); 
      alert('Service saved successfully.');
      closeServiceForm();
      
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error saving service: ' + error.message); 
    });
  }

function loadServices() {
    // Fetch and display the services in the #servicesList element
}

// Call loadServices on page load
window.onload = loadServices;
