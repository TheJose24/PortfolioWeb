//================== Notificacion para activar audio al abrir la pagina ===============
const reproducirAudio = document.getElementById('reproductor');
const icono = document.getElementById('icono-musica');
const estadoMusica = document.querySelector('.control-musica');

(async () => {
    const { value: accept } = await Swal.fire({
        title: '¡Bienvenido a mi portafolio!',
        text: '¿Quieres activar la música de fondo para una experiencia completa mientras exploras mi trabajo?',
        icon: 'question',
        background: '#333333',
        color: '#ffffff',
        showCancelButton: true,
        allowOutsideClick: false,
        backdrop: true,
        confirmButtonColor: '#27ae60',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí',
        cancelButtonText: 'No'
    });

    if (accept) {
        reproducirAudio.play();
        audioActivado = true;
    } else {
        reproducirAudio.pause();
        icono.classList.add('fa-volume-off');
        icono.classList.remove('fa-volume-up');
        estadoMusica.dataset.text = 'Musica: Off';
        audioActivado = false;
    }

})();


//==================================== Logica para el formulario de contacto ============================
document.addEventListener('DOMContentLoaded', function () {
    // Obtener el formulario por su ID
    const formulario = document.getElementById('miFormulario');
    const enviarBtn = document.getElementById('enviarBtn');

    // Agregar un manejador de eventos para el evento submit
    formulario.addEventListener('submit', function (event) {
        event.preventDefault(); // Evitar el comportamiento predeterminado de recargar la página
        // Deshabilitar el botón mientras se envía el formulario
        enviarBtn.disabled = true;

        // Obtener los valores de los campos del formulario
        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const email = document.getElementById('email').value;
        const telefono = document.getElementById('telefono').value;
        const mensaje = document.getElementById('mensaje').value;

        // Crear un objeto Usuario con los datos del formulario
        const usuario = {
            nombre: nombre,
            apellido: apellido,
            email: email,
            telefono: telefono,
            mensaje: mensaje
        };


        // Muestra el spinner y oculta el texto "Enviar"
        document.getElementById('spinner').style.display = 'inline-block';
        document.getElementById('btnText').style.display = 'none';



        // Llamar a la función para enviar los datos al backend
        enviarDatosAlBackend(usuario);
    });

    function enviarDatosAlBackend(usuario) {
        fetch('/api/enviar-correo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al enviar datos al backend');
                }
                return response.json();
            })
            .then(data => {
                if (data.exito) {
                    mostrarNotificacion('Formulario enviado con éxito', 'success');
                    resetearFormulario();
                } else {
                    mostrarNotificacion('Hubo un error al enviar el formulario', 'error');
                }
            })
            .catch(error => {
                mostrarNotificacion('Hubo un error al enviar el formulario', 'error');
                console.error('Error al enviar datos al backend:', error);
            })
            .finally(() => {
                // Oculta el spinner y muestra el texto "Enviar" después de completar la solicitud
                document.getElementById('spinner').style.display = 'none';
                document.getElementById('btnText').style.display = 'inline';
                // Habilitar el botón nuevamente después de completar la solicitud
                enviarBtn.disabled = false;
            });
    }

    function mostrarNotificacion(mensaje, tipo) {
        let mensajePrincipal = '';
        let mensajeFooter = '';

        if (tipo === 'success') {
            mensajePrincipal = '¡Gracias por contactarme, me pondré en contacto contigo lo antes posible!';
            mensajeFooter = 'Recuerda revisar tu carpeta de correo no deseado para asegurarte de recibir mi respuesta.';
        } else {
            mensajePrincipal = 'Por favor, inténtelo de nuevo más tarde.';
        }

        Swal.fire({
            title: mensaje,
            text: mensajePrincipal,
            footer: mensajeFooter,
            icon: tipo,
            confirmButtonText: 'Aceptar',
            padding: '1rem',
            background: '#333333',
            color: '#ffffff',
            backdrop: true,
            allowOutsideClick: false,
            stopKeydownPropagation: 'false',
            confirmButtonColor: tipo === 'success' ? '#27ae60' : '#e74c3c'
        });
    }


    function resetearFormulario() {
        // Restablecer campos del formulario
        formulario.reset();
    }
});