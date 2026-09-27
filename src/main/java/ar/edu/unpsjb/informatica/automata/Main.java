package ar.edu.unpsjb.informatica.automata;

import java.util.HashSet;
import java.util.Set;

import ar.edu.unpsjb.informatica.automata.model.Automaton;
import ar.edu.unpsjb.informatica.automata.model.State;
import ar.edu.unpsjb.informatica.io.AutomatonLoader;
import ar.edu.unpsjb.informatica.io.dto.AutomatonDTO;
import ar.edu.unpsjb.informatica.io.dto.TransitionDTO;
import ar.edu.unpsjb.informatica.io.exception.AutomatonReadException;

public class Main {
    private static final AutomatonLoader loader = new AutomatonLoader();

    public static void main(String[] args) {
        String inputPath = (args.length > 0 && !args[0].isBlank()) ? args[0] : null;

        AutomatonDTO automatonDTO = null;
        try {
            automatonDTO = loader.load(inputPath);
            System.out.println("Autómata cargado exitosamente:");
            // System.out.println(automatonDTO);
        } catch (AutomatonReadException e) {
            System.err.println("Error al cargar el autómata: " + e.getMessage());
            System.exit(1);
        }

        Set<State> states = new HashSet<>();
        Set<State> finalStates = new HashSet<>();
        State initialState = null;
        for (String stateDTO : automatonDTO.states()) {
            State state = new State(stateDTO);
            states.add(state);
            if (automatonDTO.acceptingStates().contains(stateDTO)) {
                finalStates.add(state);
            }
            if (automatonDTO.initialState().equals(stateDTO)) {
                initialState = state;
            }
        }

        Automaton automaton = new Automaton(states, automatonDTO.alphabet(), initialState,
                finalStates);
        for (TransitionDTO transtionDTO : automatonDTO.transitions()) {
            State from = states.stream().filter(state -> state.getName().equals(transtionDTO.from())).findFirst().get();
            Character symbol = transtionDTO.symbol();
            for (String transitionState : transtionDTO.to()) {
                State to = states.stream().filter(state -> state.getName().equals(transitionState)).findFirst().get();
                automaton.addTransition(from, symbol, to);
            }
        }

        String cadena = "abaaa";
        automaton.toAfd();
    }
}