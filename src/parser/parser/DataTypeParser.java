package parser.parser;

public class DataTypeParser {
    public boolean isByte(int number) {
        return number >= Byte.MIN_VALUE && number <= Byte.MAX_VALUE;
    }
    public boolean isShort(int number) {
        return number >= Short.MIN_VALUE && number <= Short.MAX_VALUE;
    }
}
