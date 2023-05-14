package codingweek;

import java.util.ArrayList;
import java.util.Collections;

public class StackCards extends SubjectObserver implements Comparable<StackCards> {

    // Attributs
    private String name;
    private String description;
    private ArrayList<Card> cards;
    
    // LearningView
    private int currentCard; // Carte actuelle de l'apprentissage
    private String orderMethod; // Méthode d'apprentissage
    private ArrayList<Integer> frequency;
    private int maxVictory;
    private boolean answerFirst;

    // Stats
    private int nbCorrect; // Nombre de fois où la pile a été correctement répondu
    

    // Constructeur
    public StackCards(String name, String description) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.description = description.substring(0, 1).toUpperCase() + description.substring(1);
        this.cards = new ArrayList<Card>();
        this.nbCorrect = 0;
        this.currentCard = 0;
        this.orderMethod = "classique";
        this.frequency = new ArrayList<Integer>();
        setFrequency("100");
        this.answerFirst = false ;
    }

    public StackCards() {
        super() ;
        this.frequency = new ArrayList<Integer>() ;
    }

    public void changeStackCards(String name, String description) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.description = description.substring(0, 1).toUpperCase() + description.substring(1);
        this.cards = new ArrayList<Card>();
        this.nbCorrect = 0;
        this.currentCard = 0;
        this.orderMethod = "classique";
        this.frequency = new ArrayList<Integer>();
        setFrequency("100");
        this.answerFirst = false ;
    }


    // Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public int getNbCorrect() {
        return this.nbCorrect;
    }

    public int getNbCards() {
        return this.cards.size();
    }

    public int getCurrentCard(){
        return this.currentCard;
    }

    /* Compte le nombre de cartes complétés dans la pile */
    public int getNbCardsCompleted(){
        int res = 0;
        for(Card card : cards){
            if(card.isCompleted()){
                res++;
            }
        }
        return res;
    }


    public int getNbCardCorrect(){
        int res = 0;
        for(Card card : cards){
            res += card.getNbCorrect();
        }
        return res;
    }

    public String getOrderMethod(){
        return this.orderMethod;
    }

    public ArrayList<Integer> getFrequency(){
        return this.frequency;
    }

    public int getMaxVictory(){
        return this.maxVictory;
    }

    public boolean getAnswerFirst(){
        return this.answerFirst;
    }


    // Setters

    public void setName(String name) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.notifyObserve() ;
    }

    public void setDescription(String description) {
        this.description = description.substring(0, 1).toUpperCase() + description.substring(1);
        this.notifyObserve() ;
    }
    
    public void setNbCorrect(int i) {
        this.nbCorrect = i ;
        this.notifyObserve() ;
    }

    public void setCurrentCard(int value){
        this.currentCard = value;
    }

    public void setOrderMethodR(){
        this.orderMethod = "random";
        this.notifyObserve() ;
    }

    public void setOrderMethodA(){
        this.orderMethod = "A-Z";
        this.notifyObserve() ;
    }

    public void setOrderMethodC(){
        this.orderMethod = "classique";
        this.notifyObserve() ;
    }

    public void setMaxVictory(int value){
        this.maxVictory = value;
        this.notifyObserve() ;
    }

    public void setFrequency(String freq){
        String[] frequencies = freq.split("/");
        resetFrequency();
        for(int i=0;i<frequencies.length;i++){
            this.frequency.add(Integer.parseInt(frequencies[i]));
        }
        setMaxVictory(this.frequency.size());
    }
    
    public void setAnswerFirst(boolean value){
        this.answerFirst = value;
    }

    // Méthodes

    public void addCard(Card card) {
        this.cards.add(card);
        this.notifyObserve() ;
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
        this.notifyObserve() ;
    }

    public boolean isCompleted(){
        if(getNbCardsCompleted()==getNbCards()){
            return true;
        }
        return false;
    }

    public void increaseCorrectStackCards(){
        this.nbCorrect++;
    }

    /* Réinitialise l'attribut qOrA de toutes les cartes */
    public void resetQorA(){
        for(Card card : this.cards){
            card.setQorA(0);
        }
    }

    /* Incrémente pour la carte actuelle l'attribut correct */
    public void increaseCorrect(ArrayList<Card> cards){
        Card card = cards.get(this.currentCard);
        
        card.incrementAppear();
        card.incrementCorrect();
        card.incrementCorrectConsec();

        if(card.getNbCorrectConsecutive() >= getMaxVictory()){
            if(cards.size()!=1){
                cards.remove(card);
                this.currentCard--;
            }
            else{
                setCurrentCard(-1);
                notifyObserve();
                return;
            }
        }

        if(this.currentCard==cards.size()-1){
            if(this.orderMethod.equals("random")){
                Collections.shuffle(cards);
            }
            setCurrentCard(0);
        }
        else{
            this.currentCard++;
        }
        
        resetQorA();
        learningAlgorithm(cards, this.currentCard);
        
    
    }

    /* Incrémente pour la carte actuelle l'attribut "faux" */
    public void increaseFalse(ArrayList<Card> cards){
        Card card = cards.get(this.currentCard);

        card.incrementAppear();
        card.resetCorrectConsec();
        
        if(this.currentCard==cards.size()-1){
            if(this.orderMethod.equals("random")){
                Collections.shuffle(cards);
                System.out.println("HEYYYY");
            }
            setCurrentCard(0);
        }
        else{
            this.currentCard++;
        }

        resetQorA();
        learningAlgorithm(cards, this.currentCard);
    }

    public int getNbAppear(){
        int res = 0;
        for(Card card : this.cards){
            res += card.getNbAppear();
        }
        return res;
    }


    public String toString(){
        String res = "";
        res += "Name : " + this.name + "\n";
        res += "Description : " + this.description + "\n";
        res += "Nb cards : " + this.cards.size() + "\n";
        res += "Nb correct : " + this.nbCorrect + "\n";
        for (Card card : this.cards) {
            res += card.toString() + "\n";
        }
        return res;
    }

    public int getPercentage(){
        if (getNbAppear() == 0){
            return 0;
        }
        return (int) (getNbCardCorrect() * 100)/getNbAppear();
    }

    public void resetStatistics() {
        for (Card card : this.cards) {
            card.resetCorrectConsec() ;
        }
    }

    public void resetCorrectConsec(){
        for(Card card : cards){
            card.resetCorrectConsec();
        }
    }

    public void resetFrequency(){
        this.frequency.clear();
    }

    public void learningAlgorithm(ArrayList<Card> cards, int currentCard){
        /*Algorithme de système de leitner sur maximum 3 boîtes */
        Context context = new Context();
        int nbBox = this.frequency.size();        

        if(nbBox==3){
            context.setStrategy(new FreqStrategy3Box());
        }
        else if(nbBox==2){
            context.setStrategy(new FreqStrategy2Box());
        }
        else{
            context.setStrategy(new FreqStrategy1Box());
        }
        if(cards.size()!=0){
            Card card = context.execStrategy(cards, currentCard,this.frequency);
            setCurrentCard(cards.indexOf(card));
        }

        notifyObserve();
    }
    
    public Card find(String question) {
        for (Card card : this.cards) {
            if (card.getQuestion().toLowerCase().equals(question.toLowerCase()) || card.getQuestion().toLowerCase() == question.toLowerCase()) {
                return card ;
            }
        }
        return null ;
    }

    @Override
    public int compareTo(StackCards s) {
        if (this.getName().compareTo(s.getName()) > 0) {
            return 1;
        } else if (this.getName().compareTo(s.getName()) < 0) {
            return -1;
        }
        return 0;
    }

    public void sort() {
        Collections.sort(this.cards) ;
    }
}