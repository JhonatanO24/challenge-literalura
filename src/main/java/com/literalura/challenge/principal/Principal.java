package com.literalura.challenge.principal;

import com.literalura.challenge.model.Autor;
import com.literalura.challenge.model.DatosLibro;
import com.literalura.challenge.model.Libro;
import com.literalura.challenge.model.ResultadoBusqueda;
import com.literalura.challenge.repository.AutorRepository;
import com.literalura.challenge.repository.LibroRepository;
import com.literalura.challenge.service.ConsumoAPI;
import com.literalura.challenge.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    public static String MORADO = "\u001B[35m";
    public static String AZUL = "\u001B[34m";
    public static String AMARILLO = "\u001B[33m";
    public static String RESET = "\u001B[0m";
    public static String ROJO = "\u001B[31m";
    public static String VERDE = "\u001B[32m";
    public static String CYAN = "\u001B[36m";

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    private String URL_BASE = "https://gutendex.com/books/";

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    private List<Libro> librosGuardados;
    private List<Autor> autoresGuardados;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion !=0) {
            var menu = MORADO + """
                    📚 --- Catálogo de Libros ---
                    """ + AZUL + """
                        1️⃣ - 🔎 Buscar libro por título
                        2️⃣ - 📘 Listar libros registrados
                        3️⃣ - ✍️ Listar autores registrados
                        4️⃣ - 🧓 Listar autores vivos en un determinado año
                        5️⃣ - 🌐 Listar libros por idioma
                        6️⃣ - 📊 Mostrar estadísticas de libros por idioma
                        7️⃣ - 🏆 Top 10 Libros más descargados
                        8️⃣ - 🔍 Buscar autor por nombre (en DB)
                        9️⃣ - 🎂 Listar autores nacidos después de un año
                        0️⃣ - 🚪 Salir
                    """ + MORADO + """
                        ---------------------------
                    """ + AMARILLO + """
                        📝 Elija una opción:
                    """ + RESET;

            System.out.println(menu);

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(ROJO + "❌ Entrada inválida. Por favor ingrese un número del menú." + RESET);
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    mostrarEstadisticasPorIdioma();
                    break;
                case 7:
                    mostrarTop10LibrosMasDescargados();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
                case 9:
                    listarAutoresNacidosDespuesDeAno();
                    break;
                case 0:
                    System.out.println(AMARILLO + "📕 Cerrando el catálogo... ¡Hasta pronto!" + RESET);
                    break;
                default:
                    System.out.println(ROJO + "⚠️ Opción no válida. Intente de nuevo." + RESET);
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Ingrese el titulo del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var urlBusqueda = URL_BASE + "?search=" + tituloLibro.replace(" ", "%20");

        try {
            var json = consumoAPI.obtenerDatos(urlBusqueda);
            var resultados = conversor.obtenerDatos(json, ResultadoBusqueda.class);

            if (resultados != null && !resultados.resultados().isEmpty()) {
                DatosLibro datosLibro = resultados.resultados().get(0);

                Optional<Libro> libroExistente = libroRepository.findByTituloContainingIgnoreCase(datosLibro.titulo());

                if (libroExistente.isPresent()) {
                    System.out.println(ROJO + "⚠️ El libro: " + datosLibro.titulo() + " ya está registrado." + RESET);
                    System.out.println(libroExistente.get());
                } else {
                    Autor autor = null;
                    if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
                        var datosAutor = datosLibro.autores().get(0);
                        Optional<Autor> autorExistente = autorRepository.findByNombreContainingIgnoreCase(datosAutor.nombre());

                        if (autorExistente.isPresent()) {
                            autor = autorExistente.get();
                        } else {
                            autor = new Autor(datosAutor);
                            autorRepository.save(autor);
                            System.out.println(VERDE + "✅ Autor " + autor.getNombre() + " registrado exitosamente." + RESET);
                        }
                    }

                    Libro libro = new Libro(datosLibro);
                    libro.setAutor(autor);
                    libroRepository.save(libro);

                    System.out.println(VERDE + "\n✅ --- Libro encontrado y registrado ---" + RESET);
                    System.out.println("📖 Título: " + libro.getTitulo());
                    System.out.println("👤 Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                    System.out.println("🌐 Idioma: " + libro.getIdioma());
                    System.out.println("⬇️ Número de descargas: " + libro.getNumeroDescarga());
                    System.out.println(VERDE + "-------------------------------------\n" + RESET);

                }
            } else {
                System.out.println(AMARILLO + "🔍 No se encontraron libros con ese título." + RESET);
            }
        } catch (RuntimeException e) {
            System.out.println(ROJO + "❌ Error al buscar el libro: " + e.getMessage() + RESET);
            System.out.println(ROJO + "🛠️ Asegúrese de que el título sea correcto." + RESET);
        }
    }

    private void listarLibrosRegistrados() {
        librosGuardados = libroRepository.findAll();
        if (librosGuardados.isEmpty()) {
            System.out.println(AMARILLO + "📚 No hay libros disponibles aún. ¡Pronto llegarán!" + RESET);
            return;
        }
        System.out.println(CYAN + "\n📚 --- Libros Registrados ---" + RESET);
        librosGuardados.forEach(l ->
                System.out.println("📖 Título: " + l.getTitulo() +
                        " | 👤 Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido") +
                        " | 🌐 Idioma: " + l.getIdioma() +
                        " | ⬇️ Descargas: " + l.getNumeroDescarga()));
        System.out.println(CYAN + "---------------------------------\n" + RESET);

    }

    private void listarAutoresRegistrados() {
        autoresGuardados = autorRepository.findAll();
        if (autoresGuardados.isEmpty()) {
            System.out.println(AMARILLO + "\n⚠️ No hay autores guardados aún." + RESET);
            return;
        }
        System.out.println(AZUL + "\n🖋️ --- Autores Registrados ---" + RESET);
        autoresGuardados.forEach(a ->
                System.out.println("👤 Nombre: " + a.getNombre() +
                        " | 🎂 Nacimiento: " + a.getAnoNacimiento() +
                        " | 🕯️ Fallecimiento: " + (a.getAnoFallecimiento() != null ? a.getAnoFallecimiento() : "N/A")));
        System.out.println(AZUL + "---------------------------\n" + RESET);
    }

    private void listarAutoresVivosPorAno() {
        System.out.println(VERDE + "\n🔍 Ingrese el año para buscar autores vivos:" + RESET);
        try {
            var ano = Integer.parseInt(teclado.nextLine());

            List<Autor> autoresVivos = autorRepository.findByAnoNacimientoLessThanEqualAndAnoFallecimientoGreaterThanEqual(ano, ano);

            if (autoresVivos.isEmpty()) {
                System.out.println(AMARILLO + "⚠️ No se encontraron autores vivos en el año " + ano + "." + RESET);
            } else {
                System.out.println(CYAN + "\n📅 Autores vivos en el año " + ano + RESET);
                System.out.println(MORADO + "-----------------------------------------" + RESET);
                autoresVivos.forEach(a ->
                        System.out.println(VERDE + "👤 Nombre: " + a.getNombre() +
                                " | 🎂 Nacimiento: " + a.getAnoNacimiento() +
                                " | 🕯️ Fallecimiento: " +
                                (a.getAnoFallecimiento() != null ? a.getAnoFallecimiento() : "N/A") + RESET)
                );
                System.out.println(MORADO + "-----------------------------------------\n" + RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println(AMARILLO + "😅 Ups, eso no fue un número válido. ¡Intenta de nuevo!" + RESET);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para buscar libros (ej es, en, fr):");
        var idioma = teclado.nextLine().toLowerCase();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println(AMARILLO + "⚠️ No se encontraron libros registrados en el idioma " + idioma + "." + RESET);
        } else  {
            System.out.println(CYAN + "\n📚 Libros en idioma " + idioma + RESET);
            System.out.println(MORADO + "-----------------------------------------" + RESET);

            librosPorIdioma.forEach(l ->
                    System.out.println(VERDE + "📖 Título: " + l.getTitulo() +
                            " | ✍️ Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido") +
                            " | 📥 Descargas: " + l.getNumeroDescarga() + RESET)
            );

            System.out.println(MORADO + "-----------------------------------------\n" + RESET);
        }
    }

    private void mostrarEstadisticasPorIdioma() {
        librosGuardados = libroRepository.findAll();
        if (librosGuardados.isEmpty()) {
            System.out.println(CYAN + "😅 Aún no hay libros guardados para mostrar estadísticas. ¡Agrega alguno y lo intentamos de nuevo!" + RESET);
            return;
        }

        var estadisticasPorIdioma = librosGuardados.stream()
                .collect(Collectors.groupingBy(Libro::getIdioma,
                        Collectors.summarizingDouble(Libro::getNumeroDescarga)));

        System.out.println(CYAN + "\n📈 Estadísticas de Descargas por Idioma" + RESET);

        if (estadisticasPorIdioma.isEmpty()) {
            System.out.println(AMARILLO + "⚠️ No se encontraron datos de idiomas en los libros registrados." + RESET);
        } else {
            estadisticasPorIdioma.forEach((idioma, stats) -> {
                System.out.println(MORADO + "🌐 Idioma: " + idioma.toUpperCase() + RESET);
                System.out.println(VERDE + "  📚 Cantidad de Libros: " + stats.getCount() + RESET);
                System.out.println(CYAN + "  📊 Promedio de Descargas: " + String.format("%.2f", stats.getAverage()) + RESET);
                System.out.println(AMARILLO + "  📉 Mínimo de Descargas: " + (int) stats.getMin() + RESET);
                System.out.println(ROJO + "  📈 Máximo de Descargas: " + (int) stats.getMax() + RESET);
                System.out.println(MORADO + "------------------------------------------" + RESET);
            });
        }

        System.out.println(MORADO + "------------------------------------------------------\n" + RESET);
    }

    private void mostrarTop10LibrosMasDescargados() {
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDescargaDesc();

        if (top10Libros.isEmpty()) {
            System.out.println(CYAN + "😅 Ups... no hay libros cargados para generar el Top 10. ¡Agrega alguno y volvemos a intentarlo!" + RESET);
            return;
        }

        System.out.println(CYAN + "\n🏆 Top 10 Libros Más Descargados" + RESET);
        System.out.println(MORADO + "---------------------------------------------" + RESET);

        top10Libros.forEach(l ->
                System.out.println(VERDE + "📖 Título: " + l.getTitulo() +
                        " | ✍️ Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido") +
                        " | 📥 Descargas: " + l.getNumeroDescarga() + RESET)
        );

        System.out.println(MORADO + "---------------------------------------------\n" + RESET);
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor a buscar");
        var nombreAutor = teclado.nextLine();

        Optional<Autor> autorEncontrado = autorRepository.findByNombreContainingIgnoreCase(nombreAutor);

        if (autorEncontrado.isPresent()) {
            Autor autor = autorEncontrado.get();
            System.out.println(AZUL + "\n🖋️ --- Autor Encontrado ---" + RESET);
            System.out.println(CYAN + "👤 Nombre: " + RESET + autor.getNombre());
            System.out.println(CYAN + "🎂 Año de Nacimiento: " + RESET + autor.getAnoNacimiento());
            System.out.println(CYAN + "🕯️ Año de Fallecimiento: " + RESET +
                    (autor.getAnoFallecimiento() != null ? autor.getAnoFallecimiento() : "N/A"));

            System.out.println(CYAN + "📚 Libros escritos:" + RESET);
            if (autor.getLibros() != null && !autor.getLibros().isEmpty()) {
                autor.getLibros().forEach(libro ->
                        System.out.println(VERDE + "  📖 - " + libro.getTitulo() + RESET));
            } else {
                System.out.println(AMARILLO + "  ⚠️ No hay libros asociados a este autor en la base de datos aún" + RESET);
            }
            System.out.println(MORADO + "------------------------\n" + RESET);
        } else {
            System.out.println(ROJO + "❌ No se encontró ningún autor con ese nombre en la base de datos." + RESET);
        }
    }

    private void listarAutoresNacidosDespuesDeAno() {
        System.out.println("Ingrese el año para buscar autores nacidos depues de:");
        try {
            var ano = Integer.parseInt(teclado.nextLine());

            List<Autor> autores = autorRepository.findByAnoNacimientoGreaterThan(ano);

            if (autores.isEmpty()) {
                System.out.println(AMARILLO + "⚠️ No se encontraron autores nacidos después del año " + ano + "." + RESET);
            } else {
                System.out.println(CYAN + "\n📖 --- Autores nacidos después del año " + ano + " ---" + RESET);
                autores.forEach(a -> System.out.println(MORADO + "👤 Nombre: " + a.getNombre() +
                        " | 🎂 Nacimiento: " + a.getAnoNacimiento() +
                        " | 🕯️ Fallecimiento: " + (a.getAnoFallecimiento() != null ? a.getAnoFallecimiento() : "N/A") + RESET));
                System.out.println(CYAN + "------------------------------------------------------------\n" + RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println(ROJO + "❌ Entrada inválida. Por favor ingrese un número válido." + RESET);
        }
    }
}
