¿QUÉ ES QËPI?
Es un chat de texto, de escritorio, anónimo y cifrado de punta a punta. 
Inspirado en el sistema de comunicación del Tahuantinsuyo 
realizada por los mensajeros del emperador, Los Chasquis, 
quienes corrian en postas para entregar mensajes codificados entre los monarcas de la 
civilización del Imperio INCA. Este mensajero llevaba entre otras cosas, 
un quipu que consistía en un instrumento de almacenamiento de información codificada 
en nudos de cuerdas, y el qëpi que era una bolsa para transportar objetos y encomiendas.

VERSION ACTUAL: 
Qëpi version 1.0.0 - 03AGO2022

LICENCIA:
GPL Copyleft MaverickSoft 2022

CONTACTO:
renesegovia@hotmail.com

MODO DE USO:
En el proyecto Qepi hacer:
mvn clean
mvn compile
mvn package

Se generan binarios en los targets de 3 submodulos QepiUtil, QepiServer, QepiClient

Para el SERVIDOR:
1.- El administrador debe levantar el servidor, copiandose la carpeta
$PROYECTS/Qepi/QepiServer/target/QepiServer a un directorio específico de su elección $MI_PATH/QepiServer
2.- En el archivo $MI_PATH/QepiServer/config/conexiones.xml configurar el servidor y puerto donde va a estar levantado este servidor.
3.- Cambiarse al directorio $MI_PATH/QepiServer/ y ejecutar startQepi.bat (para Windows) o ./startQepi.sh (para Linux)

Para cada uno de los USUARIOS CLIENTES:
1.- El usuario debe copiarse la carpeta
$PROYECTS/Qepi/QepiClient/target/QepiCli a un directorio específico de su elección $MI_PATH/QepiCli
2.- En el archivo $MI_PATH/QepiCli/config/conexiones.xml configurar el IP/hostname y puerto donde va a conectar,
debe corresponder con la misma información configurada en el servidor
3.- Cambiarse al directorio $MI_PATH/QepiCli/ y ejecutar runQepi.bat (para Windows) o ./runQepi.sh (para Linux)

NOTA TÉCNICA:
Está desarrollado con JAVAFX sobre JDK1.8 o también Open JDK (Zulu 8.64.0.15-CA-linux64 que tiene soporte de javafx), utilizando como IDE Intellij.
IDEA Se lo gestiona mediante Maven.

POR MEJORAR:
* BUGS de control de ventanas cuando el chat se hace entre 3 o más personas.
