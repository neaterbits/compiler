package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.model.ParseTreeElement;

final class CachedPrimary {

    private ParseTreeElement type;
    private int context;
    
    private long name;
    
    private long integerLiteralValue;
    private Base base;
    private boolean signed;
    private int bits;
    
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

    void initSubList(int context, ExpressionCacheList subList) {
        
        init(ParseTreeElement.PRIMARY_LIST, context);
        
        Objects.requireNonNull(subList);
        
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

    public ExpressionCacheList getSubList() {
        return subList;
    }

    @Override
    public String toString() {
        return "CachedPrimary [type=" + type + ", integerLiteralValue=" + integerLiteralValue + "]";
    }
}

