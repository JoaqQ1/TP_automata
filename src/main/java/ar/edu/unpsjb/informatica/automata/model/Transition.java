package ar.edu.unpsjb.informatica.automata.model;

import java.util.HashSet;
import java.util.Set;

public class Transition {
    private State from;
    private Character symbol;
    private Set<State> to;

    public Transition() {
        this.to = new HashSet<State>();
    }

    public Transition(State from, Character symbol, Set<State> to) {
        this();
        this.from = from;
        this.symbol = symbol;
        this.to = to;
    }

    public State getFrom() {
        return from;
    }

    public void setFrom(State from) {
        this.from = from;
    }

    public Character getSymbol() {
        return symbol;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    public Set<State> getTo() {
        return to;
    }

    public void setTo(Set<State> to) {
        this.to = to;
    }

}
