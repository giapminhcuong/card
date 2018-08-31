package com.cuong.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import com.cuong.service.WordService;
import com.cuong.utils.PathUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

public class MainController extends BaseController implements Initializable {

	public static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

	private WordService wordService = new WordService();

	@FXML
	private MenuItem menuItemCard;

	@FXML
	private StackPane centerPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	private void onActionShowCard(ActionEvent event) {
		CardController cardController = new CardController();
		cardController.setWordList(wordService.loadAll());
		setMainPane("Card.fxml", cardController);

	}

	private void setMainPane(String viewFileName, Object controller) {
		try {
			centerPane.getChildren().clear();
			FXMLLoader fxmlLoader = new FXMLLoader(PathUtils.getViewPath(viewFileName));
			fxmlLoader.setController(controller);
			Parent root = fxmlLoader.load();
			centerPane.getChildren().add(root);
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}

}
