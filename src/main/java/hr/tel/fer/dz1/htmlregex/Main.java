package hr.tel.fer.dz1.htmlregex;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static private String all = ".*";
    static private String tagRegex = "<.+>";
    static private String extendedTagRegex = "[^>]*>"; // priznaje bilo koji znak osim ^, 0 do n puta
    static private String emailRegex = "[a-z0-9_.]+@[a-z\\-]\\.[a-z]+";
    static private String ipv4Regex = " (([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])($| )";
    private static String dateRegex = "[0-9][0-9]?[/][0-9][0-9]?[/][0-9][0-9][0-9][0-9]";
    private static String telRegex = "[0-9]?[0-9]?[0-9]?[ \\-][0-9][0-9][ \\-][0-9][0-9][0-9][ \\-][0-9][0-9][0-9][0-9]";
    static private Map<Integer, String> regexMap = new HashMap<>();
    static private Map<Integer, String> descriptionMap = new HashMap<>();
    static private Map<Integer, String> commandMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the file path: ");
        String pathString = null;
        if (scanner.hasNext()) {
            pathString = scanner.nextLine();
        }

        Path path = Paths.get(pathString);
        File HTMLfile = path.toFile();
        if (!HTMLfile.exists()) {
            System.err.println("The given path does not exist.");
            System.exit(0);
        }

        fillMaps();

        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine();
            String[] list = command.split(" ");
            if (list[0].toUpperCase().equals("EXIT")) {
                break;

            } else if (list[0].toUpperCase().equals("ALL")) {
                if (list.length == 2) {
                    Pattern patternTag = Pattern.compile(tagRegex);
                    Matcher matcherTag = patternTag.matcher(list[1]);
                    if (matcherTag.find()) {
                        printTags(HTMLfile, list[1], Integer.MAX_VALUE);
                    } else if (list[1].toLowerCase().equals("email")) {
                        print(HTMLfile, Integer.MAX_VALUE, emailRegex);
                    } else if (list[1].toUpperCase().equals("IP")) {
                        print(HTMLfile, Integer.MAX_VALUE, ipv4Regex);
                    } else {
                        System.out.println("You didn't type correct command! Try to type HELP.");
                    }
                } else {
                    print(HTMLfile, Integer.MAX_VALUE, all);
                }

            } else if (list[0].toUpperCase().equals("HELP")) {
                printHelpTable();

            } else if (list[0].toUpperCase().equals("ONLY")) {
                if (list.length == 3) {
                    int number = Integer.valueOf(list[2]);
                    Pattern patternTag = Pattern.compile(tagRegex);
                    Matcher matcherTag = patternTag.matcher(list[1]);
                    if (matcherTag.find()) {
                        printTags(HTMLfile, list[1], number);
                    } else if (list[1].toLowerCase().equals("email")) {
                        print(HTMLfile, number, emailRegex);
                    } else if (list[1].toUpperCase().equals("IP")) {
                        print(HTMLfile, number, ipv4Regex);
                    } else {
                        System.out.println("You didn't type correct command! Try to type HELP.");
                    }
                } else {
                    System.out.println("You didn't type correct command! Try to type HELP.");
                }
            } else if (list[0].toUpperCase().equals("REGEX")) {
                if (list.length == 3) {
                    Integer number = Integer.valueOf(list[2]);
                    printRegexValues(number);
                } else {
                    System.out.println("You didn't type correct command! Try to type HELP.");
                }
            } else {
                System.out.println("You didn't type correct command! Try to type HELP.");
            }
        }
        scanner.close();
        return;
    }

    private static void printTags(File file, String tag, int number) {

        String endTag = "</" + tag.substring(1);
        tag = tag.substring(0, tag.length() - 1) + extendedTagRegex;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern patternStartTag = Pattern.compile(tag);
            Pattern patternEndTag = Pattern.compile(endTag);
            boolean start = false;

            while ((line = br.readLine()) != null && number != 0) {
                Matcher matcherStartTag = patternStartTag.matcher(line);
                Matcher matcherEndTag = patternEndTag.matcher(line);

                if (matcherStartTag.find()) {
                    start = true;
                    line = line.substring(matcherStartTag.end());
                    matcherEndTag = patternEndTag.matcher(line);
                    if (matcherEndTag.find()) {
                        line = line.substring(0, matcherEndTag.start());
                        start = false;
                        number--;
                    }
                    System.out.println(line);

                } else if (start == true) {
                    if (matcherEndTag.find()) {
                        line = line.substring(0, matcherEndTag.start());
                        start = false;
                        number--;
                    }
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void print(File file, int number, String regex) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern patternEmail = Pattern.compile(regex);

            while ((line = br.readLine()) != null && number != 0) {
                Matcher matcherEmail = patternEmail.matcher(line);
                if (matcherEmail.find()) {
                    System.out.println(matcherEmail.group());
                    number--;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printHelpTable() {
        System.out.println(" ID :      COMMAND       -----> DESCRIPTION ");
        for (Integer key : descriptionMap.keySet()) {
            System.out.println(key + " : " + commandMap.get(key) + " -----> " + descriptionMap.get(key));
        }
    }

    private static void printRegexValues(Integer key) {
        if (regexMap.containsKey(key)) {
            System.out.println(key + " : " + regexMap.get(key));
        }
    }

    private static void fillMaps() {
        regexMap.put(1, all);
        regexMap.put(2, extendedTagRegex);
        regexMap.put(3, emailRegex);
        regexMap.put(4, ipv4Regex);
        regexMap.put(5, dateRegex);
        regexMap.put(6, telRegex);
        regexMap.put(7, extendedTagRegex);
        regexMap.put(8, emailRegex);
        regexMap.put(9, ipv4Regex);
        regexMap.put(10, dateRegex);
        regexMap.put(11, telRegex);

        descriptionMap.put(1, "Prints out the whole HTML file");
        descriptionMap.put(2, "Prints out content between all wanted tag instances.");
        descriptionMap.put(3, "Prints out all e-mails.");
        descriptionMap.put(4, "Prints out all IP adresses.");
        descriptionMap.put(5, "Prints out all dates.");
        descriptionMap.put(6, "Prints out all telephone numbers.");
        descriptionMap.put(7, "Prints out content between first 'number' wanted tags.");
        descriptionMap.put(8, "Prints out first 'number' e-mails.");
        descriptionMap.put(9, "Prints out first 'number' IP adresses.");
        descriptionMap.put(10, "Prints out first 'number' dates.");
        descriptionMap.put(11, "Prints out first 'number' telephone numbers.");
        descriptionMap.put(12, "Prints out help.");
        descriptionMap.put(13, "Prints out the regular expression for the selected command ID.");
        descriptionMap.put(14, "Exit application.");

        commandMap.put(1, "ALL");
        commandMap.put(2, "ALL <tag>");
        commandMap.put(3, "ALL email");
        commandMap.put(4, "ALL IP");
        commandMap.put(5, "ALL date");
        commandMap.put(6, "ALL tel");
        commandMap.put(7, "ONLY <tag> number");
        commandMap.put(8, "ONLY email number");
        commandMap.put(9, "ONLY IP number");
        commandMap.put(10, "ONLY date number");
        commandMap.put(11, "ONLY tel number");
        commandMap.put(12, "HELP");
        commandMap.put(13, "REGEX ID number");
        commandMap.put(14, "EXIT");
    }
}