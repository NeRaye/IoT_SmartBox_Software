package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ClientController implements Initializable{

    public Label lab;
    public JFXButton client_depot;
    public JFXButton client_retirer;
    public AnchorPane anchor2;
    @FXML
    private AnchorPane anchorpane;

    @FXML
    private JFXTextField client_num_colis;

    @FXML
    private JFXPasswordField client_password;

    @FXML
    private JFXButton langue;

    @FXML
    private Label client_label;

    @FXML
    private JFXButton client_livreur_btn;

    private Client client;
    private WebTarget target;

    static String languechoisie = "francais";


    @FXML
    void sendToActivityLivreur(ActionEvent event) throws IOException {

            //ouverture
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("livreur_login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 550);
            Stage stage2 = new Stage();
            stage2.setTitle("Easy-Relay");
            stage2.setScene(scene);
            stage2.show();

            //Fermeture
            Stage stage1 = (Stage) client_livreur_btn.getScene().getWindow();
            stage1.close();
    }

    @FXML
    void verifier_depot_client(ActionEvent event) {


        String idColis = client_num_colis.getText();

        String code = client_password.getText();

        //verifier que le client a introduit le champs idColis et le champs mdp

        if (idColis.equals("")) {
            if (languechoisie.equals("arabe")){
                lab.setText("قم بادخال رقم الطلبية ");
            }
            else{lab.setText("Vous devez inseré le code du colis");}
        } else if (code.equals("")) {


            if (languechoisie.equals("arabe")){
                lab.setText("قم بادخال الرقم السري ");
            }
            else{lab.setText("Vous devez inseré votre code");}

        } else if (Pattern.matches("[0-9]+", code) ||Pattern.matches("[0-9]+", idColis) ) {

            //utiliser l'API "" pour verifier si le code du client et le idProduit sont valides ou pas

            client = ClientBuilder.newClient();
            target = client.target("https://tst2.easy-relay.com/p")
                    .queryParam("action", "auth_client").queryParam("id_livraison", idColis);

            String response = target.request(MediaType.APPLICATION_JSON).header("code", code).get(String.class);

            if (response.equals("true")) {
                //le code et le id_livraison sont valides alors lui afficher le numero de box

                //appeler l'API qui donne le numero de box
                client = ClientBuilder.newClient();
                target = client.target("http://localhost/API/get_num_box.php")
                        .queryParam("id_Colis", idColis);

                String numero = target.request(MediaType.APPLICATION_JSON).get(String.class);
                System.out.println(numero);

                Label label = new Label();

                if (languechoisie.equals("arabe"))
                {
                    label.setText("طلبيتكم في الصندوق رقم:" + numero);
                }else{
                    label.setText(" Votre colis est dans le box numero: " + numero);
                }

                anchorpane.getChildren().clear();
                anchorpane.getChildren().add(label);

            } else {
                if (languechoisie.equals("arabe")){
                    lab.setText("الرقم السري او رقم الطلبية خاطئ");
                }
                else{lab.setText("Le code ou l'identifiant du colis est erroné");}
            }
        }else {
            if (languechoisie.equals("arabe")){
                lab.setText("الرقم السري او رقم الطلبية خاطئ");
            }
            else{lab.setText("Le code ou l'identifiant du colis est erroné");}
        }
    }

    @FXML
    public void BasculerLang(ActionEvent actionEvent) {


        String lang = langue.getText();

            if (lang.equals("عربية")) {
                langue.setText("Français");
                client_label.setText("يمكنك ان تقوم بوضع او ازالة طردك");
                client_num_colis.setPromptText("أدخل رقم طلبيتك");
                client_password.setPromptText("أدخل الرقم السري الخاص بك");
                client_num_colis.setText("");
                client_password.setText("");
                client_livreur_btn.setText("عامل توصيل؟");
                client_livreur_btn.setLayoutX(50);
                client_depot.setText("ايداع");
                client_retirer.setText("استعادة");

                languechoisie = "arabe";
            }else
                {
                    langue.setText("عربية");
                    client_label.setText("Retirer ou deposer un colis");
                    client_num_colis.setPromptText("Entrer le numero de commande");client_num_colis.setText("");
                    client_password.setPromptText("Entrer le mot de passe");client_password.setText("");
                    client_livreur_btn.setText("Êtes vous un livreur");
                    client_livreur_btn.setLayoutX(613);
                    client_depot.setText("Deposer");
                    client_retirer.setText("Retirer");

                    languechoisie = "francais";
                }

        }

    public void verifier_retrait_client(ActionEvent actionEvent) {


        String idColis = client_num_colis.getText();

        String code = client_password.getText();

        //verifier que le client a introduit le champs idColis et le champs mdp
        if (idColis.equals("")) {
            if (languechoisie.equals("arabe")){
                lab.setText("قم بادخال رقم الطلبية ");
            }
            else{lab.setText("Vous devez inseré le code du colis");}
        } else if (code.equals("")) {


            if (languechoisie.equals("arabe")){
                lab.setText("قم بادخال الرقم السري ");
            }
            else{lab.setText("Vous devez inseré votre code");}
        } else if (Pattern.matches("[0-9]+", code) && Pattern.matches("[0-9]+", idColis) ) {

            //utiliser l'API "Récupération du colis par le client" pour verifier si le code du client et le idProduit sont valides ou pas

            client = ClientBuilder.newClient();
            target = client.target("https://tst2.easy-relay.com/api/iot/api.php")
                    .queryParam("action", "auth_client").queryParam("id_livraison", idColis);

            String response = target.request(MediaType.APPLICATION_JSON).header("code", code).get(String.class);

            if (response.equals("true")) {
                //le code et le id_livraison sont valides alors lui afficher le numero de box

                //appeler l'API qui donne le numero de box
                client = ClientBuilder.newClient();
                target = client.target("http://localhost/API/get_num_box.php")
                        .queryParam("id_Colis", idColis);

                String numero = target.request(MediaType.APPLICATION_JSON).get(String.class);

                Label label = new Label();
                if (languechoisie.equals("arabe"))
                {
                    label.setText("طلبيتكم في الصندوق رقم:" + numero);
                }else{
                    label.setText(" Votre colis est dans le box numero: " + numero);
                }

                anchorpane.getChildren().clear();
                anchorpane.getChildren().add(label);

                langue.setText("");
                //masquer le boutton etes vous un livreur
                anchor2.getChildren().remove(client_livreur_btn);

            } else {

                if (languechoisie.equals("arabe")){
                    lab.setText("الرقم السري او رقم الطلبية خاطئ");
                }
                else{lab.setText("Le code ou l'identifiant du colis est erroné");}

            }
        }else {
            if (languechoisie.equals("arabe")){
                lab.setText("الرقم السري او رقم الطلبية خاطئ");
            }
            else{lab.setText("Le code ou l'identifiant du colis est erroné");}
        }

    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        if (languechoisie.equals("arabe")) {
            langue.setText("Français");
            client_label.setText("يمكنك ان تقوم بوضع او ازالة طردك");
            client_num_colis.setPromptText("أدخل رقم طلبيتك");
            client_password.setPromptText("أدخل الرقم السري الخاص بك");
            client_num_colis.setText("");
            client_password.setText("");
            client_livreur_btn.setText("عامل توصيل؟");
            client_livreur_btn.setLayoutX(50);
            client_depot.setText("ايداع");
            client_retirer.setText("استعادة");

        }

    }
}

