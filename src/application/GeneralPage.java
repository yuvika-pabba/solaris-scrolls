package application;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class GeneralPage {
	
	
	public static BorderPane create_general_page(Stage primaryStage) {
		
		final int screen_width = 1080;
		final int screen_height = 820;

		// Store name
		Text store_name_1 = new Text("Solaris\n");
		store_name_1.setFill(Color.web("#FFC627"));
		store_name_1.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
		
		Text store_name_2 = new Text("Scrolls");
		store_name_2.setFill(Color.WHITE);
		store_name_2.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Arial';");
		
		TextFlow store_name = new TextFlow(store_name_1, store_name_2);
		
		// Book store logo
		Image book_store_logo = new Image("file:resources/library_logo.png");
		ImageView show_logo = new ImageView(book_store_logo);
		show_logo.setFitWidth(110);
		show_logo.setPreserveRatio(true);
		
		// Book logo for login field
		Image book_logo = new Image("file:resources/book_logo.png");
		ImageView show_book_logo = new ImageView(book_logo);
		show_book_logo.setFitWidth(150);
		show_book_logo.setPreserveRatio(true);
		
		// Background image on the right side
		Image right_background = new Image("file:resources/login_right.png");
		BackgroundImage show_right_background = new BackgroundImage(right_background, null, null, null, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true));


		// note that the border css style is just for debugging purposes
		AnchorPane left_layout = new AnchorPane();	// the logo and name goes here
		VBox mid_layout = new VBox();	// the login field layout goes here
		AnchorPane right_layout = new AnchorPane();	// nothing goes here for the login page but it should serve its purpose later
		
		// add all the elements into its respective container
		left_layout.getChildren().addAll(store_name, show_logo);
		right_layout.setBackground(new Background(show_right_background));
		
		mid_layout.setAlignment(Pos.CENTER);
		
		left_layout.prefWidthProperty().bind(Bindings.divide(primaryStage.widthProperty(), 4.5));
		mid_layout.setMaxWidth((screen_width / 3.0) + 50.0);
		right_layout.prefWidthProperty().bind(Bindings.divide(primaryStage.widthProperty(), 4.5));
		
		AnchorPane.setTopAnchor(store_name, 5.0);
		AnchorPane.setLeftAnchor(store_name, 10.0);
		AnchorPane.setBottomAnchor(show_logo, 2.5);
		AnchorPane.setLeftAnchor(show_logo, 2.5);
		
		BorderPane main_layout = new BorderPane();
		
		main_layout.setLeft(left_layout);
		main_layout.setCenter(mid_layout);
		main_layout.setRight(right_layout);
		main_layout.setStyle("-fx-background-color: #8C1D40;");
		
		return main_layout;
	}
}
