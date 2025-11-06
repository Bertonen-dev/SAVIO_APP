🇬🇧 For English instructions, [click here](#-📱-savio-app-1)

# 📱 SAVIO APP

**SAVIO** es una aplicación móvil desarrollada en **Android Studio (Kotlin)** que actúa como un asistente personal inteligente, ayudando al usuario a organizar sus tareas, notas, listas de la compra, recordatorios y eventos desde una única plataforma.  

El proyecto fue desarrollado como **Trabajo de Fin de Ciclo** en el Grado Superior de Desarrollo de Aplicaciones Multiplataforma (2024–2025) por **Alberto Ortiz Fernández**.

---

## ⚠️ Estado del proyecto

Actualmente, la aplicación **no se encuentra operativa**, ya que se han detenido los servicios en la nube donde estaban alojadas la **API** y la **base de datos (Supabase)**.  
El código y la arquitectura siguen disponibles para referencia, documentación o futuras reactivaciones del proyecto.

---

## 🚀 Descripción general

En un entorno donde la productividad y la organización son esenciales, SAVIO ofrece una solución **todo-en-uno** que unifica funcionalidades dispersas en distintas apps (notas, calendario, listas, recordatorios, etc.).  
El objetivo es **optimizar el tiempo y mejorar la eficiencia personal**, con una interfaz moderna, intuitiva y accesible para cualquier tipo de usuario.

---

## 🧩 Funcionalidades principales

- 🗒️ **Gestión de notas** – Crear, editar y eliminar notas personales.  
- 🛒 **Listas de la compra** – Crear listas y marcar productos comprados.  
- 📅 **Eventos y recordatorios** – Programar tareas, eventos o recordatorios personalizados.  
- 🌤️ **Clima** – Consultar información meteorológica en cualquier ciudad del mundo.  
- 🤖 **Chat con IA** – Interactuar con un asistente virtual inteligente integrado.  
- 🎨 **Modo claro/oscuro** – Interfaz adaptable y accesible.

---

## 🏗️ Arquitectura del sistema

SAVIO se basa en una **arquitectura de tres capas**, lo que garantiza seguridad, escalabilidad y facilidad de mantenimiento:

1. **Aplicación móvil (Frontend)**  
   - Desarrollada en Kotlin con Android Studio.  
   - Interfaz construida con Jetpack Compose y Navigation Components.  

2. **API intermedia (Servidor Node.js)**  
   - Desarrollada con Express y desplegada en **Render**.  
   - Gestiona la lógica de negocio, validaciones y comunicación con la base de datos.  

3. **Backend (Supabase / PostgreSQL)**  
   - Base de datos relacional con autenticación y seguridad (RLS).  
   - Servicios de almacenamiento, autenticación y API RESTful autogenerada.

---

## ⚙️ Tecnologías utilizadas

| Capa | Tecnología |
|------|-------------|
| Frontend | Kotlin, Android Studio, Jetpack Compose |
| API | Node.js, Express |
| Backend | Supabase (PostgreSQL, Auth, RLS) |
| Otros | Render (hosting API), Postman, GitHub, Odoo (web promocional) |

---

## 🧠 Modelo de datos

El sistema utiliza una base de datos estructurada con las siguientes tablas principales:

- **Usuarios**
- **Notas**
- **Listas de compras**
- **Productos en lista**
- **Eventos**
- **Recordatorios**
- **Configuración**

Todas las relaciones se gestionan mediante claves foráneas (1:N, 1:1), garantizando la integridad referencial.

---

## 🧪 Pruebas y calidad

Durante el desarrollo se realizaron pruebas manuales y de integración, comprobando:

- ✅ Inicio de sesión y autenticación segura (JWT)  
- ✅ Comunicación entre app, API y base de datos  
- ✅ Creación y gestión de notas, listas, eventos y recordatorios  
- ✅ Integración de API meteorológica externa  
- ✅ Estabilidad sin conexión a internet  
- ✅ Compatibilidad en diferentes dispositivos Android  

---

## 📦 Puesta en marcha

### Requisitos previos
- Android Studio (versión 2023 o superior)  
- Node.js (v18 o superior)  
- Cuenta en [Supabase](https://supabase.com/)  
- Cuenta en [Render](https://render.com/)

### Ejecución
1. Clona este repositorio.  
2. Abre el proyecto en Android Studio.  
3. Configura las credenciales y la URL de la API (dentro del archivo de configuración de la aplicación).  
4. Compila y ejecuta en un emulador o dispositivo físico.  

> **Nota:** La API y la base de datos que anteriormente estaban alojadas en servicios en la nube se encuentran actualmente inactivas.  
> Para ejecutar la aplicación en local, será necesario desplegar o simular tanto la API como la base de datos — por ejemplo, creando tu propio proyecto en Supabase y desplegando la API Node.js en Render o en un entorno local.

***

# 🇬🇧📱 SAVIO APP

**SAVIO** is a mobile application developed in **Android Studio (Kotlin)** that acts as an intelligent personal assistant, helping users organize their tasks, notes, shopping lists, reminders, and events — all from a single platform.  

The project was developed as a **Final Degree Project** for the **Higher Degree in Multiplatform Application Development (2024–2025)** by **Alberto Ortiz Fernández**.

---

## ⚠️ Project Status

The application is **currently not operational**, as the cloud services hosting the **API** and **database (Supabase)** have been discontinued.  
The code and architecture remain available for reference, documentation, or future reactivation of the project.

---

## 🚀 Overview

In an environment where productivity and organization are essential, SAVIO offers an **all-in-one solution** that unifies features usually spread across different apps (notes, calendar, lists, reminders, etc.).  
Its main goal is to **optimize time and improve personal efficiency**, through a modern, intuitive, and accessible interface for all types of users.

---

## 🧩 Key Features

- 🗒️ **Notes management** – Create, edit, and delete personal notes.  
- 🛒 **Shopping lists** – Create lists and mark purchased items.  
- 📅 **Events and reminders** – Schedule custom tasks, events, or reminders.  
- 🌤️ **Weather** – Check weather information for any city worldwide.  
- 🤖 **AI Chat** – Interact with an integrated intelligent assistant.  
- 🎨 **Light/Dark mode** – Adaptive and accessible interface.

---

## 🏗️ System Architecture

SAVIO is based on a **three-layer architecture**, ensuring security, scalability, and easy maintenance:

1. **Mobile Application (Frontend)**  
   - Developed in Kotlin using Android Studio.  
   - Interface built with Jetpack Compose and Navigation Components.  

2. **Intermediate API (Node.js Server)**  
   - Built with Express and deployed via **Render**.  
   - Handles business logic, validations, and database communication.  

3. **Backend (Supabase / PostgreSQL)**  
   - Relational database with authentication and security (RLS).  
   - Provides storage, authentication, and an auto-generated RESTful API.

---

## ⚙️ Technologies Used

| Layer | Technology |
|-------|-------------|
| Frontend | Kotlin, Android Studio, Jetpack Compose |
| API | Node.js, Express |
| Backend | Supabase (PostgreSQL, Auth, RLS) |
| Others | Render (API hosting), Postman, GitHub, Odoo (Promotional website) |

---

## 🧠 Data Model

The system uses a structured database with the following main tables:

- **Users**
- **Notes**
- **Shopping Lists**
- **Products in List**
- **Events**
- **Reminders**
- **Configuration**

All relationships are managed through foreign keys (1:N, 1:1), ensuring referential integrity.

---

## 🧪 Testing and Quality

During development, both manual and integration tests were conducted to verify:

- ✅ Secure login and authentication (JWT)  
- ✅ Communication between app, API, and database  
- ✅ Creation and management of notes, lists, events, and reminders  
- ✅ Integration with external weather API  
- ✅ Stability without internet connection  
- ✅ Compatibility across different Android devices  

---

## 📦 Setup and Execution

### Prerequisites
- Android Studio (version 2023 or higher)  
- Node.js (v18 or higher)  
- Account on [Supabase](https://supabase.com/)  
- Account on [Render](https://render.com/)

### Execution
1. Clone this repository.  
2. Open the project in Android Studio.  
3. Configure the credentials and API URL (inside the app configuration file).  
4. Build and run on an emulator or physical device.  

> **Note:** The API and database previously hosted on cloud services are currently inactive.  
> To run the app locally, you will need to deploy or simulate both the API and the database — for example, by creating your own Supabase project and deploying the Node.js API on Render or locally.

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

## 👤 Autor / Author

**Alberto Ortiz Fernández**  
📍 Cantabria, Spain  
📧 bertonen.dev@gmail.com

---

## 🏁 Licencia / License
Este proyecto se publica bajo licencia MIT, lo que permite su libre uso, modificación y distribución, siempre que se mantenga el reconocimiento al autor original.
---
This project is published under the **MIT License**, allowing free use, modification, and distribution as long as credit is given to the original author.



