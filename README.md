<h1 align="center">📚 Challenge: Literalura 📚</h1>

<h2><em>🎯 Objetivo del Challenge</em></h2>

<p><em>Este proyecto fue desarrollado como parte de un desafío de programación con el objetivo de fortalecer las habilidades en el desarrollo de aplicaciones basadas en consola, el consumo de APIs externas, el manejo de datos JSON, y la persistencia de datos con Spring Data JPA y PostgreSQL.</em></p>

<h2><em>Descripción</em></h2>

<p><em>
El Catálogo de Libros es una aplicación de consola que permite a los usuarios buscar y registrar libros y autores de una vasta biblioteca digital a través de la API de Gutendex. El sistema almacena la información de los libros y sus autores en una base de datos PostgreSQL, permitiendo realizar diversas consultas y obtener estadísticas de los datos guardados.
</em></p>

<img width="575" height="331" alt="Captura 1" src="https://github.com/user-attachments/assets/afbb0b8d-e41e-4920-895b-201acb7e615e" />

<h2><em>Funcionalidades</em></h2>

<h3><em>1️⃣ Búsqueda y Registro de Libros</em></h3> 
<p><em>La primera opción del menú permite buscar un libro por su título en la API de Gutendex. Si el libro es encontrado y no está previamente registrado, el sistema guarda tanto la información del libro como la de su autor en la base de datos.</em></p>

<img width="482" height="267" alt="Captura 2" src="https://github.com/user-attachments/assets/1f75dfca-be1b-439d-9ed5-a4ec26cb04fd" />

<h3><em>2️⃣ Listado de Libros Registrados</em></h3> 
<p><em>Esta funcionalidad muestra un listado completo de todos los libros que han sido registrados en la base de datos, incluyendo su título, autor, idioma y número de descargas.</em></p>

<img width="1188" height="320" alt="Captura 3" src="https://github.com/user-attachments/assets/a4589e86-f553-45e2-be42-a3504f61b8a1" />

<h3><em>3️⃣ Listado de Autores Registrados</em></h3> 
<p><em>La opción 3 permite ver una lista de todos los autores cuyos libros han sido almacenados, junto con sus años de nacimiento y fallecimiento.</em></p>

<img width="812" height="342" alt="Captura 4" src="https://github.com/user-attachments/assets/ccfb0f6d-6693-4ca2-ad08-30740e42792b" />

<h3><em>4️⃣ Búsqueda de Autores Vivos en un Año Específico</em></h3> 
<p><em>Una funcionalidad avanzada que utiliza consultas derivadas de Spring Data JPA para encontrar y listar a todos los autores que estaban vivos en un año determinado por el usuario. Esta consulta también considera a los autores que aún no tienen una fecha de fallecimiento registrada.</em></p>

<img width="806" height="348" alt="Captura 5" src="https://github.com/user-attachments/assets/9fc29581-0491-4e61-9597-b6c8122fd150" />

<h3><em>5️⃣ Listado de Libros por Idioma</em></h3> 
<p><em>El usuario puede filtrar los libros registrados por idioma, facilitando la búsqueda de títulos en su preferencia lingüística.</em></p>

<img width="804" height="373" alt="Captura 6" src="https://github.com/user-attachments/assets/7057b0a0-a037-488d-831f-dcf16ba4fcef" />

<h3><em>6️⃣ Estadísticas de Descargas por Idioma</em></h3> 
<p><em>Esta opción utiliza la clase DoubleSummaryStatistics para generar un resumen estadístico del número de descargas de los libros, agrupados por idioma. Muestra el total de libros, el promedio, el mínimo y el máximo de descargas para cada idioma.</em></p>

<img width="455" height="482" alt="Captura 7" src="https://github.com/user-attachments/assets/898faa81-4ef4-47d7-aa05-0e0ca7153cad" />

<h3><em>7️⃣ Top 10 Libros Más Descargados</em></h3> 
<p><em>La aplicación es capaz de mostrar un ranking de los 10 libros más descargados entre todos los títulos registrados en la base de datos, ordenándolos de forma descendente.</em></p>

<img width="741" height="352" alt="Captura 8" src="https://github.com/user-attachments/assets/3ec73fe2-d0d7-4888-ae58-85591bb86165" />

<h3><em>8️⃣ Búsqueda de Autor por Nombre (en DB)</em></h3> 
<p><em>Además de las búsquedas en la API, esta funcionalidad permite encontrar los datos de un autor específico directamente en la base de datos local, mostrando también la lista de libros asociados a él.</em></p>

<img width="383" height="273" alt="Captura 9" src="https://github.com/user-attachments/assets/fc45e6c3-7e51-44da-9c5e-ec544d3e7ab9" />

<h3><em>9️⃣ Listado de Autores Nacidos Después de un Año</em></h3> 
<p><em>Una consulta adicional que demuestra la flexibilidad del sistema, permitiendo al usuario listar a todos los autores que nacieron después de un año dado.</em></p>

<img width="648" height="190" alt="Captura 10" src="https://github.com/user-attachments/assets/2425125f-ba72-4a19-bd3d-9df6c8364098" />

<h2><em>🛠 Tecnologías utilizadas</em></h2>

<p><em>Este proyecto fue desarrollado utilizando las siguientes tecnologías y herramientas:</em></p>

☕ Java – Lenguaje de programación principal.

🍃 Spring Boot – Framework para el desarrollo de la aplicación.

🌿 Spring Data JPA – Para la persistencia de datos y la creación de repositorios.

🐘 PostgreSQL – Base de datos relacional para el almacenamiento de información.

🌐 Gutendex API – API utilizada para obtener la información de los libros.

📦 Jackson Databind – Librería para el mapeo de objetos JSON a clases Java.

💻 Entorno de ejecución – El programa se ejecuta por consola, compatible con la mayoría de los IDEs modernos como IntelliJ IDEA o Eclipse.

<h2><em>⚙ Estructura del Proyecto</em></h2>

Explicación de la estructura:
📂 dto/ → Contiene los Data Transfer Objects (DTOs), que representan la información simplificada para la capa de presentación.

📂 model/ → Define las clases de entidad que mapean las tablas de la base de datos (Autor, Libro) y las clases record para mapear las respuestas de la API (DatosAutor, DatosLibro, ResultadosBusqueda).

📂 repository/ → Contiene las interfaces de repositorio para las entidades, que gestionan la interacción con la base de datos.

📂 service/ → Incluye la lógica de negocio, como la clase para consumir la API (ConsumoApi) y la clase para convertir datos JSON a objetos (ConvierteDatos).

🚀 ChallengeApplication.java → Clase principal de la aplicación, que implementa CommandLineRunner para iniciar el menú principal.

📜 Principal.java → Contiene el bucle principal del menú y la lógica de interacción con el usuario, orquestando todas las funcionalidades.

<h2><em>⚙ Instalación del Proyecto</em></h2>

<p><em>Sigue estos pasos para clonar y ejecutar el proyecto en tu entorno de desarrollo:</em></p>

### 1️⃣ Clonar el repositorio

Ejecuta el siguiente comando en tu terminal:

```bash
git clone  https://github.com/JhonatanO24/challenge-literalura.git
```

### 2️⃣ Configurar la Base de Datos

Crea una base de datos en PostgreSQL y edita el archivo de configuración con tus credenciales:

Ruta del archivo: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nombre_de_tu_bd
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```
### 3️⃣ Ejecutar la Aplicación

Abre el proyecto en tu IDE favorito (por ejemplo, IntelliJ IDEA o Eclipse) y ejecuta la clase principal:

```java
ChallengeApplication.java
```
