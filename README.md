# 🚀 REST API for Clients and Addresses

This project is a **REST API** built with **Quarkus**, a Supersonic Subatomic Java Framework.
The solution provides a complete set of endpoints to manage **clients** and their **addresses**, following a **clean layered architecture**, with features like **exception handling, validation (CPF/CNPJ), encryption, and pagination**.

---

## 🛠️ Key Technologies

* **Quarkus** → Modern Java framework for cloud-native applications.
* **JDBI 3** → SQL persistence library with lightweight abstractions.
* **PostgreSQL** → Relational database for reliable data storage.
* **Flyway** → Schema version control with migration scripts.
* **JUnit 5** → Unit and integration testing framework.
* **Jackson** → JSON serialization/deserialization.
* **Gradle** → Build automation tool.
* **Docker** → Containerization for consistent environments.

---

## 📂 Project Structure

* **Controller** → Handles HTTP requests and maps them to services.
* **Service** → Core business logic, validation, and transaction orchestration.
* **Repository** → Encapsulates persistence and database interaction.
* **Entity** → Database schema representation.
* **DTOs** → Transfer data between layers, isolating domain models.
* **Exception** → Centralized custom exception handling (consistent error responses).
* **Flyway Migrations** → `src/main/resources/db/migration` for schema versioning.

---

## 🔒 Features

* ✅ **Custom exception handling** → Unified error responses.
* ✅ **Password encryption** → Using `BcryptUtil`.
* ✅ **Validation for CPF and CNPJ** → Ensures domain integrity.
* ✅ **Pagination & filtering** → For efficient client listing.
* ✅ **Main address logic** → Enforces one main address per client.

---

## ⚙️ Setup and Execution

### 1. Clone the repository

```bash
git clone git@github.com:Maria-Leiliane/quarkus-client-address-api.git
cd quarkus-client-address-api
```

### 2. Configure the Database

Edit `application.properties`:

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=youruser
quarkus.datasource.password=yourpassword
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/client_address_db

quarkus.flyway.migrate-at-start=true
```

### 3. Run in Dev Mode (with live coding)

```bash
./gradlew quarkusDev
```

> Dev UI: [http://localhost:8080](http://localhost:8080)

For test we have the doc: [http://localhost:8080/q/swagger-ui/](http://localhost:8080/q/swagger-ui/)  
Other options: Import the `client-address-api.json` file and lets test it.

### 5. Run with Docker (optional)

```bash
docker build -t client-address-api .
docker run -p 8080:8080 client-address-api
```

---

## 📊 API Endpoints

### **Clients**

* `GET /clients` → List clients (pagination + filters).
* `GET /clients/{id}` → Retrieve client by ID.
* `POST /clients` → Create new client (with encrypted password).
* `PATCH /clients/{id}` → Partial update.
* `DELETE /clients/{id}` → Remove client.

### **Addresses** *(sub-resource of Client)*

* `GET /clients/{id}/addresses` → List client’s addresses.
* `POST /clients/{id}/addresses` → Add new address.
* `DELETE /clients/{id}/addresses/{addressId}` → Remove client’s address.

---
