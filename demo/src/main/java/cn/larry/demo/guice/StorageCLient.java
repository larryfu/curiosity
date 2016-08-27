package cn.larry.demo.guice;

/**
 * Created by larry on 16-8-27.
 */
public class StorageCLient {

    public static void main(String[] args) {

        // Making use of file storage.
        Storage storage = new FileStorage();
        storage.store("123", new Object());

        // Making use of the database.
        storage = new DatabaseStorage();
        storage.store("456", new Object());
    }
}
