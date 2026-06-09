package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.layout.BorderPane;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class SellerPage{
    private static final String BOOKS_FILE = "book.txt";
    private final int screen_width = 1200;
    private final int screen_height = 820;
    private Stage primaryStage;
    
    public SellerPage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
    
    public void show() {
		BorderPane main_layout = new BorderPane();
		main_layout = create_seller_page();
		
		// set the title and favicon of the window
		Image book_store_logo = new Image("file:resources/library_logo.png");
		primaryStage.getIcons().add(book_store_logo);
		primaryStage.setTitle("SolarisScrolls - ASU Bookstore");
		
		// Create the scene and set it in the stage
		Scene scene = new Scene(main_layout, screen_width, screen_height);
		primaryStage.setScene(scene);

		// Show the window
		primaryStage.show();
	}

    private BorderPane create_seller_page() {
    	BorderPane main_layout = new BorderPane();
		main_layout = GeneralPage.create_general_page(primaryStage);
		
		AnchorPane right_layout = new AnchorPane();
		right_layout = (AnchorPane) main_layout.getChildren().get(2);
		right_layout.setBackground(null);
		right_layout.setMaxWidth(screen_width / 15.0);
		
		AnchorPane left_layout = new AnchorPane();
		left_layout = (AnchorPane) main_layout.getChildren().get(0);
		left_layout.setMaxWidth(screen_height / 8.0);
		
		Image user_profile = new Image("file:resources/user_profile_cropped.png");
		ImageView user_profile_show = new ImageView(user_profile);
		user_profile_show.setFitWidth(50);
		user_profile_show.setPreserveRatio(true);
		
		Image book_store_logo = new Image("file:resources/library_logo.png");
		primaryStage.getIcons().add(book_store_logo);
		
		Button logout_buttn = new Button("Logout");
		logout_buttn.setOnAction(event -> {logout_button();});
		logout_buttn.setMinWidth(65);
		logout_buttn.setStyle("-fx-font-weight: Bold; -fx-font-size: 13px;");
		
		VBox profile_container = new VBox();
		profile_container.getChildren().addAll(user_profile_show, logout_buttn);
		profile_container.setAlignment(Pos.CENTER);
		
		right_layout.getChildren().addAll(profile_container);
		
		AnchorPane.setTopAnchor(profile_container, 10.0);
		AnchorPane.setRightAnchor(profile_container, 20.0);

        VBox form = new VBox();
        form.setPadding(new Insets(20));
        form.setSpacing(10);
        form.setStyle("-fx-background-color: #8C1D40;");

        TextField titleField = new TextField();
        titleField.setPromptText("Title of the Book");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        HBox dateAndPriceBox = new HBox(10);
        TextField yearField = new TextField();
        yearField.setPromptText("Year Published");

        TextField originalPriceField = new TextField();
        originalPriceField.setPromptText("Original Price of the Book ($)");
        originalPriceField.setPrefWidth(240);

        originalPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d{0,2}")) {
                originalPriceField.setText(oldValue);
            }
        });

        dateAndPriceBox.getChildren().addAll(yearField, originalPriceField);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Natural Sciences", "Computer", "Math", "English Language", "Novels", "Sci-Fi", "Arts");
        categoryComboBox.setPromptText("Category");
        categoryComboBox.setPrefWidth(400);

        ComboBox<String> conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll("Used Like New (90% of Original Price)", "Moderately Used (75% of Original Price)", "Heavily Used (50% of Original Price)");
        conditionComboBox.setPromptText("Condition");
        conditionComboBox.setPrefWidth(400);

        form.getChildren().addAll(titleField, authorField, dateAndPriceBox, categoryComboBox, conditionComboBox);

        VBox calculationAndButtons = new VBox(20);
        calculationAndButtons.setPadding(new Insets(20));
        calculationAndButtons.setAlignment(Pos.TOP_LEFT);

        VBox priceBox = new VBox(10);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        Label newPriceLabel = new Label("New Listing Price:");
        newPriceLabel.setStyle("-fx-font-size: 16px;");
        Label calculatedPrice = new Label("$____");
        calculatedPrice.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        priceBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: white; -fx-padding: 10px;");
        priceBox.getChildren().addAll(newPriceLabel, calculatedPrice);

        CheckBox confirmCheckbox = new CheckBox("I am sure I want to list this book.");
        confirmCheckbox.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        Button listButton = new Button("Confirm Listing");
        listButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        listButton.setPrefWidth(150);
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #FF4C4C; -fx-text-fill: white; -fx-font-size: 14px;");
        cancelButton.setPrefWidth(100);
        buttons.getChildren().addAll(listButton, cancelButton);

        calculationAndButtons.getChildren().addAll(priceBox, confirmCheckbox, buttons);

        HBox sellerFullForm = new HBox(1);
        sellerFullForm.setPadding(new Insets(1));
        sellerFullForm.setAlignment(Pos.CENTER);
        sellerFullForm.getChildren().addAll(form, calculationAndButtons);

        VBox centeredMain = new VBox();
        centeredMain.setAlignment(Pos.CENTER);
        centeredMain.getChildren().add(sellerFullForm);
        main_layout.setCenter(centeredMain);

        conditionComboBox.setOnAction(event -> {
            String condition = conditionComboBox.getValue();
            try {
                double originalPrice = Double.parseDouble(originalPriceField.getText());
                double listingPrice = switch (condition) {
                    case "Used Like New (90% of Original Price)" -> originalPrice * 0.9;
                    case "Moderately Used (75% of Original Price)" -> originalPrice * 0.75;
                    case "Heavily Used (50% of Original Price)" -> originalPrice * 0.5;
                    default -> 0;
                };
                calculatedPrice.setText(String.format("$%.2f", listingPrice));
            } catch (NumberFormatException e) {
                calculatedPrice.setText("$____");
            }
        });

        listButton.setOnAction(e -> {
        	if (!confirmCheckbox.isSelected()) {
                showAlert("Confirmation Required", "Check the check box if you are sure you want to list your book.");
                return;
            }
        	try {
                double listingPrice = Double.parseDouble(calculatedPrice.getText().replace("$", "").trim()); 
                listBook(titleField.getText(), authorField.getText(), yearField.getText(), categoryComboBox.getValue(), conditionComboBox.getValue(), originalPriceField.getText(), listingPrice);
        	} catch(NumberFormatException e2) {
            	showAlert("Missing Information", "Please fill in all required fields.");
            	return;
            }
        	});

        cancelButton.setOnAction(event -> {
            titleField.clear();
            authorField.clear();
            yearField.clear();
            originalPriceField.clear();
            categoryComboBox.getSelectionModel().clearSelection();
            conditionComboBox.getSelectionModel().clearSelection();
            calculatedPrice.setText("$____");
            confirmCheckbox.setSelected(false);
        });
        
        return main_layout;
    }

    private void listBook(String title, String author, String publishedDate, String category,
            String condition, String priceField, double listingPrice) {
        
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE, true))) {
            if (title.isEmpty() || author.isEmpty() || category == null || condition == null || priceField.isEmpty()) {
            	showAlert("Missing Information", "Please fill in all required fields.");
            	return;
            }
            String condition_mod = condition.substring(0, condition.indexOf("(") - 1);
            
            boolean book_exist = false;
            List<String> lines = Files.readAllLines(Paths.get("book.txt"));
			for(int i = 0; i < lines.size(); i++) {
				if(lines.get(i).contains(title)) {
					book_exist = true;
					String[] line_items = lines.get(i).split(",\\s*");
					line_items[7] = Integer.toString(Integer.parseInt(line_items[7]) + 1);
					String line_final = "";
					for(String item_1 : line_items)
						line_final += item_1 + ",";
					
					lines.set(i, line_final.substring(0, line_final.length() - 1));
				}
			}
			
			if(book_exist)
				Files.write(Paths.get("book.txt"), lines);
			else {
            writer.write(String.format("%s,%s,%s,%s,%s,%.2f,%s,%s\n",
                title, author, publishedDate, category, condition_mod, Double.parseDouble(priceField), listingPrice, 1));
            	showAlert("Success", "Your book has been listed successfully!");
			}
        } catch (IOException | NumberFormatException e) {
            showAlert("Error", "Failed to list the book: " + e.getMessage());
        }
		
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void logout_button() {
	LaunchApplication application = new LaunchApplication(primaryStage);
        application.show(); 
    }
}
