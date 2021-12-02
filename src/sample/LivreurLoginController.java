package sample;

        import com.jfoenix.controls.JFXButton;
        import com.jfoenix.controls.JFXPasswordField;
        import com.jfoenix.controls.JFXTextField;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Scene;
        import javafx.scene.control.Hyperlink;
        import javafx.scene.control.Label;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.Stage;

        import javax.json.JsonArray;
        import javax.ws.rs.client.Client;
        import javax.ws.rs.client.ClientBuilder;
        import javax.ws.rs.client.WebTarget;
        import javax.ws.rs.core.MediaType;
        import java.io.IOException;
        import java.net.URL;
        import java.util.ResourceBundle;
        import java.util.regex.Pattern;

public class LivreurLoginController implements Initializable{


    public AnchorPane anchorExt;
    public AnchorPane anchorint;
    public Hyperlink livreur_retour;
    @FXML
    private JFXTextField livreur_email;

    @FXML
    private JFXPasswordField livreur_password;

    @FXML
    private JFXButton livreur_connecter;

    @FXML
    private Label livreur_label;

    @FXML
    private Label lab;

    private Client client;
    private WebTarget target;
    
    @FXML
    void back_btn(ActionEvent event) throws IOException {


        //ouverture
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("client.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
        Stage stage2 = new Stage();
        stage2.setTitle("Easy-Relay");
        stage2.setScene(scene);
        stage2.show();

        //Fermeture
        Stage stage1 = (Stage) livreur_retour.getScene().getWindow();
        stage1.close();
    }

    @FXML
    void login_btn(ActionEvent event) {


        String email = livreur_email.getText();

        String mdp = livreur_password.getText();

        //verifier que le client a introduit le champs idColis et le champs mdp
try{
        if (email.equals("")) {
            lab.setText("Vous devez inseré votre email");
        } else if (mdp.equals("")) {
            lab.setText("Vous devez inseré votre mot de passe");
        } else if (email.endsWith("@easy-relay.com") ) {

            //utiliser l'API "Authentification du livreur" pour verifier si le code du client et le idProduit sont valides ou pas

            client = ClientBuilder.newClient();
            target = client.target("https://tst2.easy-relay.com/ /api/mob2/api.php")
                    .queryParam("action", "login");

            JsonArray response = target.request(MediaType.APPLICATION_JSON).header("email",email).header("mdp",mdp).get(JsonArray.class);


            if (response.getJsonObject(0).get("error").equals("Message de l’erreur")) {
                //Si l'API retourne un error
                lab.setText("L'email est erroné.");

            } else {

            }
        }else {
            lab.setText("L'email que vous avez inséré ne correspond pas.");
        }}catch (javax.ws.rs.NotFoundException e){
            e.getMessage();
            lab.setText("err");
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
        if (ClientController.languechoisie.equals("arabe"))
        {
            //anchorExt.getChildren().clear();
            livreur_retour.setText("الرجوع الى الصفحة السابقة");

            //anchorint.getChildren().clear();

            livreur_label.setText("قم بلالتصال بحسابك");
            livreur_email.setPromptText("قم بادخال حسابك الالكتروني");
            livreur_password.setPromptText("قم بادخال كلمة السر");
            livreur_connecter.setText("اتصال");

        }
    }
}
