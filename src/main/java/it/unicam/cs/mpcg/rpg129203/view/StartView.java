package it.unicam.cs.mpcg.rpg129203.view;

import it.unicam.cs.mpcg.rpg129203.controller.AppController;

public interface StartView {
    void setController(AppController controller);
    void showView();
    void hideView();
    void updateHighScore(int score);
    void setLoadButtonEnabled(boolean enabled);
}