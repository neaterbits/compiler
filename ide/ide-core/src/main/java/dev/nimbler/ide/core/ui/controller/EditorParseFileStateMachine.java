package dev.nimbler.ide.core.ui.controller;

import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.util.concurrency.statemachine.BaseState;
import com.neaterbits.util.concurrency.statemachine.StateMachine;

import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.core.source.SourceFileInfo;
import dev.nimbler.ide.core.source.SourceFilesModel;
import dev.nimbler.ide.util.ui.text.Text;

final class EditorParseFileStateMachine extends StateMachine<EditorParseFileStateMachine.BaseParseState> {

	@Override
	protected String getObjectDebugString() {
		return null;
	}

	EditorParseFileStateMachine(SourceFileInfo sourceFile, SourceFilesModel sourceFilesModel) {
		super(new IdleState(sourceFile, sourceFilesModel));
	}
	
	void tryParse(Text text, Consumer<SourceFileModel> listener) {
		schedule(state -> state.tryParse(text, listener));
	}
	
	static abstract class BaseParseState extends BaseState<BaseParseState> {

		private final SourceFileInfo sourceFile;
		private final SourceFilesModel sourceFilesModel;

		abstract BaseParseState tryParse(Text text, Consumer<SourceFileModel> listener);

		BaseParseState(SourceFileInfo sourceFile, SourceFilesModel sourceFilesModel) {
			this.sourceFile = sourceFile;
			this.sourceFilesModel = sourceFilesModel;
		}

		public BaseParseState(BaseParseState state) {
			this(state.sourceFile, state.sourceFilesModel);
		}

		BaseParseState onParseDone(SourceFileModel sourceFileModel) {
			throw new IllegalStateException();
		}
		
		final void scheduleParse(Text text) {
			
			sourceFilesModel.parseOnChange(sourceFile, text, updated -> {
				schedule(state -> state.onParseDone(updated));
			});
		}
	}
	
	static class IdleState extends BaseParseState {

		IdleState(BaseParseState state) {
			super(state);
		}

		public IdleState(SourceFileInfo sourceFile, SourceFilesModel sourceFilesModel) {
			super(sourceFile, sourceFilesModel);
			// TODO Auto-generated constructor stub
		}

		@Override
		BaseParseState tryParse(Text text, Consumer<SourceFileModel> listener) {
			
			scheduleParse(text);
			
			return new ParsingState(this, listener);
		}
	}
	
	static class ParsingState extends BaseParseState {
		
		private final Consumer<SourceFileModel> listener;
		
		ParsingState(BaseParseState prevState, Consumer<SourceFileModel> listener) {
			super(prevState);
			
			Objects.requireNonNull(listener);
			
			this.listener = listener;
		}

		@Override
		BaseParseState tryParse(Text text, Consumer<SourceFileModel> listener) {
			return new ParsingTextQueuedState(this, text, listener);
		}

		@Override
		BaseParseState onParseDone(SourceFileModel sourceFileModel) {
			
			listener.accept(sourceFileModel);
			
			return new IdleState(this);
		}
	}

	static class ParsingTextQueuedState extends BaseParseState {

		private final Text queuedText;
		private final Consumer<SourceFileModel> listener;

		ParsingTextQueuedState(BaseParseState prevState, Text queuedText, Consumer<SourceFileModel> listener) {

			super(prevState);
			
			Objects.requireNonNull(queuedText);
			Objects.requireNonNull(listener);
			
			this.queuedText = queuedText;
			this.listener = listener;
		}

		@Override
		BaseParseState tryParse(Text text, Consumer<SourceFileModel> listener) {
			return new ParsingTextQueuedState(this, text, listener);
		}

		@Override
		BaseParseState onParseDone(SourceFileModel sourceFileModel) {
	
			// Schedule queued without calling listener
			scheduleParse(queuedText);
			
			return new ParsingState(this, listener);
		}
	}
}
