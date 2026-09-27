package ar.edu.unpsjb.informatica.automata.model;

import java.util.Objects;

public class State implements Comparable<State> {
    private String name;
    private boolean finalState;

    public State(String name) {
        this.name = name;
        this.finalState = false;
    }

    public String getName() {
        return name;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof State))
            return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(State o) {
        return this.name.compareTo(o.name);
    }
}