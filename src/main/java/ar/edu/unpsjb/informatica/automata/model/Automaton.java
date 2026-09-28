package ar.edu.unpsjb.informatica.automata.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
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

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<Character> alphabet) {
        this.alphabet = alphabet;
    }

    public State getInitialState() {
        return initialState;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<State> finalStates) {
        this.finalStates = finalStates;
    }

    public Map<State, Map<Character, Transition>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<State, Map<Character, Transition>> transitions) {
        this.transitions = transitions;
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
        System.out.println("\n=== INICIO DE EVALUACIÓN: \"" + chain + "\" ===");
        boolean result = transition(initialState, chain, 0);
        System.out.println("=== FIN DE EVALUACIÓN -> Resultado: " + result + " ===\n");
        return result;
    }

    private boolean transition(State currentState, String chain, int index) {
        // Caso base: llegamos al final de la cadena y estamos en un estado final
        if (index == chain.length() && currentState.isFinalState()) {
            System.out.println("-> ¡ACEPTADO! Llegamos al final de la cadena en el estado final: " + currentState);
            return true;
        }

        // 1. Explorar transiciones épsilon (si existen)
        Transition epsilonTransition = getTransition(currentState, EPSILON);
        if (epsilonTransition != null) {
            for (State epsilonDestination : epsilonTransition.getTo()) {
                System.out.println("[ε-TRANSICIÓN] De " + currentState + " -> " + epsilonDestination + " (sin consumir caracteres)");
                if (transition(epsilonDestination, chain, index)) {
                    return true;
                }
            }
        }

        // Si ya terminamos la cadena pero no estamos en un estado final
        if (index == chain.length()) {
            return false; // Silencioso para no saturar si hay múltiples caminos muertos
        }

        char currentChar = chain.charAt(index);
        
        // 2. Explorar transiciones con el símbolo actual de la cadena
        Transition symbolTransition = getTransition(currentState, currentChar);
        if (symbolTransition != null) {
            for (State destination : symbolTransition.getTo()) {
                System.out.println("[TRANSICIÓN] De " + currentState + " -> " + destination + " leyendo '" + currentChar + "' (posición " + index + ")");
                if (transition(destination, chain, index + 1)) {
                    return true;
                }
            }
        } else {
            // Opcional: Descomentar si quieres ver cuando un camino se bloquea por completo
            // System.out.println("[BLOQUEADO] No hay transición desde " + currentState + " con el símbolo '" + currentChar + "'");
        }

        return false;
    }
    public Automaton toAfd() {
        if (isDeterministic())
            return this;

        Automaton afd = new Automaton();

        // Agrego el alfabeto y el estado inicial
        afd.setAlphabet(new HashSet<>(this.alphabet));
        afd.setInitialState(this.initialState);

        Set<State> initialSet = new HashSet<>();
        initialSet.add(this.initialState);

        Map<Set<State>, State> unmarkedStatesMap = new HashMap<>();
        Queue<Set<State>> queue = new LinkedList<>();

        unmarkedStatesMap.put(initialSet, this.initialState);
        queue.add(initialSet);

        while (!queue.isEmpty()) {
            Set<State> currentSet = queue.poll();
            State currentAfdState = unmarkedStatesMap.get(currentSet);

            // Verificamos si es un estado final en el AFD
            // (es final si al menos uno de los estados del subconjunto es final en el AFN)
            for (State s : currentSet) {
                if (s.isFinalState()) {
                    afd.addAcceptingState(currentAfdState);
                    break;
                }
            }

            // Para cada símbolo del alfabeto, calculamos el conjunto de destino
            for (Character symbol : this.alphabet) {
                Set<State> nextSet = new HashSet<>();

                // Calculamos a dónde vamos desde todo el conjunto actual con este símbolo
                for (State s : currentSet) {

                    Transition destinations = getTransition(s, symbol);
                    if (destinations != null) {
                        nextSet.addAll(destinations.getTo());
                    }
                }

                if (!nextSet.isEmpty()) {
                    // Obtenemos o creamos el estado AFD correspondiente al set resultante
                    State nextAfdState = unmarkedStatesMap.get(nextSet);
                    if (nextAfdState == null) {
                        nextAfdState = createStateFromSet(nextSet);
                        unmarkedStatesMap.put(nextSet, nextAfdState);
                        queue.add(nextSet);
                    }

                    // Agregamos la transición al nuevo AFD
                    afd.addTransition(currentAfdState, symbol, nextAfdState);
                }
            }
        }

        return afd;
    }

    /**
     * Método auxiliar para crear un nombre/estado único a partir de un conjunto de
     * estados del AFN.
     */
    private State createStateFromSet(Set<State> stateSet) {
        if (stateSet.size() == 1) {
            return stateSet.iterator().next();
        }
        // Si son varios, los combinamos ordenadamente por nombre/toString
        StringBuilder sb = new StringBuilder();
        stateSet.stream()
                .sorted(Comparator.comparing(Object::toString))
                .forEach(s -> sb.append(s.toString()).append("_"));

        String compositeName = sb.substring(0, sb.length() - 1);
        return new State(compositeName);
    }
}
