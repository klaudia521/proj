import javax.sound.midi.SysexMessage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class main {

    public static HashMap<String,String> myMap;
    public static HashMap<String, String[]> myData;
    public static HashMap<HashMap<String,String>, Float> mySim;

    public static void csvReader(){
        String csvFile = "/Users/Polska/IdeaProjects/untitled1/edit2.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int count = 0;
        myMap = new HashMap<>();
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] myString = line.split(csvSplitBy);
                count++;
                //System.out.println("INT IS: " + count +"Movie title and year = " + myString[0] + " @@@ keywords=" + myString[1] );
                //System.out.println("Count is: " + count);
                myMap.put(myString[0], myString[1]);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void access(){
        myData = new HashMap<>();
        for(Map.Entry<String,String> entry : myMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            //System.out.println("this is the key " + key + " this is the value   " + value);
            String[] stringKeywords = value.split(":");
            for (String temp : stringKeywords){
                //System.out.println(temp);
            }
            myData.put(key, stringKeywords);

        }
    }
    public static int findMatchCount(final String [] a,final String [] b){
        int matchCount = 0;

        for(int i = 0, j = 0;i < a.length && j < b.length;){
            int res = a[i].compareTo(b[j]);
            if(res == 0){
                matchCount++;
                i++;
                j++;
            }else if(res < 0){
                i++;
            }else{
                j++;
            }
        }
        return matchCount;
    }

    private static void calculate() throws FileNotFoundException{
        //PrintWriter writer = new PrintWriter(new File("mySimilarities.csv"));
       // StringBuilder builder = new StringBuilder();
        PrintStream out = new PrintStream(new FileOutputStream("OutFileNew2.0.txt"));
        mySim = new HashMap<>();
        int num = 0;
        int numTwo = 0;
        for(Map.Entry<String, String[]> entry : myData.entrySet()){
            //System.out.println("LOOP ONE");
            String key = entry.getKey();
            String[] value = entry.getValue();
           num++;
           numTwo = 0;
           //System.out.println("count: "  + num);
            //System.out.println("this is the key " + key + " this is the value   " + value[0]);
            outerloop:
            for(Map.Entry<String, String[]> entryTwo : myData.entrySet()){
                //System.out.println("LOOP TWO");
                String keyTwo = entryTwo.getKey();
                String[] valueTwo = entryTwo.getValue();
                //System.out.println("this is the key " + keyTwo + " this is the value   " + valueTwo[0]);
                numTwo++;
               /* if (numTwo > 4543) {
                    System.out.println("Breaking");
                    numTwo = 0;
                    break outerloop;
                }*/
               // System.out.println("count TWO: " +numTwo + " and count ONE " + num);
                if(key.equals(keyTwo)) {
                   // System.err.println("CRYYYY " );
                } else {
                    int lenOne = value.length;
                    int lenTwo = valueTwo.length;
                    int myInt = findMatchCount(value, valueTwo);
                    int sum = lenOne + lenTwo - myInt;
                    float sim;
                    //System.out.println("lenOne: " + lenOne + " lenTwo: " + lenTwo + " This count: " + myInt);
                    if(myInt < 2){
                        sim = 0;
                    } else {
                        sim = (float)myInt/sum;
                        float simTen = sim*4;
                        if(simTen >= 1){
                            System.err.println("number is too high at " + "count TWO: " +numTwo + " and count ONE " + num);
                            System.err.println("sim is: " + sim + " for " + key + " and " + keyTwo + " numbers are " + lenOne + " " + lenTwo + " " + myInt + " " + sum);
                        }
                        //System.out.println("Sum: " + sum + " myInt " + myInt + " sim: " + sim);
                       /* builder.append(key);
                        builder.append(',');
                        builder.append(keyTwo);
                        builder.append(',');
                        builder.append(String.valueOf(sim));
                        builder.append('\n');
                        writer.write(builder.toString()); */
                        out.println(key + "," +keyTwo + ","+ sim);
                    }



                }
                //writer.write(builder.toString());
            }
        }
        //writer.write(builder.toString());
        //writer.close();
        System.out.println("Output saved");
        out.close();
    }

    public static void main(String[] args) {
       csvReader();
       access();
       try {
           calculate();
       }  catch(FileNotFoundException ex) {
           ex.printStackTrace();
           System.exit(1);
       }
        System.out.println("CRYYYY 2.0" );
    }
}