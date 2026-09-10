package it.unicam.cs.mpcg.rpg129203.view;

import it.unicam.cs.mpcg.rpg129203.controller.AppController;
import javax.swing.*;
import java.awt.*;

/**
 * Finestra di avvio (menu principale) dell'applicazione.
 * Gestisce l'inserimento del nome, il caricamento della partita e la visualizzazione del record.
 */
public class SwingStartView extends JFrame implements StartView {
    private AppController controller;
    private JTextField nameField;
    private JButton btnNewGame;
    private JButton btnLoadGame;
    private JLabel lblHighScore;

    /**
     * Costruttore: inizializza il layout, il titolo, il form per il nome e i pulsanti di avvio.
     */
    public SwingStartView() {
        setTitle("Endless RPG - Avvio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra lo schermo
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel title = new JLabel("ENDLESS RPG", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);

        lblHighScore = new JLabel("Record Personale: 0", SwingConstants.CENTER);
        add(lblHighScore);

        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.add(new JLabel("Nome Eroe: "));
        nameField = new JTextField("Eroe", 15);
        namePanel.add(nameField);
        add(namePanel);

        btnNewGame = new JButton("Nuova Partita");
        btnNewGame.addActionListener(e -> controller.onNewGame(nameField.getText()));
        add(btnNewGame);

        btnLoadGame = new JButton("Carica Partita");
        btnLoadGame.addActionListener(e -> controller.onLoadGame());
        add(btnLoadGame);
    }

    /**
     * Collega la vista al controller principale dell'applicazione.
     */
    @Override
    public void setController(AppController controller) { this.controller = controller; }

    /**
     * Rende visibile la schermata iniziale.
     */
    @Override
    public void showView() { setVisible(true); }

    /**
     * Nasconde la schermata iniziale.
     */
    @Override
    public void hideView() { setVisible(false); }

    /**
     * Aggiorna l'etichetta che mostra il record massimo di uccisioni.
     */
    @Override
    public void updateHighScore(int score) { lblHighScore.setText("Record Personale: " + score); }

    /**
     * Abilita o disabilita il pulsante di caricamento (es. se non c'è un salvataggio valido).
     */
    @Override
    public void setLoadButtonEnabled(boolean enabled) { btnLoadGame.setEnabled(enabled); }
}