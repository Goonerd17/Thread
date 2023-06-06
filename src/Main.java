import java.util.*;

class User implements Runnable {
    private Computer computer;
    private String game;

    User(Computer computer, String game) {
        this.computer = computer;
        this.game = game;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            String name = Thread.currentThread().getName();

            computer.play(game);
            System.out.println(name + " play the " + game);
        }

    }

}

class Enterprise implements Runnable {
    private Computer computer;

    Enterprise(Computer computer) { this.computer = computer;}

    public void run() {
        while(true) {
            int idx = (int)(Math.random() * computer.gameNum());
            computer.install(computer.games[idx]);
            try {
                Thread.sleep(100) ;
            } catch(InterruptedException e) {}
        }

    }
}

class Computer {
    String[] games = {"Starcraft", "FIFA", "LOL"};
    final int MAX_GAME = 7;
    private ArrayList<String> volumes = new ArrayList<>();


    public synchronized void install(String game1) {
        while (volumes.size() >= MAX_GAME) {
            String name = Thread.currentThread().getName();
            System.out.println(name + " is watiting");

            try {
                wait();
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
        volumes.add(game1);
        notify();
        System.out.println("Volumes" + volumes.toString());
    }

    public void play(String game2) {
        synchronized (this) {
            String name = Thread.currentThread().getName();

            while (volumes.size() == 0) {
                System.out.println(name + " is waiting");
                try {
                    wait();
                } catch (InterruptedException e) {}
            }

            while (true) {
                for (int i = 0; i < volumes.size(); i++) {
                    if (game2.equals(volumes.get(i))) {
                        volumes.remove(i);
                        notify();
                        return;
                    }
                }

                try {
                    System.out.println(name + " is waiting");
                    wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {}
            }
        }
    }

    public int gameNum() {return games.length; }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();

        new Thread(new Enterprise(computer), "provide").start();
        new Thread(new User(computer, "Starcraft"), "User1").start();
        new Thread(new User(computer, "FIFA"), "User2").start();
        Thread.sleep(2000);
        System.exit(0);
    }
}

