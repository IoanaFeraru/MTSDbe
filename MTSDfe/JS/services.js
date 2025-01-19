function openServiceForm() {
    document.getElementById('serviceForm').style.display = 'block';
}

function closeServiceForm() {
    document.getElementById('serviceForm').style.display = 'none';
}

const subdomains = {
  ARTISTICE: ["Fotografie artistică", "Pictură", "Muzică", "Teatru", "Dans", "Diverse"],
  CONSTRUCTII: ["Reparații", "Construcții noi", "Planificare proiecte", "Renovare", "Diverse"],
  EDUCATIE: ["Meditații", "Cursuri de sprijin", "Formare profesională", "Tutori", "Diverse"],
  INTRETINERE_SI_REPARARE: ["Reparații aparatură", "Întreținere auto", "Reparații electrice", "Diverse"],
  INFRUMUSETARE: ["Frizuri", "Manichiură", "Makeup", "Tratamente", "Diverse"],
  SANATATE: ["Tratamente", "Terapie fizică", "Diverse"],
  TRANSPORT: ["Transport persoane", "Transport marfă", "Diverse"],
  FINANCIARE: ["Consultanță fiscală", "Asigurări", "Planificare financiară", "Diverse"],
  INFORMATICE: ["Web design", "Dezvoltare software", "Securitate", "Diverse"],
  CONTABILE_SI_DE_CONSULTANTA: ["Servicii contabile", "Consultanță juridică", "Planificare fiscală", "Diverse"],
  EVENIMENTE: ["Organizare nuntă", "Conferințe", "Diverse"],
  ARCHITECTURA_SI_INGINERIE: ["Proiectare arhitecturală", "Consultanță inginerie", "Diverse"],
  DIVERSE: ["Diverse"]
};

function populateSubdomains() {
  const domainSelect = document.getElementById("domain");
  const subdomainSelect = document.getElementById("subdomain");
  const selectedDomain = domainSelect.value;

  subdomainSelect.innerHTML = '<option value="">Selectează Subdomeniul</option>';

  if (subdomains[selectedDomain]) {
      subdomains[selectedDomain].forEach(sub => {
          const option = document.createElement("option");
          option.value = sub;
          option.textContent = sub;
          subdomainSelect.appendChild(option);
      });
  }
}

document.getElementById("logout").addEventListener("click", () => {
  document.cookie = "userData=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC";
  window.location.href = "../Html/landingpage.html";
});

function saveService() {
  const userCookie = document.cookie
      .split("; ")
      .find((row) => row.startsWith("userData="));

  if (!userCookie) {
      window.location.href = "../Html/login.html";
      return;
  }

  const userData = JSON.parse(decodeURIComponent(userCookie.split("=")[1]));

  const serviceData = {
      name: document.getElementById("name").value,
      description: document.getElementById("description").value,
      domain: document.getElementById("domain").value,
      subdomain: document.getElementById("subdomain").value,
      price: parseFloat(document.getElementById("price").value),
      region: document.getElementById("region").value,
      acceptedPaymentMethods: document.getElementById("acceptedPaymentMethods").value.split(","),
      serviceType: document.getElementById("serviceType").value,
      minimumBookingTime: parseInt(document.getElementById("minimumBookingTime").value),
      username: userData.name
  };

  fetch("http://localhost:8080/services", {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
      },
      body: JSON.stringify(serviceData),
  })
      .then((response) => {
          if (response.ok) {
              return response.text();
          } else {
              throw new Error("Eroare la salvarea serviciului.");
          }
      })
      .then((message) => {
          alert(message); 
          closeServiceForm();
      })
      .catch((error) => {
          console.error(error);
          alert("A apărut o eroare la salvarea serviciului.");
      });
}

function loadServices() {
  const userCookie = document.cookie
      .split("; ")
      .find((row) => row.startsWith("userData="));

  if (!userCookie) {
      window.location.href = "../Html/login.html";
      return;
  }

  const userData = JSON.parse(decodeURIComponent(userCookie.split("=")[1]));

  const username = userData.name;

  fetch(`http://localhost:8080/services/provider/${username}`, {
    method: "GET"
  })
  .then((response) => {
      if (response.ok) {
          return response.json(); 
      } else {
          throw new Error("Eroare la încărcarea serviciilor.");
      }
  })
  .then((services) => {
      displayServices(services);
  })
  .catch((error) => {
      console.error(error);
      alert("A apărut o eroare la încărcarea serviciilor.");
  });
}

function displayServices(services) {
  const servicesList = document.getElementById('servicesList');
  servicesList.innerHTML = '';  // Clear previous content

  services.forEach(service => {
      const li = document.createElement('li');
      li.id = `service-${service.id}`;

      const serviceDetails = document.createElement('div');
      
      serviceDetails.innerHTML = `
          <strong>Name:</strong> ${service.name} <br>
          <strong>Domain:</strong> ${service.domain} <br>
          <strong>Subdomain:</strong> ${service.subdomain} <br>
          <strong>Price:</strong> ${service.price} <br>
          <strong>Description:</strong> ${service.description || 'No description available'} <br>
          <strong>Status:</strong> ${service.active ? 'Active' : 'Inactive'} <br>
      `;
      
      // Create Edit button
      const editButton = document.createElement('button');
      editButton.textContent = 'Edit';
      editButton.classList.add('edit-button');
      editButton.onclick = () => editService(service); 
      
      // Create Delete button
      const deleteButton = document.createElement('button');
      deleteButton.textContent = 'Delete';
      deleteButton.classList.add('delete-button');
      deleteButton.onclick = () => deleteService(service.id);  

      serviceDetails.appendChild(editButton);
      serviceDetails.appendChild(deleteButton);

      li.appendChild(serviceDetails);
      servicesList.appendChild(li);
  });
}

function deleteService(serviceId) {
  const confirmation = confirm('Are you sure you want to delete this service?');
  if (!confirmation) return;

  fetch(`http://localhost:8080/services/${serviceId}`, {
    method: 'DELETE',
  })
  .then(response => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Failed to delete the service');
    }
  })
  .then(data => {
    alert(data.message);
    const serviceElement = document.getElementById(`service-${serviceId}`);
    if (serviceElement) {
      serviceElement.remove();
    }
  })
  .catch(error => {
    alert('Error: ' + error.message);
  });
}

function editService(service) {
  console.log(service);

  fetch(`http://localhost:8080/services/id/${service.id}`, {
    method: 'GET',
    credentials: 'include'
  })
  .then(response => response.json())
  .then(serviceData => {
    document.getElementById('serviceId').value = serviceData.id;
    document.getElementById('serviceName').value = serviceData.name;
    document.getElementById('serviceDescription').value = serviceData.description;
    document.getElementById('serviceDomain').value = serviceData.domain;
    document.getElementById('serviceSubdomain').value = serviceData.subdomain;
    document.getElementById('servicePrice').value = serviceData.price;
    document.getElementById('serviceRegion').value = serviceData.region;
    document.getElementById('serviceActive').checked = serviceData.active;
    document.getElementById('serviceServiceType').value = serviceData.serviceType;
    document.getElementById('serviceMinimumBookingTime').value = serviceData.minimumBookingTime;

    document.getElementById("editServiceForm").style.display = "block";
    document.getElementById("modalOverlay").style.display = "block";
})
  .catch(error => {
    console.error('Error fetching service data:', error);
    alert('Eroare la încărcarea serviciului!');
  });
}

function closeEditServiceForm() {
  document.getElementById('editServiceForm').style.display = 'none';
  document.getElementById('modalOverlay').style.display = 'none';
}

function submitEditService(event) {
  event.preventDefault();

  const serviceId = document.getElementById('serviceId').value;
  const updatedService = {
    id: serviceId,
    name: document.getElementById('serviceName').value,
    description: document.getElementById('serviceDescription').value,
    domain: document.getElementById('serviceDomain').value,
    subdomain: document.getElementById('serviceSubdomain').value,
    price: parseFloat(document.getElementById('servicePrice').value),
    region: document.getElementById('serviceRegion').value,
    active: document.getElementById('serviceActive').checked,
    serviceType: document.getElementById('serviceServiceType').value,
    minimumBookingTime: parseInt(document.getElementById('serviceMinimumBookingTime').value)
  };

  fetch(`http://localhost:8080/services/${serviceId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify(updatedService),
  })
    .then(response => response.text())  
    .then(data => {
      try {
        const parsedData = JSON.parse(data);
        alert(parsedData.message); 
        location.reload();
      } catch (error) {
        alert('Error: ' + data);
      }
    })
    .catch(error => {
      alert('Error: ' + error.message);
    });

    closeServiceForm()
}

window.onload = loadServices;
