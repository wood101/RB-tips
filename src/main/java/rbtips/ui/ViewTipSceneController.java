package rbtips.ui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import rbtips.domain.AppService;
import rbtips.domain.Article;
import rbtips.domain.Tip;
import rbtips.main.Main;

/**
 * FXML Controller class
 *
 */
public class ViewTipSceneController implements Initializable {

    private Main application;
    private AppService appService;
    private Stage stage;

    @FXML
    Label authorLabel;
    @FXML
    Label headlineLabel;
    @FXML
    Hyperlink url;
    @FXML
    Label tagsLabel;

    public void setApplication(Main application) {
        this.application = application;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }

    //TODO siisti metodi
    public void display(String title, Tip t, Parent root1) throws SQLException {
        stage = new Stage();
        stage.setTitle(title);
        authorLabel.setText(t.getAuthor());
        headlineLabel.setText(t.getHeadline());
        Article a = (Article) t;
        tagsLabel.setText(appService.getAllTagsByArticle(a));
        url.setText(a.getUrl());
        url.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    new ProcessBuilder("x-www-browser", url.getText()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.showAndWait();
    }

//    @FXML
//    public void handleClickLink(ActionEvent event) throws Exception {
//        try {
//            Desktop.getDesktop().browse(new URL(url.getText()).toURI());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
