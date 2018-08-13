package ru.ezhov.remote.hints;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.ezhov.arm.plugin.gui.panel.PanelPlugin;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CommonKnowledgesPanel extends BorderPane implements PanelPlugin {
	private static final Logger LOG = Logger.getLogger(CommonKnowledgesPanel.class.getName());
	private static CommonKnowledgesPanel commonKnowledgesPanel;
	private BorderPane paneDetail;
	private InfoPane infoPane = new InfoPane("Получение знаний");
	private BorderPane borderPane = new BorderPane();

	public CommonKnowledgesPanel() {
		BorderPane borderPaneSearch = new BorderPane();
		CheckBox checkbox = new CheckBox("Содержит все слова");
		javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
		textField.setPromptText("Введите слово для поиска");

		borderPaneSearch.setCenter(textField);
		checkbox.setPadding(new Insets(5));
		borderPaneSearch.setRight(checkbox);


		ListView<Knowledge> listView = new ListView<>();
		Label placeHolder = new Label("Подсказок нет");
		listView.setPlaceholder(placeHolder);
		BorderPane paneData = new BorderPane();
		paneData.setCenter(listView);
		paneData.setTop(borderPaneSearch);
		SplitPane splitPane = new SplitPane(paneData);
		Task<ObservableList<Knowledge>> observableListTask = new Task<ObservableList<Knowledge>>() {
			@Override
			protected ObservableList<Knowledge> call() throws Exception {
				try {
					ObservableList<Knowledge> seasonList = getList();
					FilteredList<Knowledge> filteredList = new FilteredList<>(seasonList, p -> true);
					Platform.runLater(() -> {
						listView.setItems(filteredList);
						SearchChangeListener searchChangeListener =
							new SearchChangeListener(filteredList, textField, checkbox);
						textField
							.textProperty()
							.addListener(searchChangeListener);
						checkbox.setOnAction(searchChangeListener);
						borderPane.setCenter(splitPane);
					});
					return seasonList;
				} catch (Exception e) {
					e.printStackTrace();
					Platform.runLater(() -> {
						borderPane.setCenter(new InfoPane("Не удалось загрузить подсказки"));
					});
					return FXCollections.emptyObservableList();
				}
			}
		};
		new Thread(observableListTask).start();

		listView.setCellFactory(
			new Callback<ListView<Knowledge>, ListCell<Knowledge>>() {
				@Override
				public ListCell<Knowledge> call(ListView<Knowledge> listView) {
					return new ListCell<Knowledge>() {
						@Override
						public void updateItem(Knowledge item, boolean empty) {
							// Must call super
							super.updateItem(item, empty);
							int index = this.getIndex();
							String name = null;
							// Format name
							if (item == null || empty) {
								// No action to perform
							} else {
								name = (index + 1) + ". " + item.getName();
							}
							this.setText(name);
							setGraphic(null);
						}
					};
				}
			});


		listView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Knowledge>) c -> {
			Knowledge val = listView.getSelectionModel().getSelectedItem();
			if (val != null) {
				if (paneDetail == null) {
					paneDetail = new BorderPane();
					splitPane.getItems().add(paneDetail);
				}
				paneDetail.setCenter(new DetailPanel(val));
			}
		});

		borderPane.setCenter(infoPane);
		setCenter(borderPane);
	}

	@Override
	public Pane getPane() {
		return new CommonKnowledgesPanel();
	}

	private ObservableList<Knowledge> getList() throws Exception {
		KnowledgeDao knowledgeDao = new KnowledgeDao();
		DataInfo dataInfo = knowledgeDao.getDataInfo();
		return FXCollections.observableArrayList(dataInfo.getKnowledges());
	}

}

class DetailPanel extends BorderPane {
	private Knowledge knowledge;
	private Label labelName;
	private Label labelNameText;

	private Label labelDescription;
	private Label labelDescriptionText;

	private Hyperlink labelUrlText;

	private Hyperlink labelRawUrlText;

	private TextArea textAreaText;


	public DetailPanel(Knowledge knowledge) {
		this.knowledge = knowledge;
		labelName = new Label("Название: ");
		labelName.setPadding(new Insets(4));
		labelNameText = new Label(this.knowledge.getName());
		labelNameText.setPadding(new Insets(4));
		labelName.setLabelFor(labelNameText);
		labelDescription = new Label("Описание: ");
		labelDescription.setPadding(new Insets(4));
		labelDescriptionText = new Label(this.knowledge.getDescription());
		labelDescriptionText.setPadding(new Insets(4));
		labelDescription.setLabelFor(labelDescriptionText);
		labelUrlText = new Hyperlink("Редактировать в браузере");
		labelUrlText.setOnAction(e -> {
			try {
				Desktop.getDesktop().browse(new URL(this.knowledge.getUrl()).toURI());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		labelRawUrlText = new Hyperlink("Посмотреть в браузере сырые данные");
		labelRawUrlText.setOnAction(e -> {
			try {
				Desktop.getDesktop().browse(new URL(this.knowledge.getRawUrl()).toURI());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		textAreaText = new TextArea();

		InfoPane infoPane = new InfoPane("Загрузка текста");

		setTop(new VBox(
			new HBox(labelName, labelNameText),
			new HBox(labelDescription, labelDescriptionText),
			new HBox(labelUrlText),
			new HBox(labelRawUrlText)
		));
		setCenter(infoPane);

		Task<String> task = new Task<String>() {
			@Override
			protected String call() throws Exception {
				String text;
				try {
					text = new KnowledgeDao().getRawData(knowledge);
				} catch (Exception e) {
					e.printStackTrace();
					text = e.getMessage();
				}
				String finalText = text;
				Platform.runLater(() -> {
					textAreaText.setText(finalText);
					DetailPanel.this.setCenter(textAreaText);
				});
				return text;
			}
		};
		new Thread(task).start();

	}
}

class InfoPane extends VBox {
	private ProgressIndicator indeterminateInd = new ProgressIndicator();
	private Label label = new Label();
	private String text;

	public InfoPane(String text) {
		this.text = text;
		label.setText(text);
		indeterminateInd.setMaxWidth(50);
		indeterminateInd.setMaxHeight(50);
		setAlignment(Pos.CENTER);
		getChildren().addAll(indeterminateInd, label);
	}
}


class SearchChangeListener implements ChangeListener<String>, EventHandler {
	private javafx.scene.control.TextField textField;
	private CheckBox checkBox;
	private FilteredList<Knowledge> filteredList;

	public SearchChangeListener(FilteredList<Knowledge> filteredList, javafx.scene.control.TextField textField, CheckBox checkBox) {
		this.filteredList = filteredList;
		this.textField = textField;
		this.checkBox = checkBox;
	}

	@Override
	public void changed(ObservableValue observable, String oldValue, String newValue) {
		execute();
	}

	@Override
	public void handle(javafx.event.Event event) {
		execute();
	}

	private void execute() {
		String filter = textField.getText();
		if (filter == null || filter.length() == 0) {
			filteredList.setPredicate(k -> true);
		} else {
			filteredList.setPredicate(k -> {
				String[] words = filter.split(" ");
				if (checkBox.isSelected()) {
					java.util.List<Boolean> booleans = new ArrayList<>();
					for (String word : words) {
						if (k.getName().contains(word)) {
							booleans.add(true);
						}
					}
					return booleans.size() == words.length;
				} else {
					for (String word : words) {
						if (k.getName().contains(word)) {
							return true;
						}
					}
					return false;
				}


			});
		}
	}
}
