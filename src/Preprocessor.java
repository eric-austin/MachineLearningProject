import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Preprocessor {
	//instance variables
	private ArrayList<String> missingStrings;
	
	
	public Preprocessor() {
		//create list of strings that indicate field missing
		this.missingStrings = new ArrayList<String>();
		this.missingStrings.add("UU");
		this.missingStrings.add("XX");
		this.missingStrings.add("X");
		this.missingStrings.add("U");
		this.missingStrings.add("QQ");
		this.missingStrings.add("Q");
		this.missingStrings.add("UUUU");
		this.missingStrings.add("XXXX");
	}
	
	public void removeIncompleteExamples(String filepath) {
		//variable to hold lines being read/written
		String line = null;
		String output= null;
		try {
			//set up file reader and writer
			FileReader fileReader = new FileReader(filepath);
			BufferedReader buffReader = new BufferedReader(fileReader);
			FileWriter fileWriter = new FileWriter("clean_" + filepath);
			BufferedWriter buffWriter = new BufferedWriter(fileWriter);
			//copy feature labels less the case number
			line = buffReader.readLine();
			String[] values = line.split(",");
			String[] truncatedValues = Arrays.copyOf(values, values.length - 1);
			output = String.join(",", truncatedValues);
			buffWriter.write(output);
			buffWriter.newLine();
			//iterate through lines in input file
			while ((line = buffReader.readLine()) != null) {
				//split line on comma
				values = line.split(",");
				//check whether any of the fields match a string indicating missing value
				boolean valid = true;
				for (int j = 0; j < values.length; j++) {
					if (this.missingStrings.contains(values[j])) {
						valid = false;
						break;
					}
				}
				//if example valid ie. no missing fields, write to output
				if (valid) {
					truncatedValues = Arrays.copyOf(values, values.length - 1);
					output = String.join(",", truncatedValues);
					buffWriter.write(output);
					buffWriter.newLine();
				}
			}
			//close streams
			buffReader.close();
			buffWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createRandomPartition(String filepath) {
		//variable to hold lines being read/written
		String line = null;
		Random rng = new Random();
		try {
			//create file reader and writers
			FileReader fileReader = new FileReader(filepath);
			BufferedReader buffReader = new BufferedReader(fileReader);
			BufferedWriter[] buffWriters = new BufferedWriter[10];
			for (int i = 0; i < 10; i++) {
				buffWriters[i] = new BufferedWriter(new FileWriter("partition" + i + ".csv"));
			}
			//copy labels to each partition
			line = buffReader.readLine();
			for (BufferedWriter b : buffWriters) {
				b.write(line);
				b.newLine();
			}
			//iterate through all the lines of input file
			while ((line = buffReader.readLine()) != null) {
				int i = rng.nextInt(10);
				buffWriters[i].write(line);
				buffWriters[i].newLine();
			}
			//close streams
			buffReader.close();
			for (BufferedWriter b : buffWriters) {
				b.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
