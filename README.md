# RegistroIncidencias

## Propósito
RegistroIncidencias es una aplicación Android desarrollada como parte del curso
Técnicas de Producción Industrial de Software I (TPIS1) en la Universidad
Tecnológica de El Salvador. Permite registrar incidencias de equipos,
infraestructura o servicios mediante una interfaz simple, capturando un título
y una descripción, y consultarlas en una lista. Incluye una sección de
diagnóstico del dispositivo que usa el acelerómetro como recurso de entrada.

## Herramientas utilizadas
- Android Studio
- Kotlin
- Jetpack Compose (Material 3)
- SensorManager / SensorEventListener (Android Sensor API)
- Git / GitHub

## Estado actual
- [x] Pantalla inicial con título e instrucción
- [x] Campos de entrada (título y descripción de la incidencia)
- [x] Botón "Registrar incidencia" que agrega la incidencia a una lista visible
- [x] Validación de campos vacíos con mensaje de error
- [x] Lista de incidencias registradas, con la más reciente primero
- [x] Lectura del acelerómetro (valores X, Y, Z) con mensaje interpretativo (Semana 11)
- [x] Manejo del caso en que el sensor no está disponible
- [x] Sección "Diagnóstico del dispositivo" con estado de disponibilidad y de escucha (Semana 12)
- [x] Liberación del sensor al pasar la app a segundo plano
- [ ] Persistencia de datos (las incidencias viven solo en memoria, aún no hay base de datos)
- [ ] Uso del sensor para sugerir el registro de una incidencia

## Diagnóstico del dispositivo (acelerómetro)

Una tarjeta en la pantalla principal muestra:
- Disponibilidad del acelerómetro. Si no existe, muestra un mensaje claro de que no hay lectura disponible.
- Estado de la escucha: activa o detenida.
- Valores X, Y y Z en m/s².
- Una frase interpretativa según la magnitud total:

| Magnitud | Mensaje |
| --- | --- |
| Sin datos todavía | Sin lectura disponible aún |
| Entre 8.5 y 11 m/s² | Dispositivo en reposo |
| Fuera de ese rango, hasta 15 m/s² | Movimiento moderado |
| Mayor a 15 m/s² | Movimiento fuerte detectado |

**Control del ciclo de vida.** El SensorEventListener se registra en
ON_RESUME y se libera en ON_PAUSE con un LifecycleEventObserver, para no
escuchar el sensor cuando la app está en segundo plano. El DisposableEffect
también lo libera al salir de la pantalla.

## Cómo se verificó
1. Se ejecutó la app en [emulador / dispositivo físico: completar].
2. En reposo, Z ronda 9.8 m/s² y el mensaje indica reposo.
3. Al mover el dispositivo de forma moderada, los valores cambian y el mensaje se actualiza.
4. Al enviar la app a segundo plano, la escucha pasa a detenida y vuelve a activa al regresar.
5. Las capturas están en los documentos de evidencias entregados en Blackboard.

## Limitaciones
- Los umbrales del mensaje son aproximados y pueden variar según el teléfono.
- En el emulador los valores se simulan desde Extended controls > Virtual sensors.
- Las incidencias se pierden al cerrar la app, porque aún no hay persistencia.

## Cómo abrir el proyecto
1. Clona este repositorio o descárgalo como ZIP.
2. Abre Android Studio.
3. Selecciona File > Open y elige la carpeta del proyecto RegistroIncidencias.
4. Espera a que Gradle sincronice las dependencias.
5. Ejecuta la aplicación en un emulador o dispositivo físico con el botón Run.

## Autor
Carlos Javier Lopez Ceron · 2917532020
Universidad Tecnológica de El Salvador · TPIS1 · Ciclo 02-2026