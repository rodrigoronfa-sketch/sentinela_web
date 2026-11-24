/* =====================================================================
   SENTINELA - Seguridad Digital
   Pequeñas mejoras de interfaz (sin librerías externas)
   ===================================================================== */
(function () {
  "use strict";

  function listo(fn) {
    if (document.readyState === "loading") {
      document.addEventListener("DOMContentLoaded", fn);
    } else {
      fn();
    }
  }

  listo(function () {

    /* 1. Mostrar las credenciales de ejemplo según el rol elegido */
    var demoUser = document.getElementById("demo-user");
    var demoPass = document.getElementById("demo-pass");
    var roles = document.querySelectorAll("input[name='rol']");

    function actualizarDemo() {
      var elegido = null;
      for (var i = 0; i < roles.length; i++) {
        if (roles[i].checked) { elegido = roles[i].value; break; }
      }
      if (!demoUser || !elegido) return;
      if (elegido === "ADMIN") {
        demoUser.textContent = "adm";
        demoPass.textContent = "adm123";
      } else {
        demoUser.textContent = "per";
        demoPass.textContent = "per123";
      }
    }
    for (var i = 0; i < roles.length; i++) {
      roles[i].addEventListener("change", actualizarDemo);
    }
    actualizarDemo();

    /* 2. Búsqueda rápida en tablas: <input data-buscar="id-de-la-tabla"> */
    var buscadores = document.querySelectorAll("input[data-buscar]");
    for (var j = 0; j < buscadores.length; j++) {
      buscadores[j].addEventListener("input", function () {
        var tabla = document.getElementById(this.getAttribute("data-buscar"));
        if (!tabla) return;
        var texto = this.value.toLowerCase();
        var filas = tabla.querySelectorAll("tbody tr");
        for (var k = 0; k < filas.length; k++) {
          filas[k].style.display = filas[k].textContent.toLowerCase().indexOf(texto) > -1 ? "" : "none";
        }
      });
    }

    /* 3. Confirmación antes de eliminar o desactivar */
    var formularios = document.querySelectorAll("form[data-confirm]");
    for (var m = 0; m < formularios.length; m++) {
      formularios[m].addEventListener("submit", function (e) {
        if (!window.confirm(this.getAttribute("data-confirm"))) { e.preventDefault(); }
      });
    }

    /* 4. Cerrar solo avisos informativos; los errores deben permanecer visibles */
    var avisos = document.querySelectorAll(".alerta.ok, .alerta.info");
    for (var n = 0; n < avisos.length; n++) {
      (function (aviso) {
        window.setTimeout(function () {
          aviso.style.transition = "opacity .4s";
          aviso.style.opacity = "0";
          window.setTimeout(function () { if (aviso.parentNode) aviso.parentNode.removeChild(aviso); }, 400);
        }, 7000);
      })(avisos[n]);
    }
  });
})();
