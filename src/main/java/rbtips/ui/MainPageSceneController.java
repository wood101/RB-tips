package rbtips.ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rbtips.main.Main;
import rbtips.domain.*;


public class MainPageSceneController {
    
    private Main application;
    private AppService appService;
    private ObservableList<Tip> articleList;
//    private ObservableList<Tag> tagList;
    private AddNewTipSceneController addNew;
    private ViewTipSceneController viewTip;
    
    @FXML private TableView<Tip> tableView;
//    @FXML private TableView<Article> tableView;
    @FXML private TextField filter;
    @FXML private TextField tagFilter;
    
    public void setApplication(Main application) {
        this.application = application;
    }
    
    public void setAppService(AppService appService) {
        this.appService = appService;
    }
    
    public void setArticles() throws SQLException {
        this.articleList = FXCollections.observableArrayList(this.appService.getAllArticles());
//        this.tagList = FXCollections.observableArrayList(this.appService.getAllTagsByArticle());
        tableView.setItems(articleList);
    }
    
    @FXML
    private void openAddNewTipScene(ActionEvent event) throws SQLException, IOException {
        initializeAddNewTipScene();
        setArticles();
    }
    
    public void initializeAddNewTipScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddNewTipScene.fxml"));
        Parent root1 = (Parent) loader.load();
        addNew = loader.getController();
        addNew.setApplication(application);
        addNew.setAppService(appService);
        addNew.display("Add new tip!", "Input information", root1);
    }
    
    @FXML
    private void handleViewContentButton(ActionEvent event) throws IOException, SQLException {
        if(!tableView.getSelectionModel().isEmpty()) {
            Tip t = tableView.getSelectionModel().getSelectedItem();
            setViewTipScene(t);
        }
    }
    
    @FXML
    private void handleFilterButton(ActionEvent event) throws IOException {
        String filterCondition = filter.getText();
        String tagCondition = tagFilter.getText();
        //ObservableList<Tip> filteredArticleList = FXCollections.observableArrayList(appService.searchHeadline(filterCondition));
        ObservableList<Tip> filteredArticleList = FXCollections.observableArrayList(appService.filterArticles(filterCondition, tagCondition));
        tableView.setItems(filteredArticleList);
    }
    
    /*@FXML
    private void handleTagFilterButton(ActionEvent event) throws IOException {
        String filterCondition = tagFilter.getText();
        ObservableList<Tip> filteredArticleList = FXCollections.observableArrayList(appService.searchTag(filterCondition));
        tableView.setItems(filteredArticleList);
    }*/
    
    @FXML
    private void handleShowAllTips(ActionEvent event) throws Exception {
        this.setArticles();
    }
    
    public void setViewTipScene(Tip t) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ViewTipScene.fxml"));
        Parent root1 = (Parent) loader.load();
        viewTip = loader.getController();
        viewTip.setApplication(application);
        viewTip.setAppService(appService);
        viewTip.display(t.getAuthor() + ", " + t.getHeadline(), t, root1);
    }
    
    
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//    }    
    
}
