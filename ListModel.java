package project2GIVE_TO_STUDENTS;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ListModel extends AbstractTableModel {
    /** holds all the rentals */
    private ArrayList<Rental> listOfRentals;

    /** holds only the rentals that are to be displayed */
    private ArrayList<Rental> filteredListRentals;

    /** current screen being displayed */
    private ScreenDisplay display = ScreenDisplay.CurrentRentalStatus;

    private String[] columnNamesCurrentRentals = {"Renter\'s Name", "Est. Cost",
            "Rented On", "Due Date ", "Console", "Name of the Game"};

    private String[] columnNamesforRented = {"Renter\'s Name", "Rented On Date",
            "Actual date returned ", "Est. Cost", " Real Cost"};

    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public ListModel() {
        display = ScreenDisplay.CurrentRentalStatus;
        listOfRentals = new ArrayList<>();
        filteredListRentals = new ArrayList<>();
        updateScreen();
        createList();
    }

    public void setDisplay(ScreenDisplay selected) {
        display = selected;
        updateScreen();
    }

    private void updateScreen() {
        switch (display) {
            case CurrentRentalStatus:
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().
                        filter(n -> n.actualDateReturned == null)
                        .collect(Collectors.toList());

                // Note: This uses Lambda function
                Collections.sort(filteredListRentals, (n1, n2) -> n1.getNameOfRenter().compareTo(n2.nameOfRenter));
                break;

            case RetendItems:
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().
                        filter(n -> n.getActualDateReturned() != null)
                        .collect(Collectors.toList());

                Collections.sort(filteredListRentals, new Comparator<Rental>() {
                    @Override
                    public int compare(Rental n1, Rental n2) {
                        return n1.getNameOfRenter().compareTo(n2.nameOfRenter);
                    }
                });

                break;

            case SortByGameConsole:
                filteredListRentals = (ArrayList<Rental>) listOfRentals.stream().
                        filter(n -> n.actualDateReturned == null)
                        .collect(Collectors.toList());

                // TODO: Your code goes here.

                break;

            default:
                throw new RuntimeException("upDate is in undefined state: " + display);
        }
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int col) {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals[col];
            case RetendItems:
                return columnNamesforRented[col];
            case SortByGameConsole:
                return columnNamesCurrentRentals[col];
        }
        throw new RuntimeException("Undefined state for Col Names: " + display);
    }

    @Override
    public int getColumnCount() {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals.length;
            case RetendItems:
                return columnNamesforRented.length;
            case SortByGameConsole:
                return columnNamesCurrentRentals.length;

        }
        throw new IllegalArgumentException();
    }

    @Override
    public int getRowCount() {
        return filteredListRentals.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
            case CurrentRentalStatus:
                return currentRentalScreen(row, col);
            case RetendItems:
                return rentedOutScreen(row, col);
            case SortByGameConsole:
                return currentRentalScreen(row, col);

        }
        throw new IllegalArgumentException();
    }

    private Object currentRentalScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (filteredListRentals.get(row).getCost(filteredListRentals.
                        get(row).dueBack));

            case 2:
                return (formatter.format(filteredListRentals.get(row).rentedOn.getTime()));

            case 3:
                if (filteredListRentals.get(row).dueBack == null)
                    return "-";

                return (formatter.format(filteredListRentals.get(row).dueBack.getTime()));

            case 4:
                if (filteredListRentals.get(row) instanceof Console)
                    return (((Console) filteredListRentals.get(row)).getConsoleType());
                else {
                    if (filteredListRentals.get(row) instanceof Game)
                        if (((Game) filteredListRentals.get(row)).getConsole() != null)
                            return ((Game) filteredListRentals.get(row)).getConsole().getConsoleType();
                        else
                            return "";
                }

            case 5:
                if (filteredListRentals.get(row) instanceof Game)
                    return (((Game) filteredListRentals.get(row)).getNameGame());
                else
                    return "";
            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    private Object rentedOutScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(filteredListRentals.get(row).rentedOn.
                        getTime()));
            case 2:
                return (formatter.format(filteredListRentals.get(row).
                        actualDateReturned.getTime()));

            case 3:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row).dueBack));

            case 4:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row).
                        actualDateReturned
                ));

            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    public void add(Rental a) {
        listOfRentals.add(a);
        updateScreen();
    }

    public Rental get(int i) {
        return filteredListRentals.get(i);
    }

    public void upDate(int index, Rental unit) {
        updateScreen();
    }

    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            System.out.println(listOfRentals.toString());
            os.writeObject(listOfRentals);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    public void loadDatabase(String filename) {
        listOfRentals.clear();

        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listOfRentals = (ArrayList<Rental>) is.readObject();
            updateScreen();
            is.close();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);

        }
    }

    public boolean saveAsText(String filename) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(filename)));
            out.println(listOfRentals.size());
            for (int i = 0; i < listOfRentals.size(); i++) {
                Rental unit = listOfRentals.get(i);
                out.println(unit.getClass().getName());

                // TODO: more code here

            }
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void loadFromText(String filename) {
        listOfRentals.clear();

        try {
            Scanner scanner = new Scanner(new File(filename));
            int count = Integer.parseInt(scanner.nextLine().trim());
            for (int i = 0; i < count; i++) {
                String type = scanner.nextLine().trim();

                // TODO: more code here

            }
            scanner.close();

        } catch (Exception ex) {
            throw new RuntimeException("Loading text file problem " + display);

        }
        updateScreen();
    }

    // used by instructor to test your code.  Please do not change
    public void createList() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();
        GregorianCalendar g7 = new GregorianCalendar();
        GregorianCalendar g8 = new GregorianCalendar();

        try {
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("7/20/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("9/29/2020");
            g6.setTime(d6);
            Date d7 = df.parse("7/25/2020");
            g7.setTime(d7);
            Date d8 = df.parse("7/29/2020");
            g8.setTime(d8);

            Console console1 = new Console("Person1", g4, g6, null, ConsoleTypes.PlayStation4);
            Console console2 = new Console("Person2", g5, g3, null, ConsoleTypes.PlayStation4);
            Console console3 = new Console("Person5", g4, g8, null, ConsoleTypes.SegaGenesisMini);
            Console console4 = new Console("Person6", g4, g7, null, ConsoleTypes.SegaGenesisMini);
            Console console5 = new Console("Person1", g5, g4, g3, ConsoleTypes.XBoxOneS);

            Game game1 = new Game("Person1", g3, g2, null, "title1",
                    new Console("Person1", g3, g2, null, ConsoleTypes.PlayStation4));
            Game game2 = new Game("Person1", g3, g1, null, "title2",
                    new Console("Person1", g3, g1, null, ConsoleTypes.PlayStation4));
            Game game3 = new Game("Person1", g5, g3, null, "title2",
                    new Console("Person1", g5, g3, null, ConsoleTypes.SegaGenesisMini));
            Game game4 = new Game("Person7", g4, g8, null, "title2", null);
            Game game5 = new Game("Person3", g3, g1, g1, "title2",
                    new Console("Person3", g3, g1, g1, ConsoleTypes.XBoxOneS));
            Game game6 = new Game("Person6", g4, g7, null, "title1",
                    new Console("Person6", g4, g7, null, ConsoleTypes.NintendoSwich));
            Game game7 = new Game("Person5", g4, g8, null, "title1",
                    new Console("Person5", g4, g8, null, ConsoleTypes.NintendoSwich));

            add(game1);
            add(game4);
            add(game5);
            add(game2);
            add(game3);
            add(game6);
            add(game7);

            add(console1);
            add(console2);
            add(console5);
            add(console3);
            add(console4);

        } catch (ParseException e) {
            throw new RuntimeException("Error in testing, creation of list");
        }
    }
}

