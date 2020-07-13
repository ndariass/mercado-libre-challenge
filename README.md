# Desafío técnico — Mercado Libre
Versión simplificada de la aplicación móvil de Mercado Libre para Android desarrollada como reto técnico para aplicar al cargo de desarrollador móvil.

## Descripción general
La aplicación permite buscar productos dado un criterio usando la API pública de Mercado Libre, así como mostrar un detalle de cada producto. La aplicación cuenta con dos vistas:

1. Ingreso del criterio de búsqueda y visualziación de resultados
2. Visualización del detalle de un producto

La versión mínima soportada de Android es 5.0, con lo cual se cubre el 94.1 % del total de dispositivos que usan este sistema operativo.

## Instalación
Ingresando la siguiente instrucción en la línea de comandos se ejecuta las pruebas unitarias de todos los módulos y se genera el instalador en `app/build/outputs/apk/debug/mercado-libre-tech-challenge-debug.apk`

    ./gradlew testDebugUnitTest assembleDebug

## Arquitectura
La aplicación se desarrolló siguiendo el patron MVVM (*model-view-view model*) y se distribuye en cuatro módulos de la siguiente manera:

### mercadolibre-domain
Definición de alto nivel de los repositorios y de los objetos modelo a usar. Estas definiciones son independientes de la fuente de datos.

### mercadolibre-data
Implementación de los repositorios de acuerdo a la fuente de datos. Incluye la definición de objetos DTO (*data transfer object*, por sus siglas en inglés) usados para la deserialización de la respuesta de la API REST, así como clases para la conversión de estos objetos a los objetos modelo de la capa de dominio.

### mercadolibre-ui
Contiene los componentes de la vista (actividades, fragmentos, etc.), *managers* para manejar la lógica de la vista y *view models*.

### app
Es el módulo principal de la app, donde se configura la inyección de dependencias y la actividad de inicio en el archivo `AndroidManifest.xml`

Todos los módulos, excepto **mercadolibre-domain**, son librerías de Android y por tanto tienen su propio `AndroidManifest.xml`. En cada uno se añaden las configuraciones necesarias como el permiso de acceso a internet o el estilo global de la aplicación.

## Librerías
La aplicación usa varias libreras de Android y de terceros con el fin de simplificar el desarrollo. En todos los casos se trata de librerías ampliamente usadas por la comunidad.

### Código de producción
* **Retrofit:** simplifica el codigo necesario para la comunicación con la API REST.
* **Gson:** deserialización del resultado de la API REST. En lugar de Gson se consideró también Jackson, que en general se conoce por ser más rápida que la primera. Sin embargo, de acuerdo a las pruebas realizadas se notó que Gson resultó ser ligeramente más eficiente, particularmente al realizar la primera consulta luego de iniciar el proceso de la aplicación.
* **Dagger y Dagger Android:** se encargan de la creación e inyección de las dependencias de la aplicación en todos los módulos.
* **Architecture components - View Model y Live Data:** usadas para la implementación de la capa *view model* de acuerdo a la arquitectura de la aplicación. El uso de *live data* permite notificar eventos a las vistas considerando su ciclo de vida.
* **AndroidX - Paging:** implementación de *scroll* infinito para los resultados de la búsqueda. La librería permite la implementación de una fuente de datos que entrega resultados a medida que el usuario se desplaza en la lista.
* **Material:** incluye los componentes `TextInputLayout` y `TextInputEditText` usados para el ingreso del criterio de búsqueda en la primera vista de la aplicación. Estos componentes permiten la configuración de un *hint*, un ícono para limpiar el texto y la acción correspondiente para configurar el campo de texto como un campo de búsqueda.
* **Picasso:** carga de imágenes desde internet.

### Código de pruebas
* **JUnit:** *framework* para la ejecución de pruebas unitarias.
* **Mockito:** creación de *mocks* de las dependencias de cada clase a probar, según se requiera.
* **Robolectric:** necesaria para la ejecución de pruebas unitarias en casos de que se use clases del *framework* de Android, por ejemplo `Log` o `Context`.
* **Core testing:** utilidades de Android para pruebas. Se necesita, por ejemplo, para proveer instancias de la clase `Context`.

## Manejo de errores
### Desde el punto de vista del usuario
* **Error de red**, en caso de que no esté conectado a una red o esta sea deficiente.
* **Sin resultados**, en caso de que no se obtenga resultados para un criterio de búsqueda.
* **Error general** para los demás casos (por ejemplo, cuando hay un error en el servidor).
* Considerando que los resultados se cargan usando *scroll* infinito, en caso de que haya un error al intentar acceder a la siguiente página simplemente se presenta un `Toast` al usuario.
* En la vista de detalle del producto no se presentan errores al usuario dado que aquí no se realiza una consulta al API. Sin embargo, un caso borde que puede suceder es que no se pueda pasar la información del producto desde la anterior actividad; en ese caso simplemente se cierra la vista  del detalle y se retorna a la vista de búsqueda.

### Desde el punto de vista del desarrollador
El método del repositorio retorna una instancia de la clase `Response`, definida en el módulo de dominio con los siguientes campos:

* `payload`, que contiene el resultado de la búsuqueda en caso de que la solicitud a la API sea exitosa y contenga al menos un elemento
* `paging`, que contiene información de la paginación, necesaria para la implementación del *scroll* infinito.
* `successful`, una bandera indicando si el resultado fue exitoso o no.
* `error`, un valor de un `Enum` que puede ser `NOT_FOUND`, `NETWORK_ERROR`, `GENERAL_ERROR` o nulo (si el resultado fue exitoso). De acuerdo a esto se define el error a mostrar al usuario en el módulo de UI.
* `errorMessage`, mensaje de error usado únicamente con fines de pruebas de desarrollo. Se define a partir del resultado de la consulta a la API; en caso de que no se pueda obtener se define un mensaje general. En la capa de UI este mensaje se muestra en los logs.

## Consideraciones generales
* La aplicación únicamente hace uso del recurso de la API `sites/$SITE_ID/search`, que retorna el resultado de una búsqueda con algunos detalles de cada producto. A partir de este resultado se presenta la información en ambas vistas de la aplicación, lo cual se considera suficiente para el desarrollo de este reto técnico. Sin embargo, una solución más completa incluiría consultar los recursos `items/$ITEM_ID` y `reviews/item/$ITEM_ID`, con el fin de dar más información en la vista del detalle del producto, similar a como se hace en la aplicación real de Mercado Libre.
* Todas las consultas se hacen al sitio de Colombia `MCO`, según lo configurado en la clase `ProductsRestApi`.
* De acuerdo al objeto modelo del producto, definido en el módulo de dominio, el id, título y precio son obligatorios, tal que en la capa de datos se descartan los items que no cumplen esta condición. Normalmente este tipo de decisiones se toman con el equipo técnico y de producto, de modo que estas reglas podrían variar.
* Considerando que en el módulo de UI la lógica de la vista se delega a la clase `ProductsUiManager`, es posible lograr una buena cobertura de pruebas escribiendo solo pruebas unitarias. De todos modos, en general es deseable escribir también pruebas de instrumentación sobre actividades, fragmentos, *adapters* y demás componentes de la vista.

## Autor
* **Nicolás Arias** - [ndariass@unal.edu.co](mailto:ndariass@unal.edu.co)
