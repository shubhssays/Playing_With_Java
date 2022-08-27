package FolderWatcher;
import javax.swing.*;
import java.awt.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.io.*;
class watcher {

    public void init(String folderToWatch) {
        try {
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

public class WatchFolder {
    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("Watch For Folder");// creating instance of JFrame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.MAGENTA);
            JLabel pathName;
            pathName = new JLabel("Enter path you want to listen", JLabel.CENTER);
            pathName.setBounds(50, 50, 200, 30);
            pathName.setForeground(Color.BLUE);

            frame.add(pathName);

            JButton stopButton = new JButton("Stop");// creating instance of JButton
            JButton startButton = new JButton("Start");// creating instance of JButton
            startButton.setBounds(50, 300, 100, 40);// x axis, y axis, width, height
            frame.add(startButton);// adding button in JFrame

            JPanel panel = new JPanel();
            LayoutManager layout = new FlowLayout();
            panel.setLayout(layout);

            startButton.addActionListener(e -> {
                String pathToWatch = "";
                frame.getContentPane().add(panel, BorderLayout.CENTER);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    pathToWatch = file.getAbsolutePath();
                    pathName.setText("Folder Selected: " + pathToWatch);
                    System.out.println("Watching Now... " + pathToWatch);
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    watcher watch = new watcher();
                    watch.init(pathToWatch);
                } else {
                    System.out.println("Open command canceled");
                }
            });

            stopButton.setBounds(50, 400, 100, 40);// x axis, y axis, width, height
            stopButton.setEnabled(false);

            stopButton.addActionListener(e -> {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                System.out.println("Stopping...");
                pathName.setText("Enter path you want to listen");
                System.exit(0);
            });

            frame.add(stopButton);// adding button in JFrame

            frame.setSize(300, 500);// 400 width and 500 height
            frame.setLayout(null);// using no layout managers
            frame.setVisible(true);// making the frame visible
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
