function openServiceForm() {
    document.getElementById('serviceForm').style.display = 'block';
}

function closeServiceForm() {
    document.getElementById('serviceForm').style.display = 'none';
}

const subdomains = {
    ARTISTICE: ["Fotografie artistica", "Pictura", "Muzica", "Teatru", "Dans", "Diverse"],
    CONSTRUCȚII: ["Reparatii", "Constructii noi", "Planificare proiecte", "Renovare", "Diverse"],
    EDUCAȚIE: ["Meditatii", "Cursuri de sprijin", "Formare profesionala", "Tutori", "Diverse"],
    INTREȚINERE_SI_REPARARE: ["Reparatii aparatura", "Intretinere auto", "Reparatii electrice", "Reparatii mecanice", "Reparatii instalatii", "Diverse"],
    INFRUMUSEȚARE: ["Frizuri", "Manichiura", "Makeup", "Tratamente", "Diverse"],
    SĂNĂTATE: ["Tratamente", "Terapie fizica", "Diverse"],
    TRANSPORT: ["Transport persoane", "Transport marfa", "Transport medical", "Diverse"],
    FINANCIARE: ["Consultanta fiscala", "Asigurari", "Planificare financiara", "Finante personale", "Diverse"],
    INFORMATICE: ["Web design", "Dezvoltare software", "Administrare retele", "Securitate informatiilor", "Dezvoltare aplicatii mobile", "Data science", "Machine learning", "Diverse"],
    CONTABILE_SI_DE_CONSULTANȚĂ: ["Servicii contabile", "Consultanta juridica", "Planificare fiscala", "Consultanta financiara", "Diverse"],
    EVENIMENTE: ["Organizare nunta", "Organizare conferinte", "Planificare evenimente corporative", "Diverse"],
    ARCHITECTURA_SI_INGINERIE: ["Proiectare arhitecturala", "Consultanta inginerie", "Diverse"],
    TRATAMENT_DEȘEURI: ["Diverse"],
    PERSONALE: ["Diverse"],
    PUBLICITATE_CERCETARE_DE_PIATĂ_SI_SONDAJE_DE_OPINIE: ["Diverse"]
};

const subdomainMapping = {
    "Fotografie artistica": "ARTISTICE_fotografie_artistica",
    "Pictura": "ARTISTICE_pictura",
    "Muzica": "ARTISTICE_muzica",
    "Teatru": "ARTISTICE_teatru",
    "Dans": "ARTISTICE_dans",
    "Diverse": "ARTISTICE_diverse",
    "Reparatii": "CONSTRUCTII_reparatii",
    "Constructii noi": "CONSTRUCTII_constructii_noua",
    "Planificare proiecte": "CONSTRUCTII_planificare_proiecte",
    "Renovare": "CONSTRUCTII_renovare",
    "Meditatii": "EDUCATIE_meditatii",
    "Cursuri de sprijin": "EDUCATIE_cursuri_de_sprijin",
    "Formare profesionala": "EDUCATIE_formare_profesionala",
    "Tutori": "EDUCATIE_tutori",
    "Reparatii aparatura": "INTRETINERE_SI_REPARARE_reparatii_aparatura",
    "Intretinere auto": "INTRETINERE_SI_REPARARE_intretinere_auto",
    "Reparatii electrice": "INTRETINERE_SI_REPARARE_reparatii_electrice",
    "Reparatii mecanice": "INTRETINERE_SI_REPARARE_reparatii_mecanice",
    "Reparatii instalatii": "INTRETINERE_SI_REPARARE_reparatii_instalatii",
    "Frizuri": "INFRUMUSEȚARE_frizuri",
    "Manichiura": "INFRUMUSEȚARE_manicura",
    "Makeup": "INFRUMUSEȚARE_makeup",
    "Tratamente": "INFRUMUSEȚARE_tratamente",
    "Tratamente": "SANATATE_tratamente",
    "Terapie fizica": "SANATATE_terapie_fizica",
    "Transport persoane": "TRANSPORT_transport_persone",
    "Transport marfa": "TRANSPORT_transport_marfa",
    "Transport medical": "TRANSPORT_transport_medical",
    "Consultanta fiscala": "FINANCIARE_consultanta_fiscala",
    "Asigurari": "FINANCIARE_asigurari",
    "Planificare financiara": "FINANCIARE_planificare_financiara",
    "Finante personale": "FINANCIARE_finante_personale",
    "Web design": "INFORMATICE_web_design",
    "Dezvoltare software": "INFORMATICE_dezvoltare_soft",
    "Administrare retele": "INFORMATICE_administare_retele",
    "Securitate informatiilor": "INFORMATICE_securitate_informatiilor",
    "Dezvoltare aplicatii mobile": "INFORMATICE_dezvoltare_aplicatii_mobile",
    "Data science": "INFORMATICE_data_science",
    "Machine learning": "INFORMATICE_machine_learning",
    "Servicii contabile": "CONTABILE_SI_DE_CONSULTANTA_servicii_contabile",
    "Consultanta juridica": "CONTABILE_SI_DE_CONSULTANTA_consultanta_juridica",
    "Planificare fiscala": "CONTABILE_SI_DE_CONSULTANTA_planificare_fiscala",
    "Consultanta financiara": "CONTABILE_SI_DE_CONSULTANTA_consultanta_financiara",
    "Organizare nunta": "EVENIMENTE_organizare_nunta",
    "Organizare conferinte": "EVENIMENTE_organizare_conferinte",
    "Planificare evenimente corporative": "EVENIMENTE_planificare_evenimente_corporative",
    "Proiectare arhitecturala": "ARCHITECTURA_SI_INGINERIE_proiectare_architecturala",
    "Consultanta inginerie": "ARCHITECTURA_SI_INGINERIE_consultanta_inginerie",
    "Diverse": "DIVERSE"
};

function getMappedSubdomain(displayName) {
  return subdomainMapping[displayName] || null;
}

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


function closeMessageModal() {
  const modal = document.getElementById("messageModal");
  modal.style.display = "none";
}

function saveService() {
  const userCookie = document.cookie
      .split("; ")
      .find((row) => row.startsWith("userData="));

  if (!userCookie) {
      window.location.href = "../Html/login.html";
      return;
  }

  const userData = JSON.parse(decodeURIComponent(userCookie.split("=")[1]));

  const selectedSubdomain = document.getElementById("subdomain").value;
  const mappedSubdomain = getMappedSubdomain(selectedSubdomain); 

  if (!mappedSubdomain) {
      console.error("Invalid subdomain selected:", selectedSubdomain);
      showMessageModal("Subdomeniul selectat este invalid.");
      return; 
  }

  const serviceData = {
      name: document.getElementById("name").value,
      description: document.getElementById("description").value,
      domain: document.getElementById("domain").value,
      subdomain: mappedSubdomain,
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
          showMessageModal(message); 
          closeServiceForm();
          loadServices();
      })
      .catch((error) => {
          console.error(error);
          showMessageModal("A apărut o eroare la salvarea serviciului.");
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
  servicesList.innerHTML = ''; 

  services.forEach(service => {
      const li = document.createElement('li');
      li.id = `service-${service.id}`;
      li.classList.add('service-item');

      const serviceDetails = document.createElement('div');
      serviceDetails.classList.add('service-details');

      serviceDetails.innerHTML = `
          <strong>Name:</strong> ${service.name} <br>
          <strong>Domain:</strong> ${service.domain} <br>
          <strong>Subdomain:</strong> ${service.subdomain} <br>
          <strong>Price:</strong> ${service.price} <br>
          <strong>Description:</strong> ${service.description || 'No description available'} <br>
          <strong>Status:</strong> ${service.active ? 'Active' : 'Inactive'} <br>
      `;
      
      const editButton = document.createElement('button');
      editButton.textContent = 'Edit';
      editButton.classList.add('edit-button');
      editButton.onclick = () => editService(service); 
      
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
    .then(response => response.json())
    .then(data => {
      if (data.message) {
        showMessageModal(data.message);
      }
      location.reload();
    })
    .catch(error => {
      showMessageModal('Error: ' + error.message);
    });

  closeServiceForm();
}

function showMessageModal(message) {
  const modal = document.getElementById("messageModal");
  const modalMessage = document.getElementById("modalMessage");
  modalMessage.textContent = message;
  modal.style.display = "block";
}

window.onload = loadServices;
