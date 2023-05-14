package codingweek;

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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class StackViewController implements Observer, Initializable {
    /* Attributs */
    private FlashCard flashCard ;
    private StackCards stackCards ;
    
    /* Attributs FXML */
    @FXML
    private TextField stackName ;

    @FXML
    private VBox cardsView ;

    @FXML
    private TextArea stackDescription ;

    @FXML
    private Label nbCardsInStack;

    @FXML
    private MenuItem frequencyMenu1;
    @FXML
    private MenuItem frequencyMenu2A;
    @FXML
    private MenuItem frequencyMenu2B;
    @FXML
    private MenuItem frequencyMenu3A;
    @FXML
    private MenuItem frequencyMenu3B;
    @FXML
    private MenuItem frequencyMenu3C;
    @FXML
    private CheckBox answerFirst;
    @FXML
    private Button addCardButton;



    /* Constructeur */
    public StackViewController(FlashCard flashCard, StackCards stackCards) {
        this.flashCard = flashCard ;
        this.stackCards = stackCards ;
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.update() ;
    }

    @Override
    public void update() {
        this.flashCard.sort() ;
        this.stackCards.sort() ;

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("images/add.png")));
        image.setFitHeight(50.0);
        image.setFitWidth(50.0);
        this.addCardButton.setGraphic(image);

        String name = stackCards.getName();
        this.stackName.setText(name) ;
        this.nbCardsInStack.setText(this.stackCards.getCards().size() + " cards") ;
        this.stackDescription.setText(this.stackCards.getDescription()) ;
        
        this.cardsView.getChildren().clear() ;
        for (Card card : this.stackCards.getCards()) {
            String cardName = card.getQuestion();
            Button button = new Button(cardName) ;
            button.setPrefWidth(198.0) ;
            button.setOnAction(event -> {
                try {
                    /* Enregistrement des informations entrées */
                    //On enregistre les informations si les champs ne sont pas vides
                    String newName = this.stackName.getText() ;
                    String newDescription = this.stackDescription.getText() ;
            
                    // Si le nom n'est pas vide
                    if ((!(newName.isEmpty())) && (!newName.isBlank())) {
                        // Si le nom existe déjà, erreur
                        if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                            // Echec de l'enregistrement si le nom existe déjà
                            Alert alert = new Alert(Alert.AlertType.ERROR) ;
                            alert.setTitle("Echec") ;
                            alert.setContentText("This stack name already exists. Please choose another one.") ;
                            ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                            iconpop.setFitHeight(100);
                            iconpop.setFitWidth(100);
                            alert.getDialogPane().setGraphic(iconpop);
                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));
                
                            ButtonType b = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                            alert.getButtonTypes().setAll(b) ;
                            alert.showAndWait() ;
                            return ;
                        }
                        // Sinon, on enregistre le nom
                        else {
                            this.stackCards.setName(newName) ;
                        }
                    }
                    // Si la description n'est pas vide, on l'enregistre
                    if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
                        this.stackCards.setDescription(newDescription) ;
                    }
                    App.changeToCardView((Stage) stackName.getScene().getWindow(), this.flashCard, this.stackCards, card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }) ;
            this.cardsView.getChildren().add(button) ;
        }
    }

    /* Menu : Fichier */
    @FXML
    public void importStackCards() {
        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists, please try again.") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }

        /* Importation */
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
                App.changeToFirstPageView((Stage) stackName.getScene().getWindow(), flashCard);
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
        alert.setTitle("Stacks to export") ;
        alert.setContentText("Which stacks do you want to save ?") ;
        ImageView icon = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/confirmation.jpg"))));
        icon.setFitHeight(100);
        icon.setFitWidth(100);
        alert.getDialogPane().setGraphic(icon);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

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
        checkComboBox.setTitle("Choose stacks to save") ;

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
                File file = fileChooser.showSaveDialog((Stage) this.stackName.getScene().getWindow()) ;
    
                if (file != null) {
                    this.flashCard.save(file, stackList) ;

                    // Confirmation de l'enregistrement
                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION) ;
                    alertConfirmation.setTitle("Confirmation") ;
                    alertConfirmation.setContentText("These stacks are exported.") ;
                    ImageView icon1 = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/confirmation.jpg"))));
                    icon1.setFitHeight(100);
                    icon1.setFitWidth(100);
                    alert.getDialogPane().setGraphic(icon1);
                    Stage stageConfirmation = (Stage) alertConfirmation.getDialogPane().getScene().getWindow();
                    stageConfirmation.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

                    ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                    alertConfirmation.getButtonTypes().setAll(button) ;
                    alertConfirmation.showAndWait() ;
                } else {
                    // Echec de l'exportation
                    Alert alertError = new Alert(Alert.AlertType.ERROR) ;
                    alertError.setTitle("Echec") ;
                    alertError.setContentText("These stacks are not exported.") ;
                    ImageView icon1 = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/error.jpg"))));
                    icon1.setFitHeight(100);
                    icon1.setFitWidth(100);
                    alert.getDialogPane().setGraphic(icon1);
                    Stage stageError = (Stage) alertError.getDialogPane().getScene().getWindow();
                    stageError.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

                    ButtonType button = new ButtonType("Close", ButtonData.CANCEL_CLOSE) ;
                    alertError.getButtonTypes().setAll(button) ;
                    alertError.showAndWait() ;
                }
            }
            else if (result.get().getButtonData() == ButtonData.CANCEL_CLOSE) {}
        }
    }

    @FXML
    public void multiplayer() throws Exception{
        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }

        /* Changement de page */
        Player player1 = new Player("Player 1", "joueur1.jpg") ;
        Player player2 = new Player("Player 2", "joueur2.jpg") ;
        Player player3 = new Player("Player 3", "joueur3.jpg") ;

        if (this.stackCards.getCards().size()==0){
            System.out.println("This stack is empty");
            App.changeToStackView((Stage) this.addCardButton.getScene().getWindow(), this.flashCard, this.stackCards);
        }
        else{
            App.changeToMultiplayerView((Stage) this.nbCardsInStack.getScene().getWindow(), this.flashCard, this.stackCards, player1, player2, player3);
        }
    }

    @FXML
    public void home() throws Exception {
        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists, please try again.") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }

        /* Changement de page */
        App.changeToFirstPageView((Stage) stackName.getScene().getWindow(), this.flashCard) ;
    }
    
    @FXML
    public void exit() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING) ;
        alert.setTitle("You will quit the application.") ;
        alert.setContentText("Do you want to save your changes ?") ;
        ImageView iconpop = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/warning.jpg"))));
        iconpop.setFitHeight(100);
        iconpop.setFitWidth(100);
        alert.getDialogPane().setGraphic(iconpop);
        ImageView icon = new ImageView(new Image(String.valueOf(this.getClass().getResource("images/warning.jpg"))));
        icon.setFitHeight(100);
        icon.setFitWidth(100);
        alert.getDialogPane().setGraphic(icon);
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

    /* Menu : Éditer */
    @FXML
    public void newStack() {
        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists, please try again.") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }

        /* Création de la pile */
        // La boite de dialogue
        Dialog<Pair<String, String>> dialog = new Dialog<>() ;
        dialog.setTitle("Stack creation") ;
        Stage stageDialog = (Stage) dialog.getDialogPane().getScene().getWindow();
        stageDialog.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.jpg")));

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
                    /* TODO
                    StackCards newStackCards = new StackCards(name, description) ;
                    this.flashCard.addStackCards(newStackCards) ;
                    // Affichage de la nouvelle pile
                    this.stackCards = newStackCards ;
                    TODO this.update() ; */

                    StackCards stack = new StackCards(this.stackCards.getName(), this.stackCards.getDescription()) ;
                    stack.addObserve(this) ;
                    this.flashCard.addStackCards(stack) ;
                    this.stackCards.changeStackCards(name, description) ;
                }
                else {
                    // Echec de l'enregistrement
                    Alert alert = new Alert(Alert.AlertType.ERROR) ;
                    alert.setTitle("Echec") ;
                    alert.setContentText("This stack name already exists, please try again.") ;
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
                alert.setContentText("There is a blank field, please try again.") ;
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
    }

    @FXML
    public void saveStack() {
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // On vérifie que les champs ne sont pas vides
        if ((newName.isEmpty()) || (newName.isBlank()) || (newDescription.isBlank()) || (newDescription.isEmpty())) {
            
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
        else if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().toLowerCase().equals(newName.toLowerCase()))) {
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
        }
        else {
            this.stackCards.setName(newName) ;
            this.stackCards.setDescription(newDescription) ;
        }
    }
    
    @FXML
    public void removeStack() throws Exception {
        this.flashCard.removeStackCards(this.stackCards) ;
        App.changeToFirstPageView((Stage) stackName.getScene().getWindow(), this.flashCard) ;
    }
    
    @FXML
    public void refreshScore() {
        this.stackCards.resetStatistics() ;
    }

    @FXML
    public void setOrderMethodA(){
        this.stackCards.setOrderMethodA() ;
    }

    @FXML
    public void setOrderMethodR(){
        this.stackCards.setOrderMethodR() ;
    }

    @FXML
    public void setOrderMethodC(){
        this.stackCards.setOrderMethodC() ;
    }

    /* Menu : Statistiques */
    @FXML
    public void stats() throws Exception{
        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists, please try again.") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }

        /* Changement de page */
        App.changeToStatisticsView((Stage) stackName.getScene().getWindow(), this.flashCard);
    }

    @FXML
    public void setFrequency1(){
        String frequency = this.frequencyMenu1.getText();
        this.stackCards.setFrequency(frequency);
    }
    
    @FXML
    public void setFrequency2A(){
        String frequency = this.frequencyMenu2A.getText();
        this.stackCards.setFrequency(frequency);
    }
    
    @FXML
    public void setFrequency2B(){
        String frequency = this.frequencyMenu2B.getText();
        this.stackCards.setFrequency(frequency);
    }
    
    @FXML
    public void setFrequency3A(){
        String frequency = this.frequencyMenu3A.getText();
        this.stackCards.setFrequency(frequency);
    }
    
    @FXML
    public void setFrequency3B(){
        String frequency = this.frequencyMenu3B.getText();
        this.stackCards.setFrequency(frequency);
    }
    
    @FXML
    public void setFrequency3C(){
        String frequency = this.frequencyMenu3C.getText();
        this.stackCards.setFrequency(frequency);
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

    /* Actions des boutons de la pages */
    @FXML
    public void addCard() {
        /*
        Card card = new Card();
        this.stackCards.addCard(card) ;
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;
        if (!newName.equals(this.stackCards.getName())&& !newName.equals("")) {
            this.stackCards.setName(newName) ;
        }
        if (!newDescription.equals(this.stackCards.getDescription()) && !newDescription.equals("")) {
            this.stackCards.setDescription(newDescription) ;
        }
        try {
            App.changeToCardView((Stage) stackName.getScene().getWindow(), this.flashCard, this.stackCards, card);
        } catch (Exception e) {
            e.printStackTrace();
        } */

        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists, please try again.") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }

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
                    Card newCard = new Card(question, answer) ;
                    this.stackCards.addCard(newCard);
                    // On actualise la page
                    // TODO this.update() ;
                }
                else {
                    // Echec de l'enregistrement
                    Alert alert = new Alert(Alert.AlertType.ERROR) ;
                    alert.setTitle("Echec") ;
                    alert.setContentText("This card already exists, please try again.") ;
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
                alert.setContentText("There is a blank field, please try again.") ;
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

    @FXML
    public void startLearning() throws Exception {
        /* Enregistrement des informations entrées */
        //On enregistre les informations si les champs ne sont pas vides
        String newName = this.stackName.getText() ;
        String newDescription = this.stackDescription.getText() ;

        // Si le nom n'est pas vide
        if ((!(newName.isEmpty())) && (!newName.isBlank())) {
            // Si le nom existe déjà, erreur
            if ((this.flashCard.find(newName) != null) && (!this.stackCards.getName().equals(newName))) {
                // Echec de l'enregistrement si le nom existe déjà
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Echec") ;
                alert.setContentText("This stack name already exists, please try again.") ;
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
            // Sinon, on enregistre le nom
            else {
                this.stackCards.setName(newName) ;
            }
        }
        // Si la description n'est pas vide, on l'enregistre
        if ((!newDescription.isBlank()) && (!newDescription.isEmpty())) {
            this.stackCards.setDescription(newDescription) ;
        }


        if (this.answerFirst.isSelected()){
            this.stackCards.setAnswerFirst(true);
        }
        else{
            this.stackCards.setAnswerFirst(false);
        }
        App.changeToLearningView((Stage) this.stackName.getScene().getWindow(), this.flashCard, this.stackCards) ;
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
