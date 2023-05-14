package codingweek;

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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GrammarQuestionViewController implements Initializable {
    
    private ArrayList<GrammarQuestion> questions = new ArrayList<GrammarQuestion>();
    private String nameSerie;
    private int currentQuestion = 0;
    private int nbGoodAnswers = 0;

    @FXML
    private Label title;
    @FXML
    private Label sentence;
    @FXML
    private Button prop1; 
    @FXML
    private Button prop2;
    @FXML
    private Button prop3;
    @FXML
    private Button prop4;
    @FXML
    private Button nextQuestion;
    @FXML
    private Label grammarNquestion;
    


    public GrammarQuestionViewController(String nameSerie) throws Exception {
        this.nameSerie = nameSerie;
        try {       
            File file = new File(getClass().getResource("saves/grammar" + this.nameSerie + ".json").toURI().getRawPath());
            // create a reader
            FileReader reader = new FileReader(file.getAbsolutePath());
            // convert JSON array to list of users
            this.questions = new Gson().fromJson(reader, new TypeToken<ArrayList<GrammarQuestion>>() {}.getType());

            // close reader
            reader.close();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    public void update(){
        if (this.currentQuestion == this.questions.size()){
            handleEndOfStack();
        }
        else{
            if (nameSerie == "athe"){
                this.nameSerie = "a / the";
            }
            if (nameSerie == "thisthat"){
                this.nameSerie = "this / that / these / those";
            }
            this.title.setText(this.nameSerie.toUpperCase());
            this.grammarNquestion.setText(this.nbGoodAnswers + "/" + this.questions.size());
            this.sentence.setText(this.questions.get(this.currentQuestion).getSentence());
            this.prop1.setText(this.questions.get(this.currentQuestion).getProp1().toUpperCase());
            this.prop2.setText(this.questions.get(this.currentQuestion).getProp2().toUpperCase());
            this.prop3.setText(this.questions.get(this.currentQuestion).getProp3().toUpperCase());
            this.prop4.setText(this.questions.get(this.currentQuestion).getProp4().toUpperCase());
            this.nextQuestion.setText("Next");
            this.nextQuestion.setDisable(true);
            this.prop1.setDisable(false);
            this.prop2.setDisable(false);
            this.prop3.setDisable(false);
            this.prop4.setDisable(false);
            this.prop1.setStyle(null);
            this.prop2.setStyle(null);
            this.prop3.setStyle(null);
            this.prop4.setStyle(null);
        }
    }



    @FXML
    public void prop1Button(){
        if(this.questions.get(this.currentQuestion).getProp1().equals(this.questions.get(this.currentQuestion).getAnswer())){
            this.prop1.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            this.nbGoodAnswers++;
        }
        else{
            this.prop1.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8);");
            if (this.questions.get(this.currentQuestion).getProp2().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop2.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp3().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop3.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp4().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop4.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
        }
        //Rendre les boutons non clickables
        this.prop1.setDisable(true);
        this.prop2.setDisable(true);
        this.prop3.setDisable(true);
        this.prop4.setDisable(true);
        this.nextQuestion.setDisable(false);
    }

    @FXML
    public void prop2Button(){
        if(this.questions.get(this.currentQuestion).getProp2().equals(this.questions.get(this.currentQuestion).getAnswer())){
            this.prop2.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            this.nbGoodAnswers++;
        }
        else{
            this.prop2.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8);");
            if (this.questions.get(this.currentQuestion).getProp1().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop1.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp3().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop3.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp4().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop4.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
        }
        //Rendre les boutons non clickables
        this.prop1.setDisable(true);
        this.prop2.setDisable(true);
        this.prop3.setDisable(true);
        this.prop4.setDisable(true);
        this.nextQuestion.setDisable(false);
    }

    @FXML
    public void prop3Button(){
        if(this.questions.get(this.currentQuestion).getProp3().equals(this.questions.get(this.currentQuestion).getAnswer())){
            this.prop3.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            this.nbGoodAnswers++;
        }
        else{
            this.prop3.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8);");
            if (this.questions.get(this.currentQuestion).getProp1().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop1.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp2().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop2.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp4().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop4.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
        }
        //Rendre les boutons non clickables
        this.prop1.setDisable(true);
        this.prop2.setDisable(true);
        this.prop3.setDisable(true);
        this.prop4.setDisable(true);
        this.nextQuestion.setDisable(false);
    }

    @FXML
    public void prop4Button(){
        if(this.questions.get(this.currentQuestion).getProp4().equals(this.questions.get(this.currentQuestion).getAnswer())){
            this.prop4.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            this.nbGoodAnswers++;
        }
        else{
            this.prop4.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8);");
            if (this.questions.get(this.currentQuestion).getProp1().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop1.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp2().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop2.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
            else if (this.questions.get(this.currentQuestion).getProp3().equals(this.questions.get(this.currentQuestion).getAnswer())){
                this.prop3.setStyle("-fx-background-color: rgba(0, 255, 0, 0.8);");
            }
        }
        //Rendre les boutons non clickables
        this.prop1.setDisable(true);
        this.prop2.setDisable(true);
        this.prop3.setDisable(true);
        this.prop4.setDisable(true);
        this.nextQuestion.setDisable(false);
    }

    @FXML
    public void nextQuestionButton(){
        System.out.println("Next Question");
        this.currentQuestion += 1;
        update();
    }



    @FXML
    public void exit() throws IOException {
        Platform.exit();
    }

    /* Menu : Aled */
    @FXML
    public void help() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
        alert.setTitle("App operation") ;
        alert.setContentText("Instructions") ;
        ImageView iconpo = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/information.jpg"))));
        iconpo.setFitHeight(100);
        iconpo.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpo);
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
        App.changeToFirstPageView((Stage) this.title.getScene().getWindow(), flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) title.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        App.changeToToeicPageView((Stage) title.getScene().getWindow());
    }
    
    public void handleEndOfStack(){
        //Pop-up de fin 
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("This is the end.");
        alert.setHeaderText("End of questions");
        alert.setContentText("You will come back to previous page.");
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/information.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
        alert.setOnHidden(evt -> {
            try {
                App.changeToGrammarMenuView((Stage)this.title.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show();
    }
    
}
