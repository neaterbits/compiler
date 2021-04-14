package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class EnumDefinition extends BaseClassDefinition {

	private final ASTList<EnumConstantDefinition> constants;
	
	public EnumDefinition(
			Context context,
			ClassModifiers modifiers,
			Keyword enumKeyword,
			ClassDeclarationName name,
			List<TypeReference> implementsInterfaces,
			List<EnumConstantDefinition> constants,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, enumKeyword, name, implementsInterfaces, members);
	
		Objects.requireNonNull(constants);
		
		this.constants = makeList(constants);
	}

	public ASTList<EnumConstantDefinition> getConstants() {
		return constants;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ENUM_DEFINITION;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onEnumDefinition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterateModifiersAndName(recurseMode, iterator);
		doIterateImplementsInterfaces(recurseMode, iterator);
		
		doIterate(constants, recurseMode, iterator);

		super.doRecurse(recurseMode, iterator);
	}
}
