# CentroTareas — Hilos en Android (Semana 14)

App en Kotlin + Jetpack Compose + Material 3 que ejecuta trabajos que tardan sin
bloquear el hilo principal, usando corrutinas y ViewModel.

## Observación del Paso 1

Al pulsar **Tarea larga (MAL)** la pantalla se congela durante 4 segundos: el botón
**Tócame** no responde y los toques no suman. El texto *Trabajando...* nunca se ve
porque `Thread.sleep(4000)` bloquea el hilo principal, que es el mismo que dibuja la
pantalla; el estado cambia a *Trabajando...* pero Compose no tiene oportunidad de
recomponer hasta que el hilo se libera, y para entonces el mensaje ya es *Terminó*.
Si la espera fuera de 10 segundos, Android mostraría el diálogo **ANR**
(*"La aplicación no responde"*), ofreciendo cerrar la app o esperar.
