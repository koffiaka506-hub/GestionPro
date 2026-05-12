GestionPro

Application web de gestion commerciale — Spring Boot + React
Commercial Management Web Application — Spring Boot + React


🇫🇷 Français
📋 Description
GestionPro est une application web fullstack de gestion commerciale.
Le backend est développé avec Spring Boot (API REST) et le frontend avec React.js.
Elle permet à une entreprise de gérer efficacement ses produits, clients, fournisseurs, retours et utilisateurs depuis une interface moderne et sécurisée.

✨ Fonctionnalités

📦 Gestion des produits — Ajout, modification, suppression et consultation des produits
👥 Gestion des clients — Enregistrement et suivi des clients
🏭 Gestion des fournisseurs — Gestion complète des fournisseurs
🧾 Gestion des factures — Création, suivi et historique des factures clients
🔄 Gestion des retours — Suivi et traitement des retours produits
📊 Tableau de bord — Vue d'ensemble des statistiques et indicateurs clés
🔐 Gestion du compte / Profil — Modification du mot de passe et des informations de profil
🛡️ Sécurité — Authentification JWT avec Spring Security


🛠️ Technologies utilisées
Backend
TechnologieRôleJava 17Langage principalSpring Boot 3.xFramework backendSpring Security + JWTAuthentification & autorisationSpring Data JPAPersistance des donnéesMapStructMapping DTO ↔ EntitéPostgreSQLBase de donnéesMavenGestion des dépendances
Frontend
TechnologieRôleReact.jsFramework frontendAxiosAppels API RESTReact RouterNavigation entre pagesCSS / BootstrapStyle et mise en page

🏗️ Architecture
┌─────────────────────┐        REST API (JSON)       ┌─────────────────────┐
│                     │ ◄──────────────────────────► │                     │
│   React Frontend    │         HTTP / JWT            │  Spring Boot API    │
│   (port 3000)       │                               │  (port 8080)        │
│                     │                               │                     │
└─────────────────────┘                               └──────────┬──────────┘
                                                                 │
                                                                 ▼
                                                      ┌─────────────────────┐
                                                      │    PostgreSQL DB     │
                                                      └─────────────────────┘

⚙️ Installation et lancement
Prérequis

Java 17+
Node.js 18+
PostgreSQL
Maven


🔧 Backend (Spring Boot)
bash# 1. Cloner le projet
git clone https://gitlab.com/ton-utilisateur/GestionPro.git
cd GestionPro/backend

# 2. Configurer la base de données
# Modifier src/main/resources/application.properties :
spring.datasource.url=jdbc:postgresql://localhost:5432/gestionpro
spring.datasource.username=ton_utilisateur
spring.datasource.password=ton_mot_de_passe

# 3. Lancer le backend
mvn spring-boot:run
API disponible sur : http://localhost:8080

🎨 Frontend (React)
bashcd GestionPro/frontend

# Installer les dépendances
npm install

# Lancer le frontend
npm start
Interface disponible sur : http://localhost:3000

📁 Structure du projet
GestionPro/
├── backend/
│   └── src/main/java/com/gestionpro/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       ├── mapper/
│       └── security/
│
└── frontend/
    └── src/
        ├── components/
        ├── pages/
        ├── services/       ← appels Axios
        └── App.js

👤 Auteur
KOFFI FRANCK
Étudiant BTS Informatique — option Développeur d'Applications
Abidjan, Côte d'Ivoire


🇬🇧 English
📋 Description
GestionPro is a fullstack commercial management web application.
The backend is built with Spring Boot (REST API) and the frontend with React.js.
It enables businesses to efficiently manage products, customers, suppliers, returns, and user accounts through a modern and secure interface.

✨ Features

📦 Product Management — Add, update, delete and browse products
👥 Customer Management — Register and track customers
🏭 Supplier Management — Full supplier lifecycle management
🧾 Invoice Management — Create, track and view customer invoice history
🔄 Returns Management — Track and process product returns
📊 Dashboard — Overview of key statistics and business indicators
🔐 Account / Profile Management — Update password and profile information
🛡️ Security — JWT authentication with Spring Security


🛠️ Tech Stack
Backend
TechnologyRoleJava 17Main languageSpring Boot 3.xBackend frameworkSpring Security + JWTAuthentication & authorizationSpring Data JPAData persistenceMapStructDTO ↔ Entity mappingPostgreSQLDatabaseMavenDependency management
Frontend
TechnologyRoleReact.jsFrontend frameworkAxiosREST API callsReact RouterPage navigationCSS / BootstrapStyling & layout

🏗️ Architecture
┌─────────────────────┐        REST API (JSON)       ┌─────────────────────┐
│                     │ ◄──────────────────────────► │                     │
│   React Frontend    │         HTTP / JWT            │  Spring Boot API    │
│   (port 3000)       │                               │  (port 8080)        │
│                     │                               │                     │
└─────────────────────┘                               └──────────┬──────────┘
                                                                 │
                                                                 ▼
                                                      ┌─────────────────────┐
                                                      │    PostgreSQL DB     │
                                                      └─────────────────────┘

⚙️ Setup & Run
Prerequisites

Java 17+
Node.js 18+
PostgreSQL
Maven


🔧 Backend (Spring Boot)
bash# 1. Clone the project
git clone https://gitlab.com/your-username/GestionPro.git
cd GestionPro/backend

# 2. Configure the database
# Edit src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/gestionpro
spring.datasource.username=your_username
spring.datasource.password=your_password

# 3. Run the backend
mvn spring-boot:run
API available at: http://localhost:8080

🎨 Frontend (React)
bashcd GestionPro/frontend

# Install dependencies
npm install

# Start the frontend
npm start
Interface available at: http://localhost:3000

📁 Project Structure
GestionPro/
├── backend/
│   └── src/main/java/com/gestionpro/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       ├── mapper/
│       └── security/
│
└── frontend/
    └── src/
        ├── components/
        ├── pages/
        ├── services/       ← Axios API calls
        └── App.js

👤 Author
KOFFI FRANCK
BTS Computer Science Student — Application Developer
Abidjan, Côte d'Ivoire

Built with ❤️ using Spring Boot, React & PostgreSQL
