package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.InterfaceMethodMemberSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackInterface<
		COMPLEX_MEMBER_DEFINITION,
		ANNOTATION,
		INTERFACE_MODIFIER_HOLDER,
		TYPE_REFERENCE,
		INTERFACE_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION>

	extends ListStackEntry<COMPLEX_MEMBER_DEFINITION>
	implements InterfaceMethodMemberSetter<INTERFACE_METHOD_MEMBER> {

	private final String interfaceKeyword;
	private final Context interfaceKeywordContext;
	private final String name;
	private final Context nameContext;
	private final List<ANNOTATION> annotations;
	private final List<INTERFACE_MODIFIER_HOLDER> modifiers;
	private final List<TYPE_REFERENCE> extendedInterfaces;
	
	public StackInterface(ParseLogger parseLogger, String interfaceKeyword, Context interfaceKeywordContext, String name, Context nameContext) {
		super(parseLogger);

		Objects.requireNonNull(interfaceKeyword);
		Objects.requireNonNull(interfaceKeywordContext);
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);

		this.interfaceKeyword = interfaceKeyword;
		this.interfaceKeywordContext = interfaceKeywordContext;
		this.name = name;
		this.nameContext = nameContext;
		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
		this.extendedInterfaces = new ArrayList<>();
	}
	
	public String getInterfaceKeyword() {
		return interfaceKeyword;
	}

	public Context getInterfaceKeywordContext() {
		return interfaceKeywordContext;
	}

	public String getName() {
		return name;
	}

	public Context getNameContext() {
		return nameContext;
	}

	public List<ANNOTATION> getAnnotations() {
        return annotations;
    }

    public List<INTERFACE_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	public void addModifier(INTERFACE_MODIFIER_HOLDER modifier) {
		modifiers.add(modifier);
	}

	@Override
	public void addMethod(INTERFACE_METHOD_MEMBER method) {
		add(method);
	}
	
	public List<TYPE_REFERENCE> getExtendedInterfaces() {
		return extendedInterfaces;
	}

	public void addExtendedInterface(TYPE_REFERENCE extendedInterface) {
		Objects.requireNonNull(extendedInterface);

		extendedInterfaces.add(extendedInterface);
	}
}
