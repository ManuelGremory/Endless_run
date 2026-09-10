package it.unicam.cs.mpcg.rpg129203.view;

import it.unicam.cs.mpcg.rpg129203.controller.GameController;
import it.unicam.cs.mpcg.rpg129203.model.Enemy;
import it.unicam.cs.mpcg.rpg129203.model.Player;
import it.unicam.cs.mpcg.rpg129203.model.Skill;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.File;
import java.net.URL;

/**
 * Gestisce l'interfaccia grafica principale della schermata di combattimento.
 */
public class SwingGameView extends JFrame implements GameView {
    private GameController controller;
    private JLabel playerStatsLabel, enemyStatsLabel, killCountLabel;
    private JLabel playerImageLabel, enemyImageLabel;
    private JTextArea combatLog;
    private JPanel actionPanel;
    private JButton btnAttack;
    private JButton btnSaveQuit;

    /**
     * Costruttore: inizializza la finestra, i pannelli (header, log, azioni)
     * e configura la disposizione degli elementi visivi.
     */
    public SwingGameView() {
        setTitle("Endless RPG - Battaglia");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel principale
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Riquadro Giocatore (Immagine a sinistra, testo al centro)
        JPanel playerPanel = new JPanel(new BorderLayout(10, 0));
        playerImageLabel = new JLabel();
        playerStatsLabel = new JLabel();
        playerPanel.add(playerImageLabel, BorderLayout.WEST);
        playerPanel.add(playerStatsLabel, BorderLayout.CENTER);

        // Riquadro Centrale (Contatore Uccisioni)
        killCountLabel = new JLabel("Nemici: 0", SwingConstants.CENTER);
        killCountLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Riquadro Nemico (Testo a sinistra, Immagine bloccata a destra)
        JPanel enemyPanel = new JPanel(new BorderLayout(10, 0));
        enemyStatsLabel = new JLabel();
        enemyImageLabel = new JLabel();
        enemyPanel.add(enemyStatsLabel, BorderLayout.CENTER);
        enemyPanel.add(enemyImageLabel, BorderLayout.EAST);

        statsPanel.add(playerPanel);
        statsPanel.add(killCountLabel);
        statsPanel.add(enemyPanel);

        btnSaveQuit = new JButton("Salva ed Esci");
        btnSaveQuit.addActionListener(e -> controller.onSaveAndQuitPressed());

        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(btnSaveQuit, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Area di testo per i log di combattimento
        combatLog = new JTextArea();
        combatLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(combatLog);
        add(scrollPane, BorderLayout.CENTER);

        // Pannello Azioni
        actionPanel = new JPanel(new FlowLayout());
        btnAttack = new JButton("Attacca");
        btnAttack.addActionListener(e -> controller.onAttackPressed());
        actionPanel.add(btnAttack);
        add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Collega questa vista al controller per delegare la gestione degli eventi.
     */
    @Override
    public void setController(GameController controller) { this.controller = controller; }

    /**
     * Aggiorna le statistiche testuali e le immagini dell'eroe e dell'avversario.
     */
    @Override
    public void updateStats(Player player, Enemy enemy, int kills) {
        playerStatsLabel.setText(String.format("<html><b>%s</b> (Lv %d)<br>HP: %d/%d<br>Atk: %d<br>XP: %d/%d</html>",
                player.getName(), player.getLevel(), player.getHp(), player.getMaxHp(), player.getAttack(), player.getXp(), player.getXpToNextLevel()));

        playerImageLabel.setIcon(loadScaledIcon("eroe.png", 70, 70));

        if (enemy != null) {
            enemyStatsLabel.setText(String.format("<html><div style='text-align: right;'><b>%s</b> (Lv %d)<br>HP: %d/%d<br>Atk: %d</div></html>",
                    enemy.getName(), enemy.getLevel(), enemy.getHp(), enemy.getMaxHp(), enemy.getAttack()));

            String enemyNameLower = enemy.getName().toLowerCase();
            String imageName = enemyNameLower.split(" ")[0] + ".png";

            ImageIcon enemyIcon = loadScaledIcon(imageName, 70, 70);
            if (enemyIcon == null) {
                enemyIcon = loadScaledIcon("slime.png", 70, 70);
            }
            enemyImageLabel.setIcon(enemyIcon);
        }
        killCountLabel.setText("Nemici Sconfitti: " + kills);
    }

    /**
     * Aggiunge un nuovo messaggio all'area di log testuale scorrendo verso il basso.
     */
    @Override
    public void appendLog(String message) {
        combatLog.append(message + "\n");
        combatLog.setCaretPosition(combatLog.getDocument().getLength());
    }

    /**
     * Aggiorna e mostra i pulsanti delle abilità in base a quelle apprese dal giocatore.
     */
    @Override
    public void updateSkillButtons(List<Skill> skills) {
        actionPanel.removeAll();
        actionPanel.add(btnAttack);
        for (Skill skill : skills) {
            String btnText = String.format("%s (%d/%d)", skill.getName(), skill.getRemainingUses(), skill.getMaxUses());
            JButton btnSkill = new JButton(btnText);
            if(skill.getRemainingUses() <= 0) {
                btnSkill.setEnabled(false);
            }
            btnSkill.addActionListener(e -> controller.onSkillPressed(skill));
            actionPanel.add(btnSkill);
        }
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Blocca l'interfaccia e mostra il messaggio di sconfitta.
     */
    @Override
    public void showGameOver() {
        btnAttack.setEnabled(false);
        btnSaveQuit.setText("Ritorna al Menu");
        for (Component c : actionPanel.getComponents()) c.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Sei Morto! Game Over.", "Game Over", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Apre la finestra di dialogo dedicata alla vittoria contro un nemico.
     */
    @Override
    public void showVictoryDialog(String enemyName, int xpGained, Player player, List<Skill> learnableSkills, Runnable onContinue) {
        SwingVictoryDialog dialog = new SwingVictoryDialog(this, enemyName, xpGained, player, learnableSkills, onContinue);
        dialog.setVisible(true);
    }

    /**
     * Mostra un popup con un messaggio generico.
     */
    @Override
    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message); }

    /**
     * Chiude e distrugge la finestra di gioco.
     */
    @Override
    public void closeView() { this.dispose(); }

    /**
     * Cerca un file immagine nei percorsi locali o di build e lo restituisce ridimensionato.
     */
    private ImageIcon loadScaledIcon(String fileName, int width, int height) {
        String[] possiblePaths = {
                "src/resources/" + fileName,
                "resources/" + fileName,
                fileName
        };

        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        }

        URL imgURL = getClass().getClassLoader().getResource("resources/" + fileName);
        if (imgURL == null) {
            imgURL = getClass().getClassLoader().getResource(fileName);
        }
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }

        return null;
    }
}