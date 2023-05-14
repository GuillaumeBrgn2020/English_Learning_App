package codingweek;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class FlashCard extends SubjectObserver {

    // Attributs
    private ArrayList<StackCards> stacksCards;

    // Constructeur
    public FlashCard() {
        this.stacksCards = new ArrayList<StackCards>();
    }


    // Getters

    public ArrayList<StackCards> getStacksCards() {
        return this.stacksCards;
    }


    // Setters

    public void setStacksCards(ArrayList<StackCards> stacksCards) {
        this.stacksCards = stacksCards;
        this.notifyObserve() ;
    }


    // Méthodes

    public void addStackCards(StackCards stackCards) {
        this.stacksCards.add(stackCards);
        this.notifyObserve() ;
    }

    public void removeStackCards(StackCards stackCards) {
        this.stacksCards.remove(stackCards);
        this.notifyObserve() ;
    }

    public int getNbStackCards() {
        return this.stacksCards.size();
    }

    public int getNbCards() {
        int nbCards = 0;
        for (StackCards stackCards : this.stacksCards) {
            nbCards += stackCards.getNbCards();
        }
        return nbCards;
    }

    public int getNbCorrectCards() {
        int nbCorrectCards = 0;
        for (StackCards stackCards : this.stacksCards) {
            nbCorrectCards += stackCards.getNbCardCorrect();
        }
        return nbCorrectCards;
    }

    public int getNbCardsAppear() {
        int nbCardsAppear = 0;
        for (StackCards stackCards : this.stacksCards) {
            nbCardsAppear += stackCards.getNbAppear();
        }
        return nbCardsAppear;
    }

    public int getNbCorrectStack(){
        int nbCorrectStack = 0;
        for (StackCards stackCards : this.stacksCards) {
            if (stackCards.getNbCorrect()!=0){
                nbCorrectStack++ ;
            }
        }
        return nbCorrectStack;
    }


    public void save(File file, ArrayList<StackCards> stacksCards) throws IOException {
        
        if (!file.getPath().endsWith(".json")) {
            file = new File(file.getPath() + ".json") ;
        }
        FileWriter myWriter = new FileWriter(file);
        Gson gson = new GsonBuilder().create();
        gson.toJson(stacksCards, myWriter);
        myWriter.close();
    }

    public String toString(){
        String result = "";
        for (StackCards stackCards : this.stacksCards) {
            result += stackCards.toString();
        }
        return result;
    }

    public StackCards find(String name) {
        for (StackCards stack : this.stacksCards) {
            if (stack.getName().toLowerCase().equals(name.toLowerCase()) || stack.getName().toLowerCase() == name.toLowerCase()) {
                return stack ;
            }
        }
        return null ;
    }

    public int getPercentage(){
        if (getNbCardsAppear() == 0){
            return 0;
        }
        else{
            return (int)(getNbCorrectCards() * 100) / getNbCardsAppear();
        }
    }
    
    public void sort() {
        Collections.sort(this.stacksCards) ;
    }
}
