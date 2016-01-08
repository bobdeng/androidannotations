/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.androidannotations.roboguice.handler;

import static com.helger.jcodemodel.JExpr._new;
import static com.helger.jcodemodel.JExpr._super;
import static com.helger.jcodemodel.JExpr._this;
import static com.helger.jcodemodel.JExpr.invoke;
import static org.androidannotations.helper.ModelConstants.generationSuffix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.ElementValidation;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EActivityHolder;
import org.androidannotations.roboguice.annotations.RoboGuice;
import org.androidannotations.roboguice.api.RoboGuiceHelper;
import org.androidannotations.roboguice.helper.RoboGuiceClasses;
import org.androidannotations.roboguice.helper.RoboGuiceValidatorHelper;
import org.androidannotations.roboguice.holder.RoboGuiceHolder;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.IJExpression;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JSynchronizedBlock;
import com.helger.jcodemodel.JTryBlock;
import com.helger.jcodemodel.JVar;

public class RoboGuiceHandler extends BaseAnnotationHandler<EActivityHolder> {

	private final RoboGuiceValidatorHelper roboGuiceValidatorHelper;

	public RoboGuiceHandler(AndroidAnnotationsEnvironment environment) {
		super(RoboGuice.class, environment);
		roboGuiceValidatorHelper = new RoboGuiceValidatorHelper(annotationHelper);
	}

	@Override
	public void validate(Element element, ElementValidation validation) {
		validatorHelper.hasEActivity(element, validation);

		roboGuiceValidatorHelper.hasRoboGuiceJars(validation);
	}

	@Override
	public void process(Element element, EActivityHolder holder) {
		RoboGuiceHolder roboGuiceHolder = holder.getPluginHolder(new RoboGuiceHolder(holder));

		holder.getGeneratedClass()._implements(getJClass(RoboGuiceClasses.ROBO_CONTEXT));

		JFieldVar scope = roboGuiceHolder.getScopeField();
		JFieldVar scopedObjects = roboGuiceHolder.getScopedObjectsField();
		JFieldVar eventManager = roboGuiceHolder.getEventManagerField();
		roboGuiceHolder.getContentViewListenerField();
		listenerFields(element, holder);

		beforeCreateMethod(holder, scope, scopedObjects, eventManager);
		onRestartMethod(holder, eventManager);
		onStartMethod(holder, eventManager);
		onResumeMethod(holder, eventManager);
		onPauseMethod(holder, eventManager);
		onNewIntentMethod(holder, eventManager);
		onStopMethod(holder, eventManager);
		onDestroyMethod(holder, eventManager);
		onConfigurationChangedMethod(holder, roboGuiceHolder, eventManager);
		onContentChangedMethod(roboGuiceHolder, scope, eventManager);
		onActivityResultMethod(holder, eventManager);
		getScopedObjectMap(holder, scopedObjects);
	}

	private void listenerFields(Element element, EActivityHolder holder) {
		List<TypeMirror> listenerTypeMirrors = extractListenerTypeMirrors(element);
		int i = 1;
		for (TypeMirror listenerTypeMirror : listenerTypeMirrors) {
			AbstractJClass listenerClass = codeModelHelper.typeMirrorToJClass(listenerTypeMirror);
			JFieldVar listener = holder.getGeneratedClass().field(JMod.PRIVATE, listenerClass, "listener" + i + generationSuffix());
			codeModelHelper.addSuppressWarnings(listener, "unused");
			listener.annotate(getJClass(RoboGuiceClasses.INJECT));
			i++;
		}
	}

	private List<TypeMirror> extractListenerTypeMirrors(Element activityElement) {

		List<? extends AnnotationMirror> annotationMirrors = activityElement.getAnnotationMirrors();

		String annotationName = RoboGuice.class.getName();
		for (AnnotationMirror annotationMirror : annotationMirrors) {
			if (annotationName.equals(annotationMirror.getAnnotationType().toString())) {
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
					if ("value".equals(entry.getKey().getSimpleName().toString())) {
						AnnotationValue action = entry.getValue();
						@SuppressWarnings("unchecked")
						List<AnnotationValue> elements = (List<AnnotationValue>) action.getValue();
						List<TypeMirror> listenerTypeMirrors = new ArrayList<>(elements.size());

						for (AnnotationValue annotationValue : elements) {
							listenerTypeMirrors.add((TypeMirror) annotationValue.getValue());
						}

						return listenerTypeMirrors;
					}
				}
			}
		}
		return Collections.emptyList();
	}

	private void beforeCreateMethod(EActivityHolder holder, JFieldVar scope, JFieldVar scopedObjects, JFieldVar eventManager) {
		JBlock body = holder.getInitBody();
		AbstractJClass keyWildCard = getJClass(RoboGuiceClasses.KEY).narrow(getCodeModel().wildcard());
		AbstractJClass scopedHashMap = getClasses().HASH_MAP.narrow(keyWildCard, getClasses().OBJECT);
		body.assign(scopedObjects, JExpr._new(scopedHashMap));

		JVar injector = body.decl(getJClass(RoboGuiceClasses.ROBO_INJECTOR), "injector_", getJClass(RoboGuiceClasses.ROBO_GUICE).staticInvoke("getInjector").arg(_this()));
		body.assign(scope, invoke(injector, "getInstance").arg(getJClass(RoboGuiceClasses.CONTEXT_SCOPE).dotclass()));
		body.assign(eventManager, invoke(injector, "getInstance").arg(getJClass(RoboGuiceClasses.EVENT_MANAGER).dotclass()));
		body.add(injector.invoke("injectMembersWithoutViews").arg(_this()));
		fireEvent(eventManager, body, getJClass(RoboGuiceClasses.ON_CREATE_EVENT), holder.getInitSavedInstanceParam());
	}

	private void onRestartMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onRestartAfterSuperBlock = holder.getOnRestartAfterSuperBlock();
		fireEvent(eventManager, onRestartAfterSuperBlock, getJClass(RoboGuiceClasses.ON_RESTART_EVENT));
	}

	private void onStartMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onStartAfterSuperBlock = holder.getOnStartAfterSuperBlock();
		fireEvent(eventManager, onStartAfterSuperBlock, getJClass(RoboGuiceClasses.ON_START_EVENT));
	}

	private void onResumeMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onResumeAfterSuperBlock = holder.getOnResumeAfterSuperBlock();
		fireEvent(eventManager, onResumeAfterSuperBlock, getJClass(RoboGuiceClasses.ON_RESUME_EVENT));
	}

	private void onPauseMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onPauseAfterSuperBlock = holder.getOnPauseAfterSuperBlock();
		fireEvent(eventManager, onPauseAfterSuperBlock, getJClass(RoboGuiceClasses.ON_PAUSE_EVENT));
	}

	private void onNewIntentMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onNewIntentAfterSuperBlock = holder.getOnNewIntentAfterSuperBlock();
		fireEvent(eventManager, onNewIntentAfterSuperBlock, getJClass(RoboGuiceClasses.ON_NEW_INTENT_EVENT));
	}

	private void onStopMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onStopBlock = new JBlock().bracesRequired(false).indentRequired(false);

		JTryBlock tryBlock = onStopBlock._try();
		fireEvent(eventManager, tryBlock.body(), getJClass(RoboGuiceClasses.ON_STOP_EVENT));
		JBlock finallyBody = tryBlock._finally();

		finallyBody.invoke(_super(), "onStop");

		JMethod onStop = holder.getOnStop();
		codeModelHelper.replaceSuperCall(onStop, onStopBlock);
	}

	private void onDestroyMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onDestroyBlock = new JBlock().bracesRequired(false).indentRequired(false);

		JTryBlock tryBlock = onDestroyBlock._try();
		fireEvent(eventManager, tryBlock.body(), getJClass(RoboGuiceClasses.ON_DESTROY_EVENT));
		JBlock finallyBody = tryBlock._finally();

		JTryBlock tryInFinally = finallyBody._try();
		tryInFinally.body().add(getJClass(RoboGuiceClasses.ROBO_GUICE).staticInvoke("destroyInjector").arg(_this()));
		tryInFinally._finally().invoke(_super(), "onDestroy");

		JMethod onDestroy = holder.getOnDestroy();
		codeModelHelper.replaceSuperCall(onDestroy, onDestroyBlock);
	}

	private void onConfigurationChangedMethod(EActivityHolder holder, RoboGuiceHolder roboGuiceHolder, JFieldVar eventManager) {
		JVar currentConfig = roboGuiceHolder.getCurrentConfig();
		IJExpression newConfig = holder.getOnConfigurationChangedNewConfigParam();
		JBlock onConfigurationChangedAfterSuperBlock = holder.getOnConfigurationChangedAfterSuperBlock();
		fireEvent(eventManager, onConfigurationChangedAfterSuperBlock, getJClass(RoboGuiceClasses.ON_CONFIGURATION_CHANGED_EVENT), currentConfig, newConfig);
	}

	private void onContentChangedMethod(RoboGuiceHolder holder, JFieldVar scope, JFieldVar eventManager) {
		JBlock onContentChangedAfterSuperBlock = holder.getOnContentChangedAfterSuperBlock();

		JSynchronizedBlock synchronizedBlock = onContentChangedAfterSuperBlock.synchronizedBlock(getJClass(RoboGuiceClasses.CONTEXT_SCOPE).dotclass());
		JBlock synchronizedBlockBody = synchronizedBlock.body();
		synchronizedBlockBody.invoke(scope, "enter").arg(_this());
		JTryBlock tryBlock = synchronizedBlockBody._try();
		tryBlock.body().staticInvoke(getJClass(RoboGuiceHelper.class), "callInjectViews").arg(_this());
		tryBlock._finally().invoke(scope, "exit").arg(_this());
		onContentChangedAfterSuperBlock.add(synchronizedBlock);

		fireEvent(eventManager, onContentChangedAfterSuperBlock, getJClass(RoboGuiceClasses.ON_CONTENT_CHANGED_EVENT));
	}

	private void onActivityResultMethod(EActivityHolder holder, JFieldVar eventManager) {
		JBlock onActivityResultAfterSuperBlock = holder.getOnActivityResultAfterSuperBlock();
		JVar requestCode = holder.getOnActivityResultRequestCodeParam();
		JVar resultCode = holder.getOnActivityResultResultCodeParam();
		JVar data = holder.getOnActivityResultDataParam();

		fireEvent(eventManager, onActivityResultAfterSuperBlock, getJClass(RoboGuiceClasses.ON_ACTIVITY_RESULT_EVENT), requestCode, resultCode, data);
	}

	private void fireEvent(JFieldVar eventManager, JBlock body, AbstractJClass eventClass, IJExpression... eventArguments) {
		AbstractJClass actualEventClass = eventClass;
		if (eventClass.fullName().startsWith("roboguice.context.event")) {
			actualEventClass = eventClass.narrow(getClasses().ACTIVITY);
		}

		JInvocation newEvent = _new(actualEventClass);
		newEvent.arg(_this());
		for (IJExpression eventArgument : eventArguments) {
			newEvent.arg(eventArgument);
		}
		body.invoke(eventManager, "fire").arg(newEvent);
	}

	private void getScopedObjectMap(EActivityHolder holder, JFieldVar scopedObjectMap) {
		JMethod getScopedObjectMapMethod = holder.getGeneratedClass().method(JMod.PUBLIC, scopedObjectMap.type(), "getScopedObjectMap");
		getScopedObjectMapMethod.annotate(Override.class);
		getScopedObjectMapMethod.body()._return(scopedObjectMap);
	}
}
