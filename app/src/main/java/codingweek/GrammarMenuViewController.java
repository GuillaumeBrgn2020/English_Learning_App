package codingweek;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
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



public class GrammarMenuViewController implements Initializable {

    @FXML
    private Button grammarButton;
    private Image image;
    @FXML
    private ImageView logo;


    public GrammarMenuViewController() {}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL imageFile=getClass().getResource("images/logo.png");
        if (imageFile!=null) {
            this.image = new Image(imageFile.toString());
        }
        update();
    }

    public void update(){
        this.logo.setImage(this.image);
    }


    @FXML
    public void presentButton() throws Exception{
        App.changeToGrammarQuestionView((Stage) this.grammarButton.getScene().getWindow(), "Temps");
    }

    @FXML
    public void meaningButton() throws Exception{
        App.changeToGrammarQuestionView((Stage) this.grammarButton.getScene().getWindow(), "Meaning");
    }

    @FXML
    public void adjButton() throws Exception{
        App.changeToGrammarQuestionView((Stage) this.grammarButton.getScene().getWindow(), "AdjAdvNoun");
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
        App.changeToFirstPageView((Stage) this.grammarButton.getScene().getWindow(), flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) grammarButton.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        App.changeToToeicPageView((Stage) grammarButton.getScene().getWindow());
    }
}
