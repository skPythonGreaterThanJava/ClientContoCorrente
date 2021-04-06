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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

/**
 *
 * @author fedes
 */
public class MainEventHandler implements EventHandler<ActionEvent>{
    private Socket server;
    private Label balance, status;
    private TextField amount;
    private PrintWriter pw;
    private BufferedReader br;

    public MainEventHandler(Socket server, Label balance, TextField amount, Label status) {
        try {
            this.server = server;
            this.balance = balance;
            this.status = status;
            this.amount = amount;
            pw = new PrintWriter(server.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(MainEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            Button source = (Button) event.getSource();
            JSONObject request = new JSONObject();
            JSONObject response;
            if(source.getText().equals("Deposita")){ 
                request.put("event", "depot").put("amount", Integer.parseInt(amount.getText()));
                pw.println(request.toString());
                response = new JSONObject(br.readLine());
                request.clear();
                request.put("event", "getSaldo");
                pw.println(request.toString());
                response = new JSONObject(br.readLine());
                this.balance.setText(response.getInt("balance")+"");
                status.setText("Operazione eseguita con successo!!");
            } else {
                request.put("event", "take").put("amount", Integer.parseInt(amount.getText()));
                pw.println(request.toString());
                response = new JSONObject(br.readLine());
                if (response.getString("response").equals("nogo")){
                    status.setText("Impossibile eseguire!!!");
                }else {
                    status.setText("Operazione eseguita con successo!!");
                    request.clear();
                    request.put("event", "getSaldo");
                    pw.println(request.toString());
                    response = new JSONObject(br.readLine());
                    this.balance.setText(response.getInt("balance")+"");
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MainEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
