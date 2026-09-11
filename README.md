# Endless-run
Progetto per il corso di Metodologie di Programmazione (AA 2025/26) - Università di Camerino.

Questo progetto è un semplice ma completo videogioco di ruolo (RPG) a turni con progressione infinita, sviluppato in Java con interfaccia grafica Swing e basato sul pattern architetturale MVC (Model-View-Controller).

Il gioco rappresenta un eroe che affronta dei combattimenti 1 vs 1 contro svariati nemici, col tempo può progredire di livello, imparare abilità e migliorare le proprie statistiche.
Quanto lontano riuscirai a portare il nostro eroe?

## Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle

### Istruzioni

```bash
git clone https://github.com/ManuelGremory/Endless-run.git
cd Endless-run
```

### Build del progetto
```bash
./gradlew build    # Linux/Mac
.\\gradlew build   # Windows PowerShell

```

### Esecuzione
```bash
./gradlew run    # Linux/Mac
.\\gradlew run   # Windows PowerShell
```

## Struttura del progetto

Il codice sorgente è organizzato seguendo rigorosamente il pattern architetturale MVC (Model-View-Controller), suddividendo il sistema in package dedicati per garantire la netta separazione delle responsabilità.

Di seguito una panoramica della struttura dei package all'interno di `src/it/unicam/cs/mpcg/rpg129203/`:

*   **`model/` (Core Logic):** Contiene l'intero dominio applicativo.
    *   Le entità principali del gioco (`Character`, `Player`, `Enemy`).
    *   La logica degli scontri e dello stato della partita (`GameEngine`).
    *   Il sistema di abilità tramite l'interfaccia `Skill` e le sue implementazioni (`HealSkill`, `CriticalHitSkill`, `FireballSkill`), gestite dal `SkillRegistry`.
    *   La gestione della persistenza e della serializzazione dei dati (`SaveSystem`, `JsonSaveSystem`, `SaveData`).
*   **`view/` (User Interface):** Contiene unicamente i componenti dell'interfaccia grafica (GUI).
    *   Implementa le schermate di gioco utilizzando le librerie **Swing** (`SwingStartView`, `SwingGameView`).
    *   Le classi in questo package si occupano solo di renderizzare i dati e catturare gli input dell'utente, senza eseguire logica di business.
*   **`controller/` (Gestione del Flusso):** Contiene gli intermediari tra il Model e la View.
    *   `AppController`: Coordina il ciclo di vita generale dell'applicazione (avvio, salvataggio, transizione tra menu e gioco).
    *   `GameController`: Gestisce la logica di medio-livello durante la partita, traducendo le azioni dell'utente (click sui bottoni) in comandi per il `GameEngine` e aggiornando la `View` di conseguenza.

## Comandi
Usare il mouse per direzionarsi all'interno dell'interfaccia.
