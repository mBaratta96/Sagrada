package it.polimi.ingsw.net.client.socket;

import it.polimi.ingsw.view.PlayerView;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class ClientSocketMap {

    private static final Map<String, BiConsumer<Scanner, PlayerView>> actions = new HashMap<>();

    static{
        actions.put("Private", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setPrivateCards(str);
        });
        actions.put("SchemaCard", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setSchemaCards(str);
        });
        actions.put("Dash", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setGlassDash(str);
        });
        actions.put("PublicTarget", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setPublicTargetCard(str);
        });
        actions.put("Tool", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setToolCards(str);
        });
        actions.put("Players", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setOtherPlayer(str);
        });
        actions.put("ReserveView", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.setReserve(str);
        });
        actions.put("Timer", (s,p) -> {
                                        String str = s.nextLine();
                                        p.setTurnTimer(str);
        });
        actions.put("Your Turn", (s,p) -> {
                                        String str = s.nextLine();
                                        p.setIsYourTurn(str);
        });
        actions.put("It's not your turn", (s,p) -> p.setIsNotYourTurn());
        actions.put("Remove Reserve", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.removeReseve(str);
        });
        actions.put("UpdateUserSchema", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.updateUserSchema(str);
        });
        actions.put("UpdateOtherUserSchema", (s,p) ->{
                                        String str = s.nextLine();
                                        System.out.println(str);
                                        p.updateOtherUserSchema(str);
        });
        actions.put("notValidDice", (s,p) -> p.notValidDice());
        actions.put("ValidPlacement", (s,p) -> p.validPlacement());
        actions.put("addTokenMessage", (s,p) ->{
                                        String str = s.nextLine();
                                        p.addToken(str);
        });


        actions.put("AddToRoundTrack", (s,p) -> {
                                        String str = s.nextLine();
                                        p.addToRoundTrack(str);
        });
        actions.put("Login", (s,p) -> p.login(s));
        actions.put("Ranking", (s,p) -> {
                                        String str = s.nextLine();
                                        p.finalPoints(str);
        });
        actions.put("RemoveUserSchema", (s,p) -> {
                                        String str = s.nextLine();
                                        p.removeUserSchema(str);
        });
        actions.put("RemoveOtherUserSchema", (s,p) ->{
                                        String str = s.nextLine();
                                        p.removeOtherUserSchema(str);
        });
        actions.put("notValidToolCardAction", (s,p) -> p.notVaildToolCardAction());
        actions.put("UpdateRoundTrack", (s,p) -> {
                                        String str = s.nextLine();
                                        p.updateRoundTracker(str);
        });
        actions.put("validToolCardAction", (s,p) -> p.validToolCard());
        actions.put("New Disconnected", (s,p) -> {
                                        String str = s.nextLine();
                                        p.setOfflinePlayer(str);

        });
        actions.put("Reconnected", (s,p) ->{
                                        String str = s.nextLine();
                                        p.setOnlinePlayer(str);
        });
        actions.put("showRoundView", (s,p) -> p.showRoundView());
        actions.put("tokenConfirmed", (s,p) -> {
                                             String str = s.nextLine();
                                             p.tokenConfirmed(str);
        });
        actions.put("notEnoughTokens", (s,p) -> {
                                        String str = s.nextLine();
                                        p.notEnoughTokens(str);
        });
        actions.put("Current tokens", (s,p) -> {
                                        String str = s.nextLine();
                                        p.setTokens(str);
        });
        actions.put("Tokens removed", (s,p) -> {
                                    String str = s.nextLine();
                                    p.removeTokens(str);
        });
        actions.put("ToolOneToken", (s,p) -> {
            String str = s.nextLine();
            p.setToolOneToken(str);
        });
    }

    public static BiConsumer<Scanner, PlayerView> getActions(String s) {
        return actions.get(s);
    }

    public static boolean isPresent(String s){
        return actions.keySet().contains(s);
    }
}
