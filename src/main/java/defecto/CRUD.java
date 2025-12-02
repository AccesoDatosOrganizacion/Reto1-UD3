package defecto;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class CRUD {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("F1PU");
        EntityManager em = emf.createEntityManager();

        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\n=== MENÚ CRUD FÓRMULA 1 ===");
                System.out.println("1. CREATE - Crear datos iniciales F1");
                System.out.println("2. READ - Mostrar equipos, pilotos y carreras");
                System.out.println("3. UPDATE - Actualizar piloto");
                System.out.println("4. DELETE - Eliminar carrera");
                System.out.println("5. Salir");
                System.out.print("Selecciona opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> createDatos(em);
                    case 2 -> readDatos(em);
                    case 3 -> updateDatos(em, scanner);
                    case 4 -> deleteDatos(em, scanner);
                    case 5 -> {
                        System.out.println("Saliendo...");
                        return;
                    }
                    default -> System.out.println("Opción no válida");
                }
            }
        } finally {
            scanner.close();
            em.close();
            emf.close();
        }
    }

    private static void createDatos(EntityManager em) {
        em.getTransaction().begin();

        try {
            Team ferrari = new Team("Ferrari", "Italia");
            Team redbull = new Team("Red Bull", "Austria");

            Driver leclerc = new Driver("Charles Leclerc", 16, "Monaco");
            Driver sainz = new Driver("Carlos Sainz", 55, "España");
            Driver verstappen = new Driver("Max Verstappen", 1, "Países Bajos");

            leclerc.setTeam(ferrari);
            sainz.setTeam(ferrari);
            verstappen.setTeam(redbull);

            Race bahrain = new Race("GP Baréin", "Sakhir", LocalDate.of(2024, 3, 10));
            Race jeddah = new Race("GP Arabia Saudí", "Jeddah", LocalDate.of(2024, 3, 17));

            // ManyToMany
            leclerc.getRaces().add(bahrain);
            sainz.getRaces().add(bahrain);
            verstappen.getRaces().add(bahrain);

            verstappen.getRaces().add(jeddah);

            em.persist(ferrari);
            em.persist(redbull);
            em.persist(leclerc);
            em.persist(sainz);
            em.persist(verstappen);
            em.persist(bahrain);
            em.persist(jeddah);

            em.getTransaction().commit();

            System.out.println("Datos F1 creados exitosamente");

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error al crear datos: " + e.getMessage());
        }
    }

    private static void readDatos(EntityManager em) {
        System.out.println("\n=== ESCUDERÍAS ===");

        List<Team> teams = em.createQuery("SELECT t FROM Team t", Team.class).getResultList();
        for (Team t : teams) {
            System.out.println("\nEquipo: " + t.getName() + " (" + t.getCountry() + ")");
        }

        System.out.println("\n=== PILOTOS ===");
        List<Driver> drivers = em.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
        for (Driver d : drivers) {
            System.out.println("\nPiloto: " + d.getName() + " #" + d.getRaceNumber());
            System.out.println("Escudería: " + d.getTeam().getName());
            System.out.println("Carreras: " + d.getRaces().size());
        }

        System.out.println("\n=== CARRERAS ===");
        List<Race> races = em.createQuery("SELECT r FROM Race r", Race.class).getResultList();
        for (Race r : races) {
            System.out.println("\n" + r.getGrandPrix() + " - " + r.getLocation());
        }
    }

    private static void updateDatos(EntityManager em, Scanner scanner) {
        System.out.print("ID del piloto a actualizar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Nuevo número de carrera: ");
        int nuevoNumero = scanner.nextInt();
        scanner.nextLine();

        em.getTransaction().begin();

        try {
            Driver d = em.find(Driver.class, id);
            if (d != null) {
                d.setRaceNumber(nuevoNumero);
                System.out.println("Piloto actualizado");
            } else {
                System.out.println("No encontrado");
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    private static void deleteDatos(EntityManager em, Scanner scanner) {
        System.out.print("Nombre del GP a eliminar: ");
        String nombre = scanner.nextLine();

        em.getTransaction().begin();

        try {
            Race r = em.createQuery(
                            "SELECT r FROM Race r WHERE r.grandPrix = :n", Race.class)
                    .setParameter("n", nombre)
                    .getSingleResult();

            em.remove(r);

            System.out.println("Carrera eliminada");

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error eliminando carrera");
        }
    }
}
