package ar.edu.unpsjb.informatica.automata.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AutomatonTest {

    @Test
    void shouldBeDeterministicWhenThereIsAtMostOneDestinationPerSymbol() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State destination = new State("q1");

        automaton.addTransition(initial, 'a', destination);

        assertTrue(automaton.isDeterministic());
    }

    @Test
    void shouldNotBeDeterministicWhenThereIsAnEpsilonTransition() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State destination = new State("q1");

        automaton.addTransition(initial, Automaton.EPSILON, destination);

        assertFalse(automaton.isDeterministic());
    }

    @Test
    void shouldNotBeDeterministicWhenThereAreMultipleDestinationsForTheSameSymbol() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State firstDestination = new State("q1");
        State secondDestination = new State("q2");

        automaton.addTransition(initial, 'a', firstDestination);
        automaton.addTransition(initial, 'a', secondDestination);

        assertFalse(automaton.isDeterministic());
    }

    @Test
    void shouldAcceptAStringThatBelongsToTheAutomatonLanguage() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State accepting = new State("q1");

        automaton.setInitialState(initial);
        automaton.addAcceptingState(accepting);
        automaton.addTransition(initial, 'a', accepting);

        assertTrue(automaton.accepts("a"));
    }

    @Test
    void shouldRejectAStringThatDoesNotBelongToTheAutomatonLanguage() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State accepting = new State("q1");

        automaton.setInitialState(initial);
        automaton.addAcceptingState(accepting);
        automaton.addTransition(initial, 'a', accepting);

        assertFalse(automaton.accepts("b"));
    }

    @Test
    void shouldAcceptAnEmptyStringWhenTheInitialStateIsAccepting() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");

        automaton.setInitialState(initial);
        automaton.addAcceptingState(initial);

        assertTrue(automaton.accepts(""));
    }

    @Test
    void shouldFollowEpsilonTransitionsBeforeConsumingTheString() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State intermediate = new State("q1");
        State accepting = new State("q2");

        automaton.setInitialState(initial);
        automaton.addAcceptingState(accepting);
        automaton.addTransition(initial, Automaton.EPSILON, intermediate);
        automaton.addTransition(intermediate, 'a', accepting);

        assertTrue(automaton.accepts("a"));
    }

    @Test
    void fromAfndToAfd() {
        Automaton automaton = new Automaton();
        State initial = new State("q0");
        State intermediate = new State("q1");
        State accepting = new State("q2");

        automaton.setInitialState(initial);
        automaton.addAcceptingState(accepting);
        automaton.addTransition(initial, Automaton.EPSILON, intermediate);
        automaton.addTransition(intermediate, 'a', accepting);

        assertFalse(automaton.isDeterministic());
        Automaton afn = automaton.toAfn();
        assertTrue(automaton.isDeterministic());

    }
}