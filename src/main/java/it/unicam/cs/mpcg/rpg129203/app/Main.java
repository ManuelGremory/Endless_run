package it.unicam.cs.mpcg.rpg129203.app;
import it.unicam.cs.mpcg.rpg129203.model.JsonSaveSystem;
import it.unicam.cs.mpcg.rpg129203.model.SaveSystem;
import it.unicam.cs.mpcg.rpg129203.view.SwingStartView;
import it.unicam.cs.mpcg.rpg129203.controller.AppController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Assembliamo le dipendenze base
            SaveSystem saveSystem = new JsonSaveSystem();
            SwingStartView startView = new SwingStartView();

            // AppController prende il controllo e mostra la StartView
            new AppController(startView, saveSystem);
        });
    }
}