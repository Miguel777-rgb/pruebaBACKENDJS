# Guía Rápida de Cambios por Número de Línea (Backend + Front)

Esta guía toma como referencia las líneas exactas actuales del proyecto:
- **Backend (Spring Boot + JPA + Neon):** `pompompurin/src/main/java/com/maki/pompompurin/...`
- **Frontend (Google Apps Script con `clasp`):** `appscript/Code.js` y `appscript/Index.html` (con búsqueda por ID incluida).

---

## Caso 1: Agregar un nuevo dato editable (Ejemplo: `Integer stock` o `String categoria`)

### Mapa rápido de qué archivos se tocan

| Archivo | ¿Se toca? | Líneas exactas | Motivo |
| :--- | :---: | :--- | :--- |
| `model/Maki.java` | **Sí** | **20**, **26–31**, **46** | Declarar columna JPA, parámetro del constructor y getter/setter |
| `service/MakiService.java` | **Sí** | **39** | Copiar el nuevo campo al actualizar (`PUT`) |
| `PompompurinApplication.java` | **Sí** | **22–23** | Pasar el nuevo dato en los 2 makis de ejemplo |
| `appscript/Index.html` | **Sí** | **48**, **81**, **150**, **155**, **187**, **207** | Input del formulario, columna `<th>` y lectura/envío en JS |
| `controller/MakiController.java` | **No** | — | Recibe y devuelve el objeto `Maki` entero |
| `repository/MakiRepository.java` | **No** | — | `save()` y `findAll()` ya incluyen todas las columnas |
| `appscript/Code.js` | **No** | — | `JSON.stringify(datos)` envía el objeto completo |
| **Neon (PostgreSQL)** | **No** | — | `ddl-auto=update` ejecuta `ALTER TABLE makis ADD COLUMN ...` solo |

> **Regla de oro para Neon:** si el dato nuevo es numérico o booleano y la tabla `makis` en Neon ya tiene filas guardadas, **usa `Integer`, `Double` o `Boolean` (con mayúscula)** en vez de `int`, `double` o `boolean`. Los primitivos intentan crear una columna `NOT NULL` y fallan si ya existen registros previos.

---

### Paso a paso en Backend (Caso 1 — ejemplo con `Integer stock`)

#### 1. `pompompurin/src/main/java/com/maki/pompompurin/model/Maki.java`

- **Línea 20** (debajo de `private String ingredients;`): declara el nuevo atributo.
  ```java
      private int pieces;
      private String ingredients;
      private Integer stock; // <-- NUEVA LÍNEA (o private String categoria;)
  ```

- **Líneas 26–31** (constructor con parámetros; **no toques** el constructor vacío de las líneas 23–24):
  ```java
      public Maki(String name, double price, int pieces, String ingredients, Integer stock) {
          this.name = name;
          this.price = price;
          this.pieces = pieces;
          this.ingredients = ingredients;
          this.stock = stock;
      }
  ```

- **Línea 46** (debajo de `setIngredients`, antes de la llave final `}` de la línea 47): agrega `getter` y `setter`.
  ```java
      public Integer getStock() { return stock; }
      public void setStock(Integer stock) { this.stock = stock; }
  ```

#### 2. `pompompurin/src/main/java/com/maki/pompompurin/service/MakiService.java`

- **Línea 39** (dentro de `update()`, justo debajo de `maki.setIngredients(datos.getIngredients());`):
  ```java
              maki.setPieces(datos.getPieces());
              maki.setIngredients(datos.getIngredients());
              maki.setStock(datos.getStock()); // <-- NUEVA LÍNEA
              return makiRepository.save(maki);
  ```
  *(El método `create()` de las líneas 27–31 **no se toca**, porque `makiRepository.save(maki)` guarda todos los atributos automáticamente).*

#### 3. `pompompurin/src/main/java/com/maki/pompompurin/PompompurinApplication.java`

- **Líneas 22–23** (en `cargarDatosIniciales`, agrega el argumento final a los 2 `new Maki(...)`):
  ```java
  makiRepository.save(new Maki("Acevichado", 25.0, 10, "Langostino, palta, salsa acevichada", 20));
  makiRepository.save(new Maki("Furai", 22.0, 10, "Langostino empanizado, queso crema", 15));
  ```

---

### Paso a paso en Frontend (Caso 1 — solo `appscript/Index.html`, 6 puntos)

#### 4. `appscript/Index.html`

1. **Línea 48** (dentro de `<div class="fila">`, antes del `</div>` de la línea 49) — agrega el input con `id` **idéntico** al nombre del campo en `Maki.java`:
   ```html
           <div><label for="pieces">Piezas</label><input id="pieces" type="number" step="1" min="1" required></div>
           <div><label for="stock">Stock</label><input id="stock" type="number" step="1" min="0" required></div>
   ```
   *(Si fuera texto libre: `<input id="categoria" required>`; si fuera desplegable: `<select id="categoria" required><option>Clásico</option></select>`).*

2. **Línea 81** (en `<thead>`) — agrega el encabezado `<th>` antes de `<th>Acciones</th>`:
   ```html
           <tr><th>ID</th><th>Nombre</th><th>Precio (S/)</th><th>Piezas</th><th>Ingredientes</th><th>Stock</th><th>Acciones</th></tr>
   ```

3. **Línea 150** (en `pintarTabla()`, fila vacía) — sube `colspan` de `6` a `7`:
   ```javascript
           tbody.innerHTML = '<tr><td colspan="7">No hay makis registrados.</td></tr>';
   ```

4. **Línea 155** (en `pintarTabla()`, arreglo de valores de cada fila) — agrega `maki.stock` al final:
   ```javascript
           [maki.id, maki.name, maki.price.toFixed(2), maki.pieces, maki.ingredients, maki.stock].forEach(valor => {
   ```

5. **Línea 187** (en `guardar()`, dentro de `const maki = { ... }`) — pon coma al final de `ingredients` y agrega la propiedad:
   ```javascript
         const maki = {
           name: campo('name').value,
           price: Number(campo('price').value),
           pieces: Number(campo('pieces').value),
           ingredients: campo('ingredients').value,
           stock: Number(campo('stock').value) // Si fuera String: campo('categoria').value
         };
   ```

6. **Línea 207** (en `editar()`, arreglo de campos que rellenan el formulario) — agrega `'stock'`:
   ```javascript
         ['id', 'name', 'price', 'pieces', 'ingredients', 'stock'].forEach(nombre => campo(nombre).value = maki[nombre]);
   ```

---

## Caso 1-B: Agregar `fechaCreacion` automática (Forma A — el servidor la genera, el usuario NO la escribe)

Cuando el dato lo genera el servidor solo al crear (como una fecha de creación), **no agregas ningún `<input>` en el formulario del Front**: solo se guarda en el Backend y se muestra como columna en la tabla.

- Si quieres **solo fecha** (`"2026-11-26"`): usa `LocalDate` y `LocalDate.now()`.
- Si quieres **fecha y hora** (`"2026-11-26T18:30:00"`): usa `LocalDateTime` y `LocalDateTime.now()`.

### ¿Por qué se llamaba 2 veces a `.now()` (y cómo evitarlo)?

1. **Por qué apareció 2 veces en la explicación previa:**
   - **Llamada 1 en `Maki.java` (`= LocalDate.now()`):** les asignaba fecha a los 2 makis iniciales de `PompompurinApplication.java`, ya que ahí se llama directo a `makiRepository.save(new Maki(...))` **sin pasar por `MakiService.create()`** (así evitabas tocar el constructor y `PompompurinApplication.java`).
   - **Llamada 2 en `MakiService.create()` (`maki.setFechaCreacion(LocalDate.now())`):** protegía el `POST` contra clientes externos. Si alguien en Postman envía `"fechaCreacion": null` o `"fechaCreacion": "1999-01-01"` en el JSON, Jackson llama a `setFechaCreacion(...)` y pisa el valor inicial de `Maki.java`. Ponerlo en `create()` (igual que `maki.setId(null)`) obliga a usar siempre la fecha real del servidor.
2. **Cómo escribirlo 1 sola vez con `@PrePersist` (recomendado):**
   - Poniendo `@PrePersist` sobre un método en `Maki.java`, JPA lo ejecuta automáticamente **justo antes de cualquier `INSERT` en Neon** (tanto en `PompompurinApplication` como en cualquier `POST`), así solo llamas a `.now()` **una única vez** y ni siquiera tocas `MakiService.java`.

### Líneas exactas a tocar (Forma A)

| Archivo | ¿Se toca? | Líneas exactas | Motivo |
| :--- | :---: | :--- | :--- |
| `model/Maki.java` | **Sí** | **3**, **20**, **46** | Atributo `LocalDate` + `@PrePersist` + getter/setter |
| `appscript/Index.html` | **Sí** | **81**, **150**, **155** | Solo las **3 líneas de la tabla** (sin tocar formulario, `guardar()` ni `editar()`) |
| `MakiService`, `PompompurinApplication`, `Controller`, `Repository`, `Code.js` | **No** | — | No requieren ningún cambio |

#### 1. `pompompurin/src/main/java/com/maki/pompompurin/model/Maki.java`

- **Línea 3** (imports):
  ```java
  import java.time.LocalDate; // (o java.time.LocalDateTime)
  import jakarta.persistence.PrePersist;
  ```
- **Línea 20** (debajo de `private String ingredients;`):
  ```java
      private LocalDate fechaCreacion;
  ```
- **Línea 46** (antes de la llave final `}` de la línea 47):
  ```java
      @PrePersist // Se ejecuta automáticamente 1 sola vez antes de cada INSERT en Neon (nunca en UPDATE)
      public void asignarFechaCreacion() {
          this.fechaCreacion = LocalDate.now();
      }

      public LocalDate getFechaCreacion() { return fechaCreacion; }
      public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
  ```

#### 2. `appscript/Index.html` (solo 3 puntos en la tabla)

1. **Línea 81 (`<thead>`):** agrega `<th>Creación</th>` antes de `<th>Acciones</th>`:
   ```html
           <tr><th>ID</th><th>Nombre</th><th>Precio (S/)</th><th>Piezas</th><th>Ingredientes</th><th>Creación</th><th>Acciones</th></tr>
   ```
2. **Línea 150 (`pintarTabla()`, fila vacía):** sube `colspan` de `6` a `7`:
   ```javascript
           tbody.innerHTML = '<tr><td colspan="7">No hay makis registrados.</td></tr>';
   ```
3. **Línea 155 (`pintarTabla()`, arreglo de celdas):** agrega `maki.fechaCreacion || '-'` (`'-'` por si hay filas antiguas en Neon con `null`):
   ```javascript
           [maki.id, maki.name, maki.price.toFixed(2), maki.pieces, maki.ingredients, maki.fechaCreacion || '-'].forEach(valor => {
   ```

---

## Caso 2: Nueva búsqueda en base a otro dato (Ejemplo: buscar por `name` en vez de `id`)

Actualmente tu proyecto ya tiene búsqueda por `id` (`GET /api/makis/{id}`). Con **Spring Data JPA**, para buscar por otro dato declaras el método en `MakiRepository` y Spring construye el SQL solo.

### Mapa rápido de qué archivos se tocan

| Archivo | ¿Se toca? | Líneas exactas | Motivo |
| :--- | :---: | :--- | :--- |
| `repository/MakiRepository.java` | **Sí** | **3**, **7** | Declarar el Query Method de JPA |
| `service/MakiService.java` | **Sí** | **25** | Método puente que llama al repositorio |
| `controller/MakiController.java` | **Sí** | **39** | Nuevo `@GetMapping` con prefijo `/nombre/{name}` para no chocar con `/{id}` |
| `appscript/Code.js` | **Sí** | **78** | Agregar la función que llama a `/nombre/{name}` |
| `appscript/Index.html` | **Sí** | **61–75** y **119–136** | Adaptar (o duplicar) el bloque de búsqueda que ya tienes |
| `model/Maki.java` | **No** | — | Si buscas por un atributo existente (`name`, `pieces`, `ingredients`), no cambia |

---

### Paso a paso en Backend (Caso 2 — ejemplo buscando por `name`)

#### 1. `pompompurin/src/main/java/com/maki/pompompurin/repository/MakiRepository.java`

- **Línea 3** (imports): importa `List` (o `Optional`).
- **Línea 7** (dentro de `public interface MakiRepository`, antes del `}` de la línea 8): declara el método según el tipo de dato:

  ```java
  package com.maki.pompompurin.repository;

  import com.maki.pompompurin.model.Maki;
  import java.util.List;
  import org.springframework.data.jpa.repository.JpaRepository;

  public interface MakiRepository extends JpaRepository<Maki, Long> {
      // Busca makis cuyo nombre contenga el texto (sin importar mayúsculas/minúsculas)
      List<Maki> findByNameContainingIgnoreCase(String name);
  }
  ```

  **Tabla rápida de nombres de método en JPA según lo que te pidan:**
  - Texto parcial (`name` o `ingredients`): `List<Maki> findByNameContainingIgnoreCase(String name);`
  - Texto exacto (1 único maki): `Optional<Maki> findByNameIgnoreCase(String name);` *(importar `java.util.Optional`)*
  - Número exacto (`pieces`): `List<Maki> findByPieces(int pieces);`
  - Precio menor o igual (`price`): `List<Maki> findByPriceLessThanEqual(double price);`

#### 2. `pompompurin/src/main/java/com/maki/pompompurin/service/MakiService.java`

- **Línea 25** (justo debajo de `findById(Long id)`): agrega el método del servicio.
  ```java
      public List<Maki> findByName(String name) {
          return makiRepository.findByNameContainingIgnoreCase(name);
      }
  ```
  *(Si usaste `Optional<Maki> findByNameIgnoreCase`, el retorno aquí también es `Optional<Maki>`).*

#### 3. `pompompurin/src/main/java/com/maki/pompompurin/controller/MakiController.java`

- **Línea 39** (debajo de `findById`, que ocupa las líneas 36–39): agrega el `@GetMapping` **usando un prefijo como `/nombre/{name}`**.
  > **Importante:** como la línea 36 ya usa `@GetMapping("/{id}")`, no pongas `@GetMapping("/{name}")` a secas si mantienes ambos, porque chocarían en Spring.

  - **Opción A — Devolver lista de coincidencias (ideal para buscador del Front):**
    ```java
        // READ - GET http://localhost:8081/api/makis/nombre/{name} -> 200 OK (lista filtrada)
        @GetMapping("/nombre/{name}")
        public List<Maki> findByName(@PathVariable("name") String name) {
            return makiService.findByName(name);
        }
    ```

  - **Opción B — Devolver 1 solo maki exacto (200 OK o 404 Not Found, igual que `findById`):**
    ```java
        @GetMapping("/nombre/{name}")
        public ResponseEntity<Maki> findByName(@PathVariable("name") String name) {
            return ResponseEntity.of(makiService.findByName(name));
        }
    ```

---

### Paso a paso en Frontend (Caso 2 — adaptando el buscador existente en `Code.js` + `Index.html`)

#### 4. `appscript/Code.js` (`Code.gs` en Apps Script)

- **Línea 78** (debajo de `buscarMakiPorId(id)` que está en las líneas 76–78, o reemplazándola):
  ```javascript
  function buscarMakisPorNombre(nombre) {  // READ -> GET /api/makis/nombre/{name}
    return llamarApi('get', '/nombre/' + encodeURIComponent(nombre));
  }
  ```

#### 5. `appscript/Index.html`

Como el buscador por ID ya está en las **líneas 61–75** (HTML) y **119–136** (JS), si te piden cambiarlo para buscar por `name` solo ajustas esos dos bloques:

1. **Líneas 61–68** (en el `<form>` de búsqueda) — cambias `buscarPorId` → `buscarPorNombre` y quitas `type="number"`:
   ```html
     <form onsubmit="buscarPorNombre(event)">
       <fieldset>
         <legend>Buscar maki por nombre</legend>
         <div class="fila">
           <div>
             <label for="buscarId">Nombre del maki</label>
             <input id="buscarId" placeholder="Ej. Acevichado">
           </div>
   ```

2. **Líneas 119–136** (en el `<script>`) — cambias la función para llamar a `buscarMakisPorNombre`:
   ```javascript
       function buscarPorNombre(evento) {
         evento.preventDefault();
         const texto = campo('buscarId').value.trim();
         if (!texto) {
           verTodos();
           return;
         }
         google.script.run
           .withSuccessHandler(makis => {
             mostrarMensaje('Resultados encontrados: ' + makis.length);
             pintarTabla(makis); // Si en el Controller usaste Opción B (1 solo maki), deja: pintarTabla([maki])
           })
           .withFailureHandler(error => {
             pintarTabla([]);
             mostrarError(error);
           })
           .buscarMakisPorNombre(texto);
       }
   ```

---

## Recordatorio final para publicar los cambios

1. **Backend:** subir cambios y reconstruir el contenedor (`docker compose up -d --build` o redeploy en el servidor de `pompompurin.miguelangel.ing`).
2. **Frontend (Apps Script):**
   ```powershell
   clasp.cmd push
   clasp.cmd deploy -i AKfycbwmGUFrELGEx2fmxVQgOqU3iwMGBKvRnWUbt4ngJbW8IpdZHq6arVHUHBs85_eOxn9s -d "Nueva version"
   ```
