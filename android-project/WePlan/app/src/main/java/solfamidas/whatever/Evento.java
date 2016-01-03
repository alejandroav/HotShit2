package solfamidas.whatever;

/**
 * Creado por Alejandro Alarcón Villena, 2015
 * Como proyecto para la asignatura Sistemas Multimedia
 * */
public class Evento {
    int id, capacity, current;
    String title, desc, image;
    double distance;
    String date;

    public Evento(int id, String title, String desc,  double distance, int capacity, int current, String image, String date) {
        this.id = id;
        this.capacity = capacity;
        this.current = current;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.distance = distance;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrent() {
        return current;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return "http://grizzly.pw" + image;
    }

    public double getDistance() {
        return Math.round(distance);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCurrentPeople() {
        return current + "/" + capacity;
    }
}
