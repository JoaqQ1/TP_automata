package ar.edu.unpsjb.informatica.io.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;
import java.util.Set;

@JacksonXmlRootElement(localName = "automaton")
public record AutomatonDTO(
        @JacksonXmlElementWrapper(localName = "alphabet") @JacksonXmlProperty(localName = "symbol") Set<Character> alphabet,

        @JacksonXmlElementWrapper(localName = "states") @JacksonXmlProperty(localName = "state") Set<String> states,

        String initialState,

        @JacksonXmlElementWrapper(localName = "acceptingStates") @JacksonXmlProperty(localName = "state") Set<String> acceptingStates,

        @JacksonXmlElementWrapper(localName = "transitions") @JacksonXmlProperty(localName = "transition") List<TransitionDTO> transitions) {
}