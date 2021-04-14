package dev.nimbler.ide.core.ui.controller;

import dev.nimbler.ide.common.ui.translation.Translateable;
import dev.nimbler.ide.common.ui.translation.TranslationNamespaces;
import dev.nimbler.ide.common.ui.translation.Translator;

public final class IDECoreTranslator implements Translator {

	@Override
	public String translate(Translateable translateable) {

		final String nameSpace = translateable.getTranslationNamespace();
		
		final String translation;
		
		if (nameSpace.equals(TranslationNamespaces.MENUES)) {
			switch (translateable.getTranslationId()) {
			case "file":
				translation = "&File";
				break;
				
			case "edit":
				translation = "&Edit";
				break;
				
			case "refactor":
				translation = "Refac&tor";
				break;
				
			case "navigate":
				translation = "&Navigate";
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
		}
		else if (nameSpace.equals(TranslationNamespaces.BUILTIN_ACTIONS)) {
			switch (translateable.getTranslationId()) {
			case "new_popup":
			case "new_dialog":
				translation = "New";
				break;
				
			case "delete":
				translation = "&Delete";
				break;

			case "undo":
				translation = "&Undo";
				break;

			case "redo":
				translation = "&Redo";
				break;
				
			case "cut":
				translation = "Cut";
				break;
				
			case "copy":
				translation = "Copy";
				break;
				
			case "paste":
				translation = "Paste";
				break;
				
			case "select_all":
				translation = "Select &All";
				break;
				
			case "find_replace":
				translation = "&Find/Replace";
				break;

			case "find_next":
				translation = "Find &Next";
				break;
				
			case "find_previous":
				translation = "Find Pre&vious";
				break;

			case "rename":
				translation = "Re&name";
				break;
				
			case "move":
				translation = "&Move";
				break;
				
			case "type_hierarchy":
				translation = "Type &hierarchy";
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
		}
		else if (nameSpace.equals(TranslationNamespaces.FIND_REPLACE)) {
			switch (translateable.getTranslationId()) {
			case "find_replace_title":
				translation = "Find/Replace";
				break;

			case "find_text_label":
				translation = "Find:";
				break;

			case "replace_with_text_label":
				translation = "Replace with:";
				break;

			case "direction":
				translation = "Direction";
				break;

			case "forward":
				translation = "Forward";
				break;

			case "backward":
				translation = "Backward";
				break;

			case "scope":
				translation = "Scope";
				break;

			case "all":
				translation = "All";
				break;

			case "selected_lines":
				translation = "Selected lines";
				break;

			case "options":
				translation = "Options";
				break;

			case "case_sensitive":
				translation = "Case sensitive";
				break;

			case "wrap_search":
				translation = "Wrap search";
				break;

			case "whole_word":
				translation = "Whole word";
				break;

			case "find_button":
				translation = "Find";
				break;

			case "replace_find_button":
				translation = "Replace/Find";
				break;

			case "replace_button":
				translation = "Replace";
				break;

			case "replace_all_button":
				translation = "Replace All";
				break;

			case "close_button":
				translation = "Close";
				break;

			default:
				throw new UnsupportedOperationException();
			}
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return translation;
	}
	
}
