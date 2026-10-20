# Prompts utilizados

## Herramienta y alcance

Se utilizó **OpenAI Codex** para continuar el proyecto existente. No se utilizó Gemini. La guía denomina la rama de IA `mejora-ia`; el estudiante pidió explícitamente **Con-IA-Semana09** y se respetó ese nombre. No se presentaron cambios generados por IA como parte de la fase sin IA.

## Prompt 1 · Solicitud real del estudiante

**Texto recibido por Codex:**

> deje mi proyecto a medias, necesito q cumpla con todo lo que pide este archivo, necesito que lo arregles y continues desde donde lo deje, solo haz commits de la parte con IA en una rama nueva llamada Con-IA-Semana09 en este repositorio https://github.com/AngelRosales78/Programacion-en-Moviles/tree/master/Tareas/Semana09 al momento de hacer push necesito que mantengas el orden de mi repo es decir que hagas push en la carpeta donde corresponde

**Contexto proporcionado:** proyecto local en `C:\Users\ADMIN\Ciclo4\ProgramacionMovil\Tareas\Semana09` y guía `GLAB-S08-JLEONS-2026_B.pdf`.

**Aclaración real posterior:** ante los archivos previos sin commit, el estudiante eligió “Un commit inicial identificado como trabajo previo del estudiante”. Ese commit se hizo antes de modificar el código con IA.

**Qué generó/cambió la IA:** conexión de pantallas de ejemplo a Room, ViewModels incompletos, reglas de cierre/reapertura y permisos, sesión persistente y preferencias, gestión de categorías, registro ya iniciado, componentes vacíos, pruebas, documentación y las tres mejoras siguientes.

**Correcciones realizadas durante la asistencia:** combinación de Gradle/JDK incompatible, dependencia Espresso con grupo incorrecto, creación/edición de categorías que utilizaba REPLACE, eliminación en cascada de tickets al borrar categorías, validaciones que faltaban en ViewModels y pantallas que mantenían solo listas en memoria. Las correcciones las realizó Codex; no se atribuyen al estudiante.

También se corrigieron dos casos detectados al ejecutar las pruebas: el retorno tardío a Tickets tras cambiar de pestaña y el minuto extra de SLA cuando el reloj se había emitido antes de crear el ticket. La verificación final terminó con 13 pruebas aprobadas y Lint sin errores.

**¿Respeta MVVM?:** Sí. Las vistas observan StateFlow y envían eventos a sus ViewModels; los ViewModels reciben AyudaRepository como interface; la implementación Room es la que conoce los DAOs. DataStore se usa exclusivamente para preferencias/sesión.

## Tareas derivadas de la guía dentro del mismo prompt

No son conversaciones adicionales enviadas a Gemini: son las tareas desarrolladas a partir de la solicitud anterior y de los requisitos de la fase 2.

### A · Tiempo promedio de resolución

**Requisito:** mostrar en el panel `AVG(fechaCierre - fecha)` de tickets cerrados usando DAO, contrato Repository, implementación, ViewModel y View.

**Resultado:** consulta reactiva que excluye fechas inválidas; presentación en horas/minutos y estado sin datos. El cálculo corresponde al cierre confirmado por el estudiante.

**Corrección/verificación:** usar promedio SQL y `Double?` para distinguir cero de ausencia de tickets. Pruebas de consulta Room y formato de duración. **MVVM:** respetado.

### B · Contador de SLA por prioridad

**Requisito:** Alta 4 h, Media 24 h y Baja 72 h.

**Resultado:** cálculo puro en modelo y reloj en ViewModel, compartido por la lista/detalle visible; indicador restante/vencido, resuelto o cerrado.

**Corrección/verificación:** el plazo se actualiza cada minuto sin necesitar una escritura en Room; la View no consulta el reloj. Se prueban los límites exactos de cada prioridad. **MVVM:** respetado.

### C · Búsqueda avanzada en la bandeja

**Requisito:** combinar prioridad, categoría y texto al mismo tiempo.

**Resultado:** `combine` con estado/prioridad/categoría/texto y datos del Repository; búsqueda por título, número o estudiante; limpiar filtros; conserva el orden del DAO.

**Corrección/verificación:** `filter` conserva el orden original; prueba con varios filtros simultáneos y búsqueda con `#`, espacios y mayúsculas. **MVVM:** respetado.

## Validación y límites

Los resultados ejecutados y cualquier limitación están en [docs/PRUEBAS.md](docs/PRUEBAS.md). No se marcan como realizadas pruebas en un teléfono físico. Las capturas provienen del recorrido local Android/Compose con gráficos nativos.

## Reflexión propuesta · cinco líneas para revisar con tus palabras

- La IA ayudó a conectar capas que estaban incompletas y a agregar indicadores verificables.
- Una pantalla que se ve terminada puede seguir usando datos en memoria y no guardar cambios.
- Las relaciones Room deben proteger el historial; una eliminación en cascada mal elegida puede borrar tickets.
- MVVM permite probar permisos y transiciones sin depender de la apariencia de los botones.
- El código generado necesita compilarse y probarse; las capturas y el registro de resultados deben reflejar lo realmente ejecutado.
