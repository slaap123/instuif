package nl.phanos.trainignspakkenwedstrijd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.phanos.trainignspakkenwedstrijd.Main.parComparator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;









public class Main
{
  private ArrayList<String[]> table;
  
  public static void main(String[] args)
  {
    new Main();
  }
  
  public Main()
  {
    readTable();
    writeTable();
  }
  
  public void readTable() {
    table = new ArrayList();
    try {
      Document doc = Jsoup.connect("https://docs.google.com/spreadsheets/d/e/2PACX-1vSJW25z0CAb2FZLZFtDG4jhu-tblNJ6_Cykitl0vKD5Ay5UmL_aL0R4Wo5VPFF-wxxtzMWTsa8W8ebo/pubhtml?gid=2095698778single=true&headers=false&chrome=false").get();
      Elements rows = doc.select(".grid-container tbody tr");
      int rowI = 0;
      for (Element row : rows) {
        int colI = 0;
        String[] tr = new String[4];
        for (Element cell : row.select("td")) {
          tr[(colI++)] = cell.text();
          tr[(colI++)] = "";
        }
        if (!"()".equals(tr[2].trim())) {
          table.add(tr);
        }
        rowI++;
      }
    } catch (IOException ex) { int rowI;
      System.out.println("error");
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void writeTable() {
      table.sort(new parComparator());
    BufferedWriter writer = null;
    try {
      for (int i = 0; i < Math.ceil(table.size() / 6.0D); i++) {
        String str = "#wedstrijd";
        for (String[] row : table)
          str = str + "\r\n" + String.join("\t", row);
        writer = new BufferedWriter(new FileWriter(String.format("%03d", new Object[] { Integer.valueOf(i + 1) }) + ".par"));
        writer.write(str);
        writer.close();
      }
      return;
    } catch (IOException ex) { Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        writer.close();
      } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  static class parComparator implements Comparator<String[]>
 {
     public int compare(String[] line1,String[] line2)
     {
         return line1[2].compareTo(line2[2]);
     }
 }
}