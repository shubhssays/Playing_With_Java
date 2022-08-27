import javax.swing.*;
import java.awt.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.stream.Collectors;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WatchFolder {

    String pathToWatch = "";

    public void startToWatch(String folderToWatch) {
        try {
            System.out.println("Watching Now... " + folderToWatch);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderToWatch);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            boolean poll = true;
            WatchFolder wf = new WatchFolder();
            while (poll) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind : " + event.kind() + " - File : " + event.context());
                    wf.apiCall("http://localhost:3000");
                }
                poll = key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiCall(String URL) {
        try {
            URL url = new URL(URL);

            // Open a connection(?) on the URL(??) and cast the response(???)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Now it's "open", we can set the request method, headers etc.
            connection.setRequestProperty("accept", "application/json");

            // This line makes the request
            InputStream responseStream = connection.getInputStream();

            String result = new BufferedReader(new InputStreamReader(responseStream))
            .lines().collect(Collectors.joining("\n"));

            // Finally we have the response
            System.out.println("API RESPONSE ==> " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            WatchFolder watch = new WatchFolder();
            JFrame frame = new JFrame("Watch For Folder");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.MAGENTA);
            JLabel pathName;
            pathName = new JLabel("Enter path you want to listen", JLabel.CENTER);
            pathName.setBounds(50, 50, 300, 30);
            pathName.setForeground(Color.BLUE);

            frame.add(pathName);

            JButton chooseFolderButton = new JButton("Choose Folder");
            chooseFolderButton.setBounds(50, 200, 200, 40);
            frame.add(chooseFolderButton);

            JButton stopButton = new JButton("Stop");
            JButton startButton = new JButton("Start");
            startButton.setBounds(50, 300, 100, 40);
            frame.add(startButton);// adding button in JFrame

            JPanel panel = new JPanel();
            LayoutManager layout = new FlowLayout();
            panel.setLayout(layout);

            chooseFolderButton.addActionListener(e -> {
                chooseFolderButton.setFocusable(false);
                frame.getContentPane().add(panel, BorderLayout.CENTER);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    watch.pathToWatch = file.getAbsolutePath();
                    pathName.setText("Folder Selected: " + file.getName());
                } else {
                    watch.pathToWatch = "";
                    System.out.println("Open command canceled");
                }
            });

            startButton.addActionListener(e -> {
                startButton.setFocusable(false);
                if (watch.pathToWatch.length() > 0) {
                    watch.startToWatch(watch.pathToWatch);
                }
            });

            stopButton.setBounds(50, 400, 100, 40);
            frame.add(stopButton);
            stopButton.addActionListener(e -> {
                stopButton.setFocusable(false);
                System.out.println("Stopping...");
                pathName.setText("Enter path you want to listen");
                System.exit(0);
            });

            frame.setSize(300, 500);
            frame.setLayout(null);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
