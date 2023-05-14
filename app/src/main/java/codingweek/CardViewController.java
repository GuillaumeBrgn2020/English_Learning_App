package codingweek;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CardViewController implements Observer, Initializable{


    // Attributs
    private FlashCard flashCard;
    private StackCards stackCards;
    private Card card;

    // Composants FXML
    @FXML
    private TextArea questionTextArea;
    @FXML
    private TextArea answerTextArea;
    @FXML
    private Label name;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;

    // Constructeur

    public CardViewController(FlashCard flashCard, StackCards stackCards, Card card){
        this.flashCard = flashCard;
        this.stackCards = stackCards;
        this.card = card;
        this.flashCard.clear() ;
        this.flashCard.addObserve(this) ;
        for (StackCards s : this.flashCard.getStacksCards()) {
            s.clear();
            s.addObserve(this) ;
            for (Card c : s.getCards()) {
                c.clear() ;
                c.addObserve(this) ;
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.update() ;
    }

    @FXML
    public void removeCard() {
        this.stackCards.removeCard(this.card);
        try {
            App.changeToStackView((Stage) questionTextArea.getScene().getWindow(), this.flashCard,this.stackCards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveCard() throws Exception {
        String newQuestion = this.questionTextArea.getText() ;
        String newAnswer = this.answerTextArea.getText() ;

        // Si la pile ou la réponse est vide, erreur
        if ((newQuestion.isEmpty()) || (newQuestion.isBlank()) || (newAnswer.isBlank()) || (newAnswer.isEmpty())) {
            
            // Echec de l'enregistrement si un champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setTitle("Echec") ;
            alert.setContentText("There is an empty field, please try again.") ;
            ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
            iconpop.setFitHeight(100);
            iconpop.setFitWidth(100);
            alert.getDialogPane().setGraphic(iconpop);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

            ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
            alert.getButtonTypes().setAll(button) ;
            alert.showAndWait() ;
        }
        // Sinon, si la question existe déjà, erreur
        else if ((this.stackCards.find(newQuestion) != null) && (!this.card.getQuestion().toLowerCase().equals(newQuestion.toLowerCase()))) {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setTitle("Echec") ;
            alert.setContentText("This card already exists, please try again.") ;
            ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
            iconpop.setFitHeight(100);
            iconpop.setFitWidth(100);
            alert.getDialogPane().setGraphic(iconpop);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

            ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
            alert.getButtonTypes().setAll(button) ;
            alert.showAndWait() ;
        }
        // Sinon, on enregistre et on retourne à la page de la pile
        else {
            this.card.setQuestion(newQuestion) ;
            this.card.setAnswer(newAnswer) ;
            App.changeToStackView((Stage) name.getScene().getWindow(), this.flashCard, this.stackCards);
        }
    }


    public void update(){
        this.flashCard.sort() ;
        this.stackCards.sort() ;

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("images/delete.png")));
        image.setFitHeight(70.0);
        image.setFitWidth(70.0);
        this.deleteButton.setGraphic(image);

        ImageView image1 = new ImageView(new Image(getClass().getResourceAsStream("images/validate.png")));
        image1.setFitHeight(70.0);
        image1.setFitWidth(70.0);
        this.saveButton.setGraphic(image1);

        this.name.setText(this.stackCards.getName());
        if (this.card.getQuestion() != ""){
            this.questionTextArea.setText(this.card.getQuestion());
        }
        if (this.card.getAnswer() != ""){
            this.answerTextArea.setText(this.card.getAnswer());
        }
    }
    
    /* Menu : Fichier */


    @FXML
    public void home() throws Exception {
        /* Enregistrement des informations entrées */
        // On enregistre les informations si les champs ne sont pas vides
        String newQuestion = this.questionTextArea.getText() ;
        String newAnswer = this.answerTextArea.getText() ;

        // Si la question n'est pas vide
        if ((!(newQuestion.isEmpty())) && (!newQuestion.isBlank())) {
            // Si la question existe déjà, erreur
            if ((this.stackCards.find(newQuestion) != null) && (!this.card.getQuestion().equals(newQuestion))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("A stack with this name already exists, please try again.") ;
                ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                iconpop.setFitHeight(100);
                iconpop.setFitWidth(100);
                alert.getDialogPane().setGraphic(iconpop);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
    
                ButtonType button = new ButtonType("Fermer", ButtonData.CANCEL_CLOSE) ;
                alert.getButtonTypes().setAll(button) ;
                alert.showAndWait() ;
                return ;
            }
            // Si elle n'existe pas, on l'enregistre
            else {
                this.card.setQuestion(newQuestion); ;
            }
        }
        // Si la réponse n'est pas vide, on l'enregistre
        if ((!newAnswer.isBlank()) && (!newAnswer.isEmpty())) {
            this.card.setAnswer(newAnswer) ;
        }

        /* Changement de page */
        App.changeToFirstPageView((Stage) answerTextArea.getScene().getWindow(), this.flashCard) ;
    }
    
    @FXML
    public void exit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING) ;
        alert.setTitle("Vous allez quitter l'application.") ;
        alert.setContentText("Do you want to save your changes?") ;
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/warning.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

        ButtonType yesButton = new ButtonType("Oui", ButtonData.YES) ;
        ButtonType cancelButton = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE) ;
        alert.getButtonTypes().setAll(yesButton, cancelButton) ;

        Optional<ButtonType> result = alert.showAndWait() ;
        if (result.isPresent()) {
            if (result.get().getButtonData() == ButtonData.YES) {
                Platform.exit() ;
            }
        }
    }

    /* Menu : Éditer */
    @FXML
    public void newCard() throws Exception {
        /* Enregistrement des informations entrées */
        // On enregistre les informations si les champs ne sont pas vides
        String newQuestion = this.questionTextArea.getText() ;
        String newAnswer = this.answerTextArea.getText() ;

        // Si la question n'est pas vide
        if ((!(newQuestion.isEmpty())) && (!newQuestion.isBlank())) {
            // Si la question existe déjà, erreur
            if ((this.stackCards.find(newQuestion) != null) && (!this.card.getQuestion().equals(newQuestion))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("A stack with this name already exists, please try again.") ;
                ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                iconpop.setFitHeight(100);
                iconpop.setFitWidth(100);
                alert.getDialogPane().setGraphic(iconpop);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
    
                ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                alert.getButtonTypes().setAll(button) ;
                alert.showAndWait() ;
                return ;
            }
            // Sinon, on enregistre
            else {
                this.card.setQuestion(newQuestion);
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newAnswer.isBlank()) && (!newAnswer.isEmpty())) {
            this.card.setAnswer(newAnswer) ;
        }

        /* Création de la carte */
        // La boite de dialogue
        Dialog<Pair<String, String>> dialog = new Dialog<>() ;
        dialog.setTitle("Card creation") ;
        Stage stageDialog = (Stage) dialog.getDialogPane().getScene().getWindow();
        stageDialog.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

        // Les boutons de la boite
        ButtonType validateButton = new ButtonType("Valid", ButtonData.OK_DONE) ;
        dialog.getDialogPane().getButtonTypes().addAll(validateButton, ButtonType.CANCEL) ;

        VBox vbox = new VBox(10) ;
        vbox.setAlignment(Pos.CENTER) ;
        
        TextArea questionArea = new TextArea() ;
        questionArea.setMinHeight(100) ;
        questionArea.setMaxHeight(100) ;
        questionArea.setPromptText("Question") ;
        TextArea answerArea = new TextArea() ;
        answerArea.setMinHeight(100);
        answerArea.setMaxHeight(100) ;
        answerArea.setPromptText("Answer") ;

        vbox.getChildren().addAll(questionArea, answerArea) ;
        dialog.getDialogPane().setContent(vbox) ;

        // Conversion du résultat en une paire
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validateButton) {
                return new Pair<>(questionArea.getText(), answerArea.getText()) ;
            }
            else {
                return null ;
            }
        });

        // Réception des réponses
        Optional<Pair<String, String>> result = dialog.showAndWait() ;
        if (result.isPresent()) {
            String question = result.get().getKey() ;
            String answer = result.get().getValue() ;
            
            if (!((question.isEmpty()) || (answer.isEmpty()))) {
                // On vérifie que la carte n'existe pas déjà
                boolean b = true ;
                for (Card card : this.stackCards.getCards()) {
                    if (((card.getQuestion().toLowerCase().equals(question.toLowerCase()))) || (card.getQuestion().toLowerCase() == question.toLowerCase())) {
                        b = false ;
                    }
                }
                if (b) {
                    Card newCard = new Card(this.card.getQuestion(), this.card.getAnswer()) ;
                    newCard.addObserve(this) ;
                    this.stackCards.addCard(newCard);
                    this.card.changeCard(question, answer);
                    // On actualise la page
                    // TODO this.update() ;
                    App.changeToStackView((Stage) name.getScene().getWindow(), this.flashCard, this.stackCards) ;
                }
                else {
                    // Echec de l'enregistrement
                    Alert alert = new Alert(Alert.AlertType.ERROR) ;
                    alert.setTitle("Echec") ;
                    alert.setContentText("This card already exists, please try again.") ;
                    ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                    iconpop.setFitHeight(100);
                    iconpop.setFitWidth(100);
                    alert.getDialogPane().setGraphic(iconpop);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

                    ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                    alert.getButtonTypes().setAll(button) ;
                    alert.showAndWait() ;
                }
            }
            else {
                // Echec de l'enregistrement
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("There is an empty field, please try again.") ;
                ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                iconpop.setFitHeight(100);
                iconpop.setFitWidth(100);
                alert.getDialogPane().setGraphic(iconpop);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

                ButtonType button = new ButtonType("Fermer", ButtonData.CANCEL_CLOSE) ;
                alert.getButtonTypes().setAll(button) ;
                alert.showAndWait() ;
            }
        }
    }

    /* Menu : Statistiques */
    @FXML
    public void stats() throws Exception{
        /* Enregistrement des informations entrées */
        // On enregistre les informations si les champs ne sont pas vides
        String newQuestion = this.questionTextArea.getText() ;
        String newAnswer = this.answerTextArea.getText() ;

        // Si la question n'est pas vide
        if ((!(newQuestion.isEmpty())) && (!newQuestion.isBlank())) {
            // Si la question existe déjà, erreur
            if ((this.stackCards.find(newQuestion) != null) && (!this.card.getQuestion().equals(newQuestion))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack cards name is used. Please, choose another one.") ;
                ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                iconpop.setFitHeight(100);
                iconpop.setFitWidth(100);
                alert.getDialogPane().setGraphic(iconpop);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
    
                ButtonType button = new ButtonType("Fermer", ButtonData.CANCEL_CLOSE) ;
                alert.getButtonTypes().setAll(button) ;
                alert.showAndWait() ;
                return ;
            }
            // Sinon, on enregistre
            else {
                this.card.setQuestion(newQuestion);
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newAnswer.isBlank()) && (!newAnswer.isEmpty())) {
            this.card.setAnswer(newAnswer) ;
        }

        /* Changement de page */
        App.changeToStatisticsView((Stage) name.getScene().getWindow(), this.flashCard);
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
    
        ButtonType button = new ButtonType("Fermer", ButtonData.CANCEL_CLOSE) ;
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
        App.changeToFirstPageView((Stage) this.deleteButton.getScene().getWindow(), flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) this.deleteButton.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        App.changeToToeicPageView((Stage) deleteButton.getScene().getWindow());
    }
}
