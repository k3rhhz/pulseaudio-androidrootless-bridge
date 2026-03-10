# PulseAudio Android TCP Server

Para ejecutar este c�digo, debes crear un nuevo proyecto en **Android Studio**:

1. Abre Android Studio y selecciona **New Project** -> **Empty Views Activity** (o Empty Activity).
2. Usa el lenguaje **Kotlin**.
3. Copia el archivo `MainActivity.kt` en tu paquete (ej. `app/src/main/java/com/tu/paquete/MainActivity.kt`).
4. Copia el archivo `PulseAudioServer.kt` en la misma carpeta que `MainActivity.kt`.
5. Copia el contenido de `activity_main.xml` a `app/src/main/res/layout/activity_main.xml`.
6. En tu `AndroidManifest.xml` (en `app/src/main/AndroidManifest.xml`), aseg�rate de agregar los permisos de internet encima del tag `<application>`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   ```

## Configuraci�n en el PC (Linux)
Para enviar audio desde tu PC Linux a tu tel�fono Android, debes cargar el m�dulo de tcp en PulseAudio. 
Ejecuta el siguiente comando en la terminal de tu PC (reemplaza `IP_DEL_TELEFONO` por la IP de tu tel�fono en la red WiFi, por ejemplo `192.168.1.100`):

```bash
pacmd load-module module-tunnel-sink-new server=tcp:IP_DEL_TELEFONO:4713 format=s16le channels=2 rate=44100
```
O usando pactl (versiones modernas/PipeWire):
```bash
pactl load-module module-simple-protocol-tcp rate=44100 format=s16le channels=2 source=0 record=true port=4713
```
*(Ten en cuenta que dependiendo si usas PulseAudio nativo o PipeWire-Pulse, el m�dulo de salida/t�nel de PulseAudio puede llamarse distinto, por ejemplo `module-simple-protocol-tcp` o conectarlo por t�nel raw).*

Para Pipewire/Pulse moderno, una forma f�cil de hacer streaming TCP es instalar `pulseaudio-utils` y `netcat`:
```bash
parec --format=s16le --rate=44100 --channels=2 | nc IP_DEL_TELEFONO 4713
```

�Disfruta tu servidor de audio!
