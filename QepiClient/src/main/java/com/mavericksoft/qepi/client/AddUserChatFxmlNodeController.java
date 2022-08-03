/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.client;

import com.mavericksoft.qepi.util.Personaje;
import com.mavericksoft.qepi.util.Protocolo;
import com.mavericksoft.qepi.util.Util;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lsegovia
 */
public class AddUserChatFxmlNodeController implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private ListView listViewAddUserChat;
    
    private WindowFxmlNodeController windowController;
    

    public AnchorPane getAnchorPane() {
        return AnchorPane;
    }

    public void setAnchorPane(AnchorPane AnchorPane) {
        this.AnchorPane = AnchorPane;
    }

    public ListView getListViewAddUserChat() {
        return listViewAddUserChat;
    }

    public void setListViewAddUserChat(ListView listViewAddUserChat) {
        this.listViewAddUserChat = listViewAddUserChat;
    }

    public WindowFxmlNodeController getWindowController() {
        return windowController;
    }

    public void setWindowController(WindowFxmlNodeController windowController) {
        this.windowController = windowController;
    }
    
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleUsuariosListViewAddUserChatAction(MouseEvent event) {
        Personaje personajeDestino = (Personaje)listViewAddUserChat.getSelectionModel().getSelectedItem();
        Node windowAddUserChat = (Node)event.getSource(); 
        Stage stageAddUserChat  = (Stage) windowAddUserChat.getScene().getWindow();
        stageAddUserChat.close();
        peticionAccionAgregarPersonajeAdicionalAlEscenarioChat(personajeDestino);
    }
    
    private void peticionAccionAgregarPersonajeAdicionalAlEscenarioChat(Personaje personajeDestino){
        FXMLDocumentController fxmlDocController = windowController.getfXMLDocumentController();
        String idThreadDestino = "";
        String idPersonajeOrigen = fxmlDocController.getDatosClienteOrigen().getPersonaje().getId()
                +"|"+fxmlDocController.getDatosClienteOrigen().getPersonaje().getUniverso().getId();
        String idPersonajeDestino = personajeDestino.getId()+"|"+personajeDestino.getUniverso().getId();
        for (String claveMapaPersonajesConectados : fxmlDocController.getClavesMapaPersonajesConectados()) {
            if(claveMapaPersonajesConectados.startsWith(idPersonajeDestino)){
                idThreadDestino = claveMapaPersonajesConectados.substring(claveMapaPersonajesConectados.indexOf("|",claveMapaPersonajesConectados.indexOf("|")+1)+1);
                break;
            }
        }
        String idEscenario = windowController.getIdEscenarioActivo();
        String trama = "";
        Protocolo p = new Protocolo(
                Protocolo.RECIBE_FROM_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO,
                Protocolo.ACTION_ADD_ALIAS_CHAT_ROOM,
                Protocolo.RESPONSE_SUCESSFULL,
                idPersonajeOrigen, 
                idPersonajeDestino, 
                idEscenario, 
                fxmlDocController.getIdThreadServidor(),
                idThreadDestino,
                trama);
        try {
            Util.enviarMensajeCliente(fxmlDocController.getDatosClienteOrigen().getOut(), p);
            windowController.getTextAreaOut().appendText("Agregando a "+personajeDestino.getNombre()+" \n");
        } catch (Exception ex) {
            System.out.println("windowController: "+windowController);
            System.out.println("getTextAreaOut(): "+windowController.getTextAreaOut());
            System.out.println("personajeDestino: "+personajeDestino.getNombre());
            windowController.getTextAreaOut().appendText("Problemas al agregar a "+personajeDestino.getNombre()+" \n");
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
}
