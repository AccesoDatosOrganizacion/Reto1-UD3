package defecto;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grandPrix;
    private String location;
    private LocalDate date;

    @ManyToMany(mappedBy = "races")
    private List<Driver> drivers = new ArrayList<>();

    public Race() {}

    public Race(String grandPrix, String location, LocalDate date) {
        this.grandPrix = grandPrix;
        this.location = location;
        this.date = date;
    }


    public void addDriver(Driver driver) {
        drivers.add(driver);
        if (!driver.getRaces().contains(this)) {
            driver.getRaces().add(this);
        }
    }

    public void removeDriver(Driver driver) {
        drivers.remove(driver);
        driver.getRaces().remove(this);
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGrandPrix() { return grandPrix; }
    public void setGrandPrix(String grandPrix) { this.grandPrix = grandPrix; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<Driver> getDrivers() { return drivers; }
    public void setDrivers(List<Driver> drivers) { this.drivers = drivers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Race)) return false;
        Race race = (Race) o;
        return Objects.equals(id, race.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Race{id=" + id + ", grandPrix='" + grandPrix + '\'' + ", location='" + location + '\'' +
                ", date=" + date + '}';
    }
}