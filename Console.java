package project2GIVE_TO_STUDENTS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Console extends Rental {

    /** Represents the type of console player, see enum type. */
    private ConsoleTypes consoleType;

    public Console() {
    }

    public Console(String nameOfRenter,
                   GregorianCalendar rentedOn,
                   GregorianCalendar dueBack,
                   GregorianCalendar actualDateReturned,
                   ConsoleTypes consoleType) {
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);
        this.consoleType = consoleType;
    }

    public ConsoleTypes getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(ConsoleTypes consoleType) {
        this.consoleType = consoleType;
    }

    @Override
    public double getCost(GregorianCalendar returnDate) {

//      // Do not use this approach.
//      String dateBeforeString = "2017-05-24";
//      String dateAfterString = "2017-07-29";
//      
//      // Parsing the date
//      LocalDate dateBefore = LocalDate.parse(dateBeforeString);
//      LocalDate dateAfter = LocalDate.parse(dateAfterString);
//      
//      // calculating number of days in between
//      long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
//      
//      // displaying the number of days
//      System.out.println(noOfDaysBetween);

        GregorianCalendar gTemp = new GregorianCalendar();
        double cost = 5;
        //        Date d = dueBack.getTime();
        //        gTemp.setTime(d);
        // the next two line are just hints
        gTemp = (GregorianCalendar) returnDate.clone(); //  gTemp = dueBack;  does not work!!
        gTemp.add(Calendar.DATE, -1);                // this subtracts one day from gTemp
        
        // these lines will help with debugging
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(formatter.format(gTemp.getTime()));
        System.out.println(formatter.format(rentedOn.getTime()));

        // TODO: more code here
        
        return cost;
    }

    @Override
    public String toString() {
        return "Console{" +
                " consoleType=" + consoleType + " " + super.toString() +
                '}';
    }
}


