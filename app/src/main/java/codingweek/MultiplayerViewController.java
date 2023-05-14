package codingweek;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class MultiplayerViewController implements Observer, Initializable{
    
    // Attributs
    private FlashCard flashCard;
    private StackCards stackCards;
    private Player player1;
    private Player player2;
    private Player player3;
    private int currentCard;
    private ArrayList<Card> cards;

    // Attributs FXML
    @FXML 
    private Button joueur1_button;
    @FXML
    private Button joueur2_button;
    @FXML
    private Button joueur3_button;
    @FXML
    private ImageView joueur1_imageView;
    @FXML
    private ImageView joueur2_imageView;
    @FXML
    private ImageView joueur3_imageView;
    @FXML
    private Label joueur1_scoreLabel;
    @FXML
    private Label joueur2_scoreLabel;
    @FXML
    private Label joueur3_scoreLabel;
    @FXML
    private Label stackCardName;
    @FXML
    private Button questionAnswer;
    @FXML
    private Button noAnswer;


    // Constructeur
    public MultiplayerViewController(FlashCard flashCard, StackCards stackCards, Player player1, Player player2, Player player3){
        this.flashCard = flashCard;
        this.stackCards = stackCards;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
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

    // Méthodes
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.cards = (ArrayList<Card>) this.stackCards.getCards().clone();
        Collections.shuffle(this.cards);
        this.currentCard = 0;
        update();

    }

    public void update(){

        this.stackCards.resetQorA();
        if (this.cards.size() == 0) {
            handleEndOfStack();
        }
        if ((this.cards.size() == 1) && (this.currentCard == 1)) {
            handleEndOfStack();
        }
            
        if(this.currentCard==-1){
            handleEndOfStack();
        }

        else{
            Card card = this.cards.get(this.currentCard);
            this.questionAnswer.setText(card.getQuestion());
        }

        this.joueur1_imageView.setImage(new Image(getClass().getResourceAsStream("images/" + this.player1.getImagePath())));
        this.joueur2_imageView.setImage(new Image(getClass().getResourceAsStream("images/" + this.player2.getImagePath())));
        this.joueur3_imageView.setImage(new Image(getClass().getResourceAsStream("images/" + this.player3.getImagePath())));
        this.joueur1_scoreLabel.setText(player1.toString());
        this.joueur2_scoreLabel.setText(player2.toString());
        this.joueur3_scoreLabel.setText(player3.toString());
        this.stackCardName.setText(this.stackCards.getName());
        this.joueur1_button.setText(this.player1.getName());
        this.joueur2_button.setText(this.player2.getName());
        this.joueur3_button.setText(this.player3.getName());

        this.joueur1_button.setOnAction(e -> {
            this.player1.addPoint();
            nextCurrentCard();
            update();
        });
        this.joueur2_button.setOnAction(e -> {
            this.player2.addPoint();
            nextCurrentCard();
            update();
        });
        this.joueur3_button.setOnAction(e -> {
            this.player3.addPoint();
            nextCurrentCard();
            update();
        });
        this.noAnswer.setOnAction(e -> {
            nextCurrentCard();
            update();
        });
    }

    public void nextCurrentCard(){
        if(this.currentCard==this.cards.size()-1){
            this.currentCard=-1;
        }
        else{
            this.currentCard++;
        }
    }

    public void handleEndOfStack(){

        //Reset pour la prochaine fois
        this.stackCards.resetQorA();

        //Pop-up de fin 
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Winner");
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/" + getWinner().getImagePath()))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Player winner = getWinner();
        alert.setHeaderText("The winneris " + winner.getName() + " with " + winner.getScore() + " points !");
        alert.setContentText("The stack is finished !");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
        alert.setOnHidden(evt -> {
            try {
                App.changeToStackView((Stage)this.stackCardName.getScene().getWindow(), this.flashCard, this.stackCards);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show(); 
    }


    @FXML
    public void swapQA(){
        Card card = cards.get(this.currentCard);
        card.switchQOrA();
        if(card.getQOrA()==1){
            String name = card.getAnswer();
            this.questionAnswer.setText(name);
        }
        else{
            String name = card.getQuestion();
            this.questionAnswer.setText(name);
        }
    }

    public Player getWinner(){
        Player winner = this.player1;
        if(this.player2.getScore()>winner.getScore()){
            winner = this.player2;
        }
        if(this.player3.getScore()>winner.getScore()){
            winner = this.player3;
        }
        return winner;
    }

    /* Menu : Fichier */
    @FXML
    public void home() throws Exception {

        //Reset pour la prochaine fois
        this.stackCards.resetQorA();

        
        //Pop-up de fin 
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("You will stop multiplayer mode.");
        alert.setHeaderText("You will stop multiplayer mode.");
        alert.setContentText("You will go back to the home page.");
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/information.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
        alert.setOnHidden(evt -> {
            try {
                App.changeToFirstPageView((Stage) this.joueur1_button.getScene().getWindow(), this.flashCard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show(); 
    }
    
    @FXML
    public void exit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING) ;
        alert.setTitle("You will quit the application.") ;
        alert.setContentText("Do you really want to quit the application ?") ;
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
        alert.setTitle("App operation") ;
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
        App.changeToFirstPageView((Stage) this.joueur1_button.getScene().getWindow(), flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) this.joueur1_button.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        //App.changeToToeicPageView((Stage) vocabularyButton.getScene().getWindow());
    }

}
