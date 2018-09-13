package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.condition.ConditionVisitor;

public abstract class ConditionConverter implements ConditionVisitor<ConverterState, Condition> {

}
