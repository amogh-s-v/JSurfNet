package com.example.jsurfnet.views;

import com.example.jsurfnet.Prototype.TabImpl;
import com.example.jsurfnet.Prototype.TabPrototype;
import com.example.jsurfnet.controllers.TabsController;
import com.example.jsurfnet.models.History;
import com.example.jsurfnet.services.Icon;
import com.example.jsurfnet.services.IconBuilder;
import com.example.jsurfnet.services.TabBuilder;
import com.example.jsurfnet.services.TableFactory;
import com.example.jsurfnet.singleton.TabsAndWv;
import com.example.jsurfnet.singleton.ToolBar;
import com.example.jsurfnet.controllers.webHistory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class webHistoryView extends TableFactory {

    private static Tab historyTab = null;

    @Override
    public void render() {
        if (historyTab == null) {
            historyTab = new Tab();
            historyTab.setGraphic(new IconBuilder("/icons/history.png", 20, 20).getIcon());
        }
        historyTab.setText("History");

        // Create a VBox to hold the list of history items and clear history button
        VBox historyList = new VBox();
        historyList.setAlignment(Pos.TOP_LEFT);
        historyList.setSpacing(10);
        historyList.setPadding(new Insets(10));
        historyList.setStyle("-fx-background-color: #1C1C1C;");

        // Create a ScrollPane to display the history list
        ScrollPane scrollPane = new ScrollPane(historyList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1C1C1C;");

        // Load the user's history from the database
        webHistory userHistory = ToolBar.getInstance().getHistory();
        List<History> historyItems = userHistory.getList();

        // Create a TableView to display the history items in a tabular format
        TableView<History> historyTable = new TableView<>();
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setPrefHeight(400);
        historyTable.setStyle("-fx-font-size: 14; -fx-font-family: 'Arial'; -fx-background-color: #1C1C1C; -fx-text-fill: white;");
        historyTable.setPlaceholder(new Label("No History!"));

        TableColumn<History, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setPrefWidth(600);
        urlColumn.setCellFactory(column -> {
            TableCell<History, String> cell = new TableCell<History, String>() {
                @Override
                protected  void updateItem(String url, boolean empty) {
                    super.updateItem(url, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        url = url.strip();
                        if (!url.isEmpty()) {
                            setText(url);
                            setGraphic(new Icon(url).getImage());
                        }
                    }
                }
            };
            cell.setOnMouseClicked((event) -> {
                if (!(cell.isEmpty()) && event.getButton().equals(MouseButton.PRIMARY)) {
                    Tab newTab = new TabBuilder(new Tab(TabsController.gethost(cell.getText()))).getTab();
                    WebView webView = new WebView();
                    WebEngine webEngine = webView.getEngine();
                    webEngine.load(cell.getText());
                    newTab.setContent(webView);
                    TabsAndWv.getInstance().getTabPane().getTabs().add(newTab);
                    TabPane tabPane = TabsAndWv.getInstance().getTabPane();
                    TabsAndWv.getInstance().setTabPane(tabPane);
                    new TabsController().ListnersForWebView(newTab, webEngine, TabsAndWv.getInstance().getTabPane());
                    TabsAndWv.getInstance().getTabPane().getSelectionModel().select(newTab);
                    ToolBar.getInstance().getUrlField().setText(cell.getText());
                }
            });
            return cell;
        });

        TableColumn<History, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("\uD83D\uDDD1");

            {
                deleteButton.setOnAction(event -> {
                    History historyItem = getTableView().getItems().get(getIndex());
                    ToolBar.getInstance().getHistory().getList().remove(historyItem);
                    new webHistoryView().render();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        TableColumn<History, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setPrefWidth(200);

        historyTable.getColumns().addAll(urlColumn, timeColumn, deleteColumn);
        historyTable.getItems().addAll(historyItems);

        // Add the TableView to the VBox
        historyList.getChildren().add(historyTable);

        // Create a button to clear the history
        Button clearButton = new Button("Clear History");
        clearButton.setStyle("-fx-background-color: #63D471; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");
        clearButton.setOnAction(event -> {
            if (JOptionPane.showConfirmDialog(null, "Confirm delete history?") == 0) {
                ToolBar.getInstance().getHistory().clear();
                new webHistoryView().render();
            }
        });

        // Add the clear button to the VBox
        historyList.getChildren().add(clearButton);

        // Set the content of the history tab to the ScrollPane
        historyTab.setContent(scrollPane);

        // Add the history tab to the TabPane
        if (!TabsAndWv.getInstance().getTabPane().getTabs().contains(historyTab)) {
            TabsAndWv.getInstance().getTabPane().getTabs().add(historyTab);
        }

        TabsAndWv.getInstance().getTabPane().getSelectionModel().select(historyTab);
    }
}
