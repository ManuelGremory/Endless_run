package it.unicam.cs.mpcg.rpg129203.view;
import it.unicam.cs.mpcg.rpg129203.model.Player;
import it.unicam.cs.mpcg.rpg129203.model.Enemy;
import it.unicam.cs.mpcg.rpg129203.model.Skill;
import it.unicam.cs.mpcg.rpg129203.controller.GameController;
import java.util.List;

public interface GameView {
    void setController(GameController controller);
    void updateStats(Player player, Enemy enemy, int kills);
    void appendLog(String message);
    void updateSkillButtons(List<Skill> skills);
    void showGameOver();
    void showVictoryDialog(String enemyName, int xpGained, Player player, List<Skill> learnableSkills, Runnable onContinue);
    void showMessage(String message);
    void closeView();
}