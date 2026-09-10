package it.unicam.cs.mpcg.rpg129203.view;

import it.unicam.cs.mpcg.rpg129203.model.Player;
import it.unicam.cs.mpcg.rpg129203.model.Skill;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.net.URL;

/**
 * Finestra di dialogo (modale) mostrata alla sconfitta di un avversario.
 * Gestisce il resoconto dello scontro e la scelta della ricompensa in caso di Level Up.
 */
public class SwingVictoryDialog extends JDialog {
    private Player player;
    private List<Skill> learnableSkills;
    private Runnable onContinue;
    private JRadioButton rbStats;
    private JRadioButton rbSkill;
    private Skill selectedSkillToLearn;

    /**
     * Costruttore: struttura l'interfaccia della finestra di vittoria, mostrando l'XP
     * guadagnata e le opzioni di potenziamento se l'eroe sale di livello.
     */
    public SwingVictoryDialog(Frame parent, String enemyName, int xpGained, Player player, List<Skill> learnableSkills, Runnable onContinue) {
        super(parent, "Vittoria!", true);
        this.player = player;
        this.learnableSkills = learnableSkills;
        this.onContinue = onContinue;

        setSize(520, 420);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Header Panel con l'immagine della medaglia adattata
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        headerPanel.setBackground(new Color(230, 245, 230));

        // Passando -1 all'altezza, getScaledInstance ridimensiona l'immagine mantenendo le proporzioni originali
        ImageIcon medalIcon = loadScaledIcon("medaglia.png", 70, -1);
        if (medalIcon != null) {
            JLabel medalLabel = new JLabel(medalIcon);
            headerPanel.add(medalLabel);
        }

        JLabel titleLabel = new JLabel("VITTORIA CONSEGUITA!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(34, 139, 34));
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Content Panel (ripristinato il layout verticale semplice)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel infoLabel = new JLabel(String.format(
                "<html><b>Hai sconfitto:</b> %s<br>" +
                        "<b>XP Ottenuti:</b> +%d<br></html>",
                enemyName, xpGained, player.getHp(), player.getMaxHp()
        ));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allinea a sinistra
        contentPanel.add(infoLabel);

        contentPanel.add(Box.createVerticalStrut(20)); // Spazio extra prima della riga successiva

        // Sezione Level Up / Buff o Punti XP mancanti
        if (player.canLevelUp()) {
            JPanel levelUpBox = new JPanel();
            levelUpBox.setLayout(new BoxLayout(levelUpBox, BoxLayout.Y_AXIS));
            levelUpBox.setBorder(BorderFactory.createTitledBorder(" LEVEL UP! Scegli la tua Ricompensa "));
            levelUpBox.setAlignmentX(Component.LEFT_ALIGNMENT);

            ButtonGroup optionGroup = new ButtonGroup();
            rbStats = new JRadioButton("Aumenta Statistiche (+10 Max HP, +3 Attacco)");
            rbStats.setSelected(true);
            optionGroup.add(rbStats);
            levelUpBox.add(rbStats);

            if (!learnableSkills.isEmpty()) {
                selectedSkillToLearn = learnableSkills.get(new Random().nextInt(learnableSkills.size()));
                rbSkill = new JRadioButton("Impara Nuova Abilità: " + selectedSkillToLearn.getName());
                optionGroup.add(rbSkill);
                levelUpBox.add(rbSkill);
            } else {
                JLabel noMoreSkillsLabel = new JLabel("<html><i>(Tutte le abilità disponibili sono già state apprese)</i></html>");
                noMoreSkillsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                levelUpBox.add(noMoreSkillsLabel);
            }

            contentPanel.add(levelUpBox);
        } else {
            // Testo formattato nello stesso stile dei testi superiori
            JLabel statusLabel = new JLabel(String.format("<html><b>Prossimo Livello tra:</b> %d XP</html>", player.getXpToNextLevel() - player.getXp()));
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allinea a sinistra per combaciare
            contentPanel.add(statusLabel);
        }

        add(contentPanel, BorderLayout.CENTER);

        // Pulsante di Conferma
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnContinue = new JButton("Continua al Prossimo Scontro");
        btnContinue.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinue.addActionListener(e -> {
            applyChoice();
            dispose();
            if (onContinue != null) {
                onContinue.run();
            }
        });
        bottomPanel.add(btnContinue);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Legge la selezione dell'utente al level up e applica il bonus statistico
     * o la nuova abilità all'eroe prima di procedere.
     */
    private void applyChoice() {
        if (player.canLevelUp()) {
            if (rbSkill != null && rbSkill.isSelected() && selectedSkillToLearn != null) {
                player.learnSkill(selectedSkillToLearn);
            } else {
                player.levelUpStats();
            }
        }
    }

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