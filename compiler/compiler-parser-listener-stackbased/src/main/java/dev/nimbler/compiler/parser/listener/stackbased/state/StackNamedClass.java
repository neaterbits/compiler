package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ClassModifierSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.InterfaceSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.NamedGenericParametersSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackNamedClass<
		COMPLEX_MEMBER_DEFINITION,
		CLASS_FIELD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_MODIFIER_HOLDER,
        NAMED_GENERIC_TYPE_PARAMETERS,
		TYPE_REFERENCE>

	extends StackClass<COMPLEX_MEMBER_DEFINITION, CLASS_FIELD_MEMBER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER>
    implements ClassModifierSetter<CLASS_MODIFIER_HOLDER>, InterfaceSetter<TYPE_REFERENCE>, NamedGenericParametersSetter<NAMED_GENERIC_TYPE_PARAMETERS> {

	private final String classKeyword;
	private final Context classKeywordContext;
	private final String name;
	private final Context nameContext;
	private final List<CLASS_MODIFIER_HOLDER> modifiers;

	private List<NAMED_GENERIC_TYPE_PARAMETERS> genericTypes;

	private String extendsKeyword;
	private Context extendsKeywordContext;
	
	private final List<TYPE_REFERENCE> extendedClasses;
	private final List<TYPE_REFERENCE> implementedInterfaces;
	
	public StackNamedClass(ParseLogger parseLogger,
			String classKeyword, Context classKeywordContext,
			String name, Context nameContext) {
		super(parseLogger);

		Objects.requireNonNull(classKeyword);
		Objects.requireNonNull(classKeywordContext);
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);
		
		this.classKeyword = classKeyword;
		this.classKeywordContext = classKeywordContext;
		this.name = name;
		this.nameContext = nameContext;
		this.modifiers = new ArrayList<>();
		
		this.extendedClasses = new ArrayList<>();
		this.implementedInterfaces = new ArrayList<>();
	}
	
    @Override
	public void addClassModifier(CLASS_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifiers);

		modifiers.add(modifier);
	}
	
	public String getClassKeyword() {
		return classKeyword;
	}

	public Context getClassKeywordContext() {
		return classKeywordContext;
	}

	public String getName() {
		return name;
	}

	public Context getNameContext() {
		return nameContext;
	}

    public List<CLASS_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	public List<TYPE_REFERENCE> getExtendedClasses() {
		return extendedClasses;
	}

	public List<TYPE_REFERENCE> getImplementedInterfaces() {
		return implementedInterfaces;
	}
	
	public List<NAMED_GENERIC_TYPE_PARAMETERS> getGenericTypes() {
        return genericTypes;
    }

	@Override
    public void setGenericTypes(List<NAMED_GENERIC_TYPE_PARAMETERS> genericTypes) {
	    
	    Objects.requireNonNull(genericTypes);
	    
	    if (this.genericTypes != null) {
	        throw new IllegalStateException();
	    }
	    
        this.genericTypes = genericTypes;
    }

    public void setExtendsKeyword(String extendsKeyword, Context extendsKeywordContext) {
		
		Objects.requireNonNull(extendsKeyword);
		Objects.requireNonNull(extendsKeywordContext);
		
		if (this.extendsKeyword != null) {
			throw new IllegalStateException();
		}
		
		this.extendsKeyword = extendsKeyword;
		this.extendsKeywordContext = extendsKeywordContext;
	}
	
	public String getExtendsKeyword() {
		return extendsKeyword;
	}

	public Context getExtendsKeywordContext() {
		return extendsKeywordContext;
	}

	public void addExtendedClass(TYPE_REFERENCE extendedClass) {
		Objects.requireNonNull(extendedClass);

		extendedClasses.add(extendedClass);
	}

	@Override
	public void addImplementedInterface(String implementsKeyword, Context implementsKeywordContext, TYPE_REFERENCE implementedInterface) {
		Objects.requireNonNull(implementedInterface);

		implementedInterfaces.add(implementedInterface);
	}
}
