package codingweek;

public class Card extends SubjectObserver implements Comparable<Card> {

    // Attributs
    private String question;
    private String answer;
    //LearningView
    private int qOrA; //Indique si la carte actuelle affiche la question ou la réponse (0 = question)
    // Stats
    private int nbCorrect; // Nombre de fois où la carte a été correctement répondu
    private int nbCorrectConsecutive; // Nombre de fois où la carte a été correctement répondu de suite
    private int nbAppear; // Nombre de fois où la carte a été affichée


    // Constructeur
    public Card(String question, String answer) {
        this.question = question.substring(0, 1).toUpperCase() + question.substring(1);
        this.answer = answer.substring(0, 1).toUpperCase() + answer.substring(1);
        this.nbCorrect = 0;
        this.nbCorrectConsecutive = 0;
        this.nbAppear = 0;
        this.qOrA = 0;
    }

    public Card(){
        super() ;
        this.question = "";
        this.answer = "";
        this.nbCorrect = 0;
        this.nbCorrectConsecutive = 0;
        this.nbAppear = 0;
        this.qOrA = 0;
    }

    public void changeCard(String question, String answer) {
        this.question = question.substring(0, 1).toUpperCase() + question.substring(1);
        this.answer = answer.substring(0, 1).toUpperCase() + answer.substring(1);
        this.nbCorrect = 0;
        this.nbCorrectConsecutive = 0;
        this.nbAppear = 0;
        this.qOrA = 0;
        this.notifyObserve() ;
    }


    // Getters
    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public int getNbCorrect() {
        return this.nbCorrect;
    }

    public int getNbCorrectConsecutive() {
        return this.nbCorrectConsecutive;
    }

    public int getNbAppear() {
        return this.nbAppear;
    }

    public int getQOrA(){
        return this.qOrA;
    }

    // Setters

    public void setQuestion(String question) {
        this.question = question.substring(0, 1).toUpperCase() + question.substring(1);
        this.notifyObserve() ;
    }

    public void setAnswer(String answer) {
        this.answer = answer.substring(0, 1).toUpperCase() + answer.substring(1);
        this.notifyObserve() ;
    }

    public void setQorA(int value){
        this.qOrA = value;
    }

    public void setNbCorrect(int value){
        this.nbCorrect = value;
        this.notifyObserve() ;
    }

    public void setNbCorrectConsecutive(int value){
        this.nbCorrectConsecutive = value;
        this.notifyObserve() ;
    }

    public void setNbAppear(int value){
        this.nbAppear = value;
        this.notifyObserve() ;
    }

    // Méthodes

    public boolean isCompleted(){ //Might be necessary to modif if condition
        if(this.nbCorrectConsecutive>=2){
            return true;
        }
        return false;
    }
    
    public void incrementCorrect(){
        this.nbCorrect++;
    }

    public void incrementAppear(){
        this.nbAppear++;
    }

    public void incrementCorrectConsec(){
        this.nbCorrectConsecutive++;
    }

    public void resetCorrectConsec(){
        this.nbCorrectConsecutive=0;
    }

    public void switchQOrA(){
        this.qOrA = 1-this.qOrA;
    }

    public String toString(){
        return "Question : " + this.question + " Reponse : " + this.answer;
    }

    @Override
    public int compareTo(Card c) {
        if (this.getQuestion().compareTo(c.getQuestion()) > 0) {
            return 1;
        } else if (this.getQuestion().compareTo(c.getQuestion()) < 0) {
            return -1;
        }
        return 0;
    }

}
