/**
 * ============================================================================
 * SERVIDOR DE GOOGLE APPS SCRIPT (en local: appscript/Code.js -> en la nube: Code.gs)
 * ============================================================================
 * ¿Cómo funciona clasp aquí?
 * - El archivo .clasp.json enlaza la carpeta "appscript/" con el proyecto de Google.
 * - "clasp push" sube Code.js (convertido a Code.gs) e Index.html a Apps Script.
 * - "clasp deploy -i <deploymentId>" publica una nueva versión en la URL "/exec".
 *
 * ¿Cómo se conecta el Front con el Backend Spring Boot?
 *   Navegador (Index.html)
 *      │  1. Llama con google.script.run.<funcion>(...) (asíncrono, sin problemas de CORS)
 *      ▼
 *   Servidor Google (Code.js / Code.gs)
 *      │  2. Hace la petición HTTP real con UrlFetchApp.fetch(...) enviando/recibiendo JSON
 *      ▼
 *   Backend Spring Boot (https://pompompurin.miguelangel.ing/api/makis) -> PostgreSQL (Neon)
 * ============================================================================
 */

// URL base del @RequestMapping("/api/makis") en MakiController.java
const API_URL = 'https://pompompurin.miguelangel.ing/api/makis';

/**
 * Punto de entrada de toda Web App en Apps Script.
 * Cuando alguien abre la URL "/exec" en el navegador, Google ejecuta doGet()
 * y entrega el archivo appscript/Index.html como página web.
 */
function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index')
    .setTitle('CRUD de Makis')
    .addMetaTag('viewport', 'width=device-width, initial-scale=1');
}

/**
 * Función central que conecta Apps Script con el backend de Spring Boot.
 * @param {string} metodo - Verbo HTTP ('get', 'post', 'put', 'delete')
 * @param {string} ruta   - Sufijo de la ruta: '' para /api/makis o '/1' para /api/makis/1
 * @param {Object} [datos] - Objeto JS que se serializa a JSON para @RequestBody en POST y PUT
 */
function llamarApi(metodo, ruta, datos) {
  const opciones = {
    method: metodo,
    contentType: 'application/json', // Equivale al header Content-Type: application/json (necesario para @RequestBody)
    muteHttpExceptions: true         // Evita que Apps Script lance un error genérico y nos deja leer el código HTTP real
  };

  // Solo POST y PUT envían cuerpo JSON; GET y DELETE viajan sin payload
  if (datos) {
    opciones.payload = JSON.stringify(datos);
  }

  // Petición HTTP desde los servidores de Google hacia tu backend público
  const respuesta = UrlFetchApp.fetch(API_URL + ruta, opciones);
  const codigo = respuesta.getResponseCode();

  // Manejo de códigos HTTP que devuelve MakiController.java
  if (codigo === 404 && ruta) {
    throw new Error('El maki no existe (404).'); // Dispara .withFailureHandler(mostrarError) en Index.html
  }
  if (codigo >= 400) {
    throw new Error('El backend respondió con error ' + codigo + '.');
  }

  // GET, POST y PUT devuelven JSON (200 / 201); DELETE devuelve 204 No Content (texto vacío)
  const texto = respuesta.getContentText();
  return texto ? JSON.parse(texto) : null;
}

// ---------- Funciones públicas expuestas a google.script.run desde Index.html ----------

function listarMakis() {                 // READ (todos) -> GET    /api/makis       (200 OK + lista JSON)
  return llamarApi('get', '');
}

function buscarMakiPorId(id) {           // READ (uno)   -> GET    /api/makis/{id}  (200 OK + maki o 404 Not Found)
  return llamarApi('get', '/' + encodeURIComponent(id));
}

function crearMaki(maki) {               // CREATE       -> POST   /api/makis       (201 Created + maki con id)
  return llamarApi('post', '', maki);
}

function actualizarMaki(id, maki) {      // UPDATE       -> PUT    /api/makis/{id}  (200 OK o 404 Not Found)
  return llamarApi('put', '/' + id, maki);
}

function eliminarMaki(id) {              // DELETE       -> DELETE /api/makis/{id}  (204 No Content o 404 Not Found)
  return llamarApi('delete', '/' + id);
}
