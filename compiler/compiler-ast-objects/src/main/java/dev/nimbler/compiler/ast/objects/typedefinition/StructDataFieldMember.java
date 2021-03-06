package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.FieldNameDeclaration;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class StructDataFieldMember extends DataFieldMember {
	
    private ASTList<FieldNameDeclaration> names;
    
	public StructDataFieldMember(Context context, TypeReference type, List<FieldNameDeclaration> names) {
		super(context, type);
		
		this.names = makeList(names);
	}

	public ASTList<FieldNameDeclaration> getNames() {
        return names;
    }

    public void setNames(ASTList<FieldNameDeclaration> names) {
        this.names = names;
    }

    @Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.FIELD;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STRUCT_DATA_FIELD_MEMBER;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onStructDataFieldMember(this, param);
	}

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        super.doRecurse(recurseMode, iterator);

        doIterate(names, recurseMode, iterator);
    }
}
