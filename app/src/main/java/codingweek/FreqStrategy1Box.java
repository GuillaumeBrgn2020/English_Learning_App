package codingweek;

import java.util.ArrayList;
import java.util.Random;

public class FreqStrategy1Box implements Strategy{
    /*Stratégie relative à un seul essai par carte */
    @Override
    public Card execute(ArrayList<Card> cards, int currentCard, ArrayList<Integer> frequency) {
        
        Card card = cards.get(currentCard);

        int belongToBox = card.getNbCorrectConsecutive()+1;
        Random r = new Random();
        int randint = r.nextInt(100);

        if(belongToBox >= 1){
            if(randint<=frequency.get(0)){
                return card;
            }
        }

        if(currentCard==cards.size()-1){
            currentCard=0;
        }
        else{
            currentCard++;
        }
        return execute(cards,currentCard,frequency);
    }

}
