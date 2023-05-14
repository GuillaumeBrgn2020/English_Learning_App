package codingweek;

import java.util.ArrayList;

public interface Strategy {
    
    public abstract Card execute(ArrayList<Card> cards, int currentCard,ArrayList<Integer> frequency);

}
