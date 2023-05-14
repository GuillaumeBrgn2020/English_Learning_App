package codingweek;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LearningViewController implements Initializable,Observer {
    
    /* Attributs FXML */
    @FXML  
    private Button questionAnswer;
    @FXML
    private Button falseAnswer;
    @FXML
    private Button correctAnswer; 
    @FXML
    private Label stackName;
    @FXML
    private Canvas time;
    @FXML
    private Label remainingQuestions;
    @FXML
    private Label remainingTime;

    /* Attributs autres */
    private FlashCard flashCard;
    private StackCards stackCards;
    private long compteur=0;
    private Image image;
    private ArrayList<Card> cards;
    private String orderMethod;
    private ArrayList<Integer> frequency;
    GraphicsContext gc;
	double largeur;
	double hauteur;
	double rayon;
	double lAiguille;
    private double tempsDeDepart=0;
    private double tempsCourant=0;
    private int duration = 30;


    /* Constructeur(s) */
    public LearningViewController(FlashCard flashCard, StackCards stackCards){
        this.flashCard =  flashCard;
        this.stackCards = stackCards;
        this.flashCard.clear() ;
        for (StackCards s : this.flashCard.getStacksCards()) {
            s.clear();
            for (Card c : s.getCards()) {
                c.clear() ;
            }
        }
        this.flashCard.addObserve(this) ;
        for (StackCards s : this.flashCard.getStacksCards()) {
            s.addObserve(this) ;
            for (Card c : s.getCards()) {
                c.addObserve(this) ;
            }
        }
    }


    /* Initialize & Update */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* Partie OrderMethod */
        /* La boucle for gère ici le cas des : cartes supprimés pendant l'apprentissage -> home -> reprise */
        this.cards = new ArrayList<>();
        this.orderMethod = this.stackCards.getOrderMethod();
        if (this.orderMethod.equals("classic")){
            for(Card card : this.stackCards.getCards()){
                if(card.getNbCorrectConsecutive() < this.stackCards.getMaxVictory()){
                    this.cards.add(card);
                }
            }
        }
        else if (this.orderMethod.equals("random")){
            for(Card card : this.stackCards.getCards()){
                if(card.getNbCorrectConsecutive() < this.stackCards.getMaxVictory()){
                    this.cards.add(card);
                }
            }
            Collections.shuffle(this.cards);
        }
        else if (this.orderMethod.equals("A-Z")){
            for(Card card : this.stackCards.getCards()){
                if(card.getNbCorrectConsecutive() < this.stackCards.getMaxVictory()){
                    this.cards.add(card);
                }
            }
            Collections.sort(this.cards);
        }

        /* Partie Frequency */ 
        this.frequency = this.stackCards.getFrequency();

       /* Partie Timer */
        URL imageFile=getClass().getResource("images/horloge.jpg");
        if (imageFile!=null) {
            this.image = new Image(imageFile.toString());
        }

        gc=time.getGraphicsContext2D();
		largeur=time.getWidth();
		hauteur=time.getHeight();
		rayon=largeur/2.5;
		lAiguille=rayon*4/5;
		gc.translate(largeur/2,hauteur/2);
        traceCadran(0);
        mouvement.start();

        /* If gère le cas : jeu(quelconque) -> home -> pile -> supp card -> reprise du jeu */
        int currentCard = this.stackCards.getCurrentCard();
        if(currentCard<this.cards.size()){
            this.stackCards.learningAlgorithm(this.cards,currentCard); //récupère l'état de la partie précédente si on l'a quitté
        }
        else{
            this.stackCards.learningAlgorithm(this.cards, 0);
        }
    }

    public void update(){
        
        if(this.cards.size()==0 || this.stackCards.getCurrentCard()==-1){
            mouvement.stop();
            handleEndOfStack();
        }

        else{

            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("images/validate.png")));
            image.setFitHeight(60.0);
            image.setFitWidth(60.0);
            this.correctAnswer.setGraphic(image);

            ImageView image1 = new ImageView(new Image(getClass().getResourceAsStream("images/cancel.png")));
            image1.setFitHeight(60.0);
            image1.setFitWidth(60.0);
            this.falseAnswer.setGraphic(image1);

            int currentCard = this.stackCards.getCurrentCard();
            Card card = this.cards.get(currentCard);
            
            this.stackName.setText(this.stackCards.getName());
            if (this.stackCards.getAnswerFirst()){
                this.questionAnswer.setText(card.getAnswer());
            }
            else{
                this.questionAnswer.setText(card.getQuestion());
            }
            int cpt = this.stackCards.getCards().size() - this.cards.size();
            this.remainingQuestions.setText("Validated : " + String.valueOf(cpt) + "/" + String.valueOf(this.stackCards.getCards().size()));
        }
    }

    /* Méthodes */
    private void traceCadran(double inclinaison){ 
        gc.drawImage(image,-largeur/2,-hauteur/2,largeur,hauteur);
        gc.setFill(Color.BLACK);
	    for (int i=0;i<30;i=i+1){
            gc.fillOval(rayon*Math.cos(i*Math.PI/5+Math.PI/2),rayon*Math.sin(i*Math.PI/5+Math.PI/2), 5, 5);
          }
              
          gc.setFont(new Font(20));
          gc.setTextAlign(TextAlignment.CENTER);
          gc.setTextBaseline(VPos.CENTER);
          gc.setFill(Color.BLACK);
          gc.fillText("0",0,-rayon-30);
          gc.fillText("5",0, rayon+30);
              
          gc.setStroke(Color.color(0.8,0.9,1));
          gc.setLineWidth(4);
          inclinaison=inclinaison-Math.PI/2;
          gc.strokeLine(0,0,lAiguille*Math.cos(inclinaison),lAiguille*Math.sin(inclinaison));	
        }
    
    
        AnimationTimer mouvement=new AnimationTimer(){
            @Override 
            public void handle(long now){
              if (tempsDeDepart==0){
                tempsDeDepart=now;
                tempsCourant=now;
              }
              if (now-tempsCourant>1e9){
                double secondes=Math.floor((now-tempsDeDepart)/1e9);
                double tempsRestant = 30.0-secondes;
                
                if(tempsRestant==-1.0){
                    remainingTime.setText("You have 30.0 seconds");
                }
                else{
                    remainingTime.setText("You have " + String.valueOf(tempsRestant) + " seconds");
                }

                if (secondes>30){
                    increaseFalse();
                }

                traceCadran(secondes*2*Math.PI/30);
                tempsCourant=now;
              }
            }
          }; 

    
    @FXML
    public void swapQA(){
        
        Card card = this.cards.get(this.stackCards.getCurrentCard());
        card.switchQOrA();
        if (this.stackCards.getAnswerFirst()){
            if(card.getQOrA()==0){
                String name = card.getAnswer();
                this.questionAnswer.setText(name);
            }
            else{
                String name = card.getQuestion();
                this.questionAnswer.setText(name);
            }
        }
        else{
            if(card.getQOrA()==1){
                String name = card.getAnswer();
                this.questionAnswer.setText(name);
            }
            else{
                String name = card.getQuestion();
                this.questionAnswer.setText(name);
            }
        }
        
    }

    @FXML
    public void increaseCorrect(){

        this.stackCards.increaseCorrect(cards);

        /* On gère le cadran */
        remainingTime.setText("You have 30.0 seconds");
        this.tempsDeDepart=0;
        this.tempsCourant=0;
        traceCadran(0);
    }

    @FXML
    public void increaseFalse(){

        this.stackCards.increaseFalse(cards);

        /* On gère le cadran */
        remainingTime.setText("you have 30.0 seconds");
        this.tempsDeDepart=0;
        this.tempsCourant=0;
        traceCadran(0);
    }

    public void handleEndOfStack(){

        //Reset pour la prochaine fois
        this.stackCards.resetQorA();
        this.stackCards.resetCorrectConsec();
        this.stackCards.setCurrentCard(0);

        //MAJ des attributs
        this.stackCards.increaseCorrectStackCards();
        
        //Pop-up de fin 
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("End of stack");
        alert.setHeaderText("End of stack");
        alert.setContentText("You have finished the stack !");
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/information.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
        alert.setOnHidden(evt -> {
            try {
                App.changeToStackView((Stage)this.stackName.getScene().getWindow(), this.flashCard, this.stackCards);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show();
    }


    /* Menu : Fichier */
    @FXML
    public void home() throws Exception {
        mouvement.stop();

        //Reset pour la prochaine fois
        this.stackCards.resetQorA();

        
        //Pop-up de fin 
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("You are leaving the learning page.");
        alert.setHeaderText("You will be redirected to the home page.");
        alert.setContentText("You will be redirected to the home page.");
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/information.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
        alert.setOnHidden(evt -> {
            try {
                App.changeToFirstPageView((Stage) this.stackName.getScene().getWindow(), this.flashCard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show(); 
    }
    
    @FXML
    public void exit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING) ;
        alert.setTitle("You will exit the application") ;
        alert.setContentText("Do you want to exit ?") ;
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/warning.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

        ButtonType yesButton = new ButtonType("Yes", ButtonData.YES) ;
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE) ;
        alert.getButtonTypes().setAll(yesButton, cancelButton) ;

        Optional<ButtonType> result = alert.showAndWait() ;
        if (result.isPresent()) {
            if (result.get().getButtonData() == ButtonData.YES) {
                Platform.exit() ;
            }
        }
    }

    /* Menu : Aled */
    @FXML
    public void help() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
        alert.setTitle("App operations") ;
        alert.setContentText("Instructions") ;
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/information.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
    
        ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
        alert.getButtonTypes().setAll(button) ;
        alert.showAndWait() ;
    }

    @FXML
    public void vocabularyPage() throws Exception{
        FlashCard flashCard = new FlashCard();
        try {       
            File file = new File(getClass().getResource("saves/All_stack.json").toURI().getRawPath());
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
        
            // convert JSON array to list of users
            List<StackCards> stacksCards = new Gson().fromJson(reader, new TypeToken<List<StackCards>>() {}.getType());
        
            // print users
            ArrayList<StackCards> stackList = new ArrayList<StackCards>();
            for (StackCards stack : stacksCards) {
                stackList.add(stack) ;
            }

            // On importe la pile seulement lorsqu'aucune pile portant le même nom n'existe déjà
            for (StackCards stack : stackList) {
                StackCards found = flashCard.find(stack.getName()) ;
                if (found == null) {
                    flashCard.addStackCards(stack) ;
                }
            }

            // close reader
            reader.close();

            // TODO this.update();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        App.changeToFirstPageView((Stage) this.stackName.getScene().getWindow(), flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) this.stackName.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        App.changeToToeicPageView((Stage) stackName.getScene().getWindow());
    }
}