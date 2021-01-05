package it.polimi.ingsw.net.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TimerTask;

public class ThreadTimer extends TimerTask {
    private PlayersDB playersDB;

    public ThreadTimer(PlayersDB playersDB){
        this.playersDB = playersDB;
    }

    /**
     * set the end of the waiting timer
     */
    public void run(){
        System.out.println("Timer finito");
        playersDB.setTimeOut(true);
    }
}
