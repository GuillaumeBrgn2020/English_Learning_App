package codingweek;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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


public class StatisticsViewController implements Initializable, Observer {

    // Attributs
    private FlashCard flashCard;
    
    // Attributs FXML
    @FXML
    private VBox stackViewVBox;
    @FXML
    private ImageView percentageImageView;
    @FXML
    private Label nbPilesConnuesLabel;
    @FXML
    private Label nbCartesConnuesLabel;
    @FXML
    private Label typeStatsLabel;

    // Constructeur
    public StatisticsViewController(FlashCard flashCard) {
        this.flashCard = flashCard;
    }

    // Méthodes
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.flashCard.getStacksCards().size() == 0) {
            this.stackViewVBox.getChildren().add(new Button("Aucune pile"));
            this.nbCartesConnuesLabel.setText("Number of validated cards : 0/0");
            this.nbPilesConnuesLabel.setText("Number of validated stack : 0/0");
            this.percentageImageView.setImage(new Image (getClass().getResource("images/0_percent.png").toString()));
        } 
        else {
            for (StackCards stackCards : this.flashCard.getStacksCards()) {
                Button button = new Button(stackCards.getName());
                button.setOnAction(e -> {
                    this.typeStatsLabel.setText("Statistics about this stack " + stackCards.getName());
                    this.nbPilesConnuesLabel.setText("Completed stacks " + String.valueOf(stackCards.getNbCorrect()) + " fois");
                    this.nbCartesConnuesLabel.setText("Percentage of good answer : " + String.valueOf(stackCards.getNbCardCorrect()) + "/" + String.valueOf(stackCards.getNbAppear()));
                    this.percentageImageView.setImage(new Image (getClass().getResource("images/" + String.valueOf(stackCards.getPercentage()) + "_percent.png").toString()));
                });
                button.setPrefWidth(200);
                this.stackViewVBox.getChildren().add(button);
                this.nbCartesConnuesLabel.setText("Number of validated stack : " + String.valueOf((int)this.flashCard.getNbCorrectStack()) + "/" + String.valueOf((int)this.flashCard.getStacksCards().size()));
                this.nbPilesConnuesLabel.setText("Number of validated cards : " + String.valueOf((int)this.flashCard.getNbCorrectCards()) + "/" + String.valueOf((int)this.flashCard.getNbCardsAppear()));
                this.percentageImageView.setImage(new Image (getClass().getResource("images/" + String.valueOf(flashCard.getPercentage()) + "_percent.png").toString()));
            }
        }
    }


    public void update(){}


    @FXML
    public void home() throws Exception {
        App.changeToFirstPageView((Stage) nbPilesConnuesLabel.getScene().getWindow(), this.flashCard) ;
    }
    
    @FXML
    public void exit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING) ;
        alert.setTitle("You will exit the app") ;
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
        App.changeToFirstPageView((Stage) this.nbPilesConnuesLabel.getScene().getWindow(), flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) this.nbCartesConnuesLabel.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        //App.changeToToeicPageView((Stage) vocabularyButton.getScene().getWindow());
    }
    
}
