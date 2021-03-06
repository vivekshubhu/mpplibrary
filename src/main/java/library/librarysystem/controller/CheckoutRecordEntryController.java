package library.librarysystem.controller;

import de.vandermeer.asciitable.AsciiTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import library.librarysystem.business.CheckoutRecordEntry;
import library.librarysystem.dataaccess.DataAccessFacade;
import library.librarysystem.ui.CheckInBookWindow;
import library.librarysystem.ui.CheckoutRecordTableWindow;
import library.librarysystem.ui.LibrarianWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CheckoutRecordEntryController implements Initializable {

    @FXML
    private TableView<CheckoutRecordEntry> table;
    @FXML
    private TableColumn<CheckoutRecordEntry, String> isbn;
    @FXML
    private TableColumn<CheckoutRecordEntry, String> title;
    @FXML
    private TableColumn<CheckoutRecordEntry, String> copyNum;
    @FXML
    private TableColumn<CheckoutRecordEntry, String> checkoutDate;
    @FXML
    private TableColumn<CheckoutRecordEntry, String> dueDate;

    @FXML
    private Label memberId;
    @FXML
    private Label memberName;

    @FXML
    private Button printButton;

    public ObservableList<CheckoutRecordEntry> checkoutRecordEntries = FXCollections.observableArrayList(
            DataAccessFacade.getCheckoutRecordEntries(LibrarianController.currentMemberId)
    );

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        memberId.setText(LibrarianController.currentMemberId);
        memberName.setText(LibrarianController.currentMemberName);
        isbn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookCopy().getBook().getIsbn()));
        title.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookCopy().getBook().getTitle()));
        copyNum.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getBookCopy().getCopyNum())));
        checkoutDate.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        table.setItems(checkoutRecordEntries);
    }

    @FXML
    public void printCheckoutRecord(ActionEvent actionEvent) {

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ISBN", "Title", "Copy Num", "Checkout Date", "Due Date");
        asciiTable.addRule();
        checkoutRecordEntries.forEach(checkoutRecordEntry -> {
            asciiTable.addRow(
                    checkoutRecordEntry.getBookCopy().getBook().getIsbn(),
                    checkoutRecordEntry.getBookCopy().getBook().getTitle(),
                    checkoutRecordEntry.getBookCopy().getCopyNum(),
                    checkoutRecordEntry.getCheckoutDate(),
                    checkoutRecordEntry.getDueDate()
            );
            asciiTable.addRule();
        });
        System.out.println(asciiTable.render());
    }

    public void onBackPressed(MouseEvent mouseEvent) {
        try {
            LibrarianWindow.INSTANCE.init();
            CheckoutRecordTableWindow.INSTANCE.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkInBookButton() {
        try {
            CheckInBookWindow.INSTANCE.init();
            CheckInBookWindow.INSTANCE.setDataAndShow(memberId.getText(), memberName.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
