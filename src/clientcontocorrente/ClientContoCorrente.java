/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientcontocorrente;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author fedes
 */
public class ClientContoCorrente extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            Socket server = new Socket("localhost", 5000);
            
            Label email = new Label("Email: ");
            TextField emailField = new TextField();
            emailField.setMaxWidth(200);
            Label password = new Label("Password: ");
            TextField pswField = new TextField();
            pswField.setMaxWidth(200);
            Label error = new Label();
            error.setTextFill(Color.RED);
            Button loginButton = new Button("Login");
            
            VBox loginElements = new VBox(email, emailField, password, pswField, loginButton, error);
            loginElements.setAlignment(Pos.CENTER);

            Scene loginPage = new Scene(loginElements, 300, 300);
            
            Label balance = new Label();
            balance.setAlignment(Pos.TOP_CENTER);
            balance.setFont(Font.font ("Verdana", 16));
            TextField amount = new TextField();
            amount.setMaxWidth(200);
            Button depot = new Button("Deposita");
            Button take = new Button("Preleva");
            HBox buttons = new HBox(depot, take);
            buttons.setAlignment(Pos.CENTER);
            Label status = new Label();
            status.setTextFill(Color.RED);
            
            VBox mainPageElements = new VBox(balance, amount, buttons, status);
            mainPageElements.setAlignment(Pos.CENTER);
            
            Scene mainPage = new Scene (mainPageElements, 300, 300);
            
            LoginEventHandler leh = new LoginEventHandler(error, primaryStage, server, emailField, pswField, mainPage, balance);
            MainEventHandler meh = new MainEventHandler(server, balance, amount, status);
            
            loginButton.setOnAction(leh);
            depot.setOnAction(meh);
            take.setOnAction(meh);

            primaryStage.setTitle("Login");
            primaryStage.setScene(loginPage);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(ClientContoCorrente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
