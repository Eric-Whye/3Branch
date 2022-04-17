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
    public static void appendLineByLine(List<List<String>> list, char delim, char newLineDelim, String filename){
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
    public static void writeLineByLine(List<List<String>> list, char delim, char newLineDelim, String filename){
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
     * Writes a whole list of titled blocks of data. The first String in each inner list is the title
     * @param list of lists where each inner list is a tokenized string
     * @param delim between each token
     * @param newLineDelim between each line entry
     * @param filename filename
     */
    public static void writeDataBlocks(List<List<String>> list, char delim, char newLineDelim, String filename){
        Writer writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(filename));

            for (List<String> line : list) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < line.size(); i++){
                    if (i == 0){
                        str.append(line.get(0));
                        str.append(newLineDelim);
                    } else{
                        str.append(line.get(i));
                        str.append(delim);
                    }
                }
                str.deleteCharAt(str.length()-1);
                str.append(newLineDelim);
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
    public static void streamLineByLine(String filename, Callable callable){
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
