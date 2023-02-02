package test.urm;

import java.io.Serializable;



/**
 * @author davidba
 * @created 21/5/2014 (copied from MDEditor project by shirar)
 */
public class Pair<F, S> implements Serializable {

    private F o1;
    private S o2;

    public Pair(F o1, S o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public Pair() {
    }

    public F getFirst() {
        return o1;
    }

    public S getSecond() {
        return o2;
    }

    public void setFirst(F o) {
        o1 = o;
    }

    public void setSecond(S o) {
        o2 = o;
    }

    @Override
    public String toString() {
        return "Pair{" + o1 + ", " + o2 + "}";
    }
}