# CampusGPS — Localización Geográfica (Semana 13)

**Alumno:** Angel Rosales · **Curso:** Programación en Móviles · **Docente:** Juan León S.

App en Kotlin + Jetpack Compose + Material 3 que obtiene la ubicación del celular con
`FusedLocationProviderClient`, la muestra en un mapa (Maps Compose) y calcula qué lugar
del campus está más cerca. Muestra cinco lugares ficticios ordenados por distancia y
sigue funcionando aunque el usuario niegue el permiso, apague el GPS o no tenga
Google Play Services. Todos los datos son ficticios.

## Cómo ejecutarlo

1. Crear una clave en Google Cloud con **Maps SDK for Android** habilitado y
   restringida al paquete `com.tecsup.campusgps` y a la SHA-1 de `./gradlew signingReport`.
2. Agregarla en `local.properties` (este archivo **no** se sube al repositorio):
   ```
   MAPS_API_KEY=tu_clave
   ```
3. Usar un emulador con Google Play y ponerlo en el punto base:
   *Extended controls → Location → -12.0464, -77.0428 → Set location*.

## Árbol de paquetes

```
com.tecsup.campusgps/
├── MainActivity.kt
├── model/
│   └── Modelos.kt                    Posicion, Lugar, LugarConDistancia, TipoError, UbicacionUi
├── data/
│   ├── UbicacionRepository.kt        interface (+ playServicesDisponible)
│   ├── UbicacionRepositoryGoogle.kt  implementación con FusedLocationProviderClient
│   ├── LugaresRepository.kt          interface
│   └── LugaresRepositoryLocal.kt     los 5 lugares ficticios
├── util/
│   ├── Distancia.kt                  distanciaMetros (Location.distanceBetween)
│   └── Formato.kt                    formatearDistancia (m / km)
├── viewmodel/
│   ├── MapaViewModel.kt              estado, lugares, seleccionado, masCercano
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt               permisos + composición de la pantalla
    ├── MapaUbicacion.kt              GoogleMap, cámara y marcadores
    ├── ListaLugares.kt               LazyColumn de tarjetas
    ├── MensajeError.kt               recuadro de error con su acción
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaMapa)
   |  evento: pulsar el botón        ^  collectAsState()
   v                                 |
ViewModel (MapaViewModel)  --- StateFlow --->
   |  llama a las interfaces
   v
UbicacionRepository  ---> UbicacionRepositoryGoogle (FusedLocationProviderClient)
LugaresRepository    ---> LugaresRepositoryLocal (los 5 lugares fijos)
```

## Datos ficticios

| Id | Nombre | Categoría | Latitud | Longitud |
|----|--------|-----------|---------|----------|
| 1 | Biblioteca Central | Estudio | -12.0460 | -77.0435 |
| 2 | Cafetería Campus | Comida | -12.0470 | -77.0420 |
| 3 | Laboratorio de Cómputo | Estudio | -12.0455 | -77.0415 |
| 4 | Auditorio Principal | Eventos | -12.0480 | -77.0440 |
| 5 | Estacionamiento Norte | Servicios | -12.0440 | -77.0430 |

## Capturas

| Permiso | Mapa con mi ubicación | Marcadores |
|---|---|---|
| ![Permiso](capturas/01-permiso.png) | ![Mapa](capturas/02-mapa-ubicacion.png) | ![Marcadores](capturas/03-marcadores.png) |

| Lista ordenada | Error |
|---|---|
| ![Lista](capturas/04-lista-ordenada.png) | ![Error](capturas/05-error.png) |

## Para pensar

**1. ¿Por qué `FusedLocationProviderClient` está dentro del Repository y no en el ViewModel?**
Porque es un detalle de Google Play Services que necesita un `Context`. El ViewModel solo
conoce la interface `UbicacionRepository`, así no depende de Android, se puede probar con
un repositorio falso y la fuente de la ubicación se puede cambiar sin tocar la lógica.

**2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?**
El permiso lo concede el **usuario** y autoriza a la app a leer la posición del celular;
si falta, no hay ubicación y la app muestra el mensaje con *Abrir ajustes*. La clave la
emite **Google Cloud** e identifica a la app ante el servicio de mapas; si falta o es
incorrecta, la ubicación y la lista funcionan, pero el mapa sale gris
(*Authorization failure* en Logcat).

**3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?**
`local.properties` está en `.gitignore`, así que la clave nunca llega al repositorio y
cualquiera que lo clone usa la suya. Llega al Manifest con `manifestPlaceholders`. Si se
subiera, la borraría en Google Cloud y crearía una nueva, porque la anterior ya quedó
expuesta en el historial aunque se elimine el archivo.

**4. ¿Dónde calculas la distancia y por qué no en el Composable?**
En `util/Distancia.kt`, llamada desde `actualizarDistancias()` del ViewModel cuando llega
la ubicación. Un Composable puede recomponerse muchas veces; si calculara ahí repetiría
el trabajo y mezclaría lógica con dibujo, rompiendo MVVM. La pantalla solo dibuja la
lista ya ordenada.

**5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces?**
Android deja de mostrar el cuadro de permiso y el launcher devuelve `false` de inmediato.
La app no se cierra: `vm.permisoDenegado()` muestra el mensaje con el botón
*Abrir ajustes*, que abre la pantalla de la app (`ACTION_APPLICATION_DETAILS_SETTINGS`)
para que el usuario conceda el permiso manualmente.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Abrir la app por primera vez y pulsar el botón | Aparece el cuadro de permiso | ☐ |
| Aceptar el permiso | Se ven latitud, longitud y el marcador *Estás aquí* | ☐ |
| Negar el permiso | Mensaje de error con *Abrir ajustes*, la app no se cierra | ☐ |
| Ubicación en el punto base | Los 5 marcadores azules aparecen cerca de tu posición | ☐ |
| Tocar un marcador | Se ve el nombre y la categoría del lugar | ☐ |
| Lista de lugares | Ordenada de menor a mayor distancia, con m o km | ☐ |
| Tocar una tarjeta de la lista | La cámara viaja a ese lugar y la tarjeta queda resaltada | ☐ |
| Ir al más cercano | La cámara va al lugar verde; deshabilitado antes de buscar | ☐ |
| Cambiar la ubicación y buscar otra vez | Distancias, orden y lugar verde se actualizan | ☐ |
| Ubicación apagada | Mensaje de GPS con *Reintentar* | ☐ |
| Rotar la pantalla | No se pierden la ubicación ni la lista | ☐ |
| Revisar el repositorio | La clave no aparece y `local.properties` no está subido | ☐ |
