package rbtips.io;

import java.util.Scanner;

public class CommandlineIO implements IO {

    private final Scanner reader;

    public CommandlineIO(Scanner reader) {
        this.reader = reader;
    }

    @Override
    public String nextCommand() {
        return reader.nextLine();
    }

}
