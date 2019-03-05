package com.mcc.pipesAndFiltersFolders;

import com.mcc.*;

import java.io.FileInputStream;
import java.io.IOException;

public class PipesAndFiltersFoldersApplication {

  private final static String PATH_DIRECTORY = "/home/dessis-aux23/Documentos/maestria/ARSO";
  private static final String path = "./src/main/java/com/mcc/pipesAndFiltersFolders/input.txt";

  public void execute(Filter.onFinishListener finishListener) {

    try {
      Pipe inToKw = new Pipe();
      Pipe kwToF = new Pipe();
      Pipe alphabetizerToOutput = new Pipe();

      FileInputStream in = new FileInputStream(path);

      Input input = new Input(in, inToKw);
      FindFile findFile = new FindFile(inToKw, kwToF, PATH_DIRECTORY);
      Alphabetizer alpha = new Alphabetizer(kwToF, alphabetizerToOutput);
      Output output = new Output(alphabetizerToOutput, finishListener);

      input.start();
      findFile.start();
      alpha.start();
      output.start();
    } catch (IOException exc) {
      exc.printStackTrace();
    }
  }

  public static void main(String[] args) {
    PipesAndFiltersFoldersApplication app = new PipesAndFiltersFoldersApplication();
    app.execute(System.out::println);
  }

}
