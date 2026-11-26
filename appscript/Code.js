/**
 * CRUD de Makis en Apps Script.
 * La página (Index.html) llama a estas funciones con google.script.run
 * y estas funciones invocan la API REST de Spring Boot con UrlFetchApp.
 */

const API_URL = 'https://pompompurin.miguelangel.ing/api/makis';

function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index')
    .setTitle('CRUD de Makis')
    .addMetaTag('viewport', 'width=device-width, initial-scale=1');
}

function llamarApi(metodo, ruta, datos) {
  const opciones = {
    method: metodo,
    contentType: 'application/json',
    muteHttpExceptions: true
  };
  if (datos) {
    opciones.payload = JSON.stringify(datos);
  }

  const respuesta = UrlFetchApp.fetch(API_URL + ruta, opciones);
  const codigo = respuesta.getResponseCode();
  if (codigo === 404 && ruta) {
    throw new Error('El maki no existe (404).');
  }
  if (codigo >= 400) {
    throw new Error('El backend respondió con error ' + codigo + '.');
  }

  const texto = respuesta.getContentText();
  return texto ? JSON.parse(texto) : null;
}

function listarMakis() {
  return llamarApi('get', '');
}

function crearMaki(maki) {
  return llamarApi('post', '', maki);
}

function actualizarMaki(id, maki) {
  return llamarApi('put', '/' + id, maki);
}

function eliminarMaki(id) {
  return llamarApi('delete', '/' + id);
}
