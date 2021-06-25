import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Optional;
import fontdialog.FontDialogController;

public class Controller {

    @FXML
    private BorderPane mainPane;

    @FXML
    private TextArea textArea;

    private Stage currentStage;

    private File currFile;

    private static final FileChooser fileChooser = new FileChooser();

    static {
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Java", "*.java"),
                new FileChooser.ExtensionFilter("C++", "*.cpp"),
                new FileChooser.ExtensionFilter("C", "*.c"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
    }

    public void initialize() {
        textArea.setStyle("-fx-text-fill: black;");
        textArea.setFont(Font.font("Arial", 16));
    }

    @FXML
    public void createNewSpace() {
        textArea.clear();
        currFile = null;
        if(currentStage == null) {
            currentStage = (Stage) mainPane.getScene().getWindow();
        }
        currentStage.setTitle("Notepad");
    }

    @FXML
    public void showOpenDialog() {
        fileChooser.setTitle("Open");
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        currFile = selectedFile;
        if(currFile != null) {
            readFile(selectedFile);
        }
        if(currentStage == null) {
            currentStage = (Stage) mainPane.getScene().getWindow();
        }
        if(selectedFile != null) {
            currentStage.setTitle(selectedFile.getName());
        }
    }

    @FXML
    public void saveFile() {
        if(currFile != null) {
            String location = currFile.getAbsolutePath();
            writeFile(location);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainPane.getScene().getWindow());
            alert.setTitle("Error");
            alert.setHeaderText("No file selected!");
            alert.setContentText("Please choose Save as option!");
            alert.showAndWait();
        }
    }

    @FXML
    public void showSaveAsDialog() {
        fileChooser.setTitle("Save as");
        File savedFile = fileChooser.showSaveDialog(mainPane.getScene().getWindow());
        currFile = savedFile;
        writeFile(savedFile.getAbsolutePath());
        currentStage.setTitle(savedFile.getName());
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    public void handleWordWrap() {
        if(textArea.isWrapText()) {
            textArea.setWrapText(false);
        } else {
            textArea.setWrapText(true);
        }
    }

    @FXML
    public void showFontDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Font");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fontdialog/fontDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        FontDialogController controller = new FontDialogController();
//        controller.setFontFamily(textArea.getFont().getName());
//        controller.setFontSize((int)textArea.getFont().getSize());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            String currFont = controller.getFont();
            Font font = Font.font(currFont, 16);
            textArea.setFont(font);
        }
    }

    @FXML
    public void handleAbout() {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URL("https://github.com/CryptoSingh1337").toURI());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainPane.getScene().getWindow());
            Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
            alert.setTitle("About Creator");
            alert.setHeaderText("CryptoSingh1337");
            alert.setContentText("This Notepad is developed by:");
            alert.showAndWait();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void readFile(File selectedFile) {
        new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s).append("\n");
                }
                textArea.setText(sb.toString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public void writeFile(String location) {
        new Thread(() -> {
           try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(location)))) {
               String text = textArea.getText();
               bw.write(text);
           } catch(IOException e) {
               System.out.println(e.getMessage());
           }
        }).start();
    }
}
