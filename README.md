<div align="center">

<h1>
  <img src="https://api.iconify.design/lucide/utensils-crossed.svg?color=%23f59e0b" width="32" height="32" align="absmiddle" alt="Maki Icon" />
  Pompompurin — Maki REST API + CRUD en Apps Script
</h1>

<p>
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.1.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Neon-PostgreSQL-00E599?style=for-the-badge&logo=postgresql&logoColor=white" alt="Neon PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-Backend-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Apps_Script-CRUD-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Apps Script" />
  <img src="https://img.shields.io/badge/Postman-Collection-FF6C37?style=for-the-badge&logo=postman&logoColor=white" alt="Postman" />
</p>

<p>
  API REST desarrollada con <b>Spring Boot</b> en capas (<code>Controller -> Service -> Repository -> Model</code>) que guarda los makis en una base de datos <b>PostgreSQL en la nube (Neon)</b> y se puede ejecutar en un contenedor <b>Docker</b>, y una <b>página web en Google Apps Script</b> que hace el CRUD completo (Crear, Leer, Actualizar, Eliminar) llamando a esa API.
</p>

</div>

---

## <img src="https://api.iconify.design/lucide/layers.svg?color=%233b82f6" width="22" height="22" align="absmiddle" alt="Architecture" /> Arquitectura

```text
Página Apps Script (Index.html)
   │ google.script.run
   ▼
Code.js (Apps Script) ── UrlFetchApp ──►  Spring Boot /api/makis  ──►  PostgreSQL (Neon)
```

```text
pruebaBACKENDJS/
├── .clasp.json                          # Enlace con el proyecto de Apps Script (clasp)
├── appscript/                           # Código que se sube a Apps Script con "clasp push"
│   ├── appsscript.json                  # Manifiesto (zona horaria, configuración de la app web)
│   ├── Code.js                          # Servidor: doGet() + funciones CRUD que llaman a la API REST
│   └── Index.html                       # Página: formulario + tabla de makis
└── pompompurin/                         # Backend Spring Boot
    ├── Dockerfile                       # Imagen Docker del backend (compila y ejecuta el .jar)
    ├── docker-compose.yml               # Levanta el backend en Docker con los datos de .env
    ├── .env.example                     # Plantilla de la conexión a Neon (copiar como .env)
    └── src/main/java/com/maki/pompompurin/
        ├── PompompurinApplication.java  # Punto de entrada + carga de 2 makis de ejemplo
        ├── controller/MakiController.java   # Endpoints GET, POST, PUT, DELETE (@RestController)
        ├── service/MakiService.java         # Lógica de negocio (@Service)
        ├── repository/MakiRepository.java   # Acceso a la BD (JpaRepository)
        └── model/Maki.java                  # Entidad JPA -> tabla "makis" (@Entity)
```

| Capa | Archivo | Responsabilidad |
| :--- | :--- | :--- |
| <img src="https://api.iconify.design/lucide/layout.svg?color=%234285F4" width="16" height="16" align="absmiddle" /> **Página web** | `appscript/Index.html` | Formulario para crear/editar y tabla con botones **Editar** y **Eliminar**. |
| <img src="https://api.iconify.design/lucide/send.svg?color=%234285F4" width="16" height="16" align="absmiddle" /> **Apps Script** | `appscript/Code.js` | Recibe las llamadas de la página y consume la API REST con `UrlFetchApp`. |
| <img src="https://api.iconify.design/lucide/globe.svg?color=%2310b981" width="16" height="16" align="absmiddle" /> **Controller** | `MakiController.java` | Expone los endpoints HTTP y delega al servicio. |
| <img src="https://api.iconify.design/lucide/cpu.svg?color=%236366f1" width="16" height="16" align="absmiddle" /> **Service** | `MakiService.java` | Crea, lista, actualiza y elimina makis usando el repositorio. |
| <img src="https://api.iconify.design/lucide/database.svg?color=%23f59e0b" width="16" height="16" align="absmiddle" /> **Repository** | `MakiRepository.java` | `JpaRepository<Maki, Long>`: Spring genera el SQL automáticamente. |
| <img src="https://api.iconify.design/lucide/box.svg?color=%23f59e0b" width="16" height="16" align="absmiddle" /> **Model** | `Maki.java` | Entidad JPA mapeada a la tabla `makis`. |

---

## <img src="https://api.iconify.design/lucide/database.svg?color=%23f59e0b" width="22" height="22" align="absmiddle" alt="Model" /> Entidad / Tabla `makis`

| Campo | Tipo | Descripción | Ejemplo |
| :--- | :--- | :--- | :--- |
| <img src="https://api.iconify.design/lucide/key-round.svg?color=%23eab308" width="15" height="15" align="absmiddle" /> `id` | `Long` | Clave primaria autogenerada por la base de datos. | `1` |
| <img src="https://api.iconify.design/lucide/tag.svg?color=%233b82f6" width="15" height="15" align="absmiddle" /> `name` | `String` | Nombre del maki. | `"Acevichado"` |
| <img src="https://api.iconify.design/lucide/coins.svg?color=%2310b981" width="15" height="15" align="absmiddle" /> `price` | `double` | Precio en soles (S/). | `25.0` |
| <img src="https://api.iconify.design/lucide/hash.svg?color=%23a855f7" width="15" height="15" align="absmiddle" /> `pieces` | `int` | Cantidad de piezas. | `10` |
| <img src="https://api.iconify.design/lucide/list-checks.svg?color=%23ec4899" width="15" height="15" align="absmiddle" /> `ingredients` | `String` | Ingredientes principales. | `"Langostino, palta, salsa acevichada"` |
| <img src="https://api.iconify.design/lucide/ruler.svg?color=%2314b8a6" width="15" height="15" align="absmiddle" /> `tamanio` | `String` | Tamaño: `Pequeño`, `Mediano` o `Grande`. | `"Grande"` |

---

## <img src="https://api.iconify.design/lucide/radio.svg?color=%2310b981" width="22" height="22" align="absmiddle" alt="Endpoints" /> Endpoints (CRUD)

| Operación | Método | Ruta | Respuesta |
| :--- | :---: | :--- | :--- |
| **Read** (todos) | `GET` | `/api/makis` | `200 OK` con la lista |
| **Read** (uno) | `GET` | `/api/makis/{id}` | `200 OK` o `404 Not Found` |
| **Create** | `POST` | `/api/makis` | `201 Created` con el maki y su `id` |
| **Update** | `PUT` | `/api/makis/{id}` | `200 OK` con el maki actualizado o `404` |
| **Delete** | `DELETE` | `/api/makis/{id}` | `204 No Content` o `404` |

Ejemplo de cuerpo JSON para `POST` y `PUT`:

```json
{
  "name": "California",
  "price": 20.5,
  "pieces": 8,
  "ingredients": "Kanikama, palta, pepino",
  "tamanio": "Mediano"
}
```

---

## <img src="https://api.iconify.design/lucide/terminal.svg?color=%236366f1" width="22" height="22" align="absmiddle" alt="Run" /> Cómo ejecutarlo

### Paso 1: Crear la base de datos en Neon y el archivo `.env`

1. En [console.neon.tech](https://console.neon.tech) crear un proyecto y pulsar **Connect**. Neon muestra algo así:
   `postgresql://USUARIO:CLAVE@HOST/neondb?sslmode=require`
2. En la carpeta `pompompurin`, copiar `.env.example` como `.env` y pasar esos datos (sin comillas):

   ```properties
   DB_URL=jdbc:postgresql://HOST/neondb?sslmode=require
   DB_USERNAME=USUARIO
   DB_PASSWORD=CLAVE
   ```

El `.env` tiene la clave, por eso está en `.gitignore` y **no se sube a GitHub**. La tabla `makis` no hay que crearla: Hibernate la crea al iniciar.

### Paso 2: Levantar el backend (elige una opción)

**Opción A — con Gradle:**

```bash
cd pompompurin
./gradlew bootRun
```

**Opción B — con Docker** (no necesita Java instalado):

```bash
cd pompompurin
docker compose up -d --build     # construye la imagen con el Dockerfile y la ejecuta
docker compose logs -f backend   # ver los logs (Ctrl+C para salir)
docker compose down              # apagarlo
```

En ambos casos, al iniciar, Hibernate crea la tabla `makis` en Neon y, si está vacía, inserta 2 makis de ejemplo. La API queda en `http://localhost:8081/api/makis`.

> Si al arrancar aparece `'url' must start with "jdbc"`, no se encontró el `.env`: debe estar en `pompompurin/` y la app debe ejecutarse desde esa carpeta (`cd pompompurin` y luego `./gradlew bootRun`).
> Los tests (`./gradlew test`) usan una base H2 en memoria, por lo que no necesitan Neon.

### Paso 3: Hacer pública la API

Apps Script se ejecuta en los servidores de Google, por eso **no puede llamar a `localhost`**: la API necesita una URL pública.

* **GitHub Codespaces:** pestaña **PORTS** -> clic derecho en el puerto `8081` -> **Port Visibility** -> **Public** y copiar la URL (ej. `https://maki-codespace-8081.app.github.dev`).
* **En tu PC:** usar un túnel, por ejemplo `ngrok http 8081`, y copiar la URL `https://....ngrok-free.app` que muestra.

### Paso 4: Subir y abrir la página de Apps Script

```bash
clasp push --force  # desde la raíz del repositorio (usa .clasp.json); --force sube también el manifiesto
```

1. En el editor de Apps Script: **Implementar** -> **Nueva implementación** -> tipo **Aplicación web** -> **Implementar** (la primera vez pide autorizar el acceso a servicios externos).
2. Abrir la URL de la aplicación web.
3. En **Configuración**, pegar la URL pública del Paso 3 (sin `/api/makis` ni `/` al final) y pulsar **Guardar URL**. Se guarda en las *propiedades del script*, así que solo se hace una vez.
4. Usar el CRUD: **Crear** con el formulario, ver la lista en la tabla (**Leer**), **Editar** para actualizar y **Eliminar** para borrar.

### Probar con Postman (opcional)

Importar [`endpoints.json`](./endpoints.json) en Postman y, si hace falta, cambiar la variable `baseUrl` (por defecto `http://localhost:8081`). Incluye todos los endpoints del CRUD.
