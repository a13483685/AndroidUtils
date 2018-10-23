package xie.com.androidutils.IPC.ContentProviderTest;

public class Book {
    String Name = null;
    int id ;

    public Book(String name, int id) {
        Name = name;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "Name='" + Name + '\'' +
                ", id=" + id +
                '}';
    }
}
