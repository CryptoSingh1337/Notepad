package fontdialog;

import fontfamily.FontFamily;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FontDialogController {

    @FXML
    private ComboBox<String> fontFamily;

    @FXML
    private Label sample;

    private static String currFont = "Arial";

    public void initialize() {
        fontFamily.setItems(FXCollections.observableList(FontFamily.getInstance().getFontNames()));
        fontFamily.setValue("Arial");
    }

    @FXML
    private void getSelectedFont() {
        currFont = fontFamily.getValue();
        sample.setFont(Font.font(currFont));
    }

    public String getFont() {
        return currFont;
    }

//    public void setFontFamily(String fontFamily) {
//        System.out.println(fontFamily);
//        FontFamily.setValue(fontFamily);
//    }
//
//    public void setFontSize(int fontSize) {
//        System.out.println(fontSize);
//        FontSize.setValue(fontSize);
//    }
}
