package ar.edu.unpsjb.informatica.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import ar.edu.unpsjb.informatica.io.dto.AutomatonDTO;
import ar.edu.unpsjb.informatica.io.exception.AutomatonReadException;

public class AutomatonLoader {

    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    private static final String DEFAULT_INPUT_PATH = "data/input/automata.json";

    private final AutomatonFileParser parser;

    public AutomatonLoader() {
        this(new AutomatonFileParser());
    }

    public AutomatonLoader(AutomatonFileParser parser) {
        this.parser = parser;
    }

    /**
     * Carga el autómata desde la ruta provista. Si la ruta es nula o vacía,
     * la busca en config.properties o toma la ruta por defecto.
     */
    public AutomatonDTO load(String inputPath) {
        String effectivePath = (inputPath != null && !inputPath.isBlank()) 
                ? inputPath 
                : loadConfigPath();

        return loadFromFile(new File(effectivePath));
    }

    /**
     * Carga el autómata desde un objeto File específico (ideal para pruebas unitarias).
     */
    public AutomatonDTO loadFromFile(File file) {
        try {
            return parser.read(file);
        } catch (StreamReadException e) {
            throw new AutomatonReadException("Error de sintaxis en el archivo (" + file.getName() + "): " + e.getMessage(), e);
        } catch (DatabindException e) {
            throw new AutomatonReadException("Error de estructura en los datos del autómata: " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new AutomatonReadException("No se encontró el archivo en la ruta: " + file.getAbsolutePath(), e);
        } catch (IllegalArgumentException e) {
            throw new AutomatonReadException("Formato no soportado para el archivo '" + file.getName() + "': " + e.getMessage(), e);
        } catch (IOException e) {
            throw new AutomatonReadException("Error de E/S al leer el archivo '" + file.getName() + "': " + e.getMessage(), e);
        } catch (Exception e) {
            throw new AutomatonReadException("Error inesperado al procesar el archivo: " + e.getMessage(), e);
        }
    }

    private String loadConfigPath() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
            if (in != null) {
                props.load(in);
                return props.getProperty("input.automaton.path", DEFAULT_INPUT_PATH);
            }
        } catch (IOException e) {
            System.err.println("Aviso: No se pudo cargar " + DEFAULT_CONFIG_FILE + ", usando ruta por defecto.");
        }
        return DEFAULT_INPUT_PATH;
    }
}
