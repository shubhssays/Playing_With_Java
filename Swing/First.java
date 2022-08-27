import javax.swing.*;
import java.awt.Color;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Scanner;

class watcher {
    // String folderToWatch = "E:/Workspace/Java/WatchMe";

    public void init(String folderToWatch) {
        try {
            // Scanner sc = new Scanner(System.in);
            // System.out.print("Enter the path you wanna listen");
            // folderToWatch = sc.nextLine();
            // System.out.print("Path to listen :::>>>>> " + folderToWatch);

            System.out.print("Watching :::>>>>> " + folderToWatch);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderToWatch);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            boolean poll = true;
            while (poll) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind : " + event.kind() + " - File : " + event.context());
                }
                poll = key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class First {
    public static void main(String[] args) {
        try {
            JFrame f = new JFrame("Watch For Folder");// creating instance of JFrame
            f.setBackground(Color.MAGENTA);
            JTextField inputPath;
            JLabel pathName;
            JLabel errorMessage;
            pathName = new JLabel("Enter path you want to listen", JLabel.CENTER);
            errorMessage = new JLabel("Path Cannot Be Empty", JLabel.CENTER);
            pathName.setBounds(50, 50, 200, 30);
            pathName.setForeground(Color.BLUE);
            errorMessage.setBounds(50, 150, 200, 30);
            errorMessage.setForeground(Color.RED);
            errorMessage.setVisible(false);

            f.add(pathName);
            f.add(errorMessage);

            inputPath = new JTextField("", JLabel.CENTER);
            inputPath.setBounds(50, 100, 200, 30);
            f.add(inputPath);

            JButton stopButton = new JButton("Stop");// creating instance of JButton
            JButton startButton = new JButton("Start");// creating instance of JButton
            startButton.setBounds(50, 300, 100, 40);// x axis, y axis, width, height
            f.add(startButton);// adding button in JFrame

            startButton.addActionListener(e -> {
                String pathToWatch = inputPath.getText();
                if (pathToWatch.length() == 0) {
                    errorMessage.setVisible(true);
                    pathName.setVisible(false);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    errorMessage.setVisible(false);
                    pathName.setVisible(true);
                } else {
                    System.out.println("Watching Now... " + pathToWatch);
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    watcher watch = new watcher();
                    watch.init(pathToWatch);
                }
            });

            stopButton.setBounds(50, 400, 100, 40);// x axis, y axis, width, height
            stopButton.setEnabled(false);

            stopButton.addActionListener(e -> {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                System.out.println("Stopping...");
                System.exit(0);
            });

            f.add(stopButton);// adding button in JFrame

            f.setSize(300, 500);// 400 width and 500 height
            f.setLayout(null);// using no layout managers
            f.setVisible(true);// making the frame visible
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
