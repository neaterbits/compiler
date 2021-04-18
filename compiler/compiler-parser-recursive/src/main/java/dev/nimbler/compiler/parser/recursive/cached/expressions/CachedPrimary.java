package dev.nimbler.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import org.jutils.io.strings.StringRef;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.util.Base;

final class CachedPrimary {

    private ParseTreeElement type;
    private int context;
    
    private long name;
    
    private long integerLiteralValue;
    private Base base;
    private boolean signed;
    private int bits;
    
    private long stringLiteralValue;
    
    private char characterLiteralValue;
    
    private boolean booleanLiteralValue;
    
    private long methodName;
    private int methodNameContext;
    private ParametersList parametersList;
    
    private ExpressionCacheList subList;

    private void init(ParseTreeElement type, int context) {
        
        Objects.requireNonNull(type);
        
        this.type = type;
        this.context = context;
    }

    void initName(int context, long name) {

        init(ParseTreeElement.NAME, context);

        this.name = name;
    }
    
    void initIntegerLiteral(int context, long value, Base base, boolean signed, int bits) {
        
        init(ParseTreeElement.INTEGER_LITERAL, context);
        
        this.integerLiteralValue = value;
        this.base = base;
        this.signed = signed;
        this.bits = bits;
    }

    void initStringLiteral(int context, long value) {
     
        if (value == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }
        
        init(ParseTreeElement.STRING_LITERAL, context);
        
        this.stringLiteralValue = value;
    }

    void initCharacterLiteral(int context, char value) {
        
        init(ParseTreeElement.CHARACTER_LITERAL, context);
        
        this.characterLiteralValue = value;
    }

    void initBooleanLiteral(int context, boolean value) {
        
        init(ParseTreeElement.BOOLEAN_LITERAL, context);
        
        this.booleanLiteralValue = value;
    }
    
    void initMethodInvocation(int context, long methodName, int methodNameContext, ParametersList parametersList) {
        
        init(ParseTreeElement.UNRESOLVED_METHOD_INVOCATION_EXPRESSION, context);
        
        this.methodName = methodName;
        this.parametersList = parametersList;
    }

    void addParameter(ExpressionCacheList parameter) {
        parametersList.addParameter(parameter);
    }

    void initSubList(int context, ExpressionCacheList subList) {

        Objects.requireNonNull(subList);

        init(ParseTreeElement.PRIMARY_LIST, context);
        
        this.subList = subList;
    }

    public ParseTreeElement getType() {
        return type;
    }

    public int getContext() {
        return context;
    }

    public long getName() {
        return name;
    }

    public long getIntegerLiteralValue() {
        return integerLiteralValue;
    }

    public Base getBase() {
        return base;
    }

    public boolean isSigned() {
        return signed;
    }

    public int getBits() {
        return bits;
    }

    public long getStringLiteralValue() {
        return stringLiteralValue;
    }

    public char getCharacterLiteralValue() {
        return characterLiteralValue;
    }

    public boolean getBooleanLiteralValue() {
        return booleanLiteralValue;
    }

    public long getMethodName() {
        return methodName;
    }
    
    public int getMethodNameContext() {
        return methodNameContext;
    }

    public ParametersList getParametersList() {
        return parametersList;
    }

    public ExpressionCacheList getSubList() {
        return subList;
    }

    @Override
    public String toString() {
        return "CachedPrimary [type=" + type + ", integerLiteralValue=" + integerLiteralValue + ", name=" + name + "]";
    }
}

