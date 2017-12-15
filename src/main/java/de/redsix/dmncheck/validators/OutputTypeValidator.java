package de.redsix.dmncheck.validators;

import org.camunda.bpm.model.dmn.instance.Output;

public enum OutputTypeValidator implements ElementTypeDeclarationValidator<Output> {
    instance;

    @Override
    public String getTypeRef(Output output) {
        return output.getTypeRef();
    }

    @Override
    public Class<Output> getClassUnderValidation() {
        return Output.class;
    }
}