# PROMPTS.md — Fase 2 (rama `mejora-ia-sem13`)

Asistente usado: **Claude Code** (en lugar de Gemini en Android Studio).
Regla aplicada: *la IA propone, yo decido*. Todo lo generado se leyó, se compiló y se
probó en el celular antes de cada commit. Se verificó que el ViewModel siga sin recibir
`Context`, que ningún Composable calcule distancias ni filtre datos, y que la clave de
Maps nunca aparezca en un prompt ni en el repositorio.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega chips (Estudio, Comida, Eventos, Servicios) que filtren los lugares del mapa y de la lista, sin romper MVVM.» | Guardar la categoría elegida en un `MutableStateFlow<String?>` del ViewModel y exponer `lugaresVisibles` con `combine(_lugares, _categoria)`; los chips en un Composable aparte (`FiltroCategorias`) con `FilterChip` dentro de un `LazyRow`. | Lo acepté. Saqué las categorías del Repository con `distinct()` en vez de escribirlas a mano, agregué el chip **Todos** y hice que `masCercano` se derive de `lugaresVisibles`. Si el lugar seleccionado queda oculto, se quita la selección. | Filtrar es lógica, así que va en el ViewModel; la View solo dibuja la lista que recibe. Si el verde siguiera calculándose sobre todos los lugares, podría marcar uno que no está en el mapa. |
| «Agrega un selector de radio de 100, 200 o 500 m que dibuje un círculo alrededor de mi posición y marque en la lista los lugares que quedan dentro.» | `Circle` de Maps Compose centrado en la posición, y en la lista comparar `metros <= radio` para mostrar una etiqueta. | Acepté el `Circle`, pero **moví la comparación al ViewModel**: expone `idsDentroDelRadio: StateFlow<Set<Int>>` con `combine(_lugares, _radio)`. La tarjeta solo recibe un `Boolean` y muestra *Dentro del radio*. El selector solo aparece cuando ya hay ubicación. | Comparar distancias dentro de `ListaLugares` es lógica en un Composable, justo lo que la regla de oro prohíbe. Radio por defecto 200 m: con el emulador en el punto base deja 3 lugares dentro. |
| «Activa el punto azul nativo y el botón de mi ubicación del mapa, solo si el permiso fue concedido.» | `GoogleMap(properties = MapProperties(isMyLocationEnabled = ...), uiSettings = MapUiSettings(myLocationButtonEnabled = ...))`, con un estado `permisoConcedido` en la pantalla. | Lo acepté y extraje `tienePermiso()` para reutilizarlo en `alPulsar()`. El estado se actualiza al responder el cuadro de permiso y cada vez que cambia el estado de la UI (por ejemplo, al volver de *Abrir ajustes*). | Activar `isMyLocationEnabled` sin permiso lanza `SecurityException` y cierra la app. El permiso es un asunto de Android y de la View, así que no se llevó al ViewModel, que debe seguir sin `Context`. |
| «¿Puedo revisar el permiso dentro del ViewModel para no repetir código?» | Pasar el `Context` (o la `Application`) al ViewModel y llamar a `ContextCompat.checkSelfPermission` desde ahí. | **Descartado.** | Rompe la regla del laboratorio: el ViewModel no recibe `Context`. La revisión se queda en `PantallaMapa`, y el ViewModel solo se entera del resultado (`buscar()` o `permisoDenegado()`). |
| «¿Agrego también el seguimiento en vivo con `requestLocationUpdates` y `callbackFlow`?» | Una función `seguirPosicion(): Flow<Posicion>` en el Repository con `callbackFlow` y `awaitClose { removeLocationUpdates(...) }`, y botones Iniciar/Detener. | **Descartado por ahora.** | Ya había 3 mejoras funcionando y dos tocan el ViewModel. El seguimiento gasta batería y requiere cancelar bien las actualizaciones al salir; preferí no entregar algo a medio probar. |

## Mejoras implementadas

| Mejora | Capas que cambian | Commit |
|---|---|---|
| Filtrar por categoría | ViewModel + View | `ia: filtra lugares por categoria con chips` |
| Radio de cercanía | ViewModel + View | `ia: agrega radio de cercania con circulo en el mapa` |
| Punto azul nativo | View | `ia: activa el punto azul nativo de ubicacion en el mapa` |
