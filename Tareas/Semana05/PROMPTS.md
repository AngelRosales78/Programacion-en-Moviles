# Registro de Prompts - Fase 2 (Mejora con IA)

## Proyecto: Clínica Salud+
**Rama:** `mejora-ia-sem05`  
**Mejora Funcional:** Cancelación de cita médica mediante `AlertDialog` con actualización del estado local.

---

### Prompt 1: Generación del diálogo de cancelación
* **Prompt enviado:**
  > "Hola. Necesito agregar la funcionalidad de cancelar citas en la pantalla `MyAppointmentsScreen.kt` de Jetpack Compose. Quiero que cada tarjeta de cita 'Confirmada' tenga un botón 'Cancelar'. Al hacer clic, debe mostrar un `AlertDialog` pidiendo confirmación. Si el usuario confirma, el estado de la cita debe cambiar a 'Cancelada'. Todo debe manejarse con estado local `remember` y `mutableStateListOf` sin usar ViewModel."

* **Resultado de la IA:**
  La IA generó el componente `AlertDialog` y convirtió la lista estática en un `remember { mutableStateListOf(...) }`.

* **Correcciones realizadas:**
    - Se ajustaron los colores del badge para que coincidan con la paleta visual del proyecto (color rojo suave para el estado "Cancelada").
    - Se corrigió la importación de iconos de Material 3 para evitar errores de compilación.

---

### Prompt 2: Estilos visuales del estado Cancelado
* **Prompt enviado:**
  > "¿Cómo puedo personalizar visualmente la tarjeta de la cita cuando cambie a estado 'Cancelada' para diferenciarla de 'Confirmada' y 'Completada'?"

* **Resultado de la IA:**
  La IA sugirió una estructura condicional `when` para controlar el color de la barra lateral, el fondo del chip y el texto según el estado.

* **Correcciones realizadas:**
    - Se definió `Color(0xFFFFEBEE)` como fondo y `Color(0xFFC62828)` como color de texto para el estado "Cancelada".