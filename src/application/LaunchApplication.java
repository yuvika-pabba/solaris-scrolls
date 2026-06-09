package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class LaunchApplication {
	private final byte[] salt = {-24, 12, 45, -78, 89, -10, 34, -67, 22, 91, -104, 3, 45, 72, 56, 99};
	private String users_credentials = "users_credentials.txt";
	private final int screen_width = 1080;
	private final int screen_height = 820;
	private Stage primaryStage;
	
	public LaunchApplication(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void show() {
		primaryStage.setScene(new Scene(create_login_page(), screen_width, screen_height));
		primaryStage.show();
	}
	
	private BorderPane create_login_page() {
		BorderPane main_layout = new BorderPane();
		main_layout = GeneralPage.create_general_page(primaryStage);
		
		// Book logo for login field
		Image book_logo = new Image("file:resources/book_logo.png");
		ImageView show_book_logo = new ImageView(book_logo);
		show_book_logo.setFitWidth(200);
		show_book_logo.setPreserveRatio(true);
		
		//input fields for username and password
		TextField username_field = new TextField();
		PasswordField password_field = new PasswordField();
		username_field.setPromptText("Username");
		password_field.setPromptText("Password");
		
		//label to display the login result
		Label result_label = new Label();
		result_label.setText("Minimum of 8 characters");
		result_label.setStyle("-fx-text-fill: yellow;-fx-effect: dropshadow(gaussian, black, 1, 1, 0, 0);");
		
		// login button
		Button login_button = new Button("Login");
		login_button.setMinWidth(screen_width / 3.0 / 2.0 - 10);
		login_button.setOnAction(event -> login_button(username_field, password_field, result_label));
		
		// signup button
		Button sign_up_button = new Button("Sign Up");
		sign_up_button.setMinWidth(screen_width / 3.0 / 2.0 - 10);
		sign_up_button.setOnAction(event -> sign_up_button());
		
		VBox login_field_layout = new VBox(10);		// to store the fields, buttons, and labels. For alignment purposes
		
		HBox login_field_buttons = new HBox(15);
		login_field_buttons.getChildren().addAll(login_button, sign_up_button);
		login_field_buttons.setAlignment(Pos.CENTER);
		
		// the result label container so it's properly aligned
		// the fields and buttons are centered
		// while this label is left aligned
		HBox result_label_box = new HBox();
		result_label_box.getChildren().addAll(result_label);
		result_label_box.setAlignment(Pos.BASELINE_LEFT);
		
		// add all the elements into its respective container
		VBox mid_layout = new VBox();
		mid_layout = (VBox)main_layout.getChildren().get(1);
		
		login_field_layout.getChildren().addAll(show_book_logo, username_field, password_field, result_label_box, login_field_buttons);
		login_field_layout.setAlignment(Pos.CENTER);
		mid_layout.getChildren().addAll(login_field_layout);
		
		return main_layout;
	}
	
	private void login_button(TextField username_field, PasswordField password_field, Label result_label) {
		String entered_username = username_field.getText();
		String entered_password = password_field.getText();
		String result_display = "";
		
		if (!check_username(entered_username)) 
			result_display += "Username must be at least 5 characters\n";
		
		if (!check_password(entered_password))
			result_display += "Password must be at least 8 characters";
		else if (validate_credentials(entered_username, entered_password))
			result_display += "Login successful!";
		else
			result_display += "Login failed. Please check your credentials.";
		
		if(validate_credentials(entered_username, entered_password)) {
			String role = get_user_role(entered_username);
			if(role.equals("buyer")) {
				BuyerPage buyer_page = new BuyerPage(primaryStage);
				buyer_page.show();
			}else if(role.equals("seller")) {
				SellerPage seller_page = new SellerPage(primaryStage);
				seller_page.show();
			}else if(role.equals("admin")) {
				
			}else {
				result_display = "Login failed. Please check your credentials";
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("ERROR");
				error.setHeaderText("Invalid Credentials");
				error.showAndWait();
			}
		}
		
		result_label.setText(result_display);
	}
	
	// check if the username and password exist in the database
	private boolean validate_credentials(String username, String password) {		
		return read_database(username, password);
	}
	
	private String get_user_role(String username) {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(users_credentials))){
			String line;
			while((line = reader.readLine()) != null) {
				if(line.contains(username)) {
					String[] item = line.split("\\s+");
					return item[item.length - 1];
				}
			}
		}catch(IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return "";
	}
	
	private byte[] bytes_concatenate(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
	}
	
	// collects email, phone number, username, and password
	private void sign_up_button() {
        primaryStage.setScene(new Scene(create_sign_up_page(), screen_width, screen_height));
        primaryStage.show();
	}
	
	private BorderPane create_sign_up_page () {
		BorderPane main_layout = create_login_page();
		main_layout = GeneralPage.create_general_page(primaryStage);
		VBox mid_layout = (VBox)main_layout.getChildren().get(1);
		
		// Book logo for login field
		Image book_logo = new Image("file:resources/book_logo.png");
		ImageView show_book_logo = new ImageView(book_logo);
		show_book_logo.setFitWidth(200);
		show_book_logo.setPreserveRatio(true);
		
		//label to display the login result
		Label result_label = new Label();
		result_label.setText("Minimum of 8 characters");
		result_label.setStyle("-fx-text-fill: yellow;-fx-effect: dropshadow(gaussian, black, 1, 1, 0, 0);");
		
		// create text fields for email, phone number, username, and password
		TextField email = new TextField();
		TextField phone_number = new TextField();
		TextField username = new TextField();
		PasswordField password = new PasswordField();
		username.setPromptText("Username");
		password.setPromptText("Password");
		email.setPromptText("Email");
		phone_number.setPromptText("Phone Number");
		phone_number.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*") || newValue.length() > 10) {
				phone_number.setText(oldValue);
			}
		});
		
		Button submit_button = new Button("Submit");
		submit_button.setMinWidth(screen_width / 3.0 / 2.0 - 10);
		submit_button.setOnAction(event -> {
			String entered_email = email.getText();
			String entered_phone_number = phone_number.getText();
			String entered_username = username.getText();
			String entered_password = password.getText();
			boolean valid_info = true;
			
			String warning_message = "";
			
			if(entered_email.isBlank()) {
				warning_message += "Enter an email address\n";
				valid_info = false;
			}
			
			if(entered_phone_number.isBlank()) {
				warning_message += "Enter a phone number\n";
				valid_info = false;
			}
			
			if(!check_username(entered_username)) {
				warning_message += "Username must be at least 5 characters\n";
				valid_info = false;
			}
			
			if(!check_password(entered_password)) {
				warning_message += "Password must be at least 8 characters\n";
				valid_info = false;
			}
			
			if(check_user_exist(entered_username)) {
				warning_message += "Username taken\n";
				valid_info = false;
			}
			
			if(!valid_info) {
				result_label.setText(warning_message);
			}else {
				write_database(entered_username, entered_password, entered_email, entered_phone_number);
				show();
			}
		});
		
		Button back_button = new Button("Back to Login");
		back_button.setMinWidth(screen_width / 3.0 / 2.0 - 10);
		back_button.setOnAction(event -> show());
		
		VBox mid_field = new VBox(10);
		
		// the result label container so it's properly aligned
		// the fields and buttons are centered
		// while this label is left aligned
		HBox warning_container = new HBox();
		warning_container.getChildren().addAll(result_label);
		warning_container.setAlignment(Pos.BASELINE_LEFT);
		
		HBox sign_up_container = new HBox(15);
		sign_up_container.getChildren().addAll(back_button, submit_button);
		sign_up_container.setAlignment(Pos.CENTER);
		
		mid_field.getChildren().addAll(show_book_logo, email, phone_number, username, password, warning_container, sign_up_container);
		mid_field.setAlignment(Pos.CENTER);
		
		mid_layout.getChildren().addAll(mid_field);
		
		return main_layout;
	}
	
	private boolean write_database(String username, String password, String email, String phone_number) {
		String hashed_credentials = hash_credentials(username, password);
		String to_write = username + " " + hashed_credentials + " " + email + " " + phone_number + " " + "buyer" + "\n";
		
		try {
			Files.write(Paths.get(users_credentials), to_write.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private boolean read_database(String username, String password) {
		String hashed_credentials = hash_credentials(username, password);
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(users_credentials))){
			String line;
			while((line = reader.readLine()) != null) {
				if(line.contains(hashed_credentials)) {
					return true;
				}
			}
		}catch(IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return false;
	}
	
	private String hash_credentials(String username, String password) {
		byte[] user_credentials_bytes = bytes_concatenate(username.getBytes(), password.getBytes());
		byte[] user_credentials_salted = bytes_concatenate(salt, user_credentials_bytes);
		String hash = "";
		
		try {
            // create an instance of sha256 object
            MessageDigest sha256_object = MessageDigest.getInstance("SHA-256");

            // update the object with the data
            sha256_object.update(user_credentials_salted);

            // get the hash
            byte[] hashed_user_credentials = sha256_object.digest();

            // Convert to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed_user_credentials) {
                sb.append(String.format("%02x", b));
            }
            
            hash = sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException: " + e.getMessage());
        }
		
		return hash;
	}
	
	// check if the entered username meets the requirements
	private boolean check_username(String username) {
		return (username.length() >= 5);
	}
	
	// check if the entered password meets the requirements
	private boolean check_password(String password) {
		return (password.length() >= 8);
	}
	
	private boolean check_user_exist(String username) {
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(users_credentials))){
			String line;
			while((line = reader.readLine()) != null) {
				if(line.split("\\s+")[0].equals(username)) {
					return true;
				}
			}
		}catch(IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return false;
	}
	
}
