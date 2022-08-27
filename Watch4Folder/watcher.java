import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Scanner;

public class watcher {
    String folderToWatch = "E:/Workspace/Java/WatchMe";

    public void init(String watchThisLocation) {
        try {
            // Scanner sc = new Scanner(System.in);
            // System.out.print("Enter the path you wanna listen");
            // folderToWatch = sc.nextLine();
            // System.out.print("Path to listen :::>>>>> " + folderToWatch);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(watchThisLocation);
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

    public static void main(String... args) {
        watcher watch = new watcher();
        watch.init(watch.folderToWatch);
    }
}
