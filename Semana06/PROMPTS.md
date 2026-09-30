# Registro de Prompts - Fase 2 (Mejora Asistida por IA)

## Prompt 1: Elevación de Estado (State Hoisting)
* **Consulta:** "¿Cómo compartir el estado de favoritos entre el DropdownMenu de cada tarjeta de producto y el NavigationDrawer en Jetpack Compose?"
* **Resultado:** Se creó una lista mutable `favoritosIds` mediante `remember { mutableStateListOf<Int>() }` en el contenedor `AppNavegacion` para controlar los elementos seleccionados.

## Prompt 2: Badge Contador en NavigationDrawerItem
* **Consulta:** "¿Cómo agregar un Badge con contador dinámico al lado del texto de un NavigationDrawerItem en Material 3?"
* **Resultado:** Se envolvió el texto en un `Row` con `Arrangement.SpaceBetween` dentro del parámetro `label` e incluyó el componente `Badge` cuando el conteo de favoritos es mayor a cero.