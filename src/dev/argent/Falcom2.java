package dev.argent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Created by Saki on 2017-07-20.
 */
public class Falcom2 {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        int count = 27;
        String folderName = "temporary";
        String fileName = "sen3_%02dh.jpg";
        String template = "https://www.falcom.co.jp/sen3/img/special/wallpaper/" + fileName;
        new File("C:\\Users\\Saki\\Documents\\Wall Paper\\" + folderName).mkdir();
        Flow.Subscriber<Integer> subscriber = new Subscriber(item -> downloadFile(folderName, fileName, template, item));

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>(executorService, 256);
        publisher.subscribe(subscriber);
        IntStream.rangeClosed(1, count).forEach(publisher::submit);
        Scanner scanner = new Scanner(System.in);
        IntStream.rangeClosed(1, count).forEach(i -> publisher.submit(scanner.nextInt()));

        IntStream.rangeClosed(1, count).forEach(item -> {
            downloadFile(folderName, fileName, template, item);
        });

        executorService.shutdown();
    }

    private static void downloadFile(String folderName, String fileName, String template, int item) {
        try {
            URL url = new URL(String.format(template, item));
            Path target = Paths.get(String.format("C:\\Users\\Saki\\Documents\\Wall Paper\\" + folderName + File.separator + fileName, item));
//            Files.copy(url.openConnection().getInputStream(), target);
            System.out.println("[" + Thread.currentThread().getName() + "] " + url + " downloaded to " + target);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static class Subscriber implements Flow.Subscriber<Integer> {
        Consumer<Integer> consumer;

        public Subscriber(Consumer<Integer> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(Integer item) {
            consumer.accept(item);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("Done");
        }
    };
}
