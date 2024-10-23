package org.example;

import javax.sound.sampled.*;

public class Sound {

    // Statische Methode, die einen Ton mit bestimmter Frequenz und Dauer in einem eigenen Thread abspielt
    public static void playTone(int frequency, double duration) {
        // Erzeuge und starte einen neuen Thread für die Sound-Wiedergabe
        new Thread(() -> {
            try {
                float sampleRate = 44100; // Abtastrate (Samples pro Sekunde)

                // Erzeuge einen Puffer für den Ton
                byte[] buffer = new byte[(int)(sampleRate * duration)];
                for (int i = 0; i < buffer.length; i++) {
                    double angle = 2.0 * Math.PI * i / (sampleRate / frequency);
                    buffer[i] = (byte)(Math.sin(angle) * 127); // Sinuswelle
                }

                // Setze Audioformat auf 8-bit PCM
                AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();

                // Spiele den Ton ab
                line.write(buffer, 0, buffer.length);

                // Stoppe und schließe die Audioleitung
                line.drain();
                line.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start(); // Startet den Thread
    }


}
