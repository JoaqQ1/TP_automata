package ar.edu.unpsjb.informatica.automata.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Automaton {

    public static final Character EPSILON = 'ε';
    private Set<State> states;
    private Set<Character> alphabet;
    private State initialState;

    private Set<State> finalStates;
    private Map<State, Map<Character, Transition>> transitions;

    public Automaton() {
        this.states = new HashSet<State>();
        this.alphabet = new HashSet<Character>();
        this.finalStates = new HashSet<State>();
        this.transitions = new HashMap<>();
    }

    public Automaton(Set<State> states, Set<Character> alphabet, State initialState, Set<State> finalStates) {
        this();
        this.states.addAll(states);
        this.alphabet.addAll(alphabet);
        this.initialState = initialState;
        finalStates.forEach(this::addAcceptingState);
    }

    public void addState(State aState) {
        if (aState == null)
            throw new IllegalArgumentException("El estado proporcionado es nulo");
        this.states.add(aState);
    }

    public void addAlphabetSymbol(Character aSymbol) {
        if (aSymbol == null)
            throw new IllegalArgumentException("El caracter propocionado es nulo");
        this.alphabet.add(aSymbol);
    }

    public void setInitialState(State aState) {
        if (aState == null)
            throw new IllegalArgumentException("El estado proporcionado es nulo");
        this.states.add(aState);
        this.initialState = aState;
    }

    public void addAcceptingState(State aState) {
        if (aState == null)
            throw new IllegalArgumentException("El estado proporcionado es nulo");
        this.states.add(aState);
        this.finalStates.add(aState);
        aState.setFinalState(true);
    }

    /**
     * Agrega una transición entre un estado origen y uno destino con un símbolo
     * dado.
     * 
     * @param from   estado origen
     * @param symbol símbolo
     * @param to     estado destino
     */
    public void addTransition(State from, Character symbol, State to) {

        if (from == null || symbol == null || to == null)
            throw new IllegalArgumentException("Argumentos invalidos");

        this.states.add(from);
        this.states.add(to);

        // Solo se agrega al alfabeto si no es una transición épsilon o nula
        if (symbol != null && !symbol.equals(EPSILON)) {
            this.alphabet.add(symbol);
        }

        Transition transition = getTransition(from, symbol);
        if (transition == null) {
            Map<Character, Transition> transitionsFrom = transitions.get(from);
            if (transitionsFrom == null) {
                transitionsFrom = new HashMap<>();
                transitions.put(from, transitionsFrom);
            }

            transition = new Transition(from, symbol, new HashSet<>());
            transitionsFrom.put(symbol, transition);
        }
        transition.getTo().add(to);
    }

    public Set<State> getTransitions(State from, Character symbol) {

        Map<Character, Transition> transitionsFrom = transitions.get(from);
        if (transitionsFrom == null) {
            return new HashSet<>();
        }

        Transition transition = transitionsFrom.get(symbol);
        if (transition == null)
            return new HashSet<>();

        return transition.getTo();
    }

    public Transition getTransition(State from, Character symbol) {
        if (from == null || symbol == null)
            throw new IllegalArgumentException("Argumentos invalidos, no se pudo obtener la transicion");
        Map<Character, Transition> transitionsFrom = transitions.get(from);
        return transitionsFrom == null ? null : transitionsFrom.get(symbol);
    }

    /**
     * Evalúa si el autómata actual cumple la propiedad de ser AFD:
     * - No tiene transiciones épsilon.
     * - Para cada estado y cada símbolo del alfabeto, hay a lo sumo 1 transición.
     */
    public boolean isDeterministic() {
        for (Map<Character, Transition> transition : transitions.values()) {
            if (transition.containsKey(EPSILON))
                return false;

            for (Transition trasition : transition.values()) {
                if (trasition.getTo().size() > 1)
                    return false;
            }
        }
        return true;
    }

    /**
     * Corrobora si la cadena pasada por parametro es aceptada por el automata.
     * 
     * @param chain Cadena a evaluar
     * @return Retornara true en caso de pertenecer al lenguaje aceptado por el
     *         automata o false en caso contrario
     */
    public boolean accepts(String chain) {
        if (chain == null || initialState == null)
            return false;
        // Quito todos los espacios
        chain = chain.replaceAll("\\s+", "");
        return transition(initialState, chain, 0);
    }

    private boolean transition(State currentState, String chain, int index) {
        if (index == chain.length() && currentState.isFinalState()) {
            return true;
        }

        Transition epsilonTransition = getTransition(currentState, EPSILON);
        if (epsilonTransition != null) {
            for (State epsilonDestination : epsilonTransition.getTo()) {
                if (transition(epsilonDestination, chain, index)) {
                    return true;
                }
            }
        }

        if (index == chain.length()) {
            return false;
        }

        Transition symbolTransition = getTransition(currentState, chain.charAt(index));
        if (symbolTransition != null) {
            for (State destination : symbolTransition.getTo()) {
                if (transition(destination, chain, index + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Automaton toAfd() {
        if (isDeterministic())
            return this;

        for (Map<Character, Transition> outgoingTransitions : transitions.values()) {
            for (Transition transition : outgoingTransitions.values()) {
                if (transition.getTo().size() > 1) {
                    StringBuilder stateNew = new StringBuilder();
                    for (State state : transition.getTo()) {
                        stateNew.append(state.toString());
                        stateNew.append("-");
                    }
                    String nameNewState = stateNew.substring(0, stateNew.length() - 1);
                }
            }
        }

        return null;
    }
}
