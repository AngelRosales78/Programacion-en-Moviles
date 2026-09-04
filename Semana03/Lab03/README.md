Lab-03
Pantalla inicial:
![antes.png](antes.png)

Producto registrado:
![despues.png](despues.png)


¿Qué pasaría si declaras las variables de los campos SIN `remember`?
Si las variables de estado se declararan sin `remember` el estado no se conservaría durante las recomposiciones de Jetpack Compose.
Cada vez que el usuario escribe un carácter o interactúa con la interfaz, la pantalla se vuelve a renderizar ejecutando nuevamente la función composable.


PARTE B: MEJORA ASISTIDA POR IA 

Prompt que usé 

Estoy trabajando en un proyecto de Jetpack Compose en Kotlin (PantallaRegistro). 
Tengo los estados nombre, precio, cantidad y mostrarResumen. 
Necesito agregar: 1. Validación de campos vacíos o inválidos: Si el usuario presiona 'AGREGAR PRODUCTO' y falta algún campo, no se debe mostrar la Card de resumen,
sino un mensaje de error en texto de color rojo . 2. Un botón 'Limpiar' que vacíe todos los campos de texto, oculte la Card de resumen y borre el mensaje de error.
no modifiques la estructura básica del encabezado ni la Card existente.


Que genero Gemini

Estado de error: Declaró var mensajeError by remember { mutableStateOf("") } para almacenar el texto cuando la validación falla.
Lógica de validación: Agregó comprobaciones con isBlank(), toDoubleOrNull() e toIntOrNull() dentro del onClick del botón.
Controles visuales: Creó una fila (Row) con dos botones de igual peso (AGREGAR y LIMPIAR) y un composable Text en rojo para mostrar la alerta. 

Lo que se aceptó

Se mantuvo la lógica completa del estado mensajeError, la validación de vacíos y la lógica del botón para limpiar los campos y ocultar la Card.

Lo que se corrigió:

Reorganización de botones: Se cambió el botón LIMPIAR de la fila principal a un TextButton centrado debajo para no restarle importancia visual al botón principal de acción.
Texto del botón principal: Se cambió "AGREGAR" por "AGREGAR PRODUCTO" para mantener consistencia estricta con el diseño de la Parte A.
Indicadores visuales en campos: Se añadió la propiedad isError a los OutlinedTextField para que las cajas de texto se marquen automáticamente con borde rojo si el dato es incorrecto.
Formato de texto: Se agregaron espacios entre la moneda y el monto (S/ 560.00 en vez de S/560.00) para mejorar la legibilidad del resumen