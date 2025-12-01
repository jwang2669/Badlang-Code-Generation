package edu.wisc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// Main entry point for the badlang code generator.
// This program generates machine code (MIPS or x86 assembly) from badlang programs.
// Usage: java edu.wisc.Main <source-file> [output-file]
// If output-file is not specified, outputs to stdout.
public class Main {
  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java edu.wisc.Main <source-file> [output-file]");
      System.exit(1);
    }
    try {
      String inputFile = args[0], outputFile = args.length > 1 ? args[1] : null;
      String source = new String(Files.readAllBytes(Paths.get(inputFile)));
      Lexer tokenizer = new Lexer(source);
      List<Token> tokens = tokenizer.scanTokens();
      Parser parser = new Parser(tokens);
      List<Stmt> ast = parser.parseProgram();
      Checker checker = new Checker();
      Generator generator = new Generator();
      String assemblyCode = generator.generate(ast);

      System.out.println("Original File:\n" + source);
      System.out.println("\nError:\n");
      checker.check(ast);
      for (Stmt stmt : ast) if (stmt instanceof Stmt.Block) System.out.println("blockblockblock");

      if (outputFile != null) {
        Files.writeString(Paths.get(outputFile), assemblyCode);
        System.out.println("Code generated successfully: " + outputFile);
      } else System.out.println(assemblyCode);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}