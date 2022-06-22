package me.luligabi.contadorutfpr;

import java.text.DecimalFormat;

public class TweetUtils {

    public static String getProgressBar(float percentage) {
        int fullBlocks = (int) Math.floor((percentage * 100) / 5);
        return "▓".repeat(fullBlocks) + "░".repeat(Math.max(0, 20 - fullBlocks));
    }

    public static String getTweet(long daysLeft, String semester, String university, float percentage) {
        DecimalFormat df = new DecimalFormat("##.##%");

        return String.format("""
                %s apenas %d %s para o fim do período %s na %s! Você já cursou %s do período, parabéns!
                
                %s
                """,
                daysLeft != 1 ? "Faltam" : "Falta", daysLeft, daysLeft != 1 ? "dias" : "dia", semester, university, df.format(percentage), getProgressBar(percentage)
        );
    }

}