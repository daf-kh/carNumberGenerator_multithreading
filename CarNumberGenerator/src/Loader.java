import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        FileOutputStream writer = new FileOutputStream("res/numbers1.txt");

        char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

        Callable<String> task1 = () -> {
            for(int regionCode = 1; regionCode <= 20; regionCode ++) {
                generateNumbers(writer, letters, regionCode);
            }
            return "task1 is done";
        };

        Callable<String> task2 = () -> {
            for(int regionCode = 21; regionCode <= 40; regionCode ++) {
                generateNumbers(writer, letters, regionCode);
            }
            return "task2 is done";
        };

        Callable<String> task3 = () -> {
            for(int regionCode = 41; regionCode <= 60; regionCode ++) {
                generateNumbers(writer, letters, regionCode);
            }
            return "task3 is done";
        };

        Callable<String> task4 = () -> {
            for(int regionCode = 61; regionCode <= 80; regionCode ++) {
                generateNumbers(writer, letters, regionCode);
            }
            return "task4 is done";
        };

        Callable<String> task5 = () -> {
            for(int regionCode = 81; regionCode < 100; regionCode ++) {
                generateNumbers(writer, letters, regionCode);
            }
            return "task5 is done";
        };

        List<Callable<String>> callables = new ArrayList<>();
        callables.add(task1);
        callables.add(task2);
        callables.add(task3);
        callables.add(task4);
        callables.add(task5);

        ExecutorService executor = Executors.newWorkStealingPool();

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);



            writer.flush();
            writer.close();


        System.out.println((System.currentTimeMillis() - start) + " ms");
    }

    private static void padNumber(StringBuilder builder, int number, int numberLength) {
        String numberStr = Integer.toString(number);
        int padSize = numberLength - numberStr.length();

        builder.append("0".repeat(Math.max(0, padSize)));
        builder.append(numberStr);

    }

    private static void generateNumbers(FileOutputStream writer, char[] letters, int regionCode) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int number = 1; number < 1000; number++) {
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {
                        builder.append(firstLetter);
                        padNumber(builder, number, 3);
                        builder.append(secondLetter);
                        builder.append(thirdLetter);
                        padNumber(builder, regionCode, 2);
                        builder.append('\n');
                    }
                }
            }
        }
        writer.write(builder.toString().getBytes());
    }
}
