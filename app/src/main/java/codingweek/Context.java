package codingweek;

import java.util.ArrayList;

public class Context {

    private Strategy strategy;

    public void setStrategy(Strategy strategy){
        this.strategy = strategy;
    }

    public Card execStrategy(ArrayList<Card> cards, int currentCard,ArrayList<Integer> frequency){
        Card res = this.strategy.execute(cards, currentCard,frequency);
        return res;
    }

}
