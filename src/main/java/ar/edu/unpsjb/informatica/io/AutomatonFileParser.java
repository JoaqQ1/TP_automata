package ar.edu.unpsjb.informatica.io;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import ar.edu.unpsjb.informatica.io.dto.AutomatonDTO;

public class AutomatonFileParser {
    private final ObjectMapper jsonMapper;
    private final XmlMapper xmlMapper;

    public AutomatonFileParser() {
        this.jsonMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();

        // Formato prolijo para las salidas
        this.jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public AutomatonDTO read(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("El archivo vino nulo");
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".xml")) {
            return xmlMapper.readValue(file, AutomatonDTO.class);
        } else if (fileName.endsWith(".json")) {
            return jsonMapper.readValue(file, AutomatonDTO.class);
        } else {
            throw new IllegalArgumentException("Formato no soportado (debe ser .json o .xml): " + fileName);
        }
    }

    public void write(File file, AutomatonDTO dto) throws IOException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".xml")) {
            xmlMapper.writeValue(file, dto);
        } else if (fileName.endsWith(".json")) {
            jsonMapper.writeValue(file, dto);
        } else {
            throw new IllegalArgumentException("Formato no soportado (debe ser .json o .xml): " + fileName);
        }
    }

}
