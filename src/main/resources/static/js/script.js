//======================================== efecto escritura ====================================
const by = (selector) => document.querySelector(selector);
const $escribirTexto = by(".escritura-texto");
const $cursor = by(".cursor");

const palabras = ["Desarrollador Back End", "Desarrollador Front End", "Desarrollador Full Stack"];
const delay = {
    typing: 150,
    keeping: 600,
    erasing: 100,
    word: 2000,
};

const sleep = (ms) => {
    return new Promise((resolve) => {
        setTimeout(() => resolve(), ms);
    });
};

const tipo = async (word, isLastWord) => {
    $cursor.classList.add("tipeado");

    for (const char of word) {
        $escribirTexto.textContent += char;
        await sleep(delay.typing);
    }

    $cursor.classList.remove("tipeado");

    if (!isLastWord) {
        await sleep(delay.keeping);

        for (let i = 1; i <= word.length; i++) {
            $escribirTexto.textContent = word.substring(0, word.length - i);
            await sleep(delay.erasing);
        }
    }
}

const loop = async (wordIndex = 0) => {
    const currentWord = palabras[wordIndex % palabras.length];
    const isLastWord = wordIndex === palabras.length - 1;

    await tipo(currentWord, isLastWord);

    if (!isLastWord) {
        setTimeout(async () => {
            await loop(wordIndex + 1);
        }, delay.word);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    loop();
});




//================================== reproducir audio - modo claro/oscuro =======================
// Obtener elementos del DOM
const reproductorAudio = document.getElementById('reproductor');
const iconoMusica = document.getElementById('icono-musica');
const controlMusica = document.querySelector('.control-musica');
const modoClaro = document.getElementById('icono-modo-claro');
const body = document.body;

// Variable para almacenar el estado actual del audio (activado o desactivado)
let audioActivado = true;

// Función para alternar la reproducción de audio
function toggleAudio() {
    if (reproductorAudio.paused) {
        reproductorAudio.play();
        iconoMusica.classList.add('fa-volume-up');
        iconoMusica.classList.remove('fa-volume-off');
        controlMusica.dataset.text = 'Musica: On';
        audioActivado = true;
    } else {
        reproductorAudio.pause();
        iconoMusica.classList.add('fa-volume-off');
        iconoMusica.classList.remove('fa-volume-up');
        controlMusica.dataset.text = 'Musica: Off';
        audioActivado = false;
    }
}

// Función para iniciar la reproducción cuando la página es visible
function iniciarReproduccion() {
    if (audioActivado && reproductorAudio.paused) {
        reproductorAudio.play();
    }
}

// Función para detener la reproducción cuando la página no es visible
function detenerReproduccion() {
    if (!reproductorAudio.paused) {
        reproductorAudio.pause();
    }
}

// Evento cuando la página se vuelve visible
document.addEventListener('visibilitychange', function () {
    if (document.visibilityState === 'visible') {
        iniciarReproduccion();
    } else {
        detenerReproduccion();
    }
});


// Función para alternar entre el modo oscuro y el modo claro
function toggleModoClaro() {
    // Obtener el span del modo
    const controlModoClaro = document.querySelector('.control-modo-claro');

    const circuloImagenAvatar = document.querySelector('.circulo-fondo');

    // Cambiar el icono entre luna y sol
    if (body.classList.contains('modo-claro')) {
        modoClaro.classList.remove('fa-sun');
        modoClaro.classList.add('fa-moon');
        controlModoClaro.dataset.text = 'Modo Oscuro';
        circuloImagenAvatar.style.background = '#fff';
    } else {
        modoClaro.classList.remove('fa-moon');
        modoClaro.classList.add('fa-sun');
        controlModoClaro.dataset.text = 'Modo Claro';
        circuloImagenAvatar.style.background = '#333';
    }

    // Agregar o remueve la clase 'dark-mode' en el body para cambiar el modo
    body.classList.toggle('modo-claro');
}

// Agregar event listeners para alternar la reproducción de audio y el modo oscuro/claro
iconoMusica.addEventListener('click', toggleAudio);
modoClaro.addEventListener('click', toggleModoClaro);

/*================================== Slider Proyectos ==================================*/

document.addEventListener("DOMContentLoaded", function () {
    const sliderProyectos = document.querySelector(".slider-proyectos");
    const tarjetasProyectos = document.querySelectorAll(".contenedor-tarjeta-proyectos");
    const indexTarjetas = document.querySelectorAll(".index");
    let index = 0;

    function cambiarTarjeta(nuevaIndex) {
        index = nuevaIndex;
        actualizarSlider();
    }

    function actualizarSlider() {
        const translateValue = -index * 100 + "%";
        sliderProyectos.style.transform = "translateX(" + translateValue + ")";

        // Actualizar los estilos de los índices
        indexTarjetas.forEach((indice, i) => {
            if (i === index) {
                indice.classList.add('indice-activo');
                indice.classList.remove('indice-inactivo');
            } else {
                indice.classList.add('indice-inactivo');
                indice.classList.remove('indice-activo');
            }
        });
    }

    // Ajusta el evento de clic para los botones
    document.getElementById("anterior").addEventListener("click", function () {
        cambiarTarjeta((index - 1 + tarjetasProyectos.length) % tarjetasProyectos.length);
    });

    document.getElementById("siguiente").addEventListener("click", function () {
        cambiarTarjeta((index + 1) % tarjetasProyectos.length);
    });

    // Ajusta el evento de clic para los índices
    indexTarjetas.forEach((indice, i) => {
        indice.addEventListener("click", function () {
            cambiarTarjeta(i);
        });
    });
});


//================================== Mostrar informacion detallada de proyecto ==================================
document.addEventListener('DOMContentLoaded', function () {
    const iconosInfo = document.querySelectorAll('.img-icono-info');


    iconosInfo.forEach(function (icono) {
        icono.addEventListener('click', function () {
            const tarjeta = icono.closest('.contenedor-tarjeta-proyectos');
            const contenedorDetalleProyecto = document.getElementById('contenedor-detalle-proyecto-oculto');

            if (tarjeta) {
                const tarjetaId = tarjeta.id;
                const cajaDetalleId = `detalle-${tarjetaId}`;
                const cajaDetalle = document.getElementById(cajaDetalleId);

                if (cajaDetalle) {
                    cajaDetalle.style.display = 'block';
                    contenedorDetalleProyecto.style.display = 'flex';
                } else {
                    console.error(`No se encontró la caja de detalles con ID: ${cajaDetalleId}`);
                }
            } else {
                console.error('No se pudo encontrar la tarjeta asociada al ícono de información');
            }
        });
    });
});

//===================================== Cerrar informacion detallada de proyecto ==================================
document.addEventListener('DOMContentLoaded', function () {
    const iconosCerrar = document.querySelectorAll('.contenedor-icono-cerrar');

    iconosCerrar.forEach(function (iconoCerrar) {
        iconoCerrar.addEventListener('click', function () {
            // Encuentra la caja de detalle correspondiente al icono de cerrar
            const cajaDetalle = iconoCerrar.closest('.caja-detalle-proyecto');

            // Encuentra el contenedor de detalles oculto correspondiente
            const contenedorOculto = document.getElementById('contenedor-detalle-proyecto-oculto');

            // Oculta la caja de detalle
            if (cajaDetalle) {
                cajaDetalle.style.display = 'none';
            }

            // Oculta el contenedor de detalles oculto
            if (contenedorOculto) {
                contenedorOculto.style.display = 'none';
            }
        });
    });
});

//================================== Indicador pagina ==================================
function mostrarSeccion(id, elementoSeleccionado) {
    // Ocultar todas las secciones
    const secciones = document.querySelectorAll('.seccion');
    secciones.forEach(seccion => seccion.classList.remove('activa'));

    // Mostrar la sección seleccionada
    const seccionSeleccionada = document.getElementById(id);
    seccionSeleccionada.classList.add('activa');

    // Remover la clase 'index-activo' de todos los elementos de la barra de navegación
    const elementosNavegacion = document.querySelectorAll('.contenedor-icono');
    elementosNavegacion.forEach(elemento => {
        // Elimina el div existente si existe
        const indexActivoExistente = elemento.querySelector('.index-activo');
        if (indexActivoExistente) {
            indexActivoExistente.remove();
        }
    });

    // Agregar el div 'index-activo' como hermano del elemento seleccionado
    const indexActivo = document.createElement('div');
    indexActivo.classList.add('index-activo');
    elementoSeleccionado.parentNode.insertBefore(indexActivo, elementoSeleccionado.nextSibling);
}

/*================= Simulacion de click en la tarjeta de presentacion para la seccion de contacto =========================*/
function simularClicBarraNavegacion(id) {
    // Simula un clic en el enlace de la barra de navegación correspondiente
    const enlaceBarraNavegacion = document.getElementById(id);

    // Elimina la clase 'index-activo' de todos los elementos de la barra de navegación
    const elementosNavegacion = document.querySelectorAll('.contenedor-icono');
    elementosNavegacion.forEach(elemento => {
        elemento.querySelector('.index-activo')?.remove();
    });

    // Agrega el div 'index-activo' como hermano del enlace de la barra de navegación correspondiente
    const indexActivo = document.createElement('div');
    indexActivo.classList.add('index-activo');
    enlaceBarraNavegacion.appendChild(indexActivo);

    // Simula un clic en el enlace
    enlaceBarraNavegacion.querySelector('a').click();
}

//======================================= Boton Chat ==================================================
let clicks = 1; // Variable para controlar el estado de la ventana de chat (1: visible, 2: oculta)

// Función para cambiar la visibilidad de la ventana de chat
function clickear() {
  let chat = document.getElementById("chat");
  let container = document.getElementById("contenedor-chatbot");
  let icono = document.getElementById("icono-mensaje-chat");
  let footer = document.getElementById("footer")
  let body = document.getElementById("body")
  if (clicks === 1) {
    // Hace visible el chat
    chat.style.opacity = "1";
    footer.style.display = "none";
    icono.src = "/images/icons/png/cerrar.png"
    icono.style.width = "33px"

    container.classList.add("animado");
    body.style.overflow = "auto"

    clicks = 2;
  } else if (clicks === 2) {
    // Oculta el chat
    footer.style.display = "flex";
    icono.src = "/images/icons/png/mensaje.png"

    container.classList.remove("animado");
    body.style.overflow = "auto"

    clicks = 1;
  }
}



/*=====================================================================================================
   Ajustar la altura del textarea automáticamente:
   - Se guarda la altura inicial del textarea.
   - Se añade un evento de escucha para detectar cambios en el input del textarea.
   - La altura del textarea se ajusta para mantener la altura original y permitir el desplazamiento vertical según el contenido.
*/
let cajaTexto = document.getElementById("cajaTexto");

cajaTexto.addEventListener("input", function() {
  // Verificar si el texto tiene más de una línea
  let lineas = this.value.split('\n');

  // Si el texto tiene más de una línea, ajustar la altura en función del contenido
  if (lineas.length > 1) {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
  } else {
    this.style.height = 'auto';
  }
});
/*
   Habilitar el ícono de enviar cuando se ingresa texto:
   - Se obtiene el botón de enviar mediante su identificador.
   - Se añade un evento de escucha para detectar cambios en el input del textarea.
   - Se verifica si el contenido del textarea no está vacío y ajusta la visibilidad del botón de enviar en consecuencia.
*/
const btnEnviar = document.getElementById("btnEnviar");

cajaTexto.addEventListener("input", function () {
  if (cajaTexto.value.trim() !== "") {
    btnEnviar.style.visibility = "visible";
  } else {
    btnEnviar.style.visibility = "hidden";
  }
});

//=====================================================================================================
/*
   Función para capitalizar la primera letra del texto ingresado en un input:
   - Obtiene el elemento de input mediante su identificador ("cajaTexto").
   - Obtiene el valor actual del input.
   - Verifica si el texto tiene una longitud mayor que cero.
   - Si el texto no está vacío, capitaliza la primera letra y actualiza el valor del input con la versión capitalizada.
*/

function capitalizarPrimeraLetra() {
  // Obtén el elemento de input mediante su identificador
  let input = document.getElementById("cajaTexto");
  // Obtiene el valor actual del input
  let texto = input.value;

  // Verifica si el texto tiene una longitud mayor que cero
  if (texto.length > 0) {
    // Capitaliza la primera letra y actualiza el valor del input
    input.value = texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}


//===================================== visualizar CV pdf ==================================================
function abrirModal() {
    document.getElementById('modal').style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

//======================================LOGICA DEL CHATBOT=============================================
const cajaTextoChat = document.querySelector(".contenedor-caja-texto textarea");
const containerChat = document.getElementById("chat");

const CLASE_USUARIO = "usuario";
const CLASE_BOT = "bot";
const TIEMPO_ESPERA_RESPUESTA = 700;

function crearMensaje(contenedor, clase, contenido, iconoSrc = null) {
    const nuevoMensaje = document.createElement("div");
    nuevoMensaje.classList.add("contenedor-mensaje", clase);

    if (clase === CLASE_USUARIO) {
        const mensajeUsuario = document.createElement("div");
        mensajeUsuario.classList.add("mensaje", "msg-usuario");
        mensajeUsuario.textContent = contenido;

        nuevoMensaje.appendChild(mensajeUsuario);
    } else {
        const contenedorIconoBot = document.createElement("div");
        contenedorIconoBot.classList.add("img-mensaje-bot");

        const iconoBot = document.createElement("img");
        iconoBot.src = iconoSrc || "/images/avatars/avatar-presentacion.svg";
        iconoBot.alt = "Imagen de bot";

        contenedorIconoBot.appendChild(iconoBot);

        const mensajeBot = document.createElement("div");
        mensajeBot.classList.add("mensaje", "msg-bot");
        mensajeBot.textContent = contenido;

        nuevoMensaje.appendChild(contenedorIconoBot);
        nuevoMensaje.appendChild(mensajeBot);
    }

    contenedor.appendChild(nuevoMensaje);
    return nuevoMensaje;
}

function enviar() {
    const msjUsuario = cajaTextoChat.value;

    if (msjUsuario.trim() !== "") {
        crearMensaje(containerChat, CLASE_USUARIO, msjUsuario);
        containerChat.scrollTo(0, containerChat.scrollHeight);
        cajaTextoChat.value = "";

        // Mensaje de respuesta del bot
        setTimeout(function () {
            const mensajeBot = crearMensaje(containerChat, CLASE_BOT, "Un momento por favor...", "/images/avatars/avatar-presentacion.svg");
            containerChat.scrollTo(0, containerChat.scrollHeight);

            const preguntaUsuario = { pregunta: msjUsuario };

            fetch("/api/chatbot", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(preguntaUsuario),
            })
                .then((response) => response.text())
                .then((data) => {
                    console.log("Respuesta del servidor:", data);
                    mensajeBot.querySelector(".msg-bot").textContent = data;
                    containerChat.scrollTo(0, containerChat.scrollHeight);
                })
                .catch((error) => {
                    console.error("Error al obtener la respuesta del servidor:", error);
                });
        }, TIEMPO_ESPERA_RESPUESTA);
    }
}




