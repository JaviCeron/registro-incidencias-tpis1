\# RegistroIncidencias



\## Propósito

RegistroIncidencias es una aplicación Android desarrollada como parte del curso

Técnicas de Producción Industrial de Software I (TPIS1) en la Universidad

Tecnológica de El Salvador. Permite registrar incidencias de equipos,

infraestructura o servicios mediante una interfaz simple, capturando un título

y una descripción, y consultarlas en una lista. En la Semana 11 se integró el

acelerómetro del dispositivo como primera evidencia de uso de sensores.



\## Herramientas utilizadas

\- Android Studio

\- Kotlin

\- Jetpack Compose (Material 3)

\- SensorManager / SensorEventListener (Android Sensor API)

\- Git / GitHub



\## Estado actual

\- \[x] Pantalla inicial con título e instrucción

\- \[x] Campos de entrada (título y descripción de la incidencia)

\- \[x] Botón "Registrar incidencia" que agrega la incidencia a una lista visible

\- \[x] Validación de campos vacíos con mensaje de error

\- \[x] Lista de incidencias registradas, con la más reciente primero

\- \[x] Lectura del acelerómetro (valores X, Y, Z) con mensaje interpretativo

\- \[x] Manejo del caso en que el sensor no está disponible

\- \[ ] Persistencia de datos (las incidencias viven solo en memoria, aún no hay base de datos)

\- \[ ] Uso del sensor para sugerir el registro de una incidencia



\## Sensor integrado: acelerómetro (Semana 11)



\*\*Qué hace.\*\* Una tarjeta en la pantalla principal muestra en tiempo real los

valores de aceleración en los ejes X, Y y Z (m/s²) y un mensaje según la

magnitud total:



| Magnitud | Mensaje |

| --- | --- |

| Entre 8.5 y 11 m/s² | Dispositivo en reposo |

| Fuera de ese rango, hasta 15 m/s² | Movimiento moderado |

| Mayor a 15 m/s² | Movimiento fuerte detectado |



\*\*Cómo funciona.\*\* Se obtiene el sensor con

`getDefaultSensor(Sensor.TYPE\_ACCELEROMETER)` y se registra un

`SensorEventListener` dentro de un `DisposableEffect`, que lo libera con

`unregisterListener` al salir de la pantalla.



\*\*Si no hay sensor.\*\* La app no registra el listener y muestra

"Acelerómetro no disponible en este dispositivo.", sin afectar el resto de

las funciones.



\*\*Por qué es útil.\*\* Detectar movimientos fuertes permitiría, en una mejora

futura, sugerir al usuario reportar un golpe o caída de un equipo.



\## Cómo se verificó

1\. Se ejecutó la app en \[emulador / dispositivo físico: completar].

2\. En reposo, Z ronda 9.8 m/s² y el mensaje indica reposo.

3\. Al mover el dispositivo de forma moderada, los valores cambian y el mensaje se actualiza.

4\. Las capturas están en el documento de evidencias entregado en Blackboard.



\## Limitaciones

\- Los umbrales del mensaje son aproximados y pueden variar según el teléfono.

\- En el emulador los valores se simulan desde Extended controls > Virtual sensors.

\- Las incidencias se pierden al cerrar la app, porque aún no hay persistencia.



\## Cómo abrir el proyecto

1\. Clona este repositorio o descárgalo como ZIP.

2\. Abre Android Studio.

3\. Selecciona File > Open y elige la carpeta del proyecto RegistroIncidencias.

4\. Espera a que Gradle sincronice las dependencias.

5\. Ejecuta la aplicación en un emulador o dispositivo físico con el botón Run.



\## Autor

Carlos Javier Lopez Ceron · 2917532020

Universidad Tecnológica de El Salvador · TPIS1 · Ciclo 02-2026

