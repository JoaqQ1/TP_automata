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
    // delta: (Origen, Símbolo) -> Conjunto de Destinos
    private Map<State, Map<Character, Set<State>>> transitions;

    public Automaton() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    public Automaton(Set<State> states, Set<Character> alphabet, State initialState, Set<State> finalStates) {
        this();
        this.states.addAll(states);
        this.alphabet.addAll(alphabet);
        this.initialState = initialState;
        this.finalStates.addAll(finalStates);
        finalStates.forEach(state -> state.setFinalState(true));
    }

    public void addState(State aState) {
        if (aState == null)
            return;
        states.add(aState);
    }

    public void addAlphabetSymbol(Character c) {
        alphabet.add(c);
    }

    public void setInitialState(State aState) {
        this.states.add(aState);
        this.initialState = aState;
    }

    public void addAcceptingState(State aState) {
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

        if (from == null || to == null)
            throw new IllegalArgumentException("invalid state");

        this.states.add(from);
        this.states.add(to);

        // Solo se agrega al alfabeto si no es una transición épsilon o nula
        if (symbol != null && !symbol.equals(EPSILON)) {
            this.alphabet.add(symbol);
        }

        Map<Character, Set<State>> transitionsFrom = transitions.get(from);
        if (transitionsFrom == null) {
            transitionsFrom = new HashMap<>();
            this.transitions.put(from, transitionsFrom);
        }

        Set<State> transitionsStates = transitionsFrom.get(symbol);
        if (transitionsStates == null) {
            transitionsStates = new HashSet<State>();
            transitionsFrom.put(symbol, transitionsStates);
        }
        transitionsStates.add(to);
    }

    public Set<State> getTransitions(State from, Character symbol) {

        Map<Character, Set<State>> transitionsFrom = transitions.get(from);
        if (transitionsFrom == null) {
            return new HashSet<>();
        }

        Set<State> statesTransition = transitionsFrom.get(symbol);
        if (statesTransition == null)
            return new HashSet<>();

        return statesTransition;
    }

    /**
     * Evalúa si el autómata actual cumple la propiedad de ser AFD:
     * - No tiene transiciones épsilon.
     * - Para cada estado y cada símbolo del alfabeto, hay a lo sumo 1 transición.
     */
    public boolean isDeterministic() {
        for (Map<Character, Set<State>> transition : transitions.values()) {
            if (transition.containsKey(EPSILON))
                return false;

            for (Set<State> statesTransion : transition.values()) {
                if (statesTransion.size() > 1)
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

        for (State epsilonDestination : getTransitions(currentState, EPSILON)) {
            if (transition(epsilonDestination, chain, index)) {
                return true;
            }
        }

        if (index == chain.length()) {
            return false;
        }

        for (State destination : getTransitions(currentState, chain.charAt(index))) {
            if (transition(destination, chain, index + 1)) {
                return true;
            }
        }

        return false;
    }

    public Automaton toAfd() {
        if (isDeterministic())
            return this;

        for (Map<Character, Set<State>> transition : transitions.values()) {

            for (Set<State> statesTransion : transition.values()) {
                if (statesTransion.size() > 1) {
                    StringBuilder stateNew = new StringBuilder();
                    for (State state : statesTransion) {
                        stateNew.append(state.toString());
                        stateNew.append("-");
                    }
                    String nameNewState = stateNew.substring(0,stateNew.length() - 1);
                    System.out.println("El nuevo nombre es ");
                    System.out.println(nameNewState);
                }
            }
        }

        return null;
    }
}
