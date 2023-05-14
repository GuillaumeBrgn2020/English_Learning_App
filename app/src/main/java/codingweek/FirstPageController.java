package codingweek;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

import org.controlsfx.control.CheckComboBox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class FirstPageController implements Observer, Initializable {

    /* Attributs */
    private FlashCard flashCard;

    private Image image;

    /* Composants FXML*/
    @FXML
    private ImageView logo;

    @FXML
    private HBox stack;

    @FXML
    private Button newButton;


    /* Constructeur */
    public FirstPageController(FlashCard flashCard) {
        this.flashCard = flashCard;
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

    /* Affichage */
    public void initialize(URL location, ResourceBundle resources) {
        URL imageFile=getClass().getResource("images/logo.png");
        if (imageFile!=null) {
            this.image = new Image(imageFile.toString());
        }
        update();
    }

    public void update() {
        this.flashCard.sort() ;
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("images/add.png")));
        image.setFitHeight(50.0);
        image.setFitWidth(50.0);
        this.newButton.setGraphic(image);
        this.logo.setImage(getImage());
        ArrayList<StackCards> flashCard = this.flashCard.getStacksCards();
        this.stack.getChildren().clear() ;
        for (StackCards stackCards : flashCard) {
            String name = stackCards.getName();
            Button button = new Button(name) ;
            button.setPrefHeight(80.0) ;
            button.setPrefWidth(130.0) ;
            button.setOnAction(event -> {
                try {
                    App.changeToStackView((Stage) logo.getScene().getWindow(), this.flashCard, stackCards) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }) ;
            this.stack.getChildren().add(button) ;
        }
    }

    /* Menu : Fichier */
    
    @FXML
    public void importStackCards() {
        // Ouverture de l'explorateur de fichiers
        FileChooser fileChooser = new FileChooser() ;
        FileChooser.ExtensionFilter extFilterjson = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json") ;
        fileChooser.getExtensionFilters().addAll(extFilterjson) ;

        File file = fileChooser.showOpenDialog(null) ;
        if (file != null) {
            try {        
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
                    StackCards found = this.flashCard.find(stack.getName()) ;
                    if (found == null) {
                        this.flashCard.addStackCards(stack) ;
                    }
                }

                // close reader
                reader.close();

                // TODO this.update();
            } 
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void exportStackCards() throws IOException {
        // La boite de dialogue pour sélectionner les stacks à exporter
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION) ;
        alert.setTitle("Enregistrement de piles") ;
        alert.setContentText("Chose the stacks to save") ;
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/confirmation.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);

        ButtonType validateButton = new ButtonType("Valid", ButtonData.YES) ;
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE) ;
        alert.getButtonTypes().setAll(validateButton, cancelButton) ;
        
        // Création de la liste des piles à sauvegarder
        ArrayList<StackCards> stackList = new ArrayList<>() ;

        // Création de la checkbox à afficher
        ObservableList<String> strings = FXCollections.observableArrayList() ;
        for (StackCards stack : this.flashCard.getStacksCards()) {
            strings.add(stack.getName()) ;
        }
        CheckComboBox<String> checkComboBox = new CheckComboBox<String>(strings) ;
        checkComboBox.setTitle("Stacks to save") ;

        // On récupère la liste des items sélectionnés à chaque clic
        checkComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                stackList.clear() ;
                for (String item : checkComboBox.getCheckModel().getCheckedItems()) {
                    stackList.add(flashCard.find(item)) ;
                }
            }
        }) ;
        checkComboBox.show() ;

        alert.getDialogPane().setContent(checkComboBox) ;
        Optional<ButtonType> result = alert.showAndWait() ;
        if (result.isPresent()) {
            if (result.get().getButtonData() == ButtonData.YES) {
                FileChooser fileChooser = new FileChooser();

                // Les extensions
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichier JSON (*.json)", "*.json") ;
                fileChooser.getExtensionFilters().add(extFilter) ;
    
                // Afficher le dialogue save
                File file = fileChooser.showSaveDialog((Stage) this.logo.getScene().getWindow()) ;
    
                if (file != null) {
                    this.flashCard.save(file, stackList) ;

                    // Confirmation de l'exportation
                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION) ;
                    alertConfirmation.setTitle("Confirmation") ;
                    alertConfirmation.setContentText("These stacks have been saved") ;
                    Stage stage1 = (Stage) alertConfirmation.getDialogPane().getScene().getWindow();
                    stage1.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
                    ImageView iconpo = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/confirmation.jpg"))));
                    iconpo.setFitHeight(100);
                    iconpo.setFitWidth(100);
                    alert.getDialogPane().setGraphic(iconpo);
            
                    ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                    alertConfirmation.getButtonTypes().setAll(button) ;
                    alertConfirmation.showAndWait() ;
                }
                else {
                    // Echec de l'exportation
                    Alert alertError = new Alert(Alert.AlertType.ERROR) ;
                    alertError.setTitle("Echec") ;
                    alertError.setContentText("The stacks have not been saved") ;
                    Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage1.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
                    ImageView iconpo = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                    iconpo.setFitHeight(100);
                    iconpo.setFitWidth(100);
                    alert.getDialogPane().setGraphic(iconpo);
            
                    ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                    alertError.getButtonTypes().setAll(button) ;
                    alertError.showAndWait() ;
                }
            }
            else if (result.get().getButtonData() == ButtonData.CANCEL_CLOSE) {}
        }
    }

    @FXML
    public void exit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING) ;
        alert.setTitle("You will exit the application") ;
        alert.setContentText("Do you want to save your stacks before exiting?") ;
        ImageView iconpo = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/warning.jpg"))));
        iconpo.setFitHeight(100);
        iconpo.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpo);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

        ButtonType yesButton = new ButtonType("Yes", ButtonData.YES) ;
        ButtonType noButton = new ButtonType("No", ButtonData.NO) ;
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE) ;
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton) ;

        Optional<ButtonType> result = alert.showAndWait() ;
        if (result.isPresent()) {
            if (result.get().getButtonData() == ButtonData.YES) {
                this.exportStackCards() ;
                Platform.exit() ;
            }
            else if (result.get().getButtonData() == ButtonData.NO) {
                Platform.exit() ;
            }
        }
    }

    /* Menu : Aled */
    @FXML
    public void help() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
        alert.setTitle("App operation") ;
        alert.setContentText("Help") ;
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
    public void stats(){
        try {
            App.changeToStatisticsView((Stage) stack.getScene().getWindow(), this.flashCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Action des boutons */
    @FXML
    public void newStack() {
        // La boite de dialogue
        Dialog<Pair<String, String>> dialog = new Dialog<>() ;
        dialog.setTitle("Stack creation") ;
        Stage s = (Stage) dialog.getDialogPane().getScene().getWindow();
        s.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

        // Les boutons de la boite
        ButtonType validateButton = new ButtonType("Valid", ButtonData.OK_DONE) ;
        dialog.getDialogPane().getButtonTypes().addAll(validateButton, ButtonType.CANCEL) ;

        VBox vbox = new VBox(10) ;
        vbox.setAlignment(Pos.CENTER) ;        
        
        TextField nameField = new TextField() ;
        nameField.setMaxHeight(20) ;
        nameField.setPromptText("Name") ;
        TextArea descriptionArea = new TextArea() ;
        descriptionArea.setMinHeight(100);
        descriptionArea.setMaxHeight(100) ;
        descriptionArea.setPromptText("Description") ;

        vbox.getChildren().addAll(nameField, descriptionArea) ;
        dialog.getDialogPane().setContent(vbox) ;

        // Conversion du résultat en une paire
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validateButton) {
                return new Pair<>(nameField.getText(), descriptionArea.getText()) ;
            }
            else {
                return null ;
            }
        });

        // Réception des réponses
        Optional<Pair<String, String>> result = dialog.showAndWait() ;
        if (result.isPresent()) {
            String name = result.get().getKey() ;
            String description = result.get().getValue() ;

            if ((!(name.isEmpty()) && (!name.isBlank()) && (!description.isEmpty()) && (!description.isBlank()))) {
                
                // On vérifie que la pile n'existe pas déjà
                boolean b = true ;
                for (StackCards stack : this.flashCard.getStacksCards()) {
                    if ((stack.getName().toLowerCase().equals(name.toLowerCase())) || (stack.getName().toLowerCase() == name.toLowerCase())) {
                        b = false ;
                    }
                }
                if (b) {
                    StackCards newStackCards = new StackCards(name, description) ;
                    // TODO
                    newStackCards.addObserve(this);
                    this.flashCard.addStackCards(newStackCards) ;
                    // TODO this.update() ;
                }
                else {
                    // Echec de l'enregistrement
                    Alert alert = new Alert(Alert.AlertType.ERROR) ;
                    alert.setTitle("Echec") ;
                    alert.setContentText("This stack name already exists, please try again") ;
                    ImageView iconpo = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                    iconpo.setFitHeight(100);
                    iconpo.setFitWidth(100);
                    alert.getDialogPane().setGraphic(iconpo);
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
                alert.setContentText("A stack name and a description are required") ;
                ImageView iconpo = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                iconpo.setFitHeight(100);
                iconpo.setFitWidth(100);
                alert.getDialogPane().setGraphic(iconpo);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

                ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                alert.getButtonTypes().setAll(button) ;
                alert.showAndWait() ;
            }
        }
    }

    public Image getImage() {
        return this.image;
    }

    @FXML
    public void vocabularyPage() throws Exception{
        System.out.println("vocabularyPage");
        App.changeToFirstPageView((Stage) stack.getScene().getWindow(), this.flashCard);
    }

    @FXML
    public void grammarPage() throws Exception{
        App.changeToGrammarMenuView((Stage) stack.getScene().getWindow());
    }

    @FXML
    public void toeicPage() throws Exception{
        App.changeToToeicPageView((Stage) stack.getScene().getWindow());
    }
}