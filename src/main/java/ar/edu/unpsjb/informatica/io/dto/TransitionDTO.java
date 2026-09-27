package ar.edu.unpsjb.informatica.io.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Set;

public record TransitionDTO(
        String from,
        Character symbol,

        @JacksonXmlElementWrapper(localName = "to") @JacksonXmlProperty(localName = "target") Set<String> to) {

}