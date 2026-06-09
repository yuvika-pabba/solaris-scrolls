package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class BuyerPage {
	private final int screen_width = 1280;
	private final int screen_height = 820;
	private Stage primaryStage;
	
	public BuyerPage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void show() {
		BorderPane main_layout = new BorderPane();
		main_layout = create_buyer_page();
		
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
	
	private BorderPane create_buyer_page() {
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
		
		HBox mid_layout = new HBox(10);
		mid_layout.setAlignment(Pos.CENTER);
		main_layout.getChildren().remove(1);
		main_layout.setCenter(mid_layout);
		
		CheckBox natural_sciences = new CheckBox("Natural Sciences");
		CheckBox computer = new CheckBox("Computer");
		CheckBox math = new CheckBox("Math");
		CheckBox english = new CheckBox("English");
		CheckBox novels = new CheckBox("Novels");
		CheckBox sci_fi = new CheckBox("Sci-Fi");
		CheckBox arts = new CheckBox("Arts");
		
		CheckBox condition1 = new CheckBox("Used Like New");
		CheckBox condition2 = new CheckBox("Moderately Used");
		CheckBox condition3 = new CheckBox("Heavily Used");
		
		VBox categories_container = new VBox(10);
		categories_container.getChildren().addAll(natural_sciences, computer, math, english, novels, sci_fi, arts);
		VBox conditions_container = new VBox(10);
		conditions_container.getChildren().addAll(condition1, condition2, condition3);
		
		TitledPane categories = new TitledPane();
		TitledPane conditions = new TitledPane();
		
		categories.setText("Categories");
		categories.setContent(categories_container);
		categories.setExpanded(true);
		categories.setMaxWidth(150);
		
		conditions.setText("Conditions");
		conditions.setContent(conditions_container);
		conditions.setExpanded(true);
		conditions.setMaxWidth(150);
		
		TableView<bookItem> book_table = new TableView<>();
		TableColumn<bookItem, String> item_name_col = new TableColumn<>("Name");
		item_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
		item_name_col.setCellFactory(column -> new javafx.scene.control.TableCell<bookItem, String>(){
			private final Text text = new Text();
			
			{
				text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
				setGraphic(text);
			}
			
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(empty || item == null)
					text.setText(null);
				else
					text.setText(item);
			}
		});
		
		TableColumn<bookItem, String> condition_col = new TableColumn<>("Condition");
		condition_col.setCellValueFactory(new PropertyValueFactory<>("condition"));
		TableColumn<bookItem, Integer> in_stock_col = new TableColumn<>("In Stock");
		in_stock_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
		TableColumn<bookItem, String> price_col = new TableColumn<>("Price");
		price_col.setCellValueFactory(new PropertyValueFactory<>("PricePrint"));
		book_table.getColumns().addAll(item_name_col, condition_col, in_stock_col, price_col);
		
		ObservableList<bookItem> book_items = FXCollections.observableArrayList();
		book_table.setItems(book_items);
		
		// get the books list the first time
		getBooksList(book_items);
		
		TextField search_field = new TextField();
		search_field.setPromptText("Enter search key...");
		search_field.setMinWidth(screen_width / 5.0);
		search_field.setMaxWidth(screen_width / 3.25);
		
		Button search_button = new Button("Search");
		search_button.setOnAction(event -> {search_button_function(book_items, categories_container, conditions_container, search_field);});
		
		HBox search_container = new HBox(10);
		search_container.getChildren().addAll(search_field, search_button);
		search_container.setAlignment(Pos.CENTER);
		search_container.setMinWidth(screen_width / 5.0);
		search_container.setMaxWidth(screen_width / 3.25);
		
		TableView<cartItem> cart_list = new TableView<>();
		TableColumn<cartItem, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setCellFactory(column -> new javafx.scene.control.TableCell<cartItem, String>(){
			private final Text text = new Text();
			
			{
				text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
				setGraphic(text);
			}
			
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(empty || item == null)
					text.setText(null);
				else
					text.setText(item);
			}
		});
		TableColumn<cartItem, Integer> quantity = new TableColumn<>("Qty");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		TableColumn<cartItem, String> price = new TableColumn<>("Price");
		price.setCellValueFactory(new PropertyValueFactory<>("PricePrint"));
		cart_list.getColumns().addAll(name, quantity, price);
		
		ObservableList<cartItem> cart_items = FXCollections.observableArrayList();
		cart_list.setItems(cart_items);
		
		Label subtotal = new Label(" * Subtotal: $0.00");
		Label total = new Label(" * Total (7% tax): $0.00");
		subtotal.setStyle("-fx-font-color: black; -fx-font-weight: bold;");
		total.setStyle("-fx-font-color: black; -fx-font-weight: bold;");
		
		VBox totals_container = new VBox();
		totals_container.setAlignment(Pos.BASELINE_LEFT);
		totals_container.setStyle("-fx-background-color: White;");
		totals_container.getChildren().addAll(subtotal, total);
		totals_container.setMinHeight(50);
		
		Button add_to_cart = new Button("Add to Cart");
		add_to_cart.setOnAction(event -> {
			bookItem selectedItem = book_table.getSelectionModel().getSelectedItem();
			if(selectedItem != null) {
				// pop up a quantity window
		        Popup popup = new Popup();
		        HBox popupContent = new HBox();
		        popupContent.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black;");
		        
		        Label popupLabel = new Label("Enter Quantity:");
		        Spinner<Integer> quantity_spinner = new Spinner<Integer>();
		        quantity_spinner.setEditable(true);
		        quantity_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selectedItem.getStock(), 1));
		        Button cancel = new Button("Cancel");
		        Button submit = new Button("Submit");
		        
		        VBox container1 = new VBox();
		        container1.setAlignment(Pos.CENTER);
		        container1.getChildren().addAll(popupLabel, quantity_spinner);
		        
		        HBox container2 = new HBox(10);
		        container2.setAlignment(Pos.CENTER);
		        container2.getChildren().addAll(cancel, submit);
		        
		        VBox container3 = new VBox(10);
		        container3.setAlignment(Pos.CENTER);
		        container3.getChildren().addAll(container1, container2);
		        
		        cancel.setOnAction(e1 -> {popup.hide();});
		        
		        submit.setOnAction(e2 -> {
		        	// add to cart
					cart_list.getItems().add(new cartItem(selectedItem.getName(), quantity_spinner.getValue(), selectedItem.getPrice()));
					popup.hide();
					updatePriceTotal(cart_list, subtotal, total);
		        });
		        
		        popupContent.getChildren().addAll(container3);
		        popup.getContent().add(popupContent);
		        
		        // Set button action to show the popup
				if (!popup.isShowing())
					popup.show(primaryStage);
				else 
					popup.hide();
			}
		});
		
		add_to_cart.setMinWidth(200);
		HBox add_to_cart_container = new HBox();
		add_to_cart_container.getChildren().addAll(add_to_cart);
		add_to_cart_container.setAlignment(Pos.BASELINE_LEFT);
		
		Button clear_cart = new Button("Clear Cart");
		clear_cart.setOnAction(event -> {
			cart_items.clear();
			subtotal.setText(" * Subtotal: $0.00");
			total.setText(" * Total (7% tax): $0.00");
		});
		Button checkout = new Button("Checkout");
		checkout.setOnAction(event -> {
			check_out_button(cart_list);
			clear_cart.fire();
			book_items.clear();
			getBooksList(book_items);
		});
		clear_cart.setMinWidth(95);
		checkout.setMinWidth(95);
		
		HBox cart_buttons_container = new HBox(10);
		cart_buttons_container.getChildren().addAll(clear_cart, checkout);
		cart_buttons_container.setAlignment(Pos.CENTER);
		cart_buttons_container.setPadding(new Insets(10, 0, 0, 0));
		
		VBox left = new VBox(10);
		VBox mid = new VBox(10);
		VBox right = new VBox();
		left.setAlignment(Pos.CENTER);
		mid.setAlignment(Pos.CENTER);
		right.setAlignment(Pos.CENTER);
		right.getChildren().addAll();
		
		right.getChildren().addAll(cart_list, totals_container, cart_buttons_container);
		
		left.getChildren().addAll(categories, conditions);
		mid.getChildren().addAll(search_container, book_table, add_to_cart_container);
		
		
		left.setMinWidth(screen_width / 6.0 - 50);
		mid.setMinWidth(screen_width / 5.0);
		mid.setMaxWidth(screen_width / 3.25);
		right.setMinWidth(screen_width / 6.0);
		right.setMaxHeight(300);
		
		mid_layout.getChildren().addAll(left, mid, right);
		
		return main_layout;
	}
	
	private void updatePriceTotal(TableView<cartItem> cart_list, Label subtotal, Label total) {
		double subtotal_price = 0.00;
		double total_price = 0.00;
		for(cartItem item : cart_list.getItems())
			subtotal_price += item.getSubTotal();
		
		total_price = subtotal_price * 1.07;
		String subtotal_text = String.format(" * Subtotal: $%.2f", subtotal_price);
		String total_text = String.format(" * Total (7%% tax): $%.2f", total_price);
		subtotal.setText(subtotal_text);
		total.setText(total_text);
	}
	
	private void check_out_button(TableView<cartItem> cart_list) {
		if (cart_list.getItems().isEmpty())
			return;
		for(cartItem item : cart_list.getItems()) {
			int quantity = item.getQuantity();
			String name = item.getName();
			
			try{
				List<String> lines = Files.readAllLines(Paths.get("book.txt"));
				for(int i = 0; i < lines.size(); i++) {
					if(lines.get(i).contains(name)) {
						String[] line_items = lines.get(i).split(",\\s*");
						line_items[7] = Integer.toString(Integer.parseInt(line_items[7]) - quantity);
						String line_final = "";
						for(String item_1 : line_items)
							line_final += item_1 + ",";
						
						lines.set(i, line_final.substring(0, line_final.length() - 1));
					}
				}
				
				Files.write(Paths.get("book.txt"), lines);
			}catch(IOException e){
				System.out.println("ERROR: " + e.getMessage());
			}
			
		}
		
		Alert error = new Alert(AlertType.INFORMATION);
		error.setTitle("Success");
		error.setHeaderText("Success!");
		error.showAndWait();
	}
	
	private void search_button_function(ObservableList<bookItem> book_items, VBox categories, VBox container, TextField search) {
		getBooksList(book_items, categories, container, search);
	}
	
	private void logout_button() {
		LaunchApplication application = new LaunchApplication(primaryStage);
		application.show();
	}
	
	private void getBooksList(ObservableList<bookItem> book_items) {
		String books_list_path = "book.txt";
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(books_list_path))){
			String line;
			while((line = reader.readLine()) != null) {
				//title, author, publishedDate, category, condition, listingPrice
				String[] words = line.split(",\\s*");
				String book_name = words[0];
				String author = words[1];
				String published_date = words[2];
				String book_categories = words[3];
				String condition = words[4];
				double price = Double.parseDouble(words[6]);
				int in_stock = Integer.parseInt(words[7]);
				
				bookItem temp_book = new bookItem(book_name, condition, in_stock, price);
				book_items.add(temp_book);
			}
		}catch(IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void getBooksList(ObservableList<bookItem> book_items, VBox categories, VBox conditions, TextField search) {
		String search_query = search.getText();
		String[] categories_list = new String[7];
		String[] conditions_list = new String[3];
		String books_list_path = "book.txt";
		
		if(search_query.isBlank()) {
			for(int i = 0; i < 7; i++) {
				if(((CheckBox)categories.getChildren().get(i)).isSelected())
					categories_list[i] = ((CheckBox)categories.getChildren().get(i)).getText();
			}

			for(int i = 0; i < 3; i++) {
				if(((CheckBox)conditions.getChildren().get(i)).isSelected())
					conditions_list[i] = ((CheckBox)conditions.getChildren().get(i)).getText();
			}
		}
		
		//clears the books_list
		book_items.clear();
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(books_list_path))){
			String line;
			while((line = reader.readLine()) != null) {
				//title, author, publishedDate, category, condition, listingPrice
				String[] words = line.split(",\\s*");
				String book_name = words[0];
				String author = words[1];
				String published_date = words[2];
				String book_categories = words[3];
				String condition = words[4];
				double price = Double.parseDouble(words[6]);
				int in_stock = Integer.parseInt(words[7]);

				if(!book_name.toLowerCase().contains(search_query.toLowerCase()) && !search_query.isBlank()) {
					continue;
				}else if(search_query.isBlank()) {
					boolean found_condition = conditions_list[0] == null && conditions_list[1] == null && conditions_list[2] == null ? true : false;
					for (String item : conditions_list) {
						if(item != null && condition.contains(item)) {
							found_condition = true;
							break;
						}
					}

					boolean found_category = categories_list[0] == null && categories_list[1] == null && categories_list[2] == null && categories_list[3] == null && categories_list[4] == null && categories_list[5] == null && categories_list[6] == null ? true : false;
					for (String item : categories_list) {
						if(item != null && book_categories.contains(item)) {
							found_category = true;
							break;
						}
					}

					if(!found_condition && !found_category) continue;
					if(found_condition && !found_category) continue;
					if(!found_condition && found_category) continue;
				}
				bookItem temp_book = new bookItem(book_name, condition, in_stock, price);
				book_items.add(temp_book);
			}
		}catch(IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public static class bookItem{
		private String name;
		private String condition;
		private int stock;
		private double price;
		
		public bookItem(String name, String condition, int stock, double price) {
			this.name = name;
			this.condition = condition;
			this.stock = stock;
			this.price = price;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getCondition() {
			return this.condition;
		}
		
		public int getStock() {
			return this.stock;
		}
		
		public double getPrice() {
			return this.price;
		}
		
		public String getPricePrint() {
			return String.format("$%.2f", this.price);
		}
	}
	
	public static class cartItem{
		private String name;
		private int quantity;
		private double price;
		
		public cartItem(String name, int quantity, double price) {
			this.name = name;
			this.quantity = quantity;
			this.price = price;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getQuantity() {
			return this.quantity;
		}
		
		public String getPricePrint() {
			return String.format("$%.2f", this.price);
		}
		
		public double getPrice() {
			return this.price;
		}
		
		public double getSubTotal() {
			return (double)this.quantity * this.price;
		}
	}
}
