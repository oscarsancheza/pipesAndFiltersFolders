package com.mcc.pipesAndFiltersFolders;

import com.mcc.Filter;
import com.mcc.Pipe;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindFile extends Filter {

  private String path;

  public FindFile(Pipe in, Pipe out, String path) {
    super(in, out);
    this.path = path;
  }

  public static List<String> findFile(String name, File file) {
    List<String> filesNames = new ArrayList<>();
    File[] list = file.listFiles();
    if (list != null)
      for (File fil : list) {
        if (fil.isDirectory()) {
          filesNames.addAll(findFile(name, fil));
        } else if (fil.getName().toLowerCase().contains(name.toLowerCase())) {
          filesNames.add(fil.getName() + " --Path:" + fil.getParentFile());
        }
      }

    return filesNames;
  }

  @Override
  protected void transform() {
    List<String> lines = new ArrayList<>();
    CharArrayWriter writer = new CharArrayWriter();
    try {
      int c = input.read();
      while (c != -1) {
        writer.write(c);
        if (((char) c) == '\n') {
          String line = writer.toString();
          lines.add(line.replaceAll("\n", ""));
          writer.reset();
        }
        c = input.read();
      }

      if (!lines.isEmpty()) {


        List<String> filesNames = new ArrayList<>();
        File file = new File(path);
        for (String item : lines) {
          filesNames.addAll(findFile(item, file));
        }


        if (!filesNames.isEmpty()) {
          for (String word : filesNames) {
            String shift = word + '\n';

            char[] chars = shift.toCharArray();
            for (char aChar : chars) {
              output.write(aChar);
            }
          }
          output.closeWriter();
        }
      }
    } catch (IOException e) {
      System.err.println("KWIC Error: Ha ocurrido un error al leer los datos.");
    }
  }
}
