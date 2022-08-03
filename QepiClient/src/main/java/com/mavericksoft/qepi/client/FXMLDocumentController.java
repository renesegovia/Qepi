/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.client;

import com.mavericksoft.qepi.util.Escenario;
import com.mavericksoft.qepi.util.Personaje;
import com.mavericksoft.qepi.util.Protocolo;
import com.mavericksoft.qepi.util.Util;
import com.mavericksoft.qepi.util.DatosCliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 *
 * @author lsegovia
 */
public class FXMLDocumentController implements Initializable {
    
    private Util util;
    private DatosCliente datosClienteOrigen;
    private ObservableList<Personaje> personajesObservable;
    private ObservableList<Personaje> personajesConectadosObservable;

    @FXML
    private ComboBox aliasComboBox;
    @FXML
    private ListView usuariosListView;
    @FXML
    private Label nombreSistemaLb;
    
    private ToggleGroup group;
    @FXML
    private ToggleButton onToggleButton;
    @FXML
    private ToggleButton offToggleButton;
    //@FXML
    //private AnchorPane AnchorPane;
    private Stage stagePrincipal;
    @FXML
    private Label mensajeLb;
    private HiloProcesaRespuesta procesarRespuesta;
    private String idThreadServidor;
    private String idThreadDestino;
    private List<String> clavesMapaPersonajesConectados;
    private Map<String, Stage> stagesChatporIdEscenario;
    private Map<String, WindowFxmlNodeController> windowFxmlNodeControllersPorIdEscenario;

    public Map<String, Stage> getStagesChatporIdEscenario() {
        return stagesChatporIdEscenario;
    }

    public void setStagesChatporIdEscenario(Map<String, Stage> stagesChatporIdEscenario) {
        this.stagesChatporIdEscenario = stagesChatporIdEscenario;
    }
    
    public List<String> getClavesMapaPersonajesConectados() {
        return clavesMapaPersonajesConectados;
    }

    public void setClavesMapaPersonajesConectados(List<String> clavesMapaPersonajesConectados) {
        this.clavesMapaPersonajesConectados = clavesMapaPersonajesConectados;
    }
    
    public Stage getStagePrincipal() {
        return stagePrincipal;
    }

    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    public DatosCliente getDatosClienteOrigen() {
        return datosClienteOrigen;
    }

    public void setDatosClienteOrigen(DatosCliente datosClienteOrigen) {
        this.datosClienteOrigen = datosClienteOrigen;
    }

    public ObservableList<Personaje> getPersonajesObservable() {
        return personajesObservable;
    }

    public void setPersonajesObservable(ObservableList<Personaje> personajesObservable) {
        this.personajesObservable = personajesObservable;
    }

    public ObservableList<Personaje> getPersonajesConectadosObservable() {
        return personajesConectadosObservable;
    }

    public void setPersonajesConectadosObservable(ObservableList<Personaje> personajesConectadosObservable) {
        this.personajesConectadosObservable = personajesConectadosObservable;
    }

    public ComboBox getAliasComboBox() {
        return aliasComboBox;
    }

    public void setAliasComboBox(ComboBox aliasComboBox) {
        this.aliasComboBox = aliasComboBox;
    }

    public ListView getUsuariosListView() {
        return usuariosListView;
    }

    public void setUsuariosListView(ListView usuariosListView) {
        this.usuariosListView = usuariosListView;
    }

    public Util getUtil() {
        return util;
    }

    public void setUtil(Util util) {
        this.util = util;
    }

    public Label getMensajeLb() {
        return mensajeLb;
    }

    public void setMensajeLb(Label mensajeLb) {
        this.mensajeLb = mensajeLb;
    }

    public ToggleButton getOnToggleButton() {
        return onToggleButton;
    }

    public void setOnToggleButton(ToggleButton onToggleButton) {
        this.onToggleButton = onToggleButton;
    }

    public ToggleButton getOffToggleButton() {
        return offToggleButton;
    }

    public void setOffToggleButton(ToggleButton offToggleButton) {
        this.offToggleButton = offToggleButton;
    }

    public String getIdThreadServidor() {
        return idThreadServidor;
    }

    public void setIdThreadServidor(String idThreadServidor) {
        this.idThreadServidor = idThreadServidor;
    }

    public String getIdThreadDestino() {
        return idThreadDestino;
    }

    public void setIdThreadDestino(String idThreadDestino) {
        this.idThreadDestino = idThreadDestino;
    }
    
    
    
    
    
    @FXML
    private void handleOnToggleButtonAction(ActionEvent event) {
        if(onToggleButton.isSelected()){
            onToggleButton.setSelected(true);
            offToggleButton.setSelected(false);
            //System.out.println("You clicked me On!");
            peticionAccionConectarPersonaje();
        }else{
            onToggleButton.setSelected(false);
            offToggleButton.setSelected(true);
            peticionAccionDesconectarPersonaje();
        }
    }
    
    @FXML
    private void handleOffToggleButtonAction(ActionEvent event) {
        if(offToggleButton.isSelected()){
            //System.out.println("You clicked me Off!");
            offToggleButton.setSelected(true);
            onToggleButton.setSelected(false);
            aliasComboBox.setDisable(false);
            peticionAccionDesconectarPersonaje();
        }else{
            offToggleButton.setSelected(false);
            onToggleButton.setSelected(true);
            peticionAccionConectarPersonaje();
        }
    }
    
    /**
     * Dibuja las ventanas de chat en los participantes
     * @param p corresponde a los datos con los que llega desde el servidor
     */
    public void windowChat( final Protocolo p) {
        /*
        Stage stageWindowChat = new Stage();
        WindowFxmlNode windowChatRoot = new WindowFxmlNode();
        Scene sceneChat = new Scene(windowChatRoot);
        Image applicationIcon = new Image(getClass().getResourceAsStream("/com/mavericksoft/qepi/recurso/qepi33.png"));
        stageWindowChat.getIcons().add(applicationIcon);      
        stageWindowChat.setTitle("Qëpi");
        stageWindowChat.setScene(sceneChat);
        stageWindowChat.show();
        */
        Stage stageWindowChat = new Stage();
        Image applicationIcon = new Image(getClass().getResourceAsStream("qepi3333.png"));
        stageWindowChat.getIcons().add(applicationIcon);      
        stageWindowChat.setTitle("Qëpi");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WindowFxmlNode.fxml"));
        //OJO WindowFxmlNode es un AnchorPane
        WindowFxmlNode windowNodeChat = new WindowFxmlNode();
        fxmlLoader.setRoot(windowNodeChat);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        Scene sceneChat = new Scene(windowNodeChat);
        WindowFxmlNodeController wController = fxmlLoader.getController();
        //guardando en la clase el idEscenarioActivo que es la clave del mapa de escenarios activos del servidor
        wController.setIdEscenarioActivo(p.getIdEscenario());
        Escenario escenario = util.getEscenario(p.getIdEscenario());
        datosClienteOrigen.setEscenario(escenario);
        wController.setfXMLDocumentController(this);
        wController.getLabelSala().setText(escenario.getNombre());
        procesarRespuesta.getwControllers().add(wController);
        stageWindowChat.setScene(sceneChat);
        stageWindowChat.initOwner(stagePrincipal);
        stageWindowChat.resizableProperty().setValue(Boolean.FALSE);
        //stageWindowChat.initStyle(StageStyle.UTILITY);
        stagesChatporIdEscenario.put(p.getIdEscenario(), stageWindowChat);
        windowFxmlNodeControllersPorIdEscenario.put(p.getIdEscenario(), wController);
        stageWindowChat.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                peticionAccionCerrarEscenarioChat(p.getIdEscenario());
            }
        });
        stagePrincipal.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                peticionAccionCerrarEscenarioChat(p.getIdEscenario());
                peticionAccionDesconectarPersonaje();
            }
        });
        stageWindowChat.show();
    }
    
    
    public void interceptarComando(String trama, String idEscenario){
        switch(trama){
            case "::cls":
                windowFxmlNodeControllersPorIdEscenario.get(idEscenario).getListViewOut().getItems().clear();
                break;
            case "::min":
                stagesChatporIdEscenario.get(idEscenario).setIconified(true);
                stagePrincipal.setIconified(true);
                break;
            case "::bye":
                peticionAccionCerrarEscenarioChat(idEscenario);
                stagesChatporIdEscenario.get(idEscenario).close();
                break;
            case "::exi":
                peticionAccionCerrarEscenarioChat(idEscenario);
                peticionAccionDesconectarPersonaje();
                stagePrincipal.close();
                Platform.exit();
                break;    
            default:
                break;
        }
    }
    
    
    private void peticionAccionCerrarEscenarioChat(String idEscenarioActivo){
        String idPersonajeOrigen = datosClienteOrigen.getPersonaje().getId()+"|"+datosClienteOrigen.getPersonaje().getUniverso().getId();
        String idPersonajeDestino = "";
        String idThreadDestino = "";
        String idEscenario = idEscenarioActivo;
        String trama = datosClienteOrigen.getPersonaje().getNombre()+" salió de "+util.getEscenario(idEscenarioActivo).getNombre();
        Protocolo p = new Protocolo(
                Protocolo.RECIBE_FROM_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO,
                Protocolo.ACTION_CLOSE_CHAT_ALIAS_ROOM,
                Protocolo.RESPONSE_SUCESSFULL,
                idPersonajeOrigen, 
                idPersonajeDestino, 
                idEscenario, 
                idThreadServidor,
                idThreadDestino,
                trama);
        try {
            Util.enviarMensajeCliente(datosClienteOrigen.getOut(), p);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    
    public void alertaMensaje(String idEscenarioActivo){
        Stage stage = getStagesChatporIdEscenario().get(idEscenarioActivo);
        if(stage.isIconified() || !stage.isFocused()){
            try {
                //Para que la linea siguiente funcione debo copiar mp3 en el directorio de esta clase.
                //Media hit = new Media(getClass().getResource("pin_dropping-Brian_Rocca.mp3").toURI().toString());
                //Media hit = new Media(getClass().getResource("pin_dropping-Brian_Rocca.mp3").toURI().toString());
                //En Ubuntu no funciona bien el mp3 en el MediaPlayer, hay que instalar jdk9 o mejor usar .wav
                Media hit = new Media(getClass().getResource("pin_dropping-Brian_Rocca.wav").toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit); 
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.setVolume(0.9);   // from 0 to 1  
                mediaPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargar();
    }
    
    private void cargar(){
        util = new Util();
        ToggleGroup group = new ToggleGroup();
        onToggleButton.setToggleGroup(group);
        offToggleButton.setToggleGroup(group);
        offToggleButton.setSelected(true);
        onToggleButton.setSelected(false);
        personajesObservable = FXCollections.observableArrayList();
        personajesObservable.addAll(util.getPersonajes());
        personajesObservable.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                //System.out.println(".onChanged()");
            }
        });
        personajesConectadosObservable = FXCollections.observableArrayList();
        personajesConectadosObservable.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
            }
        });
        /*
         // Se insta una lista.
        List<String> list = new ArrayList<String>();
        list.add("Clark");
        list.add("Peter");
        list.add("Luke");
        // se agrega la observación usando FXCollections:
        ObservableList<String> observableList = FXCollections.observableList(list);
        observableList.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                //System.out.println("Ocurrio un cambio! ");
            }
        });
        // Se reportan cambios a observableList.
        // Aquí se va imprimir la alerta 
        observableList.add("Aquaman");
        // cambios directas a la lista escapan la observación
        // no se imprime ninguna alerta 
        list.add("Thor");
        //System.out.println("Tamano: "+observableList.size());
        aliasComboBox.setItems(observableList);
        */
        //aliasComboBox = new ComboBox<>(o);
        aliasComboBox.setItems(personajesObservable);
        aliasComboBox.getSelectionModel().selectFirst();
        //aliasComboBox.show();
        //System.out.println("ObservaleList o: "+personajesObservable);
        usuariosListView.setItems(personajesConectadosObservable);
        //usuariosListView.setDisable(true);
        clavesMapaPersonajesConectados = new ArrayList<>();
        stagesChatporIdEscenario = new ConcurrentHashMap<>();
        windowFxmlNodeControllersPorIdEscenario = new ConcurrentHashMap<>();
    }
    

    @FXML
    private void handleViewAcercaDeQuepiLabelAction(MouseEvent event) {
        windowAcercaDe();
    }
    
    
    private void windowAcercaDe() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AcercaDeFxmlNode.fxml"));
        AnchorPane AnchorPaneAcercaDe = new AnchorPane();
        fxmlLoader.setRoot(AnchorPaneAcercaDe);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        Stage stageAcercaDe = new Stage();
        Scene sceneAcercaDe = new Scene(AnchorPaneAcercaDe);
        Image applicationIcon = new Image(getClass().getResourceAsStream("qepi3333.png"));
        stageAcercaDe.getIcons().add(applicationIcon);      
        stageAcercaDe.setTitle("Qëpi");
        stageAcercaDe.setScene(sceneAcercaDe);
        stageAcercaDe.initOwner(stagePrincipal);
        //stageAcercaDe.initStyle(StageStyle.UTILITY);
        stageAcercaDe.resizableProperty().setValue(Boolean.FALSE);
        stageAcercaDe.initModality(Modality.APPLICATION_MODAL); 
        stageAcercaDe.showAndWait();
    }
    
    
    private void peticionAccionConectarPersonaje(){
        Personaje personajeOrigen = ((Personaje)aliasComboBox.getValue()); 
        Socket clientSocket;
        DataInputStream in;
        DataOutputStream out;
        String idPersonajeOrigen = personajeOrigen.getId()+"|"+personajeOrigen.getUniverso().getId();
        String idEscenario = "";
        String idPersonajeDestino = "";
        String idThread = "";
        String idThreadDestino = "";
        String trama = "";
        Protocolo p = new Protocolo(
                Protocolo.RECIBE_FROM_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO,
                Protocolo.ACTION_CONNECT,
                Protocolo.RESPONSE_SUCESSFULL,
                idPersonajeOrigen, 
                idPersonajeDestino, 
                idEscenario,
                idThread,
                idThreadDestino,
                trama);
        try {
            clientSocket = new Socket(util.getRecursoConexion().getServidor(), util.getRecursoConexion().getPuerto());
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            datosClienteOrigen = new DatosCliente(personajeOrigen, null, in, out);
            procesarRespuesta = new HiloProcesaRespuesta(this);
            procesarRespuesta.setDaemon(true);
            procesarRespuesta.start();
            Util.enviarMensajeCliente(out, p);
            mensajeLb.setText("Conectado");
        } catch (IOException ex) {
            mensajeLb.setText("Servidor indispuesto");
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            mensajeLb.setText("Servidor indispuesto");
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void peticionAccionDesconectarPersonaje(){
        Personaje personajeOrigen = ((Personaje)aliasComboBox.getValue()); 
        String idPersonajeOrigen = personajeOrigen.getId()+"|"+personajeOrigen.getUniverso().getId();
        String idEscenario = "";
        String idPersonajeDestino = "";
        String idThread = "";
        String idThreadDestino = "";
        String trama = personajeOrigen.getNombre()+" desconectado";
        Protocolo p = new Protocolo(
                Protocolo.RECIBE_FROM_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO,
                Protocolo.ACTION_DISCONNECT,
                Protocolo.RESPONSE_SUCESSFULL,
                idPersonajeOrigen, 
                idPersonajeDestino, 
                idEscenario,
                idThread,
                idThreadDestino,
                trama);
        try {
            Util.enviarMensajeCliente(datosClienteOrigen.getOut(), p);
            datosClienteOrigen = null;
            mensajeLb.setText("Desconectado");
        } catch (IOException ex) {
            mensajeLb.setText("Servidor indispuesto");
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            mensajeLb.setText("Servidor indispuesto");
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleUsuariosListViewAction(MouseEvent event) {
        Personaje personajeDestino = (Personaje)usuariosListView.getSelectionModel().getSelectedItem();
        if( personajeDestino!=null 
            && ( !personajeDestino.getId().equalsIgnoreCase(datosClienteOrigen.getPersonaje().getId()) 
            || !personajeDestino.getUniverso().getId().equalsIgnoreCase(datosClienteOrigen.getPersonaje().getUniverso().getId())) ){
            mensajeLb.setText("");
            peticionAccionAgregarPersonajeAlEscenarioChat(personajeDestino);
        }else{
            mensajeLb.setText("Chatee con otro personaje");
        }
    }
    
    
    private void peticionAccionAgregarPersonajeAlEscenarioChat(Personaje personajeDestino){
        String idPersonajeOrigen = datosClienteOrigen.getPersonaje().getId()+"|"+datosClienteOrigen.getPersonaje().getUniverso().getId();
        String idPersonajeDestino = personajeDestino.getId()+"|"+personajeDestino.getUniverso().getId();
        for (String claveMapaPersonajesConectados : clavesMapaPersonajesConectados) {
            if(claveMapaPersonajesConectados.startsWith(idPersonajeDestino)){
                idThreadDestino = claveMapaPersonajesConectados.substring(claveMapaPersonajesConectados.indexOf("|",claveMapaPersonajesConectados.indexOf("|")+1)+1);
                break;
            }
        }
        String idEscenario = "";
        String trama = "";
        Protocolo p = new Protocolo(
                Protocolo.RECIBE_FROM_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO,
                Protocolo.ACTION_ADD_ALIAS_CHAT_ROOM,
                Protocolo.RESPONSE_SUCESSFULL,
                idPersonajeOrigen, 
                idPersonajeDestino, 
                idEscenario, 
                idThreadServidor,
                idThreadDestino,
                trama);
        try {
            Util.enviarMensajeCliente(datosClienteOrigen.getOut(), p);
            mensajeLb.setText("");
        } catch (Exception ex) {
            mensajeLb.setText("Problemas de comunicacion");
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
