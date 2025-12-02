package defecto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int raceNumber;
    private String nationality;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToMany
    @JoinTable(
            name = "driver_race",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "race_id")
    )
    private List<Race> races = new ArrayList<>();

    public Driver() {
    }

    public Driver(String name, int raceNumber, String nationality) {
        this.name = name;
        this.raceNumber = raceNumber;
        this.nationality = nationality;
    }


    public void addRace(Race race) {
        races.add(race);
        if (!race.getDrivers().contains(this)) {
            race.getDrivers().add(this);
        }
    }

    public void removeRace(Race race) {
        races.remove(race);
        race.getDrivers().remove(this);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        if (team != null && !team.getDrivers().contains(this)) {
            team.getDrivers().add(this);
        }
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Driver{id=" + id + ", name='" + name + '\'' + ", raceNumber=" + raceNumber +
                ", nationality='" + nationality + '\'' + '}';
    }
}