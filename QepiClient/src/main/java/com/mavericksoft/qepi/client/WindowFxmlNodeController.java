/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.client;

import com.mavericksoft.qepi.util.Protocolo;
import com.mavericksoft.qepi.util.Util;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lsegovia
 */
public class WindowFxmlNodeController implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label labelSala;
    private TextArea textAreaOut;
    @FXML
    private TextField textFieldIn;
    @FXML
    private Button buttonAgregarUsuario;

    private Stage stagePrincipal;

    private String idEscenarioActivo;

    private FXMLDocumentController fXMLDocumentController;
    @FXML
    private ListView listViewOut;

    public FXMLDocumentController getfXMLDocumentController() {
        return fXMLDocumentController;
    }

    public void setfXMLDocumentController(FXMLDocumentController fXMLDocumentController) {
        this.fXMLDocumentController = fXMLDocumentController;
    }

    public String getIdEscenarioActivo() {
        return idEscenarioActivo;
    }

    public void setIdEscenarioActivo(String idEscenarioActivo) {
        this.idEscenarioActivo = idEscenarioActivo;
    }

    public Stage getStagePrincipal() {
        return stagePrincipal;
    }

    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    public Label getLabelSala() {
        return labelSala;
    }

    public void setLabelSala(Label labelSala) {
        this.labelSala = labelSala;
    }

    public TextArea getTextAreaOut() {
        return textAreaOut;
    }

    public void setTextAreaOut(TextArea textAreaOut) {
        this.textAreaOut = textAreaOut;
    }

    public TextField getTextFieldIn() {
        return textFieldIn;
    }

    public void setTextFieldIn(TextField textFieldIn) {
        this.textFieldIn = textFieldIn;
    }

    public Button getButtonAgregarUsuario() {
        return buttonAgregarUsuario;
    }

    public void setButtonAgregarUsuario(Button buttonAgregarUsuario) {
        this.buttonAgregarUsuario = buttonAgregarUsuario;
    }

    public ListView getListViewOut() {
        return listViewOut;
    }

    public void setListViewOut(ListView listViewOut) {
        this.listViewOut = listViewOut;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Platform.setImplicitExit(false);
        textFieldIn.requestFocus();
        textFieldIn.setFocusTraversable(true);
        textFieldIn.setBackground(new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY)));
        listViewOut.setEditable(false);
        listViewOut.setFocusTraversable(false);
        listViewOut.setPadding(new Insets(0));
        listViewOut.setBackground(new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY)));
        agregarChat("", Util.getFecha(), "CENTRO");
    }

    public void agregarChat(String usuario, String mensaje, String alineacion) {
        SVGPath indicadorDireccion = new SVGPath();
        Label bl6 = new Label();
        bl6.setFont(new Font(10));
        bl6.setText(mensaje);
        bl6.setWrapText(true);
        Label bl7 = new Label();
        bl7.setStyle("-fx-font-weight: bold");
        bl7.setFont(new Font(10));
        Label bl8 = new Label();
        bl8.setId("bl8");
        bl8.setFont(new Font(10));
        bl8.setTextFill(Color.web("#615a5a"));
        HBox x = new HBox(0.0);
        x.setBackground(new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY)));
        x.setMaxWidth(listViewOut.getWidth() - 25);

        //bl7.setMinWidth(75);//usuario
        bl7.setMinWidth(Region.USE_PREF_SIZE);//usuario

        //FontMetrics metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(bl7.getFont());
        //bl7.setPadding(new Insets(-metrics.getDescent(), -metrics.getDescent(), -metrics.getDescent(), -metrics.getDescent()));
        //FontMetrics metrics8 = Toolkit.getToolkit().getFontLoader().getFontMetrics(bl8.getFont());
        //bl8.setPadding(new Insets(-metrics8.getDescent(), -metrics8.getDescent(), -metrics8.getDescent(), -metrics8.getDescent()));
        //NB: You need to call that code after scene.show(). Before that graphics engine is not ready to provide correct metrics.
        //bl8.setMinWidth(30); //hora
        //bl8.setPrefWidth(30);
        bl8.setMinWidth(Region.USE_PREF_SIZE); //hora

        //bl6.setMaxWidth(x.getMaxWidth()- (bl7.getMaxWidth()+bl8.getMaxWidth()));
        //bl6.setPrefWidth(x.getMaxWidth()- (bl7.getPrefWidth()+bl8.getPrefWidth()));
        //bl6.setMaxWidth(x.getMaxWidth() - (bl7.getMinWidth() + bl8.getMinWidth())); //original
        bl6.setMaxWidth(x.getMaxWidth() - (bl7.getPrefWidth() + bl8.getPrefWidth())); //nuevo original

        /*
        //se pegan los bordes pero se arregla lo de los puntos suspensivos ...
        bl6.setMinHeight(Region.USE_PREF_SIZE);
        bl7.setMinHeight(Region.USE_PREF_SIZE);
        bl8.setMinHeight(Region.USE_PREF_SIZE);
        x.setMinHeight(Region.USE_PREF_SIZE);
        */
        //puede que esta linea sola arregle lo de los puntos supensivos ... 
        //pero toca hacer mas pruebas sobre el texto pegado, quizas
        //se debe deshabilitar el dar click en la fila hbox o fila
        //de la lista
        //x.setMinHeight(Region.USE_PREF_SIZE);
        
        
        bl7.setPadding(new Insets(0));
        bl8.setPadding(new Insets(0));
        x.setPadding(new Insets(0));
        x.setSpacing(0.0);
        /*
        HBox.setHgrow(bl8, Priority.ALWAYS);
        HBox.setHgrow(bl7, Priority.ALWAYS);
        HBox.setHgrow(bl6, Priority.ALWAYS);
         */
        HBox.setMargin(bl6, new Insets(0, 0, 0, 0));
        HBox.setMargin(bl7, new Insets(0, 0, 0, 0));
        HBox.setMargin(bl8, new Insets(0, 0, 0, 0));
        //bl6.setMinWidth(200); //mensaje
        if (alineacion.equalsIgnoreCase("DERECHA")) {
            x.setAlignment(Pos.TOP_RIGHT);
            //bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,new CornerRadii(5,0,5,5,false), Insets.EMPTY)));
            bl6.setId("bl6d");
            bl6.setAlignment(Pos.TOP_RIGHT);
            bl7.setAlignment(Pos.TOP_RIGHT);
            bl8.setAlignment(Pos.TOP_RIGHT);
            indicadorDireccion.setContent("M10 0 L0 10 L0 0 Z");
            indicadorDireccion.setFill(Color.web("#c3e59d"));
            bl7.setText(" " + usuario);
            bl8.setText(" " + Util.getHora());
            x.getChildren().addAll(bl6, indicadorDireccion, bl7, bl8);
        } else if (alineacion.equalsIgnoreCase("IZQUIERDA")) {
            x.setAlignment(Pos.TOP_LEFT);
            bl6.setTextFill(Color.BLACK);
            //bl6.setBackground(new Background(new BackgroundFill(Color.web("#0096c9"),null, null)));
            bl6.setId("bl6i");
            bl6.setAlignment(Pos.TOP_LEFT);
            bl7.setAlignment(Pos.TOP_LEFT);
            bl8.setAlignment(Pos.TOP_LEFT);
            indicadorDireccion.setContent("M0 0 L10 0 L10 10 Z");
            indicadorDireccion.setFill(Color.web("#91e1ff"));
            bl8.setText(Util.getHora() + " ");
            bl7.setText(usuario + " ");
            x.getChildren().addAll(bl8, bl7, indicadorDireccion, bl6);
        } else {
            Label bl0 = new Label(mensaje);
            bl0.setId("bl0");
            x.setAlignment(Pos.CENTER);
            bl0.setTextFill(Color.web("#fb7dba"));
            //bl0.setBackground(new Background(new BackgroundFill(Color.web("#F4F4F4"),null, null)));
            bl0.setMinWidth(200);
            bl0.setPadding(new Insets(0, 0, 0, 0));
            HBox.setMargin(bl0, new Insets(0, 0, 0, 70));
            bl0.setAlignment(Pos.CENTER);
            x.getChildren().addAll(bl0);
        }
        listViewOut.getItems().add(x);
        listViewOut.scrollTo(listViewOut.getItems().size() - 1);
    }

    @FXML
    private void handletextFieldInSend(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String trama = textFieldIn.getText();
            if (!(trama.trim()).equals("")) {
                textFieldIn.clear();
                if (trama.startsWith("::")) {
                    getfXMLDocumentController().interceptarComando(trama, idEscenarioActivo);
                } else {
                    try {
                        DataOutputStream out = getfXMLDocumentController().getDatosClienteOrigen().getOut();
                        String idPersonajeOrigen = getfXMLDocumentController().getDatosClienteOrigen().getPersonaje().getId()
                                + "|" + getfXMLDocumentController().getDatosClienteOrigen().getPersonaje().getUniverso().getId();
                        peticionAccionChatearEnEscenario(
                                out,
                                idPersonajeOrigen,
                                idEscenarioActivo,
                                getfXMLDocumentController().getIdThreadServidor(),
                                getfXMLDocumentController().getIdThreadDestino(),
                                trama);
                        agregarChat("Yo", trama, "DERECHA");
                    } catch (Exception ex) {
                        Logger.getLogger(WindowFxmlNodeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }

    private void peticionAccionChatearEnEscenario(DataOutputStream out, String idPersonajeOrigen, String idEscenario, String idThread, String idThreadDestino, String trama) throws Exception {
        Protocolo p = new Protocolo(
                Protocolo.RECIBE_FROM_CLIENT,
                Protocolo.TIPO_EVENTO_USUARIO,
                Protocolo.ACTION_CHAT_ROOM,
                Protocolo.RESPONSE_SUCESSFULL,
                idPersonajeOrigen,
                "",
                idEscenario,
                idThread,
                idThreadDestino,
                trama);
        Util.enviarMensajeCliente(out, p);
    }

    @FXML
    private void handleButtonAgregarUsuarioSala(ActionEvent event) {
        windowAddUserChat();
    }

    private void windowAddUserChat() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddUserChatFxmlNode.fxml"));
        AddUserChatFxmlNode windowAddUserChat = new AddUserChatFxmlNode();
        fxmlLoader.setRoot(windowAddUserChat);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        Scene sceneAddUserChat = new Scene(windowAddUserChat);
        AddUserChatFxmlNodeController addUserChatController = fxmlLoader.getController();
        //poniendo personajes conectados observable
        addUserChatController.getListViewAddUserChat().setItems(fXMLDocumentController.getPersonajesConectadosObservable());
        //addUserChatController.getListViewAddUserChat().setDisable(true);
        addUserChatController.setWindowController(this);
        Stage stageAddUserChat = new Stage();
        Image applicationIcon = new Image(getClass().getResourceAsStream("/com/mavericksoft/qepi/recurso/qepi3333.png"));
        stageAddUserChat.getIcons().add(applicationIcon);
        stageAddUserChat.setTitle("Qëpi");
        stageAddUserChat.setScene(sceneAddUserChat);
        stageAddUserChat.initOwner(stagePrincipal);
        //stageAcercaDe.initStyle(StageStyle.UTILITY);
        stageAddUserChat.resizableProperty().setValue(Boolean.FALSE);
        stageAddUserChat.initModality(Modality.APPLICATION_MODAL);
        stageAddUserChat.showAndWait();
    }

}
