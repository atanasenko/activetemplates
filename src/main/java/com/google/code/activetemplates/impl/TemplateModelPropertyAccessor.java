package com.google.code.activetemplates.impl;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

import com.google.code.activetemplates.TemplateModel;

public class TemplateModelPropertyAccessor implements PropertyAccessor{

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class<?>[]{ TemplateModel.class };
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return true;
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        TemplateModel b = (TemplateModel) target;
        return new TypedValue(b.get(name));
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return false;
    }

    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        throw new UnsupportedOperationException();
    }

}
