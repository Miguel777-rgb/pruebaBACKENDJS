<div align="center">

<h1>
  <img src="https://api.iconify.design/lucide/utensils-crossed.svg?color=%23f59e0b" width="32" height="32" align="absmiddle" alt="Maki Icon" />
  Pompompurin — Maki REST API
</h1>

<p>
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.1.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Gradle-9.8.2-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
  <img src="https://img.shields.io/badge/GitHub_Codespaces-Ready-181717?style=for-the-badge&logo=github&logoColor=white" alt="Codespaces" />
  <img src="https://img.shields.io/badge/Postman-Collection-FF6C37?style=for-the-badge&logo=postman&logoColor=white" alt="Postman" />
</p>

<p>
  Aplicación REST minimalista desarrollada con <b>Spring Boot</b> bajo una arquitectura limpia en capas (<code>Controller -> Service -> Model</code>) para la consulta y registro de makis en memoria.
</p>

</div>

---

## <img src="https://api.iconify.design/lucide/layers.svg?color=%233b82f6" width="22" height="22" align="absmiddle" alt="Architecture" /> Arquitectura del Proyecto (`pompompurin`)

El proyecto sigue una estructura de **N-Capas simplificada** donde cada clase tiene una única responsabilidad clara:

```text
pompompurin/src/main/java/com/maki/pompompurin/
├── PompompurinApplication.java      # Punto de entrada de Spring Boot (@SpringBootApplication)
├── controller/
│   └── MakiController.java          # Expone los endpoints HTTP GET y POST (@RestController)
├── service/
│   └── MakiService.java             # Lógica de negocio y lista en memoria (@Service)
└── model/
    └── Maki.java                    # Modelo inmutable con 5 atributos (Java record)
```

| Capa | Archivo | Anotación Clave | Responsabilidad |
| :--- | :--- | :--- | :--- |
| <img src="https://api.iconify.design/lucide/globe.svg?color=%2310b981" width="16" height="16" align="absmiddle" /> **Presentación (REST)** | `MakiController.java` | `@RestController`, `@RequestMapping("/api/makis")` | Recibe las peticiones HTTP (`GET` y `POST`) desde Postman y delega al servicio. |
| <img src="https://api.iconify.design/lucide/cpu.svg?color=%236366f1" width="16" height="16" align="absmiddle" /> **Negocio / Datos** | `MakiService.java` | `@Service` | Administra la lista dinámica `List<Maki>` en memoria, precarga el menú inicial y autogenera el `id`. |
| <img src="https://api.iconify.design/lucide/box.svg?color=%23f59e0b" width="16" height="16" align="absmiddle" /> **Modelo (Dominio)** | `Maki.java` | `public record Maki(...)` | Define la estructura de datos del maki con **5 campos** (`id`, `name`, `price`, `pieces`, `ingredients`). |

---

## <img src="https://api.iconify.design/lucide/database.svg?color=%23f59e0b" width="22" height="22" align="absmiddle" alt="Model" /> Modelo de Datos (`Maki`)

Cada objeto `Maki` procesado por la API cuenta con **5 campos**:

| Campo | Tipo | Descripción | Ejemplo |
| :--- | :--- | :--- | :--- |
| <img src="https://api.iconify.design/lucide/key-round.svg?color=%23eab308" width="15" height="15" align="absmiddle" /> `id` | `Long` | Identificador único autogenerado por el servidor (`1, 2, 3...`). | `1` |
| <img src="https://api.iconify.design/lucide/tag.svg?color=%233b82f6" width="15" height="15" align="absmiddle" /> `name` | `String` | Nombre del corte de maki. | `"Acevichado"` |
| <img src="https://api.iconify.design/lucide/coins.svg?color=%2310b981" width="15" height="15" align="absmiddle" /> `price` | `double` | Precio de la tabla en soles (S/). | `25.0` |
| <img src="https://api.iconify.design/lucide/hash.svg?color=%23a855f7" width="15" height="15" align="absmiddle" /> `pieces` | `int` | Cantidad de piezas que incluye el corte. | `10` |
| <img src="https://api.iconify.design/lucide/list-checks.svg?color=%23ec4899" width="15" height="15" align="absmiddle" /> `ingredients` | `String` | Descripción de los ingredientes principales. | `"Langostino, palta, salsa acevichada"` |

---

## <img src="https://api.iconify.design/lucide/radio.svg?color=%2310b981" width="22" height="22" align="absmiddle" alt="Endpoints" /> Endpoints Disponibles

| Método | Ruta | Código HTTP | Descripción |
| :---: | :--- | :---: | :--- |
| <img src="https://img.shields.io/badge/GET-007EC6?style=flat-square&logo=fastapi&logoColor=white" alt="GET" /> | `/api/makis` | `200 OK` | Devuelve el listado completo (`JSON Array`) de todos los makis registrados. |
| <img src="https://img.shields.io/badge/GET-007EC6?style=flat-square&logo=fastapi&logoColor=white" alt="GET" /> | `/api/makis/{id}` | `200 OK` / `404` | Busca y devuelve un maki específico por su `id`. |
| <img src="https://img.shields.io/badge/POST-49CC90?style=flat-square&logo=fastapi&logoColor=white" alt="POST" /> | `/api/makis` | `201 Created` | Registra un nuevo maki en la lista en memoria y lo devuelve con su `id` asignado. |

---

### <img src="https://api.iconify.design/lucide/arrow-down-circle.svg?color=%23007EC6" width="18" height="18" align="absmiddle" alt="GET" /> Consultar todos los makis (`GET /api/makis`)

* **Request:**
  ```http
  GET http://localhost:8080/api/makis
  Accept: application/json
  ```

* **Response (`200 OK`):**
  ```json
  [
    {
      "id": 1,
      "name": "Acevichado",
      "price": 25.0,
      "pieces": 10,
      "ingredients": "Langostino, palta, salsa acevichada"
    },
    {
      "id": 2,
      "name": "Furai",
      "price": 22.0,
      "pieces": 10,
      "ingredients": "Langostino empanizado, queso crema"
    }
  ]
  ```

---

### <img src="https://api.iconify.design/lucide/search.svg?color=%23007EC6" width="18" height="18" align="absmiddle" alt="GET ID" /> Buscar un maki por ID (`GET /api/makis/{id}`)

* **Request:**
  ```http
  GET http://localhost:8080/api/makis/1
  Accept: application/json
  ```

* **Response (`200 OK`):**
  ```json
  {
    "id": 1,
    "name": "Acevichado",
    "price": 25.0,
    "pieces": 10,
    "ingredients": "Langostino, palta, salsa acevichada"
  }
  ```

---

### <img src="https://api.iconify.design/lucide/plus-circle.svg?color=%2349CC90" width="18" height="18" align="absmiddle" alt="POST" /> Registrar un nuevo maki (`POST /api/makis`)

* **Request:**
  ```http
  POST http://localhost:8080/api/makis
  Content-Type: application/json
  ```
  ```json
  {
    "name": "California",
    "price": 20.5,
    "pieces": 8,
    "ingredients": "Kanikama, palta, pepino"
  }
  ```

* **Response (`201 Created`):**
  ```json
  {
    "id": 3,
    "name": "California",
    "price": 20.5,
    "pieces": 8,
    "ingredients": "Kanikama, palta, pepino"
  }
  ```

---

## <img src="https://api.iconify.design/lucide/terminal.svg?color=%236366f1" width="22" height="22" align="absmiddle" alt="Run" /> Ejecución en GitHub Codespaces y Pruebas en Postman

### Paso 1: Levantar el servidor en Codespaces
Abrimos la terminal integrada en GitHub Codespaces y ejecutamos:

```bash
cd pompompurin
./gradlew bootRun
```

> <img src="https://api.iconify.design/lucide/info.svg?color=%233b82f6" width="16" height="16" align="absmiddle" alt="Info" /> **Importante en Codespaces:** Nos dirigimos a la pestaña **PORTS** (junto a la terminal), hacemos clic derecho sobre el puerto **`8080`** -> **Port Visibility** -> **Public** y copiamos la dirección generada (ej. `https://maki-codespace-8080.app.github.dev`).

### Paso 2: Importar `endpoints.json` en Postman / Insomnia
1. Abrimos **Postman** (o Insomnia) y hacemos clic en el botón **Import**.
2. Seleccionamos el archivo [`endpoints.json`](./endpoints.json) ubicado en la raíz de este repositorio.
3. Si estamos probando desde **GitHub Codespaces**, editamos la variable de colección `baseUrl` en Postman y reemplazamos `http://localhost:8080` por la URL pública de nuestro puerto `8080` (sin barra `/` al final).
4. Ejecutamos primero **1. Listar todos los makis (GET)**, luego **2. Crear maki California (POST)** y nuevamente el **GET** para comprobar cómo se agregó a la lista en tiempo real.
