package com.mygdx.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExpressionParser<T> extends BaseParser {
    private static final Map<String, Integer> PRIORITIES = new HashMap<String, Integer>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
        put("^", 3);
    }};
    private final int LAST_PRIORITY = 4;
    private final int START_PRIORITY = 1;
    private String last = "";
    private final ParserAlgebra<T> algebra;

    public ExpressionParser(final ParserAlgebra<T> algebra) {
        this.algebra = algebra;
    }

    public T parse(final String expression) {
        setSource(new StringSource(expression));
        final T res = parseOperation(START_PRIORITY);
        if (hasNext()) {
            throw new IllegalStateException(getPos() + ": " + ch);
        }
        return res;
    }

    private String testWordOperation() {
        skipWhitespaces();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < BUFFER_SIZE && (Character.isLetter(buffer[i]) || Character.isDigit(buffer[i]) || buffer[i] == '_')) {
            sb.append(buffer[i]);
            i++;
        }
        String s = sb.toString();
        if (PRIORITIES.containsKey(s)) {
            skipChars(i);
            return s;
        } else {
            return null;
        }
    }

    private String testOperation() {
        skipWhitespaces();
        if (Character.isLetter(ch)) {
            return testWordOperation();
        }
        for (int i = BUFFER_SIZE; i > 0; i--) {
            String s = String.valueOf(buffer, 0, i);
            if (PRIORITIES.containsKey(s)) {
                skipChars(i);
                return s;
            }
        }
        return null;
    }

    private void skipWhitespaces() {
        while (hasNext() && Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    private T parseOperation(int priority) {
        skipWhitespaces();
        if (priority == LAST_PRIORITY) {
            return parseUnary();
        }
        T result = parseOperation(priority + 1);
        skipWhitespaces();
        while (hasNext() || !last.isEmpty()) {
            String operator;
            if (last.isEmpty()) {
                operator = testOperation();
                skipWhitespaces();
            } else {
                operator = last;
                last = "";
            }
            if (operator == null) {
                break;
            }
            if (PRIORITIES.get(operator) != priority) {
                last = operator;
                break;
            }
            switch (operator) {
                case "+":
                    result = algebra.add(result, parseOperation(priority + 1));
                    break;
                case "-":
                    result = algebra.subtract(result, parseOperation(priority + 1));
                    break;
                case "*":
                    result = algebra.multiply(result, parseOperation(priority + 1));
                    break;
                case "/":
                    result = algebra.divide(result, parseOperation(priority + 1));
                    break;
                case "^":
                    result = algebra.powConst(result, parseOperation(priority + 1));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + operator);
            }
        }

        skipWhitespaces();
        return result;
    }

    private T parseUnary() {
        skipWhitespaces();
        String operator = testOperation();
        if (Objects.equals(operator, "-")) {
            skipWhitespaces();
            if (Character.isDigit(ch)) {
                return parseNumber(true);
            } else {
                return algebra.negate(parseOperation(LAST_PRIORITY));
            }
        } else if (operator != null) {
            throw new IllegalStateException(getPos() + ": " + "expected number or expression");
        }
        skipWhitespaces();
        if (test('(')) {
            T result = parseOperation(START_PRIORITY);
            expect(')');
            return result;
        }
        return parseSimpleElement();
    }

    private T parseSimpleElement() {
        skipWhitespaces();
        final StringBuilder sb = new StringBuilder();
        int count = 0;
        while (hasNext() && Character.isLetter(ch) || ch == '_' || count > 0 && Character.isDigit(ch)) {
            count++;
            sb.append(ch);
            nextChar();
        }
        if (count != 0) {
            return algebra.of(sb.toString());
        } else {
            return parseNumber(false);
        }
    }

    private T parseNumber(final boolean negative) {
        StringBuilder sb = new StringBuilder();
        if (negative) {
            sb.append('-');
        }
        skipWhitespaces();
        while (hasNext() && (Character.isDigit(ch) || ch == '.')) {
            sb.append(ch);
            nextChar();
        }
        if (sb.length() == 0) {
            if (hasNext() && ch != ')') {
                throw new IllegalStateException(getPos() + ": " + ch);
            } else {
                throw new IllegalStateException(getPos() + ": " + "expected number or expression");
            }
        }
        return algebra.of(Double.parseDouble(sb.toString()));
    }
}