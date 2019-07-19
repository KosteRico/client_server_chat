package com.kosterico.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;

public class Recorder {

    private String id;

    private File wavFile;

    private AudioFileFormat.Type fileType;

    private TargetDataLine line;

    private final AudioFormat format;

    public Recorder() throws IOException {
        fileType = AudioFileFormat.Type.WAVE;
        System.out.println(getClass().getResource(""));
        wavFile = new File("Network/resources/mes_" + UUID.randomUUID().toString() + ".wav");

        if (!wavFile.createNewFile()) {
            throw new FileAlreadyExistsException("File " + wavFile.getName() + " already exists");
        }

        format = new AudioFormat(16_000.0f,
                8,
                2,
                true,
                true);
    }

    public void start() throws LineUnavailableException, IOException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Line not supported");
        }

        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        AudioInputStream ais = new AudioInputStream(line);

        AudioSystem.write(ais, fileType, wavFile);
    }

    public void stop() {
        line.stop();
        line.close();
    }

    public Player getPlayer() throws FileNotFoundException {
        return new Player(wavFile);
    }

}
