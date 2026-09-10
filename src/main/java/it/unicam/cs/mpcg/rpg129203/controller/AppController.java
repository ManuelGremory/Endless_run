package it.unicam.cs.mpcg.rpg129203.controller;

import it.unicam.cs.mpcg.rpg129203.model.*;
import it.unicam.cs.mpcg.rpg129203.view.*;

/**
 * Gestisce il ciclo di vita dell'applicazione, passando dal menu principale al gioco attivo,
 * e gestendo il sistema di salvataggio generale.
 */
public class AppController {
    private StartView startView;
    private SaveSystem saveSystem;
    private int highScore = 0;

    /**
     * Costruttore: inizializza la vista iniziale, il sistema di salvataggio e mostra il menu.
     */
    public AppController(StartView startView, SaveSystem saveSystem) {
        this.startView = startView;
        this.saveSystem = saveSystem;
        this.startView.setController(this);
        initStartScreen();
    }

    /**
     * Carica i dati salvati per impostare il record personale e verificare se esiste
     * una partita caricabile (giocatore non morto).
     */
    private void initStartScreen() {
        SaveData data = saveSystem.load();
        boolean canLoad = false;
        if (data != null) {
            this.highScore = data.highScore;
            canLoad = data.hp > 0; // Puoi caricare solo se non sei morto nell'ultimo save
        }
        startView.updateHighScore(this.highScore);
        startView.setLoadButtonEnabled(canLoad);
        startView.showView();
    }

    /**
     * Avvia una nuova partita creando un GameEngine vuoto e passando alla schermata di gioco.
     */
    public void onNewGame(String playerName) {
        startView.hideView();
        GameEngine engine = new GameEngine(playerName);
        startGame(engine);
    }

    /**
     * Carica una partita esistente ripristinando i dati nel GameEngine.
     */
    public void onLoadGame() {
        SaveData data = saveSystem.load();
        if (data != null && data.hp > 0) {
            startView.hideView();
            GameEngine engine = new GameEngine(data.name);
            engine.restoreData(data);
            startGame(engine);
        }
    }

    /**
     * Metodo di supporto che istanzia la vista di gioco e cede il controllo al GameController.
     */
    private void startGame(GameEngine engine) {
        SwingGameView gameView = new SwingGameView();
        // Passiamo dall'AppController al GameController
        new GameController(engine, gameView, this);
        gameView.setVisible(true);
    }

    /**
     * Gestisce la sconfitta: aggiorna il record, sovrascrive il salvataggio per impedire
     * il respawn e riporta l'utente al menu principale.
     */
    public void onGameOver(GameEngine engine, GameView gameView) {
        updateHighScore(engine.getKillCount());
        SaveData data = engine.exportData();
        data.highScore = this.highScore;
        saveSystem.save(data); // Sovrascrive il salvataggio con hp=0 (morto) e nuovo record
        gameView.closeView();
        initStartScreen();
    }

    /**
     * Salva i progressi attuali, chiude la sessione di gioco e torna al menu principale.
     */
    public void onSaveAndQuit(GameEngine engine, GameView gameView) {
        updateHighScore(engine.getKillCount());
        SaveData data = engine.exportData();
        data.highScore = this.highScore;
        saveSystem.save(data);
        gameView.closeView();
        initStartScreen();
    }

    /**
     * Verifica se le uccisioni correnti superano il record salvato e lo aggiorna.
     */
    private void updateHighScore(int currentKills) {
        if (currentKills > this.highScore) {
            this.highScore = currentKills;
        }
    }
}