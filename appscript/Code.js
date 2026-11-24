/**
 * CRUD de Makis en Apps Script.
 * La página (Index.html) llama a estas funciones con google.script.run
 * y estas funciones invocan la API REST de Spring Boot con UrlFetchApp.
 */

// Muestra la página web
function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index')
    .setTitle('CRUD de Makis')
    .addMetaTag('viewport', 'width=device-width, initial-scale=1');
}

// ---------- URL del backend (se guarda en las propiedades del script) ----------

function obtenerUrlApi() {
  return PropertiesService.getScriptProperties().getProperty('API_URL') || '';
}

function guardarUrlApi(url) {
  url = String(url).trim().replace(/\/+$/, ''); // sin "/" al final
  if (!/^https?:\/\//.test(url)) {
    throw new Error('La URL debe empezar con http:// o https://');
  }
  PropertiesService.getScriptProperties().setProperty('API_URL', url);
  return url;
}

// ---------- Llamada genérica a la API REST ----------

function llamarApi(metodo, ruta, datos) {
  const baseUrl = obtenerUrlApi();
  if (!baseUrl) {
    throw new Error('Primero configura la URL del backend.');
  }

  const opciones = {
    method: metodo,
    contentType: 'application/json',
    headers: { 'ngrok-skip-browser-warning': 'true' }, // solo por si se usa ngrok
    muteHttpExceptions: true // así podemos leer el código de error nosotros
  };
  if (datos) {
    opciones.payload = JSON.stringify(datos);
  }

  const respuesta = UrlFetchApp.fetch(baseUrl + '/api/makis' + ruta, opciones);
  const codigo = respuesta.getResponseCode();
  if (codigo === 404 && ruta) { // 404 al pedir un id concreto (editar/eliminar)
    throw new Error('El maki no existe (404).');
  }
  if (codigo >= 400) {
    throw new Error('El backend respondió con error ' + codigo + '.');
  }

  const texto = respuesta.getContentText();
  return texto ? JSON.parse(texto) : null; // DELETE responde 204 sin cuerpo
}

// ---------- CRUD ----------

function listarMakis() {                 // READ   -> GET    /api/makis
  return llamarApi('get', '');
}

function crearMaki(maki) {               // CREATE -> POST   /api/makis
  return llamarApi('post', '', maki);
}

function actualizarMaki(id, maki) {      // UPDATE -> PUT    /api/makis/{id}
  return llamarApi('put', '/' + id, maki);
}

function eliminarMaki(id) {              // DELETE -> DELETE /api/makis/{id}
  return llamarApi('delete', '/' + id);
}
