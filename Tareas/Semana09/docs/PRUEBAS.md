# Verificación de AyudaCampus

Fecha: 20 de octubre de 2026. Proyecto: `Tareas/Semana09`, rama `Con-IA-Semana09`.

## Resultado ejecutado

- Compilación de Kotlin, Java y procesamiento Room/KSP: correctos.
- `:app:testDebugUnitTest`: **13 pruebas, 0 fallos, 0 errores**.
- `:app:lintDebug`: **0 errores y 59 advertencias**. Los informes completos se regeneran en `app/build/reports/`.
- Seis capturas de la ventana real de Compose generadas con Robolectric/Android SDK 34 y gráficos nativos; revisadas visualmente. No son mockups ni imágenes generadas con IA.
- Verificación de Git: los commits de esta entrega solo incluyen archivos bajo `Tareas/Semana09`.

Comando de la comprobación final (en esta carpeta, JDK 17):

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug --no-daemon --no-configuration-cache "-Pkotlin.compiler.execution.strategy=in-process" "-Pkotlin.incremental=false"
```

Se usa compilación en el proceso de Gradle para evitar que un daemon de Kotlin conserve rutas temporales del entorno de ejecución. No cambia el comportamiento de la app.

## Pruebas por archivo

| Archivo | Cantidad | Comprobación |
|---|---:|---|
| ReglasTicketTest | 4 | Propietario/rol; cierre solo de resueltos y con 1–5 estrellas; prohibición de comentarios en cerrados; reapertura; soporte no cierra ni modifica cerrados; categorías con tickets y nombres vacíos. |
| SlaTest | 2 | Límites exactos de 4/24/72 h; reloj anterior a la creación; resueltos/cerrados sin vencimiento activo. |
| BandejaTest | 1 | Cuatro filtros simultáneos, texto con número/nombre y conservación del orden original. |
| DuracionTest | 1 | Formato del promedio en minutos/horas y menos de un minuto. |
| RoomContractTest | 3 | Semilla única, seis tickets, orden y conteos/alerta/promedio; ciclo de ticket y comentarios; visibilidad por cliente; correo único; categorías; migración 1 → 2 sin pérdida de historial. |
| AppAcceptanceTest | 1 | Recorrido de ambos perfiles, login incorrecto/correcto, validación del formulario, creación real, cambio de pestaña durante guardado, panel, categorías, recepción cerrada y modo oscuro después de recrear la actividad; genera seis capturas. |
| ExampleUnitTest | 1 | Prueba aritmética que ya incluía el proyecto. |

## Matriz de aceptación de la guía

| Caso | Evidencia local |
|---|---|
| Login incorrecto | Recorrido Compose comprueba el mensaje y después ingresa con la clave correcta. |
| Ticket vacío | Recorrido Compose comprueba errores junto a título/descripción y captura el formulario. |
| Ticket válido | Compose crea y encuentra el ticket; Room verifica estado inicial y visibilidad por cliente. |
| Bandeja priorizada | Consulta Room verifica ALTA/MEDIA/BAJA; filtros conservan el orden. |
| Responder y cambiar estado | Room comprueba respuesta persistida y estado; la UI observa los mismos Flow. |
| Soporte no puede cerrar | Prueba directa del ViewModel; selector CERRADO deshabilitado en la pantalla. |
| Resolver, calificar y cerrar | Room comprueba estado, calificación y fecha; ViewModel rechaza cerrar sin calificación válida. |
| Reabrir resuelto | ViewModel/Room comprueban ABIERTO y limpieza de calificación/fecha. |
| No comentar en cerrado | ViewModel rechaza la acción; las vistas ocultan el formulario. |
| Panel | Room comprueba conteos, categorías, alerta y promedio; captura del panel con siete tickets tras la creación de prueba. |
| Desactivar categoría | Consulta Room deja de ofrecerla como activa; envío vuelve a comprobar que siga activa. |
| Preferencias y sesión | Compose cambia entre perfiles, cierra la recepción, guarda modo oscuro y verifica que permanezca al recrear la actividad. |

## Correcciones detectadas al verificar

- El callback de envío podía forzar el regreso a Tickets después de que el usuario hubiera elegido Perfil. Ahora solo vuelve automáticamente si todavía está en Nuevo; el recorrido prueba el cambio durante la escritura.
- El primer valor del reloj podía ser anterior al ticket recién creado y mostrar un minuto extra de SLA. Se limita la antigüedad mínima a cero y se prueba ese caso.
- Se sustituyó la eliminación en cascada por protección del historial y se verificó la migración con datos de la versión anterior.
- Se corrigieron la combinación Gradle/JDK y el grupo Maven de Espresso. AGP 8.7.3 incluye la corrección del análisis de Kotlin de la versión previa.
- Las capturas locales usan el dibujo nativo de la ventana porque PixelCopy depende de callbacks de redibujado de una ventana de hardware. El test espera a los datos reactivos y a los cambios de perfil antes de capturar.

## Límites y advertencias

No se ejecutaron pruebas en un teléfono físico ni el conjunto de instrumentación conectado. El emulador disponible no tenía aceleración de hardware utilizable. La recreación de la actividad no equivale a una prueba física de forzar detención y abrir otra vez; se recomienda repetir esa comprobación al ejecutar desde Android Studio.

Las advertencias de Lint incluyen recomendaciones de dependencias/SDK y organización del catálogo de versiones; no se ocultaron con un baseline. Gradle avisa además que AGP 8.7.x fue probado oficialmente hasta compileSdk 35, mientras el proyecto existente compila con el SDK 36 instalado. La compilación y las pruebas indicadas terminaron correctamente con esa configuración.

La app permanece local, con datos ficticios; las preferencias de notificaciones no implementan un servicio push. Las credenciales de práctica y los seis tickets se siembran solo en instalaciones nuevas.
