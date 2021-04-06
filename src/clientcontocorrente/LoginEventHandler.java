/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientcontocorrente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author fedes
 */
public class LoginEventHandler implements EventHandler<ActionEvent>{
    private Label errorLabel, balance;
    private Stage primaryStage;
    private Socket server;
    private TextField emailField, pswField;
    private PrintWriter pw;
    private BufferedReader br;
    private Scene mainPage;
    
    public LoginEventHandler(Label errorLabel, Stage primaryStage, Socket server, TextField emailField, TextField pswField, Scene mainPage, Label balance) {
        try {
            this.errorLabel = errorLabel;
            this.primaryStage = primaryStage;
            this.server = server;
            this.emailField = emailField;
            this.pswField = pswField;
            this.pw = new PrintWriter(server.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(server.getInputStream()));
            this.mainPage = mainPage;
            this.balance = balance;
        } catch (IOException ex) {
            Logger.getLogger(LoginEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            if(emailField.getText().equals("") || pswField.getText().equals("")){
                errorLabel.setText("Parametri mancanti!!!");
                return;
            }
            JSONObject output = new JSONObject();
            output.put("event", "login").put("email", emailField.getText()).put("password", pswField.getText());
            pw.println(output.toString());
            
            JSONObject response = new JSONObject(br.readLine());
            if(response.getString("response").equals("go")){ 
                output.clear();
                output.put("event", "getSaldo");
                pw.println(output.toString());
                response = new JSONObject(br.readLine());
                this.balance.setText(response.getInt("balance")+"");
                primaryStage.setScene(mainPage);
                primaryStage.setTitle("Conto Corrente");
                primaryStage.show();
            }else{
                errorLabel.setText("email o password errati!!!");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(LoginEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    
    
}
