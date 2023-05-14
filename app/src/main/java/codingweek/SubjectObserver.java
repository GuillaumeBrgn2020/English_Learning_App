package codingweek;

import java.util.List;
import java.util.ArrayList;

public class SubjectObserver {

    private transient List<Observer> observers = new ArrayList<>();

    public void addObserve(Observer o) {
        this.observers.add(o);
    }

    public void notifyObserve() {
        for (Observer o : this.observers) {
            o.update();
        }
    }

    public void rmvObserve(Observer o){
        this.observers.remove(o);
    }

    public void clear() {
        this.observers.clear() ;
    }

}