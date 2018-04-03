import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    private static List<String> htmlDocument = null;
    private static Map<Integer, String> allRegexCommands = null;

    private static String emailRegex = "[a-z0-9_.]+@[a-z\\-]\\.[a-z]+";
    private static String ipv4Regex = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}";
    private static String dateRegex = "[0-9][0-9]?[/][0-9][0-9]?[/][0-9][0-9][0-9][0-9]";
    private static String telRegex = "[0-9]?[0-9]?[0-9]?[ \\-][0-9][0-9][ \\-][0-9][0-9][0-9][ \\-][0-9][0-9][0-9][0-9]";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the file path: ");
            String path = null;
            if (scanner.hasNext()) {
                path = scanner.nextLine();
            }
            Path filePath = Paths.get(path);

            try {
                htmlDocument = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                break;
            } catch (IOException exc) {
                System.out.println("IOException occurred during the reading of the file at " + path);
                continue;
            }

        }

        fillMap();

        System.out.println("Enter a command: ");
        while (scanner.hasNext()) { //TODO provjeriti da li je unos ispravan
            String command = scanner.nextLine();

            if (command.contains("<") && command.contains(">")) {
                getTag(command);
            } else if (command.contains("email")) {
                getEmail(command);
            } else if (command.contains("IP")) {
                getIP(command);
            } else if (command.contains("date")) {
                getDate(command);
            } else if (command.contains("tel")) {
                getTel(command);
            } else if (command.equals("ALL")) {
                for (String line : htmlDocument) {
                    System.out.println(line);
                }
            } else if (command.equals("HELP")) {
                System.out.println("Popis opcija:");
                for (Map.Entry<Integer, String> entry : allRegexCommands.entrySet()) {
                    System.out.printf("%d\t%s\n", entry.getKey(), entry.getValue());
                }
            } else if (command.equals("REGEX ID")) {
                //TODO pronaci upisani integer
                // int id = command.get(command.indexOf("D")+2);
                //getRegex(id);
            } else if (command.equals("EXIT")) {
                break;
            }

            System.out.println("Enter a command: ");
        }
    }

    private static void fillMap() {
        allRegexCommands = new HashMap<>();
        allRegexCommands.put(1, "ALL");
        allRegexCommands.put(2, "ALL <tag>");
        allRegexCommands.put(3, "ALL email");
        allRegexCommands.put(4, "ALL IP");
        allRegexCommands.put(5, "ALL date");
        allRegexCommands.put(6, "ALL tel");
        allRegexCommands.put(7, "ONLY <tag> broj");
        allRegexCommands.put(8, "ONLY email broj");
        allRegexCommands.put(9, "ONLY IP broj");
        allRegexCommands.put(10, "ONLY date broj");
        allRegexCommands.put(11, "ONLY tel broj");
        allRegexCommands.put(12, "HELP");
        allRegexCommands.put(13, "REGEX ID broj");
        allRegexCommands.put(14, "EXIT");
    }

    private static void getTag(String command) {
        String regexAll = "^ALL <[a-z]+>$"; //regex koji se koristi za nalazenje tag-a
        String regexOnly = "^ONLY <[a-z]+> [0-9]+$";
        Integer numberOfOccurrances = null;

        Pattern pattern = Pattern.compile(regexAll);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {   // for ALL command
            numberOfOccurrances = Integer.MAX_VALUE;
        } else {  // for ONLY command
            try {
                numberOfOccurrances = Integer.parseInt(command.substring(command.indexOf(">") + 1 + 1)); // the first +1 is for the space and the second is for the (first) number
                pattern = Pattern.compile(regexOnly);
                matcher = pattern.matcher(command); //TODO da li ovdje trebam napraviti matcher.find(), prije nego se izvede matcher.group() koji dolazi nakon?
            } catch (NumberFormatException exc) {
                System.err.println("Couldn't parse the number from the command!" + exc);
            }

        }

        String matchedString = matcher.group(); //pohrana pronadenog tag-a u varijablu
        String tagType = matchedString.substring(matchedString.indexOf("<") + 1, matchedString.indexOf(">")); //TODO provjeriti da li dobro nalazi tag

        String tagRegex = "<\\/?" + tagType + ".*>"; //regex koji ce se koristiti za trazenje tag-a u HTML dokumentu
        pattern = Pattern.compile(tagRegex);
        StringBuilder sb = new StringBuilder();
        boolean zastavicaZaDodavanjeSljedecegRedaUIspis = false;
        int brojacZaBrojPronadenihTagova = 0;

        for (String line : htmlDocument) { //trazimo liniju po liniju HTML dokumenta
            matcher = pattern.matcher(line);
            if (!matcher.find() && zastavicaZaDodavanjeSljedecegRedaUIspis) { //nismo nasli kraj tag-a i dodajemo liniju u StringBuilder
                sb.append(line);
                //namjerno ne resetiramo zastavicu jer je moguce da ce i sljedeci red biti ukljucen u ispis (ako nije oznaka kraja tag-a)
            }
            if (matcher.find() && zastavicaZaDodavanjeSljedecegRedaUIspis) { //nasli smo kraj tag-a, ispisujemo StringBuilder i resetiramo njega i zastavicu
                System.out.println(sb.toString());
                if (++brojacZaBrojPronadenihTagova == numberOfOccurrances) { //ispisali smo trazeni broj tag-ova, izlazimo iz metode
                    break;
                }
                sb = new StringBuilder();
                zastavicaZaDodavanjeSljedecegRedaUIspis = false;
            }
                System.out.println(matcher.find());

            if (matcher.find() && !zastavicaZaDodavanjeSljedecegRedaUIspis) { //ako smo nasli tag u trenutnoj liniji
                int endingOfExpStart = matcher.end(); //spremamo index kraja tog tag-a
                if (matcher.find(endingOfExpStart)) { //trazimo da li postoji oznaka kraja tag-a u toj liniji
                    int beginningOfExpEnd = matcher.start(); //ako postoji oznaka kraja tag-a, onda ispisujemo samo ono sto je izmedu njih
                    System.out.println(line.substring(endingOfExpStart, beginningOfExpEnd - 1));
                } else {  //ako nismo nasli oznaku kraja tag-a u toj liniji, trazimo ju u narednim retcima i pripremamo se za dodavanje linija u ispis
                    zastavicaZaDodavanjeSljedecegRedaUIspis = true;
                }
            }
        }
    }

    private static void getEmail(String command) {


    }

    private static void getIP(String command) {


    }

    private static void getDate(String command) {


    }

    private static void getTel(String command) {

    }

    private static void getRegex(int id) {

    }
}
