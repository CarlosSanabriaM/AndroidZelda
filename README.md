# Android Zelda

Videojuego basado en "The Legend of Zelda", realizado para la plataforma Android y desarrollado en Java, para la asignatura "Software de Entretenimiento y Videojuegos".


## Contenido
- **La carpeta *app*** contiene el código de la aplicación. El resto de ficheros (excepto los pdf y el txt) son generados automáticamente por Android Studio. No son relevantes.
- **La carpeta *app/src/main/*** contiene:
    - **La carpeta *assets***: Aquí se almacenan los mapas de los niveles. Son ficheros de texto, donde cada símbolo representa un elemento en el mapa. Por ejemplo, la 'o' es un árbol, y el '1' es el jugador.
    - **La carpeta *res***: Aquí se almacenan, entre otras cosas: 
        - Los sprites e imágenes de los elementos gráficos (dentro de la carpeta *drawable*)
        - Las GUI de cada 'actividad Android' de la aplicación (dentro de la carpeta *layout*)
        - Los archivos de audio, tanto efectos de sonido como canciones (dentro de la carpeta *raw*)
    - **La carpeta *java/com/zelda***: Aquí se encuentran todos los ficheros .java.
    - **El fichero *AndroidManifest.xml***: Proporciona información esencial sobre la aplicación al sistema Android, información que el sistema debe tener para poder ejecutar el código de la app.
    - **El fichero *ic_launcher-web.png***: Es una imágen que representa el icono de la aplicación. Este icono será el que se muestre en el dispositivo Android cuando se instale el .apk.

- **El pdf *Propuesta inicial del juego*** explica la idea inicial de cómo iba a ser el videojuego.
- **El pdf *Funcionalidad del juego*** explica la funcionalidad final: Cómo jugar, qué detalles hay, qué se ha implementado realmente, ...
- **El txt *Posibles mejoras*** contiene una serie de posibles mejoras a realizar en el juego.


## Cómo ejecutar la aplicación
Abrir el proyecto con Android Studio. Es posible que al sincronizar el proyecto de algunos errores y pida actualizar algunas cosas.

Cuando esté todo listo, darle al botón *Run 'app'* (el triángulo verde) y seleccionar un dispositivo móvil virtual donde probar el juego.
