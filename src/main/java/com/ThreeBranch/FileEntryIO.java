package com.ThreeBranch;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public abstract class FileEntryIO {

    /**
     * Appends line entries to filename where each entry is a tokenized string in the form of a List.
     * @param list of lists where each inner list is a tokenized string
     * @param delim between each token
     * @param newLineDelim between each line entry
     * @param filename filename
     */
    public static void appendToFile(List<List<String>> list, char delim, char newLineDelim, String filename){
        Writer writer = null;

        try{
            writer = new BufferedWriter(new FileWriter(filename, true));

            for (List<String> line : list) {
                StringBuilder str = new StringBuilder();
                for (String token : line)
                    str.append(token).append(delim);

                str.deleteCharAt(str.length()-1);
                str.append(newLineDelim);

                writer.write(str.toString());
            }

        }catch(IOException e){e.printStackTrace();}
        finally{
            try{
                assert writer != null;
                writer.close();
            }catch(IOException e){e.printStackTrace();}
        }
    }

    /**
     * Writes line entries to filename where each entry is a tokenized string in the form of a List.
     * @param list of lists where each inner list is a tokenized string
     * @param delim between each token
     * @param newLineDelim between each line entry
     * @param filename filename
     */
    public static void writeToFile(List<List<String>> list, char delim, char newLineDelim, String filename){
        Writer writer = null;

        try{
            writer = new BufferedWriter(new FileWriter(filename));

            for (List<String> line : list) {
                StringBuilder str = new StringBuilder();
                for (String token : line)
                    str.append(token).append(delim);

                str.deleteCharAt(str.length()-1);
                str.append(newLineDelim);

                writer.write(str.toString());
            }

        }catch(IOException e){e.printStackTrace();}
        finally{
            try{
                assert writer != null;
                writer.close();
            }catch(IOException e){e.printStackTrace();}
        }
    }

    /**
     * Stream files from filename, and applies a Callable "function" call to each line
     * @param callable is called for each line
     * @param filename filename
     */
    public static void streamFromFile(String filename, Callable callable){
        FileInputStream inputStream = null;
        Scanner sc = null;
        try{
            inputStream = new FileInputStream(filename);
            sc = new Scanner(inputStream);
            while (sc.hasNextLine())
                callable.call(sc.nextLine());

        }catch(IOException e){e.printStackTrace();
        } finally{
            try{
                assert inputStream != null;
                inputStream.close();
                assert sc != null;
                sc.close();
            }catch(IOException e){e.printStackTrace();}
        }


    }
}
