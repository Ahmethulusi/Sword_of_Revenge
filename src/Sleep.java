//
//import java.util.Scanner;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class SafeHouse extends NormalLoc {
//    public SafeHouse(Player player) {
//        super(player, "Güvenli Ev");
//    }
//
//    @Override
//    public boolean onLocatin() {
//        boolean flag = true;
//
//            System.out.println("Kaç saat uyumak istiyorsunuz ?");
//            int time = input.nextInt();
//            ExecutorService executorService = Executors.newFixedThreadPool(1);
//            executorService.execute(() -> {
//                for (int i = 0; i < time; i++) {
//                    try {
//                        Thread.sleep(500); // Her 1 saniyede bir canı 10'er artıracak
//                        int newHealth = this.getPlayer().getHealth() + 10;
//                        if (newHealth < 100) {
//                            this.getPlayer().setHealth(newHealth);
//                        } else if (newHealth==100) {
//                            break;
//                        } else {
//                            this.getPlayer().setHealth(100);
//                        }
//                        System.out.println("Canınız: " + this.getPlayer().getHealth());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            });
//
//            try {
//                executorService.awaitTermination(time, TimeUnit.SECONDS);
//                executorService.shutdown();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return true;
//    }
//}
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Sleep extends NormalLoc {

    public Sleep(Player player) {
        super(player, "Uyu");
    }

    @Override
    public boolean onLocatin() {
        Scanner input = new Scanner(System.in);

        System.out.println("Kaç saat uyumak istiyorsunuz ?");
        int sleepHours = input.nextInt();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            int maxHealth = getPlayer().getMax_health();
            int healthIncreasePerSecond = 10;
            int totalHealthIncrease = sleepHours * healthIncreasePerSecond;
            int currentHealth = getPlayer().getHealth();

            for (int i = 0; i < totalHealthIncrease && currentHealth < maxHealth; i += healthIncreasePerSecond) {
                currentHealth += healthIncreasePerSecond;
                if (currentHealth > maxHealth) {
                    currentHealth = maxHealth;
                }
                getPlayer().setHealth(currentHealth);
                System.out.println("Canınız: " + currentHealth);
                try {
                    Thread.sleep(1000); // Her 1 saniyede bir canı artır
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Uyku bitti.");
        });

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        return true;
    }
}
