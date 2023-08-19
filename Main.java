import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Foo foo = new Foo();

        CompletableFuture.runAsync(foo::third);
        CompletableFuture.runAsync(foo::first);
        CompletableFuture.runAsync(foo::second);

        Thread.sleep(1000);
    }

    private static class Foo {

        private final Semaphore betweenFirstAndSecond = new Semaphore(0);
        private final Semaphore betweenSecondAndThird = new Semaphore(0);

        public void first() {
            print("first");
            betweenFirstAndSecond.release();
        }
        public void second() {
            try {
                betweenFirstAndSecond.acquire();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            print("second");
            betweenSecondAndThird.release();
        }

        public void third() {
            try {
                betweenSecondAndThird.acquire();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            print("third");
        }

        private void print(String message) {
            System.out.print(message);
        }
    }
}
