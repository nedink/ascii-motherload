package main;

import java.io.IOException;

public enum Confirmation {
    SAVE_ON_QUIT("Save game? y/n", () -> {
        try { Main.saveAndQuit(); }
        catch (IOException e) { e.printStackTrace(); }
        Main.confirmation = null;
    }, () -> {
        Main.running = false;
        Main.confirmation = null;
    }),
    QUIT("Really quit? y/n", () -> {
        Main.confirmation = SAVE_ON_QUIT;
        System.out.print("\r" + Main.confirmation.getMessage() + "                                                                    ");
    }, () -> {
        Main.confirmation = null;
        System.out.print("\r                                                                    ");
    }),
    LOAD("Load save? y/n", () -> {
        try { Main.loadSave(); }
        catch (IOException e) { e.printStackTrace(); }
        Main.confirmation = null;
    }, () -> {
        Main.confirmation = null;
        System.out.print("\r                                                                    ");
    }),
    NEW_GAME("Start a new game? y/n", () -> {
        Main.newGame();
        Main.confirmation = null;
        System.out.print("\r                                                                    ");
    }, () -> {
        Main.confirmation = null;
        System.out.print("\r                                                                    ");
    })

    ;

    private String message;
    private Runnable successAction;
    private Runnable failureAction;

    private Confirmation(String message, Runnable successAction, Runnable failureAction) {
        this.message = message;
        this.successAction = successAction;
        this.failureAction = failureAction;
    }

    public String getMessage() {
        return message;
    }

    public Runnable getSuccessAction() {
        return successAction;
    }

    public Runnable getFailureAction() {
        return failureAction;
    }
}
